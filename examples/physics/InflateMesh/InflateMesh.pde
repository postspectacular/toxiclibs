import processing.opengl.*;

import toxi.geom.*;
import toxi.geom.mesh.subdiv.*;
import toxi.geom.mesh.*;
import toxi.util.*;
import toxi.physics.*;
import toxi.physics.behaviors.*;
import toxi.physics.constraints.*;
import toxi.processing.*;

VerletPhysics physics;
WETriangleMesh box;

ToxiclibsSupport gfx;

void setup() {
    size(1024, 576, OPENGL);
    gfx = new ToxiclibsSupport(this);
    initPhysics();
}

void draw() {
    physics.update();
    for (Vertex v : box.vertices.values()) {
        v.set(physics.particles.get(v.id));
    }
    box.center(null);
    for (Vertex v : box.vertices.values()) {
        physics.particles.get(v.id).set(v);
    }
    box.computeFaceNormals();
    box.faceOutwards();
    box.computeVertexNormals();
    background(51);
    translate(width / 2, height / 2, 0);
    rotateX((height / 2 - mouseY) * 0.01f);
    rotateY((width / 2 - mouseX) * 0.01f);
    noFill();
    lights();
    directionalLight(255, 255, 255, -200, 1000, 500);
    specular(255);
    shininess(16);
    gfx.origin(new Vec3D(), 50);
    fill(192);
    noStroke();
    gfx.mesh(box, true);
}

void initPhysics() {
    box = new WETriangleMesh();
    box.addMesh(new Cone(new Vec3D(0, 0, 0), new Vec3D(0, 1, 0), 10, 50, 100).toMesh(16));
    box.subdivide();
    box.subdivide();
    box.subdivide();
    physics = new VerletPhysics();
    physics.setWorldBounds(new AABB(new Vec3D(), 180));
    physics.addBehavior(new AttractionBehavior(new Vec3D(), 400, -0.2f, 0.01f));
    // turn mesh vertices into physics particles
    for (Vertex v : box.vertices.values()) {
        physics.addParticle(new VerletParticle(v));
    }
    // turn mesh edges into springs
    for (WingedEdge e : box.edges.values()) {
        VerletParticle a = physics.particles.get(((WEVertex) e.a).id);
        VerletParticle b = physics.particles.get(((WEVertex) e.b).id);
        physics.addSpring(new VerletSpring(a, b, a.distanceTo(b), 0.003f));
    }
}

void keyPressed() {
    if (key == 'r') {
        initPhysics();
    }
}
