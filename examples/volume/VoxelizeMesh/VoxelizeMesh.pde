/**
 * <p>This example demonstrates the MeshVoxelizer utility to turn a given
 * triangle mesh into a volumetric representation for further manipulation.
 * E.g. This is useful for some digital fabrication tasks when only a shell
 * with a physical wall thickness is desired rather than a completely solid/filled
 * polygon model. Other use cases incl. experimentation with VolumetricBrushes
 * to drill holes into models etc.</p>
 *
 * <p>The MeshVoxelizer class is currently still in ongoing development, so any
 * feature requests/ideas/help is appreciated.</p>
 *
 * <p><strong>Usage:</strong><ul>
 * <li>v: voxelize current mesh (see details in function comment)</li>
 * <li>l: apply laplacian mesh smooth</li>
 * <li>w: wireframe on/off</li>
 * <li>n: show normals on/off</li>
 * <li>r: reset mesh</li>
 * <li>-/=: adjust zoom</li>
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
import toxi.geom.mesh.subdiv.*;
import toxi.processing.*;
import toxi.util.*;
import toxi.volume.*;
import toxi.color.*;

int RES=64;

ToxiclibsSupport gfx;
WETriangleMesh mesh;

boolean isWireframe;
float currZoom = 1.25f;

void setup() {
  size(1280, 720, P3D);
  gfx = new ToxiclibsSupport(this);
  initMesh();
}

void draw() {
  background(255);
  translate(width / 2, height / 2, 0);
  rotateX(mouseY * 0.01f);
  rotateY(mouseX * 0.01f);
  scale(currZoom);
  if (!isWireframe) {
    fill(255);
    noStroke();
    lights();
  } 
  else {
    gfx.origin(new Vec3D(), 100);
    noFill();
    stroke(0);
  }
  gfx.meshNormalMapped(mesh, !isWireframe);
}

// creates a simple cube mesh and applies displacement subdivision
// on all edges for several iterations
void initMesh() {
  mesh = new WETriangleMesh();
  new AABB(new Vec3D(0, 0, 0), 100).toMesh(mesh);
  for(int i=0; i<5; i++) {
    SubdivisionStrategy subdiv = new MidpointDisplacementSubdivision(
      mesh.computeCentroid(),
      i % 2 == 0 ? 0.35f : -0.2f
    );
    mesh.subdivide(subdiv,0);
  }
  mesh.computeFaceNormals();
  mesh.faceOutwards();
  mesh.computeVertexNormals();
}

// this function will first translate the mesh into a volumetric version
// this volumetric representation will constitute of a solid shell
// coinciding (albeit with loss of precision) with the original mesh
// the function then calculates a new mesh of an iso surface in this voxel space
// the original mesh will be discarded (overwritten)
//
// if you have enough RAM and would like less holes in the resulting surface
// try a higher voxel resolution (e.g. 128, 192) and/or increase wall thickness
void voxelizeMesh() {
  MeshVoxelizer voxelizer=new MeshVoxelizer(RES);
  // try setting to 1 or 2 (voxels)
  voxelizer.setWallThickness(0);
  VolumetricSpace vol = voxelizer.voxelizeMesh(mesh);
  vol.closeSides();
  IsoSurface surface = new HashIsoSurface(vol);
  mesh = new WETriangleMesh();
  surface.computeSurfaceMesh(mesh, 0.2f);
  mesh.computeVertexNormals();
}

void keyPressed() {
  if (key == 'w') {
    isWireframe = !isWireframe;
  }
  if (key == 'l') {
    new LaplacianSmooth().filter(mesh, 1);
  }
  if (key == '-') {
    currZoom -= 0.1;
  }
  if (key == '=') {
    currZoom += 0.1;
  }
  if (key == 'v') {
    voxelizeMesh();
  }
  if (key=='r') {
    initMesh();
  }
}
