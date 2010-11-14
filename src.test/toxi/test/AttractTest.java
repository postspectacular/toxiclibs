package toxi.test;

import java.util.Iterator;

import processing.core.PApplet;
import toxi.geom.AABB;
import toxi.geom.Vec3D;
import toxi.physics.VerletParticle;
import toxi.physics.VerletPhysics;
import toxi.physics.behaviors.AttractionBehavior;
import toxi.physics.behaviors.GravityBehavior;
import toxi.processing.ToxiclibsSupport;

public class AttractTest extends PApplet {

	ToxiclibsSupport gfx;

	int NUM_PARTICLES = 1000;

	VerletPhysics physics;

	public void draw() {
		background(0);
		translate(width / 2, height / 2, 0);
		rotateX(mouseY * 0.01f);
		rotateY(mouseX * 0.01f);
		stroke(255, 100);
		noFill();
		gfx.box(physics.getWorldBounds());
		physics.update();
		for (Iterator<VerletParticle> i = physics.particles.iterator(); i
				.hasNext();) {
			VerletParticle p = i.next();
			gfx.box(new AABB(p, 5));
		}
	}

	public void setup() {
		size(1024, 768, P3D);
		gfx = new ToxiclibsSupport(this);
		physics = new VerletPhysics();
		physics.setWorldBounds(new AABB(new Vec3D(), 250));
		physics.addBehavior(new GravityBehavior(new Vec3D(0, 0.05f, 0), 0.6f));
		physics.addBehavior(new AttractionBehavior(new Vec3D(), 500, 0.1f));
		for (int i = 0; i < NUM_PARTICLES; i++) {
			VerletParticle p = new VerletParticle(random(-250, 250), random(
					-250, 250), random(-250, 250));
			physics.addParticle(p);
			for (int j = 0; j < i; j++) {
				physics.particles.get(j).addBehavior(
						new AttractionBehavior(p, 20, -0.9f),
						physics.getTimeStep());
			}
		}
	}
}
