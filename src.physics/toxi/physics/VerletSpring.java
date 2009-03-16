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

package toxi.physics;

import toxi.geom.Vec3D;

/**
 * <p>
 * A spring class connecting two VerletParticles in space. Based on the
 * configuration of the spring instance and that of the physics engine, the
 * behaviour of the spring can vary between springy and stiff/stick like.
 * </p>
 * 
 * <p>
 * The simulation takes particle weights into account and can be configured to
 * lock either particle in space in order to force the other one to move. This
 * is sometimes handy for resolving collisions (currently outside the scope of
 * this library).
 * </p>
 * 
 * @see toxi.physics.VerletPhysics
 * @author toxi
 * 
 */
public class VerletSpring {

	protected static final float EPS = 1e-6f;

	/**
	 * Spring end points / particles
	 */
	public VerletParticle a, b;

	/**
	 * Spring rest length to which it always wants to return too
	 */
	public float restLength;

	/**
	 * Spring strength, possible value range depends on engine configuration
	 * (time step, friction)
	 */
	public float strength;

	/**
	 * Flag, if either particle is locked in space (only within the scope of
	 * this spring)
	 */
	protected boolean isALocked, isBLocked;

	/**
	 * @param a
	 *            1st particle
	 * @param b
	 *            2nd particle
	 * @param len
	 *            desired rest length
	 * @param str
	 *            spring strength
	 */
	public VerletSpring(VerletParticle a, VerletParticle b, float len, float str) {
		this.a = a;
		this.b = b;
		restLength = len;
		strength = str;
	}

	/**
	 * Updates both particle positions (if not locked) based on their current
	 * distance, weight and spring configuration *
	 */
	protected void update(boolean applyConstraints) {
		Vec3D delta = b.sub(a);
		// add minute offset to avoid div-by-zero errors
		float dist = delta.magnitude() + EPS;
		float invWeightA = 1f / a.weight;
		float invWeightB = 1f / b.weight;
		float normDistStrength = (dist - restLength)
				/ (dist * (invWeightA + invWeightB)) * strength;
		if (!a.isLocked && !isALocked) {
			a.addSelf(delta.scale(normDistStrength * invWeightA));
			if (applyConstraints) {
				a.applyConstraints();
			}
		}
		if (!b.isLocked && !isBLocked) {
			b.addSelf(delta.scale(-normDistStrength * invWeightB));
			if (applyConstraints) {
				b.applyConstraints();
			}
		}
	}

	/**
	 * (Un)Locks the 1st end point of the spring. <b>NOTE: this acts purely
	 * within the scope of this spring instance and does NOT call
	 * {@link VerletParticle#lock()}</b>
	 * 
	 * @param s
	 * @return itself
	 */
	public VerletSpring lockA(boolean s) {
		isALocked = s;
		return this;
	}

	/**
	 * (Un)Locks the 2nd end point of the spring
	 * 
	 * @param s
	 * @return itself
	 */

	public VerletSpring lockB(boolean s) {
		isBLocked = s;
		return this;
	}
}
