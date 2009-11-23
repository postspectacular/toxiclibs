/**
 * A little doodle demo using the Spline class to compress & smooth mouse inputs.
 * Points are recorded at a fixed interval (distance) and used as handles for a
 * continous curve.
 * 
 * Key controls:
 * h - toggle spline handles on/off
 * s - toggle display of smoothed spline
 * l - toggle display of raw linear connection between handles (to compare with curvature)
 * any other key clears the canvas/history
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

ArrayList points=new ArrayList();

// desired distance between points/handles
int sampleDistance=50;

boolean showLine=true;
boolean showSpline=true;
boolean showHandles=true;

void setup() {
  size(600,600);
  smooth();
}

void draw() {
  background(255);
  noFill();
  int numP=points.size();
  Vec2D currP=new Vec2D(mouseX,mouseY);
  if (numP>0) {
    // check distance to previous point
    Vec2D prevP=(Vec2D)points.get(numP-1);
    if (currP.distanceTo(prevP)>sampleDistance) {
      points.add(currP);
    }
  } 
  else {
    // add first point regardless
    points.add(currP);
  }
  numP=points.size();
  if (showLine) {
    stroke(255,0,0,50);
    beginShape();
    for(int i=0; i<numP; i++) {
      Vec2D p=(Vec2D)points.get(i);
      vertex(p.x,p.y);
    }
    endShape();
  }

  stroke(0);
  // highlight the positions of the points with circles
  Vec2D[] handles=new Vec2D[numP];
  for(int i=0; i<numP; i++) {
    Vec2D p=(Vec2D)points.get(i);
    handles[i]=p;
    if (showHandles) ellipse(p.x,p.y,5,5);    
  }

  // need at least 4 vertices for a spline
  if (numP>3 && showSpline) {
    // pass the points into the Spline container class
    Spline2D spline=new Spline2D(handles);
    // sample the curve at a higher resolution
    // so that we get extra 8 points between each original pair of points
    java.util.List vertices=spline.computeVertices(8);
    // draw the smoothened curve
    beginShape();
    for(Iterator i=vertices.iterator(); i.hasNext(); ) {
      Vec2D v=(Vec2D)i.next();
      vertex(v.x,v.y);
    }
    endShape();
  }
}

void keyPressed() {
  if (key=='h') showHandles=!showHandles;
  else if (key=='l') showLine=!showLine;
  else if (key=='s') showSpline=!showSpline;
  else points.clear();
}

