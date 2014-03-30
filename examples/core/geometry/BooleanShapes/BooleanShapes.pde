import toxi.geom.*;
import toxi.processing.*;
import toxi.color.*;
import toxi.geom.BooleanShapeBuilder.*;

import java.util.List;

ToxiclibsSupport gfx;

void setup() {
  size(400, 400);
  gfx = new ToxiclibsSupport(this);
}

void draw() {
  background(160);
  BooleanShapeBuilder builder = new BooleanShapeBuilder(
  BooleanShapeBuilder.Type.UNION);
  float phi = frameCount * 0.01f;
  builder.addShape(new Circle(mouseX, mouseY, 50));
  builder.addShape(new Ellipse(150, 130 + sin(phi) * 50, 120, 60));
  builder.addShape(new Rect(200 + sin(phi * 13 / 8) * 50, 180, 100, 100));
  builder.addShape(Triangle2D.createEquilateralFrom(new Vec2D(
  50 + sin(phi * 15 / 13) * 50, 200), new Vec2D(300, 200)));
  builder.addShape(new Circle(100, 300, 50 + 30 * sin(phi * 21 / 15))
    .toPolygon2D(6));
  noFill();
  stroke(255, 0, 0);
  List<Polygon2D> polies = builder.computeShapes();
  for (Polygon2D p : polies) {
    gfx.polygon2D(p);
  }
  noStroke();
  Vec2D p = new Vec2D();
  for (int y = 10; y < height; y += 20) {
    for (int x = 10; x < height; x += 20) {
      gfx.fill(pointInShapes(p.set(x, y), polies) ? TColor.GREEN
        : TColor.BLUE);
      gfx.circle(p, 3);
    }
  }
}

boolean pointInShapes(Vec2D p, List<Polygon2D> polies) {
  for (Polygon2D poly : polies) {
    if (poly.containsPoint(p)) {
      return true;
    }
  }
  return false;
}


