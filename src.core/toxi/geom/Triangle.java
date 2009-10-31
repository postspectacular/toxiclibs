/*
 * Copyright (c) 2006-2008 Karsten Schmidt
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

@XmlAccessorType(XmlAccessType.FIELD)
public class Triangle {

	public static boolean isClockwiseInXY(Vec3D a, Vec3D b, Vec3D c) {
		float determ = (b.x - a.x) * (c.y - a.y) - (c.x - a.x) * (b.y - a.y);
		return (determ < 0.0);
	}

	public static boolean isClockwiseInXZ(Vec3D a, Vec3D b, Vec3D c) {
		float determ = (b.x - a.x) * (c.z - a.z) - (c.x - a.x) * (b.z - a.z);
		return (determ < 0.0);
	}

	public static boolean isClockwiseInYZ(Vec3D a, Vec3D b, Vec3D c) {
		float determ = (b.y - a.y) * (c.z - a.z) - (c.y - a.y) * (b.z - a.z);
		return (determ < 0.0);
	}

	@XmlElement(required = true)
	public Vec3D a, b, c;

	@XmlElement(required = true)
	public Vec3D normal;

	@XmlTransient
	public Vec3D centroid;

	public Triangle() {
	}

	public Triangle(Vec3D a, Vec3D b, Vec3D c) {
		this.a = a;
		this.b = b;
		this.c = c;
	}

	/**
	 * @see #closestPointOnSurface(Vec3D)
	 * @deprecated
	 */
	@Deprecated
	public Vec3D closedPoint(Vec3D p) {
		return closestPointOnSurface(p);
	}

	/**
	 * Computes the the point closest to the current vector on the surface of
	 * triangle abc.
	 * 
	 * From Real-Time Collision Detection by Christer Ericson, published by
	 * Morgan Kaufmann Publishers, Copyright 2005 Elsevier Inc
	 * 
	 * @return closest point on triangle (result may also be one of a, b or c)
	 */
	public Vec3D closestPointOnSurface(Vec3D p) {
		Vec3D ab = b.sub(a);
		Vec3D ac = c.sub(a);
		Vec3D bc = c.sub(b);

		Vec3D pa = p.sub(a);
		Vec3D pb = p.sub(b);
		Vec3D pc = p.sub(c);

		Vec3D ap = a.sub(p);
		Vec3D bp = b.sub(p);
		Vec3D cp = c.sub(p);

		// Compute parametric position s for projection P' of P on AB,
		// P' = A + s*AB, s = snom/(snom+sdenom)
		float snom = pa.dot(ab);

		// Compute parametric position t for projection P' of P on AC,
		// P' = A + t*AC, s = tnom/(tnom+tdenom)
		float tnom = pa.dot(ac);

		if (snom <= 0.0f && tnom <= 0.0f) {
			return a; // Vertex region early out
		}

		float sdenom = pb.dot(a.sub(b));
		float tdenom = pc.dot(a.sub(c));

		// Compute parametric position u for projection P' of P on BC,
		// P' = B + u*BC, u = unom/(unom+udenom)
		float unom = pb.dot(bc);
		float udenom = pc.dot(b.sub(c));

		if (sdenom <= 0.0f && unom <= 0.0f) {
			return b; // Vertex region early out
		}
		if (tdenom <= 0.0f && udenom <= 0.0f) {
			return c; // Vertex region early out
		}

		// P is outside (or on) AB if the triple scalar product [N PA PB] <= 0
		Vec3D n = ab.cross(ac);
		float vc = n.dot(ap.crossSelf(bp));

		// If P outside AB and within feature region of AB,
		// return projection of P onto AB
		if (vc <= 0.0f && snom >= 0.0f && sdenom >= 0.0f) {
			// return a + snom / (snom + sdenom) * ab;
			return a.add(ab.scaleSelf(snom / (snom + sdenom)));
		}

		// P is outside (or on) BC if the triple scalar product [N PB PC] <= 0
		float va = n.dot(bp.crossSelf(cp));
		// If P outside BC and within feature region of BC,
		// return projection of P onto BC
		if (va <= 0.0f && unom >= 0.0f && udenom >= 0.0f) {
			// return b + unom / (unom + udenom) * bc;
			return b.add(bc.scaleSelf(unom / (unom + udenom)));
		}

		// P is outside (or on) CA if the triple scalar product [N PC PA] <= 0
		float vb = n.dot(cp.crossSelf(ap));
		// If P outside CA and within feature region of CA,
		// return projection of P onto CA
		if (vb <= 0.0f && tnom >= 0.0f && tdenom >= 0.0f) {
			// return a + tnom / (tnom + tdenom) * ac;
			return a.add(ac.scaleSelf(tnom / (tnom + tdenom)));
		}

		// P must project inside face region. Compute Q using barycentric
		// coordinates
		float u = va / (va + vb + vc);
		float v = vb / (va + vb + vc);
		float w = 1.0f - u - v; // = vc / (va + vb + vc)
		// return u * a + v * b + w * c;
		return a.scale(u).addSelf(b.scale(v)).addSelf(c.scale(w));
	}

	public Vec3D computeCentroid() {
		centroid = a.add(b).addSelf(c).scaleSelf(1f / 3);
		return centroid;
	}

	public Vec3D computeNormal() {
		normal = a.sub(c).crossSelf(a.sub(b)).normalize();
		return normal;
	}

	/**
	 * Checks if point vector is inside the triangle created by the points a, b
	 * and c. These points will create a plane and the point checked will have
	 * to be on this plane in the region between a,b,c.
	 * 
	 * Note: The triangle must be defined in clockwise order a,b,c
	 * 
	 * @return true, if point is in triangle.
	 */
	public boolean containsPoint(Vec3D p) {
		Vec3D v1 = p.sub(a).normalize();
		Vec3D v2 = p.sub(b).normalize();
		Vec3D v3 = p.sub(c).normalize();

		double total_angles = Math.acos(v1.dot(v2));
		total_angles += Math.acos(v2.dot(v3));
		total_angles += Math.acos(v3.dot(v1));

		return (MathUtils.abs((float) total_angles - MathUtils.TWO_PI) <= 0.005f);
	}

	/**
	 * Finds and returns the closest point on any of the triangle edges to the
	 * point given.
	 * 
	 * @param p
	 *            point to check
	 * @return closest point
	 */

	public Vec3D getClosestVertexTo(Vec3D p) {
		Vec3D Rab = p.closestPointOnLine(a, b);
		Vec3D Rbc = p.closestPointOnLine(b, c);
		Vec3D Rca = p.closestPointOnLine(c, a);

		float dAB = p.sub(Rab).magSquared();
		float dBC = p.sub(Rbc).magSquared();
		float dCA = p.sub(Rca).magSquared();

		float min = dAB;
		Vec3D result = Rab;

		if (dBC < min) {
			min = dBC;
			result = Rbc;
		}
		if (dCA < min) {
			result = Rca;
		}

		return result;
	}

	public boolean isClockwiseInXY() {
		return Triangle.isClockwiseInXY(a, b, c);
	}

	public boolean isClockwiseInXZ() {
		return Triangle.isClockwiseInXY(a, b, c);
	}

	public boolean isClockwiseInYZ() {
		return Triangle.isClockwiseInXY(a, b, c);
	}

	public void set(Vec3D a2, Vec3D b2, Vec3D c2) {
		a = a2;
		b = b2;
		c = c2;
	}

	public String toString() {
		return "Triangle: " + a + "," + b + "," + c;
	}
}
