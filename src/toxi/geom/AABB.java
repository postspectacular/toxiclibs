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

import toxi.math.FastMath;

/**
 * Axis-aligned bounding box with basic intersection features for Ray, AABB and
 * Sphere classes.
 */
public class AABB extends Vec3D {

	private Vec3D extend;

	private Vec3D min, max;

	/**
	 * 
	 * @param min
	 * @param max
	 * @return
	 */
	public static final AABB fromMinMax(Vec3D min, Vec3D max) {
		Vec3D a=Vec3D.min(min,max);
		Vec3D b=Vec3D.max(min,max);
		return new AABB(a.interpolateTo(b, 0.5f), b.sub(a).scaleSelf(0.5f));
	}

	public AABB(Vec3D pos, Vec3D extend) {
		super(pos);
		setExtend(extend);
	}

	public AABB(AABB box) {
		this(box, box.extend);
	}

	public void setExtend(Vec3D extend) {
		this.extend = new Vec3D(extend);
		this.min = this.sub(extend);
		this.max = this.add(extend);
	}

	public float minX() {
		return min.x;
	}

	public float maxX() {
		return max.x;
	}

	public float minY() {
		return min.y;
	}

	public float maxY() {
		return max.y;
	}

	public float minZ() {
		return min.z;
	}

	public float maxZ() {
		return max.z;
	}

	public final Vec3D getMin() {
		return new Vec3D(min);
	}

	public final Vec3D getMax() {
		return new Vec3D(max);
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
			d += s * s;
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

	public boolean intersectsSphere(Sphere s) {
		return intersectsSphere(s, s.radius);
	}

	/**
	 * @param b
	 * @return
	 */
	public boolean intersectsBox(AABB b) {
		Vec3D t = b.sub(this);
		return FastMath.abs(t.x) <= (extend.x + b.extend.x)
				&& FastMath.abs(t.y) <= (extend.y + b.extend.y)
				&& FastMath.abs(t.z) <= (extend.z + b.extend.z);
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
	 * @param minDir
	 * @param maxDir
	 * @return intersection point on the bounding box (only the first is
	 *         returned) or null if no intersection
	 */
	public Vec3D intersectsRay(Ray3D ray, float minDir, float maxDir) {
		Vec3D invDir = new Vec3D(1 / ray.dir.x, 1 / ray.dir.y, 1 / ray.dir.z);
		boolean signDirX = invDir.x < 0;
		boolean signDirY = invDir.y < 0;
		boolean signDirZ = invDir.z < 0;
		Vec3D min = getMin();
		Vec3D max = getMax();
		Vec3D bbox = signDirX ? max : min;
		float tmin = (bbox.x - ray.x) * invDir.x;
		bbox = signDirX ? min : max;
		float tmax = (bbox.x - ray.x) * invDir.x;
		bbox = signDirY ? max : min;
		float tymin = (bbox.y - ray.y) * invDir.y;
		bbox = signDirY ? min : max;
		float tymax = (bbox.y - ray.y) * invDir.y;

		if ((tmin > tymax) || (tymin > tmax))
			return null;
		if (tymin > tmin)
			tmin = tymin;
		if (tymax < tmax)
			tmax = tymax;

		bbox = signDirZ ? max : min;
		float tzmin = (bbox.z - ray.z) * invDir.z;
		bbox = signDirZ ? min : max;
		float tzmax = (bbox.z - ray.z) * invDir.z;

		if ((tmin > tzmax) || (tzmin > tmax))
			return null;
		if (tzmin > tmin)
			tmin = tzmin;
		if (tzmax < tmax)
			tmax = tzmax;
		if ((tmin < maxDir) && (tmax > minDir)) {
			return ray.getPointAtDistance(tmin);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see toxi.geom.Vec3D#toString()
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("<aabb> pos: ").append(super.toString()).append(" ext: ")
				.append(extend);
		return sb.toString();
	}
}
