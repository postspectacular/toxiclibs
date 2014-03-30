/**
 * This sketch demonstrates the effect & usage of the curve interpolation
 * tightnessparameter added in release 0014 of the toxiclibscore package.
 */

/* 
 * Copyright (c) 2006-2009 Karsten Schmidt
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

float MAX_IMPACT = 0.5;

void setup() {
  size(400,400);
  smooth();
  noFill();
}

void draw() {
  background(255);

  Vec2D[] points=new Vec2D[11];
  stroke(255,0,0,100);
  beginShape();
  for(int i=0; i<points.length; i++) {
    points[i]=new Vec2D(100,(i+0.5)/10.0*TWO_PI).toCartesian().add(width/2,height/2);
    vertex(points[i].x,points[i].y);
  }
  endShape();

  // highlight the positions of the points with circles
  stroke(0);
  for(int i=0; i<points.length; i++) {
    ellipse(points[i].x,points[i].y,5,5);    
  }

  // calc tightness based on vertical mouse position (centre = linear)
  float tight=(mouseY-height/2.0)/(height/2.0)*MAX_IMPACT;
  Spline2D spline=new Spline2D(points,null,tight);

  // sample the curve at a higher resolution
  // so that we get extra points between each original pair of points
  LineStrip2D vertices = spline.toLineStrip2D(32);

  // draw the smoothened curve
  beginShape();
  for(Vec2D v : vertices) {
    vertex(v.x,v.y);
  }
  endShape();
}
