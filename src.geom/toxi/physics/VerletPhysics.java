/* 
 * Copyright (c) 2008 Karsten Schmidt
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
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

package toxi.physics;

import java.util.ArrayList;
import java.util.Iterator;

import toxi.geom.AABB;
import toxi.geom.Vec3D;

public class VerletPhysics {
	public ArrayList particles;
	public ArrayList springs;

	/**
	 * Default friction value = 0.15
	 */
	public float friction = 0.15f;

	/**
	 * Default time step = 0.02
	 */
	public float timeStep = 0.01f;

	/**
	 * Default iterations for verlet solver = 50
	 */
	public int numIterations = 50;

	/**
	 * Gravity vector (by default inactive)
	 */
	public Vec3D gravity = new Vec3D();

	public AABB worldBox;

	/**
	 * Initializes a Verlet engine instance using the default values.
	 */
	public VerletPhysics() {
		particles = new ArrayList();
		springs = new ArrayList();
	}

	/**
	 * Initializes an Verlet engine instance with the passed in configuration.
	 * 
	 * @param gravity
	 *            3D gravity vector
	 * @param numIterations
	 *            iterations per time step for verlet solver
	 * @param friction
	 *            friction value 0...1
	 * @param timeStep
	 *            time step for calculating forces
	 */
	public VerletPhysics(Vec3D gravity, int numIterations, float friction,
			float timeStep) {
		this.gravity.set(gravity);
		this.numIterations = numIterations;
		this.friction = friction;
		this.timeStep = timeStep;
	}

	/**
	 * @param p
	 */
	public void addParticle(VerletParticle p) {
		particles.add(p);
	}

	/**
	 * @param s
	 */
	public void addSpring(VerletSpring s) {
		springs.add(s);
	}

	public void update() {
		applyGravity();
		updateParticles();
		updateSprings();
		updateCollisions();
	}

	protected void applyGravity() {
		if (!gravity.isZeroVector()) {
			Iterator i = particles.iterator();
			while (i.hasNext()) {
				VerletParticle p = (VerletParticle) i.next();
				if (!p.isLocked)
					p.addSelf(gravity.scale(p.weight));
			}
		}
	}

	protected void updateParticles() {
		float force = 1.0f - friction * timeStep * timeStep;
		// TODO use weight for friction too?
		Iterator i = particles.iterator();
		while (i.hasNext()) {
			VerletParticle p = (VerletParticle) i.next();
			p.update(force);
		}
	}

	protected void updateSprings() {
		for (int i = 0; i < numIterations; i++) {
			Iterator is = springs.iterator();
			while (is.hasNext()) {
				VerletSpring s = (VerletSpring) is.next();
				s.update();
			}
		}
	}

	protected void updateCollisions() {
		Iterator i = particles.iterator();
		while (i.hasNext()) {
			VerletParticle p = (VerletParticle) i.next();
			p.constrain(worldBox);
		}
	}

	public VerletSpring getSpring(Vec3D a, Vec3D b) {
		Iterator is = springs.iterator();
		while (is.hasNext()) {
			VerletSpring s = (VerletSpring) is.next();
			if ((s.a == a && s.b == b) || (s.a == b && s.b == a)) {
				return s;
			}
		}
		return null;
	}
}
