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

import toxi.geom.Vec3D;
import toxi.geom.Vec3D.Axis;
import toxi.physics3d.VerletParticle3D;

/**
 * Constrains a particle's movement by locking it to a fixed axis aligned plane.
 */
public class PlaneConstraint implements ParticleConstraint3D {

    public Vec3D constraint;
    public Axis axis1, axis2;

    /**
     * @param axis
     *            1st axis to lock
     * @param axis2
     *            2d axis to lock
     * @param constraint
     *            point on the desired constraint plane
     */
    public PlaneConstraint(Axis axis, Axis axis2, Vec3D constraint) {
        this.axis1 = axis;
        this.axis2 = axis2;
        this.constraint = constraint;
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.physics.IParticleConstraint#apply(toxi.physics.VerletParticle)
     */
    public void apply(VerletParticle3D p) {
        p.setComponent(axis1, constraint.getComponent(axis1));
        p.setComponent(axis2, constraint.getComponent(axis2));
    }

}
