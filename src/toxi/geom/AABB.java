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
 * Axis-aligned bounding box
 */
public class AABB extends Vec3D {

	Vec3D extend;

	public AABB(Vec3D pos, Vec3D extend) {
		super(pos);
		this.extend = extend;
	}

	public AABB(AABB box) {
		this(box,box.extend);
	}
	
	public float minX() {
		return x - extend.x;
	}

	public float maxX() {
		return x + extend.x;
	}

	public float minY() {
		return y - extend.y;
	}

	public float maxY() {
		return y + extend.y;
	}

	public float minZ() {
		return z - extend.z;
	}

	public float maxZ() {
		return z + extend.z;
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
		if (c.x < minX()) {
			s = c.x - minX();
			d += s * s;
		} else if (c.x > maxX()) {
			s = c.x - maxX();
			d += s * s;
		}

		if (c.y < minY()) {
			s = c.y - minY();
			d += s * s;
		} else if (c.y > maxY()) {
			s = c.y - maxY();
			d += s * s;
		}

		if (c.z < minZ()) {
			s = c.z - minZ();
			d += s * s;
		} else if (c.z > maxZ()) {
			s = c.z - maxZ();
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
	
	public String toString() {
		StringBuffer sb=new StringBuffer();
		sb.append("<aabb> pos: ").append(super.toString()).append(" ext: ").append(extend);
		return sb.toString();
	}
}
