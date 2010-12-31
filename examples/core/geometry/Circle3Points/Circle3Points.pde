/**
 * <p>
 * This example demonstrates the construction of a circle through 3 given points.
 * You can specify one of the points using your mouse, the other two are moved automatically.
 * </p>
 *
 * <p><strong>Usage:</strong><ul>
 * <li>move mouse to move one of the 3 points</li>
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
import toxi.math.waves.*;
import toxi.processing.*;

ToxiclibsSupport gfx;

AbstractWave wave1=new SineWave(0,0.02,100,200);
AbstractWave wave2=new SineWave(0,0.023,100,200);

void setup() {
  size(680, 382);
  smooth();
  stroke(#330077);
  noFill();
  gfx = new ToxiclibsSupport(this);
}

void draw() {
  background(#cceeff);
  Vec2D p1 = new Vec2D(200, wave1.update());
  Vec2D p2 = new Vec2D(400, wave2.update());
  Vec2D p3 = new Vec2D(mouseX, mouseY);
  Circle circle = Circle.from3Points(p1, p2, p3);
  if (circle != null) {
    gfx.ellipse(circle);
    gfx.circle(p1, 3);
    gfx.circle(p2, 3);
    gfx.circle(p3, 3);
  }
}

