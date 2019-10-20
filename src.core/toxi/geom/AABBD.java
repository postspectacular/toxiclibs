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

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import toxi.geom.mesh.MeshD3D;
import toxi.geom.mesh.TriangleMeshD;
import toxi.math.MathUtils;

/**
 * Axis-aligned bounding box with basic intersection features for Ray, AABBD and
 * Sphere classes.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class AABBD extends VecD3D implements ShapeD3D {

    /**
     * Creates a new instance from two vectors specifying opposite corners of
     * the box
     * 
     * @param min
     *            first corner point
     * @param max
     *            second corner point
     * @return new AABBD with centre at the half point between the 2 input
     *         vectors
     */
    public static final AABBD fromMinMax(VecD3D min, VecD3D max) {
        VecD3D a = VecD3D.min(min, max);
        VecD3D b = VecD3D.max(min, max);
        return new AABBD(a.interpolateTo(b, 0.5f), b.sub(a).scaleSelf(0.5f));
    }

    /**
     * Factory method, computes & returns the bounding box for the given list of
     * points.
     * 
     * @param points
     * @return bounding rect
     */
    public static final AABBD getBoundingBox(List<? extends VecD3D> points) {
        if (points == null || points.size() == 0) {
            return null;
        }
        VecD3D first = points.get(0);
        VecD3D min = first.copy();
        VecD3D max = first.copy();
        for (VecD3D p : points) {
            min.minSelf(p);
            max.maxSelf(p);
        }
        return fromMinMax(min, max);
    }

    @XmlElement(required = true)
    protected VecD3D extent;

    @XmlTransient
    protected VecD3D min, max;

    public AABBD() {
        super();
        setExtent(new VecD3D());
    }

    /**
     * Creates an independent copy of the passed in boxD
     * 
     * @param box
     */
    public AABBD(AABBD boxd) {
        this(boxd, boxd.getExtent());
    }
    /**
     * Creates an independent copy of the passed in box
     * 
     * @param box
     */
    public AABBD(AABB box) {
    	this(new VecD3D(box.x,box.y,box.z),new VecD3D(box.getExtent()));
    }
    /**
     * Creates a new box of the given size at the world origin.
     * 
     * @param extent
     */
    public AABBD(double extent) {
        this(new VecD3D(), extent);
    }

    /**
     * Creates a new instance from centre point and uniform extent in all
     * directions.
     * 
     * @param pos
     * @param extent
     */
    public AABBD(ReadonlyVecD3D pos, double extent) {
        super(pos);
        setExtent(new VecD3D(extent, extent, extent));
    }

    /**
     * Creates a new instance from centre point and extent
     * 
     * @param pos
     * @param extent
     *            box dimensions (the box will be double the size in each
     *            direction)
     */
    public AABBD(ReadonlyVecD3D pos, ReadonlyVecD3D extent) {
        super(pos);
        setExtent(extent);
    }

    //public boolean containsPoint(ReadonlyVecD3D p) {
    //    return p.isInAABBD(this);
    //}
    public boolean containsPoint(ReadonlyVecD3D p) {
        return p.isInAABBD(this);
    }

    public AABBD copy() {
        return new AABBD(this);
    }

    public SphereD getBoundingSphere() {
        return new SphereD(this, extent.magnitude());
    }

    /**
     * Returns the current box size as new VecD3D instance (updating this vector
     * will NOT update the box size! Use {@link #setExtent(ReadonlyVecD3D)} for
     * those purposes)
     * 
     * @return box size
     */
    public final VecD3D getExtent() {
        return extent.copy();
    }

    public final VecD3D getMax() {
        // return this.add(extent);
        return max.copy();
    }

    public final VecD3D getMin() {
        return min.copy();
    }


    public VecD3D getNormalForPoint(ReadonlyVecD3D p) {
        p = p.sub(this);
        VecD3D pabs = extent.sub(p.getAbs());
        VecD3D psign = p.getSignum();
        VecD3D normal = VecD3D.X_AXIS.scale(psign.x);
        double minDist = pabs.x;
        if (pabs.y < minDist) {
            minDist = pabs.y;
            normal = VecD3D.Y_AXIS.scale(psign.y);
        }
        if (pabs.z < minDist) {
            normal = VecD3D.Z_AXIS.scale(psign.z);
        }
        return normal;
    }

    /**
     * Adjusts the box size and position such that it includes the given point.
     * 
     * @param p
     *            point to include
     * @return itself
     */

    public AABBD growToContainPoint(ReadonlyVecD3D p) {
        min.minSelf(p);
        max.maxSelf(p);
        set(min.interpolateTo(max, 0.5));
        extent.set(max.sub(min).scaleSelf(0.5));
        return this;
    }

    /**
     * Checks if the box intersects the passed in one.
     * 
     * @param box
     *            box to check
     * @return true, if boxes overlap
     */
    //public boolean intersectsBox(AABBD box) {
    //    VecD3D t = box.sub(this);
    //    return MathUtils.abs(t.x) <= (extent.x + box.extent.x)
    //            && MathUtils.abs(t.y) <= (extent.y + box.extent.y)
    //            && MathUtils.abs(t.z) <= (extent.z + box.extent.z);
    //}
    public boolean intersectsBox(AABBD box) {
        VecD3D t = box.sub(this);
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

    public VecD3D intersectsRay(RayD3D ray, double minDist, double maxDist) {
        VecD3D invDir = ray.getDirection().reciprocal();
        boolean signDirX = invDir.x < 0;
        boolean signDirY = invDir.y < 0;
        boolean signDirZ = invDir.z < 0;
        VecD3D bbox = signDirX ? max : min;
        double tmin = (bbox.x - ray.x) * invDir.x;
        bbox = signDirX ? min : max;
        double tmax = (bbox.x - ray.x) * invDir.x;
        bbox = signDirY ? max : min;
        double tymin = (bbox.y - ray.y) * invDir.y;
        bbox = signDirY ? min : max;
        double tymax = (bbox.y - ray.y) * invDir.y;
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
        double tzmin = (bbox.z - ray.z) * invDir.z;
        bbox = signDirZ ? min : max;
        double tzmax = (bbox.z - ray.z) * invDir.z;
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

    public boolean intersectsSphereD(SphereD s) {
        return intersectsSphereD(s, s.radius);
    }

    /**
     * @param c
     *            sphere centre
     * @param r
     *            sphere radius
     * @return true, if AABBD intersects with sphere
     */
    public boolean intersectsSphereD(VecD3D c, double r) {
        double s, d = 0;
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
    public boolean intersectsTriangle(Triangle3D tri) {
    	return intersectsTriangle(new TriangleD3D(tri)); 
   	}
    public boolean intersectsTriangle(TriangleD3D tri) {
        // use separating axis theorem to test overlap between triangle and box
        // need to test for overlap in these directions:
        //
        // 1) the {x,y,z}-directions (actually, since we use the AABBD of the
        // triangle
        // we do not even need to test these)
        // 2) normal of the triangle
        // 3) crossproduct(edge from tri, {x,y,z}-directin)
        // this gives 3x3=9 more tests
        VecD3D v0, v1, v2;
        VecD3D normal, e0, e1, e2, f;

        // move everything so that the boxcenter is in (0,0,0)
        v0 = tri.a.sub(this);
        v1 = tri.b.sub(this);
        v2 = tri.c.sub(this);

        // compute triangle edges
        e0 = v1.sub(v0);
        e1 = v2.sub(v1);
        e2 = v0.sub(v2);

        // test the 9 tests first (this was faster)
        f = e0.getAbs();
        if (testAxis(e0.z, -e0.y, f.z, f.y, v0.y, v0.z, v2.y, v2.z, extent.y,
                extent.z)) {
            return false;
        }
        if (testAxis(-e0.z, e0.x, f.z, f.x, v0.x, v0.z, v2.x, v2.z, extent.x,
                extent.z)) {
            return false;
        }
        if (testAxis(e0.y, -e0.x, f.y, f.x, v1.x, v1.y, v2.x, v2.y, extent.x,
                extent.y)) {
            return false;
        }

        f = e1.getAbs();
        if (testAxis(e1.z, -e1.y, f.z, f.y, v0.y, v0.z, v2.y, v2.z, extent.y,
                extent.z)) {
            return false;
        }
        if (testAxis(-e1.z, e1.x, f.z, f.x, v0.x, v0.z, v2.x, v2.z, extent.x,
                extent.z)) {
            return false;
        }
        if (testAxis(e1.y, -e1.x, f.y, f.x, v0.x, v0.y, v1.x, v1.y, extent.x,
                extent.y)) {
            return false;
        }

        f = e2.getAbs();
        if (testAxis(e2.z, -e2.y, f.z, f.y, v0.y, v0.z, v1.y, v1.z, extent.y,
                extent.z)) {
            return false;
        }
        if (testAxis(-e2.z, e2.x, f.z, f.x, v0.x, v0.z, v1.x, v1.z, extent.x,
                extent.z)) {
            return false;
        }
        if (testAxis(e2.y, -e2.x, f.y, f.x, v1.x, v1.y, v2.x, v2.y, extent.x,
                extent.y)) {
            return false;
        }

        // first test overlap in the {x,y,z}-directions
        // find min, max of the triangle each direction, and test for overlap in
        // that direction -- this is equivalent to testing a minimal AABBD around
        // the triangle against the AABBD

        // test in X-direction
        if (MathUtils.min(v0.x, v1.x, v2.x) > extent.x
                || MathUtils.max(v0.x, v1.x, v2.x) < -extent.x) {
            return false;
        }

        // test in Y-direction
        if (MathUtils.min(v0.y, v1.y, v2.y) > extent.y
                || MathUtils.max(v0.y, v1.y, v2.y) < -extent.y) {
            return false;
        }

        // test in Z-direction
        if (MathUtils.min(v0.z, v1.z, v2.z) > extent.z
                || MathUtils.max(v0.z, v1.z, v2.z) < -extent.z) {
            return false;
        }

        // test if the box intersects the plane of the triangle
        // compute plane equation of triangle: normal*x+d=0
        normal = e0.cross(e1);
        double d = -normal.dot(v0);
        if (!planeBoxOverlap(normal, d, extent)) {
            return false;
        }
        return true;
    }

    private boolean planeBoxOverlap(VecD3D normal, double d, VecD3D maxbox) {
        VecD3D vmin = new VecD3D();
        VecD3D vmax = new VecD3D();

        if (normal.x > 0.0f) {
            vmin.x = -maxbox.x;
            vmax.x = maxbox.x;
        } else {
            vmin.x = maxbox.x;
            vmax.x = -maxbox.x;
        }

        if (normal.y > 0.0f) {
            vmin.y = -maxbox.y;
            vmax.y = maxbox.y;
        } else {
            vmin.y = maxbox.y;
            vmax.y = -maxbox.y;
        }

        if (normal.z > 0.0f) {
            vmin.z = -maxbox.z;
            vmax.z = maxbox.z;
        } else {
            vmin.z = maxbox.z;
            vmax.z = -maxbox.z;
        }
        if (normal.dot(vmin) + d > 0.0f) {
            return false;
        }
        if (normal.dot(vmax) + d >= 0.0f) {
            return true;
        }
        return false;
    }

    public AABBD set(AABBD box) {
        extent.set(box.extent);
        return set((ReadonlyVecD3D) box);
    }

    /**
     * Updates the position of the box in space and calls
     * {@link #updateBounds()} immediately
     * 
     * @see toxi.geom.VecD3D#set(double, double, double)
     */
    public VecD3D set(double x, double y, double z) {
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
     * @see toxi.geom.VecD3D#set(toxi.geom.VecD3D)
     */
    public AABBD set(ReadonlyVecD3D v) {
        x = v.x();
        y = v.y();
        z = v.z();
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
    public AABBD setExtent(ReadonlyVecD3D extent) {
        this.extent = extent.copy();
        return updateBounds();
    }

    private boolean testAxis(double a, double b, double fa, double fb, double va,
            double vb, double wa, double wb, double ea, double eb) {
        double p0 = a * va + b * vb;
        double p2 = a * wa + b * wb;
        double min, max;
        if (p0 < p2) {
            min = p0;
            max = p2;
        } else {
            min = p2;
            max = p0;
        }
        double rad = fa * ea + fb * eb;
        return (min > rad || max < -rad);
    }

    public MeshD3D toMeshD() {
        return toMeshD(null);
    }

    public MeshD3D toMeshD(MeshD3D mesh) {
        if (mesh == null) {
            mesh = new TriangleMeshD("aabb", 8, 12);
        }
        VecD3D a = min;
        VecD3D g = max;
        VecD3D b = new VecD3D(a.x, a.y, g.z);
        VecD3D c = new VecD3D(g.x, a.y, g.z);
        VecD3D d = new VecD3D(g.x, a.y, a.z);
        VecD3D e = new VecD3D(a.x, g.y, a.z);
        VecD3D f = new VecD3D(a.x, g.y, g.z);
        VecD3D h = new VecD3D(g.x, g.y, a.z);
        VecD2D ua = new VecD2D(0, 0);
        VecD2D ub = new VecD2D(1, 0);
        VecD2D uc = new VecD2D(1, 1);
        VecD2D ud = new VecD2D(0, 1);
        // left
        mesh.addFaceD(a, b, f, ud, uc, ub);
        mesh.addFaceD(a, f, e, ud, ub, ua);
        // front
        mesh.addFaceD(b, c, g, ud, uc, ub);
        mesh.addFaceD(b, g, f, ud, ub, ua);
        // right
        mesh.addFaceD(c, d, h, ud, uc, ub);
        mesh.addFaceD(c, h, g, ud, ub, ua);
        // back
        mesh.addFaceD(d, a, e, ud, uc, ub);
        mesh.addFaceD(d, e, h, ud, ub, ua);
        // top
        mesh.addFaceD(e, f, h, ua, ud, ub);
        mesh.addFaceD(f, g, h, ud, uc, ub);
        // bottom
        mesh.addFaceD(a, d, b, ud, uc, ua);
        mesh.addFaceD(b, d, c, ua, uc, ub);
        return mesh;
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.geom.VecD3D#toString()
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(  "<aabbd> pos: ").append(super.toString())
          .append("\n        ext: ").append(extent)
          .append("\n        min: ").append(getMin())
          .append("\n        max: ").append(getMax())
        ;
        return sb.toString();
    }

    public AABBD union(AABBD box) {
        min.minSelf(box.getMin());
        max.maxSelf(box.getMax());
        set(min.interpolateTo(max, 0.5f));
        extent.set(max.sub(min).scaleSelf(0.5f));
        return this;
    }

    /**
     * Updates the min/max corner points of the box. MUST be called after moving
     * the box in space by manipulating the public x,y,z coordinates directly.
     * 
     * @return itself
     */
    public final AABBD updateBounds() {
        // this is check is necessary for the constructor
        if (extent != null) {
            this.min = this.sub(extent);
            this.max = this.add(extent);
        }
        return this;
    }
}