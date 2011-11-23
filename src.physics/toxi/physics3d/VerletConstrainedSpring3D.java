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

package toxi.physics3d;

import toxi.geom.Vec3D;

/**
 * Implements a spring whose maximum relaxation distance at every time step can
 * be limited to achieve better (if physically incorrect) stability of the whole
 * spring system.
 */
public class VerletConstrainedSpring3D extends VerletSpring3D {

    /**
     * Maximum relaxation distance for either end of the spring in world units
     */
    public float limit = Float.MAX_VALUE;

    /**
     * @param a
     * @param b
     * @param len
     * @param str
     */
    public VerletConstrainedSpring3D(VerletParticle3D a, VerletParticle3D b,
            float len, float str) {
        super(a, b, len, str);
    }

    /**
     * @param a
     * @param b
     * @param len
     * @param str
     * @param limit
     */
    public VerletConstrainedSpring3D(VerletParticle3D a, VerletParticle3D b,
            float len, float str, float limit) {
        super(a, b, len, str);
        this.limit = limit;
    }

    protected void update(boolean applyConstraints) {
        Vec3D delta = b.sub(a);
        // add minute offset to avoid div-by-zero errors
        float dist = delta.magnitude() + EPS;
        float normDistStrength = (dist - restLength)
                / (dist * (a.invWeight + b.invWeight)) * strength;
        if (!a.isLocked && !isALocked) {
            a.addSelf(delta.scale(normDistStrength * a.invWeight).limit(limit));
            if (applyConstraints) {
                a.applyConstraints();
            }
        }
        if (!b.isLocked && !isBLocked) {
            b.subSelf(delta.scale(normDistStrength * b.invWeight).limit(limit));
            if (applyConstraints) {
                b.applyConstraints();
            }
        }
    }
}
