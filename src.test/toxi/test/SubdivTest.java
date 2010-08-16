package toxi.test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import processing.core.PApplet;
import processing.core.PGraphics;
import toxi.geom.AABB;
import toxi.geom.Cone;
import toxi.geom.Matrix4x4;
import toxi.geom.Plane;
import toxi.geom.Vec3D;
import toxi.geom.mesh.Face;
import toxi.geom.mesh.LaplacianSmooth;
import toxi.geom.mesh.MidpointDisplacementSubdivision;
import toxi.geom.mesh.STLReader;
import toxi.geom.mesh.SubdivisionStrategy;
import toxi.geom.mesh.Vertex;
import toxi.geom.mesh.WEFace;
import toxi.geom.mesh.WETriangleMesh;
import toxi.processing.ToxiclibsSupport;
import toxi.util.DateUtils;

public class SubdivTest extends PApplet {

    public static void main(String[] args) {
        PApplet.main(new String[] { "toxi.test.SubdivTest" });
    }

    private ToxiclibsSupport gfx;
    private WETriangleMesh mesh;
    private int depth;
    private boolean isWireframe;

    private Matrix4x4 normalMap = new Matrix4x4().translateSelf(128, 128, 128)
            .scaleSelf(127);
    private float currZoom = 1.5f;
    private boolean doSave;

    public void addHole(WEFace f, float d, float l) {
        Vec3D centroid = f.getCentroid();
        Vec3D a2 = f.a.interpolateTo(centroid, d);
        Vec3D b2 = f.b.interpolateTo(centroid, d);
        Vec3D c2 = f.c.interpolateTo(centroid, d);
        // Vec3D s = centroid.add(f.normal.scale(l));
        mesh.removeFace(f);
        mesh.addFace(f.a, b2, a2);
        mesh.addFace(f.a, f.b, b2);
        mesh.addFace(f.b, c2, b2);
        mesh.addFace(f.b, f.c, c2);
        mesh.addFace(f.c, a2, c2);
        mesh.addFace(f.c, f.a, a2);
        // mesh.addFace(a2, b2, s);
        // mesh.addFace(b2, c2, s);
        // mesh.addFace(c2, a2, s);
    }

    public void draw() {
        background(255);
        translate(width / 2, height / 2, 0);
        // rotateX(HALF_PI);
        // rotateY(QUARTER_PI);
        rotateX(mouseY * 0.01f);
        rotateY(mouseX * 0.01f);
        scale(currZoom);
        if (!isWireframe) {
            fill(255);
            noStroke();
            lights();
        } else {
            // gfx.origin(new Vec3D(), 100);
            noFill();
            stroke(0);
        }
        // gfx.mesh(mesh, false, 0);
        drawMesh(g, mesh, true, isWireframe);
        if (doSave) {
            saveFrame("sd-mid-" + DateUtils.timeStamp() + ".png");
            doSave = false;
        }
    }

    void drawMesh(PGraphics gfx, WETriangleMesh mesh, boolean vertexNormals,
            boolean showNormals) {
        gfx.beginShape(TRIANGLES);
        if (vertexNormals) {
            for (Iterator<Face> i = mesh.faces.iterator(); i.hasNext();) {
                Face f = i.next();
                Vec3D n = normalMap.applyTo(f.a.normal);
                gfx.fill(n.x, n.y, n.z);
                gfx.normal(f.a.normal.x, f.a.normal.y, f.a.normal.z);
                gfx.vertex(f.a.x, f.a.y, f.a.z);
                n = normalMap.applyTo(f.b.normal);
                gfx.fill(n.x, n.y, n.z);
                gfx.normal(f.b.normal.x, f.b.normal.y, f.b.normal.z);
                gfx.vertex(f.b.x, f.b.y, f.b.z);
                n = normalMap.applyTo(f.c.normal);
                gfx.fill(n.x, n.y, n.z);
                gfx.normal(f.c.normal.x, f.c.normal.y, f.c.normal.z);
                gfx.vertex(f.c.x, f.c.y, f.c.z);
            }
        } else {
            for (Iterator<Face> i = mesh.faces.iterator(); i.hasNext();) {
                Face f = i.next();
                gfx.normal(f.normal.x, f.normal.y, f.normal.z);
                gfx.vertex(f.a.x, f.a.y, f.a.z);
                gfx.vertex(f.b.x, f.b.y, f.b.z);
                gfx.vertex(f.c.x, f.c.y, f.c.z);
            }
        }
        gfx.endShape();
        if (showNormals) {
            if (vertexNormals) {
                for (Iterator<Vertex> i = mesh.vertices.values().iterator(); i
                        .hasNext();) {
                    Vertex v = i.next();
                    Vec3D w = v.add(v.normal.scale(10));
                    Vec3D n = v.normal.scale(127);
                    gfx.stroke(n.x + 128, n.y + 128, n.z + 128);
                    gfx.line(v.x, v.y, v.z, w.x, w.y, w.z);
                }
            } else {
                for (Iterator<Face> i = mesh.faces.iterator(); i.hasNext();) {
                    Face f = i.next();
                    Vec3D c = f.a.add(f.b).addSelf(f.c).scaleSelf(1f / 3);
                    Vec3D d = c.add(f.normal.scale(20));
                    Vec3D n = f.normal.scale(127);
                    gfx.stroke(n.x + 128, n.y + 128, n.z + 128);
                    gfx.line(c.x, c.y, c.z, d.x, d.y, d.z);
                }
            }
        }
    }

    private void initMesh(int id) {
        mesh = new WETriangleMesh();
        switch (id) {
            case 0:
                mesh.addMesh(new Plane(new Vec3D(), new Vec3D(0, 1, 0))
                        .toMesh(400));
                break;
            case 1:
                mesh.addMesh(new AABB(new Vec3D(0, 0, 0), 100).toMesh());
                break;
            case 2:
                mesh.addMesh(new Cone(new Vec3D(), new Vec3D(0, 1, 0), 200,
                        200, 400).toMesh(8));
                break;
            case 3:
                mesh.addMesh(new Cone(new Vec3D(), new Vec3D(0, 1, 0), 0, 200,
                        200).toMesh(3));
                break;
            case 4:
                mesh.addMesh(new STLReader().loadBinary("test/test.stl",
                        STLReader.TRIANGLEMESH));
                break;
        }
    }

    public void keyPressed() {
        if (key >= '1' && key <= '5') {
            initMesh(key - '1');
        }
        if (key == 's') {
            SubdivisionStrategy subdiv;
            subdiv =
                    new MidpointDisplacementSubdivision(mesh.computeCentroid(),
                            depth % 3 == 0 ? -0.25f : 0.25f);
            // subdiv = new MidpointSubdivision();
            // subdiv = new DualSubdivision();
            // subdiv = new TriSubdivision();
            // subdiv = new NormalDisplacementSubdivision(0.25f);
            // subdiv =
            // new DualDisplacementSubdivision(mesh.computeCentroid(),
            // 0.25f, -0.25f);
            depth++;
            // subdiv.setEdgeOrdering(new FaceCountComparator());
            mesh.subdivide(subdiv, 0);
            // mesh.splitEdge(mesh.faces.get(0).c.edges.get(0), subdiv);
            mesh.computeFaceNormals();
            mesh.faceOutwards();
            mesh.computeVertexNormals();
        }
        if (key == 'x') {
            mesh.saveAsSTL(sketchPath("subdiv-" + DateUtils.timeStamp()
                    + ".stl"));
        }
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
        if (key == ' ') {
            doSave = true;
        }
        if (key == 'h') {
            List<Face> faces = new ArrayList<Face>(mesh.faces);
            for (Face f : faces) {
                addHole((WEFace) f, 0.25f, 50);
            }
            mesh.computeFaceNormals();
            mesh.computeVertexNormals();
        }
    }

    public void setup() {
        size(1280, 720, OPENGL);
        gfx = new ToxiclibsSupport(this);
        initMesh(0);
    }
}
