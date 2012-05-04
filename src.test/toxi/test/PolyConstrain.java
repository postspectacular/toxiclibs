package toxi.test;

import processing.core.PApplet;
import toxi.geom.Circle;
import toxi.geom.Polygon2D;
import toxi.geom.Vec2D;
import toxi.processing.ToxiclibsSupport;
import toxi.util.DateUtils;

public class PolyConstrain extends PApplet {

    public static void main(String[] args) {
        PApplet.main(new String[] {
            "toxi.test.PolyConstrain"
        });
    }

    private ToxiclibsSupport gfx;

    private boolean doSave;

    private Polygon2D poly;

    public void draw() {
        background(255);
        noFill();
        gfx.polygon2D(poly);
        fill(255, 0, 0);
        gfx.circle(new Vec2D(mouseX, mouseY).constrain(poly), 10);
        if (doSave) {
            saveFrame("PolyConstrain-" + DateUtils.timeStamp() + ".png");
            doSave = false;
        }
    }

    public void keyPressed() {
        switch (key) {
            case ' ':
                doSave = true;
                break;
        }
    }

    public void setup() {
        size(600, 600, OPENGL);
        gfx = new ToxiclibsSupport(this);
        poly = new Circle(200).toPolygon2D(9).translate(width / 2, height / 2);
        poly.get(0).x *= 0.66f;
    }
}
