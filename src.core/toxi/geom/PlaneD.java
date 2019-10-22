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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import toxi.geom.mesh.MeshD3D;
import toxi.geom.mesh.TriangleMeshD;
import toxi.math.MathUtils;

/**
 * Class to describe and work with infinite generic 3D planes. Useful for
 * intersection problems and classifying points.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class PlaneD extends VecD3D implements ShapeD3D {

    /**
     * Classifier constant for {@link PlaneD#classifyPoint(ReadonlyVecD3D, double)}
     */
    public enum Classifier {
        FRONT,
        BACK,
        ON_PLANE;
    }

    public static final PlaneD XY = new PlaneD(new VecD3D(), VecD3D.Z_AXIS);
    public static final PlaneD XZ = new PlaneD(new VecD3D(), VecD3D.Y_AXIS);
    public static final PlaneD YZ = new PlaneD(new VecD3D(), VecD3D.X_AXIS);

    @XmlElement(required = true)
    public VecD3D normal;

    public PlaneD() {
        super();
        normal = VecD3D.Y_AXIS.copy();
    }

    public PlaneD(ReadonlyVecD3D origin, ReadonlyVecD3D norm) {
        super(origin);
        normal = norm.getNormalized();
    }

    public PlaneD(TriangleD3D t) {
        this(t.computeCentroid(), t.computeNormal());
    }

    /**
     * Classifies the relative position of the given point to the plane using
     * the given tolerance.
     * 
     * @return One of the 3 classification types: FRONT, BACK, ON_PLANE
     */
    public Classifier classifyPoint(ReadonlyVecD3D p, double tolerance) {
        double d = this.sub(p).normalize().dot(normal);
        if (d < -tolerance) {
            return Classifier.FRONT;
        } else if (d > tolerance) {
            return Classifier.BACK;
        }
        return Classifier.ON_PLANE;
    }

    public boolean containsPoint(ReadonlyVecD3D p) {
        return classifyPoint(p, MathUtils.EPS) == Classifier.ON_PLANE;
    }

    public double getDCoeff() {
        return this.dot(normal);
    }

    /**
     * Calculates distance from the plane to point P.
     * 
     * @param p
     * @return distance
     */
    public double getDistanceToPoint(VecD3D p) {
        double sn = -normal.dot(p.sub(this));
        double sd = normal.magSquared();
        VecD3D isec = p.add(normal.scale(sn / sd));
        return isec.distanceTo(p);
    }

    /**
     * Calculates the intersection point between plane and ray (line).
     * 
     * @param r
     * @return intersection point or null if ray doesn't intersect plane
     */
    public ReadonlyVecD3D getIntersectionWithRay(RayD3D r) {
        double denom = normal.dot(r.getDirection());
        if (denom > MathUtils.EPS) {
            double u = normal.dot(this.sub(r)) / denom;
            return r.getPointAtDistance(u);
        } else {
            return null;
        }
    }

    public VecD3D getProjectedPoint(VecD3D p) {
        VecD3D dir;
        if (normal.dot(sub(p)) < 0) {
            dir = normal.getInverted();
        } else {
            dir = normal;
        }
        VecD3D proj = new RayD3D(p, dir)
                .getPointAtDistance(getDistanceToPoint(p));
        return proj;
    }

    /**
     * Calculates the distance of the vector to the given plane in the specified
     * direction. A plane is specified by a 3D point and a normal vector
     * perpendicular to the plane. Normalized directional vectors expected (for
     * rayDir and planeNormal).
     * 
     * @param ray
     *            intersection ray
     * @return distance to plane in world units, -1 if no intersection.
     */
    public double intersectRayDistance(RayD3D ray) {
        double d = -normal.dot(this);
        double numer = normal.dot(ray) + d;
        double denom = normal.dot(ray.dir);

        // normal is orthogonal to vector, cant intersect
        if (MathUtils.abs(denom) < MathUtils.EPS) {
            return -1;
        }

        return -(numer / denom);
    }

    /**
     * Computes the intersection ray between this plane and the given one. If
     * the planes are parallel or coincident the method returns null. If the
     * planes are intersecting, the returned {@link RayD3D} will start at a point
     * lying on both planes and point along the infinite intersection line
     * between them.
     * 
     * Code ported from:
     * http://forums.create.msdn.com/forums/p/39074/234178.aspx#234178
     * 
     * @param plane
     *            intersection partner
     * @return intersection ray or null
     */
    public RayD3D intersectsPlaneD(PlaneD plane) {
        double d = getDCoeff();
        double d2 = plane.getDCoeff();

        if (normal.equalsWithTolerance(plane.normal, 0.0001f) || d == d2) {
            return null;
        }

        double offDiagonal = normal.dot(plane.normal);
        double det = 1.0 / (1 - offDiagonal * offDiagonal);
        double a = (d - d2 * offDiagonal) * det;
        double b = (d2 - d * offDiagonal) * det;
        VecD3D anchor = normal.scale(a).addSelf(
                plane.normal.scale( b));
        VecD3D dir = normal.cross(plane.normal);

        return new RayD3D(anchor, dir);
    }

    /**
     * Creates a TriangleMesh representation of the plane as a finite, squared
     * quad of the requested size, centred around the current plane point.
     * 
     * @param size
     *            desired edge length
     * @return mesh
     */
    public MeshD3D toMesh(double size) {
        return toMesh(null, size);
    }

    public MeshD3D toMesh(MeshD3D mesh, double size) {
        if (mesh == null) {
            mesh = new TriangleMeshD("plane", 4, 2);
        }
        ReadonlyVecD3D p = equalsWithTolerance(VecD3D.ZERO, 0.01f) ? add(0.01f,
                0.01f, 0.01f) : this;
        size *= 0.5f;
        VecD3D n = p.cross(normal).normalizeTo(size);
        VecD3D m = n.cross(normal).normalizeTo(size);
        VecD3D a = this.add(n).addSelf(m);
        VecD3D b = this.add(n).subSelf(m);
        VecD3D c = this.sub(n).subSelf(m);
        VecD3D d = this.sub(n).addSelf(m);
        mesh.addFaceD(a, d, b, null, null, null, null);
        mesh.addFaceD(b, d, c, null, null, null, null);
        return mesh;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("origin: ").append(super.toString()).append(" norm: ")
                .append(normal.toString());
        return sb.toString();
    }
}
