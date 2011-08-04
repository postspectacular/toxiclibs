package toxi.test;

import processing.core.PApplet;
import toxi.geom.Circle;
import toxi.geom.ConvexPolygonClipper;
import toxi.geom.Polygon2D;
import toxi.geom.SutherlandHodgemanClipper;
import toxi.geom.Vec2D;
import toxi.processing.ToxiclibsSupport;

public class ClipTest extends PApplet {

    private ToxiclibsSupport gfx;
    private SutherlandHodgemanClipper rectClipper;
    private ConvexPolygonClipper convexClipper;

    private boolean useConvex = true;
    private boolean showBounds = true;

    public void draw() {
        background(51);
        noFill();
        Polygon2D poly = new Circle(new Vec2D(mouseX, mouseY), 100)
                .toPolygon2D(12);
        if (showBounds) {
            if (useConvex) {
                stroke(255, 0, 0);
                gfx.polygon2D(convexClipper.getBounds());
            } else {
                stroke(255, 0, 255);
                gfx.rect(rectClipper.getBounds());
            }
            stroke(255, 255, 0);
            gfx.polygon2D(poly);
        }
        fill(255, 128, 0);
        noStroke();
        Polygon2D clipped;
        if (useConvex) {
            clipped = convexClipper.clipPolygon(poly);
        } else {
            clipped = rectClipper.clipPolygon(poly);
        }
        gfx.polygon2D(clipped);
        int id = 0;
        fill(208);
        for (Vec2D v : clipped.vertices) {
            text(id++, v.x, v.y);
        }
    }

    public void keyPressed() {
        if (key == 'c') {
            useConvex = !useConvex;
        }
        if (key == 'b') {
            showBounds = !showBounds;
        }
    }

    public void setup() {
        size(400, 400);
        gfx = new ToxiclibsSupport(this);
        Polygon2D bounds = new Polygon2D();
        bounds.add(new Vec2D(100, 100));
        bounds.add(new Vec2D(150, 80));
        bounds.add(new Vec2D(300, 130));
        bounds.add(new Vec2D(320, 300));
        bounds.add(new Vec2D(200, 220));
        bounds.center(bounds.getBounds().getCentroid());
        rectClipper = new SutherlandHodgemanClipper(bounds.getBounds());
        convexClipper = new ConvexPolygonClipper(bounds);
        textFont(createFont("SansSerif", 10));
    }
}
