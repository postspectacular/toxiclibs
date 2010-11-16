/**
 * <p>BoxFLuid demo combining 3D physics particles with the IsoSurface class to
 * create an animated mesh with a fluid behaviour. The mesh is optionally created
 * within a boundary sphere, but other forms can be created using a custom
 * ParticleConstraint class.</p>
 * 
 * <p>Dependencies:</p>
 * <ul>
 * <li>toxiclibscore-0015 or newer package from <a href="http://toxiclibs.org">toxiclibs.org</a></li>
 * <li>verletphysics-0004 or newer package from <a href="http://toxiclibs.org">toxiclibs.org</a></li>
 * <li>volumeutils-0002 or newer package from <a href="http://toxiclibs.org">toxiclibs.org</a></li>
 * <li>controlP5 GUI library from <a href="http://sojamo.de">sojamo.de</a></li>
 * </ul>
 * 
 * <p>Key controls:</p>
 * <ul>
 * <li>w : wireframe on/off</li>
 * <li>c : close sides on/off</li>
 * <li>p : show particles only on/off</li>
 * <li>b : turn bounding sphere on/off</li>
 * <li>r : reset particles</li>
 * <li>s : save current mesh as OBJ & STL format</li>
 * <li>- / = : decrease/increase surface threshold/tightness</li>
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

import processing.opengl.*;

import toxi.physics.*;
import toxi.physics.behaviors.*;
import toxi.physics.constraints.*;
import toxi.geom.*;
import toxi.geom.mesh.*;
import toxi.math.*;
import toxi.volume.*;

import controlP5.*;

int NUM_PARTICLES = 200;
float REST_LENGTH=375;
int DIM=200;

int GRID=18;
float VS=2*DIM/GRID;
Vec3D SCALE=new Vec3D(DIM,DIM,DIM).scale(2);
float isoThreshold=3;

int numP;
VerletPhysics physics;
ParticleConstraint boundingSphere;
GravityBehavior gravity;

VolumetricSpaceArray volume;
IsoSurface surface;

TriangleMesh mesh=new TriangleMesh("fluid");

boolean showPhysics=false;
boolean isWireFrame=false;
boolean isClosed=true;
boolean useBoundary=false;

Vec3D colAmp=new Vec3D(400, 200, 200);

void setup() {
  size(1280,720,OPENGL);
  smooth();
  initPhysics();
  initGUI();
  volume=new VolumetricSpaceArray(SCALE,GRID,GRID,GRID);
  surface=new ArrayIsoSurface(volume);
  textFont(createFont("SansSerif",12));
}

void draw() {
  updateParticles();
  computeVolume();
  background(224);
  pushMatrix();
  translate(width/2,height*0.5,0);
  rotateX(mouseY*0.01);
  rotateY(mouseX*0.01);
  noFill();
  stroke(255,192);
  strokeWeight(1);
  box(physics.getWorldBounds().getExtent().x*2);
  if (showPhysics) {
    strokeWeight(4);
    stroke(0);
    for(Iterator i=physics.particles.iterator(); i.hasNext();) {
      VerletParticle p=(VerletParticle)i.next();
      Vec3D col=p.add(colAmp).scaleSelf(0.5);
      stroke(col.x,col.y,col.z);
      point(p.x,p.y,p.z);
    }
  } 
  else {
    ambientLight(216, 216, 216);
    directionalLight(255, 255, 255, 0, 1, 0);
    directionalLight(96, 96, 96, 1, 1, -1);
    if (isWireFrame) {
      stroke(255);
      noFill();
    } 
    else {
      noStroke();
      fill(224,0,51);
    }
    beginShape(TRIANGLES);
    if (!isWireFrame) {
      drawFilledMesh();
    } 
    else {
      drawWireMesh();
    }
    endShape();
  }
  popMatrix();
  noLights();
  fill(0);
  text("faces: "+mesh.getNumFaces(),20,600);
  text("vertices: "+mesh.getNumVertices(),20,615);
  text("particles: "+physics.particles.size(),20,630);
  text("springs: "+physics.springs.size(),20,645);
  text("fps: "+frameRate,20,690);
}

void keyPressed() {
  if (key=='r') initPhysics();
  if (key=='w') isWireFrame=!isWireFrame;
  if (key=='p') showPhysics=!showPhysics;
  if (key=='c') isClosed=!isClosed;
  if (key=='b') {
    toggleBoundary();
  }
  if (key=='-' || key=='_') {
    isoThreshold-=0.001;
  }
  if (key=='=' || key=='+') {
    isoThreshold+=0.001;
  }
  if (key=='s') {
    mesh.saveAsOBJ(sketchPath(mesh.name+".obj"));
    mesh.saveAsSTL(sketchPath(mesh.name+".stl"));
  }
}

void toggleBoundary() {
  useBoundary=!useBoundary;
  initPhysics();
}

