/**
 * <p>
 * In this example we calculate the intersection point of two lines, one of which
 * can be moved by user interactively.
 * </p>
 *
 * <p><strong>Usage:</strong><ul>
 * <li>move mouse to move endpoint of line</li>
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
import toxi.processing.*;

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
