/**
 * <p>This example is building on an exercise done at the
 * V&A Computational Design workshop (Feb/Mar 2011)</p>
 *
 * <p>Here we demonstrate how to use a combination of springs & attraction forces
 * to build two distinct objects (a string and a ball). Both will repel each other
 * to resolve collisions (but will still allow forceful intersection in extreme cases).
 * The repulsion is achieved by attaching negative force fields around each string particle.</p>
 *
 * <p>Usage:<ul>
 * <li>Click and drag mouse to select & move particles</li>
 * <li>Press 'r' to reset</li>
 * </ul></p>
 */

/* 
 * Copyright (c) 2011 Karsten Schmidt
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



import toxi.physics2d.constraints.*;
import toxi.physics2d.behaviors.*;
import toxi.physics2d.*;

import toxi.geom.*;

import java.util.List;

// number of particles for string
int STRING_RES=100;
// number particles for ball
int BALL_RES=60;
// ball size
int BALL_RADIUS=80;

// squared snap distance for mouse selection
float SNAP_DIST = 20 * 20;

VerletPhysics2D physics;
VerletParticle2D selectedParticle;

void setup() {
  size(1024,720,P3D);
  initPhysics();
}

void draw() {
  // 1st update
  physics.update();
  // then drawing
  background(224);
  // draw all springs
  stroke(255,0,255);
  for(VerletSpring2D s : physics.springs) {
    line(s.a.x,s.a.y, s.b.x, s.b.y);
  }
  // show all particles
  fill(0);
  noStroke();
  for(VerletParticle2D p : physics.particles) {
    ellipse(p.x,p.y,5,5);
  }
  // highlight selected particle (if there is one currently)
  if (selectedParticle!=null) {
    fill(255,0,255);
    ellipse(selectedParticle.x,selectedParticle.y,20,20);
  }
}

void mousePressed() {
  // find particle under mouse
  Vec2D mousePos=new Vec2D(mouseX,mouseY);
  for(int i=1; i<physics.particles.size()-1; i++) {
    VerletParticle2D p=physics.particles.get(i);
    // using distanceToSquared() is faster than distanceTo()
    if (mousePos.distanceToSquared(p)<SNAP_DIST) {
      // lock it and store for further reference
      selectedParticle=p.lock();
      // force quit the loop
      break;
    }
  }
}

void mouseDragged() {
  if (selectedParticle!=null) {
    // move selected particle to new mouse pos
    selectedParticle.set(mouseX,mouseY);
  }
}

void mouseReleased() {
  // unlock the selected particle
  if (selectedParticle!=null) {
    selectedParticle.unlock();
    selectedParticle=null;
  }
}

void keyPressed() {
  if (key=='r') {
    initPhysics();
  }
}
