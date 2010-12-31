/**
 * <p>SphericalHarmonicsMeshBuilder demonstrates how to use the SurfaceMeshBuilder class
 * in conjunction with a spherical harmonics function to dynamically create a variety
 * of organic looking forms. The function is described in detail on Paul Bourke's website.
 * Included is also a re-usable function for displaying a generic TriangleMesh instance
 * using normal mapping. the display of surface normals useful for debug purposes.</p>
 * 
 * <p><strong>Usage:</strong>
 * <ul>
 * <li>r: randomize spherical harmonics</li>
 * <li>w: toggle wireframe on/off</li>
 * <li>n: toggle normal vector display on/off</li>
 * <li>s: save current mesh as STL file</li>
 * <li>space: save screenshot</li>
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

import processing.opengl.*;

import toxi.math.waves.*;
import toxi.geom.*;
import toxi.geom.mesh.*;

TriangleMesh mesh = new TriangleMesh();

boolean isWireFrame;
boolean showNormals;
boolean doSave;

Matrix4x4 normalMap = new Matrix4x4().translateSelf(128,128,128).scaleSelf(127);

void setup() {
  size(1024,576, OPENGL);
  randomizeMesh();
}

void draw() {
  background(0);
  translate(width / 2, height / 2, 0);
  rotateX(mouseY * 0.01f);
  rotateY(mouseX * 0.01f);
  lights();
  shininess(16);
  directionalLight(255,255,255,0,-1,1);
  specular(255);
  drawAxes(400);
  if (isWireFrame) {
    noFill();
    stroke(255);
  } 
  else {
    fill(255);
    noStroke();
  }
  drawMesh(g, mesh, !isWireFrame, showNormals);
  if (doSave) {
    saveFrame("sh-"+(System.currentTimeMillis()/1000)+".png");
    doSave=false;
  }
}

void drawAxes(float l) {
  stroke(255, 0, 0);
  line(0, 0, 0, l, 0, 0);
  stroke(0, 255, 0);
  line(0, 0, 0, 0, l, 0);
  stroke(0, 0, 255);
  line(0, 0, 0, 0, 0, l);
}

void drawMesh(PGraphics gfx, TriangleMesh mesh, boolean vertexNormals, boolean showNormals) {
  gfx.beginShape(PConstants.TRIANGLES);
  AABB bounds=mesh.getBoundingBox();
  Vec3D min=bounds.getMin();
  Vec3D max=bounds.getMax();
  if (vertexNormals) {
    for (Iterator i=mesh.faces.iterator(); i.hasNext();) {
      Face f=(Face)i.next();
      Vec3D n = normalMap.applyTo(f.a.normal);
      gfx.fill(n.x, n.y, n.z);
      gfx.normal(f.a.normal.x, f.a.normal.y, f.a.normal.z);
      gfx.vertex(f.a.x, f.a.y, f.a.z);
      n = normalMap.applyTo(f.b.normal);
      gfx.fill(n.x, n.y, n.z);
      gfx.normal(f.b.normal.x, f.b.normal.y, f.b.normal.z);
      gfx.vertex(f.b.x, f.b.y, f.b.z);
      n = normalMap.applyTo(f.c.normal);
      gfx.fill(n.x, n.y, n.z);
      gfx.normal(f.c.normal.x, f.c.normal.y, f.c.normal.z);
      gfx.vertex(f.c.x, f.c.y, f.c.z);
    }
  } 
  else {
    for (Iterator i=mesh.faces.iterator(); i.hasNext();) {
      Face f=(Face)i.next();
      gfx.normal(f.normal.x, f.normal.y, f.normal.z);
      gfx.vertex(f.a.x, f.a.y, f.a.z);
      gfx.vertex(f.b.x, f.b.y, f.b.z);
      gfx.vertex(f.c.x, f.c.y, f.c.z);
    }
  }
  gfx.endShape();
  if (showNormals) {
    if (vertexNormals) {
      for (Iterator i=mesh.vertices.values().iterator(); i.hasNext();) {
        Vertex v=(Vertex)i.next();
        Vec3D w = v.add(v.normal.scale(10));
        Vec3D n = v.normal.scale(127);
        gfx.stroke(n.x + 128, n.y + 128, n.z + 128);
        gfx.line(v.x, v.y, v.z, w.x, w.y, w.z);
      }
    } 
    else {
      for (Iterator i=mesh.faces.iterator(); i.hasNext();) {
        Face f=(Face)i.next();
        Vec3D c = f.a.add(f.b).addSelf(f.c).scaleSelf(1f / 3);
        Vec3D d = c.add(f.normal.scale(20));
        Vec3D n = f.normal.scale(127);
        gfx.stroke(n.x + 128, n.y + 128, n.z + 128);
        gfx.line(c.x, c.y, c.z, d.x, d.y, d.z);
      }
    }
  }
}

void keyPressed() {
  if (key == 'r') {
    randomizeMesh();
  }
  if (key == 'w') {
    isWireFrame = !isWireFrame;
  }
  if (key == 'n') {
    showNormals = !showNormals;
  }
  if (key == 's') {
    mesh.saveAsSTL(sketchPath("superellipsoid-"+(System.currentTimeMillis()/1000)+".stl"));
  }
  if (key == ' ') {
    doSave=true;
  }
}

void randomizeMesh() {
  float[] m=new float[8];
  for(int i=0; i<8; i++) {
    m[i]=(int)random(9);
  }
  SurfaceMeshBuilder b = new SurfaceMeshBuilder(new SphericalHarmonics(m));
  mesh = (TriangleMesh)b.createMesh(null,80, 60);
}
