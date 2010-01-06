package toxi.physics.constraints;

import toxi.geom.AxisAlignedCylinder;
import toxi.geom.Vec3D;
import toxi.physics.VerletParticle;

public class CylinderConstraint implements ParticleConstraint {

    protected AxisAlignedCylinder cylinder;
    protected Vec3D centroid = new Vec3D();
    protected Vec3D.Axis axis;

    public CylinderConstraint(AxisAlignedCylinder cylinder) {
        setCylinder(cylinder);
    }

    public void apply(VerletParticle p) {
        if (cylinder.containsPoint(p)) {
            centroid.setComponent(axis, p.getComponent(axis));
            p.set(centroid.add(p.sub(centroid)
                    .normalizeTo(cylinder.getRadius())));
        }
    }

    /**
     * @return the cylinder
     */
    public AxisAlignedCylinder getCylinder() {
        return cylinder;
    }

    /**
     * @param cylinder
     *            the cylinder to set
     */
    public void setCylinder(AxisAlignedCylinder cylinder) {
        this.cylinder = cylinder;
        centroid.set(cylinder.getPosition());
        axis = cylinder.getMajorAxis();
    }
}
