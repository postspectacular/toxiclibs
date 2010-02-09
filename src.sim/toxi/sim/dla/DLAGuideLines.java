package toxi.sim.dla;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import toxi.geom.Line3D;
import toxi.geom.Vec3D;

public class DLAGuideLines {

	public SortedSet<DLASegment> segments;

	public Iterator<DLASegment> iterator;

	private double currT;

	private DLASegment currSegment;

	private Vec3D currPoint;

	private Vec3D workDir;

	public DLAGuideLines() {
		this(new CircularComparator());
	}

	public DLAGuideLines(Comparator<Line3D> comparator) {
		segments = new TreeSet<DLASegment>(comparator);
	}

	public void addLineStrip(ArrayList<Vec3D> points) {
		Vec3D a = null;
		Vec3D b = null;
		for (Vec3D p : points) {
			if (b != null) {
				DLASegment l = new DLASegment(a, b, p);
				segments.add(l);
			}
			a = b;
			b = p;
		}
	}

	public Vec3D getDirection() {
		if (workDir == null) {
			getPoint();
		}
		return workDir;
	}

	public Vec3D getPoint() {
		workDir = currSegment.getDirection().interpolateToSelf(
				currSegment.getNextDirection(), (float) currT);
		workDir.normalize();
		Vec3D v = currPoint.add(workDir.scale(currSegment.getLength()
				* (float) currT));
		return v;
	}

	public boolean isComplete() {
		return !iterator.hasNext();
	}

	public DLAGuideLines reset() {
		iterator = segments.iterator();
		currT = 0;
		currSegment = iterator.next();
		currPoint = currSegment.a.copy();
		return this;
	}

	public DLASegment updatePoint(float delta) {
		currT += delta;
		if (currT >= 1.0) {
			currT -= 1.0;
			if (iterator.hasNext()) {
				currSegment = iterator.next();
				currPoint = currSegment.a.copy();
			}
		}
		return currSegment;
	}
}
