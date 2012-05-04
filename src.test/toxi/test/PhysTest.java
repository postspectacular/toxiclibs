package toxi.test;

import java.util.Iterator;

import processing.core.PApplet;
import toxi.geom.Vec3D;
import toxi.physics3d.ParticleString3D;
import toxi.physics3d.VerletParticle3D;
import toxi.physics3d.VerletPhysics3D;
import toxi.physics3d.behaviors.AttractionBehavior3D;

public class PhysTest extends PApplet {

    int NUM_PARTICLES = 50;
    int REST_LENGTH = 10;

    VerletPhysics3D physics;
    VerletParticle3D head, tail;

    boolean isTailLocked;
    private Vec3D mousePos = new Vec3D();

    public void draw() {
        background(0);
        translate(width / 2, height * 0.25f, 0);
        stroke(255, 100);
        noFill();
        head.set(mouseX, mouseY, 0);
        physics.update();
        beginShape();
        for (Iterator<VerletParticle3D> i = physics.particles.iterator(); i
                .hasNext();) {
            VerletParticle3D p = i.next();
            vertex(p.x, p.y, p.z);
        }
        endShape();
    }

    public void mousePressed() {
        isTailLocked = !isTailLocked;
        if (isTailLocked) {
            tail.lock();
        } else {
            tail.unlock();
        }
    }

    public void setup() {
        size(1024, 768, P3D);
        smooth();
        physics = new VerletPhysics3D();
        // physics.addBehavior(new GravityBehavior(new Vec3D(0, 0.2f, 0),
        // 0.6f));
        Vec3D stepDir = new Vec3D(1, 0, 0).normalizeTo(REST_LENGTH);
        ParticleString3D s = new ParticleString3D(physics, new Vec3D(), stepDir,
                NUM_PARTICLES, 1, 0.1f);
        head = s.getHead();
        head.lock();
        tail = s.getTail();
        physics.addBehavior(new AttractionBehavior3D(new Vec3D(), 400, 0.5f));
    }
}
