/**
 * <p>NoiseSurface demo showing how to utilize the IsoSurface class to efficiently
 * visualise volumetric data, in this case using 3D SimplexNoise. The demo also
 * shows how to save the generated mesh as binary STL file (or alternatively in
 * OBJ format) for later use in other 3D tools/digital fabrication.</p>
 * 
 * <p>Further classes for the toxi.volume package are planned to easier draw
 * and manipulate volumetric data.</p>
 * 
 * <p>Key controls:</p>
 * <ul>
 * <li>w : toggle rendering style between shaded/wireframe</li>
 * <li>s : save model as STL & quit</li>
 * <li>1-9 : adjust brush density</li>
 * <li>a-z : adjust density threshold for calculating surface</li>
 * <li>-/= : adjust zoom</li>
 * </ul>
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

import processing.opengl.*;

int DIMX=96;
int DIMY=96;
int DIMZ=128;

float ISO_THRESHOLD = 0.1;
float NS=0.03;
Vec3D SCALE=new Vec3D(256,256,384);

VolumetricSpace volume;
VolumetricBrush brush;
IsoSurface surface;
TriangleMesh mesh;

AbstractWave brushSize;

boolean isWireframe=false;
boolean doSave=false;

float currScale=1;
float density=0.5;
float spin=8;
float currZ=0;
float Z_STEP=0.005;

ToxiclibsSupport gfx;

void setup() {
  size(1024,768,OPENGL);
  hint(ENABLE_OPENGL_4X_SMOOTH);
  gfx=new ToxiclibsSupport(this);
  strokeWeight(0.5);
  volume=new VolumetricSpaceArray(SCALE,DIMX,DIMY,DIMZ);
  brush=new RoundBrush(volume,SCALE.x/2);
  brushSize=new SineWave(-HALF_PI,2*TWO_PI*Z_STEP,SCALE.x*0.03,SCALE.x*0.06);
  surface=new ArrayIsoSurface(volume);
  mesh=new TriangleMesh();
}

void draw() {
  brush.setSize(brushSize.update());
  float offsetZ=-SCALE.z+currZ*SCALE.z*2.6666;
  float currRadius=SCALE.x*0.4*sin(currZ*PI);
  for(float t=0; t<TWO_PI; t+=TWO_PI/6) {
    brush.drawAtAbsolutePos(new Vec3D(currRadius*cos(t+currZ*spin),currRadius*sin(t+currZ*spin),offsetZ),density);
    brush.drawAtAbsolutePos(new Vec3D(currRadius*cos(t-currZ*spin),currRadius*sin(t-currZ*spin),offsetZ),density);
  }
  currZ+=Z_STEP;
  volume.closeSides();
  surface.reset();
  surface.computeSurfaceMesh(mesh,ISO_THRESHOLD);
  if (doSave) {
    // save mesh as STL or OBJ file
    mesh.saveAsSTL(sketchPath("cup"+(System.currentTimeMillis()/1000)+".stl"));
    doSave=false;
    System.exit(1);
  }
  background(128);
  translate(width/2,height/2,0);
  lightSpecular(230,230,230);
  directionalLight(255,255,255,1,1,-1);
  shininess(1.0);
  rotateX(-0.4);
  rotateY(frameCount*0.05);
  scale(currScale);
  noStroke();
  gfx.mesh(mesh);
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

