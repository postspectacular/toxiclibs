package toxi.test;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import toxi.geom.AABB;
import toxi.geom.Vec3D;
import toxi.geom.mesh.LaplacianSmooth;
import toxi.geom.mesh.MidpointDisplacementSubdivision;
import toxi.geom.mesh.SubdivisionStrategy;
import toxi.geom.mesh.WETriangleMesh;
import toxi.geom.mesh.WingedEdge;
import toxi.math.ScaleMap;
import toxi.processing.ToxiclibsSupport;
import toxi.util.DateUtils;
import toxi.util.datatypes.FloatRange;
import toxi.volume.HashIsoSurface;
import toxi.volume.IsoSurface;
import toxi.volume.RoundBrush;
import toxi.volume.VolumetricSpaceArray;

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
            saveMesh();
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

    private void saveMesh() {
        mesh.saveAsSTL(sketchPath("lattice-" + DateUtils.timeStamp() + ".stl"));
    }

    public void setup() {
        size(1280, 720, OPENGL);
        gfx = new ToxiclibsSupport(this);
        mesh = new WETriangleMesh();
        mesh.addMesh(new AABB(new Vec3D(0, 0, 0), 150).toMesh());
        // mesh.addMesh(new Cone(new Vec3D(), new Vec3D(0, 1, 0), 0, 150, 300)
        // .toMesh(3));
        for (int i = 0; i < 3; i++) {
            SubdivisionStrategy subdiv =
                    new MidpointDisplacementSubdivision(mesh.computeCentroid(),
                            i % 3 == 0 ? 0.25f : -0.25f);
            // subdiv = new DualSubdivision();
            mesh.subdivide(subdiv, 0);
        }
        System.out.println("creating lattice...");
        VolumetricSpaceArray volume =
                new VolumetricSpaceArray(new Vec3D(460, 460, 460), 192, 192,
                        192);
        RoundBrush brush = new RoundBrush(volume, 4.5f);
        List<Float> edgeLengths = new ArrayList<Float>();
        for (WingedEdge e : mesh.edges.values()) {
            edgeLengths.add(e.getLength());
        }
        FloatRange range = FloatRange.fromSamples(edgeLengths);
        ScaleMap brushSize = new ScaleMap(range.min, range.max, 3.75f, 8);
        for (WingedEdge e : mesh.edges.values()) {
            List<Vec3D> points = e.splitIntoSegments(null, 1, true);
            brush.setSize((float) brushSize.getClippedValueFor(e.getLength()));
            for (Vec3D p : points) {
                brush.drawAtAbsolutePos(p, 0.5f);
            }
        }
        volume.closeSides();
        mesh = null;
        System.out.println("creating surface...");
        IsoSurface surface;
        // surface = new ArrayIsoSurface(volume);
        surface = new HashIsoSurface(volume);
        // surface.useMinimumRAM(true);
        mesh =
                (WETriangleMesh) surface.computeSurfaceMesh(new WETriangleMesh(
                        "iso", 300000, 900000), 0.2f);
        volume = null;
        surface = null;
        System.out.println("mesh: " + mesh);
        System.out.println("scaling...");
        // float scale = 75f / mesh.getBoundingBox().getExtent().x;
        // mesh.scale(scale);
        System.out.println("smoothing...");
        new LaplacianSmooth().filter(mesh, 4);
        saveMesh();
        System.exit(0);
    }
}
