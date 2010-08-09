package toxi.test;

import processing.core.PApplet;
import toxi.geom.Triangle2D;
import toxi.geom.Vec2D;
import toxi.processing.ToxiclibsSupport;

public class CircumCircleTest extends PApplet {

    private ToxiclibsSupport gfx;

    public void draw() {
        background(255);
        Triangle2D t =
                new Triangle2D(new Vec2D(100, 100), new Vec2D(mouseX, mouseY),
                        new Vec2D(200, 300));
        noFill();
        gfx.triangle(t, true);
        gfx.ellipse(t.getCircumCircle());
    }

    public void setup() {
        size(400, 400);
        gfx = new ToxiclibsSupport(this);
    }
}
