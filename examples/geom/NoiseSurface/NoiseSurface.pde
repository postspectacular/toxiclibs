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
 */

/* 
 * Copyright (c) 2009 Karsten Schmidt
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
import toxi.geom.volume.*;
import toxi.math.noise.*;

import processing.opengl.*;

int DIM=64;
float ISO_THRESHOLD = 0.1;
float NS=0.03;
float VOXEL_SCALE=DIM*8;

IsoSurface surface;

boolean isWireframe=false;

void setup() {
  size(1024,768,OPENGL);
  hint(ENABLE_OPENGL_4X_SMOOTH);
  strokeWeight(0.5);
  // fill volume with noise
  float[] cloud=new float[DIM*DIM*DIM];
  for(int z=0,index=0; z<DIM; z++) {
    for(int y=0; y<DIM; y++) {
      for(int x=0; x<DIM; x++) {
        cloud[index++]=(float)SimplexNoise.noise(x*NS,y*NS,z*NS)*0.5;
      } 
    } 
  }
  long t0=System.nanoTime();
  // store in IsoSurface and compute surface mesh for the given threshold value
  surface=new IsoSurface(DIM,ISO_THRESHOLD,cloud);
  surface.computeSurface();
  float timeTaken=(System.nanoTime()-t0)*1e-6;
  println(timeTaken+"ms to compute "+surface.numFaces+" faces");
  // save mesh as STL or OBJ file
  surface.saveAsSTL(sketchPath("noise.stl"));
  //surface.saveAsOBJ(sketchPath("noise.obj"));
}

void draw() {
  background(128);
  translate(width/2,height/2,0);
  rotateX(mouseY*0.01);
  rotateY(mouseX*0.01);
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
  // draw all faces of the computed mesh 
  Vec3D[] verts=null;
  for(int i=0; i<surface.numFaces; i++) {
    verts=surface.getVerticesForFace(i,verts);
    vertex(verts[0].x*VOXEL_SCALE,verts[0].y*VOXEL_SCALE,verts[0].z*VOXEL_SCALE);
    vertex(verts[1].x*VOXEL_SCALE,verts[1].y*VOXEL_SCALE,verts[1].z*VOXEL_SCALE);
    vertex(verts[2].x*VOXEL_SCALE,verts[2].y*VOXEL_SCALE,verts[2].z*VOXEL_SCALE);
  }
  endShape();
}

void mousePressed() {
  isWireframe=!isWireframe;
}
