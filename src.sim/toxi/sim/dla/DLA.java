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

    protected static final Logger logger =
            Logger.getLogger(DLA.class.getName());

    protected int numParticles;
    protected DLAParticle currParticle;
    protected PointOctree octree, octreeCurve;

    protected Vec3D currCurvePoint;
    protected Vec3D dirCurvePoint;
    protected Vec3D minBounds, maxBounds;

    protected DLAGuideLines guidlines;
    protected ArrayList<DLASegment> activeSegments =
            new ArrayList<DLASegment>();

    protected List<DLAEventListener> listeners =
            new ArrayList<DLAEventListener>();

    protected int numActiveSegments = 0;

    protected float snapDistance = 1.8f;
    protected float snapDistanceSquared = snapDistance * snapDistance;
    protected float curveAttachDistanceSquared = 4;
    protected float curveSnapDistanceSquared = 3.6f * 3.6f;

    protected float spawnRadius = 12;
    protected float escapeRadius = 36;
    protected float particleRadius = 0.25f;

    protected float stickiness = 0.2f;
    protected float curveAlign = 0.85f;

    protected float curveSpeed = 0.00045f;
    protected float searchSpeed = snapDistance * 0.9f;
    protected float particleSpeed = 0.01f;

    protected float continousGrowthRatio = 0.1f;

    protected boolean isComplete;

    public DLA(float size, DLAGuideLines guides) {
        octree = createOctree(new Vec3D(-0.5f, -0.5f, -0.5f).scale(size), size);
        octreeCurve =
                createOctree(new Vec3D(-0.5f, -0.5f, -0.5f).scale(size), size);
        minBounds = Vec3D.MAX_VALUE.copy();
        maxBounds = Vec3D.MIN_VALUE.copy();
        guidlines = guides;
        parseGuidelines();
        updateCurvePoint();
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
            Vec3D found = null;
            for (int i = parts.size() - 1; i > 0; i--) {
                float d = (parts.get(i)).distanceToSquared(p);
                if (d < minDist) {
                    minDist = d;
                    found = parts.get(i);
                }
            }
            if (minDist < snapDistanceSquared
                    && MathUtils.random(1f) < stickiness) {
                Vec3D d = p.sub(found).normalize();
                d.interpolateToSelf(dirCurvePoint, curveAlign);
                p.set(found).add(d.scale(particleRadius));
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

    /**
     * @return the snapDistance
     */
    public float getSnapDistance() {
        return snapDistance;
    }

    public void parseGuidelines() {
        guidlines.reset();
        while (!guidlines.isComplete()) {
            guidlines.updatePoint(0.1);
            Vec3D p = guidlines.getPoint();
            octreeCurve.addPoint(p);
            System.out.println("added to octree: " + p);
        }
        guidlines.reset();
    }

    public DLA removeListener(DLAEventListener l) {
        listeners.remove(l);
        logger.info("removing listener: " + l);
        return this;
    }

    public void save(String fname, boolean isCentered) {
        ArrayList<Vec3D> parts =
                octree.getPointsWithinSphere(new Vec3D(), 200 * 200);
        if (parts != null) {
            Vec3D origin = minBounds.add(maxBounds).scaleSelf(0.5f);
            logger.info("bounds: " + minBounds + " -> " + maxBounds
                    + " offset origin: " + origin);
            try {
                DataOutputStream ds =
                        new DataOutputStream(new FileOutputStream(fname));
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

    /**
     * @param snapDistance
     *            the snapDistance to set
     */
    public void setSnapDistance(float snapDistance) {
        this.snapDistance = snapDistance;
        this.snapDistanceSquared = snapDistance * snapDistance;
    }

    public boolean update() {
        if (currParticle == null) {
            Vec3D spawnPos = Vec3D.randomVector();
            spawnPos =
                    currCurvePoint.add(spawnPos.scale(MathUtils
                            .random(spawnRadius)));
            currParticle =
                    new DLAParticle(spawnPos, escapeRadius, particleSpeed,
                            searchSpeed);
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
                DLASegment segment =
                        activeSegments.get(MathUtils.random(numActiveSegments));
                float currT = MathUtils.random(1f);
                dirCurvePoint =
                        segment.getDirection().interpolateToSelf(
                                segment.getNextDirection(), currT);
                dirCurvePoint.normalize();
                currCurvePoint =
                        segment.a.add(dirCurvePoint.scale(segment.getLength()
                                * currT));
            } else {
                DLASegment s = guidlines.updatePoint(curveSpeed);
                if (!activeSegments.contains(s)) {
                    activeSegments.add(s);
                    numActiveSegments++;
                    if (listeners != null) {
                        for (DLAEventListener l : listeners) {
                            l.dlaSegmentSwitched(this, s);
                        }
                    }
                }
                currCurvePoint = guidlines.getPoint();
                dirCurvePoint = guidlines.getDirection();
            }
            if (guidlines.isComplete()) {
                if (listeners != null) {
                    for (DLAEventListener l : listeners) {
                        l.dlaAllSegmentsProcessed(this);
                    }
                }
            }
        }
    }
}