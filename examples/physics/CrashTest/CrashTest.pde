/**
 * This example demonstrates the new winged-edge mesh structure in combination with
 * the 3d verlet physics engine to simulate a crash test scenario. The winged-edge mesh
 * provides connectivity information for each mesh vertex which is used to create a
 * physical representation of the mesh and allows each vertex/particle to be connected with
 * springs. Every frame the mesh vertices are updated to the position of their corresponding
 * particle and due to the gravity in the space, the mesh is being deformed.
 * 
 * <p>Usage: Press 'r' to restart the simulation.</p>
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
import toxi.physics.*;
import toxi.physics.behaviors.*;
import toxi.processing.*;
import processing.opengl.*;

ToxiclibsSupport gfx;
VerletPhysics physics;
WETriangleMesh box;

void setup() {
    size(680, 382, OPENGL);
    gfx = new ToxiclibsSupport(this);
    initPhysics();
}

void draw() {
    physics.update();
    // update mesh vertices based on the current particle positions
    for (Vertex v : box.vertices.values()) {
        v.set(physics.particles.get(v.id));
    }
    // update mesh normals
    box.computeFaceNormals();
    // setup lighting
    background(51);
    lights();
    directionalLight(255, 255, 255, -200, 1000, 500);
    specular(255);
    shininess(16);
    // point camera at mesh centroid
    Vec3D c = box.computeCentroid();
    camera(-100, -50, 80, c.x, c.y, c.z, 0, 1, 0);
    // draw coordinate system
    gfx.origin(new Vec3D(), 50);
    // draw physics bounding box
    stroke(255, 80);
    noFill();
    gfx.box(physics.getWorldBounds());
    // draw car
    fill(160);
    noStroke();
    gfx.mesh(box, false, 0);
}

void initPhysics() {
    physics = new VerletPhysics();
    box = new WETriangleMesh().addMesh(new STLReader().loadBinary(openStream("audi.stl"),"car",STLReader.WEMESH));
    // properly orient and scale mesh
    box.rotateX(HALF_PI);
    box.scale(8);
    // adjust physics bounding box based on car (but bigger)
    // and align car with bottom of the new box
    AABB bounds = box.getBoundingBox();
    Vec3D ext = bounds.getExtent();
    Vec3D min = bounds.sub(ext.scale(4, 3, 2));
    Vec3D max = bounds.add(ext.scale(4, 3, 2));
    physics.setWorldBounds(AABB.fromMinMax(min, max));
    box.translate(new Vec3D(ext.scale(3, 2, 0)));
    // set gravity along negative X axis with slight downward
    physics.addBehavior(new GravityBehavior(new Vec3D(-0.1f, 0.001f, 0)));
    // turn mesh vertices into physics particles
    for (Vertex v : box.vertices.values()) {
        physics.addParticle(new VerletParticle(v));
    }
    // turn mesh edges into springs
    for (WingedEdge e : box.edges.values()) {
        VerletParticle a = physics.particles.get(((WEVertex) e.a).id);
        VerletParticle b = physics.particles.get(((WEVertex) e.b).id);
        physics.addSpring(new VerletSpring(a, b, a.distanceTo(b), 1f));
    }
}

void keyPressed() {
    if (key == 'r') {
        initPhysics();
    }
}
