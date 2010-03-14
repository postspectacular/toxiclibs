/**
 * This example shows how to dynamically create a simple box mesh and align it
 * with a given direction vector using the pointTowards() method of the
 * TriangleMesh class.
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

import toxi.geom.*;
import toxi.geom.mesh.*;

Vec3D BOX_SIZE = new Vec3D(5,5,50);
float SCALE=200;

TriangleMesh[] boxes=new TriangleMesh[600];

void setup() {
  size(600,600,OPENGL);
  for(int i=0; i<boxes.length; i++) {
    // create a new direction vector for each box
    Vec3D dir=new Vec3D(cos(i*TWO_PI/75),sin(i*TWO_PI/50),sin(i*TWO_PI/25)).normalize();
    // create a position on a sphere, using the direction vector
    Vec3D pos=dir.scale(SCALE);
    // create a box mesh at the origin
    TriangleMesh b=new AABB(new Vec3D(), BOX_SIZE).toMesh();
    // align the Z axis of the box with the direction vector
    b.pointTowards(dir);
    // move the box to the correct position
    b.transform(new Matrix4x4().translateSelf(pos.x,pos.y,pos.z));
    boxes[i]=b;
  }
}

void draw() {
  background(51);
  lights();
  translate(width / 2, height / 2, 0);
  rotateX(mouseY * 0.01f);
  rotateY(mouseX * 0.01f);
  noStroke();
  for(int i=0; i<boxes.length; i++) {
    drawMesh(boxes[i]);
  }
}

void drawMesh(TriangleMesh mesh) {
  beginShape(TRIANGLES);
  for(Iterator i=mesh.faces.iterator(); i.hasNext();) {
    TriangleMesh.Face f=(TriangleMesh.Face)i.next();
    Vec3D n=f.normal;
    normal(n.x,n.y,n.z);
    vertex(f.a.x,f.a.y,f.a.z);
    vertex(f.b.x,f.b.y,f.b.z);
    vertex(f.c.x,f.c.y,f.c.z);
  }
  endShape();
}


