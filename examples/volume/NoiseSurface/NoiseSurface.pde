/**
 * NoiseSurface demo showing how to utilize the IsoSurface class to efficiently
 * visualise volumetric data, in this case using 3D SimplexNoise. The demo also
 * shows how to save the generated mesh as binary STL file (or alternatively in
 * OBJ format) for later use in other 3D tools/digital fabrication.
 * 
 * I've planned further classes for the toxi.geom.volume package to easier draw
 * and manipulate volumetric data.
 * 
 * Controls:
 * Click mouse button to toggle rendering style between shaded/wireframe.
 * Press 's' to save generated mesh as STL file
 */

/* 
 * Copyright (c) 2010 Karsten Schmidt
 * 
 * This demo & library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * http://creativecommons.org/licenses/LGPL/2.1/
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
import toxi.geom.*;
import toxi.geom.mesh.*;
import toxi.volume.*;
import toxi.math.noise.*;
import toxi.processing.*;

import processing.opengl.*;

int DIMX=192;
int DIMY=32;
int DIMZ=64;

float ISO_THRESHOLD = 0.1;
float NS=0.03;
Vec3D SCALE=new Vec3D(3,0.5,1).scaleSelf(300);

IsoSurface surface;
TriangleMesh mesh;

boolean isWireframe=false;
float currScale=1;

ToxiclibsSupport gfx;

void setup() {
  size(1024,768,OPENGL);
  hint(ENABLE_OPENGL_4X_SMOOTH);
  gfx=new ToxiclibsSupport(this);
  strokeWeight(0.5);
  VolumetricSpace volume=new VolumetricSpaceArray(SCALE,DIMX,DIMY,DIMZ);
  // fill volume with noise
  for(int z=0; z<DIMZ; z++) {
    for(int y=0; y<DIMY; y++) {
      for(int x=0; x<DIMX; x++) {
        volume.setVoxelAt(x,y,z,(float)SimplexNoise.noise(x*NS,y*NS,z*NS)*0.5);
      } 
    } 
  }
  volume.closeSides();
  long t0=System.nanoTime();
  // store in IsoSurface and compute surface mesh for the given threshold value
  mesh=new TriangleMesh("iso");
  surface=new HashIsoSurface(volume,0.333333);
  surface.computeSurfaceMesh(mesh,ISO_THRESHOLD);
  float timeTaken=(System.nanoTime()-t0)*1e-6;
  println(timeTaken+"ms to compute "+mesh.getNumFaces()+" faces");
}

void draw() {
  background(128);
  translate(width/2,height/2,0);
  rotateX(mouseY*0.01);
  rotateY(mouseX*0.01);
  scale(currScale);
  ambientLight(48,48,48);
  lightSpecular(230,230,230);
  directionalLight(255,255,255,0,-0.5,-1);
  specular(255,255,255);
  shininess(16.0);
  beginShape(TRIANGLES);
  if (isWireframe) {
    stroke(255);
    noFill();
  } 
  else {
    noStroke();
    fill(255);
  }
  gfx.mesh(mesh);
}

void mousePressed() {
  isWireframe=!isWireframe;
}
void keyPressed() {
  if(key=='-') currScale=max(currScale-0.1,0.5);
  if(key=='=') currScale=min(currScale+0.1,10);
  if (key=='s') {
    // save mesh as STL or OBJ file
    mesh.saveAsSTL(sketchPath("noise.stl"));
  }
}
