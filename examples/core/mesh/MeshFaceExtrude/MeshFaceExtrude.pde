/**
 * <p>Manual mesh face extrusion</p>
 *
 * <p><strong>Usage:</strong>
 * <ul>
 * <li>f - toggle filled/wireframe display</li>
 * <li>a/d - move extruded face along X axis</li>
 * <li>w/s - moved face along Y</li>
 * <li>z/x - move face along Z</li>
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
 
import toxi.processing.*;

import toxi.geom.*;
import toxi.geom.mesh.*;

TriangleMesh mesh;
boolean isFilled = true;

ToxiclibsSupport gfx;

void setup() {
  size(680, 382, P3D);
  gfx = new ToxiclibsSupport(this);
  // create a mesh from the axis-aligned bounding box (AABB)
  mesh = (TriangleMesh)new AABB(new Vec3D(), 100).toMesh();
  // get first face/triangle of mesh
  Face f = mesh.faces.get(0);
  // extrude along positive Z axis and shrink to 25% of original size
  // shrinking is done by moving vertices towards centroid of the face
  // before extruding
  float shrink=0.25;
  Vec3D centroid = new Triangle3D(f.a, f.b, f.c).computeCentroid();
  Vec3D extrude = new Vec3D(0, 0, 100);
  Vec3D a = f.a.interpolateTo(centroid, 1-shrink).add(extrude);
  Vec3D b = f.b.interpolateTo(centroid, 1-shrink).add(extrude);
  Vec3D c = f.c.interpolateTo(centroid, 1-shrink).add(extrude);
  // begin by adding new side faces:
  // side A
  mesh.addFace(f.a, a, f.c);
  mesh.addFace(a, c, f.c);
  // side B
  mesh.addFace(f.a, b, a);
  mesh.addFace(f.a, f.b, b);
  // side C
  mesh.addFace(f.c, c, f.b);
  mesh.addFace(c, b, f.b);
  // remove original face
  mesh.faces.remove(0);
  // add new face as cap
  mesh.addFace(a, b, c);
  // update normals (for shading)
  mesh.computeVertexNormals();
}

void draw() {
  background(0);
  camera(width / 2 - mouseX, height / 2 - mouseY, 400, 0, 0, 0, 0, 1, 0);
  lights();
  if (!isFilled) {
    noFill();
    stroke(255);
    gfx.mesh(mesh, false, 10);
  } 
  else {
    fill(255);
    stroke(255);
    gfx.mesh(mesh, false, 10);
  }
}

void keyPressed() {
  if (key == 'f') {
    isFilled = !isFilled;
  }
  Vec3D offset = new Vec3D();
  if (key == 'a') {
    offset.x = -1;
  }
  if (key == 'd') {
    offset.x = 1;
  }
  if (key == 'w') {
    offset.y = -1;
  }
  if (key == 's') {
    offset.y = 1;
  }
  if (key == 'z') {
    offset.z = -1;
  }
  if (key == 'x') {
    offset.z = 1;
  }
  Face f = mesh.faces.get(mesh.faces.size() - 1);
  translateFace(f, offset);
}

void translateFace(Face f, Vec3D offset) {
  f.a.addSelf(offset);
  f.b.addSelf(offset);
  f.c.addSelf(offset);
  mesh.computeFaceNormals();
  mesh.computeVertexNormals();
}

