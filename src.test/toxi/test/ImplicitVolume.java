package toxi.test;

import processing.core.PApplet;
import toxi.geom.Vec3D;
import toxi.geom.mesh.LaplacianSmooth;
import toxi.geom.mesh.WETriangleMesh;
import toxi.math.noise.SimplexNoise;
import toxi.processing.ToxiclibsSupport;
import toxi.volume.HashIsoSurface;
import toxi.volume.IsoSurface;
import toxi.volume.VolumetricSpace;

public class ImplicitVolume extends PApplet {

    public class SphereVolume extends VolumetricSpace {

        private int cx;
        private int cy;
        private int cz;
        private int radius;

        public SphereVolume(Vec3D scale, int resX, int resY, int resZ,
                int radius) {
            super(scale, resX, resY, resZ);
            cx = resX / 2;
            cy = resY / 2;
            cz = resZ / 2;
            this.radius = radius * radius;
        }

        @Override
        public float getVoxelAt(int index) {
            int z = index / sliceRes;
            int y = index % sliceRes / resX;
            int x = index % resX;
            return getVoxelAt(x, y, z);
        }

        public float getVoxelAt(int x, int y, int z) {
            if (x > 0 && x < resX1 && y > 0 && y < resY1 && z > 0 && z < resZ1) {
                x -= cx;
                y -= cy;
                z -= cz;
                return (x * x + y * y + z * z) < radius ? 1 : 0;
            }
            return 0;
        }

        public float getVoxelAt2(int x, int y, int z) {
            if (x > 0 && x < resX1 && y > 0 && y < resY1 && z > 0 && z < resZ1) {
                return (float) (SimplexNoise.noise(x * 0.05f, y * 0.05f,
                        z * 0.05f) * 0.5);
            }
            return 0;
        }

    }

    public static void main(String[] args) {
        PApplet.main(new String[] { "toxi.test.ImplicitVolume" });
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
    }

    public void setup() {
        size(1280, 720, OPENGL);
        gfx = new ToxiclibsSupport(this);
        VolumetricSpace vol =
                new SphereVolume(new Vec3D(400, 400, 400), 64, 64, 64, 32);
        IsoSurface surface = new HashIsoSurface(vol);
        mesh =
                (WETriangleMesh) surface.computeSurfaceMesh(
                        new WETriangleMesh(), 0.2f);
    }
}
