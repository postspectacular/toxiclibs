package toxi.sim.dla;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import toxi.geom.PointOctree;
import toxi.geom.Vec3D;
import toxi.math.MathUtils;

public class DLA {

	protected static final Logger logger = Logger
			.getLogger(DLA.class.getName());

	protected int numParticles;
	protected DLAParticle currParticle;
	protected PointOctree octree, octreeCurve;

	protected Vec3D currCurvePoint;
	protected Vec3D dirCurvePoint;
	protected Vec3D minBounds, maxBounds;

	protected DLAGuideLines outline;
	protected ArrayList<DLASegment> activeSegments = new ArrayList<DLASegment>();

	protected List<DLAEventListener> listeners = new ArrayList<DLAEventListener>();

	protected int numActiveSegments = 0;

	protected float snapDistance;
	protected float snapDistanceSquared;
	protected float curveAttachDistanceSquared;
	protected float curveSnapDistanceSquared;

	protected float curveStepLength;

	protected float spawnRadius = 12;
	protected float escapeRadius = 36;
	protected float particleRadius = 0.25f;

	protected float stickiness = 0.2f;
	protected float curveAlign = 0.85f;

	protected float curveSpeed;
	protected float searchSpeed;
	protected float particleSpeed = 0.01f;

	protected float continousGrowthRatio;

	protected boolean isComplete;

	public DLA(float size) {
		octree = createOctree(new Vec3D(), size);
		octreeCurve = createOctree(new Vec3D(), size);
		minBounds = Vec3D.MAX_VALUE.copy();
		maxBounds = Vec3D.MIN_VALUE.copy();
	}

	public DLA addListener(DLAEventListener l) {
		listeners.add(l);
		logger.info("adding listener: " + l);
		return this;
	}

	protected void addParticle(DLAParticle p) {
		if (octree.addPoint(new Vec3D(p))) {
			numParticles++;
			minBounds.minSelf(p);
			maxBounds.maxSelf(p);
			if (listeners != null) {
				for (DLAEventListener l : listeners) {
					l.dlaNewParticleAdded(this, p);
				}
			}
		}
	}

	/**
	 * Checks if the given particle is close to an existing one or a curve. If
	 * so, the particle is attached based on the current DLA parameters and the
	 * method return true.
	 * 
	 * @param p
	 * @return true, if particle attached.
	 */
	protected boolean checkParticle(DLAParticle p) {
		ArrayList<Vec3D> parts = octree.getPointsWithinSphere(p, snapDistance);
		if (parts != null) {
			// first check collision with existing particles
			float minDist = Integer.MAX_VALUE;
			int idx = -1;
			for (int i = parts.size() - 1; i > 0; i--) {
				float d = (parts.get(i)).distanceToSquared(p);
				if (d < minDist) {
					minDist = d;
					idx = i;
				}
			}
			if (minDist < snapDistanceSquared
					&& MathUtils.random(1f) < stickiness) {
				Vec3D d = p.sub((parts.get(idx))).normalize();
				d.interpolateToSelf(dirCurvePoint, curveAlign);
				p.set((parts.get(idx)).add(d.scale(particleRadius)));
				addParticle(p);
				return true;
			}
		}
		if (p.sub(currCurvePoint).magSquared() < curveAttachDistanceSquared) {
			parts = octreeCurve.getPointsWithinSphere(p, 2 * 2);
			if (parts != null) {
				float snap = 0.1f * 0.1f;
				for (Vec3D q : parts) {
					float d = p.distanceToSquared(q);
					if (d < snapDistanceSquared
							|| (d < curveSnapDistanceSquared && p.dir
									.magSquared() < snap)) {
						if (MathUtils.random(1f) < stickiness) {
							addParticle(p);
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	protected PointOctree createOctree(Vec3D origin, float size) {
		return new PointOctree(origin, size);
	}

	public DLA removeListener(DLAEventListener l) {
		listeners.remove(l);
		logger.info("removing listener: " + l);
		return this;
	}

	public void save(String fname, boolean isCentered) {
		ArrayList<Vec3D> parts = octree.getPointsWithinSphere(new Vec3D(),
				200 * 200);
		if (parts != null) {
			Vec3D origin = minBounds.add(maxBounds).scaleSelf(0.5f);
			logger.info("bounds: " + minBounds + " -> " + maxBounds
					+ " offset origin: " + origin);
			try {
				DataOutputStream ds = new DataOutputStream(
						new FileOutputStream(fname));
				int numP = parts.size();
				for (int i = 0; i < numP; i++) {
					Vec3D p = (parts.get(i));
					if (isCentered) {
						p = p.sub(origin);
					}
					ds.writeFloat(p.x);
					ds.writeFloat(p.y);
					ds.writeFloat(p.z);
				}
				ds.flush();
				ds.close();
				logger.info("written " + numP + " to " + fname);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public boolean update() {
		if (currParticle == null) {
			Vec3D spawnPos = Vec3D.randomVector();
			spawnPos = currCurvePoint.add(spawnPos.scale(MathUtils
					.random(spawnRadius)));
			currParticle = new DLAParticle(spawnPos, escapeRadius,
					particleSpeed, searchSpeed);
		}
		currParticle.update(currCurvePoint);
		if (checkParticle(currParticle)) {
			currParticle = null;
			// TODO nextParticle();
			updateCurvePoint();
		}
		return currParticle == null;
	}

	protected void updateCurvePoint() {
		if (!isComplete) {
			// FIXME this logic should be separated out
			// FIXME need to provide hook for user logic here (listeners or
			// FIXME abstract class w/ extension points)
			if (MathUtils.random(1f) < continousGrowthRatio
					&& numActiveSegments > 0) {
				DLASegment segment = activeSegments.get(MathUtils
						.random(numActiveSegments));
				float currT = MathUtils.random(1f);
				dirCurvePoint = segment.getDirection().interpolateToSelf(
						segment.getNextDirection(), currT);
				dirCurvePoint.normalize();
				currCurvePoint = segment.a.add(dirCurvePoint.scale(segment
						.getLength()
						* currT));
			} else {
				DLASegment s = outline.updatePoint(curveSpeed);
				if (!activeSegments.contains(s)) {
					activeSegments.add(s);
					numActiveSegments++;
					if (listeners != null) {
						for (DLAEventListener l : listeners) {
							l.dlaSegmentSwitched(this, s);
						}
					}
				}
				currCurvePoint = outline.getPoint();
				dirCurvePoint = outline.getDirection();
			}
			if (outline.isComplete()) {
				if (listeners != null) {
					for (DLAEventListener l : listeners) {
						l.dlaAllSegmentsProcessed(this);
					}
				}
			}
		}
	}
}