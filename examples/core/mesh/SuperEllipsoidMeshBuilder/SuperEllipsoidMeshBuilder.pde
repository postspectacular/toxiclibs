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



import toxi.geom.*;
import toxi.geom.mesh.*;
import toxi.math.*;
import toxi.math.waves.*;
import toxi.processing.*;

TriangleMesh mesh = new TriangleMesh();

AbstractWave modX, modY;

boolean isWireFrame;
boolean showNormals;

ToxiclibsSupport gfx;

void setup() {
  size(1024,576, P3D);
  modX = new SineWave(0, 0.01f, 2.5f, 2.5f);
  modY = new SineWave(PI, 0.017f, 2.5f, 2.5f);
  gfx=new ToxiclibsSupport(this);
}

void draw() {
  SurfaceFunction functor=new SuperEllipsoid(modX.update(), modY.update());
  SurfaceMeshBuilder b = new SurfaceMeshBuilder(functor);
  mesh = (TriangleMesh)b.createMesh(null,80, 80);
  mesh.computeVertexNormals();
  background(0);
  lights();
  translate(width / 2, height / 2, 0);
  rotateX(mouseY * 0.01f);
  rotateY(mouseX * 0.01f);
  gfx.origin(300);
  if (isWireFrame) {
    noFill();
    stroke(255);
  } 
  else {
    fill(255);
    noStroke();
  }
  scale(2);
  gfx.mesh(mesh, !isWireFrame, showNormals ? 10 : 0);
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

