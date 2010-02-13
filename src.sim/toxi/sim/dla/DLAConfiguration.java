package toxi.sim.dla;

public class DLAConfiguration {

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

    protected double guideLineDensity = 0.1;
    protected float continousGrowthRatio = 0.1f;

    public DLAConfiguration() {

    }

    /**
     * @return the continousGrowthRatio
     */
    public float getContinousGrowthRatio() {
        return continousGrowthRatio;
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

    public float getCurveAttachDistanceSquared() {
        return curveAttachDistanceSquared;
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

    public float getSnapDistanceSquared() {
        return snapDistanceSquared;
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

}
