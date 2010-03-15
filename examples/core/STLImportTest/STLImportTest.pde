/**
 * <p>Tiny demo showing usage of STLReader class to load binary STL files
 * and display them (with face normals). There're 2 example meshes provided
 * with one of them being exported using a flipped Y axis. The TriangleMesh
 * class has a convenient method to reorient all faces.</p>
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

TriangleMesh mesh;

void setup() {
  size(600,600,P3D);
  mesh=new STLReader().loadBinary(sketchPath("mesh.stl"));
  //mesh=new STLReader().loadBinary(sketchPath("mesh-flipped.stl")).flipYAxis();
}

void draw() {
  background(51);
  lights();
  translate(width/2,height/2,0);
  rotateX(mouseY*0.01);
  rotateY(mouseX*0.01);
  drawAxes(200);
  noStroke();
  drawMesh(mesh);
}

void drawAxes(float l) {
  stroke(255, 0, 0);
  line(0, 0, 0, l, 0, 0);
  stroke(0, 255, 0);
  line(0, 0, 0, 0, l, 0);
  stroke(0, 0, 255);
  line(0, 0, 0, 0, 0, l);
}

void drawMesh(TriangleMesh mesh) {
  beginShape(TRIANGLES);
  int numFaces=mesh.getNumFaces();
  for(int i=0; i<numFaces; i++) {
    TriangleMesh.Face f=mesh.faces.get(i);
    Vec3D n=f.normal;
    normal(n.x,n.y,n.z);
    vertex(f.a.x,f.a.y,f.a.z);
    vertex(f.b.x,f.b.y,f.b.z);
    vertex(f.c.x,f.c.y,f.c.z);
  }
  endShape();
  for(int i=0; i<numFaces; i++) {
    TriangleMesh.Face f=mesh.faces.get(i);
    Vec3D c=f.a.add(f.b).addSelf(f.c).scaleSelf(1f/3);
    Vec3D d=c.add(f.normal.scale(20));
    Vec3D n=f.normal.scale(127);
    stroke(n.x+128,n.y+128,n.z+128);
    line(c.x,c.y,c.z,d.x,d.y,d.z);
  }
}
