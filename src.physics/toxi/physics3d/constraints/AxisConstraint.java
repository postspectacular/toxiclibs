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

import toxi.geom.Vec3D.Axis;
import toxi.physics3d.VerletParticle3D;

/**
 * Constrains a particle's movement by locking a given axis to a fixed value.
 */
public class AxisConstraint implements ParticleConstraint3D {

    public float constraint;
    public Axis axis;

    /**
     * @param axis
     *            axis to lock
     * @param constraint
     *            constrain the axis to this value
     */
    public AxisConstraint(Axis axis, float constraint) {
        this.axis = axis;
        this.constraint = constraint;
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.physics.IParticleConstraint#apply(toxi.physics.VerletParticle)
     */
    public void apply(VerletParticle3D p) {
        p.setComponent(axis, constraint);
    }

}
