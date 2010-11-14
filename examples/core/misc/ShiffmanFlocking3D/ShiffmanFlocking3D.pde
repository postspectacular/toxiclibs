/**
 * <p>Flocking by <a href="http://www.shiffman.net">Daniel Shiffman</a>
 * created for The Nature of Code class, ITP, Spring 2009.</p>
 * 
 * <p>Ported to 3D & toxiclibs by Karsten Schmidt</p>
 * 
 * <p>Demonstration of <a href="http://www.red3d.com/cwr/">Craig Reynolds' "Flocking" behavior</a><br/>
 * <p>Rules: Cohesion, Separation, Alignment</p>
 * 
 * <p><strong>Usage:</strong> Click mouse to add boids into the system</p>
 */

/* 
 * Copyright (c) 2009 Daniel Shiffman
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
 
import processing.opengl.*;
import toxi.processing.*;
import toxi.geom.*;
import toxi.geom.mesh.*;
import toxi.math.*;

int DIM = 200;
int NUM = 200;
int NEIGHBOR_DIST = 50;
int SEPARATION = 25;
float BOID_SIZE = 5;

Flock flock;

// color transformation matrix used to map XYZ position into RGB values
Matrix4x4 colorMatrix=new Matrix4x4().scale(255f/(DIM*2)).translate(DIM,DIM,DIM);

ToxiclibsSupport gfx;

void setup() {
  size(1024,576,OPENGL);
  gfx=new ToxiclibsSupport(this);
  flock = new Flock();
  // Add an initial set of boids into the system
  for (int i = 0; i < NUM; i++) {
    flock.addBoid(new Boid(new Vec3D(), 3, 0.05, NEIGHBOR_DIST, SEPARATION));
  }
  smooth();
}

void draw() {
  background(0);
  lights();
  translate(width/2,height/2,0);
  rotateY(mouseX*0.01);
  noFill();
  stroke(255,50);
  box(DIM*2);
  fill(255);
  noStroke();
  flock.run();
}

// Add a new boid into the System
void mousePressed() {
  flock.addBoid(new Boid(new Vec3D(), 3, 0.05, NEIGHBOR_DIST, SEPARATION));
}
