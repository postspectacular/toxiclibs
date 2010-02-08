package toxi.geom;

import java.util.ArrayList;

import toxi.geom.Line2D.LineIntersection.Type;

public class Line2D {

	public static class LineIntersection {

		public static enum Type {
			COINCIDENT, PARALLEL, NON_INTERSECTING, INTERSECTING
		}

		private final Type type;
		private final Vec2D pos;

		public LineIntersection(Type type, Vec2D pos) {
			this.type = type;
			this.pos = pos;
		}

		/**
		 * @return the pos
		 */
		public Vec2D getPos() {
			return pos.copy();
		}

		/**
		 * @return the type
		 */
		public Type getType() {
			return type;
		}

		public String toString() {
			return "type: " + type + " pos: " + pos;
		}
	}

	public Vec2D a, b;

	public Line2D(Vec2D a, Vec2D b) {
		this.a = a;
		this.b = b;
	}

	public Line2D copy() {
		return new Line2D(a.copy(), b.copy());
	}

	public Vec2D getNormal() {
		return b.sub(a).perpendicular();
	}

	/**
	 * Computes intersection between this and the given line. The returned value
	 * is a {@link LineIntersection} instance and contains both the type of
	 * intersection as well as the intersection point (if existing).
	 * 
	 * Based on: http://local.wasp.uwa.edu.au/~pbourke/geometry/lineline2d/
	 * 
	 * @param l
	 *            line to intersect with
	 * @return intersection result
	 */
	public LineIntersection intersectLine(Line2D l) {
		LineIntersection isec = null;
		float denom = (l.b.y - l.a.y) * (b.x - a.x) - (l.b.x - l.a.x)
				* (b.y - a.y);

		float na = (l.b.x - l.a.x) * (a.y - l.a.y) - (l.b.y - l.a.y)
				* (a.x - l.a.x);
		float nb = (b.x - a.x) * (a.y - l.a.y) - (b.y - a.y) * (a.x - l.a.x);

		if (denom != 0.0) {
			float ua = na / denom;
			float ub = nb / denom;
			if (ua >= 0.0f && ua <= 1.0 && ub >= 0.0 && ub <= 1.0) {
				isec = new LineIntersection(Type.INTERSECTING, a.interpolateTo(
						b, ua));
			} else {
				isec = new LineIntersection(Type.NON_INTERSECTING, null);
			}
		} else {
			if (na == 0.0 && nb == 0.0) {
				isec = new LineIntersection(Type.COINCIDENT, null);
			} else {
				isec = new LineIntersection(Type.COINCIDENT, null);
			}
		}
		return isec;
	}

	public ArrayList<Vec2D> splitIntoSegments(ArrayList<Vec2D> segments,
			float stepLength, boolean addFirst) {
		return splitIntoSegments(a, b, stepLength, segments, addFirst);
	}

	public Ray2D toRay2D() {
		return new Ray2D(a.copy(), b.sub(a).normalize());
	}

	/**
	 * Splits the line between A and B into segments of the given length,
	 * starting at point A. The tweened points are added to the given result
	 * list. The last point added is B itself and hence it is likely that the
	 * last segment has a shorter length than the step length requested. The
	 * first point (A) can be omitted and not be added to the list if so
	 * desired.
	 * 
	 * @param a
	 *            start point
	 * @param b
	 *            end point (always added to results)
	 * @param stepLength
	 *            desired distance between points
	 * @param segments
	 *            existing array list for results (or a new list, if null)
	 * @param addFirst
	 *            false, if A is NOT to be added to results
	 * @return list of result vectors
	 */
	public static final ArrayList<Vec2D> splitIntoSegments(Vec2D a, Vec2D b,
			float stepLength, ArrayList<Vec2D> segments, boolean addFirst) {
		if (segments == null) {
			segments = new ArrayList<Vec2D>();
		}
		if (addFirst) {
			segments.add(a.copy());
		}
		float dist = a.distanceTo(b);
		if (dist > stepLength) {
			Vec2D pos = a.copy();
			Vec2D step = b.sub(a).limit(stepLength);
			while (dist > stepLength) {
				pos.addSelf(step);
				segments.add(pos.copy());
				dist -= stepLength;
			}
		}
		segments.add(b.copy());
		return segments;
	}
}
