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
  fill(255);
  int res=(int)map(mouseX,0,width,3,72);
  Polygon2D poly=new Circle(new Vec2D(width/2,height/2),200).toPolygon2D(res);
  gfx.polygon2D(poly);
  fill(255,0,0);
  for(Vec2D v : poly) {
     gfx.circle(v,5); 
  }
  text(res,20,20);
}

