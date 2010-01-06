package toxi.geom;

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

    public Vec3D.Axis getMajorAxis();

    /**
     * Returns the cylinder's position (centroid).
     * 
     * @return the pos
     */
    public Vec3D getPosition();

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

    public void setRadius(float radius);
}