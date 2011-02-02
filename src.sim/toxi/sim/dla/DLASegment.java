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

package toxi.sim.dla;

import toxi.geom.Line3D;
import toxi.geom.Vec3D;

public class DLASegment extends Line3D {

    protected Vec3D dir, nextDir;

    public DLASegment(Vec3D a, Vec3D b, Vec3D c) {
        super(a, b);
        this.dir = b.sub(a).normalize();
        this.nextDir = c != null ? c.sub(b).normalize() : dir.copy();
    }

    public Vec3D getDirectionAt(float currT) {
        return getDirection().interpolateToSelf(nextDir, currT);
    }

    public Vec3D getNextDirection() {
        return nextDir;
    }

    public String toString() {
        return a.toString() + " -> " + b.toString() + " dir: " + dir + " nd: "
                + nextDir;
    }
}