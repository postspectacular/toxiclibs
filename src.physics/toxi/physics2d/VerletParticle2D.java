package toxi.physics2d;

/*
 * Copyright (c) 2008 Karsten Schmidt
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * http://creativecommons.org/licenses/LGPL/2.1/
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA
 */

import java.util.ArrayList;

import toxi.geom.Rect;
import toxi.geom.Vec2D;
import toxi.physics.constraints.ParticleConstraint;
import toxi.physics2d.constraints.Particle2DConstraint;

/**
 * An individual 3D particle for use by the VerletPhysics and VerletSpring
 * classes. A particle has weight, can be locked in space and its position
 * constrained inside an (optional) axis-aligned bounding box.
 * 
 * @author toxi
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
	public ArrayList<Particle2DConstraint> constraints;

	/**
	 * Particle weight, default = 1
	 */
	public float weight = 1.0f;

	/**
	 * Creates particle at position xyz
	 * 
	 * @param x
	 * @param y
	 */
	public VerletParticle2D(float x, float y) {
		super(x, y);
		prev = new Vec2D(this);
		temp = new Vec2D();
	}

	/**
	 * Creates particle at position xyz with weight w
	 * 
	 * @param x
	 * @param y
	 * @param w
	 */
	public VerletParticle2D(float x, float y, float w) {
		this(x, y);
		weight = w;
	}

	/**
	 * Creates particle at the position of the passed in vector
	 * 
	 * @param v
	 *            position
	 */
	public VerletParticle2D(Vec2D v) {
		this(v.x, v.y);
	}

	/**
	 * Creates particle with weight w at the position of the passed in vector
	 * 
	 * @param v
	 *            position
	 * @param w
	 *            weight
	 */
	public VerletParticle2D(Vec2D v, float w) {
		this(v.x, v.y);
		weight = w;
	}

	/**
	 * Creates a copy of the passed in particle
	 * 
	 * @param p
	 */
	public VerletParticle2D(VerletParticle2D p) {
		this(p.x, p.y);
		weight = p.weight;
		isLocked = p.isLocked;
	}

	/**
	 * Adds the given constraint implementation to the list of constraints
	 * applied to this particle at each time step.
	 * 
	 * @param c
	 *            constraint instance
	 * @return itself
	 */
	public VerletParticle2D addConstraint(Particle2DConstraint c) {
		if (constraints == null) {
			constraints = new ArrayList<Particle2DConstraint>(2);
		}
		constraints.add(c);
		return this;
	}

	public void applyConstraints() {
		if (constraints != null) {
			for (Particle2DConstraint pc : constraints) {
				pc.apply(this);
			}
		}
	}

	/**
	 * Returns the particle's position at the most recent time step.
	 * 
	 * @return previous position
	 */
	public Vec2D getPreviousPosition() {
		return prev;
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

	/**
	 * Removes any currently applied constraints from this particle.
	 * 
	 * @return itself
	 */
	public VerletParticle2D removeAllConstraints() {
		constraints.clear();
		return this;
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

	/**
	 * Unlocks particle again
	 * 
	 * @return itself
	 */
	public VerletParticle2D unlock() {
		prev.set(this);
		isLocked = false;
		return this;
	}

	protected void update(float force) {
		if (!isLocked) {
			temp.set(this);
			addSelf(sub(prev).scaleSelf(force));
			prev.set(temp);
			if (constraints != null) {
				applyConstraints();
			}
		}
	}
}
