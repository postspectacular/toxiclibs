package toxi.test;

import processing.core.PApplet;
import toxi.geom.Line3D;
import toxi.geom.Line3D.LineIntersection;
import toxi.geom.Line3D.LineIntersection.Type;
import toxi.geom.Vec3D;
import toxi.processing.ToxiclibsSupport;

public class Line3DIsecTest extends PApplet {

    public static void main(String[] args) {
        PApplet.main(new String[] { "toxi.test.Line3DIsecTest" });
    }

    ToxiclibsSupport gfx;

    public void draw() {
        background(255);
        translate(width / 2, height / 2, 0);
        rotateX(mouseY * 0.01f);
        rotateY(mouseX * 0.01f);
        gfx.origin(new Vec3D(), 50);
        Line3D a = new Line3D(new Vec3D(-100, 0, 0), new Vec3D(100, 10, 0));
        Line3D b =
                new Line3D(new Vec3D(-80, -50, 100), new Vec3D(20, 100, -100));
        gfx.line(a);
        gfx.line(b);
        LineIntersection isec = a.closestLineTo(b);
        if (isec.getType() == Type.INTERSECTING) {
            gfx.line(isec.getLine());
        }
    }

    public void keyPressed() {

    }

    public void setup() {
        size(1280, 720, OPENGL);
        gfx = new ToxiclibsSupport(this);
    }
}
