/**
 * <p>This demo demonstrates how to use a ParticleConstraint implementation to
 * create box obstactles in the physical simulation space. Two boxes are defined and attached as
 * constraint to each particle. The particles themselves are connected via springs to form a long
 * string/thread with one of its ends anchored in space. The ParticleString class is used to create
 * this string.</p>
 * 
 * <p>Usage: Press 'r' to restart the simulation</p>
 *
 * <p>UPDATES:
 * <ul>
 * <li>2010-12-30: updated gravity handling to use behaviours</li>
 * </ul></p>
 */

/* 
 * Copyright (c) 2010 Karsten Schmidt
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
import toxi.physics3d.*;
import toxi.physics3d.behaviors.*;
import toxi.physics3d.constraints.*;

VisibleBoxConstraint[] boxes=new VisibleBoxConstraint[2];

int NUM_PARTICLES = 90;
int REST_LENGTH=10;

VerletPhysics3D physics;
VerletParticle3D head;

void setup() {
  size(1024,576,P3D);
  // create 2 boxes
  boxes[0]=new VisibleBoxConstraint(new Vec3D(100,-100,-100),new Vec3D(150,100,100));
  boxes[1]=new VisibleBoxConstraint(new Vec3D(-150,-100,-100),new Vec3D(-100,100,100));
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
  for(VerletSpring3D s : physics.springs) {
    vertex(s.a); 
    vertex(s.b);
  }
  endShape();
  for(VerletParticle3D p : physics.particles) {
    ellipse(p.x,p.y,2,2);
  }
}

void initPhysics() {
  // setup physics engine
  physics=new VerletPhysics3D();
  physics.addBehavior(new GravityBehavior3D(Vec3D.Y_AXIS.scale(0.05)));
  // string start position & direction
  Vec3D startPos=new Vec3D(-NUM_PARTICLES/2*REST_LENGTH, -110, 0);
  Vec3D dir=new Vec3D(REST_LENGTH,0,0);
  // create the string as a line along the direction vector
  ParticleString3D string=new ParticleString3D(physics,startPos, dir, NUM_PARTICLES, 1, 0.5);
  // add the 2 boxes as constraints to all string particles
  for(int j=0; j<boxes.length; j++) {
    VerletPhysics3D.addConstraintToAll(boxes[j],string.particles);
  }
  // anchor the 1st particle in space 
  string.particles.get(0).lock();
}

void keyPressed() {
  if (key=='r') {
    initPhysics();
  }
}

void vertex(Vec3D v) {
  vertex(v.x,v.y,v.z);
}

