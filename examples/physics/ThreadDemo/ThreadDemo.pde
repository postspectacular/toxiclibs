/**
 * Thread demo showing the following:
 * - construction of a 2D string made from particles and springs/sticks using the ParticleString2D class 
 * - dynamic locking & unlocking of particles
 *
 * Click the mouse to lock/unlock the end of the string at its current position.
 * The head of the string is always linked to the current mouse position
 *
 * @author Karsten Schmidt <info at postspectacular dot com>
 */

/* 
 * Copyright (c) 2008-2009 Karsten Schmidt
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

import toxi.physics2d.constraints.*;
import toxi.physics2d.*;

import toxi.geom.*;
import toxi.math.*;

int NUM_PARTICLES = 100;
int REST_LENGTH=10;

VerletPhysics2D physics;
VerletParticle2D head,tail;

boolean isTailLocked;

void setup() {
  size(1024,768,OPENGL);
  smooth();
  physics=new VerletPhysics2D();
  Vec2D stepDir=new Vec2D(1,1).normalizeTo(REST_LENGTH);
  ParticleString2D s=new ParticleString2D(physics, new Vec2D(), stepDir, NUM_PARTICLES, 1, 0.1);
  head=s.getHead();
  head.lock();
  tail=s.getTail();
}

void draw() {
  background(0);
  stroke(255,100);
  noFill();
  head.set(mouseX,mouseY);
  physics.update();
  beginShape();
  for(Iterator i=physics.particles.iterator(); i.hasNext();) {
    VerletParticle2D p=(VerletParticle2D)i.next();
    vertex(p.x,p.y);
  }
  endShape();
  for(Iterator i=physics.particles.iterator(); i.hasNext();) {
    VerletParticle2D p=(VerletParticle2D)i.next();
    ellipse(p.x,p.y,5,5);
  }
}

void mousePressed() {
  isTailLocked=!isTailLocked;
  if (isTailLocked) {
    tail.lock();
  } 
  else {
    tail.unlock();
  }
}


