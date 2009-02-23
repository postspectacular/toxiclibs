/**
 * MRISurface demo showing how to utilize the IsoSurface class to efficiently
 * visualise volumetric data, in this case using MRI scan data obtained from the
 * volvis.org repository of free datasets.
 * The demo also shows how to save the generated mesh as binary STL file (or
 * alternatively in OBJ format) for later use in other 3D tools/digital fabrication.
 * 
 * I've planned further classes for the toxi.geom.volume package to easier draw
 * and manipulate volumetric data.
 * 
 * Important note: This demo is fairly memory hungry, so if you receive OutOfMemory
 * errors, please make sure to give Processing as much RAM as possible (see preferences)
 * and if this still fails, reduce the surface resolution from DIM=128 to 64 or 32...
 *
 * Controls:
 * Click mouse button to toggle rendering style between shaded/wireframe.
 * Press 's' to save mesh as STL file
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

int DIM=128;
float ISO_THRESHOLD = 0.2;
Vec3D SCALE=new Vec3D(DIM,DIM,DIM).scaleSelf(8);

IsoSurface surface;

boolean isWireframe=false;

void setup() {
  size(1024,768,OPENGL);
  hint(ENABLE_OPENGL_4X_SMOOTH);
  strokeWeight(0.5);
  // convert MRI scan data into floats
  // MRI data is 256 x 256 x 256 voxels @ 8bit/voxel
  byte[] mriData=loadBytes("aneurism.raw.gz");
  // scale factor to normalize 8bit to the 0.0 - 1.0 interval
  float mriNormalize=1/255.0;
  // setup lower resolution grid for IsoSurface
  VolumetricSpace volume=new VolumetricSpace(SCALE,DIM,DIM,DIM);
  float[] cloud=volume.getData();
  int stride=256/DIM;
  for(int z=0,idx=0; z<256; z+=stride) {
    for(int y=0; y<256; y+=stride) {
      int sliceIdx=y*256+z*65536;
      for(int x=0; x<256; x+=stride) {
        byte b=mriData[x+sliceIdx];
        cloud[idx++]=(int)(b<0 ? 256+b : b)*mriNormalize;
        ;
      }
    }
  }
  long t0=System.nanoTime();
  // store in IsoSurface and compute surface mesh for the given threshold value
  surface=new IsoSurface(volume);
  surface.computeSurface(ISO_THRESHOLD);
  float timeTaken=(System.nanoTime()-t0)*1e-6;
  println(timeTaken+"ms to compute "+surface.getNumFaces()+" faces");
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
  for(int i=0; i<surface.getNumFaces(); i++) {
    verts=surface.getVerticesForFace(i,verts);
    vertex(verts[0].x,verts[0].y,verts[0].z);
    vertex(verts[1].x,verts[1].y,verts[1].z);
    vertex(verts[2].x,verts[2].y,verts[2].z);
  }
  endShape();
}

void mousePressed() {
  isWireframe=!isWireframe;
}

void keyPressed() {
  if (key=='s') {
    // save mesh as STL or OBJ file
    surface.saveAsSTL(sketchPath("noise.stl"));
  }
}

