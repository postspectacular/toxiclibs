package toxi.sim.dla;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import toxi.geom.PointOctree;
import toxi.geom.Spline3D;
import toxi.geom.Vec3D;

public class DLATest extends PApplet implements DLAEventListener {

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
        System.out.println("new particle: " + p);
    }

    public void dlaSegmentSwitched(DLA dla, DLASegment s) {
        System.out.println("new segment: " + s);
    }

    public void draw() {
        background(255);
        for (int i = 0; i < 10000; i++) {
            dla.update();
        }
        translate(width / 2, height / 2, 0);
        rotateX(mouseY * 0.01f);
        rotateY(mouseX * 0.01f);
        scale(currScale);
        drawOctree(dla.octree);
        // drawOctree(dla.octreeCurve);
    }

    void drawOctree(PointOctree oc) {
        if (oc.getNumChildren() > 0) {
            noFill();
            stroke(0, oc.getDepth() * 16);
            pushMatrix();
            translate(oc.x, oc.y, oc.z);
            box(oc.getSize());
            popMatrix();
            PointOctree[] children = oc.getChildren();
            for (int i = 0; i < 8; i++) {
                if (children[i] != null) {
                    drawOctree(children[i]);
                }
            }
        } else {
            List<Vec3D> points = oc.getPoints();
            if (points != null) {
                stroke(255, 0, 0);
                beginShape(POINTS);
                int numP = points.size();
                for (int i = 0; i < numP; i += 5) {
                    Vec3D p = points.get(i);
                    vertex(p.x, p.y, p.z);
                }
                endShape();
            }
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
            dla.save(sketchPath("test.dla"), false);
        }
    }

    public void setup() {
        size(640, 480, OPENGL);
        ArrayList<Vec3D> points = new ArrayList<Vec3D>();
        points.add(new Vec3D(0, 0, 0));
        points.add(new Vec3D(30, 20, 30));
        points.add(new Vec3D(40, -10, 0));
        points.add(new Vec3D(-30, 20, -30));
        points.add(new Vec3D(-40, -10, 0));
        DLAGuideLines guides = new DLAGuideLines();
        guides.addLineStrip(new Spline3D(points).computeVertices(16));
        dla = new DLA(128, guides);
        dla.octree.setMinNodeSize(2);
        // dla.addListener(this);
    }
}
