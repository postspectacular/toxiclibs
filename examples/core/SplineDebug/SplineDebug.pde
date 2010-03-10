import toxi.geom.*;

int RES=32;

size(300,500);
background(255);
smooth();
textFont(createFont("SansSerif",10));

Spline2D s=new Spline2D();
s.add(new Vec2D(60,100));
s.add(new Vec2D(60,0));
s.add(new Vec2D(100,0));
s.add(new Vec2D(100,100));
s.add(new Vec2D(200,0));
s.add(new Vec2D(200,100));

translate(50,20);
stroke(255,0,0);
fill(255,0,0);
text("control points",-40,0);
noFill();
beginShape();
for(Iterator i=s.pointList.iterator(); i.hasNext();) {
  Vec2D v=(Vec2D)i.next();
  vertex(v.x, v.y);
}
endShape();

translate(0,160);
fill(0);
text("tweened vertices",-40,0);
noFill();
int c=0;
for(Iterator i=s.computeVertices(RES).iterator(); i.hasNext();) {
  Vec2D p=(Vec2D)i.next();
  if (0 == c % RES) stroke(255,0,0);
  else stroke((c % RES)*(255f/RES));
  ellipse(p.x,p.y,5,5);
  c++;
}

translate(0,160);
stroke(0,0,255);
fill(0,0,255);
text("fixed interval",-40,0);
noFill();
for(Iterator i=s.getDecimatedVertices(20).iterator(); i.hasNext();) {
  Vec2D p=(Vec2D)i.next();
  line(p.x-2,p.y,p.x+2,p.y);
  line(p.x,p.y-2,p.x,p.y+2);
}

