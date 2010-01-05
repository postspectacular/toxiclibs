package toxi.physics.constraints;

import toxi.geom.Vec3D;
import toxi.geom.ZAxisCylinder;
import toxi.physics.VerletParticle;

public class ZAxisCylinderConstraint implements ParticleConstraint {

    protected ZAxisCylinder cylinder;
    protected Vec3D temp = new Vec3D();

    public ZAxisCylinderConstraint(ZAxisCylinder cylinder) {
        this.cylinder = cylinder;
        temp = cylinder.getPosition();
    }

    public void apply(VerletParticle p) {
        if (cylinder.containsPoint(p)) {
            temp.z = p.z;
            p.set(temp.add(p.sub(temp).normalizeTo(cylinder.getRadius())));
        }
    }

    /**
     * @return the cylinder
     */
    public ZAxisCylinder getCylinder() {
        return cylinder;
    }

    /**
     * @param cylinder
     *            the cylinder to set
     */
    public void setCylinder(ZAxisCylinder cylinder) {
        this.cylinder = cylinder;
        temp.set(cylinder.getPosition());
    }
}
