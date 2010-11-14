import toxi.processing.*;

import toxi.math.conversion.*;
import toxi.geom.*;
import toxi.math.*;
import toxi.geom.mesh2d.*;
import toxi.util.datatypes.*;
import toxi.util.events.*;
import toxi.geom.mesh.*;
import toxi.math.waves.*;
import toxi.util.*;
import toxi.math.noise.*;

ToxiclibsSupport gfx;

void setup() {
  size(400,400);
  smooth();
  textSize(9);
  gfx=new ToxiclibsSupport(this);
}

void draw() {
  background(255);
  Line2D l=new Line2D(new Vec2D(50,50), new Vec2D(350,350));
  Line2D m=new Line2D(new Vec2D(350,200), new Vec2D(mouseX,mouseY));
  Line2D.LineIntersection i=l.intersectLine(m);
  if (i.getType()!=Line2D.LineIntersection.Type.NON_INTERSECTING) {
    Vec2D isec=i.getPos();
    stroke(255,0,192);
    fill(255,0,192);
    ellipse(isec.x,isec.y,5,5);
    textAlign(isec.x>width/2 ? RIGHT : LEFT);
    text(isec.toString(),isec.x,isec.y-10);
  } else {
    stroke(0);
  }
  gfx.line(l);
  gfx.line(m);
}
