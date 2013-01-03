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

package toxi.physics2d;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import toxi.geom.ReadonlyVec2D;
import toxi.geom.Rect;
import toxi.geom.Vec2D;
import toxi.physics2d.behaviors.ParticleBehavior2D;
import toxi.physics2d.constraints.ParticleConstraint2D;

/**
 * An individual 3D particle for use by the VerletPhysics and VerletSpring
 * classes. A particle has weight, can be locked in space and its position
 * constrained inside an (optional) axis-aligned bounding box.
 */
public class VerletParticle2D extends Vec2D {

    protected Vec2D prev, temp;
    protected boolean isLocked;

    /**
     * Bounding box, by default set to null to disable
     */
    public Rect bounds;

    /**
     * An optional particle constraints, called immediately after a particle is
     * updated (and only used if particle is unlocked (default)
     */
    public List<ParticleConstraint2D> constraints;

    public List<ParticleBehavior2D> behaviors;
    /**
     * Particle weight, default = 1
     */
    protected float weight, invWeight;

    protected Vec2D force = new Vec2D();

    /**
     * Creates particle at position xyz
     * 
     * @param x
     * @param y
     */
    public VerletParticle2D(float x, float y) {
        this(x, y, 1);
    }

    /**
     * Creates particle at position xyz with weight w
     * 
     * @param x
     * @param y
     * @param w
     */
    public VerletParticle2D(float x, float y, float w) {
        super(x, y);
        prev = new Vec2D(this);
        temp = new Vec2D();
        setWeight(w);
    }

    /**
     * Creates particle at the position of the passed in vector
     * 
     * @param v
     *            position
     */
    public VerletParticle2D(ReadonlyVec2D v) {
        this(v.x(), v.y(), 1);
    }

    /**
     * Creates particle with weight w at the position of the passed in vector
     * 
     * @param v
     *            position
     * @param w
     *            weight
     */
    public VerletParticle2D(ReadonlyVec2D v, float w) {
        this(v.x(), v.y(), w);
    }

    /**
     * Creates a copy of the passed in particle
     * 
     * @param p
     */
    public VerletParticle2D(VerletParticle2D p) {
        this(p.x, p.y, p.weight);
        isLocked = p.isLocked;
    }

    public VerletParticle2D addBehavior(ParticleBehavior2D behavior) {
        return addBehavior(behavior, 1);
    }

    public VerletParticle2D addBehavior(ParticleBehavior2D behavior,
            float timeStep) {
        if (behaviors == null) {
            behaviors = new ArrayList<ParticleBehavior2D>(1);
        }
        behavior.configure(timeStep);
        behaviors.add(behavior);
        return this;
    }

    public VerletParticle2D addBehaviors(
            Collection<ParticleBehavior2D> behaviors) {
        return addBehaviors(behaviors, 1);
    }

    public VerletParticle2D addBehaviors(
            Collection<ParticleBehavior2D> behaviors, float timeStemp) {
        for (ParticleBehavior2D b : behaviors) {
            addBehavior(b, timeStemp);
        }
        return this;
    }

    /**
     * Adds the given constraint implementation to the list of constraints
     * applied to this particle at each time step.
     * 
     * @param c
     *            constraint instance
     * @return itself
     */
    public VerletParticle2D addConstraint(ParticleConstraint2D c) {
        if (constraints == null) {
            constraints = new ArrayList<ParticleConstraint2D>(1);
        }
        constraints.add(c);
        return this;
    }

    public VerletParticle2D addConstraints(
            Collection<ParticleConstraint2D> constraints) {
        for (ParticleConstraint2D c : constraints) {
            addConstraint(c);
        }
        return this;
    }

    public VerletParticle2D addForce(Vec2D f) {
        force.addSelf(f);
        return this;
    }

    public VerletParticle2D addVelocity(Vec2D v) {
        prev.subSelf(v);
        return this;
    }

    public void applyBehaviors() {
        if (behaviors != null) {
            for (ParticleBehavior2D b : behaviors) {
                b.apply(this);
            }
        }
    }

    public void applyConstraints() {
        if (constraints != null) {
            for (ParticleConstraint2D pc : constraints) {
                pc.apply(this);
            }
        }
    }

    protected void applyForce() {
        temp.set(this);
        addSelf(sub(prev).addSelf(force.scale(weight)));
        prev.set(temp);
        force.clear();
    }

    public VerletParticle2D clearForce() {
        force.clear();
        return this;
    }

    public VerletParticle2D clearVelocity() {
        prev.set(this);
        return this;
    }

    public Vec2D getForce() {
        return force;
    }

    /**
     * @return the inverse weight (1/weight)
     */
    public final float getInvWeight() {
        return invWeight;
    }

    /**
     * Returns the particle's position at the most recent time step.
     * 
     * @return previous position
     */
    public Vec2D getPreviousPosition() {
        return prev;
    }

    public Vec2D getVelocity() {
        return sub(prev);
    }

    /**
     * @return the weight
     */
    public final float getWeight() {
        return weight;
    }

    /**
     * @return true, if particle is locked
     */
    public final boolean isLocked() {
        return isLocked;
    }

    /**
     * Locks/immobilizes particle in space
     * 
     * @return itself
     */
    public VerletParticle2D lock() {
        isLocked = true;
        return this;
    }

    public VerletParticle2D removeAllBehaviors() {
        behaviors.clear();
        return this;
    }

    /**
     * Removes any currently applied constraints from this particle.
     * 
     * @return itself
     */
    public VerletParticle2D removeAllConstraints() {
        constraints.clear();
        return this;
    }

    public boolean removeBehavior(ParticleBehavior2D b) {
        return behaviors.remove(b);
    }

    public boolean removeBehaviors(Collection<ParticleBehavior2D> behaviors) {
        return this.behaviors.removeAll(behaviors);
    }

    /**
     * Attempts to remove the given constraint instance from the list of active
     * constraints.
     * 
     * @param c
     *            constraint to remove
     * @return true, if successfully removed
     */
    public boolean removeConstraint(ParticleConstraint2D c) {
        return constraints.remove(c);
    }

    public boolean removeConstraints(
            Collection<ParticleConstraint2D> constraints) {
        return this.constraints.removeAll(constraints);
    }

    public VerletParticle2D scaleVelocity(float scl) {
        prev.interpolateToSelf(this, 1f - scl);
        return this;
    }

    public VerletParticle2D setPreviousPosition(Vec2D p) {
        prev.set(p);
        return this;
    }

    public void setWeight(float w) {
        weight = w;
        invWeight = 1f / w;
    }

    /**
     * Unlocks particle again
     * 
     * @return itself
     */
    public VerletParticle2D unlock() {
        clearVelocity();
        isLocked = false;
        return this;
    }

    public void update() {
        if (!isLocked) {
            applyBehaviors();
            applyForce();
            applyConstraints();
        }
    }
}
