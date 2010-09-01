package toxi.test;

import processing.core.PApplet;
import toxi.geom.Vec3D;
import toxi.geom.mesh.LaplacianSmooth;
import toxi.geom.mesh.SuperEllipsoid;
import toxi.geom.mesh.SurfaceFunction;
import toxi.geom.mesh.SurfaceMeshBuilder;
import toxi.geom.mesh.WETriangleMesh;
import toxi.processing.ToxiclibsSupport;
import toxi.util.DateUtils;

public class MeshBuilderTest extends PApplet {

    public static void main(String[] args) {
        PApplet.main(new String[] { "toxi.test.MeshBuilderTest" });
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
        gfx.origin(new Vec3D(), 200);
        if (!isWireframe) {
            fill(255);
            noStroke();
            lights();
        } else {
            noFill();
            stroke(255);
        }
        gfx.mesh(mesh, true, 10);
    }

    public void keyPressed() {
        if (key == 'w') {
            isWireframe = !isWireframe;
        }
        if (key == '-') {
            currZoom -= 0.1;
        }
        if (key == '=') {
            currZoom += 0.1;
        }
        if (key == 'l') {
            new LaplacianSmooth().filter(mesh, 1);
        }
        if (key == 'x') {
            mesh.saveAsSTL(sketchPath("builder-" + DateUtils.timeStamp()
                    + ".stl"));
        }
    }

    public void setup() {
        size(1280, 720, OPENGL);
        gfx = new ToxiclibsSupport(this);
        SurfaceFunction f;
        // f = new SphereFunction(new Sphere(new Vec3D(100, 100, 100), 100));
        f = new SuperEllipsoid(4, 1);
        // f = new SphericalHarmonics(new float[] { 8, 1, 8, 8, 3, 3, 4, 1 });
        SurfaceMeshBuilder builder = new SurfaceMeshBuilder(f);
        mesh =
                (WETriangleMesh) builder.createMesh(new WETriangleMesh(), 100,
                        100);
    }
}
