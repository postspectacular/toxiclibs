import processing.opengl.*;

import toxi.geom.*;

import toxi.physics2d.*;
import toxi.physics2d.behaviors.*;

int NUM_PARTICLES = 1000;

VerletPhysics2D physics;
AttractionBehavior mouseAttractor;

Vec2D mousePos;

void setup() {
  size(1024, 640, OPENGL);
  physics = new VerletPhysics2D();
  physics.setDrag(0.1f);
  physics.setWorldBounds(new Rect(0, 0, width, height));
  physics.addBehavior(new GravityBehavior(new Vec2D(0, 0.15f)));
}

void addParticle() {
  VerletParticle2D p = new VerletParticle2D(Vec2D.randomVector().scale(5).addSelf(width / 2, 0));
  physics.addParticle(p);
  for (int j = 0; j < physics.particles.size(); j++) {
    physics.particles.get(j).addBehavior(
    new AttractionBehavior(p, 10, -1.2f, 0.01f),
    physics.getTimeStep());
  }
}

void draw() {
  background(0);
  noStroke();
  fill(255);
  if (physics.particles.size() < NUM_PARTICLES) {
    addParticle();
  }
  physics.update();
  for (VerletParticle2D p : physics.particles) {
    ellipse(p.x, p.y, 5, 5);
  }
}

void mousePressed() {
  mousePos = new Vec2D(mouseX, mouseY);
  mouseAttractor = new AttractionBehavior(mousePos, 500, 0.9f);
  physics.addBehavior(mouseAttractor);
}

void mouseDragged() {
  mousePos.set(mouseX, mouseY);
}

void mouseReleased() {
  physics.removeBehavior(mouseAttractor);
}

