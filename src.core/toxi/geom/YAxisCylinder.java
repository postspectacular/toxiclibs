package toxi.geom;

import toxi.math.MathUtils;

public class YAxisCylinder extends AxisAlignedCylinder {

    public YAxisCylinder(ReadonlyVec3D pos, float radius, float length) {
        super(pos, radius, length);
    }

    public boolean containsPoint(ReadonlyVec3D p) {
        if (MathUtils.abs(p.y() - pos.y) < length * 0.5) {
            float dx = p.x() - pos.x;
            float dz = p.z() - pos.z;
            if (Math.abs(dz * dz + dx * dx) < radiusSquared) {
                return true;
            }
        }
        return false;
    }

    public ReadonlyVec3D getMajorAxis() {
        return Vec3D.Y_AXIS;
    }

}
