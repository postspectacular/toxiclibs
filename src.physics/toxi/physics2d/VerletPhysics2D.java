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

package toxi.physics2d;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import toxi.geom.Rect;
import toxi.geom.Vec2D;
import toxi.physics2d.constraints.Particle2DConstraint;

/**
 * 3D particle physics engine using Verlet integration based on:
 * http://en.wikipedia.org/wiki/Verlet_integration
 * http://www.teknikus.dk/tj/gdc2001.htm
 * 
 */
public class VerletPhysics2D {

    public static void addConstraintToAll(Particle2DConstraint c,
            List<VerletParticle2D> list) {
        for (VerletParticle2D p : list) {
            p.addConstraint(c);
        }
    }

    public static void removeConstraintFromAll(Particle2DConstraint c,
            List<VerletParticle2D> list) {
        for (VerletParticle2D p : list) {
            p.removeConstraint(c);
        }
    }

    /**
     * List of particles (Vec2D subclassed)
     */
    public ArrayList<VerletParticle2D> particles;

    /**
     * List of spring/sticks connectors
     */
    public ArrayList<VerletSpring2D> springs;

    /**
     * Default friction value = 0.15
     */
    public float friction = 0.15f;

    /**
     * Default time step = 1.0
     */
    public float timeStep = 1.0f;

    /**
     * Default iterations for verlet solver = 50
     */
    public int numIterations = 50;

    /**
     * Gravity vector (by default inactive)
     */
    public Vec2D gravity = new Vec2D();

    /**
     * Optional 3D bounding box to constrain particles too
     */
    public Rect worldBounds;

    /**
     * Initializes a Verlet engine instance using the default values.
     */
    public VerletPhysics2D() {
        particles = new ArrayList<VerletParticle2D>();
        springs = new ArrayList<VerletSpring2D>();
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
    public VerletPhysics2D(Vec2D gravity, int numIterations, float friction,
            float timeStep) {
        this();
        if (gravity != null) {
            this.gravity.set(gravity);
        }
        this.numIterations = numIterations;
        this.friction = friction;
        this.timeStep = timeStep;
    }

    /**
     * Adds a particle to the list
     * 
     * @param p
     * @return itself
     */
    public VerletPhysics2D addParticle(VerletParticle2D p) {
        particles.add(p);
        return this;
    }

    /**
     * Adds a spring connector
     * 
     * @param s
     * @return itself
     */
    public VerletPhysics2D addSpring(VerletSpring2D s) {
        if (getSpring(s.a, s.b) == null) {
            springs.add(s);
        }
        return this;
    }

    public VerletPhysics2D clear() {
        particles.clear();
        springs.clear();
        return this;
    }

    /**
     * Constrains all particle positions to the world bounding box set
     */
    protected void constrainToBounds() {
        for (VerletParticle2D p : particles) {
            if (p.bounds != null) {
                p.constrain(p.bounds);
            }
        }
        if (worldBounds != null) {
            for (VerletParticle2D p : particles) {
                p.constrain(worldBounds);
            }
        }
    }

    public Rect getCurrentBounds() {
        Vec2D min = new Vec2D(Float.MAX_VALUE, Float.MAX_VALUE);
        Vec2D max = new Vec2D(Float.MIN_VALUE, Float.MIN_VALUE);
        for (Iterator<VerletParticle2D> i = particles.iterator(); i.hasNext();) {
            VerletParticle2D p = i.next();
            min.minSelf(p);
            max.maxSelf(p);
        }
        return new Rect(min, max);
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
    public VerletSpring2D getSpring(Vec2D a, Vec2D b) {
        for (VerletSpring2D s : springs) {
            if ((s.a == a && s.b == b) || (s.a == b && s.b == a)) {
                return s;
            }
        }
        return null;
    }

    /**
     * Removes a particle from the simulation.
     * 
     * @param p
     *            particle to remove
     * @return true, if removed successfully
     */
    public boolean removeParticle(VerletParticle2D p) {
        return particles.remove(p);
    }

    /**
     * Removes a spring connector from the simulation instance.
     * 
     * @param s
     *            spring to remove
     * @return true, if the spring has been removed
     */
    public boolean removeSpring(VerletSpring2D s) {
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
    public boolean removeSpringElements(VerletSpring2D s) {
        if (removeSpring(s)) {
            return (removeParticle(s.a) && removeParticle(s.b));
        }
        return false;
    }

    /**
     * Sets bounding box
     * 
     * @param world
     * @return itself
     */
    public VerletPhysics2D setWorldBounds(Rect world) {
        worldBounds = world;
        return this;
    }

    /**
     * Progresses the physics simulation by 1 time step and updates all forces
     * and particle positions accordingly
     * 
     * @return itself
     */
    public VerletPhysics2D update() {
        updateParticles();
        updateSprings();
        constrainToBounds();
        return this;
    }

    /**
     * Updates all particle positions
     */
    protected void updateParticles() {
        Vec2D force = gravity.scale((1.0f - friction) * timeStep * timeStep);
        for (VerletParticle2D p : particles) {
            p.update(force);
        }
    }

    /**
     * Updates all spring connections based on new particle positions
     */
    protected void updateSprings() {
        for (int i = numIterations; i > 0; i--) {
            for (VerletSpring2D s : springs) {
                s.update(i == 1);
            }
        }
    }
}
