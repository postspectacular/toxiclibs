/**
 * Thread demo showing the following:
 * - construction of a 3D string made from particles and springs/sticks
 * - dynamic locking & unlocking of particles
 * - usage of the SphereConstraint class to keep particles in/outside the sphere
 *
 * Click the mouse to lock/unlock the end of the string at its current position.
 * The head of the string is always linked to the current mouse position
 *
 * @author Karsten Schmidt <info at postspectacular dot com>
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

import toxi.physics.constraints.*;
import toxi.physics.behaviors.*;
import toxi.physics.*;

import toxi.geom.*;
import toxi.math.*;

int NUM_PARTICLES = 100;
int REST_LENGTH=10;
int SPHERE_RADIUS=200;

VerletPhysics physics;
VerletParticle head;

void setup() {
  size(1024,768,OPENGL);
  smooth();
  // create collision sphere at origin, replace OUTSIDE with INSIDE to keep particles inside the sphere
  ParticleConstraint sphere=new SphereConstraint(new Sphere(new Vec3D(),SPHERE_RADIUS),SphereConstraint.OUTSIDE);
  physics=new VerletPhysics();
  // weak gravity along Y axis
  physics.addBehavior(new GravityBehavior(new Vec3D(0,0.01,0)));
  // set bounding box to 110% of sphere radius
  physics.setWorldBounds(new AABB(new Vec3D(),new Vec3D(SPHERE_RADIUS,SPHERE_RADIUS,SPHERE_RADIUS).scaleSelf(1.1)));
  VerletParticle prev=null;
  for(int i=0; i<NUM_PARTICLES; i++) {
    // create particles at random positions outside sphere
    VerletParticle p=new VerletParticle(Vec3D.randomVector().scaleSelf(SPHERE_RADIUS*2));
    // set sphere as particle constraint
    p.addConstraint(sphere);
    physics.addParticle(p);
    if (prev!=null) {
      physics.addSpring(new VerletSpring(prev,p,REST_LENGTH, 0.5));
    }
    prev=p;
  }
  head=physics.particles.get(0);
  head.lock();
}

void draw() {
  background(0);
  translate(width/2,height/2,0);
  rotateY(frameCount*0.01);
  noFill();
  stroke(255,100);
  strokeWeight(1);
  // draw visual reference for collision sphere
  sphere(SPHERE_RADIUS);
  // and world bounds
  box(physics.getWorldBounds().getExtent().x*2);
  // move head to mouse pos in XY plane
  head.set(mouseX-width/2,mouseY-height/2,0);
  // also apply sphere constraint to head
  // this needs to be done manually because if this particle is locked
  // it won't be updated automatically
  head.applyConstraints();
  // update sim
  physics.update();
  // draw all springs
  beginShape(LINES);
  for(Iterator i=physics.springs.iterator(); i.hasNext();) {
    VerletSpring s=(VerletSpring)i.next();
    vertex(s.a.x,s.a.y,s.a.z);
    vertex(s.b.x,s.b.y,s.b.z);
  }
  endShape();
  // then all particles as dots
  strokeWeight(4);
  for(Iterator i=physics.particles.iterator(); i.hasNext();) {
    VerletParticle p=(VerletParticle)i.next();
    if (abs(p.magnitude()-SPHERE_RADIUS)<1) {
      stroke(0,255,0);
    } 
    else {
      stroke(255,0,0);
    }
    point(p.x,p.y,p.z);
  }
}

