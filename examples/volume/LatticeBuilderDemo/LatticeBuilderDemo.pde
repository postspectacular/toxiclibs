/**
 * <p>This example uses the MeshLatticeBuilder class to produce a lattice mesh of a
 * sphere. The lattices are created by taking all edges of the original 3D mesh and
 * re-sampling them with a volumetric brush in voxel space. Finally the resulting
 * surface mesh of that voxel space is returned and can be filtered/processed further
 * (in this case by applying the laplacian smooth mesh filter).</p>
 *
 * <p><strong>Usage:</strong><ul>
 * <li>press 'w' to toggle wireframe on/off</li>
 * </ul></p>
 */
 
/* 
 * Copyright (c) 2011 Karsten Schmidt
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
import toxi.volume.*;
import toxi.color.*;
import toxi.processing.*;

int VOXEL_RES=64;
int VOXEL_STROKE_WEIGHT=5;
int SPHERE_RES=8;
int SMOOTH_ITER=6;

ToxiclibsSupport gfx;
WETriangleMesh mesh;

boolean isWireframe;

void setup() {
  size(680, 382, P3D);
  gfx = new ToxiclibsSupport(this);
  // create a hemi-sphere mesh
  WETriangleMesh tmp=new WETriangleMesh().addMesh(new Sphere(160).toMesh(SPHERE_RES));
  // the MeshLatticeBuilder constructs a voxel space and
  // traces all edges in the mesh with a volumetric brush
  // it then computes a new mesh of the mesh's isosurface in that voxel space
  mesh=MeshLatticeBuilder.build(tmp, VOXEL_RES, VOXEL_STROKE_WEIGHT);
  // run a few iterations of the laplacian smooth mesh filter
  // to reduce the voxel look & feel
  for(int i=0; i<SMOOTH_ITER; i++) {
    new LaplacianSmooth().filter(mesh,1);
  }
}

void draw() {
  background(51);
  lights();
  translate(width/2,height/2,0);
  rotateX(mouseY*0.01);
  rotateY(mouseX*0.01);
  // apply wireframe/filled render settings
  gfx.chooseStrokeFill(isWireframe,TColor.newRGBA(1,1,1,0.25),TColor.WHITE);
  // draw lattice mesh
  gfx.mesh(mesh);
}

void mousePressed() {
  if(key=='w') {
    isWireframe=!isWireframe;
  }
}
