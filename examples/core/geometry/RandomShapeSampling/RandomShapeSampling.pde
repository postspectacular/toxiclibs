import toxi.geom.*;
import toxi.color.*;
import toxi.processing.*;

// container to store shapes with an associated color
HashMap<Shape2D,ReadonlyTColor> shapes=new HashMap<Shape2D,ReadonlyTColor>();

// helper class for rendering
ToxiclibsSupport gfx;

void setup() {
  size(680, 382);
  smooth();
  noFill();
  background(255);
  gfx=new ToxiclibsSupport(this);
  // define pairs of shapes & colors
  // here we're making use of polymorphism since
  // each of these classes implements the Shape2D interface
  shapes.put(new Circle(new Vec2D(100, 100), 80), TColor.RED);
  shapes.put(new Ellipse(new Vec2D(300, 100), new Vec2D(80, 50)), TColor.GREEN);
  shapes.put(new Triangle2D(new Vec2D(400, 20), new Vec2D(650, 100), new Vec2D(500, 180)), TColor.BLUE);
  shapes.put(Rect.fromCenterExtent(new Vec2D(200,280), new Vec2D(100,60)), TColor.CYAN);
  shapes.put(new Circle(new Vec2D(450,280),80).toPolygon2D(5), TColor.MAGENTA);
  // draw shape outlines
  for (Shape2D s : shapes.keySet()) {
    gfx.polygon2D(s.toPolygon2D());
  }
}

void draw() {
  noStroke();
  // randomly sample all shapes
  for (Shape2D s : shapes.keySet()) {
    // use color associated with shape
    // and create a slightly modified/varying version for each dot
    fill(shapes.get(s).getAnalog(0.5,0.5).toARGB());
    gfx.circle(s.getRandomPoint(), 5);
  }
}

