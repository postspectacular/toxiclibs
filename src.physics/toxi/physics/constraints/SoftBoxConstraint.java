package toxi.physics.constraints;

import java.util.LinkedList;
import java.util.List;

import toxi.geom.AABB;
import toxi.geom.Vec3D.Axis;
import toxi.physics.VerletParticle;

public class SoftBoxConstraint implements ParticleConstraint {

    public AABB box;
    public List<Axis> axes = new LinkedList<Axis>();
    public float smooth;

    public SoftBoxConstraint(AABB box, float smooth) {
        this.box = box;
        this.smooth = smooth;
    }

    public SoftBoxConstraint addAxis(Axis a) {
        axes.add(a);
        return this;
    }

    public void apply(VerletParticle p) {
        if (p.isInAABB(box)) {
            for (Axis a : axes) {
                float val = p.getComponent(a);
                p.setComponent(a, val + (box.getComponent(a) - val) * smooth);
            }
        }
    }

}
