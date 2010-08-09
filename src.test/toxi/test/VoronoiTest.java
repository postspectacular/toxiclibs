package toxi.test;

import processing.core.PApplet;
import toxi.geom.Polygon2D;
import toxi.geom.Triangle2D;
import toxi.geom.Vec2D;
import toxi.geom.mesh2d.Voronoi;
import toxi.processing.ToxiclibsSupport;
import toxi.util.datatypes.BiasedFloatRange;

public class VoronoiTest extends PApplet {

    public static void main(String[] args) {
        PApplet.main(new String[] { "toxi.test.VoronoiTest" });
    }

    private BiasedFloatRange ypos = new BiasedFloatRange(0, 1, 1, 1f);
    private ToxiclibsSupport gfx;

    Voronoi voronoi = new Voronoi();

    private boolean doShowPoints = true;
    private boolean doSave;
    private boolean doShowDelaunay;

    public void draw() {
        background(0);
        stroke(0);
        for (Polygon2D poly : voronoi.getRegions()) {
            fill(poly.getCentroid().y / height * 255);
            gfx.polygon2D(poly);
        }
        if (doShowDelaunay) {
            noFill();
            stroke(0, 0, 255);
            beginShape(TRIANGLES);
            for (Triangle2D t : voronoi.getTriangles()) {
                gfx.triangle(t, false);
            }
            endShape();
        }
        if (doShowPoints) {
            fill(255);
            noStroke();
            for (Vec2D c : voronoi.getSites()) {
                ellipse(c.x, c.y, 5, 5);
            }
        }
        if (doSave) {
            saveFrame("voronoi-" + (System.currentTimeMillis() / 1000) + ".png");
            doSave = false;
        }
        fill(255, 0, 0);
        text("p: toggle points", 20, 20);
        text("t: toggle triangles", 20, 40);
        text("c: clear all", 20, 60);
        text("r: add random", 20, 80);
        text("space: save frame", 20, 100);

    }

    public void keyPressed() {
        if (key == ' ') {
            doSave = true;
        }
        if (key == 't') {
            doShowDelaunay = !doShowDelaunay;
        }
        if (key == 'c') {
            voronoi = new Voronoi();
        }
        if (key == 'p') {
            doShowPoints = !doShowPoints;
        }
        if (key == 'r') {
            for (int i = 0; i < 10; i++) {
                voronoi.addPoint(new Vec2D(random(width), ypos.pickRandom()
                        * height));
            }
        }
    }

    public void mousePressed() {
        voronoi.addPoint(new Vec2D(mouseX, mouseY));
    }

    public void setup() {
        size(250, 700);
        gfx = new ToxiclibsSupport(this);
        textFont(createFont("SansSerif", 10));
    }
}
