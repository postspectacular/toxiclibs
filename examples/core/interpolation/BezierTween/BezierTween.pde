import toxi.geom.*;
import toxi.math.*;
import toxi.processing.*;

InterpolateStrategy tween=new DecimatedInterpolation(8);

ToxiclibsSupport gfx;
void setup() {
  size(400,400);
  gfx=new ToxiclibsSupport(this);
}

void draw() {
  background(255);
  noFill();
  Vec2D a= new Vec2D(0,3);
  Vec2D h1= new Vec2D(30,200);
  Vec2D h2= new Vec2D(60,-80);
  Vec2D b= new Vec2D(100,32);
  bezier(a.x,a.y,h1.x,h1.y,h2.x,h2.y,b.x,b.y);
  float t=mouseX/(float)width;
  float x=bezierPoint(a.x,h1.x,h2.x,b.x,t);
  float y=bezierPoint(a.y,h1.y,h2.y,b.y,t);
  ellipse(x,y,5,5);
  
  Polygon2D poly=new Circle(new Vec2D(width/2,height/2),100).toPolygon2D(round(y));
  gfx.polygon2D(poly);
}

