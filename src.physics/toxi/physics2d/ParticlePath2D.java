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

import toxi.geom.Spline2D;
import toxi.geom.Vec2D;

/**
 * This class is used as a builder to dynamically construct a
 * {@link ParticleString2D} following a given spline path, sampled at a fixed
 * frequency/step distance. This functionality is needed especially when working
 * with various obstacles/mechanic constraints which the string should flow/wrap
 * around.
 */
public class ParticlePath2D extends Spline2D {

    List<VerletParticle2D> particles = new ArrayList<VerletParticle2D>();

    public ParticlePath2D() {
        super();
    }

    public ParticlePath2D(List<Vec2D> points) {
        super(points);
    }

    /**
     * Creates particles along the spline at the fixed interval given. The
     * precision of this interval will largely depend on the number of
     * subdivision vertices created, but can be adjusted via the related
     * parameter.
     * 
     * @param physics
     *            physics instance
     * @param subDiv
     *            number spline segment subdivisions
     * @param step
     *            desired rest length between particles
     * @param mass
     *            desired particle mass
     * @return list of particles
     */
    public List<VerletParticle2D> createParticles(VerletPhysics2D physics,
            int subDiv, float step, float mass) {
        particles.clear();
        for (Vec2D v : toLineStrip2D(subDiv).getDecimatedVertices(step, true)) {
            VerletParticle2D p = createSingleParticle(v, mass);
            particles.add(p);
            physics.addParticle(p);
        }
        return particles;
    }

    /**
     * Extension point for creating a custom/sub-classed VerletParticle
     * instance.
     * 
     * @param pos
     * @param mass
     * @return particle
     */
    protected VerletParticle2D createSingleParticle(Vec2D pos, float mass) {
        return new VerletParticle2D(pos, mass);
    }
}
