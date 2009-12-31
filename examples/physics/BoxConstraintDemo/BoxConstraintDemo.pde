/**
 * <p>This demo demonstrates how to define and use a custom ParticleConstraint implementation to
 * create box obstactles in the physical simulation space. Two boxes are defined and attached as
 * constraint to each particle. The particles themselves are connected via springs to form a long
 * string/thread with one of its ends anchored in space.</p>
 * 
 * <p>Usage: Press 'r' to restart the simulation</p>
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

import toxi.geom.*;
import toxi.physics.*;
import toxi.physics.constraints.*;

BoxConstraint[] boxes=new BoxConstraint[2];

int NUM_PARTICLES = 30;
int REST_LENGTH=20;

VerletPhysics physics;
VerletParticle head;

void setup() {
  size(1024,576,OPENGL);
  // create 2 boxes
  boxes[0]=new BoxConstraint(new Vec3D(100,-100,-100),new Vec3D(150,100,100));
  boxes[1]=new BoxConstraint(new Vec3D(-150,-100,-100),new Vec3D(-100,100,100));
  initPhysics();
}

void draw() {
  physics.update();
  background(255);
  noFill();
  translate(width/2,height/2,0);
  rotateX(-0.33);
  rotateY(frameCount*0.01);
  // draw the box obstacles
  for(int i=0; i<boxes.length; i++) {
    boxes[i].draw();
  }
  // draw all springs
  beginShape(LINES);
  for(Iterator i=physics.springs.iterator(); i.hasNext();) {
    VerletSpring s=(VerletSpring)i.next();
    vertex(s.a.x,s.a.y,s.a.z); 
    vertex(s.b.x,s.b.y,s.b.z);
  }
  endShape();
  for(Iterator i=physics.particles.iterator(); i.hasNext();) {
    VerletParticle p=(VerletParticle)i.next();
    ellipse(p.x,p.y,2,2);
  }
}

void initPhysics() {
  // setup physics engine
  physics=new VerletPhysics();
  physics.gravity=Vec3D.Y_AXIS.scale(0.02);
  VerletParticle prev=null;
  // create particle string
  // and assign boxes as constraints to each particle
  for(int i=0; i<NUM_PARTICLES; i++) {
    VerletParticle p=new VerletParticle(new Vec3D((i-NUM_PARTICLES/2)*REST_LENGTH,-110,0));
    for(int j=0; j<boxes.length; j++) {
      p.addConstraint(boxes[j]);
    }
    physics.addParticle(p);
    if (prev!=null) {
      physics.addSpring(new VerletSpring(prev,p,REST_LENGTH, 0.5));
    }
    prev=p;
  }
  // anchor the 1st particle in space 
  physics.particles.get(0).lock();
}

void keyPressed() {
  if (key=='r') {
    initPhysics();
  }
}

