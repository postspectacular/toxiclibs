/**
 * GlobeVolume demo showing how to utilize the IsoSurface class to efficiently
 * visualise volumetric data, in this case using the Volumetric brush. The demo also
 * shows how to save the generated mesh as binary STL file (or alternatively in
 * OBJ format) for later use in other 3D tools/digital fabrication.
 * 
 * Controls:
 * Press 'w' to toggle rendering style between shaded/wireframe.
 * Press 's' to save mesh as STL file
 * Press 1-9 to adjust surface threshold value to see the surface at different densities
 * Press - / = to zoom in/out
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
import toxi.math.waves.*;

import processing.opengl.*;

int DIMX=96;
int DIMY=96;
int DIMZ=96;

float ISO_THRESHOLD = 0.15;
Vec3D SCALE=new Vec3D(1,1,1).scaleSelf(200);

VolumetricSpace volume;
VolumetricBrush brush;
IsoSurface surface;

boolean isWireframe=false;
boolean doSave=false;

float currScale=4;
float density=0.5;

void setup() {
  size(1024,768,OPENGL);
  hint(ENABLE_OPENGL_4X_SMOOTH);
  strokeWeight(0.5);
  volume=new VolumetricSpace(SCALE,DIMX,DIMY,DIMZ);
  brush=new RoundBrush(volume,SCALE.x/2);
  brush.drawAtAbsolutePos(new Vec3D(),0.5);
  for(int i=0; i<100; i++) {
    brush.setSize(random(0.05,0.15)*SCALE.x);
    brush.drawAtAbsolutePos(Vec3D.randomVector().scaleSelf(DIMX,DIMY,DIMZ).scaleSelf(random(-1,1)),-0.5);
  } 
  for(int i=0; i<1000; i++) {
    brush.setSize(random(0.02,0.05)*SCALE.x);
    brush.drawAtAbsolutePos(Vec3D.randomVector().scaleSelf(DIMX,DIMY,DIMZ).scaleSelf(random(-0.5,0.5)),-0.5);
  } 
  surface=new IsoSurface(volume);
  volume.closeSides();  
  surface.reset();
  surface.computeSurface(ISO_THRESHOLD);

}

void draw() {
  if (doSave) {
    // save mesh as STL or OBJ file
    surface.saveAsSTL(sketchPath("scribble"+(System.currentTimeMillis()/1000)+".stl"));
    doSave=false;
  }
  background(128);
  translate(width/2,height/2,0);
  ambientLight(48,48,48);
  lightSpecular(230,230,230);
  directionalLight(255,255,255,1,1,-1);
  specular(255,255,255);
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
  // draw all faces of the computed mesh
  int num=surface.getNumFaces(); 
  Vec3D[] verts=null;
  for(int i=0; i<num; i++) {
    verts=surface.getVerticesForFace(i,verts);
    vertex(verts[2].x,verts[2].y,verts[2].z);
    vertex(verts[1].x,verts[1].y,verts[1].z);
    vertex(verts[0].x,verts[0].y,verts[0].z);
  }
  endShape();
}

void keyPressed() {
  if(key=='-') currScale=max(currScale-0.1,0.5);
  if(key=='=') currScale=min(currScale+0.1,20);
  if(key=='w') { 
    isWireframe=!isWireframe; 
    return; 
  }
  if(key=='s') { 
    doSave=true; 
    return; 
  }
  if (key>='1' && key<='9') {
    ISO_THRESHOLD=(key-'1')*0.025+0.01;
    surface.reset();
    surface.computeSurface(ISO_THRESHOLD);
  }
}



