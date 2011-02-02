/*
 *   __               .__       .__  ._____.           
 * _/  |_  _______  __|__| ____ |  | |__\_ |__   ______
 * \   __\/  _ \  \/  /  |/ ___\|  | |  || __ \ /  ___/
 *  |  | (  <_> >    <|  \  \___|  |_|  || \_\ \\___ \ 
 *  |__|  \____/__/\_ \__|\___  >____/__||___  /____  >
 *                   \/       \/             \/     \/ 
 *
 * Copyright (c) 2006-2011 Karsten Schmidt
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * http://creativecommons.org/licenses/LGPL/2.1/
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301, USA
 */

package toxi.sim.dla;

import toxi.geom.Vec3D;
import toxi.util.datatypes.BiasedFloatRange;

public class DLAConfiguration {

    protected float snapDistance = 1.8f;
    protected float snapDistanceSquared = snapDistance * snapDistance;
    protected float curveAttachDistance = 2;
    protected float curveAttachDistanceSquared = curveAttachDistance
            * curveAttachDistance;

    protected float spawnRadius = 12;
    protected float escapeRadius = 36;
    protected float particleRadius = 0.25f;

    protected float stickiness = 0.1f;
    protected float curveAlign = 0.74f;

    protected float curveSpeed = 0.00045f;
    protected float searchSpeed = snapDistance * 0.66f;
    protected float particleSpeed = 0.001f;

    protected double guideLineDensity = 0.1;
    protected float continuousGrowthRatio = 0.1f;
    protected Vec3D growthScale = new Vec3D(1, 1, 1);

    protected BiasedFloatRange growthBiasRange = new BiasedFloatRange(0, 1, 0,
            1);

    public DLAConfiguration() {

    }

    public float getContinuousGrowthBias() {
        return growthBiasRange.getBias();
    }

    public float getContinuousGrowthCoeff() {
        return growthBiasRange.pickRandom();
    }

    /**
     * @return the continuousGrowthRatio
     */
    public float getContinuousGrowthRatio() {
        return continuousGrowthRatio;
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

    public Vec3D getGrowthScale() {
        return growthScale;
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
     * @param bias
     * @param sd
     */
    public void setContinuousGrowthBias(float bias, float sd) {
        growthBiasRange.setBias(bias);
        growthBiasRange.setStandardDeviation(sd);
    }

    /**
     * @param continousGrowthRatio
     *            the continuousGrowthRatio to set
     */
    public void setContinuousGrowthRatio(float continousGrowthRatio) {
        this.continuousGrowthRatio = continousGrowthRatio;
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
     * @param growthScale
     *            the growthScale to set
     */
    public void setGrowthScale(Vec3D growthScale) {
        this.growthScale = growthScale;
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
