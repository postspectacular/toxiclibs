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
import java.util.Iterator;
import java.util.List;

import toxi.geom.Rect;
import toxi.geom.SpatialIndex;
import toxi.geom.Vec2D;
import toxi.physics2d.behaviors.GravityBehavior2D;
import toxi.physics2d.behaviors.ParticleBehavior2D;
import toxi.physics2d.constraints.ParticleConstraint2D;

/**
 * 3D particle physics engine using Verlet integration based on:
 * http://en.wikipedia.org/wiki/Verlet_integration
 * http://www.teknikus.dk/tj/gdc2001.htm
 * 
 */
public class VerletPhysics2D {

    public static void addConstraintToAll(ParticleConstraint2D c,
            List<VerletParticle2D> list) {
        for (VerletParticle2D p : list) {
            p.addConstraint(c);
        }
    }

    public static void removeConstraintFromAll(ParticleConstraint2D c,
            List<VerletParticle2D> list) {
        for (VerletParticle2D p : list) {
            p.removeConstraint(c);
        }
    }

    /**
     * List of particles
     */
    public ArrayList<VerletParticle2D> particles;

    /**
     * List of spring/stick connectors
     */
    public ArrayList<VerletSpring2D> springs;

    /**
     * Default time step = 1.0
     */
    protected float timeStep;

    /**
     * Default iterations for verlet solver = 50
     */
    protected int numIterations;

    /**
     * Optional bounding rect to constrain particles too
     */
    protected Rect worldBounds;

    public final List<ParticleBehavior2D> behaviors = new ArrayList<ParticleBehavior2D>(
            1);

    public final List<ParticleConstraint2D> constraints = new ArrayList<ParticleConstraint2D>(
            1);

    protected float drag;

    protected SpatialIndex<Vec2D> index;

    /**
     * Initializes a Verlet engine instance using the default values.
     */
    public VerletPhysics2D() {
        this(null, 50, 0, 1);
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
    public VerletPhysics2D(Vec2D gravity, int numIterations, float drag,
            float timeStep) {
        particles = new ArrayList<VerletParticle2D>();
        springs = new ArrayList<VerletSpring2D>();
        this.numIterations = numIterations;
        this.timeStep = timeStep;
        setDrag(drag);
        if (gravity != null) {
            addBehavior(new GravityBehavior2D(gravity));
        }
    }

    public void addBehavior(ParticleBehavior2D behavior) {
        behavior.configure(timeStep);
        behaviors.add(behavior);
    }

    public void addConstraint(ParticleConstraint2D constraint) {
        constraints.add(constraint);
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

    /**
     * Applies all global constraints and constrains all particle positions to
     * the world bounding rect set.
     */
    protected void applyConstaints() {
        boolean hasGlobalConstraints = constraints.size() > 0;
        for (VerletParticle2D p : particles) {
            if (hasGlobalConstraints) {
                for (ParticleConstraint2D c : constraints) {
                    c.apply(p);
                }
            }
            if (p.bounds != null) {
                p.constrain(p.bounds);
            }
            if (worldBounds != null) {
                p.constrain(worldBounds);
            }
        }
    }

    public VerletPhysics2D clear() {
        behaviors.clear();
        constraints.clear();
        particles.clear();
        springs.clear();
        return this;
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

    public float getDrag() {
        return 1f - drag;
    }

    /**
     * @return the index
     */
    public SpatialIndex<Vec2D> getIndex() {
        return index;
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
    public VerletSpring2D getSpring(Vec2D a, Vec2D b) {
        for (VerletSpring2D s : springs) {
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
    public Rect getWorldBounds() {
        return worldBounds;
    }

    public boolean removeBehavior(ParticleBehavior2D c) {
        return behaviors.remove(c);
    }

    public boolean removeConstraint(ParticleConstraint2D c) {
        return constraints.remove(c);
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

    public void setDrag(float drag) {
        this.drag = 1f - drag;
    }

    /**
     * @param index
     *            the index to set
     */
    public void setIndex(SpatialIndex<Vec2D> index) {
        this.index = index;
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
        for (ParticleBehavior2D b : behaviors) {
            b.configure(timeStep);
        }
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
        applyConstaints();
        updateIndex();
        return this;
    }

    private void updateIndex() {
        if (index != null) {
            index.clear();
            for (VerletParticle2D p : particles) {
                index.index(p);
            }
        }
    }

    /**
     * Updates all particle positions
     */
    protected void updateParticles() {
        for (ParticleBehavior2D b : behaviors) {
            if (index != null && b.supportsSpatialIndex()) {
                b.applyWithIndex(index);
            } else {
                for (VerletParticle2D p : particles) {
                    b.apply(p);
                }
            }
        }
        for (VerletParticle2D p : particles) {
            p.scaleVelocity(drag);
            p.update();
        }
    }

    /**
     * Updates all spring connections based on new particle positions
     */
    protected void updateSprings() {
        if (springs.size() > 0) {
            for (int i = numIterations; i > 0; i--) {
                for (VerletSpring2D s : springs) {
                    s.update(i == 1);
                }
            }
        }
    }
}