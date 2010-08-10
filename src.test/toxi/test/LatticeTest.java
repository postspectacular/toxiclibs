package toxi.test;

import java.util.List;

import processing.core.PApplet;
import toxi.geom.Cone;
import toxi.geom.Vec3D;
import toxi.geom.mesh.LaplacianSmooth;
import toxi.geom.mesh.MidpointDisplacementSubdivision;
import toxi.geom.mesh.WETriangleMesh;
import toxi.geom.mesh.WingedEdge;
import toxi.processing.ToxiclibsSupport;
import toxi.util.DateUtils;
import toxi.volume.IsoSurface;
import toxi.volume.RoundBrush;
import toxi.volume.VolumetricSpace;

public class LatticeTest extends PApplet {

    public static void main(String[] args) {
        PApplet.main(new String[] { "toxi.test.LatticeTest" });
    }

    ToxiclibsSupport gfx;
    private WETriangleMesh mesh;
    private float currZoom = 1;
    private boolean isWireframe;

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
        gfx.mesh(mesh, true);
    }

    public void keyPressed() {
        if (key == 'x') {
            mesh.saveAsSTL(sketchPath("lattice-" + DateUtils.timeStamp()
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
    }

    public void setup() {
        size(1280, 720, OPENGL);
        gfx = new ToxiclibsSupport(this);
        mesh = new WETriangleMesh();
        // mesh.addMesh(new AABB(new Vec3D(0, 0, 0), 150).toMesh());
        mesh.addMesh(new Cone(new Vec3D(), new Vec3D(0, 1, 0), 0, 150, 300)
                .toMesh(3));
        for (int i = 0; i < 3; i++) {
            MidpointDisplacementSubdivision subdiv =
                    new MidpointDisplacementSubdivision(mesh.getCentroid(),
                            i % 2 == 0 ? 0.25f : -0.25f);
            mesh.subdivide(subdiv, 0);
        }
        VolumetricSpace volume =
                new VolumetricSpace(new Vec3D(310, 310, 310), 96, 96, 96);
        RoundBrush brush = new RoundBrush(volume, 5);
        for (WingedEdge e : mesh.edges.values()) {
            List<Vec3D> points = e.splitIntoSegments(null, 1, true);
            for (Vec3D p : points) {
                brush.drawAtAbsolutePos(p, 0.5f);
            }
        }
        volume.closeSides();
        IsoSurface surface = new IsoSurface(volume);
        mesh.clear().addMesh(surface.computeSurfaceMesh(null, 0.2f));
    }
}
