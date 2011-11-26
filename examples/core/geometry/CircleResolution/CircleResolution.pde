import toxi.geom.*;
import toxi.processing.*;

ToxiclibsSupport gfx;

void setup() {
  size(600,600);
  smooth();
  gfx=new ToxiclibsSupport(this);
}

void draw() {
  background(0);
  noStroke();
  int res=(int)map(mouseX,0,width,3,72);
  Polygon2D poly=new Circle(new Vec2D(width/2,height/2),200).toPolygon2D(res);
  gfx.polygon2D(poly);
  text(res,20,20);
}

