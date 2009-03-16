package toxi.geom;

import toxi.math.MathUtils;

/**
 * Quaternion implementation with SLERP based on http://is.gd/2n9s
 * 
 */
public class Quaternion implements DimensionalVector {

	public static final float DOT_THRESHOLD = 0.9995f;

	float x, y, z, w;

	Quaternion() {
		reset();
	}

	Quaternion(float w, float x, float y, float z) {
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
		return new Quaternion(x + q.x, y + q.y, z - q.z, w + q.w);
	}

	public Quaternion addSelf(Quaternion q) {
		x += q.x;
		y += q.y;
		z += q.z;
		w += q.w;
		return this;
	}

	public int getDimensions() {
		return 4;
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

	public Quaternion getNormalized() {
		return new Quaternion(this).normalize();
	}

	public float[] getValue() {
		float[] res = new float[4];
		float sa = (float) Math.sqrt(1.0f - w * w);
		if (sa < MathUtils.EPS)
			sa = 1.0f;
		else
			sa = 1.0f / sa;
		res[0] = (float) Math.acos(w) * 2.0f;
		res[1] = x * sa;
		res[2] = y * sa;
		res[3] = z * sa;
		return res;
	}

	/**
	 * Spherical interpolation to target quat (code ported from
	 * http://is.gd/2n7t)
	 * 
	 * @param target
	 *            quaternion
	 * @param t
	 *            interpolation factor (0..1)
	 * @return new interpolated quat
	 */
	public Quaternion interpolateTo(Quaternion target, float t) {
		float dot = new Vec3D(x, y, z).dot(new Vec3D(target.x, target.y,
				target.z));
		if (dot > DOT_THRESHOLD) {
			Quaternion result = new Quaternion(w + (target.w - w) * t, x
					+ (target.x - x) * t, y + (target.y - y) * t, z
					+ (target.z - z) * t);
			result.normalize();
			return result;
		}
		dot = MathUtils.clip(dot, -1, 1);
		double theta = Math.acos(dot) * t;
		Quaternion q = target.sub(scale(dot));
		q.normalize();
		return scale((float) Math.cos(theta)).addSelf(
				q.scaleSelf((float) Math.sin(theta)));
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

	public Quaternion reset() {
		w = 1.0f;
		x = 0.0f;
		y = 0.0f;
		z = 0.0f;
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
		return new float[] { x, y, z, w };
	}

	public String toString() {
		StringBuffer sb = new StringBuffer(48);
		sb.append("{axis: [").append(x).append(",").append(y).append(",")
				.append(z).append("], w: ").append(w).append("}");
		return sb.toString();
	}
}
