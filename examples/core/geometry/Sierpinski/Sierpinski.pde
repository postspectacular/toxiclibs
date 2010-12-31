/**
 * <p>
 * This example demonstrates the construction of the Sierpinski triangle using
 * Michael Barnsley's Chaos Game method. This method relies on picking a random
 * start point inside a triangle, which in this case here is done using
 * barycentric coordinates to ensure the point is indeed inside, guaranteed.
 * </p>
 *
 * <p>Further information:
 * http://en.wikipedia.org/wiki/Sierpinski_triangle
 * http://en.wikipedia.org/wiki/Chaos_game
 * http://en.wikipedia.org/wiki/Barycentric_coordinate_system_(mathematics)
 * </p>
 *
 * <p><strong>Usage:</strong><ul>
 * <li>click to recreate another triangle</li>
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
import toxi.math.*;

Line2D l = new Line2D(new Vec2D(), new Vec2D());
Vec2D[] verts;
Vec2D p;

void setup() {
  size(680, 382, P3D);
  background(255);
  initTri();
}

void draw() {
  for (int i = 0; i < 1000; i++) {
    l.set(verts[MathUtils.random(3)], p);
    p = l.getMidPoint();
    point(p.x, p.y);
  }
}

void initTri() {
  Vec2D o = new Vec2D(width / 2, height / 2);
  Vec2D a = Vec2D.randomVector().scale(random(width / 2)).add(o);
  Vec2D b = Vec2D.randomVector().scale(random(width / 2)).add(o);
  Vec2D c = Vec2D.randomVector().scale(random(width / 2)).add(o);
  verts = new Vec2D[] { a, b, c };
  Triangle2D tri = new Triangle2D(a, b, c);
  // pick random barycentric point
  float r1 = random(1);
  float r2 = random(1 - r1);
  float r3 = 1 - (r2 + r1);
  // get actual point in cartesian space (will be inside triangle)
  p = tri.fromBarycentric(new Vec3D(r1, r2, r3));
}

void mousePressed() {
  initTri();
  background(255);
}

