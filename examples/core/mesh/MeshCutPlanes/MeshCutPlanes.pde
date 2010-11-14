/**
 * <p>This demo shows how to filter a mesh using VertexSelectors and
 * manipulate the resulting vertex selections using boolean set operations.
 * The selector implementation used is selecting vertices on a given side
 * of cut plane (here all vertices in front). The example is using two of
 * such selectors whose results are merged and the final selection removed
 * from the mesh. Both the resulting mesh and cut planes are visualized.</p>
 *
 * <p>Furthermore, the example mesh itself is generated using subdivision
 * modelling, starting from a simple cube.</p>
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

import processing.opengl.*;

import toxi.processing.*;

import toxi.geom.*;
import toxi.geom.mesh.subdiv.*;
import toxi.geom.mesh.*;

ToxiclibsSupport gfx;
WETriangleMesh mesh;

void setup() {
  size(680,382, OPENGL);
  gfx = new ToxiclibsSupport(this);
  mesh = (WETriangleMesh) new AABB(100).toMesh(new WETriangleMesh());
  MidpointSubdivision sd = new MidpointSubdivision();
  mesh.subdivide(sd, 0);
  mesh.subdivide(sd, 0);
  mesh.subdivide(sd, 0);
  MidpointDisplacementSubdivision sd2 = new MidpointDisplacementSubdivision(new Vec3D(),-0.5f);
  mesh.subdivide(sd2, 0);
}

public void draw() {
  background(0);
  lights();
  translate(width / 2, height / 2, 0);
  rotateX((height / 2 - mouseY) * 0.01f);
  rotateY((width / 2 - mouseX) * 0.01f);
  gfx.origin(300);
  noStroke();
  WETriangleMesh filteredMesh = new WETriangleMesh().addMesh(mesh);
  PlaneSelector sel = new PlaneSelector(filteredMesh, new Plane(new Vec3D(), new Vec3D(0, 1, 0).rotateX(frameCount * 0.02f)), Plane.Classifier.FRONT);
  PlaneSelector sel2 = new PlaneSelector(filteredMesh, new Plane(new Vec3D(), new Vec3D(1, 1, 0).rotateX(frameCount * 0.04f + HALF_PI)), Plane.Classifier.FRONT);
  sel.selectVertices();
  sel2.selectVertices();
  sel.addSelection(sel2);
  filteredMesh.removeVertices(sel.getSelection());
  fill(0, 255, 255);
  gfx.mesh(filteredMesh, false);
  fill(255,0,255);
  gfx.plane(sel.plane,300);
  fill(255,255,0);
  gfx.plane(sel2.plane,300);
}

