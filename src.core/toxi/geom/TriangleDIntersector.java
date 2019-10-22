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

public class TriangleDIntersector implements IntersectorD3D {

    public TriangleD3D triangle;
    private IsectDataD3D isectData;

    public TriangleDIntersector() {
        this(new TriangleD3D());
    }

    public TriangleDIntersector(TriangleD3D t) {
        this.triangle = t;
        this.isectData = new IsectDataD3D();
    }

    public IsectDataD3D getIntersectionDataD() {
        return isectData;
    }

    /**
     * @return the triangle
     */
    public TriangleD3D getTriangleD() {
        return triangle;
    }

    public boolean intersectsRayD(RayD3D ray) {
        isectData.isIntersection = false;
        VecD3D n = triangle.computeNormal();
        double dotprod = n.dot(ray.dir);
        if (dotprod < 0) {
            VecD3D rt = ray.sub(triangle.a);
            double t = -(double) (n.x * rt.x + n.y * rt.y + n.z * rt.z)
                    / (n.x * ray.dir.x + n.y * ray.dir.y + n.z * ray.dir.z);
            if (t >= MathUtils.EPS) {
                VecD3D pos = ray.getPointAtDistance(t);
                // check if pos is inside triangle
                if (triangle.containsPoint(pos)) {
                    isectData.isIntersection = true;
                    isectData.pos = pos;
                    isectData.normal = n;
                    isectData.dist = t;
                    isectData.dir = isectData.pos.sub(ray).normalize();
                }
            }
        }
        return isectData.isIntersection;
    }

    /**
     * @param tri
     *            the triangle to set
     */
    public TriangleDIntersector setTriangleD(TriangleD3D tri) {
        this.triangle = tri;
        return this;
    }
}
