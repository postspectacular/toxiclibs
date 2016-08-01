/**
 * PointOctree demo showing how to use the structure to efficiently retrieve
 * and isolate particle like objects within a defined neighbourhood from a much
 * larger set of objects.
 * 
 * Key controls:
 * 
 * SPACE : add 100 more particles
 * S : choose between sphere and bounding box culling.
 * O : toggle display of the octree structure
 * - / + : adjust the size of the culling radius
 */

/* 
 * Copyright (c) 2006-2008 Karsten Schmidt
 * 
 * This library is free software; you can redistribute it and/or
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
import toxi.processing.*;
import java.util.List;

// octree dimensions
float DIM = 100;
float DIM2 = DIM/2;

// sphere clip radius
float RADIUS = 20;

// number of particles to add at once
int NUM = 100;

// show octree debug info
boolean showOctree = true;

// use clip sphere or axis aligned bounding box
boolean useSphere = false;

// view rotation
float xrot = THIRD_PI;
float zrot = 0.1;

VisibleOctree octree;
Vec3D cursor = new Vec3D();

ToxiclibsSupport gfx;

// start with one particle
int numParticles = 1;

void setup() {
  size(1024, 768, P3D);
  textFont(createFont("SansSerif", 18));
  // setup empty octree so that it's centered around the world origin
  octree=new VisibleOctree(new Vec3D(-1, -1, -1).scaleSelf(DIM2), DIM);
  octree.setMinNodeSize(4);
  // add an initial particle at the origin
  octree.addPoint(new Vec3D());
  gfx=new ToxiclibsSupport(this);
}

void draw() {
  background(255);
  pushMatrix();
  lights();
  translate(width/2, height/2, 0);
  // rotate view on mouse drag
  if (mousePressed) {
    xrot+=(mouseY*0.01-xrot)*0.1;
    zrot+=(mouseX*0.01-zrot)*0.1;
  } 
  // or move cursor
  else {
    cursor.x=-(width*0.5-mouseX)/(width/2)*DIM2;
    cursor.y=-(height*0.5-mouseY)/(height/2)*DIM2;
  }
  rotateX(xrot);
  rotateZ(zrot);
  scale(4);
  // show debug view of tree
  if (showOctree) octree.draw();
  // show crosshair 3D cursor
  stroke(255, 0, 0);
  noFill();
  beginShape(LINES);
  vertex(cursor.x, -DIM2, 0);
  vertex(cursor.x, DIM2, 0);
  vertex(-DIM2, cursor.y, 0);
  vertex(DIM2, cursor.y, 0);
  endShape();
  // show particles within the specific clip radius
  noStroke();
  long t0=System.nanoTime();
  List<Vec3D> points=null;
  if (useSphere) {
    points=octree.getPointsWithinSphere(cursor, RADIUS);
  } 
  else {
    points=octree.getPointsWithinBox(new AABB(cursor, new Vec3D(RADIUS, RADIUS, RADIUS)));
  }
  float dt=(float)((System.nanoTime()-t0)*1e-6);
  int numClipped=0;
  if (points!=null) {
    numClipped=points.size();
    for (Vec3D p : points) {
      fill(abs(p.x)*8, abs(p.y)*8, abs(p.z)*8);
      gfx.box(new AABB(p, 1));
    }
  }
  // show clipping sphere
  fill(0, 30);
  translate(cursor.x, cursor.y, 0);
  sphere(RADIUS);
  popMatrix();
  fill(0);
  text("total: "+numParticles, 10, 30);
  text("clipped: "+numClipped+" (time: "+nf(dt, 1, 4)+"ms)", 10, 50);
}

void keyPressed() {
  if (key==' ') {
    // add NUM new particles within a sphere of radius DIM2
    for (int i=0; i<NUM; i++) octree.addPoint(Vec3D.randomVector().scaleSelf(random(DIM2)));
    numParticles+=NUM;
  } 
  else if (key=='s') {
    useSphere=!useSphere;
  } 
  else if (key=='o') {
    showOctree=!showOctree;
  }
  else if (key=='-') {
    RADIUS=max(RADIUS-1, 2);
  }
  else if (key=='=') {
    RADIUS=min(RADIUS+1, DIM);
  }
}
