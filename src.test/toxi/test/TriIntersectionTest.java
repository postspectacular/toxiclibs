package toxi.test;

import processing.core.PApplet;
import toxi.geom.Ray3D;
import toxi.geom.Triangle;
import toxi.geom.TriangleIntersector;
import toxi.geom.Vec3D;

public class TriIntersectionTest extends PApplet {

    public static void main(String[] args) {
        PApplet.main(new String[] { "toxi.test.TriIntersectionTest" });
    }

    public void draw() {
        background(128);
        background(0);
        lights();
        translate(width / 2, height / 2, 0);
        rotateX(mouseY * 0.01f);
        rotateY(mouseX * 0.01f);
        fill(255);

        Vec3D a = new Vec3D(-100, 50, 100);
        Vec3D b = new Vec3D(0, 0, -100);
        Vec3D c = new Vec3D(100, -10, 100);
        Triangle tri = new Triangle(a, b, c);
        TriangleIntersector ti = new TriangleIntersector(tri);
        ti.intersectsRay(new Ray3D(new Vec3D(10, 100, 0), new Vec3D(0, -1, 0)));
        println(ti.getIntersectionData());
    }

    public void setup() {
        size(800, 600, OPENGL);
    }
}
