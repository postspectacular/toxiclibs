package toxi.test;

import processing.core.PApplet;
import toxi.geom.Circle;
import toxi.geom.Ray3D;
import toxi.geom.Ray3DIntersector;
import toxi.geom.Triangle2D;
import toxi.geom.Vec2D;
import toxi.geom.Vec3D;
import toxi.processing.ToxiclibsSupport;

public class Circle3 extends PApplet {

    private ToxiclibsSupport gfx;

    public void draw() {
        background(255);
        noFill();
        final Vec2D p1 = new Vec2D(100, 200);
        final Vec2D p2 = new Vec2D(200, 50);
        final Vec2D p3 = new Vec2D(mouseX, mouseY);
        gfx.triangle(new Triangle2D(p1, p2, p3));
        Circle circle = Circle.from3Points(p1, p2, p3);
        if (circle != null) {
            gfx.ellipse(circle);
            gfx.circle(p1, 3);
            gfx.circle(p2, 3);
        }
    }

    public void setup() {
        size(400, 400);
        gfx = new ToxiclibsSupport(this);
        Ray3D r = new Ray3D(new Vec3D(), new Vec3D(1, 0, 0));
        Ray3D r2 = new Ray3D(new Vec3D(10, 10, 0), new Vec3D(1, -1, 0));
        Ray3DIntersector ri = new Ray3DIntersector(r);
        ri.intersectsRay(r2);
        println(ri.getIntersectionData());
    }
}
