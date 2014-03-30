import toxi.geom.*;
import toxi.processing.*;
import java.util.List;

List<Vec2D> points = new ArrayList<Vec2D>();
Rect bounds=new Rect(200,200,0,0);

ToxiclibsSupport gfx;

void setup() {
  size(400,400);
  smooth();
  gfx=new ToxiclibsSupport(this);
}

void draw() {
  background(255);
  noFill();
  stroke(0);
  gfx.rect(bounds);
  fill(255,0,0);
  noStroke();
  for(Vec2D p : points) {
    gfx.circle(p,5);
  }
}

void mousePressed() {
  Vec2D p=new Vec2D(mouseX,mouseY);
  points.add(p);
  bounds.growToContainPoint(p);
}
