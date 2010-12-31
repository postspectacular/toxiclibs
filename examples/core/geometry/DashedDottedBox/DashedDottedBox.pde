/**
 * <p>
 * This example shows how to easily create & render dashed and dotted lines.
 * UPDATE: 2010-12-29 added marching ants effect to animate lines
 * </p>
 *
 * <p><strong>Usage:</strong><ul>
 * <li>press any key to toggle between dashed/dotted mode</li>
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
import toxi.geom.mesh.*;

List<Line3D> edges = new ArrayList<Line3D>();
// cube radius
float s=100;
// step size between points on each edge
float step=5;
// marching ants animation phase (will count from 0.0 ... 1.0)
float phase;

boolean isDashed=false;

void setup() {
  size(400,400,P3D);
  // create a cube mesh
  WETriangleMesh box=new WETriangleMesh();
  box.addMesh(new AABB(new Vec3D(),100).toMesh());
  // scan all edges and only pick out the major axes
  // put them all in our edge/line list for drawing later
  for(WingedEdge e : box.edges.values()) {
    if (e.getDirection().isMajorAxis(0.01)) {
      edges.add(e);
    }
  }
}

void draw() {
  background(0);
  translate(width/2,height/2,0);
  rotateX(mouseY*0.01);
  rotateY(mouseX*0.01);
  stroke(255);
  phase=(phase+0.05)%1;
  for(Line3D l : edges) {
    if (isDashed) {
      drawDashedLine(l,phase);
    } else {
      drawDottedLine(l,phase);
    }
  }
}

void drawDashedLine(Line3D l, float phase) {
  l=new Line3D(l.a.add(l.getDirection().normalizeTo(phase*step)),l.b);
  // compute inbetween points every "STEP" units and iterate over them
  List<Vec3D> points=l.splitIntoSegments(null,step,true);
  for(int i=0, num=points.size()-1; i<num; i+=2) {
    Vec3D p=points.get(i);
    Vec3D q=points.get(i+1);
    line(p.x,p.y,p.z,q.x,q.y,q.z);
  }
}

void drawDottedLine(Line3D l, float phase) {
  l=new Line3D(l.a.add(l.getDirection().normalizeTo(phase*step)),l.b);
  // compute inbetween points every "STEP" units and iterate over them
  for(Vec3D p : l.splitIntoSegments(null,step,true)) {
    point(p.x,p.y,p.z);
  }
}

void keyPressed() {
  isDashed=!isDashed;
}
