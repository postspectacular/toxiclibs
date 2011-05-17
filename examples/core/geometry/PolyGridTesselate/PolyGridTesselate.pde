import toxi.geom.*;
import toxi.geom.mesh2d.*;
import toxi.geom.mesh.*;
import toxi.processing.*;

ToxiclibsSupport gfx;
Polygon2D poly;
GridTesselator tess=new GlobalGridTesselator(30);

boolean isGlobal=true;

void setup() {
  size(640, 480);
  smooth();
  gfx = new ToxiclibsSupport(this);
  poly=new Ellipse(200, 100).toPolygon2D(8);
}

void draw() {
  background(255);
  noFill();
  Polygon2D p=poly.copy().translate(new Vec2D(mouseX, mouseY));
  for (Triangle2D t : tess.tesselatePolygon(p)) {
    gfx.triangle(t);
  }
}

void mousePressed() {
  isGlobal=!isGlobal;
  if (isGlobal) {
    tess=new GlobalGridTesselator(30);
  } 
  else {
    tess=new LocalGridTesselator(10);
  }
}

