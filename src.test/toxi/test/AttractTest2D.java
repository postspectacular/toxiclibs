package toxi.test;

import processing.core.PApplet;
import toxi.geom.PointQuadtree;
import toxi.geom.Rect;
import toxi.geom.Vec2D;
import toxi.physics2d.VerletParticle2D;
import toxi.physics2d.VerletPhysics2D;
import toxi.physics2d.behaviors.AttractionBehavior2D;
import toxi.physics2d.behaviors.GravityBehavior2D;
import toxi.processing.ToxiclibsSupport;

public class AttractTest2D extends PApplet {

    public static void main(String[] args) {
        PApplet.main(new String[] {
            "toxi.test.AttractTest2D"
        });
    }

    ToxiclibsSupport gfx;

    int NUM_PARTICLES = 2000;

    VerletPhysics2D physics;

    private Vec2D mousePos;

    private AttractionBehavior2D mouseAttractor;

    private void addParticle() {
        VerletParticle2D p = new VerletParticle2D(Vec2D.randomVector().scale(5)
                .addSelf(width * 0.5f, 0));
        physics.addParticle(p);
        // add a negative attraction force field around the new particle
        physics.addBehavior(new AttractionBehavior2D(p, 30, -1.2f, 0.01f));
    }

    public void draw() {
        background(0);
        noStroke();
        fill(255);
        if (physics.particles.size() < NUM_PARTICLES) {
            addParticle();
        }
        physics.update();
        for (VerletParticle2D p : physics.particles) {
            rect(p.x, p.y, 5, 5);
        }
        // Quadtree tree = (Quadtree) physics.getIndex();
        // noFill();
        // stroke(255, 50);
        // tree.prewalk(new QuadtreeVisitor() {
        //
        // public void visitNode(Quadtree node) {
        // gfx.rect(node);
        // }
        // });
        fill(255);
        text("fps: " + frameRate, 20, 20);
        text("count: " + physics.particles.size(), 20, 40);
    }

    public void mouseDragged() {
        mousePos.set(mouseX, mouseY);
    }

    public void mousePressed() {
        mousePos = new Vec2D(mouseX, mouseY);
        mouseAttractor = new AttractionBehavior2D(mousePos, 400, 1.2f);
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
        physics.addBehavior(new GravityBehavior2D(new Vec2D(0, 0.15f)));
        physics.setIndex(new PointQuadtree(null, 0, 0, width + 1, height + 1));
        // physics.setIndex(new SpatialBins<Vec2D>(0, width, 80,
        // new CoordinateExtractor<Vec2D>() {
        //
        // public final float coordinate(Vec2D p) {
        // return p.x;
        // }
        // }));
        textFont(createFont("SansSerif", 10));
    }
}
