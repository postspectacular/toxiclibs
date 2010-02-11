package toxi.sim.dla;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Logger;

import toxi.geom.Line3D;
import toxi.geom.Vec3D;

public class DLAGuideLines {

    protected static final Logger logger =
            Logger.getLogger(DLAGuideLines.class.getName());

    public SortedSet<DLASegment> segments;

    public Iterator<DLASegment> iterator;

    private double currT;

    private DLASegment currSegment;

    private Vec3D currPoint;

    private Vec3D workDir;

    public DLAGuideLines() {
        this(new PipelineOrder());
    }

    public DLAGuideLines(Comparator<Line3D> comparator) {
        segments = new TreeSet<DLASegment>(comparator);
    }

    public void addLineStrip(List<Vec3D> points) {
        int numP = points.size();
        for (int i = 1; i < numP; i++) {
            Vec3D p = i < numP - 1 ? points.get(i + 1) : null;
            DLASegment s = new DLASegment(points.get(i - 1), points.get(i), p);
            logger.info("adding segment: " + s);
            segments.add(s);
        }
        System.out.println(segments.size() + " segments");
    }

    public Vec3D getDirection() {
        if (workDir == null) {
            getPoint();
        }
        return workDir;
    }

    public Vec3D getPoint() {
        workDir =
                currSegment.getDirection().interpolateToSelf(
                        currSegment.getNextDirection(), (float) currT);
        workDir.normalize();
        Vec3D v =
                currPoint.add(workDir.scale(currSegment.getLength()
                        * (float) currT));
        return v;
    }

    public boolean isComplete() {
        return !iterator.hasNext() && currT >= 1.0;
    }

    public DLAGuideLines reset() {
        iterator = segments.iterator();
        currT = 0;
        currSegment = iterator.next();
        currPoint = currSegment.a.copy();
        return this;
    }

    public DLASegment updatePoint(double delta) {
        currT += delta;
        if (currT >= 1.0) {
            if (iterator.hasNext()) {
                currT -= 1.0;
                currSegment = iterator.next();
                currPoint = currSegment.a.copy();
            }
        }
        return currSegment;
    }
}
