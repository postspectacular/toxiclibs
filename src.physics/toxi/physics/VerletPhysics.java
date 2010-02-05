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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import toxi.geom.AABB;
import toxi.geom.Vec3D;
import toxi.physics.constraints.ParticleConstraint;

/**
 * 3D particle physics engine using Verlet integration based on:
 * http://en.wikipedia.org/wiki/Verlet_integration
 * http://www.teknikus.dk/tj/gdc2001.htm
 * 
 */
public class VerletPhysics {

    public static void addConstraintToAll(ParticleConstraint c,
            List<VerletParticle> list) {
        for (VerletParticle p : list) {
            p.addConstraint(c);
        }
    }

    public static void removeConstraintFromAll(ParticleConstraint c,
            List<VerletParticle> list) {
        for (VerletParticle p : list) {
            p.removeConstraint(c);
        }
    }

    /**
     * List of particles (Vec3D subclassed)
     */
    public ArrayList<VerletParticle> particles;
    /**
     * List of spring/sticks connectors
     */
    public ArrayList<VerletSpring> springs;

    /**
     * Default drag value = 0.0
     */
    protected float drag = 0.0f;

    /**
     * Default time step = 1.0
     */
    protected float timeStep = 1.0f;

    /**
     * Default iterations for verlet solver = 50
     */
    protected int numIterations = 50;

    /**
     * Gravity vector (by default inactive)
     */
    protected Vec3D gravity = new Vec3D();

    /**
     * Optional 3D bounding box to constrain particles too
     */
    protected AABB worldBounds;

    /**
     * Initializes a Verlet engine instance using the default values.
     */
    public VerletPhysics() {
        particles = new ArrayList<VerletParticle>();
        springs = new ArrayList<VerletSpring>();
    }

    /**
     * Initializes an Verlet engine instance with the passed in configuration.
     * 
     * @param gravity
     *            3D gravity vector
     * @param numIterations
     *            iterations per time step for verlet solver
     * @param drag
     *            drag value 0...1
     * @param timeStep
     *            time step for calculating forces
     */
    public VerletPhysics(Vec3D gravity, int numIterations, float drag,
            float timeStep) {
        this();
        if (gravity != null) {
            this.gravity.set(gravity);
        }
        this.numIterations = numIterations;
        this.drag = drag;
        this.timeStep = timeStep;
    }

    /**
     * Adds a particle to the list
     * 
     * @param p
     * @return itself
     */
    public VerletPhysics addParticle(VerletParticle p) {
        particles.add(p);
        return this;
    }

    /**
     * Adds a spring connector
     * 
     * @param s
     * @return itself
     */
    public VerletPhysics addSpring(VerletSpring s) {
        if (getSpring(s.a, s.b) == null) {
            springs.add(s);
        }
        return this;
    }

    public VerletPhysics clear() {
        particles.clear();
        springs.clear();
        return this;
    }

    /**
     * Constrains all particle positions to the world bounding box set
     */
    protected void constrainToBounds() {
        for (VerletParticle p : particles) {
            if (p.bounds != null) {
                p.constrain(p.bounds);
            }
        }
        if (worldBounds != null) {
            for (VerletParticle p : particles) {
                p.constrain(worldBounds);
            }
        }
    }

    public AABB getCurrentBounds() {
        Vec3D min =
                new Vec3D(Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE);
        Vec3D max =
                new Vec3D(Float.MIN_VALUE, Float.MIN_VALUE, Float.MIN_VALUE);
        for (Iterator<VerletParticle> i = particles.iterator(); i.hasNext();) {
            VerletParticle p = i.next();
            min.minSelf(p);
            max.maxSelf(p);
        }
        return AABB.fromMinMax(min, max);
    }

    /**
     * @return the drag
     */
    public float getDrag() {
        return drag;
    }

    /**
     * @return the gravity
     */
    public Vec3D getGravity() {
        return gravity;
    }

    /**
     * @return the numIterations
     */
    public int getNumIterations() {
        return numIterations;
    }

    /**
     * Attempts to find the spring element between the 2 particles supplied
     * 
     * @param a
     *            particle 1
     * @param b
     *            particle 2
     * @return spring instance, or null if not found
     */
    public VerletSpring getSpring(Vec3D a, Vec3D b) {
        for (VerletSpring s : springs) {
            if ((s.a == a && s.b == b) || (s.a == b && s.b == a)) {
                return s;
            }
        }
        return null;
    }

    /**
     * @return the timeStep
     */
    public float getTimeStep() {
        return timeStep;
    }

    /**
     * @return the worldBounds
     */
    public AABB getWorldBounds() {
        return worldBounds;
    }

    /**
     * Removes a particle from the simulation.
     * 
     * @param p
     *            particle to remove
     * @return true, if removed successfully
     */
    public boolean removeParticle(VerletParticle p) {
        return particles.remove(p);
    }

    /**
     * Removes a spring connector from the simulation instance.
     * 
     * @param s
     *            spring to remove
     * @return true, if the spring has been removed
     */
    public boolean removeSpring(VerletSpring s) {
        return springs.remove(s);
    }

    /**
     * Removes a spring connector and its both end point particles from the
     * simulation
     * 
     * @param s
     *            spring to remove
     * @return true, only if spring AND particles have been removed successfully
     */
    public boolean removeSpringElements(VerletSpring s) {
        if (removeSpring(s)) {
            return (removeParticle(s.a) && removeParticle(s.b));
        }
        return false;
    }

    /**
     * @param drag
     *            the drag to set
     */
    public void setDrag(float drag) {
        this.drag = drag;
    }

    /**
     * @param gravity
     *            the gravity to set
     */
    public void setGravity(Vec3D gravity) {
        this.gravity = gravity;
    }

    /**
     * @param numIterations
     *            the numIterations to set
     */
    public void setNumIterations(int numIterations) {
        this.numIterations = numIterations;
    }

    /**
     * @param timeStep
     *            the timeStep to set
     */
    public void setTimeStep(float timeStep) {
        this.timeStep = timeStep;
    }

    /**
     * Sets bounding box
     * 
     * @param world
     * @return itself
     */
    public VerletPhysics setWorldBounds(AABB world) {
        worldBounds = world;
        return this;
    }

    /**
     * Progresses the physics simulation by 1 time step and updates all forces
     * and particle positions accordingly
     * 
     * @return itself
     */
    public VerletPhysics update() {
        updateParticles();
        updateSprings();
        constrainToBounds();
        return this;
    }

    /**
     * Updates all particle positions
     */
    protected void updateParticles() {
        Vec3D force = gravity.scale((1.0f - drag) * timeStep * timeStep);
        for (VerletParticle p : particles) {
            p.update(force);
        }
    }

    /**
     * Updates all spring connections based on new particle positions
     */
    protected void updateSprings() {
        for (int i = numIterations; i > 0; i--) {
            for (VerletSpring s : springs) {
                s.update(i == 1);
            }
        }
    }
}
