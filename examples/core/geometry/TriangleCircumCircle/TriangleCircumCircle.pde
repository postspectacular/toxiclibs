/**
 * This demo shows how to construct a circle through 3 points by computing
 * the triangles circumcircle (a circle defined for any triangle and which
 * goes through all its points)
 *
 * <p><strong>Usage:</strong><br/>
 * Move mouse to adjust size/shape of triangle
 * </p>
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
  size(680, 382);
  gfx = new ToxiclibsSupport(this);
  noFill();
}

void draw() {
  background(255);
  Vec2D m=new Vec2D(mouseX, mouseY);
  Vec2D o=new Vec2D(width/2,height/2);
  Vec2D n=m.sub(o).perpendicular().normalizeTo(100);
  Triangle2D t = new Triangle2D(o.sub(n),m,o.add(n));
  stroke(255,0,0);
  gfx.triangle(t, true);
  stroke(0,224,255);
  gfx.ellipse(t.getCircumCircle());
}
