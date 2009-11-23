import toxi.geom.*;

int steps=8;

size(300,300);
background(255);
noFill();
smooth();

translate(50,100);

Spline2D s=new Spline2D();
s.add(new Vec2D(0,100));
s.add(new Vec2D(50,0));
s.add(new Vec2D(100,0));
s.add(new Vec2D(100,100));
s.add(new Vec2D(200,0));
s.add(new Vec2D(200,100));

stroke(255,0,0);
beginShape();
for(int i=0; i<s.pointList.size(); i++) {
  vertex(s.pointList.get(i).x,s.pointList.get(i).y);
}
endShape();
java.util.List v=s.computeVertices(8);
println("num verts: "+v.size());

int c=0;
Vec2D prev=null;
for(Iterator i=v.iterator(); i.hasNext();) {
  Vec2D p=(Vec2D)i.next();
  if (0==c%steps) stroke(255,0,0);
  else stroke((c%steps)*32);
  ellipse(p.x,p.y,5,5);
  println(c+": "+p);
  prev=p;
  c++;
}
