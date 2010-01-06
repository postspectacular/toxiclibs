package toxi.geom;

import toxi.math.MathUtils;

public class ZAxisCylinder implements AxisAlignedCylinder {

    protected Vec3D pos;
    protected float radius;
    protected float radiusSquared;
    protected float length;

    public ZAxisCylinder(Vec3D pos, float radius, float length) {
        this.pos = pos;
        setRadius(radius);
        this.length = length;
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.geom.AxisAlignedCylinder#containsPoint(toxi.geom.Vec3D)
     */
    public boolean containsPoint(Vec3D p) {
        if (MathUtils.abs(p.z - pos.z) < length * 0.5) {
            float dx = p.x - pos.x;
            float dy = p.y - pos.y;
            if (Math.abs(dx * dx + dy * dy) < radiusSquared) {
                return true;
            }
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.geom.AxisAlignedCylinder#getLength()
     */
    public float getLength() {
        return length;
    }

    public Vec3D.Axis getMajorAxis() {
        return Vec3D.Axis.Z;
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.geom.AxisAlignedCylinder#getPosition()
     */
    public Vec3D getPosition() {
        return pos.copy();
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.geom.AxisAlignedCylinder#getRadius()
     */
    public float getRadius() {
        return radius;
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.geom.AxisAlignedCylinder#setLength(float)
     */
    public void setLength(float length) {
        this.length = length;
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.geom.AxisAlignedCylinder#setPos(toxi.geom.Vec3D)
     */
    public void setPosition(Vec3D pos) {
        this.pos.set(pos);
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.geom.AxisAlignedCylinder#setRadius(float)
     */
    public void setRadius(float radius) {
        this.radius = radius;
        this.radiusSquared = radius * radius;
    }
}
