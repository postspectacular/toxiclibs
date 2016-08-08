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
import toxi.math.noise.*;
import toxi.processing.*;



int DIM=128;
float ISO_THRESHOLD = 0.1;
Vec3D SCALE=new Vec3D(DIM,DIM,DIM).scaleSelf(8);

IsoSurface surface;
Mesh3D mesh;
ToxiclibsSupport gfx;

boolean isWireframe=false;

void setup() {
  size(1024,768,P3D);
  
  gfx=new ToxiclibsSupport(this);
  strokeWeight(0.5);
  // convert MRI scan data into floats
  // MRI data is 256 x 256 x 256 voxels @ 8bit/voxel
  byte[] mriData=loadBytes("aneurism.raw.gz");
  // scale factor to normalize 8bit to the 0.0 - 1.0 interval
  float mriNormalize=1/255.0;
  // setup lower resolution grid for IsoSurface
  VolumetricSpaceArray volume=new VolumetricSpaceArray(SCALE,DIM,DIM,DIM);
  float[] cloud=volume.getData();
  int stride=256/DIM;
  for(int z=0,idx=0; z<256; z+=stride) {
    for(int y=0; y<256; y+=stride) {
      int sliceIdx=y*256+z*65536;
      for(int x=0; x<256; x+=stride) {
        byte b=mriData[x+sliceIdx];
        cloud[idx++]=(int)(b<0 ? 256+b : b)*mriNormalize;
      }
    }
  }
  long t0=System.nanoTime();
  // create IsoSurface and compute surface mesh for the given iso threshold value
  surface=new HashIsoSurface(volume,0.15);
  mesh=surface.computeSurfaceMesh(null,ISO_THRESHOLD);
  float timeTaken=(System.nanoTime()-t0)*1e-6;
  println(timeTaken+"ms to compute "+mesh.getNumFaces()+" faces");
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

void normal(Vec3D v) {
  normal(v.x,v.y,v.z);
}

void vertex(Vec3D v) {
  vertex(v.x,v.y,v.z);
}

void mousePressed() {
  isWireframe=!isWireframe;
}
