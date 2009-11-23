package toxi.geom;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

import toxi.math.InterpolateStrategy;
import toxi.math.MathUtils;

/**
 * Quaternion implementation with SLERP based on http://is.gd/2n9s
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Quaternion {

    public static final float DOT_THRESHOLD = 0.9995f;

    /**
     * Creates a Quaternion from a axis and a angle.
     * 
     * @param axis
     *            axis vector
     * @param angle
     *            angle in radians.
     * 
     * @return new quaternion
     */
    public static Quaternion createFromAxisAngle(Vec3D axis, float angle) {
        float sin = (float) Math.sin(angle / 2);
        float cos = (float) Math.cos(angle / 2);
        Quaternion q =
                new Quaternion(cos, axis.x * sin, axis.y * sin, axis.z * sin);
        q.normalize();
        return q;
    }

    /**
     * Creates a Quaternion from Euler angles.
     * 
     * @param ax
     *            X-angle in radians.
     * @param ay
     *            Y-angle in radians.
     * @param az
     *            Z-angle in radians.
     * 
     * @return new quaternion
     */
    public static Quaternion createFromEuler(float ax, float ay, float az) {
        float sinPitch = (float) Math.sin(ax * 0.5);
        float cosPitch = (float) Math.cos(ax * 0.5);
        float sinYaw = (float) Math.sin(ay * 0.5);
        float cosYaw = (float) Math.cos(ay * 0.5);
        float sinRoll = (float) Math.sin(az * 0.5);
        float cosRoll = (float) Math.cos(az * 0.5);
        float cosPitchCosYaw = cosPitch * cosYaw;
        float sinPitchSinYaw = sinPitch * sinYaw;

        Quaternion q = new Quaternion();

        q.x = sinRoll * cosPitchCosYaw - cosRoll * sinPitchSinYaw;
        q.y = cosRoll * sinPitch * cosYaw + sinRoll * cosPitch * sinYaw;
        q.z = cosRoll * cosPitch * sinYaw - sinRoll * sinPitch * cosYaw;
        q.w = cosRoll * cosPitchCosYaw + sinRoll * sinPitchSinYaw;

        return q;
    }

    public static Quaternion createFromMatrix(Matrix4x4 m) {
        // Creates a quaternion from a rotation matrix.
        // The algorithm used is from Allan and Mark Watt's "Advanced
        // Animation and Rendering Techniques" (ACM Press 1992).

        double s = 0.0f;
        double[] q = new double[4];
        double trace = m.matrix[0][0] + m.matrix[1][1] + m.matrix[2][2];

        if (trace > 0.0f) {
            s = Math.sqrt(trace + 1.0f);
            q[3] = s * 0.5f;
            s = 0.5f / s;
            q[0] = (m.matrix[1][2] - m.matrix[2][1]) * s;
            q[1] = (m.matrix[2][0] - m.matrix[0][2]) * s;
            q[2] = (m.matrix[0][1] - m.matrix[1][0]) * s;
        } else {
            int[] nxt = new int[] { 1, 2, 0 };
            int i = 0, j = 0, k = 0;

            if (m.matrix[1][1] > m.matrix[0][0]) {
                i = 1;
            }

            if (m.matrix[2][2] > m.matrix[i][i]) {
                i = 2;
            }

            j = nxt[i];
            k = nxt[j];
            s =
                    Math
                            .sqrt((m.matrix[i][i] - (m.matrix[j][j] + m.matrix[k][k])) + 1.0f);

            q[i] = s * 0.5f;
            s = 0.5f / s;
            q[3] = (m.matrix[j][k] - m.matrix[k][j]) * s;
            q[j] = (m.matrix[i][j] + m.matrix[j][i]) * s;
            q[k] = (m.matrix[i][k] + m.matrix[k][i]) * s;
        }

        return new Quaternion((float) q[3], (float) q[0], (float) q[1],
                (float) q[2]);
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

    public Quaternion(float w, Vec3D v) {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
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
     * Converts the quat into a 4x4 Matrix. Assumes the quat is currently
     * normalized (if not, you'll need to call {@link #normalize()} first). This
     * calculation would be a lot more complicated for non-unit length
     * quaternions Note: The constructor of Matrix4 expects the Matrix in
     * column-major format like expected by OpenGL
     * 
     * @return result matrix
     */
    public Matrix4x4 getMatrix() {
        float x2 = x * x;
        float y2 = y * y;
        float z2 = z * z;
        float xy = x * y;
        float xz = x * z;
        float yz = y * z;
        float wx = w * x;
        float wy = w * y;
        float wz = w * z;

        return new Matrix4x4(1.0f - 2.0f * (y2 + z2), 2.0f * (xy - wz),
                2.0f * (xz + wy), 0.0f, 2.0f * (xy + wz),
                1.0f - 2.0f * (x2 + z2), 2.0f * (yz - wx), 0.0f,
                2.0f * (xz - wy), 2.0f * (yz + wx), 1.0f - 2.0f * (x2 + y2),
                0.0f, 0.0f, 0.0f, 0.0f, 1.0f);
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
     * Spherical interpolation to target quaternion (code ported from <a href="http://www.gamasutra.com/view/feature/3278/rotating_objects_using_quaternions.php"
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
     * Spherical interpolation to target quaternion (code ported from <a href="http://www.gamasutra.com/view/feature/3278/rotating_objects_using_quaternions.php"
     * >GamaSutra</a>)
     * 
     * @param target
     *            quaternion
     * @param t
     *            interpolation factor (0..1)
     * @return new interpolated quat
     */
    public Quaternion interpolateToSelf(Quaternion target, float t) {
        float scale;
        float invscale;
        float dot = MathUtils.clip(dot(target), -1, 1);
        if ((1.0 - dot) >= MathUtils.EPS) {
            double theta = Math.acos(dot);
            double invsintheta = 1.0 / Math.sin(theta);
            scale = (float) (Math.sin(theta * (1.0 - t)) * invsintheta);
            invscale = (float) (Math.sin(theta * t) * invsintheta);
        } else {
            scale = 1f - t;
            invscale = t;
        }
        if (dot < 0.0) {
            w = scale * w - invscale * target.w;
            x = scale * x - invscale * target.x;
            y = scale * y - invscale * target.y;
            z = scale * z - invscale * target.z;
        } else {
            w = scale * w + invscale * target.w;
            x = scale * x + invscale * target.x;
            y = scale * y + invscale * target.y;
            z = scale * z + invscale * target.z;
        }
        return this;
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
        float mag = (float) Math.sqrt(x * x + y * y + z * z + w * w);
        if (mag > 0) {
            mag = 1f / mag;
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
        return new float[] { w, x, y, z };
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

    public Matrix4x4 toMatrix4x4() {
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

        return new Matrix4x4(1 - (yy + zz), xy + wz, xz - wy, 0, xy - wz,
                1 - (xx + zz), yz + wx, 0, xz + wy, yz - wx, 1 - (xx + yy), 0,
                0, 0, 0, 1);
    }

    public String toString() {
        StringBuffer sb = new StringBuffer(48);
        sb.append("{axis: [").append(x).append(",").append(y).append(",")
                .append(z).append("], w: ").append(w).append("}");
        return sb.toString();
    }
}
