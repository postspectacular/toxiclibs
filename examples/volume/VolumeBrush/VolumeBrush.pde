/**
 * VolumeBrush demo showing how to utilize the IsoSurface class to efficiently
 * visualise volumetric data. The demo also shows how to save the generated mesh
 * as binary STL file (or alternatively in OBJ format) for later use in other
 * 3D tools/digital fabrication.
 * 
 * Controls:
 * Click mouse button to manipulate the volume at the curso position
 * (this analogous to drawing or erasing).
 * Press 1-9 to set density between -0.5 and +0.5 (negative density will erode volume)
 * Press 'w' to toggle rendering style between shaded/wireframe.
 * Press 's' to save mesh as STL file
 * Press a-z to adjust surface threshold value to see the surface at different densities
 * Press - / = to zoom in/out
 * 
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
import toxi.geom.mesh.*;
import toxi.volume.*;
import toxi.math.waves.*;
import toxi.processing.*;



int DIMX=64;
int DIMY=64;
int DIMZ=64;

float ISO_THRESHOLD = 0.1;
Vec3D SCALE=new Vec3D(1,1,1).scaleSelf(100);

VolumetricSpace volume;
VolumetricBrush brush;
IsoSurface surface;
TriangleMesh mesh;

AbstractWave brushSize;

boolean isWireframe=false;
boolean doSave=false;

float currScale=4;
float density=0.5;

ToxiclibsSupport gfx;

void setup() {
  size(1024,768,P3D);
  smooth(4);
  gfx=new ToxiclibsSupport(this);
  strokeWeight(0.5);
  volume=new VolumetricSpaceArray(SCALE,DIMX,DIMY,DIMZ);
  brush=new RoundBrush(volume,SCALE.x/2);
  brushSize=new SineWave(0,0.1,SCALE.x*0.07,SCALE.x*0.1);
  surface=new ArrayIsoSurface(volume);
  mesh=new TriangleMesh();
}

void draw() {
  brush.setSize(brushSize.update());
  Vec3D mousePos=new Vec3D((mouseX-width/2)*0.5,(mouseY-height/2)*0.5,sin(frameCount*0.05)*SCALE.z*0.5);
  if (mousePressed) {
    brush.drawAtAbsolutePos(mousePos,density);
    volume.closeSides();  
    surface.reset();
    surface.computeSurfaceMesh(mesh,ISO_THRESHOLD);
    if (doSave) {
      // save mesh as STL or OBJ file
      mesh.saveAsSTL(sketchPath("scribble"+(System.currentTimeMillis()/1000)+".stl"));
      doSave=false;
    }
  }
  background(128);
  translate(width/2,height/2,0);
  lightSpecular(230,230,230);
  directionalLight(255,255,255,1,1,-1);
  shininess(1.0);
  rotateX(-0.4);
  rotateY(frameCount*0.05);
  scale(currScale);
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
  noFill();
  stroke(255,0,0);
  gfx.sphere(new Sphere(mousePos,brushSize.value),20);
}

void keyPressed() {
  if(key=='-') currScale=max(currScale-0.1,0.5);
  if(key=='=') currScale=min(currScale+0.1,10);
  if(key=='w') { 
    isWireframe=!isWireframe; 
    return;
  }
  if(key=='s') { 
    doSave=true; 
    return;
  }
  if (key>='1' && key<='9') {
    density=-0.5+(key-'1')*0.1;
    println(density);
  }
  if (key=='0') density=0.5;
  if (key>='a' && key<='z') ISO_THRESHOLD=(key-'a')*0.019+0.01;
}
