/**
 * <p>Tiny demo showing usage of STLReader class to load binary STL files
 * and display them (with face normals). There're 2 example meshes provided
 * with one of them having been exported using a flipped Y axis. The TriangleMesh
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

import toxi.processing.*;

TriangleMesh mesh;
ToxiclibsSupport gfx;

void setup() {
  size(600,600,P3D);
  mesh=(TriangleMesh)new STLReader().loadBinary(sketchPath("mesh.stl"),STLReader.TRIANGLEMESH);
  //mesh=(TriangleMesh)new STLReader().loadBinary(sketchPath("mesh-flipped.stl"),STLReader.TRIANGLEMESH).flipYAxis();
  gfx=new ToxiclibsSupport(this);
}

void draw() {
  background(51);
  lights();
  translate(width/2,height/2,0);
  rotateX(mouseY*0.01);
  rotateY(mouseX*0.01);
  gfx.origin(new Vec3D(),200);
  noStroke();
  gfx.mesh(mesh,false,10);
}

