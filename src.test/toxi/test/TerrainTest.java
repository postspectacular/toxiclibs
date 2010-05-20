package toxi.test;

import processing.core.PApplet;
import processing.core.PImage;
import toxi.geom.AABB;
import toxi.geom.IsectData;
import toxi.geom.ReadonlyVec3D;
import toxi.geom.Rect;
import toxi.geom.Vec2D;
import toxi.geom.Vec3D;
import toxi.geom.mesh.Terrain;
import toxi.geom.mesh.TriangleMesh;
import toxi.math.MathUtils;
import toxi.processing.ToxiclibsSupport;

public class TerrainTest extends PApplet {

    class Bot extends Vec2D {

        private Vec3D currNormal = new Vec3D(0, 1, 0);
        private float currTheta;
        private float targetTheta = HALF_PI;
        private float targetSpeed;
        private float speed;
        private Vec3D pos;
        private IsectData isec;

        public Bot(float x, float y) {
            super(x, y);
            pos = to3DXZ();
        }

        public void accelerate(float a) {
            targetSpeed += a;
            targetSpeed = MathUtils.clip(targetSpeed, -20, 20);
        }

        public void draw() {
            TriangleMesh box =
                    new AABB(new Vec3D(), new Vec3D(20, 10, 10)).toMesh();
            box.pointTowards(currNormal);
            box.rotateAroundAxis(currNormal, currTheta);
            box.translate(pos);
            fill(255, 0, 0);
            gfx.mesh(box);
        }

        public void steer(float t) {
            targetTheta += t;
        }

        public void update() {
            targetSpeed *= 0.992f;
            currTheta += (targetTheta - currTheta) * 0.1f;
            speed += (targetSpeed - speed) * 0.1f;
            addSelf(Vec2D.fromTheta(currTheta).scaleSelf(speed));
            AABB b = mesh.getBoundingBox();
            constrain(new Rect(b.getMin().to2DXZ().scale(0.99f), b.getMax()
                    .to2DXZ().scale(0.99f)));
            isec = terrain.intersectAtPoint(x, y);
            if (isec.isIntersection) {
                currNormal.interpolateToSelf(isec.normal, 0.25f);
                Vec3D newPos = isec.pos.add(0, 10, 0);
                pos.interpolateToSelf(newPos, 0.25f);
            }
        }
    }

    private static final float NOISE_SCALE = 0.08f;

    public static void main(String[] args) {
        PApplet.main(new String[] { "toxi.test.TerrainTest" });
    }

    private Terrain terrain;
    private ToxiclibsSupport gfx;
    private PImage img;
    private TriangleMesh mesh;
    private Bot bot;
    private ReadonlyVec3D camOffset = new Vec3D(0, 100, 300);
    private Vec3D eyePos = new Vec3D(0, 1000, 0);

    public void draw() {
        if (keyPressed) {
            if (keyCode == UP) {
                bot.accelerate(1);
            }
            if (keyCode == DOWN) {
                bot.accelerate(-1);
            }
            if (keyCode == LEFT) {
                bot.steer(0.1f);
            }
            if (keyCode == RIGHT) {
                bot.steer(-0.1f);
            }
        }
        bot.update();
        Vec3D camPos =
                bot.pos.add(camOffset.getRotatedY(bot.currTheta + HALF_PI));
        eyePos.interpolateToSelf(camPos, 0.05f);
        background(0xffaaeeff);
        camera(eyePos.x, eyePos.y, eyePos.z, bot.pos.x, bot.pos.y, bot.pos.z,
                0, -1, 0);
        directionalLight(192, 160, 128, 0, -1000, -0.5f);
        directionalLight(255, 64, 0, 0.5f, -0.1f, 0.5f);
        fill(255);
        noStroke();
        gfx.mesh(mesh, false);
        bot.draw();
    }

    public void setup() {
        size(800, 600, OPENGL);
        terrain = new Terrain(64, 64, 50);
        float[] el = new float[terrain.getWidth() * terrain.getDepth()];
        noiseDetail(8);
        for (int z = 0, i = 0; z < terrain.getDepth(); z++) {
            for (int x = 0; x < terrain.getWidth(); x++) {
                el[i++] = noise(x * NOISE_SCALE, z * NOISE_SCALE) * 400;
            }
        }
        terrain.setElevation(el);
        mesh = terrain.toMesh(0);
        // terrain = null;
        gfx = new ToxiclibsSupport(this);
        bot = new Bot(0, 0);
    }
}
