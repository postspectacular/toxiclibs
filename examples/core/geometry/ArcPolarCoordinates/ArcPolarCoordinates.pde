/**
 * Quick demo & test for working with 2D polar coordinates, interpolation and
 * circle segments. Might be useful for some...
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

float theta=0;
float innerRadius=150;
float outerRadius=180;
int numSteps=10;

void setup() {
  size(400,400);
}

void draw() {
  background(200);
  translate(width/2,height/2);
  // translate mouse position into polar coordinates
  // in the polar space the vector components are interpreted as:
  // x = radius
  // y = angle
  Vec2D mousePos=new Vec2D(mouseX,mouseY).sub(width/2,height/2).toPolar();
  // ensure we always interpolate the angle over the smaller difference
  if (abs(theta-mousePos.y)>PI) {
    if (theta>mousePos.y) {
      theta-=TWO_PI;
    } 
    else {
      mousePos.y-=TWO_PI;
    }
  }
  // interpolate to the new angle, adaptive speed based on current velocity
  theta+=(mousePos.y-theta)*abs(mousePos.y-theta)*0.2;
  // avoid "over-spinning"
  theta%=TWO_PI;
  // create the arc as tri strip
  noStroke();
  beginShape(TRIANGLE_STRIP);
  Vec2D p=new Vec2D();
  for(float i=0,t=theta-PI*0.25; i<numSteps; i++) {
    // convert theta back into cartesian coordinate space
    // the radius of 1 means the resulting vector will be normalized
    p.set(1,t).toCartesian();
    // scale point to inner/outer radius
    vertex(p.x*innerRadius,p.y*innerRadius);
    vertex(p.x*outerRadius,p.y*outerRadius);
    t+=HALF_PI/numSteps;
  }
  endShape();
  // draw comparison vector to mouse vs. arc positions
  mousePos.toCartesian();
  stroke(255,0,0);
  line(0,0,mousePos.x,mousePos.y);
  Vec2D arcPos=new Vec2D(innerRadius,theta).toCartesian();
  stroke(0,0,255);
  line(0,0,arcPos.x,arcPos.y);
}

