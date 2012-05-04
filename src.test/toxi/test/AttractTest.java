package toxi.test;

import java.util.Iterator;

import processing.core.PApplet;
import toxi.geom.AABB;
import toxi.geom.Vec3D;
import toxi.physics3d.VerletParticle3D;
import toxi.physics3d.VerletPhysics3D;
import toxi.physics3d.behaviors.AttractionBehavior3D;
import toxi.physics3d.behaviors.GravityBehavior3D;
import toxi.processing.ToxiclibsSupport;

public class AttractTest extends PApplet {

    public static void main(String[] args) {
        PApplet.main(new String[] { "toxi.test.AttractTest" });
    }

    ToxiclibsSupport gfx;

    int NUM_PARTICLES = 100;

    VerletPhysics3D physics;

    public void draw() {
        background(0);
        translate(width / 2, height / 2, 0);
        rotateX(mouseY * 0.01f);
        rotateY(mouseX * 0.01f);
        stroke(255, 100);
        noFill();
        gfx.box(physics.getWorldBounds());
        physics.update();
        for (Iterator<VerletParticle3D> i = physics.particles.iterator(); i
                .hasNext();) {
            VerletParticle3D p = i.next();
            gfx.box(new AABB(p, 5));
        }
    }

    public void setup() {
        size(1024, 576, OPENGL);
        gfx = new ToxiclibsSupport(this);
        physics = new VerletPhysics3D();
        physics.setDrag(0.15f);
        physics.setWorldBounds(new AABB(new Vec3D(), 250));
        physics.addBehavior(new GravityBehavior3D(new Vec3D(0, 0.2f, 0)));
        physics.addBehavior(new AttractionBehavior3D(new Vec3D(), 500, 0.1f));
        for (int i = 0; i < NUM_PARTICLES; i++) {
            VerletParticle3D p = new VerletParticle3D(random(-250, 250), random(
                    -250, 250), random(-250, 250));
            physics.addParticle(p);
            for (int j = 0; j < i; j++) {
                physics.particles.get(j).addBehavior(
                        new AttractionBehavior3D(p, 50, -0.9f, 0.02f),
                        physics.getTimeStep());
            }
        }
    }
}
