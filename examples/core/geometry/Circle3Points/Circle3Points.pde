import toxi.geom.*;
import toxi.math.waves.*;
import toxi.processing.*;

ToxiclibsSupport gfx;

AbstractWave wave1=new SineWave(0,0.02,100,200);
AbstractWave wave2=new SineWave(0,0.023,100,200);

void setup() {
  size(400, 400);
  smooth();
  stroke(#330077);
  noFill();
  gfx = new ToxiclibsSupport(this);
}

void draw() {
  background(#cceeff);
  Vec2D p1 = new Vec2D(100, wave1.update());
  Vec2D p2 = new Vec2D(300, wave2.update());
  Vec2D p3 = new Vec2D(mouseX, mouseY);
  Circle circle = Circle.from3Points(p1, p2, p3);
  if (circle != null) {
    gfx.ellipse(circle);
    gfx.circle(p1, 3);
    gfx.circle(p2, 3);
    gfx.circle(p3, 3);
  }
}

