/**
 * <p>This simple example shows how to compute tangent points on a circle using
 * the new Circle and Line2D classes. The demo also shows how these points
 * are constructed geometrically.</p>
 *
 * <p><strong>Usage:</strong><ul>
 * <li>move mouse to move/recalculate tangents</li>
 * <li>click+drag mouse to adjust circle radius</li>
 * </ul></p>
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

// define a fixed circle at 300,300 with radius=100
Circle c=new Circle(300,300,100);

void setup() {
  size(600,600);
  noFill();
  ellipseMode(RADIUS);
}

void draw() {
  background(255);
  Vec2D p=new Vec2D(mouseX,mouseY);
  // adjust the main circle radius on mouse press
  if (mousePressed) {
    c.setRadius(p.distanceTo(c));
  }
  Line2D l=new Line2D(p,c);
  ellipse(c.x,c.y,c.getRadius(),c.getRadius());
  line(p,c);
  // compute the tangent points to P on the circle
  Vec2D[] isec=c.getTangentPoints(p);
  if (isec!=null) {
    for(int i=0; i<2; i++) {
      ellipse(isec[i].x,isec[i].y,5,5);
      Vec2D q=new Ray2D(p,isec[i].sub(p)).getPointAtDistance(1000);
      line(p,q);
      line(c,isec[i]);
    }
    // draw secondary circle around mid-point
    // from main circle to mouse position
    Vec2D m=l.getMidPoint();
    float r=l.getLength()/2;
    ellipse(m.x,m.y,r,r);
  }
}

void line(Vec2D a, Vec2D b) {
  line(a.x,a.y,b.x,b.y);
}



