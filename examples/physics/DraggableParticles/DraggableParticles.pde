/**
 * Draggable particle demo using verletphysics-0006 or newer
 * http://toxiclibs.org
 *
 * Copyright (c) 2010 Karsten Schmidt
 * Licensed under GNU LGPL v2.1
 * http://creativecommons.org/licenses/LGPL/2.1/
 *
 */

import toxi.geom.*;
import toxi.physics2d.*;

VerletPhysics2D physics;
VerletParticle2D selected=null;

// squared snap distance for picking particles
float snapDist=10*10;

void setup() {
  size(600,600);
  smooth();
  physics=new VerletPhysics2D();
  physics.setWorldBounds(new Rect(0,0,width,height));
  // create 10 particle strings of 20 particles each
  for(int i=0; i<10; i++) {
    ParticleString2D s=new ParticleString2D(physics,new Vec2D(width/2,height/2),Vec2D.randomVector().scaleSelf(10),20,1,0.5);
  }
}

void draw() {
  background(255);
  noFill();
  physics.update();
  // draw all springs
  for(Iterator i=physics.springs.iterator(); i.hasNext();) {
    VerletSpring2D s=(VerletSpring2D)i.next();
    line(s.a.x,s.a.y,s.b.x,s.b.y);
  }
  // draw all particles
  for(Iterator i=physics.particles.iterator(); i.hasNext();) {
    VerletParticle2D p=(VerletParticle2D)i.next();
    // selected particle in cyan, all others in black
    stroke(p==selected ? 0xff00ffff : 0xff000000);
    ellipse(p.x,p.y,5,5);
  }
}

// check all particles if mouse pos is less than snap distance
void mousePressed() {
  selected=null;
  Vec2D mousePos=new Vec2D(mouseX,mouseY);
  for(Iterator i=physics.particles.iterator(); i.hasNext();) {
    VerletParticle2D p=(VerletParticle2D)i.next();
    // if mouse is close enough, keep a reference to
    // the selected particle and lock it (becomes unmovable by physics)
    if (p.distanceToSquared(mousePos)<snapDist) {
      selected=p;
      selected.lock();
      break;
    }
  }
}

// only react to mouse dragging events if we have a selected particle
void mouseDragged() {
  if (selected!=null) {
    selected.set(mouseX,mouseY);
  }
}

// if we had a selected particle unlock it again and kill reference
void mouseReleased() {
  if (selected!=null) {
    selected.unlock();
    selected=null;
  }
}

