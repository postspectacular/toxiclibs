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

public class Vec4D implements ReadonlyVec4D, Cloneable {

    /** X coordinate */
    @XmlAttribute(required = true)
    public float x;

    /** Y coordinate */
    @XmlAttribute(required = true)
    public float y;

    /** Z coordinate */
    @XmlAttribute(required = true)
    public float z;

    /** W coordinate (weight) */
    @XmlAttribute(required = true)
    public float w;

    public Vec4D() {
    }

    public Vec4D(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public Vec4D(ReadonlyVec3D v, float w) {
        this.x = v.x();
        this.y = v.y();
        this.z = v.z();
        this.w = w;
    }

    public Vec4D(ReadonlyVec4D v) {
        set(v);
    }

    public Vec4D abs() {
        x = MathUtils.abs(x);
        y = MathUtils.abs(y);
        z = MathUtils.abs(z);
        w = MathUtils.abs(w);
        return this;
    }

    public final Vec4D add(ReadonlyVec4D v) {
        return new Vec4D(x + v.x(), y + v.y(), z + v.z(), w + v.w());
    }

    public final Vec4D addScaled(ReadonlyVec4D t, float s) {
        return new Vec4D(s * t.x(), s * t.y(), s * t.z(), s * t.w());
    }

    public final Vec4D addScaledSelf(ReadonlyVec4D t, float s) {
        x += s * t.x();
        y += s * t.y();
        z += s * t.z();
        w += s * t.w();
        return this;
    }

    public final Vec4D addSelf(ReadonlyVec4D v) {
        this.x += v.x();
        this.y += v.y();
        this.z += v.z();
        this.w += v.w();
        return this;
    }

    public final Vec4D addXYZ(float xx, float yy, float zz) {
        return new Vec4D(x + xx, y + yy, z + zz, w);
    }

    public final Vec4D addXYZ(ReadonlyVec3D v) {
        return new Vec4D(x + v.x(), y + v.y(), z + v.z(), w);
    }

    public final Vec4D addXYZSelf(float xx, float yy, float zz) {
        x += xx;
        y += yy;
        z += zz;
        return this;
    }

    public final Vec4D addXYZSelf(ReadonlyVec3D v) {
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
    public final float angleBetween(ReadonlyVec4D v) {
        double vDot = dot(v) / (magnitude() * v.magnitude());
        if (vDot < -1.0) {
            vDot = -1.0;
        }
        if (vDot > 1.0) {
            vDot = 1.0;
        }
        return (float) (Math.acos(vDot));
    }

    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError();
        }
    }

    public int compareTo(ReadonlyVec4D v) {
        if (x == v.x() && y == v.y() && z == v.z() && w == v.w()) {
            return 0;
        }
        float a = magSquared();
        float b = v.magSquared();
        if (a < b) {
            return -1;
        }
        return +1;
    }

    public final Vec4D copy() {
        return new Vec4D(this);
    }

    public final float distanceTo(ReadonlyVec4D v) {
        if (v != null) {
            final float dx = x - v.x();
            final float dy = y - v.y();
            final float dz = z - v.z();
            final float dw = w - v.z();
            return (float) Math.sqrt(dx * dx + dy * dy + dz * dz + dw * dw);
        } else {
            return Float.NaN;
        }
    }

    public final float distanceToSquared(ReadonlyVec4D v) {
        if (v != null) {
            final float dx = x - v.x();
            final float dy = y - v.y();
            final float dz = z - v.z();
            final float dw = w - v.z();
            return dx * dx + dy * dy + dz * dz + dw * dw;
        } else {
            return Float.NaN;
        }
    }

    public final float dot(ReadonlyVec4D v) {
        return (x * v.x() + y * v.y() + z * v.z() + w * v.w());
    }

    /**
     * Returns true if the Object v is of type ReadonlyVec4D and all of the data
     * members of v are equal to the corresponding data members in this vector.
     * 
     * @param v
     *            the Object with which the comparison is made
     * @return true or false
     */
    public boolean equals(Object v) {
        try {
            ReadonlyVec4D vv = (ReadonlyVec4D) v;
            return (x == vv.x() && y == vv.y() && z == vv.z() && w == vv.w());
        } catch (NullPointerException e) {
            return false;
        } catch (ClassCastException e) {
            return false;
        }
    }

    /**
     * Returns true if the Object v is of type Vec4D and all of the data members
     * of v are equal to the corresponding data members in this vector.
     * 
     * @param v
     *            the vector with which the comparison is made
     * @return true or false
     */
    public boolean equals(ReadonlyVec4D v) {
        try {
            return (x == v.x() && y == v.y() && z == v.z() && w == v.w());
        } catch (NullPointerException e) {
            return false;
        }
    }

    public boolean equalsWithTolerance(ReadonlyVec4D v, float tolerance) {
        try {
            float diff = x - v.x();
            if (Float.isNaN(diff)) {
                return false;
            }
            if ((diff < 0 ? -diff : diff) > tolerance) {
                return false;
            }
            diff = y - v.y();
            if (Float.isNaN(diff)) {
                return false;
            }
            if ((diff < 0 ? -diff : diff) > tolerance) {
                return false;
            }
            diff = z - v.z();
            if (Float.isNaN(diff)) {
                return false;
            }
            if ((diff < 0 ? -diff : diff) > tolerance) {
                return false;
            }
            diff = w - v.w();
            if (Float.isNaN(diff)) {
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

    public Vec4D getAbs() {
        return copy().abs();
    }

    public final Vec4D getInvertedXYZ() {
        return copy().invertXYZ();
    }

    public Vec4D getMapped(ScaleMap map) {
        return new Vec4D((float) map.getClippedValueFor(x),
                (float) map.getClippedValueFor(y),
                (float) map.getClippedValueFor(z),
                (float) map.getClippedValueFor(w));
    }

    public Vec4D getMappedXYZ(ScaleMap map) {
        return new Vec4D((float) map.getClippedValueFor(x),
                (float) map.getClippedValueFor(y),
                (float) map.getClippedValueFor(z), w);
    }

    public Vec4D getNormalized() {
        return copy().normalize();
    }

    public Vec4D getNormalizedTo(float len) {
        return copy().normalizeTo(len);
    }

    public Vec4D getRotatedAroundAxis(ReadonlyVec3D axis, float theta) {
        return copy().rotateAroundAxis(axis, theta);
    }

    public Vec4D getRotatedX(float theta) {
        return copy().rotateX(theta);
    }

    public Vec4D getRotatedY(float theta) {
        return copy().rotateY(theta);
    }

    public Vec4D getRotatedZ(float theta) {
        return copy().rotateZ(theta);
    }

    public Vec4D getRoundedXYZTo(float prec) {
        return copy().roundXYZTo(prec);
    }

    public Vec4D getUnweighted() {
        return copy().unweight();
    }

    public Vec4D getWeighted() {
        return copy().weight();
    }

    /**
     * Returns a hash code value based on the data values in this object. Two
     * different Vec4D objects with identical data values (i.e., Vec4D.equals
     * returns true) will return the same hash code value. Two objects with
     * different data members may return the same hash value, although this is
     * not likely.
     * 
     * @return the integer hash code value
     */
    public int hashCode() {
        long bits = 1L;
        bits = 31L * bits + VecMathUtil.floatToIntBits(x);
        bits = 31L * bits + VecMathUtil.floatToIntBits(y);
        bits = 31L * bits + VecMathUtil.floatToIntBits(z);
        bits = 31L * bits + VecMathUtil.floatToIntBits(w);
        return (int) (bits ^ (bits >> 32));
    }

    public final Vec4D interpolateTo(ReadonlyVec4D v, float t) {
        return copy().interpolateToSelf(v, t);
    }

    public final Vec4D interpolateTo(ReadonlyVec4D v, float f,
            InterpolateStrategy s) {
        return new Vec4D(s.interpolate(x, v.x(), f),
                s.interpolate(y, v.y(), f), s.interpolate(z, v.z(), f),
                s.interpolate(w, v.w(), f));
    }

    public final Vec4D interpolateToSelf(ReadonlyVec4D v, float t) {
        this.x += (v.x() - x) * t;
        this.y += (v.y() - y) * t;
        this.z += (v.z() - z) * t;
        this.w += (v.w() - w) * t;
        return this;
    }

    public final Vec4D interpolateToSelf(ReadonlyVec4D v, float f,
            InterpolateStrategy s) {
        x = s.interpolate(x, v.x(), f);
        y = s.interpolate(y, v.y(), f);
        z = s.interpolate(z, v.z(), f);
        w = s.interpolate(w, v.w(), f);
        return this;
    }

    public final Vec4D invertXYZ() {
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

    public final float magnitude() {
        return (float) Math.sqrt(x * x + y * y + z * z + w * w);
    }

    public final float magSquared() {
        return x * x + y * y + z * z + w * w;
    }

    /**
     * Normalizes the vector so that its magnitude = 1.
     * 
     * @return itself
     */
    public final Vec4D normalize() {
        float mag = (float) Math.sqrt(x * x + y * y + z * z);
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
    public final Vec4D normalizeTo(float len) {
        float mag = (float) Math.sqrt(x * x + y * y + z * z);
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
    public final Vec4D rotateAroundAxis(ReadonlyVec3D axis, float theta) {
        final float ax = axis.x();
        final float ay = axis.y();
        final float az = axis.z();
        final float ux = ax * x;
        final float uy = ax * y;
        final float uz = ax * z;
        final float vx = ay * x;
        final float vy = ay * y;
        final float vz = ay * z;
        final float wx = az * x;
        final float wy = az * y;
        final float wz = az * z;
        final double si = Math.sin(theta);
        final double co = Math.cos(theta);
        float xx = (float) (ax * (ux + vy + wz)
                + (x * (ay * ay + az * az) - ax * (vy + wz)) * co + (-wy + vz)
                * si);
        float yy = (float) (ay * (ux + vy + wz)
                + (y * (ax * ax + az * az) - ay * (ux + wz)) * co + (wx - uz)
                * si);
        float zz = (float) (az * (ux + vy + wz)
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
    public final Vec4D rotateX(float theta) {
        final float co = (float) Math.cos(theta);
        final float si = (float) Math.sin(theta);
        final float zz = co * z - si * y;
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
    public final Vec4D rotateY(float theta) {
        final float co = (float) Math.cos(theta);
        final float si = (float) Math.sin(theta);
        final float xx = co * x - si * z;
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
    public final Vec4D rotateZ(float theta) {
        final float co = (float) Math.cos(theta);
        final float si = (float) Math.sin(theta);
        final float xx = co * x - si * y;
        y = si * x + co * y;
        x = xx;
        return this;
    }

    public Vec4D roundXYZTo(float prec) {
        x = MathUtils.roundTo(x, prec);
        y = MathUtils.roundTo(y, prec);
        z = MathUtils.roundTo(z, prec);
        return this;
    }

    public final Vec4D scale(float s) {
        return new Vec4D(x * s, y * s, z * s, w * s);
    }

    public final Vec4D scale(float xx, float yy, float zz, float ww) {
        return new Vec4D(x * xx, y * yy, z * zz, w * ww);
    }

    public Vec4D scale(ReadonlyVec4D s) {
        return copy().scaleSelf(s);
    }

    public final Vec4D scaleSelf(float s) {
        this.x *= s;
        this.y *= s;
        this.z *= s;
        this.w *= s;
        return this;
    }

    public Vec4D scaleSelf(ReadonlyVec4D s) {
        this.x *= s.x();
        this.y *= s.y();
        this.z *= s.z();
        this.w *= s.w();
        return this;
    }

    public final Vec4D scaleXYZSelf(float s) {
        this.x *= s;
        this.y *= s;
        this.z *= s;
        return this;
    }

    public final Vec4D scaleXYZSelf(float xscale, float yscale, float zscale) {
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
    public Vec4D set(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
        return this;
    }

    public final Vec4D set(ReadonlyVec4D v) {
        this.x = v.x();
        this.y = v.y();
        this.z = v.z();
        this.w = v.w();
        return this;
    }

    public Vec4D setW(float w) {
        this.w = w;
        return this;
    }

    public Vec4D setX(float x) {
        this.x = x;
        return this;
    }

    public final Vec4D setXYZ(ReadonlyVec3D v) {
        this.x = v.x();
        this.y = v.y();
        this.z = v.z();
        return this;
    }

    public Vec4D setY(float y) {
        this.y = y;
        return this;
    }

    public Vec4D setZ(float z) {
        this.z = z;
        return this;
    }

    public final Vec4D sub(ReadonlyVec4D v) {
        return new Vec4D(x - v.x(), y - v.y(), z - v.z(), w - v.w());
    }

    public final Vec4D subSelf(ReadonlyVec4D v) {
        this.x -= v.x();
        this.y -= v.y();
        this.z -= v.z();
        this.w -= v.w();
        return this;
    }

    public final Vec4D subXYZ(float xx, float yy, float zz) {
        return new Vec4D(x - xx, y - yy, z - zz, w);
    }

    public final Vec4D subXYZ(ReadonlyVec3D v) {
        return new Vec4D(x - v.x(), y - v.y(), z - v.z(), w);
    }

    public final Vec4D subXYZSelf(float xx, float yy, float zz) {
        this.x -= xx;
        this.y -= yy;
        this.z -= zz;
        return this;
    }

    public final Vec4D subXYZSelf(ReadonlyVec3D v) {
        this.x -= v.x();
        this.y -= v.y();
        this.z -= v.z();
        return this;
    }

    public final Vec3D to3D() {
        return new Vec3D(x, y, z);
    }

    public float[] toArray() {
        return new float[] {
                x, y, z, w
        };
    }

    public String toString() {
        return "[x=" + x + ", y=" + y + ", z=" + z + ", w=" + w + "]";
    }

    public final Vec4D translate(float xx, float yy, float zz) {
        this.x += w * xx;
        this.y += w * yy;
        this.z += w * zz;
        return this;
    }

    /**
     * Divides each coordinate bythe weight with and sets the coordinate to the
     * newly calculatd ones.
     */
    public final Vec4D unweight() {
        float iw = MathUtils.abs(w) > MathUtils.EPS ? 1f / w : 0;
        x *= iw;
        y *= iw;
        z *= iw;
        return this;
    }

    public final Vec3D unweightInto(Vec3D out) {
        float iw = MathUtils.abs(w) > MathUtils.EPS ? 1f / w : 0;
        out.x = x * iw;
        out.y = y * iw;
        out.z = z * iw;
        return out;
    }

    /**
     * @return the w
     */
    public final float w() {
        return w;
    }

    /**
     * Multiplies the weight with each coordinate and sets the coordinate to the
     * newly calculatd ones.
     * 
     * @return itself
     */
    public final Vec4D weight() {
        x *= w;
        y *= w;
        z *= w;
        return this;
    }

    public final Vec3D weightInto(Vec3D out) {
        out.x = x * w;
        out.y = y * w;
        out.z = z * w;
        return out;
    }

    /**
     * @return the x
     */
    public final float x() {
        return x;
    }

    /**
     * @return the y
     */
    public final float y() {
        return y;
    }

    /**
     * @return the z
     */
    public final float z() {
        return z;
    }
}
