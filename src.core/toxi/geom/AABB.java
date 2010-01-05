/*
 * Copyright (c) 2007 Karsten Schmidt
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * http://creativecommons.org/licenses/LGPL/2.1/
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA
 */

package toxi.geom;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import toxi.math.MathUtils;

/**
 * Axis-aligned bounding box with basic intersection features for Ray, AABB and
 * Sphere classes.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class AABB extends Vec3D {

    /**
     * Creates a new instance from two vectors specifying opposite corners of
     * the box
     * 
     * @param min
     *            first corner point
     * @param max
     *            second corner point
     * @return new AABB with centre at the half point between the 2 input
     *         vectors
     */
    public static final AABB fromMinMax(Vec3D min, Vec3D max) {
        Vec3D a = Vec3D.min(min, max);
        Vec3D b = Vec3D.max(min, max);
        return new AABB(a.interpolateTo(b, 0.5f), b.sub(a).scaleSelf(0.5f));
    }

    @XmlElement(required = true)
    protected Vec3D extent;

    @XmlTransient
    protected Vec3D min, max;

    public AABB() {
        super();
        setExtent(new Vec3D());
    }

    /**
     * Creates an independent copy of the passed in box
     * 
     * @param box
     */
    public AABB(AABB box) {
        this(box, box.getExtent());
    }

    /**
     * Creates a new instance from centre point and extent
     * 
     * @param pos
     * @param extent
     *            box dimensions (the box will be double the size in each
     *            direction)
     */
    public AABB(Vec3D pos, Vec3D extent) {
        super(pos);
        setExtent(extent);
    }

    public AABB copy() {
        return new AABB(this);
    }

    /**
     * Returns the current box size as new Vec3D instance (updating this vector
     * will NOT update the box size! Use {@link #setExtent(Vec3D)} for those
     * purposes)
     * 
     * @return box size
     */
    public final Vec3D getExtent() {
        return extent.copy();
    }

    public final Vec3D getMax() {
        // return this.add(extent);
        return max.copy();
    }

    public final Vec3D getMin() {
        return min.copy();
        // return this.sub(extent);
    }

    /**
     * Checks if the box intersects the passed in one.
     * 
     * @param box
     *            box to check
     * @return true, if boxes overlap
     */
    public boolean intersectsBox(AABB box) {
        Vec3D t = box.sub(this);
        return MathUtils.abs(t.x) <= (extent.x + box.extent.x)
                && MathUtils.abs(t.y) <= (extent.y + box.extent.y)
                && MathUtils.abs(t.z) <= (extent.z + box.extent.z);
    }

    /**
     * Calculates intersection with the given ray between a certain distance
     * interval.
     * 
     * Ray-box intersection is using IEEE numerical properties to ensure the
     * test is both robust and efficient, as described in:
     * 
     * Amy Williams, Steve Barrus, R. Keith Morley, and Peter Shirley: "An
     * Efficient and Robust Ray-Box Intersection Algorithm" Journal of graphics
     * tools, 10(1):49-54, 2005
     * 
     * @param ray
     *            incident ray
     * @param minDist
     * @param maxDist
     * @return intersection point on the bounding box (only the first is
     *         returned) or null if no intersection
     */
    public Vec3D intersectsRay(Ray3D ray, float minDist, float maxDist) {
        Vec3D invDir = ray.getDirection().reciprocal();
        boolean signDirX = invDir.x < 0;
        boolean signDirY = invDir.y < 0;
        boolean signDirZ = invDir.z < 0;
        Vec3D bbox = signDirX ? max : min;
        float tmin = (bbox.x - ray.x) * invDir.x;
        bbox = signDirX ? min : max;
        float tmax = (bbox.x - ray.x) * invDir.x;
        bbox = signDirY ? max : min;
        float tymin = (bbox.y - ray.y) * invDir.y;
        bbox = signDirY ? min : max;
        float tymax = (bbox.y - ray.y) * invDir.y;
        if ((tmin > tymax) || (tymin > tmax)) {
            return null;
        }
        if (tymin > tmin) {
            tmin = tymin;
        }
        if (tymax < tmax) {
            tmax = tymax;
        }
        bbox = signDirZ ? max : min;
        float tzmin = (bbox.z - ray.z) * invDir.z;
        bbox = signDirZ ? min : max;
        float tzmax = (bbox.z - ray.z) * invDir.z;
        if ((tmin > tzmax) || (tzmin > tmax)) {
            return null;
        }
        if (tzmin > tmin) {
            tmin = tzmin;
        }
        if (tzmax < tmax) {
            tmax = tzmax;
        }
        if ((tmin < maxDist) && (tmax > minDist)) {
            return ray.getPointAtDistance(tmin);
        }
        return null;
    }

    public boolean intersectsSphere(Sphere s) {
        return intersectsSphere(s, s.radius);
    }

    /**
     * @param c
     *            sphere centre
     * @param r
     *            sphere radius
     * @return true, if AABB intersects with sphere
     */
    public boolean intersectsSphere(Vec3D c, float r) {
        float s, d = 0;
        // find the square of the distance
        // from the sphere to the box
        if (c.x < min.x) {
            s = c.x - min.x;
            d = s * s;
        } else if (c.x > max.x) {
            s = c.x - max.x;
            d += s * s;
        }

        if (c.y < min.y) {
            s = c.y - min.y;
            d += s * s;
        } else if (c.y > max.y) {
            s = c.y - max.y;
            d += s * s;
        }

        if (c.z < min.z) {
            s = c.z - min.z;
            d += s * s;
        } else if (c.z > max.z) {
            s = c.z - max.z;
            d += s * s;
        }

        return d <= r * r;
    }

    @Deprecated
    public final float maxX() {
        return x + extent.x;
    }

    @Deprecated
    public final float maxY() {
        return y + extent.y;
    }

    @Deprecated
    public final float maxZ() {
        return z + extent.z;
    }

    @Deprecated
    public final float minX() {
        return x - extent.x;
    }

    @Deprecated
    public final float minY() {
        return y - extent.y;
    }

    @Deprecated
    public final float minZ() {
        return z - extent.z;
    }

    public AABB set(AABB box) {
        extent.set(box.extent);
        return set((Vec3D) box);
    }

    /**
     * Updates the position of the box in space and calls
     * {@link #updateBounds()} immediately
     * 
     * @see toxi.geom.Vec3D#set(float, float, float)
     */
    public Vec3D set(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        updateBounds();
        return this;
    }

    /**
     * Updates the position of the box in space and calls
     * {@link #updateBounds()} immediately
     * 
     * @see toxi.geom.Vec3D#set(toxi.geom.Vec3D)
     */
    public AABB set(Vec3D v) {
        x = v.x;
        y = v.y;
        z = v.z;
        updateBounds();
        return this;
    }

    /**
     * Updates the size of the box and calls {@link #updateBounds()} immediately
     * 
     * @param extent
     *            new box size
     * @return itself, for method chaining
     */
    public AABB setExtent(Vec3D extent) {
        this.extent = new Vec3D(extent);
        return updateBounds();
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.geom.Vec3D#toString()
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("<aabb> pos: ").append(super.toString()).append(" ext: ")
                .append(extent);
        return sb.toString();
    }

    /**
     * Updates the min/max corner points of the box. MUST be called after moving
     * the box in space by manipulating the public x,y,z coordinates directly.
     * 
     * @return itself
     */
    public final AABB updateBounds() {
        // this is check is necessary for the constructor
        if (extent != null) {
            this.min = this.sub(extent);
            this.max = this.add(extent);
        }
        return this;
    }
}