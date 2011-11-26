/**
 * <p>Demonstration of the core features of the Spline2D/3D classes:</p>
 * 
 * <ul>
 * <li>Automatic joining of bezier segments</li>
 * <li>Computation of points along the curve inbetween the given control points</li>
 * <li>Sampling of the curve at a fixed/uniform interval</li>
 * </ul>
 */

/* 
 * Copyright (c) 2010 Karsten Schmidt
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * http://creativecommons.org/licenses/LGPL/2.1/
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

import toxi.geom.*;

int RES=32;

void setup() {
  size(300,500);
  smooth();
  textFont(createFont("SansSerif",10));
}

void draw() {
  background(255);

  float x=map(mouseX,0,width,80,150);
  Spline2D s=new Spline2D();
  s.add(new Vec2D(60,100));
  s.add(new Vec2D(60,0));
  s.add(new Vec2D(x,0));
  s.add(new Vec2D(x,100));
  s.add(new Vec2D(200,0));
  s.add(new Vec2D(200,100));

  translate(50,20);
  stroke(255,0,0);
  fill(255,0,0);
  text("control points",-40,0);
  noFill();
  beginShape();
  for(Vec2D v : s.pointList) {
    vertex(v.x, v.y);
  }
  endShape();

  translate(0,160);
  fill(0);
  text("tweened vertices",-40,0);
  noFill();
  int c=0;
  LineStrip2D strip=s.toLineStrip2D(RES);
  for(Vec2D p : strip) {
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
  for(Vec2D p : strip.getDecimatedVertices(20,false)) {
    line(p.x-2,p.y,p.x+2,p.y);
    line(p.x,p.y-2,p.x,p.y+2);
  }
}
