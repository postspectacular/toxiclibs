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

/**
 * Implements a string which will only enforce its rest length if the current
 * distance is less than its rest length. This is handy if you just want to
 * ensure objects are at least a certain distance from each other, but don't
 * care if it's bigger than the enforced minimum.
 */
public class VerletMinDistanceSpring3D extends VerletSpring3D {

    public VerletMinDistanceSpring3D(VerletParticle3D a, VerletParticle3D b,
            float len, float str) {
        super(a, b, len, str);
    }

    public void update(boolean applyConstraints) {
        if (b.distanceToSquared(a) < restLengthSquared) {
            super.update(applyConstraints);
        }
    }
}
