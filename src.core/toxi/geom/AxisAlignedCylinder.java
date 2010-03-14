package toxi.geom;

import toxi.geom.mesh.TriangleMesh;

public interface AxisAlignedCylinder {

    /**
     * Checks if the given point is inside the cylinder.
     * 
     * @param p
     * @return true, if inside
     */
    public boolean containsPoint(Vec3D p);

    /**
     * @return the length
     */
    public float getLength();

    /**
     * @return the cylinder's orientation axis
     */
    public Vec3D.Axis getMajorAxis();

    /**
     * Returns the cylinder's position (centroid).
     * 
     * @return the pos
     */
    public Vec3D getPosition();

    /**
     * @return the cylinder radius
     */
    public float getRadius();

    /**
     * @param length
     *            the length to set
     */
    public void setLength(float length);

    /**
     * @param pos
     *            the pos to set
     */
    public void setPosition(Vec3D pos);

    /**
     * @param radius
     */
    public void setRadius(float radius);

    /**
     * Builds a TriangleMesh representation of the cylinder at a default
     * resolution 30 degrees.
     * 
     * @return mesh instance
     */
    public TriangleMesh toMesh();

    /**
     * Builds a TriangleMesh representation of the cylinder using the given
     * number of steps and start angle offset.
     * 
     * @param steps
     * @param thetaOffset
     * @return mesh
     */
    public TriangleMesh toMesh(int steps, float thetaOffset);
}