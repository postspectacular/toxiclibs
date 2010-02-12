package toxi.sim.dla;

import toxi.geom.Vec3D;

public interface DLAEventListener {

    void dlaAllSegmentsProcessed(DLA dla);

    void dlaNewParticleAdded(DLA dla, Vec3D p);

    void dlaSegmentSwitched(DLA dla, DLASegment s);
}
