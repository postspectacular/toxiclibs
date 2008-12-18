/**
 * Thread demo showing the following:
 * - construction of a 2D string made from particles and springs/sticks
 * - dynamic locking & unlocking of particles
 *
 * Click the mouse to lock/unlock the end of the string at its current position.
 * The head of the string is always linked to the current mouse position
 *
 * @author Karsten Schmidt <info at postspectacular dot com>
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
  VerletParticle2D prev=null;
  for(int i=0; i<NUM_PARTICLES; i++) {
    VerletParticle2D p=new VerletParticle2D(new Vec2D());
    physics.addParticle(p);
    if (prev!=null) {
      physics.addSpring(new VerletSpring2D(prev,p,REST_LENGTH, 0.5));
    }
    prev=p;
  }
  tail=prev;
  head=physics.particles.get(0);
  head.lock();
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
  } else {
    tail.unlock();
  }
}
