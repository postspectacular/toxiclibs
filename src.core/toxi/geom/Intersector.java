/* 
 * Copyright (c) 2007 Karsten Schmidt
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
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

package toxi.geom;

/**
 * Generic interface for ray intersection with 3D geometry
 */
public interface Intersector {

    /**
     * @param normalized
     *            true, if a normalized version should be returned
     * @return direction vector from ray origin to intersection point
     */
    public Vec3D getIntersectionDir(boolean normalized);

    /**
     * @return distance from ray origin to intersection point
     */
    public float getIntersectionDistance();

    /**
     * @return point of intersection on the entity surface
     */
    public Vec3D getIntersectionPoint();

    /**
     * @return entity's surface normal vector at intersection point
     */
    public Vec3D getNormalAtIntersection();

    /**
     * Check if entity intersects with the given ray
     * 
     * @param ray
     *            ray to check
     * @return true, if ray hits the entity
     */
    public boolean intersectsRay(Ray3D ray);

}