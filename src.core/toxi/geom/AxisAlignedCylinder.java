package toxi.geom;

import toxi.geom.mesh.TriangleMesh;

public abstract class AxisAlignedCylinder implements Shape3D {

    protected Vec3D pos;
    protected float radius;
    protected float radiusSquared;
    protected float length;

    protected AxisAlignedCylinder(ReadonlyVec3D pos, float radius, float length) {
        this.pos = pos.copy();
        setRadius(radius);
        setLength(length);
    }

    /**
     * Checks if the given point is inside the cylinder.
     * 
     * @param p
     * @return true, if inside
     */
    public abstract boolean containsPoint(ReadonlyVec3D p);

    /**
     * @return the length
     */
    public float getLength() {
        return length;
    }

    /**
     * @return the cylinder's orientation axis
     */
    public abstract ReadonlyVec3D getMajorAxis();

    /**
     * Returns the cylinder's position (centroid).
     * 
     * @return the pos
     */
    public Vec3D getPosition() {
        return pos.copy();
    }

    /**
     * @return the cylinder radius
     */
    public float getRadius() {
        return radius;
    }

    /**
     * @param length
     *            the length to set
     */
    public void setLength(float length) {
        this.length = length;
    }

    /**
     * @param pos
     *            the pos to set
     */
    public void setPosition(Vec3D pos) {
        this.pos.set(pos);
    }

    /**
     * @param radius
     */
    public void setRadius(float radius) {
        this.radius = radius;
        this.radiusSquared = radius * radius;
    }

    /**
     * Builds a TriangleMesh representation of the cylinder at a default
     * resolution 30 degrees.
     * 
     * @return mesh instance
     */
    public TriangleMesh toMesh() {
        return toMesh(12, 0);
    }

    /**
     * Builds a TriangleMesh representation of the cylinder using the given
     * number of steps and start angle offset.
     * 
     * @param steps
     * @param thetaOffset
     * @return mesh
     */
    public TriangleMesh toMesh(int steps, float thetaOffset) {
        return new Cone(pos, getMajorAxis(), radius, radius, length).toMesh(
                "cylinder", steps, thetaOffset, true, true);
    }
}