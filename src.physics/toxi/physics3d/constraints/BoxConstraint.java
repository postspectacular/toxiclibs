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

import toxi.geom.AABB;
import toxi.geom.Ray3D;
import toxi.geom.Vec3D;
import toxi.physics3d.VerletParticle3D;

public class BoxConstraint implements ParticleConstraint3D {

    protected AABB box;
    protected Ray3D intersectRay;
    private float restitution = 1;

    public BoxConstraint(AABB box) {
        this.box = box.copy();
        this.intersectRay = new Ray3D(box, new Vec3D());
    }

    public BoxConstraint(Vec3D min, Vec3D max) {
        this(AABB.fromMinMax(min, max));
    }

    public void apply(VerletParticle3D p) {
        if (p.isInAABB(box)) {
            Vec3D dir = p.getVelocity();
            Vec3D prev = p.getPreviousPosition();
            if (prev.isInAABB(box)) {
                dir.invert();
            }
            intersectRay.set(prev);
            intersectRay.setDirection(dir);
            Vec3D isec = box.intersectsRay(intersectRay, 0, Float.MAX_VALUE);
            if (isec != null) {
                isec.addSelf(box.getNormalForPoint(isec).scaleSelf(0.01f));
                p.setPreviousPosition(isec);
                p.set(isec.sub(dir.scaleSelf(restitution)));
            }
        }
    }

    public AABB getBox() {
        return box.copy();
    }

    public float getRestitution() {
        return restitution;
    }

    public void setBox(AABB box) {
        this.box = box.copy();
        intersectRay.set(box);
    }

    public void setRestitution(float restitution) {
        this.restitution = restitution;
    }
}
