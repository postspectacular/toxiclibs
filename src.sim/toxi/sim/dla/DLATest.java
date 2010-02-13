package toxi.sim.dla;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import toxi.geom.PointOctree;
import toxi.geom.Spline3D;
import toxi.geom.Vec3D;
import toxi.geom.Vec3D.Axis;

public class DLATest extends PApplet implements DLAEventListener {

    class CustomDLA extends DLA {

        public CustomDLA(float size, DLAGuideLines guides) {
            super(size, guides);
        }

        public PointOctree getOctree() {
            return octree;
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        PApplet.main(new String[] { "toxi.sim.dla.DLATest" });
    }

    private DLA dla;

    private float currScale = 6;

    public void dlaAllSegmentsProcessed(DLA dla) {
        System.out.println("all done.");
    }

    public void dlaNewParticleAdded(DLA dla, Vec3D p) {
        // System.out.println("new particle: " + p);
    }

    public void dlaSegmentSwitched(DLA dla, DLASegment s) {
        System.out.println("new segment: " + s);
    }

    public void draw() {
        background(255);
        for (int i = 0; i < 10000; i++) {
            dla.update();
        }
        fill(0);
        text(
                "t: "
                        + nf((float) dla.getGuidelines().getCurrentSweepPos(),
                                1, 5), 20, 20);
        text("num: " + dla.numParticles, 20, 40);
        translate(width / 2, height / 2, 0);
        rotateX(mouseY * 0.01f);
        rotateY(mouseX * 0.01f);
        scale(currScale);
        drawOctree(dla.octree, true);
        drawOctree(dla.octreeGuides, false);
    }

    void drawOctree(PointOctree oc, boolean doShowGrid) {
        if (oc.getNumChildren() > 0 && doShowGrid) {
            noFill();
            stroke(0, oc.getDepth() * 16);
            pushMatrix();
            translate(oc.x, oc.y, oc.z);
            box(oc.getSize());
            popMatrix();
            PointOctree[] children = oc.getChildren();
            for (int i = 0; i < 8; i++) {
                if (children[i] != null) {
                    drawOctree(children[i], doShowGrid);
                }
            }
        } else {
            List<Vec3D> points = oc.getPoints();
            stroke(255, 0, 0);
            beginShape(POINTS);
            int numP = points.size();
            for (int i = 0; i < numP; i += 100) {
                Vec3D p = points.get(i);
                vertex(p.x, p.y, p.z);
            }
            endShape();
        }
    }

    public void keyPressed() {
        if (key == '-') {
            currScale -= 0.1f;
        }
        if (key == '=') {
            currScale += 0.1f;
        }
        if (key == 'x') {
            dla.save(sketchPath("spiral.dla"), false);
        }
    }

    public void setup() {
        size(640, 480, OPENGL);
        ArrayList<Vec3D> points = new ArrayList<Vec3D>();
        for (float theta = -1 * TWO_PI, r = 20; theta < 3 * TWO_PI; theta +=
                PI * 0.25) {
            points.add(Vec3D.fromXYTheta(theta).scale(r).setComponent(Axis.Z,
                    theta * 4));
            r *= 0.92;
        }
        DLAGuideLines guides = new DLAGuideLines();
        guides.addCurveStrip(new Spline3D(points).computeVertices(16));
        dla = new DLA(128, guides);
        // dla.addListener(this);
        textFont(createFont("SansSerif", 12));
    }
}
