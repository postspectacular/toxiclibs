package toxi.test;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import toxi.color.TColor;
import toxi.geom.AABB;
import toxi.geom.Matrix4x4;
import toxi.geom.Vec3D;
import toxi.geom.mesh.LaplacianSmooth;
import toxi.geom.mesh.WETriangleMesh;
import toxi.geom.mesh.WEVertex;
import toxi.geom.mesh.WingedEdge;
import toxi.math.MathUtils;
import toxi.processing.ToxiclibsSupport;
import toxi.util.DateUtils;

public class SubdivHoles extends PApplet {

    public static void main(String[] args) {
        PApplet.main(new String[] { "toxi.test.SubdivHoles" });
    }

    ToxiclibsSupport gfx;
    private float currZoom = 1;
    private boolean isWireframe;
    private WETriangleMesh mesh;

    private Matrix4x4 normalMap =
            new Matrix4x4().translateSelf(128, 128, 128).scaleSelf(127);

    private List<Vec3D> verts = new ArrayList<Vec3D>();

    private void backup() {
        verts.clear();
        for (Vec3D v : mesh.vertices.values()) {
            verts.add(v.copy());
        }
    }

    public void draw() {
        background(0);
        translate(width / 2, height / 2, 0);
        rotateX(mouseY * 0.01f);
        rotateY(mouseX * 0.01f);
        scale(currZoom);
        if (!isWireframe) {
            fill(255);
            noStroke();
            lights();
        } else {
            noFill();
            stroke(255);
        }
        // gfx.mesh(mesh, true, 0);
        drawMesh(mesh);
    }

    private void drawMesh(WETriangleMesh mesh) {
        beginShape(LINES);
        TColor col = TColor.newHSV(0, 1, 1);
        for (WingedEdge e : mesh.edges.values()) {
            float da = e.a.distanceTo(verts.get(((WEVertex) e.a).id));
            float db = e.b.distanceTo(verts.get(((WEVertex) e.b).id));
            col.setHue(da / 20);
            stroke(col.toARGB());
            vertex(e.a.x, e.a.y, e.a.z);
            col.setHue(db / 20);
            stroke(col.toARGB());
            vertex(e.b.x, e.b.y, e.b.z);
        }
        endShape();
    }

    public void keyPressed() {
        if (key == '-') {
            currZoom -= 0.1f;
        }
        if (key == '=') {
            currZoom += 0.1f;
        }
        if (key == 'w') {
            isWireframe = !isWireframe;
        }
        if (key == 'l') {
            new LaplacianSmooth().filter(mesh, 1);
        }
        if (key == 's') {
            mesh.subdivide();
            backup();
        }
        if (key == 'x') {
            mesh
                    .saveAsSTL(sketchPath("holes-" + DateUtils.timeStamp()
                            + ".stl"));
        }
    }

    public void setup() {
        size(1280, 720, OPENGL);
        gfx = new ToxiclibsSupport(this);
        mesh = new WETriangleMesh();
        // mesh.addMesh(new Plane(new Vec3D(), new Vec3D(0, 1, 0)).toMesh(400));
        mesh.addMesh(new AABB(new Vec3D(0, 0, 0), 200).toMesh());
        mesh.subdivide();
        mesh.subdivide();
        mesh.subdivide();
        mesh.subdivide();
        for (int i = 0; i < 500; i++) {
            int id = (int) (MathUtils.random(0.1f, 0.9f) * mesh.faces.size());
            mesh.removeFace(mesh.faces.get(id));
        }
        backup();
    }
}
