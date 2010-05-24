package toxi.geom;

import toxi.math.MathUtils;

public class ZAxisCylinder extends AxisAlignedCylinder {

    public ZAxisCylinder(ReadonlyVec3D pos, float radius, float length) {
        super(pos, radius, length);
    }

    public boolean containsPoint(ReadonlyVec3D p) {
        if (MathUtils.abs(p.z() - pos.z) < length * 0.5) {
            float dx = p.x() - pos.x;
            float dy = p.y() - pos.y;
            if (Math.abs(dx * dx + dy * dy) < radiusSquared) {
                return true;
            }
        }
        return false;
    }

    public Vec3D.Axis getMajorAxis() {
        return Vec3D.Axis.Z;
    }
}
