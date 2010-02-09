package toxi.sim.dla;

public interface DLAEventListener {

	void dlaAllSegmentsProcessed(DLA dla);

	void dlaNewParticleAdded(DLA dla, DLAParticle p);

	void dlaSegmentSwitched(DLA dla, DLASegment s);
}
