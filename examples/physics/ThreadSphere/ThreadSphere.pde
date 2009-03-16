/**
 * ThreadSphere demo showing the following:
 * - construction of a 2D string made from particles and springs/sticks
 * - usage of ParticleConstraint interface for collision detection
 *
 * Click the mouse to lock/unlock the end of the string at its current position.
 * The head of the string is always linked to the current mouse position
 *
 * @author Karsten Schmidt <info at postspectacular dot com>
 */

import processing.opengl.*;

import toxi.physics.constraints.*;
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
  // create collision sphere at origin
  ParticleConstraint sphere=new SphereAvoider(SPHERE_RADIUS);
  physics=new VerletPhysics();
  // weak gravity along Y axis
  physics.gravity=Vec3D.Y_AXIS.scale(0.01);
  // set bounding box to 110% of sphere radius
  physics.worldBounds=new AABB(new Vec3D(),new Vec3D(SPHERE_RADIUS,SPHERE_RADIUS,SPHERE_RADIUS).scaleSelf(1.1));
  VerletParticle prev=null;
  for(int i=0; i<NUM_PARTICLES; i++) {
    // create particles at random positions outside sphere
    VerletParticle p=new VerletParticle(Vec3D.randomVector().scaleSelf(SPHERE_RADIUS*2));
    // set sphere as particle constraint
    p.constraint=sphere;
    physics.addParticle(p);
    if (prev!=null) {
      physics.addSpring(new VerletSpring(prev,p,REST_LENGTH, 0.5));
    }
    prev=p;
  }
  // make the 1st particle immovable (for the simulation)
  head=physics.particles.get(0);
  head.lock();
}

void draw() {
  background(0);
  translate(width/2,height/2,0);
  rotateY(frameCount*0.01);
  noFill();
  stroke(255,50);
  strokeWeight(0.5);
  // draw visual reference for collision sphere
  sphere(SPHERE_RADIUS);
  // and world bounds
  box(physics.worldBounds.getExtent().x*2);
  // move head to mouse pos in XY plane
  head.set(mouseX-width/2,mouseY-height/2,0);
  // also apply sphere constraint to head
  // this needs to be done manually because if this particle is locked
  // it won't be updated automatically
  head.constraint.apply(head);
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

// every time step this constraint is applied to all particles it's been attached to
class SphereAvoider implements ParticleConstraint {
  float radius, radSquared;

  SphereAvoider(float r) {
    radius=r;
    radSquared=r*r;
  }

  // don't allow particle inside sphere
  // (to keep them inside and not allow outside, just flip the "<" into ">")
  public void apply(VerletParticle p) {
    if (p.magSquared() < radSquared) {
      p.normalize().scaleSelf(radius);
    }
  }
}
