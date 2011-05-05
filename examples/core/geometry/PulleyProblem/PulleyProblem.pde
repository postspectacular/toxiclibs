/**
 * <p>This is a solution to the well known Pulley Problem:
 * http://en.wikipedia.org/wiki/Belt_problem#Pulley_problem
 * Several features of the toxiclibs geometry classes are used
 * to compute the location of tangent points.</p>
 *
 * <p><strong>Usage:</strong><ul>
 * <li>move mouse to move one of the pulley wheels</li>
 * </ul></p>
 */

/* 
 * Copyright (c) 2011 Karsten Schmidt
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
import toxi.processing.*;

// define pulley wheels
Circle c1=new Circle(140,190,100);
Circle c2=new Circle(340,190,50);

ToxiclibsSupport gfx;

void setup() {
  size(680,382);
  noFill();
  gfx=new ToxiclibsSupport(this);
}

void draw() {
  background(255);
  // lock circle to mouse
  c1.set(mouseX,mouseY);
  // length between circle centers
  float len=c1.distanceTo(c2);
  // compute angle between circle centers
  float theta=c1.sub(c2).heading();
  // compute angle of tangent point
  float phi=acos((c2.getRadius()-c1.getRadius())/len);
  // compute tangent point on c1 using polar coordinates
  // adding theta as offset to take into account direction between wheels
  Vec2D c1A=pointOnCircle(c1,phi+theta);
  // 2nd tangent point is flipped (TWO_PI-phi)
  Vec2D c1B=pointOnCircle(c1,TWO_PI-phi+theta);
  // compute 2nd set of tangent points for c2
  Vec2D c2A=pointOnCircle(c2,phi+theta);  
  Vec2D c2B=pointOnCircle(c2,TWO_PI-phi+theta);
  // draw ropes
  gfx.line(c1A,c2A);
  gfx.line(c1B,c2B);
  // draw wheels
  gfx.ellipse(c1);
  gfx.ellipse(c2);
}

// computes a point at the given angle & circle
Vec2D pointOnCircle(Circle c, float theta) {
  return c.add(new Vec2D(c.getRadius(),theta).toCartesian());
}
