/**
 * <p>Demonstration of the Polygon2D.smooth() function, applying a low pass filter
 * to the vertex positions of polygons in order to reduce their spatial contrast/sharpness
 * and slowly approach a rounder form.</p>
 * 
 * <p><strong>Usage</strong>:<br/>
 * Click anywhere to add a polygon</p>
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
 
import toxi.color.*;
import toxi.geom.*;
import toxi.processing.*;
import java.util.List;

// number of vertices in each polygon
int num=30;

List<ColoredPolygon> polygons = new ArrayList<ColoredPolygon>();

ToxiclibsSupport gfx;

void setup() {
  size(680,382);
  noStroke();
  smooth();
  gfx=new ToxiclibsSupport(this);
}

void draw() {
  background(255);
  // iterate over all polygon created so far
  for(ColoredPolygon p : polygons) {
    // apply vertex smoothing
    p.smooth(0.01,0.05);
    // and draw
    fill(p.col.toARGB());
    gfx.polygon2D(p);
  }
}

// create a new polygon around the mouse position using a random radius for each vertex
void mousePressed() {
  // pick a random bright color and set its alpha to 50-80%
  TColor col=ColorRange.BRIGHT.getColor().setAlpha(random(0.5,0.8));
  // add randomized vertices
  ColoredPolygon poly=new ColoredPolygon(col);
  float radius=random(50,200);
  for(int i=0; i<num; i++) {
    poly.add(Vec2D.fromTheta((float)i/num*TWO_PI).scaleSelf(random(0.2,1)*radius).addSelf(mouseX,mouseY));
  }
  // add poly to list of polygons
  polygons.add(poly);
}

// extend the standard Polygon2D class to include color information
class ColoredPolygon extends Polygon2D {
  ReadonlyTColor col;
  
  public ColoredPolygon(ReadonlyTColor col) {
    this.col=col;
  }
}
