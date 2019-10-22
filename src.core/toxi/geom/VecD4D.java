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

import javax.xml.bind.annotation.XmlAttribute;

import toxi.math.InterpolateStrategy;
import toxi.math.MathUtils;
import toxi.math.ScaleMap;

public class VecD4D implements ReadonlyVecD4D, Cloneable {

    /** X coordinate */
    @XmlAttribute(required = true)
    public double x;

    /** Y coordinate */
    @XmlAttribute(required = true)
    public double y;

    /** Z coordinate */
    @XmlAttribute(required = true)
    public double z;

    /** W coordinate (weight) */
    @XmlAttribute(required = true)
    public double w;

    public VecD4D() {
    }

    public VecD4D(double x, double y, double z, double w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }
    public VecD4D(Vec4D v) {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
        this.w = v.w;
    }

    public VecD4D(ReadonlyVecD3D v, double w) {
        this.x = v.x();
        this.y = v.y();
        this.z = v.z();
        this.w = w;
    }

    public VecD4D(ReadonlyVecD4D v) {
        set(v);
    }

    public VecD4D abs() {
        x = MathUtils.abs(x);
        y = MathUtils.abs(y);
        z = MathUtils.abs(z);
        w = MathUtils.abs(w);
        return this;
    }

    public final VecD4D add(ReadonlyVecD4D v) {
        return new VecD4D(x + v.x(), y + v.y(), z + v.z(), w + v.w());
    }

    public final VecD4D addScaled(ReadonlyVecD4D t, double s) {
        return new VecD4D(s * t.x(), s * t.y(), s * t.z(), s * t.w());
    }

    public final VecD4D addScaledSelf(ReadonlyVecD4D t, double s) {
        x += s * t.x();
        y += s * t.y();
        z += s * t.z();
        w += s * t.w();
        return this;
    }

    public final VecD4D addSelf(ReadonlyVecD4D v) {
        this.x += v.x();
        this.y += v.y();
        this.z += v.z();
        this.w += v.w();
        return this;
    }

    public final VecD4D addXYZ(double xx, double yy, double zz) {
        return new VecD4D(x + xx, y + yy, z + zz, w);
    }

    public final VecD4D addXYZ(ReadonlyVecD3D v) {
        return new VecD4D(x + v.x(), y + v.y(), z + v.z(), w);
    }

    public final VecD4D addXYZSelf(double xx, double yy, double zz) {
        x += xx;
        y += yy;
        z += zz;
        return this;
    }

    public final VecD4D addXYZSelf(ReadonlyVecD3D v) {
        this.x += v.x();
        this.y += v.y();
        this.z += v.z();
        return this;
    }

    /**
     * Returns the (4-space) angle in radians between this vector and the vector
     * parameter; the return value is constrained to the range [0,PI].
     * 
     * @param v
     *            the other vector
     * @return the angle in radians in the range [0,PI]
     */
    public final double angleBetween(ReadonlyVecD4D v) {
        double vDot = dot(v) / (magnitude() * v.magnitude());
        if (vDot < -1.0) {
            vDot = -1.0;
        }
        if (vDot > 1.0) {
            vDot = 1.0;
        }
        return Math.acos(vDot);
    }

    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError();
        }
    }

    public int compareTo(ReadonlyVecD4D v) {
        if (x == v.x() && y == v.y() && z == v.z() && w == v.w()) {
            return 0;
        }
        double a = magSquared();
        double b = v.magSquared();
        if (a < b) {
            return -1;
        }
        return +1;
    }

    public final VecD4D copy() {
        return new VecD4D(this);
    }

    public final double distanceTo(ReadonlyVecD4D v) {
        if (v != null) {
            final double dx = x - v.x();
            final double dy = y - v.y();
            final double dz = z - v.z();
            final double dw = w - v.z();
            return Math.sqrt(dx * dx + dy * dy + dz * dz + dw * dw);
        } else {
            return Double.NaN;
        }
    }

    public final double distanceToSquared(ReadonlyVecD4D v) {
        if (v != null) {
            final double dx = x - v.x();
            final double dy = y - v.y();
            final double dz = z - v.z();
            final double dw = w - v.z();
            return dx * dx + dy * dy + dz * dz + dw * dw;
        } else {
            return Double.NaN;
        }
    }

    public final double dot(ReadonlyVecD4D v) {
        return (x * v.x() + y * v.y() + z * v.z() + w * v.w());
    }

    /**
     * Returns true if the Object v is of type ReadonlyVecD4D and all of the data
     * members of v are equal to the corresponding data members in this vector.
     * 
     * @param v
     *            the Object with which the comparison is made
     * @return true or false
     */
    public boolean equals(Object v) {
        try {
            ReadonlyVecD4D vv = (ReadonlyVecD4D) v;
            return (x == vv.x() && y == vv.y() && z == vv.z() && w == vv.w());
        } catch (NullPointerException e) {
            return false;
        } catch (ClassCastException e) {
            return false;
        }
    }

    /**
     * Returns true if the Object v is of type VecD4D and all of the data members
     * of v are equal to the corresponding data members in this vector.
     * 
     * @param v
     *            the vector with which the comparison is made
     * @return true or false
     */
    public boolean equals(ReadonlyVecD4D v) {
        try {
            return (x == v.x() && y == v.y() && z == v.z() && w == v.w());
        } catch (NullPointerException e) {
            return false;
        }
    }

    public boolean equalsWithTolerance(ReadonlyVecD4D v, double tolerance) {
        try {
            double diff = x - v.x();
            if (Double.isNaN(diff)) {
                return false;
            }
            if ((diff < 0 ? -diff : diff) > tolerance) {
                return false;
            }
            diff = y - v.y();
            if (Double.isNaN(diff)) {
                return false;
            }
            if ((diff < 0 ? -diff : diff) > tolerance) {
                return false;
            }
            diff = z - v.z();
            if (Double.isNaN(diff)) {
                return false;
            }
            if ((diff < 0 ? -diff : diff) > tolerance) {
                return false;
            }
            diff = w - v.w();
            if (Double.isNaN(diff)) {
                return false;
            }
            if ((diff < 0 ? -diff : diff) > tolerance) {
                return false;
            }
            return true;
        } catch (NullPointerException e) {
            return false;
        }
    }

    public VecD4D getAbs() {
        return copy().abs();
    }

    public final VecD4D getInvertedXYZ() {
        return copy().invertXYZ();
    }

    public VecD4D getMapped(ScaleMap map) {
        return new VecD4D(map.getClippedValueFor(x),
                          map.getClippedValueFor(y),
                          map.getClippedValueFor(z),
                          map.getClippedValueFor(w));
    }

    public VecD4D getMappedXYZ(ScaleMap map) {
        return new VecD4D(map.getClippedValueFor(x),
                          map.getClippedValueFor(y),
                          map.getClippedValueFor(z), w);
    }

    public VecD4D getNormalized() {
        return copy().normalize();
    }

    public VecD4D getNormalizedTo(double len) {
        return copy().normalizeTo(len);
    }

    public VecD4D getRotatedAroundAxis(ReadonlyVecD3D axis, double theta) {
        return copy().rotateAroundAxis(axis, theta);
    }

    public VecD4D getRotatedX(double theta) {
        return copy().rotateX(theta);
    }

    public VecD4D getRotatedY(double theta) {
        return copy().rotateY(theta);
    }

    public VecD4D getRotatedZ(double theta) {
        return copy().rotateZ(theta);
    }

    public VecD4D getRoundedXYZTo(double prec) {
        return copy().roundXYZTo(prec);
    }

    public VecD4D getUnweighted() {
        return copy().unweight();
    }

    public VecD4D getWeighted() {
        return copy().weight();
    }

    /**
     * Returns a hash code value based on the data values in this object. Two
     * different VecD4D objects with identical data values (i.e., VecD4D.equals
     * returns true) will return the same hash code value. Two objects with
     * different data members may return the same hash value, although this is
     * not likely.
     * 
     * @return the integer hash code value
     */
    public int hashCode() {
        return ((Double)x).hashCode()+((Double)y).hashCode()+((Double)z).hashCode()+((Double)w).hashCode();
    }

    public final VecD4D interpolateTo(ReadonlyVecD4D v, double t) {
        return copy().interpolateToSelf(v, t);
    }

    public final VecD4D interpolateTo(ReadonlyVecD4D v, double f,
            InterpolateStrategy s) {
        return new VecD4D(s.interpolate(x, v.x(), f),
                s.interpolate(y, v.y(), f), s.interpolate(z, v.z(), f),
                s.interpolate(w, v.w(), f));
    }

    public final VecD4D interpolateToSelf(ReadonlyVecD4D v, double t) {
        this.x += (v.x() - x) * t;
        this.y += (v.y() - y) * t;
        this.z += (v.z() - z) * t;
        this.w += (v.w() - w) * t;
        return this;
    }

    public final VecD4D interpolateToSelf(ReadonlyVecD4D v, double f,
            InterpolateStrategy s) {
        x = s.interpolate(x, v.x(), f);
        y = s.interpolate(y, v.y(), f);
        z = s.interpolate(z, v.z(), f);
        w = s.interpolate(w, v.w(), f);
        return this;
    }

    public final VecD4D invertXYZ() {
        this.x *= -1;
        this.y *= -1;
        this.z *= -1;
        return this;
    }

    public final boolean isZeroVector() {
        return MathUtils.abs(x) < MathUtils.EPS
                && MathUtils.abs(y) < MathUtils.EPS
                && MathUtils.abs(z) < MathUtils.EPS
                && MathUtils.abs(w) < MathUtils.EPS;
    }

    public final double magnitude() {
        return Math.sqrt(x * x + y * y + z * z + w * w);
    }

    public final double magSquared() {
        return x * x + y * y + z * z + w * w;
    }

    /**
     * Normalizes the vector so that its magnitude = 1.
     * 
     * @return itself
     */
    public final VecD4D normalize() {
        double mag =  Math.sqrt(x * x + y * y + z * z);
        if (mag > 0) {
            mag = 1f / mag;
            x *= mag;
            y *= mag;
            z *= mag;
            w *= mag;
        }
        return this;
    }

    /**
     * Normalizes the vector to the given length.
     * 
     * @param len
     *            desired length
     * @return itself
     */
    public final VecD4D normalizeTo(double len) {
        double mag =  Math.sqrt(x * x + y * y + z * z);
        if (mag > 0) {
            mag = len / mag;
            x *= mag;
            y *= mag;
            z *= mag;
            w *= mag;
        }
        return this;
    }

    /**
     * Rotates the vector around the giving axis.
     * 
     * @param axis
     *            rotation axis vector
     * @param theta
     *            rotation angle (in radians)
     * 
     * @return itself
     */
    public final VecD4D rotateAroundAxis(ReadonlyVecD3D axis, double theta) {
        final double ax = axis.x();
        final double ay = axis.y();
        final double az = axis.z();
        final double ux = ax * x;
        final double uy = ax * y;
        final double uz = ax * z;
        final double vx = ay * x;
        final double vy = ay * y;
        final double vz = ay * z;
        final double wx = az * x;
        final double wy = az * y;
        final double wz = az * z;
        final double si = Math.sin(theta);
        final double co = Math.cos(theta);
        double xx =  (ax * (ux + vy + wz)
                + (x * (ay * ay + az * az) - ax * (vy + wz)) * co + (-wy + vz)
                * si);
        double yy =  (ay * (ux + vy + wz)
                + (y * (ax * ax + az * az) - ay * (ux + wz)) * co + (wx - uz)
                * si);
        double zz =  (az * (ux + vy + wz)
                + (z * (ax * ax + ay * ay) - az * (ux + vy)) * co + (-vx + uy)
                * si);
        x = xx;
        y = yy;
        z = zz;
        return this;
    }

    /**
     * Rotates the vector by the given angle around the X axis.
     * 
     * @param theta
     *            the theta
     * 
     * @return itself
     */
    public final VecD4D rotateX(double theta) {
        final double co =  Math.cos(theta);
        final double si =  Math.sin(theta);
        final double zz = co * z - si * y;
        y = si * z + co * y;
        z = zz;
        return this;
    }

    /**
     * Rotates the vector by the given angle around the Y axis.
     * 
     * @param theta
     *            the theta
     * 
     * @return itself
     */
    public final VecD4D rotateY(double theta) {
        final double co = Math.cos(theta);
        final double si =  Math.sin(theta);
        final double xx = co * x - si * z;
        z = si * x + co * z;
        x = xx;
        return this;
    }

    /**
     * Rotates the vector by the given angle around the Z axis.
     * 
     * @param theta
     *            the theta
     * 
     * @return itself
     */
    public final VecD4D rotateZ(double theta) {
        final double co =  Math.cos(theta);
        final double si =  Math.sin(theta);
        final double xx = co * x - si * y;
        y = si * x + co * y;
        x = xx;
        return this;
    }

    public VecD4D roundXYZTo(double prec) {
        x = MathUtils.roundTo(x, prec);
        y = MathUtils.roundTo(y, prec);
        z = MathUtils.roundTo(z, prec);
        return this;
    }

    public final VecD4D scale(double s) {
        return new VecD4D(x * s, y * s, z * s, w * s);
    }

    public final VecD4D scale(double xx, double yy, double zz, double ww) {
        return new VecD4D(x * xx, y * yy, z * zz, w * ww);
    }

    public VecD4D scale(ReadonlyVecD4D s) {
        return copy().scaleSelf(s);
    }

    public final VecD4D scaleSelf(double s) {
        this.x *= s;
        this.y *= s;
        this.z *= s;
        this.w *= s;
        return this;
    }

    public VecD4D scaleSelf(ReadonlyVecD4D s) {
        this.x *= s.x();
        this.y *= s.y();
        this.z *= s.z();
        this.w *= s.w();
        return this;
    }

    public final VecD4D scaleXYZSelf(double s) {
        this.x *= s;
        this.y *= s;
        this.z *= s;
        return this;
    }

    public final VecD4D scaleXYZSelf(double xscale, double yscale, double zscale) {
        this.x *= xscale;
        this.y *= yscale;
        this.z *= zscale;
        return this;
    }

    /**
     * Overrides coordinates with the given values.
     * 
     * @param x
     *            the x
     * @param y
     *            the y
     * @param z
     *            the z
     * @param w
     *            the w
     * 
     * @return itself
     */
    public VecD4D set(double x, double y, double z, double w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
        return this;
    }

    public final VecD4D set(ReadonlyVecD4D v) {
        this.x = v.x();
        this.y = v.y();
        this.z = v.z();
        this.w = v.w();
        return this;
    }

    public VecD4D setW(double w) {
        this.w = w;
        return this;
    }

    public VecD4D setX(double x) {
        this.x = x;
        return this;
    }

    public final VecD4D setXYZ(ReadonlyVecD3D v) {
        this.x = v.x();
        this.y = v.y();
        this.z = v.z();
        return this;
    }

    public VecD4D setY(double y) {
        this.y = y;
        return this;
    }

    public VecD4D setZ(double z) {
        this.z = z;
        return this;
    }

    public final VecD4D sub(ReadonlyVecD4D v) {
        return new VecD4D(x - v.x(), y - v.y(), z - v.z(), w - v.w());
    }

    public final VecD4D subSelf(ReadonlyVecD4D v) {
        this.x -= v.x();
        this.y -= v.y();
        this.z -= v.z();
        this.w -= v.w();
        return this;
    }

    public final VecD4D subXYZ(double xx, double yy, double zz) {
        return new VecD4D(x - xx, y - yy, z - zz, w);
    }

    public final VecD4D subXYZ(ReadonlyVecD3D v) {
        return new VecD4D(x - v.x(), y - v.y(), z - v.z(), w);
    }

    public final VecD4D subXYZSelf(double xx, double yy, double zz) {
        this.x -= xx;
        this.y -= yy;
        this.z -= zz;
        return this;
    }

    public final VecD4D subXYZSelf(ReadonlyVecD3D v) {
        this.x -= v.x();
        this.y -= v.y();
        this.z -= v.z();
        return this;
    }

    public final VecD3D to3D() {
        return new VecD3D(x, y, z);
    }

    public double[] toArray() {
        return new double[] {
                x, y, z, w
        };
    }

    public String toString() {
        return "[x=" + x + ", y=" + y + ", z=" + z + ", w=" + w + "]";
    }

    public final VecD4D translate(double xx, double yy, double zz) {
        this.x += w * xx;
        this.y += w * yy;
        this.z += w * zz;
        return this;
    }

    /**
     * Divides each coordinate bythe weight with and sets the coordinate to the
     * newly calculatd ones.
     */
    public final VecD4D unweight() {
        double iw = MathUtils.abs(w) > MathUtils.EPS ? 1f / w : 0;
        x *= iw;
        y *= iw;
        z *= iw;
        return this;
    }

    public final VecD3D unweightInto(VecD3D out) {
        double iw = MathUtils.abs(w) > MathUtils.EPS ? 1f / w : 0;
        out.x = x * iw;
        out.y = y * iw;
        out.z = z * iw;
        return out;
    }

    /**
     * @return the w
     */
    public final double w() {
        return w;
    }

    /**
     * Multiplies the weight with each coordinate and sets the coordinate to the
     * newly calculatd ones.
     * 
     * @return itself
     */
    public final VecD4D weight() {
        x *= w;
        y *= w;
        z *= w;
        return this;
    }

    public final VecD3D weightInto(VecD3D out) {
        out.x = x * w;
        out.y = y * w;
        out.z = z * w;
        return out;
    }

    /**
     * @return the x
     */
    public final double x() {
        return x;
    }

    /**
     * @return the y
     */
    public final double y() {
        return y;
    }

    /**
     * @return the z
     */
    public final double z() {
        return z;
    }
}
