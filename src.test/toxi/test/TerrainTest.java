package toxi.test;

import processing.core.PApplet;
import processing.core.PImage;
import toxi.geom.AABB;
import toxi.geom.Ray3D;
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

        public Bot(float x, float y) {
            super(x, y);
            pos = to3DXZ();
        }

        public void accelerate(float a) {
            targetSpeed += a;
            targetSpeed = MathUtils.clip(targetSpeed, -10, 10);
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
            targetSpeed *= 0.99f;
            currTheta += (targetTheta - currTheta) * 0.1f;
            speed += (targetSpeed - speed) * 0.1f;
            addSelf(Vec2D.fromTheta(currTheta).scaleSelf(speed));
            AABB b = mesh.getBoundingBox();
            constrain(new Rect(b.getMin().to2DXZ().scale(0.99f), b.getMax()
                    .to2DXZ().scale(0.99f)));
            Ray3D ray = new Ray3D(new Vec3D(x, 1000, y), new Vec3D(0, -1, 0));
            if (mesh.intersectsRay(ray)) {
                currNormal.interpolateToSelf(mesh.getIntersectionData().normal,
                        0.25f);
                Vec3D newPos = mesh.getIntersectionData().pos.add(0, 10, 0);
                pos.interpolateToSelf(newPos, 0.25f);
            }
        }
    }

    public static void main(String[] args) {
        PApplet.main(new String[] { "toxi.test.TerrainTest" });
    }

    private Terrain terrain;
    private ToxiclibsSupport gfx;
    private PImage img;
    private TriangleMesh mesh;
    private Bot bot;
    private Vec3D camOffset = new Vec3D(0, 100, 300);
    private Vec3D eyePos = new Vec3D();

    public void draw() {
        background(0);
        lights();
        bot.update();
        Vec3D camPos =
                bot.pos.add(camOffset.getRotatedY(bot.currTheta + HALF_PI));
        eyePos.interpolateToSelf(camPos, 0.05f);
        camera(eyePos.x, eyePos.y, eyePos.z, bot.pos.x, bot.pos.y, bot.pos.z,
                0, -1, 0);

        // translate(width / 2, height / 2, 0);
        // rotateX(0.8f * PI);
        // rotateY(frameCount * 0.01f);
        // rotateX(mouseY * 0.01f);
        // rotateY(mouseX * 0.01f);
        fill(255);
        noStroke();
        gfx.mesh(mesh, true);
        bot.draw();
    }

    public void keyPressed() {
        if (key == 'w') {
            bot.accelerate(0.5f);
        }
        if (key == 's') {
            bot.accelerate(-0.5f);
        }
        if (key == 'a') {
            bot.steer(0.1f);
        }
        if (key == 'd') {
            bot.steer(-0.1f);
        }
    }

    public void setup() {
        size(800, 600, OPENGL);
        img = loadImage("terrain.jpg");
        terrain = new Terrain(img.width, img.height, 50);
        float[] el = new float[img.pixels.length];
        for (int i = 0; i < el.length; i++) {
            el[i] = brightness(img.pixels[i]) / 255f * 60;
        }
        terrain.setElevation(el);
        mesh = terrain.toMesh(0);
        terrain = null;
        gfx = new ToxiclibsSupport(this);
        bot = new Bot(0, 0);
    }
}
