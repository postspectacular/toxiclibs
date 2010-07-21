package toxi.test;

import processing.core.PApplet;
import toxi.math.BezierInterpolation;
import toxi.processing.ToxiclibsSupport;

public class BezierTest extends PApplet {

    public static void main(String[] args) {
        PApplet.main(new String[] { "toxi.test.BezierTest" });
    }

    ToxiclibsSupport gfx;

    BezierInterpolation b = new BezierInterpolation(1f / 3, -1f / 3);

    public void draw() {
        background(255);
        float m = map(mouseX, 0, width, 0f, 3);
        println(m);
        b.setCoefficients(m, -m);
        for (int x = 0; x < width; x++) {
            float y = b.interpolate(0, height, (float) x / width);
            point(x, y);
        }
    }

    public void keyPressed() {

    }

    public void setup() {
        size(200, 200, OPENGL);
        gfx = new ToxiclibsSupport(this);
    }
}
