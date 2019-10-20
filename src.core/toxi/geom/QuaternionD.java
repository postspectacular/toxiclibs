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
public class QuaternionD {

    public static final double DOT_THRESHOLD = 0.9995f;

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
    public static QuaternionD createFromAxisAngle(ReadonlyVecD3D axis, double angle) {
        angle *= 0.5;
        double sin = MathUtils.sin(angle);
        double cos = MathUtils.cos(angle);
        QuaternionD q = new QuaternionD(cos, axis.getNormalizedTo(sin));
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
    public static QuaternionD createFromEuler(double pitch, double yaw, double roll) {
        pitch *= 0.5;
        yaw *= 0.5;
        roll *= 0.5;
        double sinPitch = MathUtils.sin(pitch);
        double cosPitch = MathUtils.cos(pitch);
        double sinYaw = MathUtils.sin(yaw);
        double cosYaw = MathUtils.cos(yaw);
        double sinRoll = MathUtils.sin(roll);
        double cosRoll = MathUtils.cos(roll);
        double cosPitchCosYaw = cosPitch * cosYaw;
        double sinPitchSinYaw = sinPitch * sinYaw;

        QuaternionD q = new QuaternionD();

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
    public static QuaternionD createFromMatrix(Matrix4x4 m) {

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

        return new QuaternionD( q[3],  q[0],  q[1],  q[2]);
    }

    /**
     * Constructs a quaternion that rotates the vector given by the "forward"
     * param into the direction given by the "dir" param.
     * 
     * @param dir
     * @param forward
     * @return quaternion
     */
    public static QuaternionD getAlignmentQuat(ReadonlyVecD3D dir,
            ReadonlyVecD3D forward) {
        VecD3D target = dir.getNormalized();
        ReadonlyVecD3D axis = forward.cross(target);
        double length = axis.magnitude() + 0.0001f;
        double angle =  Math.atan2(length, forward.dot(target));
        return createFromAxisAngle(axis, angle);
    }

    @XmlAttribute(required = true)
    public double x, y, z, w;

    public QuaternionD() {
        identity();
    }

    public QuaternionD(double w, double x, double y, double z) {
        this.w = w;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public QuaternionD(double w, ReadonlyVecD3D v) {
        this.x = v.x();
        this.y = v.y();
        this.z = v.z();
        this.w = w;
    }

    public QuaternionD(QuaternionD q) {
        this.w = q.w;
        this.x = q.x;
        this.y = q.y;
        this.z = q.z;
    }
    public QuaternionD(Quaternion q) {
        this.w = q.w;
        this.x = q.x;
        this.y = q.y;
        this.z = q.z;
    }
    

    public QuaternionD add(QuaternionD q) {
        return new QuaternionD(x + q.x, y + q.y, z + q.z, w + q.w);
    }

    public QuaternionD addSelf(QuaternionD q) {
        x += q.x;
        y += q.y;
        z += q.z;
        w += q.w;
        return this;
    }

    public VecD3D applyTo(VecD3D v) {
        double ix = w * v.x + y * v.z - z * v.y;
        double iy = w * v.y + z * v.x - x * v.z;
        double iz = w * v.z + x * v.y - y * v.x;
        double iw = -x * v.x - y * v.y - z * v.z;
        double xx = ix * w - iw * x - iy * z + iz * y;
        double yy = iy * w - iw * y - iz * x + ix * z;
        double zz = iz * w - iw * z - ix * y + iy * x;
        v.set(xx, yy, zz);
        return v;
    }

    public QuaternionD copy() {
        return new QuaternionD(w, x, y, z);
    }

    /**
     * Computes the dot product with the given quaternion.
     * 
     * @param q
     * @return dot product
     */
    public double dot(QuaternionD q) {
        return (x * q.x) + (y * q.y) + (z * q.z) + (w * q.w);
    }

    /**
     * Computes this quaternion's conjugate, defined as the same w around the
     * inverted axis.
     * 
     * @return new conjugate quaternion
     */
    public QuaternionD getConjugate() {
        QuaternionD q = new QuaternionD();
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
    public QuaternionD getNormalized() {
        return new QuaternionD(this).normalize();
    }

    public QuaternionD identity() {
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
    public QuaternionD interpolateTo(QuaternionD target, double t) {
        return copy().interpolateToSelf(target, t);
    }

    /**
     * @param target
     * @param t
     * @param is
     * @return interpolated quaternion as new instance
     */
    public QuaternionD interpolateTo(QuaternionD target, double t,
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
    public QuaternionD interpolateToSelf(QuaternionD target, double t) {
        double scale;
        double invscale;
        double dot = dot(target);
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
            w =  (scale * w - invscale * target.w);
            x =  (scale * x - invscale * target.x);
            y =  (scale * y - invscale * target.y);
            z =  (scale * z - invscale * target.z);
        } else {
            w =  (scale * w + invscale * target.w);
            x =  (scale * x + invscale * target.x);
            y =  (scale * y + invscale * target.y);
            z =  (scale * z + invscale * target.z);
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
    public QuaternionD interpolateToSelf(QuaternionD target, double t,
            InterpolateStrategy is) {
        return interpolateToSelf(target, is.interpolate(0, 1, t));
    }

    public double magnitude() {
        return  Math.sqrt(x * x + y * y + z * z + w * w);
    }

    public QuaternionD multiply(QuaternionD q2) {
        QuaternionD res = new QuaternionD();
        res.w = w * q2.w - x * q2.x - y * q2.y - z * q2.z;
        res.x = w * q2.x + x * q2.w + y * q2.z - z * q2.y;
        res.y = w * q2.y + y * q2.w + z * q2.x - x * q2.z;
        res.z = w * q2.z + z * q2.w + x * q2.y - y * q2.x;

        return res;
    }

    public QuaternionD normalize() {
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

    public QuaternionD scale(double t) {
        return new QuaternionD(x * t, y * t, z * t, w * t);
    }

    public QuaternionD scaleSelf(double t) {
        x *= t;
        y *= t;
        z *= t;
        w *= t;
        return this;
    }

    public QuaternionD set(double w, double x, double y, double z) {
        this.w = w;
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    public QuaternionD set(double w, VecD3D v) {
        this.w = w;
        x = v.x;
        y = v.y;
        z = v.z;
        return this;
    }

    public QuaternionD set(QuaternionD q) {
        w = q.w;
        x = q.x;
        y = q.y;
        z = q.z;
        return this;
    }

    public QuaternionD sub(QuaternionD q) {
        return new QuaternionD(x - q.x, y - q.y, z - q.z, w - q.w);
    }

    public QuaternionD subSelf(QuaternionD q) {
        x -= q.x;
        y -= q.y;
        z -= q.z;
        w -= q.w;
        return this;
    }

    public double[] toArray() {
        return new double[] {
                w, x, y, z
        };
    }

    /**
     * Converts the quaternion into a double array consisting of: rotation angle
     * in radians, rotation axis x,y,z
     * 
     * @return 4-element double array
     */
    public double[] toAxisAngle() {
        double[] res = new double[4];
        double sa =  Math.sqrt(1.0f - w * w);
        if (sa < MathUtils.EPS) {
            sa = 1.0f;
        } else {
            sa = 1.0f / sa;
        }
        res[0] =  Math.acos(w) * 2.0f;
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

        double x2 = x + x;
        double y2 = y + y;
        double z2 = z + z;
        double xx = x * x2;
        double xy = x * y2;
        double xz = x * z2;
        double yy = y * y2;
        double yz = y * z2;
        double zz = z * z2;
        double wx = w * x2;
        double wy = w * y2;
        double wz = w * z2;

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
