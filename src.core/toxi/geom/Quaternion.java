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
import javax.xml.bind.annotation.XmlAttribute;

import toxi.math.InterpolateStrategy;
import toxi.math.MathUtils;

/**
 * Quaternion implementation with SLERP based on http://is.gd/2n9s
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Quaternion {

    public static final float DOT_THRESHOLD = 0.9995f;

    /**
     * Creates a Quaternion from a axis and a angle.
     * 
     * @param axis
     *            axis vector (will be normalized)
     * @param angle
     *            angle in radians.
     * 
     * @return new quaternion
     */
    public static Quaternion createFromAxisAngle(ReadonlyVec3D axis, float angle) {
        angle *= 0.5;
        float sin = MathUtils.sin(angle);
        float cos = MathUtils.cos(angle);
        Quaternion q = new Quaternion(cos, axis.getNormalizedTo(sin));
        return q;
    }

    /**
     * Creates a Quaternion from Euler angles.
     * 
     * @param pitch
     *            X-angle in radians.
     * @param yaw
     *            Y-angle in radians.
     * @param roll
     *            Z-angle in radians.
     * 
     * @return new quaternion
     */
    public static Quaternion createFromEuler(float pitch, float yaw, float roll) {
        pitch *= 0.5;
        yaw *= 0.5;
        roll *= 0.5;
        float sinPitch = MathUtils.sin(pitch);
        float cosPitch = MathUtils.cos(pitch);
        float sinYaw = MathUtils.sin(yaw);
        float cosYaw = MathUtils.cos(yaw);
        float sinRoll = MathUtils.sin(roll);
        float cosRoll = MathUtils.cos(roll);
        float cosPitchCosYaw = cosPitch * cosYaw;
        float sinPitchSinYaw = sinPitch * sinYaw;

        Quaternion q = new Quaternion();

        q.x = sinRoll * cosPitchCosYaw - cosRoll * sinPitchSinYaw;
        q.y = cosRoll * sinPitch * cosYaw + sinRoll * cosPitch * sinYaw;
        q.z = cosRoll * cosPitch * sinYaw - sinRoll * sinPitch * cosYaw;
        q.w = cosRoll * cosPitchCosYaw + sinRoll * sinPitchSinYaw;

        // alternative solution from:
        // http://is.gd/6HdEB
        //
        // double c1 = Math.cos(yaw/2);
        // double s1 = Math.sin(yaw/2);
        // double c2 = Math.cos(pitch/2);
        // double s2 = Math.sin(pitch/2);
        // double c3 = Math.cos(roll/2);
        // double s3 = Math.sin(roll/2);
        // double c1c2 = c1*c2;
        // double s1s2 = s1*s2;
        // w =c1c2*c3 - s1s2*s3;
        // x =c1c2*s3 + s1s2*c3;
        // y =s1*c2*c3 + c1*s2*s3;
        // z =c1*s2*c3 - s1*c2*s3;

        return q;
    }

    /**
     * Creates a quaternion from a rotation matrix. The algorithm used is from
     * Allan and Mark Watt's "Advanced Animation and Rendering Techniques" (ACM
     * Press 1992).
     * 
     * @param m
     *            rotation matrix
     * @return quaternion
     */
    public static Quaternion createFromMatrix(Matrix4x4 m) {

        double s = 0.0f;
        double[] q = new double[4];
        double trace = m.matrix[0][0] + m.matrix[1][1] + m.matrix[2][2];
        if (trace > 0.0f) {
            s = 0.5 / Math.sqrt(trace + 1.0);
            q[0] = (m.matrix[2][1] - m.matrix[1][2]) * s;
            q[1] = (m.matrix[0][2] - m.matrix[2][0]) * s;
            q[2] = (m.matrix[1][0] - m.matrix[0][1]) * s;
            q[3] = 0.25 / s;
        } else {
            int[] nxt = new int[] {
                    1, 2, 0
            };
            int i = 0, j = 0, k = 0;

            if (m.matrix[1][1] > m.matrix[0][0]) {
                i = 1;
            }

            if (m.matrix[2][2] > m.matrix[i][i]) {
                i = 2;
            }

            j = nxt[i];
            k = nxt[j];
            s = 2.0f * Math
                    .sqrt((m.matrix[i][i] - m.matrix[j][j] - m.matrix[k][k]) + 1.0f);

            double ss = 1.0 / s;
            q[i] = s * 0.25f;
            q[j] = (m.matrix[j][i] + m.matrix[i][j]) * ss;
            q[k] = (m.matrix[k][i] + m.matrix[i][k]) * ss;
            q[3] = (m.matrix[k][j] - m.matrix[j][k]) * ss;
        }

        return new Quaternion((float) q[3], (float) q[0], (float) q[1],
                (float) q[2]);
    }

    /**
     * Constructs a quaternion that rotates the vector given by the "forward"
     * param into the direction given by the "dir" param.
     * 
     * @param dir
     * @param forward
     * @return quaternion
     */
    public static Quaternion getAlignmentQuat(ReadonlyVec3D dir,
            ReadonlyVec3D forward) {
        Vec3D target = dir.getNormalized();
        ReadonlyVec3D axis = forward.cross(target);
        float length = axis.magnitude() + 0.0001f;
        float angle = (float) Math.atan2(length, forward.dot(target));
        return createFromAxisAngle(axis, angle);
    }

    @XmlAttribute(required = true)
    public float x, y, z, w;

    public Quaternion() {
        identity();
    }

    public Quaternion(float w, float x, float y, float z) {
        this.w = w;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Quaternion(float w, ReadonlyVec3D v) {
        this.x = v.x();
        this.y = v.y();
        this.z = v.z();
        this.w = w;
    }

    public Quaternion(Quaternion q) {
        this.w = q.w;
        this.x = q.x;
        this.y = q.y;
        this.z = q.z;
    }

    public Quaternion add(Quaternion q) {
        return new Quaternion(x + q.x, y + q.y, z + q.z, w + q.w);
    }

    public Quaternion addSelf(Quaternion q) {
        x += q.x;
        y += q.y;
        z += q.z;
        w += q.w;
        return this;
    }

    public Vec3D applyTo(Vec3D v) {
        float ix = w * v.x + y * v.z - z * v.y;
        float iy = w * v.y + z * v.x - x * v.z;
        float iz = w * v.z + x * v.y - y * v.x;
        float iw = -x * v.x - y * v.y - z * v.z;
        float xx = ix * w - iw * x - iy * z + iz * y;
        float yy = iy * w - iw * y - iz * x + ix * z;
        float zz = iz * w - iw * z - ix * y + iy * x;
        v.set(xx, yy, zz);
        return v;
    }

    public Quaternion copy() {
        return new Quaternion(w, x, y, z);
    }

    /**
     * Computes the dot product with the given quaternion.
     * 
     * @param q
     * @return dot product
     */
    public float dot(Quaternion q) {
        return (x * q.x) + (y * q.y) + (z * q.z) + (w * q.w);
    }

    /**
     * Computes this quaternion's conjugate, defined as the same w around the
     * inverted axis.
     * 
     * @return new conjugate quaternion
     */
    public Quaternion getConjugate() {
        Quaternion q = new Quaternion();
        q.x = -x;
        q.y = -y;
        q.z = -z;
        q.w = w;
        return q;
    }

    /**
     * @deprecated use {@link #toMatrix4x4()} instead
     * @return result matrix
     */
    @Deprecated
    public Matrix4x4 getMatrix() {
        return toMatrix4x4();
    }

    /**
     * Computes normalized version of this quaternion.
     * 
     * @return new normalized quaternion
     */
    public Quaternion getNormalized() {
        return new Quaternion(this).normalize();
    }

    public Quaternion identity() {
        w = 1.0f;
        x = 0.0f;
        y = 0.0f;
        z = 0.0f;
        return this;
    }

    /**
     * Spherical interpolation to target quaternion (code ported from <a href=
     * "http://www.gamasutra.com/view/feature/3278/rotating_objects_using_quaternions.php"
     * >GamaSutra</a>)
     * 
     * @param target
     *            quaternion
     * @param t
     *            interpolation factor (0..1)
     * @return new interpolated quat
     */
    public Quaternion interpolateTo(Quaternion target, float t) {
        return copy().interpolateToSelf(target, t);
    }

    /**
     * @param target
     * @param t
     * @param is
     * @return interpolated quaternion as new instance
     */
    public Quaternion interpolateTo(Quaternion target, float t,
            InterpolateStrategy is) {
        return copy().interpolateToSelf(target, is.interpolate(0, 1, t));
    }

    /**
     * Spherical interpolation to target quaternion (code ported from <a href=
     * "http://www.gamasutra.com/view/feature/3278/rotating_objects_using_quaternions.php"
     * >GamaSutra</a>)
     * 
     * @param target
     *            quaternion
     * @param t
     *            interpolation factor (0..1)
     * @return new interpolated quat
     */
    public Quaternion interpolateToSelf(Quaternion target, double t) {
        double scale;
        double invscale;
        float dot = dot(target);
        double theta = Math.acos(dot);
        double sintheta = Math.sin(theta);
        if (sintheta > 0.001f) {
            scale = Math.sin(theta * (1.0 - t)) / sintheta;
            invscale = Math.sin(theta * t) / sintheta;
        } else {
            scale = 1 - t;
            invscale = t;
        }
        if (dot < 0) {
            w = (float) (scale * w - invscale * target.w);
            x = (float) (scale * x - invscale * target.x);
            y = (float) (scale * y - invscale * target.y);
            z = (float) (scale * z - invscale * target.z);
        } else {
            w = (float) (scale * w + invscale * target.w);
            x = (float) (scale * x + invscale * target.x);
            y = (float) (scale * y + invscale * target.y);
            z = (float) (scale * z + invscale * target.z);
        }
        return normalize();
    }

    /**
     * Uses spherical interpolation to approach the target quaternion. The
     * interpolation factor is manipulated by the chosen
     * {@link InterpolateStrategy} first.
     * 
     * @param target
     * @param t
     * @param is
     * @return itself
     */
    public Quaternion interpolateToSelf(Quaternion target, float t,
            InterpolateStrategy is) {
        return interpolateToSelf(target, is.interpolate(0, 1, t));
    }

    public float magnitude() {
        return (float) Math.sqrt(x * x + y * y + z * z + w * w);
    }

    public Quaternion multiply(Quaternion q2) {
        Quaternion res = new Quaternion();
        res.w = w * q2.w - x * q2.x - y * q2.y - z * q2.z;
        res.x = w * q2.x + x * q2.w + y * q2.z - z * q2.y;
        res.y = w * q2.y + y * q2.w + z * q2.x - x * q2.z;
        res.z = w * q2.z + z * q2.w + x * q2.y - y * q2.x;

        return res;
    }

    public Quaternion normalize() {
        double mag = Math.sqrt(x * x + y * y + z * z + w * w);
        if (mag > MathUtils.EPS) {
            mag = 1.0 / mag;
            x *= mag;
            y *= mag;
            z *= mag;
            w *= mag;
        }
        return this;
    }

    public Quaternion scale(float t) {
        return new Quaternion(x * t, y * t, z * t, w * t);
    }

    public Quaternion scaleSelf(float t) {
        x *= t;
        y *= t;
        z *= t;
        w *= t;
        return this;
    }

    public Quaternion set(float w, float x, float y, float z) {
        this.w = w;
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    public Quaternion set(float w, Vec3D v) {
        this.w = w;
        x = v.x;
        y = v.y;
        z = v.z;
        return this;
    }

    public Quaternion set(Quaternion q) {
        w = q.w;
        x = q.x;
        y = q.y;
        z = q.z;
        return this;
    }

    public Quaternion sub(Quaternion q) {
        return new Quaternion(x - q.x, y - q.y, z - q.z, w - q.w);
    }

    public Quaternion subSelf(Quaternion q) {
        x -= q.x;
        y -= q.y;
        z -= q.z;
        w -= q.w;
        return this;
    }

    public float[] toArray() {
        return new float[] {
                w, x, y, z
        };
    }

    /**
     * Converts the quaternion into a float array consisting of: rotation angle
     * in radians, rotation axis x,y,z
     * 
     * @return 4-element float array
     */
    public float[] toAxisAngle() {
        float[] res = new float[4];
        float sa = (float) Math.sqrt(1.0f - w * w);
        if (sa < MathUtils.EPS) {
            sa = 1.0f;
        } else {
            sa = 1.0f / sa;
        }
        res[0] = (float) Math.acos(w) * 2.0f;
        res[1] = x * sa;
        res[2] = y * sa;
        res[3] = z * sa;
        return res;
    }

    /**
     * Converts the quat to a 4x4 rotation matrix (in row-major format). Assumes
     * the quat is currently normalized (if not, you'll need to call
     * {@link #normalize()} first).
     * 
     * @return result matrix
     */
    public Matrix4x4 toMatrix4x4() {
        return toMatrix4x4(new Matrix4x4());
    }

    public Matrix4x4 toMatrix4x4(Matrix4x4 result) {
        // Converts this quaternion to a rotation matrix.
        //
        // | 1 - 2(y^2 + z^2) 2(xy + wz) 2(xz - wy) 0 |
        // | 2(xy - wz) 1 - 2(x^2 + z^2) 2(yz + wx) 0 |
        // | 2(xz + wy) 2(yz - wx) 1 - 2(x^2 + y^2) 0 |
        // | 0 0 0 1 |

        float x2 = x + x;
        float y2 = y + y;
        float z2 = z + z;
        float xx = x * x2;
        float xy = x * y2;
        float xz = x * z2;
        float yy = y * y2;
        float yz = y * z2;
        float zz = z * z2;
        float wx = w * x2;
        float wy = w * y2;
        float wz = w * z2;

        return result.set(1 - (yy + zz), xy - wz, xz + wy, 0, xy + wz,
                1 - (xx + zz), yz - wx, 0, xz - wy, yz + wx, 1 - (xx + yy), 0,
                0, 0, 0, 1);
    }

    public String toString() {
        StringBuffer sb = new StringBuffer(48);
        sb.append("{axis: [").append(x).append(",").append(y).append(",")
                .append(z).append("], w: ").append(w).append("}");
        return sb.toString();
    }
}
