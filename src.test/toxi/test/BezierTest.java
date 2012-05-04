package toxi.test;

import processing.core.PApplet;
import toxi.geom.BezierCurve2D;
import toxi.geom.Vec2D;
import toxi.processing.ToxiclibsSupport;
import toxi.util.DateUtils;

public class BezierTest extends PApplet {

    public static void main(String[] args) {
        PApplet.main(new String[] {
            "toxi.test.BezierTest"
        });
    }

    private ToxiclibsSupport gfx;

    private boolean doSave;

    private BezierCurve2D bezier;

    public void draw() {
        background(255);
        noFill();
        bezier = new BezierCurve2D();
        Vec2D a = new Vec2D(300, 400);
        Vec2D b = new Vec2D(300, 100);
        Vec2D c = new Vec2D(mouseX, mouseY);
        Vec2D d = new Vec2D(600, 100);
        bezier.add(a).add(b).add(c).add(d);
        //
        Vec2D e = new Vec2D(600, 100);
        Vec2D f = new Vec2D(900, 100);
        Vec2D g = new Vec2D(900, 400);
        bezier.add(e).add(f).add(g);
        //
        bezier.add(new Vec2D(1100, 700));
        bezier.add(new Vec2D(600, 700));
        bezier.add(new Vec2D(600, 700));
        //
        bezier.add(new Vec2D(600, 700));
        bezier.add(new Vec2D(100, 700));
        bezier.add(new Vec2D(300, 400));
        bezier.alignAllHandles();
        stroke(0);
        fill(255, 255, 0);
        gfx.polygon2D(bezier.toPolygon2D(20));
        stroke(255, 0, 0);
        for (int i = 0, res = 10; i <= res; i++) {
            Vec2D p = BezierCurve2D.computePointInSegment(a, b, c, d, (float) i
                    / res);
            Vec2D t = BezierCurve2D.computeTangentInSegment(a, b, c, d,
                    (float) i / res).scaleSelf(50);
            gfx.line(p, p.add(t));
        }
        if (doSave) {
            saveFrame("BezierTest-" + DateUtils.timeStamp() + ".png");
            doSave = false;
        }
    }

    private void drawHandles(Vec2D a, Vec2D b, Vec2D c, Vec2D d) {
        gfx.line(a, b);
        gfx.line(c, d);
    }

    public void keyPressed() {
        switch (key) {
            case ' ':
                doSave = true;
                break;
        }
    }

    public void setup() {
        size(1280, 720, OPENGL);
        gfx = new ToxiclibsSupport(this);
    }
}
