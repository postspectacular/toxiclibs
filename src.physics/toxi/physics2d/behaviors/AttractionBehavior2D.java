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

package toxi.physics2d.behaviors;

import java.util.List;

import toxi.geom.SpatialIndex;
import toxi.geom.Vec2D;
import toxi.physics2d.VerletParticle2D;

public class AttractionBehavior2D implements ParticleBehavior2D {

    protected Vec2D attractor;
    protected float attrStrength;

    protected float radius, radiusSquared;
    protected float strength;
    protected float jitter;
    protected float timeStep;

    public AttractionBehavior2D(Vec2D attractor, float radius, float strength) {
        this(attractor, radius, strength, 0);
    }

    public AttractionBehavior2D(Vec2D attractor, float radius, float strength,
            float jitter) {
        this.attractor = attractor;
        this.strength = strength;
        this.jitter = jitter;
        setRadius(radius);
    }

    public void apply(VerletParticle2D p) {
        Vec2D delta = attractor.sub(p);
        float dist = delta.magSquared();
        if (dist < radiusSquared) {
            Vec2D f = delta.normalizeTo((1.0f - dist / radiusSquared))
                    .jitter(jitter).scaleSelf(attrStrength);
            p.addForce(f);
        }
    }

    public void applyWithIndex(SpatialIndex<Vec2D> spaceHash) {
        List<Vec2D> selection = spaceHash.itemsWithinRadius(attractor, radius,
                null);
        final Vec2D temp = new Vec2D();
        if (selection != null) {
            for (Vec2D p : selection) {
                temp.set(p);
                apply((VerletParticle2D) p);
                spaceHash.reindex(temp, p);
            }
        }
    }

    public void configure(float timeStep) {
        this.timeStep = timeStep;
        setStrength(strength);
    }

    /**
     * @return the attractor
     */
    public Vec2D getAttractor() {
        return attractor;
    }

    /**
     * @return the jitter
     */
    public float getJitter() {
        return jitter;
    }

    /**
     * @return the radius
     */
    public float getRadius() {
        return radius;
    }

    /**
     * @return the strength
     */
    public float getStrength() {
        return strength;
    }

    /**
     * @param attractor
     *            the attractor to set
     */
    public void setAttractor(Vec2D attractor) {
        this.attractor = attractor;
    }

    /**
     * @param jitter
     *            the jitter to set
     */
    public void setJitter(float jitter) {
        this.jitter = jitter;
    }

    public void setRadius(float r) {
        this.radius = r;
        this.radiusSquared = r * r;
    }

    /**
     * @param strength
     *            the strength to set
     */
    public void setStrength(float strength) {
        this.strength = strength;
        this.attrStrength = strength * timeStep;
    }

    public boolean supportsSpatialIndex() {
        return true;
    }

}
