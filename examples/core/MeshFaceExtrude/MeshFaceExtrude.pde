/**
 * Mesh face extrusion with toxiclibs-0019 or later
 *
 * Usage:
 * f - toggle filled/wireframe display
 * a/d - move extruded face along X axis
 * w/s - moved face along Y
 * z/x - move face along Z
 *
 * (c) 2010 Karsten Schmidt // LGPL licensed
 */
import toxi.processing.*;

import toxi.geom.*;
import toxi.geom.mesh.*;
import toxi.geom.mesh.TriangleMesh.Face;

TriangleMesh mesh;
boolean isFilled = true;

ToxiclibsSupport gfx;

void setup() {
  size(680, 382, P3D);
  gfx = new ToxiclibsSupport(this);
  // create a mesh from the axis-aligned bounding box (AABB)
  mesh = new AABB(new Vec3D(), 100).toMesh();
  // get first face/triangle of mesh
  Face f = mesh.faces.get(0);
  // extrude along positive Z axis and shrink to 25% of original size
  // shrinking is done by moving vertices towards centroid of the face
  // before extruding
  float shrink=0.25;
  Vec3D centroid = new Triangle(f.a, f.b, f.c).computeCentroid();
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
  lights();
  camera(width / 2 - mouseX, height / 2 - mouseY, 400, 0, 0, 0, 0, 1, 0);
  if (!isFilled) {
    noFill();
    stroke(255);
    gfx.mesh(mesh, false, 10);
  } 
  else {
    fill(255);
    stroke(255);
    gfx.mesh(mesh, true, 10);
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

