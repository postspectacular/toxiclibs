package toxi.geom;

/**
 * An immutable origin + axis in 3D-Space.
 */
public class Axis3D {

    /**
     * Creates a new x-Axis3D object from the world origin.
     */
    public static final Axis3D xAxis() {
        return new Axis3D(Vec3D.X_AXIS);
    }

    /**
     * Creates a new y-Axis3D object from the world origin.
     */
    public static final Axis3D yAxis() {
        return new Axis3D(Vec3D.Y_AXIS);
    }

    /**
     * Creates a new z-Axis3D object from the world origin.
     */
    public static final Axis3D zAxis() {
        return new Axis3D(Vec3D.Z_AXIS);
    }

    public final ReadonlyVec3D origin;
    public final ReadonlyVec3D dir;

    /**
     * Creates a new z-Axis3D object from the world origin.
     */
    public Axis3D() {
        this(Vec3D.Z_AXIS);
    }

    public Axis3D(float x, float y, float z) {
        this(new Vec3D(x, y, z));
    }

    public Axis3D(Ray3D ray) {
        this(ray, ray.getDirection());
    }

    /**
     * Creates a new Axis3D from the world origin in the given direction.
     * 
     * @param dir
     *            direction vector
     */
    public Axis3D(ReadonlyVec3D dir) {
        this(new Vec3D(), dir);
    }

    /**
     * Creates a new Axis3D from the given origin and direction.
     * 
     * @param o
     *            origin
     * @param dir
     *            direction
     */
    public Axis3D(ReadonlyVec3D o, ReadonlyVec3D dir) {
        this.origin = o;
        this.dir = dir.getNormalized();
    }
}
