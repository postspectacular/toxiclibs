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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import toxi.math.MathUtils;

public class PointCloud3D implements Iterable<Vec3D> {

    protected List<Vec3D> points;

    protected Vec3D min, max;
    protected Vec3D centroid;

    protected float radiusSquared;

    public PointCloud3D() {
        this(100);
    }

    public PointCloud3D(int numPoints) {
        points = new ArrayList<Vec3D>(numPoints);
        clear();
    }

    public PointCloud3D addAll(List<? extends Vec3D> plist) {
        for (Vec3D p : plist) {
            addPoint(p);
        }
        return this;
    }

    public PointCloud3D addPoint(Vec3D p) {
        points.add(p);
        min.minSelf(p);
        max.maxSelf(p);
        centroid.set(min.add(max).scaleSelf(0.5f));
        radiusSquared = MathUtils.max(radiusSquared,
                p.distanceToSquared(centroid));
        return this;
    }

    /**
     * Applies the given transformation matrix to all points in the cloud.
     * 
     * @param m
     *            transformation matrix
     * @return itself
     */
    public PointCloud3D applyMatrix(Matrix4x4 m) {
        for (Vec3D p : points) {
            p.set(m.applyTo(p));
        }
        updateBounds();
        return this;
    }

    /**
     * Updates all points in the cloud so that their new centroid is at the
     * origin.
     * 
     * @return itself
     */
    public PointCloud3D center() {
        return center(null);
    }

    /**
     * Updates all points in the cloud so that their new centroid is at the
     * given point.
     * 
     * @param origin
     *            new centroid
     * @return itself
     */
    public PointCloud3D center(ReadonlyVec3D origin) {
        getCentroid();
        Vec3D delta = origin != null ? origin.sub(centroid) : centroid
                .getInverted();
        for (Vec3D p : points) {
            p.addSelf(delta);
        }
        min.addSelf(delta);
        max.addSelf(delta);
        centroid.addSelf(delta);
        return this;
    }

    /**
     * Removes all points from the cloud and resets the bounds and centroid.
     * 
     * @return itself
     */
    public PointCloud3D clear() {
        points.clear();
        min = Vec3D.MAX_VALUE.copy();
        max = Vec3D.NEG_MAX_VALUE.copy();
        centroid = new Vec3D();
        return this;
    }

    /**
     * Creates a deep copy of the cloud
     * 
     * @return copied instance
     */
    public PointCloud3D copy() {
        PointCloud3D c = new PointCloud3D(points.size());
        for (ReadonlyVec3D p : points) {
            c.addPoint(p.copy());
        }
        return c;
    }

    public AABB getBoundingBox() {
        return AABB.fromMinMax(min, max);
    }

    public Sphere getBoundingSphere() {
        return new Sphere(getCentroid(), (float) Math.sqrt(radiusSquared));
    }

    /**
     * @return the cloud centroid
     */
    public Vec3D getCentroid() {
        return centroid;
    }

    /**
     * @return an iterator for the backing point collection.
     * 
     * @see java.lang.Iterable#iterator()
     */
    public Iterator<Vec3D> iterator() {
        return points.iterator();
    }

    /**
     * Removes the point from the cloud, but doesn't update the bounds
     * automatically.
     * 
     * @param p
     * @return true, if point has been removed.
     */
    public boolean removePoint(ReadonlyVec3D p) {
        return points.remove(p);
    }

    /**
     * @return the current number of points in the cloud
     */
    public int size() {
        return points.size();
    }

    /**
     * Recalculates the bounding box, bounding sphere and centroid of the cloud.
     * 
     * @return itself
     */
    public PointCloud3D updateBounds() {
        min = Vec3D.MAX_VALUE.copy();
        max = Vec3D.NEG_MAX_VALUE.copy();
        for (Vec3D p : points) {
            min.minSelf(p);
            max.maxSelf(p);
        }
        centroid.set(min.add(max).scaleSelf(0.5f));
        radiusSquared = 0;
        for (ReadonlyVec3D p : points) {
            radiusSquared = MathUtils.max(radiusSquared,
                    p.distanceToSquared(centroid));
        }
        return this;
    }
}
