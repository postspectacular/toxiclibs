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

package toxi.geom;

import toxi.math.MathUtils;

public class Ray3DIntersector implements Intersector3D {

    public Ray3D ray;
    private IsectData3D isec;

    public Ray3DIntersector(Ray3D ray) {
        this.ray = ray;
        isec = new IsectData3D();
    }

    public IsectData3D getIntersectionData() {
        return isec;
    }

    public boolean intersectsRay(Ray3D other) {
        Vec3D n = ray.dir.cross(other.dir);
        Vec3D sr = ray.sub(other);
        float absX = MathUtils.abs(n.x);
        float absY = MathUtils.abs(n.y);
        float absZ = MathUtils.abs(n.z);
        float t;
        if (absZ > absX && absZ > absY) {
            t = (sr.x * other.dir.y - sr.y * other.dir.x) / n.z;
        } else if (absX > absY) {
            t = (sr.y * other.dir.z - sr.z * other.dir.y) / n.x;
        } else {
            t = (sr.z * other.dir.x - sr.x * other.dir.z) / n.y;
        }
        isec.isIntersection = (t <= MathUtils.EPS && !Float.isInfinite(t));
        isec.pos = ray.getPointAtDistance(-t);
        return isec.isIntersection;
    }
}