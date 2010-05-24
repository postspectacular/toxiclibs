package toxi.geom;

import toxi.math.MathUtils;

public class XAxisCylinder extends AxisAlignedCylinder {

    public XAxisCylinder(ReadonlyVec3D pos, float radius, float length) {
        super(pos, radius, length);
    }

    public boolean containsPoint(ReadonlyVec3D p) {
        if (MathUtils.abs(p.x() - pos.x) < length * 0.5) {
            float dy = p.y() - pos.y;
            float dz = p.z() - pos.z;
            if (Math.abs(dz * dz + dy * dy) < radiusSquared) {
                return true;
            }
        }
        return false;
    }

    public Vec3D.Axis getMajorAxis() {
        return Vec3D.Axis.X;
    }
}
