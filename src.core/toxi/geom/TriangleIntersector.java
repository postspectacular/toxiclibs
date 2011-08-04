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

public class TriangleIntersector implements Intersector3D {

    public Triangle3D triangle;
    private IsectData3D isectData;

    public TriangleIntersector() {
        this(new Triangle3D());
    }

    public TriangleIntersector(Triangle3D t) {
        this.triangle = t;
        this.isectData = new IsectData3D();
    }

    public IsectData3D getIntersectionData() {
        return isectData;
    }

    /**
     * @return the triangle
     */
    public Triangle3D getTriangle() {
        return triangle;
    }

    public boolean intersectsRay(Ray3D ray) {
        isectData.isIntersection = false;
        Vec3D n = triangle.computeNormal();
        float dotprod = n.dot(ray.dir);
        if (dotprod < 0) {
            Vec3D rt = ray.sub(triangle.a);
            double t = -(double) (n.x * rt.x + n.y * rt.y + n.z * rt.z)
                    / (n.x * ray.dir.x + n.y * ray.dir.y + n.z * ray.dir.z);
            if (t >= MathUtils.EPS) {
                Vec3D pos = ray.getPointAtDistance((float) t);
                // check if pos is inside triangle
                if (triangle.containsPoint(pos)) {
                    isectData.isIntersection = true;
                    isectData.pos = pos;
                    isectData.normal = n;
                    isectData.dist = (float) t;
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
    public TriangleIntersector setTriangle(Triangle3D tri) {
        this.triangle = tri;
        return this;
    }
}
