/**
 * <p>DLASpiral shows the general usage pattern for the current implementation of the
 * Diffusion-limited Aggregation process. Unlike the standard unguided DLA growth,
 * this implementation is utilizing line segments to guide & control the growth process.
 * The guidelines in this demo are forming a spiral extruded along the Z-axis and along
 * which the growth will happen.</p>
 * <p>The DLA process also emits a number of different events to which a client application
 * can subscribe to. The package provides an event adapter class (see Adapter design pattern)
 * which is also used in this demo to trigger the automatic saving of all particles when the
 * spiral has grown to full size/is complete.</p>
 * <p>The last key feature of the demo deals with the visualization of the octree structure
 * underlying the DLA simulation. Both the tree structure and particle contents are shown.</p>
 *
 * <p><em>Please note that DLA is an extremely slow & resource intensive process and can take
 * several hours to complete. You should also increase your max. available memory setting in
 * Processing to be able to store the possibly several million particles.</em></p>
 * 
 * <p><strong>Usage:</strong><ul>
 * <li>move mouse to rotate view</li>
 * <li>- / = : adjust zoom</li>
 * <li>o : toggle octree display</li>
 * <li>s : save current particles</li>
 * <li>r : restart simulation</li>
 * </ul></p>
 */

/* 
 * Copyright (c) 2010 Karsten Schmidt
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

import toxi.sim.dla.*;
import toxi.geom.*;

import processing.opengl.*;

DLA dla;
DLAListener listener;

float currScale = 6;
boolean isOctreeVisible=true;

void setup() {
  size(640, 480, OPENGL);
  // compute spiral key points (every 45 degrees)
  ArrayList points = new ArrayList();
  for (float theta=-TWO_PI, r=20; theta<3*TWO_PI; theta+=QUARTER_PI) {
    Vec3D p=Vec3D.fromXYTheta(theta).scale(r);
    p.z=theta*4;
    points.add(p);
    r *= 0.92;
  }
  // use points to compute a spline and
  // use resulting segments as DLA guidelines
  DLAGuideLines guides = new DLAGuideLines();
  guides.addCurveStrip(new Spline3D(points).computeVertices(8));
  // create DLA 3D simulation space 128 units wide (cubic)
  dla = new DLA(128);
  // use default configuration
  dla.setConfig(new DLAConfiguration());
  // add guide lines
  dla.setGuidelines(guides);
  // set leaf size of octree
  dla.getParticleOctree().setMinNodeSize(1);
  // add a listener for simulation events
  listener=new DLAListener();
  dla.addListener(listener);
  textFont(createFont("SansSerif", 12));
}

void draw() {
  background(255);
  // DLA is a *VERY* slow process so we need to
  // compute a large number of iterations each frame
  dla.update(10000);
  fill(0);
  text("particles: " + dla.getNumParticles(), 20, 20);
  translate(width / 2, height / 2, 0);
  rotateX(mouseY * 0.01f);
  rotateY(mouseX * 0.01f);
  scale(currScale);
  // draw growth progress and guide particles
  drawOctree(dla.getParticleOctree(), isOctreeVisible, 0xffff0000);
  drawOctree(dla.getGuideOctree(), false, 0xff0000ff);
}

// this method recursively paints an entire octree structure
void drawOctree(PointOctree node, boolean doShowGrid, int col) {
  if (doShowGrid) {
    drawBox(node);
  }
  if (node.getNumChildren() > 0) {
    PointOctree[] children = node.getChildren();
    for (int i = 0; i < 8; i++) {
      if (children[i] != null) {
        drawOctree(children[i], doShowGrid, col);
      }
    }
  } 
  else {
    java.util.List points = node.getPoints();
    if (points != null) {
      stroke(col);
      beginShape(POINTS);
      int numP = points.size();
      for (int i = 0; i < numP; i += 10) {
        Vec3D p = (Vec3D)points.get(i);
        vertex(p.x, p.y, p.z);
      }
      endShape();
    }
  }
}

void drawBox(PointOctree node) {
  noFill();
  stroke(0, 24);
  pushMatrix();
  translate(node.x, node.y, node.z);
  box(node.getSize());
  popMatrix();
}

void keyPressed() {
  if (key == '-') {
    currScale -= 0.25f;
  }
  if (key == '=') {
    currScale += 0.25f;
  }
  if (key=='o') {
    isOctreeVisible=!isOctreeVisible;
  }
  if (key == 's') {
    listener.save();
  }
}

class DLAListener extends DLAEventAdapter {

  // this method will be called when all guide segments
  // have been processed
  public void dlaAllSegmentsProcessed(DLA dla) {
    println("all done, saving...");
    save();
  }

  public void save() {
    dla.save(sketchPath("spiral.dla"), false);
  }
}

