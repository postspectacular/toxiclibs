package toxi.test;

import processing.core.PApplet;
import toxi.geom.AABB;
import toxi.geom.Vec3D;
import toxi.geom.mesh.STLReader;
import toxi.geom.mesh.Vertex;
import toxi.geom.mesh.WETriangleMesh;
import toxi.geom.mesh.WEVertex;
import toxi.geom.mesh.WingedEdge;
import toxi.physics.VerletParticle;
import toxi.physics.VerletPhysics;
import toxi.physics.VerletSpring;
import toxi.processing.ToxiclibsSupport;

public class WEPhysics extends PApplet {

    public static void main(String[] args) {
        PApplet.main(new String[] { "toxi.test.WEPhysics" });
    }

    ToxiclibsSupport gfx;
    private VerletPhysics physics;
    private WETriangleMesh box;
    private int frameID;

    public void draw() {
        Vec3D grav = Vec3D.Y_AXIS.copy();
        grav.rotateX(mouseY * 0.01f);
        grav.rotateY(mouseX * 0.01f);
        physics.update();
        for (Vertex v : box.vertices.values()) {
            v.set(physics.particles.get(v.id));
        }
        box.computeFaceNormals();
        box.computeVertexNormals();
        Vec3D c = box.computeCentroid();
        background(51);
        noFill();
        lights();
        directionalLight(255, 255, 255, -200, 1000, 500);
        specular(255);
        shininess(16);
        camera(-100, -50, 80, c.x, c.y, c.z, 0, 1, 0);
        gfx.origin(new Vec3D(), 50);
        stroke(255, 80);
        gfx.box(physics.getWorldBounds());
        fill(128);
        noStroke();
        gfx.mesh(box, false, 0);
        // box.saveAsSTL(sketchPath(String.format("export/car/car-%03d.stl",
        // frameID++)));
    }

    private void initPhysics() {
        physics = new VerletPhysics();
        box =
                (WETriangleMesh) new STLReader().loadBinary("test/audi.stl",
                        STLReader.WEMESH);
        box.rotateX(HALF_PI);
        box.scale(8);
        AABB bounds = box.getBoundingBox();
        Vec3D ext = bounds.getExtent();
        Vec3D min = bounds.sub(ext.scale(4, 3, 2));
        Vec3D max = bounds.add(ext.scale(4, 3, 2));
        physics.setWorldBounds(AABB.fromMinMax(min, max));
        physics.setGravity(new Vec3D(-0.1f, 0.001f, 0));
        box.translate(new Vec3D(ext.scale(3, 2, 0)));
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
        frameID = 0;
    }

    public void keyPressed() {
        if (key == 'r') {
            initPhysics();
        }
    }

    public void setup() {
        size(680, 382, OPENGL);
        gfx = new ToxiclibsSupport(this);
        initPhysics();
    }
}
