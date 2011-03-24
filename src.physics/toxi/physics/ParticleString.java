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

import toxi.geom.Vec3D;

/**
 * Utility builder/grouping/management class to connect a set of particles into
 * a physical string/thread. Custom spring types can be used by subclassing this
 * class and overwriting the
 * {@link #createSpring(VerletParticle, VerletParticle, float, float)} method.
 */
public class ParticleString {

    protected VerletPhysics physics;
    public List<VerletParticle> particles;
    public List<VerletSpring> links;

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
    public ParticleString(VerletPhysics physics, List<VerletParticle> plist,
            float strength) {
        this.physics = physics;
        particles = new ArrayList<VerletParticle>(plist);
        links = new ArrayList<VerletSpring>(particles.size() - 1);
        VerletParticle prev = null;
        for (VerletParticle p : particles) {
            physics.addParticle(p);
            if (prev != null) {
                VerletSpring s = createSpring(prev, p, prev.distanceTo(p),
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
    public ParticleString(VerletPhysics physics, Vec3D pos, Vec3D step,
            int num, float mass, float strength) {
        this.physics = physics;
        particles = new ArrayList<VerletParticle>(num);
        links = new ArrayList<VerletSpring>(num - 1);
        float len = step.magnitude();
        VerletParticle prev = null;
        pos = pos.copy();
        for (int i = 0; i < num; i++) {
            VerletParticle p = new VerletParticle(pos.copy(), mass);
            particles.add(p);
            physics.particles.add(p);
            if (prev != null) {
                VerletSpring s = createSpring(prev, p, len, strength);
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
        for (VerletSpring s : links) {
            physics.removeSpringElements(s);
        }
        particles.clear();
        links.clear();
    }

    /**
     * Creates a spring instance connecting 2 successive particles of the
     * string. Overwrite this method to create a string custom spring types
     * (subclassed from {@link VerletSpring}).
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
    protected VerletSpring createSpring(VerletParticle a, VerletParticle b,
            float len, float strength) {
        return new VerletSpring(a, b, len, strength);
    }

    /**
     * Returns the first particle of the string.
     * 
     * @return first particle
     */
    public VerletParticle getHead() {
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
    public VerletParticle getTail() {
        return particles.get(particles.size() - 1);
    }
}
