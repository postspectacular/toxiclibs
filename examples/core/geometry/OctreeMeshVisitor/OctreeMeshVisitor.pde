/**
 * This example demonstrates the application of the Visitor design pattern to build a 3D mesh
 * of all boxes surrounding each node in a PointOctree. It uses an interface called OctreeVisitor
 * whose implementation will be recursively applied to each node. Separating the actual function
 * of building the mesh from all the recursive parts inherent with tree structures, not only
 * simplifies the code but also provides a general mechanism to process tree nodes without
 * having to resort to other (more complex) mechanisms like inheritance etc. Links about the
 * concepts used are in the comments further below.
 *
 * The demo first creates a sphere mesh and places all vertices in the octree. Then the mesh of
 * the tree hierarchy is built and exported as STL model.
 *
 * toxiclibs version 0021 onwards will contain the OctreeVisitor interface and applyVisitor() method.
 * An updated/simplified version will be bundled with these future releases.
 */

/* 
 * Copyright (c) 2011 Karsten Schmidt
 * 
 * This demo & library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * http://creativecommons.org/licenses/LGPL/2.1/2
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

float radius=200;

ToxiclibsSupport gfx;
TreeMeshBuilder builder;

void setup() {
  size(640, 480, P3D);
  gfx=new ToxiclibsSupport(this);
  // create the root node of the octree
  PointOctree tree=new PointOctree(new Vec3D(-radius, -radius, -radius), radius*2);
  tree.setMinNodeSize(2);
  // create a sphere mesh (slightly smaller than tree max bounds)
  Mesh3D sphere=new Sphere(radius*0.9).toMesh(20);
  // add all sphere vertices to tree
  tree.addAll(new ArrayList<Vec3D>(sphere.getVertices()));
  // starting at the root node, recursively apply the mesh builder (see below) as visitor to all nodes
  builder=new TreeMeshBuilder();
  tree.applyVisitor(builder);
  
  // export mesh
  builder.mesh.saveAsSTL(sketchPath("octree.stl"));
}

void draw() {
  background(255);
  noFill();
  stroke(0, 100);
  translate(width/2, height/2, 0);
  rotateX(mouseY*0.01);
  rotateY(mouseX*0.01);
  // display the box mesh
  gfx.mesh(builder.mesh);
}

/**
 * This class is a tree visitor implementation and creates a triangle mesh of
 * all octree nodes.
 */
class TreeMeshBuilder implements OctreeVisitor {

  TriangleMesh mesh=new TriangleMesh();

  void visitNode(PointOctree node) {
    // Octree nodes are AABBs and therefore have a toMesh() method
    // simply add each to the main mesh... 
    mesh.addMesh(node.toMesh());
  }
}
