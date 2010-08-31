package toxi.test;

import processing.core.PApplet;
import toxi.geom.Vec3D;
import toxi.geom.mesh.LaplacianSmooth;
import toxi.geom.mesh.WETriangleMesh;
import toxi.processing.ToxiclibsSupport;
import toxi.volume.HashIsoSurface;
import toxi.volume.IsoSurface;
import toxi.volume.VolumetricSpace;

public class ImplicitVolume extends PApplet {

    public class EvaluatingVolume extends VolumetricSpace {

        private float upperBound;

        public EvaluatingVolume(Vec3D scale, int resX, int resY, int resZ,
                float upperBound) {
            super(scale, resX, resY, resZ);
            this.upperBound = upperBound;
        }

        @Override
        public float getVoxelAt(int index) {
            int z = index / sliceRes;
            int y = index % sliceRes / resX;
            int x = index % resX;
            return getVoxelAt(x, y, z);
        }

        public float getVoxelAt(int x, int y, int z) {
            float val = 0;
            if (x > 0 && x < resX1 && y > 0 && y < resY1 && z > 0 && z < resZ1) {
                float xx = (float) x / resX - 0.5f;
                float yy = (float) y / resY - 0.5f;
                float zz = (float) z / resZ - 0.5f;
                val = sin(xx * PI * 8) + cos(yy * PI * 8) + sin(zz * PI * 8);
                if (val > upperBound) {
                    val = 0;
                }
            }
            return val;
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
                new EvaluatingVolume(new Vec3D(400, 400, 400), 64, 64, 64, 0.4f);
        IsoSurface surface = new HashIsoSurface(vol);
        mesh =
                (WETriangleMesh) surface.computeSurfaceMesh(
                        new WETriangleMesh(), 0.2f);
    }
}
