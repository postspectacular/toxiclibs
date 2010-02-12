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
    protected PointOctree octree, octreeGuides;

    protected Vec3D currCurvePoint;
    protected Vec3D dirCurvePoint;
    protected Vec3D minBounds, maxBounds;

    protected DLAGuideLines guidelines;
    protected ArrayList<DLASegment> activeSegments =
            new ArrayList<DLASegment>();

    protected List<DLAEventListener> listeners =
            new ArrayList<DLAEventListener>();

    protected int numActiveSegments = 0;

    protected float snapDistance = 1.8f;
    protected float snapDistanceSquared = snapDistance * snapDistance;
    protected float curveAttachDistance = 2;
    protected float curveAttachDistanceSquared =
            curveAttachDistance * curveAttachDistance;

    protected float spawnRadius = 12;
    protected float escapeRadius = 36;
    protected float particleRadius = 0.25f;

    protected float stickiness = 0.1f;
    protected float curveAlign = 0.74f;

    protected float curveSpeed = 0.00045f;
    protected float searchSpeed = snapDistance * 0.66f;
    protected float particleSpeed = 0.001f;

    protected double guideLineDensity = 0.01;
    protected float continousGrowthRatio = 0.1f;

    protected boolean isComplete;

    public DLA(float size, DLAGuideLines guides) {
        octree = createOctree(new Vec3D(-0.5f, -0.5f, -0.5f).scale(size), size);
        octreeGuides =
                createOctree(new Vec3D(-0.5f, -0.5f, -0.5f).scale(size), size);
        minBounds = Vec3D.MAX_VALUE.copy();
        maxBounds = Vec3D.MIN_VALUE.copy();
        guidelines = guides;
        parseGuidelines();
        updateCurvePoint();
    }

    public DLA addListener(DLAEventListener l) {
        listeners.add(l);
        logger.info("adding listener: " + l);
        return this;
    }

    public void addParticle(Vec3D p) {
        if (octree.addPoint(p.copy())) {
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
            float minDist = Integer.MAX_VALUE;
            Vec3D found = null;
            for (Vec3D pp : parts) {
                float d = pp.distanceToSquared(p);
                if (d < minDist) {
                    minDist = d;
                    found = pp;
                }
            }
            if (minDist < snapDistanceSquared
                    && MathUtils.random(1f) < stickiness) {
                Vec3D d = p.sub(found).normalize();
                d.interpolateToSelf(dirCurvePoint, curveAlign);
                d.normalizeTo(particleRadius);
                p.set(found).addSelf(d);
                addParticle(p);
                return true;
            }
        }
        if (p.sub(currCurvePoint).magSquared() < curveAttachDistanceSquared) {
            parts = octreeGuides.getPointsWithinSphere(p, curveAttachDistance);
            if (parts != null) {
                for (int i = parts.size(); i > 0; i--) {
                    if (MathUtils.random(1f) < stickiness) {
                        addParticle(p);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void clear() {
        octree.empty();
        octreeGuides.empty();
    }

    protected PointOctree createOctree(Vec3D origin, float size) {
        return new PointOctree(origin, size);
    }

    /**
     * @return the continousGrowthRatio
     */
    public float getContinousGrowthRatio() {
        return continousGrowthRatio;
    }

    /**
     * @return the currCurvePoint
     */
    public Vec3D getCurrCurvePoint() {
        return currCurvePoint;
    }

    /**
     * @return the currParticle
     */
    public DLAParticle getCurrParticle() {
        return currParticle;
    }

    /**
     * @return the curveAlign
     */
    public float getCurveAlign() {
        return curveAlign;
    }

    /**
     * @return the curveAttachDistance
     */
    public float getCurveAttachDistance() {
        return curveAttachDistance;
    }

    /**
     * @return the curveSpeed
     */
    public float getCurveSpeed() {
        return curveSpeed;
    }

    /**
     * @return the escapeRadius
     */
    public float getEscapeRadius() {
        return escapeRadius;
    }

    /**
     * @return the guideLineDensity
     */
    public double getGuideLineDensity() {
        return guideLineDensity;
    }

    /**
     * @return the guidelines
     */
    public DLAGuideLines getGuidelines() {
        return guidelines;
    }

    /**
     * @return the octreeGuides
     */
    public PointOctree getGuideOctree() {
        return octreeGuides;
    }

    /**
     * @return the numActiveSegments
     */
    public int getNumActiveSegments() {
        return numActiveSegments;
    }

    /**
     * @return the numParticles
     */
    public int getNumParticles() {
        return numParticles;
    }

    /**
     * @return the octree
     */
    public PointOctree getOctree() {
        return octree;
    }

    /**
     * @return the particleRadius
     */
    public float getParticleRadius() {
        return particleRadius;
    }

    /**
     * @return the particleSpeed
     */
    public float getParticleSpeed() {
        return particleSpeed;
    }

    /**
     * @return the searchSpeed
     */
    public float getSearchSpeed() {
        return searchSpeed;
    }

    /**
     * @return the snapDistance
     */
    public float getSnapDistance() {
        return snapDistance;
    }

    /**
     * @return the spawnRadius
     */
    public float getSpawnRadius() {
        return spawnRadius;
    }

    /**
     * @return the stickiness
     */
    public float getStickiness() {
        return stickiness;
    }

    /**
     * @return the isComplete
     */
    public boolean isComplete() {
        return isComplete;
    }

    public void parseGuidelines() {
        guidelines.reset();
        octreeGuides.empty();
        while (!guidelines.isComplete()) {
            guidelines.updatePoint(guideLineDensity);
            Vec3D p = guidelines.getPoint();
            octreeGuides.addPoint(p);
            System.out.println("added to octree: " + p);
        }
        guidelines.reset();
    }

    public DLA removeListener(DLAEventListener l) {
        listeners.remove(l);
        logger.info("removing listener: " + l);
        return this;
    }

    public void save(String fname, boolean isCentered) {
        List<Vec3D> parts = octree.getPoints();
        if (parts != null) {
            Vec3D origin = minBounds.add(maxBounds).scaleSelf(0.5f);
            logger.info("bounds: " + minBounds + " -> " + maxBounds
                    + " offset origin: " + origin);
            try {
                DataOutputStream ds =
                        new DataOutputStream(new FileOutputStream(fname));
                if (isCentered) {
                    for (Vec3D p : parts) {
                        p = p.sub(origin);
                        ds.writeFloat(p.x);
                        ds.writeFloat(p.y);
                        ds.writeFloat(p.z);
                    }
                } else {
                    for (Vec3D p : parts) {
                        ds.writeFloat(p.x);
                        ds.writeFloat(p.y);
                        ds.writeFloat(p.z);
                    }
                }
                ds.flush();
                ds.close();
                logger.info("written " + parts.size() + " to " + fname);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param continousGrowthRatio
     *            the continousGrowthRatio to set
     */
    public void setContinousGrowthRatio(float continousGrowthRatio) {
        this.continousGrowthRatio = continousGrowthRatio;
    }

    /**
     * @param curveAlign
     *            the curveAlign to set
     */
    public void setCurveAlign(float curveAlign) {
        this.curveAlign = curveAlign;
    }

    /**
     * @param curveAttachDistance
     *            the curveAttachDistance to set
     */
    public void setCurveAttachDistance(float curveAttachDistance) {
        this.curveAttachDistance = curveAttachDistance;
        this.curveAttachDistanceSquared =
                curveAttachDistance * curveAttachDistance;
    }

    /**
     * @param curveSpeed
     *            the curveSpeed to set
     */
    public void setCurveSpeed(float curveSpeed) {
        this.curveSpeed = curveSpeed;
    }

    /**
     * @param escapeRadius
     *            the escapeRadius to set
     */
    public void setEscapeRadius(float escapeRadius) {
        this.escapeRadius = escapeRadius;
    }

    /**
     * @param guideLineDensity
     *            the guideLineDensity to set
     */
    public void setGuideLineDensity(double guideLineDensity) {
        this.guideLineDensity = guideLineDensity;
        parseGuidelines();
    }

    /**
     * @param guidelines
     *            the guidelines to set
     */
    public void setGuidelines(DLAGuideLines guidelines) {
        this.guidelines = guidelines;
        parseGuidelines();
    }

    /**
     * @param particleRadius
     *            the particleRadius to set
     */
    public void setParticleRadius(float particleRadius) {
        this.particleRadius = particleRadius;
    }

    /**
     * @param particleSpeed
     *            the particleSpeed to set
     */
    public void setParticleSpeed(float particleSpeed) {
        this.particleSpeed = particleSpeed;
    }

    /**
     * @param searchSpeed
     *            the searchSpeed to set
     */
    public void setSearchSpeed(float searchSpeed) {
        this.searchSpeed = searchSpeed;
    }

    /**
     * @param snapDistance
     *            the snapDistance to set
     */
    public void setSnapDistance(float snapDistance) {
        this.snapDistance = snapDistance;
        this.snapDistanceSquared = snapDistance * snapDistance;
    }

    /**
     * @param spawnRadius
     *            the spawnRadius to set
     */
    public void setSpawnRadius(float spawnRadius) {
        this.spawnRadius = spawnRadius;
    }

    /**
     * @param stickiness
     *            the stickiness to set
     */
    public void setStickiness(float stickiness) {
        this.stickiness = stickiness;
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
                DLASegment s = guidelines.updatePoint(curveSpeed);
                if (!activeSegments.contains(s)) {
                    activeSegments.add(s);
                    numActiveSegments++;
                    if (listeners != null) {
                        for (DLAEventListener l : listeners) {
                            l.dlaSegmentSwitched(this, s);
                        }
                    }
                }
                currCurvePoint = guidelines.getPoint();
                dirCurvePoint = guidelines.getDirection();
            }
            if (guidelines.isComplete()) {
                guidelines.reset();
                if (listeners != null) {
                    for (DLAEventListener l : listeners) {
                        l.dlaAllSegmentsProcessed(this);
                    }
                }
            }
        }
    }
}