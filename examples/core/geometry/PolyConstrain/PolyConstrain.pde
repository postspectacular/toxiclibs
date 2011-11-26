import toxi.geom.*;
import toxi.util.*;
import toxi.processing.*;

Polygon2D poly;

ToxiclibsSupport gfx;
boolean doSave;

void setup() {
  size(600, 600);
  gfx = new ToxiclibsSupport(this);
  poly = new Circle(200).toPolygon2D(9).translate(width / 2, height / 2);
  poly.get(0).x *= 0.66f;
}

void draw() {
  background(255);
  noFill();
  gfx.polygon2D(poly);
  fill(255, 0, 0);
  gfx.circle(new Vec2D(mouseX, mouseY).constrain(poly), 10);
  if (doSave) {
    saveFrame("PolyConstrain-" + DateUtils.timeStamp() + ".png");
    doSave = false;
  }
}

void keyPressed() {
  switch (key) {
  case ' ':
    doSave = true;
    break;
  }
}


