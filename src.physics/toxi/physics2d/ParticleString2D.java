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
import java.util.List;

import toxi.geom.Vec2D;

/**
 * Utility builder/grouping/management class to connect a set of particles into
 * a physical string/thread. Custom spring types can be used by subclassing this
 * class and overwriting the
 * {@link #createSpring(VerletParticle2D, VerletParticle2D, float, float)}
 * method.
 */
public class ParticleString2D {

    protected VerletPhysics2D physics;
    public List<VerletParticle2D> particles;
    public List<VerletSpring2D> links;

    /**
     * Takes a list of already created particles connects them into a continuous
     * string using springs.
     * 
     * @param physics
     *            physics engine instance
     * @param plist
     *            particle list
     * @param strength
     *            spring strength
     */
    public ParticleString2D(VerletPhysics2D physics,
            List<VerletParticle2D> plist, float strength) {
        this.physics = physics;
        particles = new ArrayList<VerletParticle2D>(plist);
        links = new ArrayList<VerletSpring2D>(particles.size() - 1);
        VerletParticle2D prev = null;
        for (VerletParticle2D p : particles) {
            physics.addParticle(p);
            if (prev != null) {
                VerletSpring2D s = createSpring(prev, p, prev.distanceTo(p),
                        strength);
                links.add(s);
                physics.addSpring(s);
            }
            prev = p;
        }
    }

    /**
     * Creates a number of particles along a line and connects them into a
     * string using springs.
     * 
     * @param physics
     *            physics engine
     * @param pos
     *            start position
     * @param step
     *            step direction & distance between successive particles
     * @param num
     *            number of particles
     * @param mass
     *            particle mass
     * @param strength
     *            spring strength
     */
    public ParticleString2D(VerletPhysics2D physics, Vec2D pos, Vec2D step,
            int num, float mass, float strength) {
        this.physics = physics;
        particles = new ArrayList<VerletParticle2D>(num);
        links = new ArrayList<VerletSpring2D>(num - 1);
        float len = step.magnitude();
        VerletParticle2D prev = null;
        pos = pos.copy();
        for (int i = 0; i < num; i++) {
            VerletParticle2D p = new VerletParticle2D(pos.copy(), mass);
            particles.add(p);
            physics.particles.add(p);
            if (prev != null) {
                VerletSpring2D s = createSpring(prev, p, len, strength);
                links.add(s);
                physics.addSpring(s);
            }
            prev = p;
            pos.addSelf(step);
        }
    }

    /**
     * Removes the entire string from the physics simulation, incl. all of its
     * particles & springs.
     */
    public void clear() {
        for (VerletSpring2D s : links) {
            physics.removeSpringElements(s);
        }
        particles.clear();
        links.clear();
    }

    /**
     * Creates a spring instance connecting 2 successive particles of the
     * string. Overwrite this method to create a string custom spring types
     * (subclassed from {@link VerletSpring3D}).
     * 
     * @param a
     *            1st particle
     * @param b
     *            2nd particle
     * @param len
     *            rest length
     * @param strength
     * @return spring
     */
    protected VerletSpring2D createSpring(VerletParticle2D a,
            VerletParticle2D b, float len, float strength) {
        return new VerletSpring2D(a, b, len, strength);
    }

    /**
     * Returns the first particle of the string.
     * 
     * @return first particle
     */
    public VerletParticle2D getHead() {
        return particles.get(0);
    }

    /**
     * Returns number of particles of the string.
     * 
     * @return particle count
     */
    public int getNumParticles() {
        return particles.size();
    }

    /**
     * Returns last particle of the string.
     * 
     * @return last particle
     */
    public VerletParticle2D getTail() {
        return particles.get(particles.size() - 1);
    }
}
