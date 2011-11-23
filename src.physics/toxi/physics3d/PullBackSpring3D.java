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
 * Creates a pullback spring (default restlength=0.5) between 2 particles and
 * locks the first one given at the current position. The spring is only
 * enforced if the current length of the spring exceeds the rest length. This
 * behaviour is the opposite to the {@link VerletMinDistanceSpring3D}.
 */
class PullBackSpring3D extends VerletSpring3D {

    public PullBackSpring3D(VerletParticle3D a, VerletParticle3D b, float strength) {
        super(a, b, 0, strength);
        a.lock();
        setRestLength(0);
    }

    protected void update(boolean applyConstraints) {
        if (b.distanceToSquared(a) > 0.5f) {
            super.update(applyConstraints);
        }
    }
}