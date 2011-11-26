/**
 * <p>This is a demonstration of the two available polygon clipping tools based
 * on the Sutherland-Hodgeman algorithm. As the name indicates, the ConvexPolygonClipper
 * class can clip a given polygon to the boundary of another (convex) polygon. Only the
 * boundary polygon needs to be convex for this to work. The SutherlandHodgemanClipper
 * is the original implementation of the algorithm and optimized for rectangular clipping
 * regions. You can toggle between both implementations dynamically.</p>
 *
 * <p><strong>Usage:</strong>
 * <ul>
 * <li>c: toggle between convex/rectangular clipping regions</li>
 * <li>b: toggle display of polygon boundaries/outlines</li>
 * <li>move mouse to move subject polygon</li>
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

SutherlandHodgemanClipper rectClipper;
ConvexPolygonClipper convexClipper;
ToxiclibsSupport gfx;

boolean useConvex = true;
boolean showBounds = true;

void setup() {
  size(400, 400);
  gfx = new ToxiclibsSupport(this);
  Polygon2D bounds = new Polygon2D();
  bounds.add(new Vec2D(100, 100));
  bounds.add(new Vec2D(150, 80));
  bounds.add(new Vec2D(300, 130));
  bounds.add(new Vec2D(320, 300));
  bounds.add(new Vec2D(200, 220));
  bounds.center(bounds.getBounds().getCentroid());
  rectClipper = new SutherlandHodgemanClipper(bounds.getBounds());
  convexClipper = new ConvexPolygonClipper(bounds);
  textFont(createFont("SansSerif", 10));
}

void draw() {
  background(51);
  noFill();
  Polygon2D poly = new Circle(new Vec2D(mouseX, mouseY), 100)
    .toPolygon2D(12);
  if (showBounds) {
    if (useConvex) {
      stroke(255, 0, 0);
      gfx.polygon2D(convexClipper.getBounds());
    } 
    else {
      stroke(255, 0, 255);
      gfx.rect(rectClipper.getBounds());
    }
    stroke(255, 255, 0);
    gfx.polygon2D(poly);
  }
  fill(255, 128, 0);
  noStroke();
  Polygon2D clipped;
  if (useConvex) {
    clipped = convexClipper.clipPolygon(poly);
  } 
  else {
    clipped = rectClipper.clipPolygon(poly);
  }
  gfx.polygon2D(clipped);
  int id = 0;
  fill(255);
  for (Vec2D v : clipped.vertices) {
    text(id++, v.x, v.y);
  }
}

void keyPressed() {
  if (key == 'c') {
    useConvex = !useConvex;
  }
  if (key == 'b') {
    showBounds = !showBounds;
  }
}

