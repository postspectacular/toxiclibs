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

package toxi.physics3d.constraints;

import toxi.geom.Sphere;
import toxi.geom.Vec3D;
import toxi.physics3d.VerletParticle3D;

/**
 * This class implements a spherical constraint for 3D
 * {@linkplain VerletParticle3D}s. The constraint can be configured in two ways: A
 * bounding sphere not allowing particles to escape or alternatively does not
 * allow particles to enter the space occupied by the sphere.
 */
public class SphereConstraint implements ParticleConstraint3D {

    public Sphere sphere;

    public boolean isBoundingSphere;

    public final static boolean INSIDE = true;
    public final static boolean OUTSIDE = false;

    /**
     * Creates a new instance using the sphere definition and constraint mode
     * given.
     * 
     * @param sphere
     *            sphere instance
     * @param isBoundary
     *            constraint mode. Use {@linkplain #INSIDE} or
     *            {@linkplain #OUTSIDE} to specify constraint behaviour.
     */
    public SphereConstraint(Sphere sphere, boolean isBoundary) {
        this.sphere = sphere;
        this.isBoundingSphere = isBoundary;
    }

    /**
     * Creates a new instance using the sphere definition and constraint mode
     * given.
     * 
     * @param origin
     *            sphere origin
     * @param radius
     *            sphere radius
     * @param isBoundary
     *            constraint mode. Use {@linkplain #INSIDE} or
     *            {@linkplain #OUTSIDE} to specify constraint behaviour.
     */
    public SphereConstraint(Vec3D origin, float radius, boolean isBoundary) {
        sphere = new Sphere(origin, radius);
        this.isBoundingSphere = isBoundary;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * toxi.physics.constraints.ParticleConstraint#apply(toxi.physics.VerletParticle
     * )
     */
    public void apply(VerletParticle3D p) {
        boolean isInside = sphere.containsPoint(p);
        if ((isBoundingSphere && !isInside) || (!isBoundingSphere && isInside)) {
            p.set(sphere.add(p.subSelf(sphere).normalizeTo(sphere.radius)));
        }
    }

}
