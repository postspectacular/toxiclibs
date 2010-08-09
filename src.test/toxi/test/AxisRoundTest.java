package toxi.test;

import processing.core.PApplet;
import toxi.geom.Vec3D;
import toxi.processing.ToxiclibsSupport;

public class AxisRoundTest extends PApplet {

    ToxiclibsSupport gfx;

    public void draw() {
        background(255);
        translate(width / 2, height / 2);
        rotateX(0.3f);
        rotateY(PI * 0.25f);
        gfx.origin(new Vec3D(), 20);
        stroke(0);
        Vec3D v =
                new Vec3D(mouseX - width / 2, mouseY - height / 2, 0)
                        .normalize().roundToAxis();
        gfx.line(new Vec3D(0, 0, 0), v.scale(100));
    }

    public void keyPressed() {

    }

    public void setup() {
        size(400, 400, P3D);
        gfx = new ToxiclibsSupport(this);
    }
}
