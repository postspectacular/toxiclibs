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

package toxi.physics;

import java.util.ArrayList;
import java.util.List;

import toxi.geom.AABB;
import toxi.geom.ReadonlyVec3D;
import toxi.geom.Vec3D;
import toxi.physics.behaviors.ParticleBehavior;
import toxi.physics.constraints.ParticleConstraint;

/**
 * An individual 3D particle for use by the VerletPhysics and VerletSpring
 * classes. A particle has weight, can be locked in space and its position
 * constrained inside an (optional) axis-aligned bounding box.
 */
public class VerletParticle extends Vec3D {

    protected Vec3D prev, temp;
    protected boolean isLocked;

    /**
     * Bounding box, by default set to null to disable
     */
    public AABB bounds;

    /**
     * An optional particle constraints, called immediately after a particle is
     * updated (and only used if particle is unlocked (default)
     */
    public List<ParticleConstraint> constraints;

    public List<ParticleBehavior> behaviors;

    /**
     * Particle weight, default = 1
     */
    protected float weight, invWeight;

    protected Vec3D force = new Vec3D();

    /**
     * Creates particle at position xyz
     * 
     * @param x
     * @param y
     * @param z
     */
    public VerletParticle(float x, float y, float z) {
        this(x, y, z, 1);
    }

    /**
     * Creates particle at position xyz with weight w
     * 
     * @param x
     * @param y
     * @param z
     * @param w
     */
    public VerletParticle(float x, float y, float z, float w) {
        super(x, y, z);
        prev = new Vec3D(this);
        temp = new Vec3D();
        setWeight(w);
    }

    /**
     * Creates particle at the position of the passed in vector
     * 
     * @param v
     *            position
     */
    public VerletParticle(ReadonlyVec3D v) {
        this(v.x(), v.y(), v.z(), 1);
    }

    /**
     * Creates particle with weight w at the position of the passed in vector
     * 
     * @param v
     *            position
     * @param w
     *            weight
     */
    public VerletParticle(ReadonlyVec3D v, float w) {
        this(v.x(), v.y(), v.z(), w);
    }

    /**
     * Creates a copy of the passed in particle
     * 
     * @param p
     */
    public VerletParticle(VerletParticle p) {
        this(p.x, p.y, p.z, p.weight);
        isLocked = p.isLocked;
    }

    public VerletParticle addBehavior(ParticleBehavior behavior) {
        return addBehavior(behavior, 1);
    }

    public VerletParticle addBehavior(ParticleBehavior behavior, float timeStep) {
        if (behaviors == null) {
            behaviors = new ArrayList<ParticleBehavior>(1);
        }
        behavior.configure(timeStep);
        behaviors.add(behavior);
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
    public VerletParticle addConstraint(ParticleConstraint c) {
        if (constraints == null) {
            constraints = new ArrayList<ParticleConstraint>(1);
        }
        constraints.add(c);
        return this;
    }

    public VerletParticle addForce(Vec3D f) {
        force.addSelf(f);
        return this;
    }

    public VerletParticle addVelocity(Vec3D v) {
        prev.subSelf(v);
        return this;
    }

    public void applyBehaviors() {
        if (behaviors != null) {
            for (ParticleBehavior b : behaviors) {
                b.apply(this);
            }
        }
    }

    public void applyConstraints() {
        if (constraints != null) {
            for (ParticleConstraint pc : constraints) {
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

    /**
     * Removes any currently applied constraints from this particle.
     * 
     * @return itself
     */
    public VerletParticle clearConstraints() {
        constraints.clear();
        return this;
    }

    public VerletParticle clearForce() {
        force.clear();
        return this;
    }

    public VerletParticle clearVelocity() {
        prev.set(this);
        return this;
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
    public Vec3D getPreviousPosition() {
        return prev;
    }

    public Vec3D getVelocity() {
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
    public VerletParticle lock() {
        isLocked = true;
        return this;
    }

    public boolean removeBehavior(ParticleBehavior b) {
        return behaviors.remove(b);
    }

    /**
     * Attempts to remove the given constraint instance from the list of active
     * constraints.
     * 
     * @param c
     *            constraint to remove
     * @return true, if successfully removed
     */
    public boolean removeConstraint(ParticleConstraint c) {
        return constraints.remove(c);
    }

    public VerletParticle scaleVelocity(float scl) {
        prev.interpolateToSelf(this, 1f - scl);
        return this;
    }

    public VerletParticle setPreviousPosition(Vec3D p) {
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
    public VerletParticle unlock() {
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
