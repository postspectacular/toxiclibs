/**
 * <p>Softbody square demo is showing how to create a 2D square mesh out of
 * verlet particles and make it stable enough to avoid total structural
 * deformation by including an inner skeleton.</p>
 *
 * <p>Usage: move mouse to drag/deform the square</p>
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

import toxi.physics2d.behaviors.*;
import toxi.physics2d.*;

import toxi.geom.*;
import toxi.math.*;

int DIM=10;
int REST_LENGTH=20;
float STRENGTH=0.125;
float INNER_STRENGTH = 0.13;

VerletPhysics2D physics;
VerletParticle2D head,tail;

void setup() {
  size(1280,720,OPENGL);
  smooth();
  physics=new VerletPhysics2D();
  physics.addBehavior(new GravityBehavior(new Vec2D(0,0.1)));
  physics.setWorldBounds(new Rect(0,0,width,height));
  for(int y=0,idx=0; y<DIM; y++) {
    for(int x=0; x<DIM; x++) {
      VerletParticle2D p=new VerletParticle2D(x*REST_LENGTH,y*REST_LENGTH);
      physics.addParticle(p);
      if (x>0) {
        VerletSpring2D s=new VerletSpring2D(p,physics.particles.get(idx-1),REST_LENGTH,STRENGTH);
        physics.addSpring(s);
      }
      if (y>0) {
        VerletSpring2D s=new VerletSpring2D(p,physics.particles.get(idx-DIM),REST_LENGTH,STRENGTH);
        physics.addSpring(s);
      }
      idx++;
    }
  }
  VerletParticle2D p=physics.particles.get(0);
  VerletParticle2D q=physics.particles.get(physics.particles.size()-1);
  float len=sqrt(sq(REST_LENGTH*(DIM-1))*2);
  VerletSpring2D s=new VerletSpring2D(p,q,len,INNER_STRENGTH);
  physics.addSpring(s);
  p=physics.particles.get(DIM-1);
  q=physics.particles.get(physics.particles.size()-DIM);
  s=new VerletSpring2D(p,q,len,INNER_STRENGTH);
  physics.addSpring(s);
  head=physics.particles.get((DIM-1)/2);
  head.lock();
}

void draw() {
  background(0);
  stroke(255);
  head.set(mouseX,mouseY);
  physics.update();
  for(Iterator i=physics.springs.iterator(); i.hasNext();) {
    VerletSpring2D s=(VerletSpring2D)i.next();
    line(s.a.x,s.a.y,s.b.x,s.b.y);
  }
}


