package toxi.geom;

import toxi.math.MathUtils;

/**
 * Class to describe and work with infinite generic 3D planes. Useful for
 * intersection problems and classifying points.
 * 
 * @author Karsten Schmidt
 * 
 */
public class Plane extends Vec3D {

	public static final Plane XY = new Plane(new Vec3D(), Vec3D.Z_AXIS);
	public static final Plane XZ = new Plane(new Vec3D(), Vec3D.Y_AXIS);
	public static final Plane YZ = new Plane(new Vec3D(), Vec3D.X_AXIS);

	/**
	 * Classifier constant for {@link #classifyPoint(Vec3D)}
	 */
	public static final int PLANE_FRONT = -1;

	/**
	 * Classifier constant for {@link #classifyPoint(Vec3D)}
	 */
	public static final int PLANE_BACK = 1;

	/**
	 * Classifier constant for {@link #classifyPoint(Vec3D)}
	 */
	public static final int ON_PLANE = 0;

	public Vec3D normal;

	public Plane(Vec3D origin, Vec3D norm) {
		super(origin);
		normal = norm.getNormalized();
	}

	// TODO add constructor for creating a plane from a Triangle or 3 Vec3D's

	/**
	 * Calculates distance from the plane to point P.
	 * 
	 * @param p
	 * @return distance
	 */
	public float getDistanceToPoint(Vec3D p) {
		float sn = -normal.dot(p.sub(this));
		float sd = normal.magSquared();
		Vec3D isec = p.add(normal.scale(sn / sd));
		return isec.distanceTo(p);
	}

	/**
	 * Calculates the intersection point between plane and ray (line).
	 * 
	 * @param r
	 * @return intersection point or null if ray doesn't intersect plane
	 */
	public Vec3D getIntersectionWithRay(Ray3D r) {
		float denom = normal.dot(r.getDirection());
		if (denom > MathUtils.EPS) {
			float u = normal.dot(this.sub(r)) / denom;
			return r.getPointAtDistance(u);
		} else
			return null;
	}

	/**
	 * Checks and classifies the relative position of the point to the given
	 * plane.
	 * 
	 * @param pO
	 *            plane origin
	 * @param pN
	 *            plane normal vector
	 * @return One of the 3 integer classification codes: PLANE_FRONT,
	 *         PLANE_BACK, ON_PLANE
	 */
	public int classifyPoint(Vec3D p) {
		float d = this.sub(p).dot(normal);
		if (d < -MathUtils.EPS)
			return PLANE_FRONT;
		else if (d > MathUtils.EPS)
			return PLANE_BACK;

		return ON_PLANE;
	}
	
	public String toString() {
		StringBuffer sb=new StringBuffer();
		sb.append("origin: ").append(super.toString()).append(" norm: ").append(normal.toString());
		return sb.toString();
	}
}
