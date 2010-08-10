package toxi.geom;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import toxi.geom.mesh.TriangleMesh;
import toxi.math.MathUtils;

/**
 * Class to describe and work with infinite generic 3D planes. Useful for
 * intersection problems and classifying points.
 * 
 * @author Karsten Schmidt
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Plane extends Vec3D implements Shape3D {

    public static final Plane XY = new Plane(new Vec3D(), Vec3D.Z_AXIS);
    public static final Plane XZ = new Plane(new Vec3D(), Vec3D.Y_AXIS);
    public static final Plane YZ = new Plane(new Vec3D(), Vec3D.X_AXIS);

    /**
     * Classifier constant for {@link #classifyPoint(ReadonlyVec3D)}
     */
    public static final int PLANE_FRONT = -1;

    /**
     * Classifier constant for {@link #classifyPoint(ReadonlyVec3D)}
     */
    public static final int PLANE_BACK = 1;

    /**
     * Classifier constant for {@link #classifyPoint(ReadonlyVec3D)}
     */
    public static final int ON_PLANE = 0;

    @XmlElement(required = true)
    public Vec3D normal;

    public Plane() {
        super();
        normal = Vec3D.Y_AXIS.copy();
    }

    public Plane(Triangle t) {
        this(t.computeCentroid(), t.computeNormal());
    }

    public Plane(Vec3D origin, ReadonlyVec3D norm) {
        super(origin);
        normal = norm.getNormalized();
    }

    /**
     * Classifies the relative position of the given point to the plane.
     * 
     * @return One of the 3 integer classification codes: PLANE_FRONT,
     *         PLANE_BACK, ON_PLANE
     */
    public int classifyPoint(ReadonlyVec3D p) {
        float d = this.sub(p).dot(normal);
        if (d < -MathUtils.EPS) {
            return PLANE_FRONT;
        } else if (d > MathUtils.EPS) {
            return PLANE_BACK;
        }
        return ON_PLANE;
    }

    public boolean containsPoint(ReadonlyVec3D p) {
        return classifyPoint(p) == ON_PLANE;
    }

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
    public ReadonlyVec3D getIntersectionWithRay(Ray3D r) {
        float denom = normal.dot(r.getDirection());
        if (denom > MathUtils.EPS) {
            float u = normal.dot(this.sub(r)) / denom;
            return r.getPointAtDistance(u);
        } else {
            return null;
        }
    }

    /**
     * Calculates the distance of the vector to the given plane in the specified
     * direction. A plane is specified by a 3D point and a normal vector
     * perpendicular to the plane. Normalized directional vectors expected (for
     * rayDir and planeNormal).
     * 
     * @param ray
     *            intersection ray
     * @return distance to plane in world units, -1 if no intersection.
     */
    public float intersectRayDistance(Ray3D ray) {
        float d = -normal.dot(this);
        float numer = normal.dot(ray) + d;
        float denom = normal.dot(ray.dir);

        // normal is orthogonal to vector, cant intersect
        if (MathUtils.abs(denom) < MathUtils.EPS) {
            return -1;
        }

        return -(numer / denom);
    }

    /**
     * Creates a TriangleMesh representation of the plane as a finite, squared
     * quad of the requested size, centred around the current plane point.
     * 
     * @param size
     *            desired edge length
     * @return mesh
     */
    public TriangleMesh toMesh(float size) {
        ReadonlyVec3D p =
                equalsWithTolerance(Vec3D.ZERO, 0.01f) ? add(0.01f, 0.01f,
                        0.01f) : this;
        size *= 0.5f;
        Vec3D n = p.cross(normal).normalizeTo(size);
        Vec3D m = n.cross(normal).normalizeTo(size);
        Vec3D a = this.add(n).addSelf(m);
        Vec3D b = this.add(n).subSelf(m);
        Vec3D c = this.sub(n).subSelf(m);
        Vec3D d = this.sub(n).addSelf(m);
        TriangleMesh mesh = new TriangleMesh("plane", 4, 2);
        mesh.addFace(a, d, b, null);
        mesh.addFace(b, d, c, null);
        return mesh;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("origin: ").append(super.toString()).append(" norm: ")
                .append(normal.toString());
        return sb.toString();
    }
}
