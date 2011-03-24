package toxi.test;

import java.util.Iterator;

import processing.core.PApplet;
import toxi.geom.Rect;
import toxi.geom.Vec2D;
import toxi.physics2d.VerletParticle2D;
import toxi.physics2d.VerletPhysics2D;
import toxi.physics2d.behaviors.AttractionBehavior;
import toxi.physics2d.behaviors.GravityBehavior;
import toxi.processing.ToxiclibsSupport;

public class AttractTest2D extends PApplet {

    public static void main(String[] args) {
        PApplet.main(new String[] { "toxi.test.AttractTest2D" });
    }

    ToxiclibsSupport gfx;

    int NUM_PARTICLES = 1000;

    VerletPhysics2D physics;

    private Vec2D mousePos;

    private AttractionBehavior mouseAttractor;

    private void addParticle() {
        VerletParticle2D p = new VerletParticle2D(Vec2D.randomVector().scale(5)
                .addSelf(width / 2, 0));
        physics.addParticle(p);
        for (int j = 0; j < physics.particles.size(); j++) {
            physics.particles.get(j).addBehavior(
                    new AttractionBehavior(p, 10, -2f, 0.01f),
                    physics.getTimeStep());
        }
    }

    public void draw() {
        background(0);
        noStroke();
        fill(255);
        if (physics.particles.size() < NUM_PARTICLES) {
            addParticle();
        }
        physics.update();
        for (Iterator<VerletParticle2D> i = physics.particles.iterator(); i
                .hasNext();) {
            VerletParticle2D p = i.next();
            ellipse(p.x, p.y, 5, 5);
        }
    }

    public void mouseDragged() {
        mousePos.set(mouseX, mouseY);
    }

    public void mousePressed() {
        mousePos = new Vec2D(mouseX, mouseY);
        mouseAttractor = new AttractionBehavior(mousePos, 500, 0.9f);
        physics.addBehavior(mouseAttractor);
    }

    public void mouseReleased() {
        physics.removeBehavior(mouseAttractor);
    }

    public void setup() {
        size(1024, 640, OPENGL);
        frameRate(999);
        gfx = new ToxiclibsSupport(this);
        physics = new VerletPhysics2D();
        physics.setDrag(0.1f);
        physics.setWorldBounds(new Rect(0, 0, width, height));
        physics.addBehavior(new GravityBehavior(new Vec2D(0, 0.15f)));
    }
}
