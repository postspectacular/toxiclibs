/**
 * <p>SuperEllipsoidMeshBuilder demonstrates how to use the SurfaceMeshBuilder class
 * in conjunction with a SuperEllipsoid function to dynamically create a variety
 * of useful geometric forms. The super ellipsoid is described in detail on Paul
 * Bourke's website. In this demo 2 out-of-sync sine waves are used to animate
 * the ellipsoid parameters. Included is also a re-usable function for displaying
 * a generic TriangleMesh instance, incl. the display of surface normals useful for
 * debug purposes.</p>
 * 
 * <p><strong>Usage:</strong>
 * <ul>
 * <li>w: toggle wireframe on/off</li>
 * <li>n: toggle normal vector display on/off</li>
 * <li>s: save current mesh as STL file</li>
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

import toxi.util.datatypes.*;
import toxi.math.noise.*;
import toxi.math.waves.*;
import toxi.geom.*;
import toxi.math.*;
import toxi.math.conversion.*;
import toxi.geom.mesh.*;

TriangleMesh mesh = new TriangleMesh();

AbstractWave modX, modY;

boolean isWireFrame;
boolean showNormals;

void setup() {
  size(1024,576, OPENGL);
  modX = new SineWave(0, 0.01f, 2.5f, 2.5f);
  modY = new SineWave(PI, 0.017f, 2.5f, 2.5f);
}

void draw() {
  SurfaceFunction functor=new SuperEllipsoid(modX.update(), modY.update());
  SurfaceMeshBuilder b = new SurfaceMeshBuilder(functor);
  mesh = b.createMesh(80, 80);
  background(0);
  lights();
  translate(width / 2, height / 2, 0);
  rotateX(mouseY * 0.01f);
  rotateY(mouseX * 0.01f);
  drawAxes(300);
  if (isWireFrame) {
    noFill();
    stroke(255);
  } 
  else {
    fill(255);
    noStroke();
  }
  scale(2);
  drawMesh(g, mesh, !isWireFrame, showNormals);
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
  if (vertexNormals) {
    for (Iterator i=mesh.faces.iterator(); i.hasNext();) {
      TriangleMesh.Face f=(TriangleMesh.Face)i.next();
      gfx.normal(f.a.normal.x, f.a.normal.y, f.a.normal.z);
      gfx.vertex(f.a.x, f.a.y, f.a.z);
      gfx.normal(f.b.normal.x, f.b.normal.y, f.b.normal.z);
      gfx.vertex(f.b.x, f.b.y, f.b.z);
      gfx.normal(f.c.normal.x, f.c.normal.y, f.c.normal.z);
      gfx.vertex(f.c.x, f.c.y, f.c.z);
    }
  } 
  else {
    for (Iterator i=mesh.faces.iterator(); i.hasNext();) {
      TriangleMesh.Face f=(TriangleMesh.Face)i.next();
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
        TriangleMesh.Vertex v=(TriangleMesh.Vertex)i.next();
        Vec3D w = v.add(v.normal.scale(10));
        Vec3D n = v.normal.scale(127);
        gfx.stroke(n.x + 128, n.y + 128, n.z + 128);
        gfx.line(v.x, v.y, v.z, w.x, w.y, w.z);
      }
    } 
    else {
      for (Iterator i=mesh.faces.iterator(); i.hasNext();) {
        TriangleMesh.Face f=(TriangleMesh.Face)i.next();
        Vec3D c = f.a.add(f.b).addSelf(f.c).scaleSelf(1f / 3);
        Vec3D d = c.add(f.normal.scale(10));
        Vec3D n = f.normal.scale(127);
        gfx.stroke(n.x + 128, n.y + 128, n.z + 128);
        gfx.line(c.x, c.y, c.z, d.x, d.y, d.z);
      }
    }
  }
}

void keyPressed() {
  if (key == 'w') {
    isWireFrame = !isWireFrame;
  }
  if (key == 'n') {
    showNormals = !showNormals;
  }
  if (key == 's') {
    mesh.saveAsSTL(sketchPath("superellipsoid-"+(System.currentTimeMillis()/1000)+".stl"));
  }
}

