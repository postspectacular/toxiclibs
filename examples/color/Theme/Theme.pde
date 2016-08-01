/**
 * ColorTheme demo showing the following:
 * - construction of TColorthemes via textual descriptions of shades and colors
 * - adding an random element to the theme
 *
 * Press any key to re-generate a random variation of the color theme
 *
 * @author Karsten Schmidt <info at postspectacular dot com>
 */

/* 
 * Copyright (c) 2009 Karsten Schmidt
 * 
 * This demo & library is free software; you can redistribute it and/or
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
import toxi.util.datatypes.*;
import java.util.List;

float   XRAD = 300;
float   YRAD = 500;
int     RES = 6;
int     NUM_POINTS=6;

void setup() {
  size(1024, 768);
  noiseDetail(2);
  smooth();
  noLoop();
}

void draw() {
  noiseSeed(System.currentTimeMillis());
  // first define our new theme
  ColorTheme t = new ColorTheme("test");
  // add different color options, each with their own weight
  t.addRange("soft ivory", 0.5);
  t.addRange("intense goldenrod", 0.25);
  t.addRange("warm saddlebrown", 0.15);
  t.addRange("fresh teal", 0.05);
  t.addRange("bright yellow", 0.05);

  // now add another random hue which is using only bright shades
  t.addRange(ColorRange.BRIGHT, TColor.newRandom(), random(0.02, 0.05));

  // use the TColortheme to create a list of 160 colors
  ColorList list = t.getColors(160);

  background(list.getLightest().toARGB());
  drawSpline(list);
  //saveFrame("theme-"+(System.currentTimeMillis()/1000)+".png");
}

void keyPressed() {
  redraw();
}

void drawSpline(ColorList list) {
  noStroke();
  float numCols = list.size();
  Vec2D[] points=new Vec2D[NUM_POINTS];
  points[0]=new Vec2D(-XRAD,random(0.2,0.9)*height);
  for(int i=1; i<points.length-1; i++) {
    points[i]=new Vec2D(random(-1,1)*50+(float)i/points.length*width,random(0.25,0.75)*height);
  }
  points[points.length-1]=new Vec2D(width+XRAD,random(height));
  
  List<Vec2D> vertices=new Spline2D(points).getPointList();
  for(Vec2D v: vertices) {
    fill(list.get((int) random(numCols)).toARGB());
    ellipse(v.x,v.y,noise(v.y*0.01)*XRAD,noise(v.x*0.01)*YRAD);
  }
}