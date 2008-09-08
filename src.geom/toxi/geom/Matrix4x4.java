package toxi.geom;

/**
 * Implements a simple row-major 4x4 matrix class, all matrix operations are
 * applied to new instances. Use {@link #transpose()} to convert from
 * column-major formats...
 */

// FIXME considere OpenGL is using column-major ordering
// TODO add methods to apply to current instance only
// TODO still needs lots of refactoring
public class Matrix4x4 {

	public double[][] matrix;

	static double[] retval = new double[3];

	// Default constructor
	// Sets matrix to identity
	public Matrix4x4() {
		init();
		matrix[0][0] = 1;
		matrix[1][1] = 1;
		matrix[2][2] = 1;
		matrix[3][3] = 1;
	}

	public Matrix4x4(Matrix4x4 m) {
		for (int i = 0; i < 4; i++)
			for (int j = 0; j < 4; j++)
				matrix[i][j] = m.matrix[i][j];
	}

	public Matrix4x4(double v11, double v12, double v13, double v14,
			double v21, double v22, double v23, double v24, double v31,
			double v32, double v33, double v34, double v41, double v42,
			double v43, double v44) {
		init();

		matrix[0][0] = v11;
		matrix[0][1] = v12;
		matrix[0][2] = v13;
		matrix[0][3] = v14;

		matrix[1][0] = v21;
		matrix[1][1] = v22;
		matrix[1][2] = v23;
		matrix[1][3] = v24;

		matrix[2][0] = v31;
		matrix[2][1] = v32;
		matrix[2][2] = v33;
		matrix[2][3] = v34;

		matrix[3][0] = v41;
		matrix[3][1] = v42;
		matrix[3][2] = v43;
		matrix[3][3] = v44;
	}

	// Initializing constructor - single-dimensional array
	// Assumes row-major ordering (column idx increases faster)
	public Matrix4x4(double[] array) {
		if (array.length != 9 && array.length != 16) {
			throw new RuntimeException("Array.length must == 9 or 16");
		}

		init();

		if (array.length == 16) {
			matrix[0][0] = array[0];
			matrix[0][1] = array[1];
			matrix[0][2] = array[2];
			matrix[0][3] = array[3];

			matrix[1][0] = array[4];
			matrix[1][1] = array[5];
			matrix[1][2] = array[6];
			matrix[1][3] = array[7];

			matrix[2][0] = array[8];
			matrix[2][1] = array[9];
			matrix[2][2] = array[10];
			matrix[2][3] = array[11];

			matrix[3][0] = array[12];
			matrix[3][1] = array[13];
			matrix[3][2] = array[14];
			matrix[3][3] = array[15];
		} else if (array.length == 9) {
			matrix[0][0] = array[0];
			matrix[0][1] = array[1];
			matrix[0][2] = array[2];

			matrix[1][0] = array[3];
			matrix[1][1] = array[4];
			matrix[1][2] = array[5];

			matrix[2][0] = array[6];
			matrix[2][1] = array[7];
			matrix[2][2] = array[8];

			matrix[3][0] = array[9];
			matrix[3][1] = array[10];
			matrix[3][2] = array[11];
			matrix[3][3] = 1;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "| " + matrix[0][0] + " " + matrix[0][1] + " " + matrix[0][2]
				+ " " + matrix[0][3] + " |\n" + "| " + matrix[1][0] + " "
				+ matrix[1][1] + " " + matrix[1][2] + " " + matrix[1][3]
				+ " |\n" + "| " + matrix[2][0] + " " + matrix[2][1] + " "
				+ matrix[2][2] + " " + matrix[2][3] + " |\n" + "| "
				+ matrix[3][0] + " " + matrix[3][1] + " " + matrix[3][2] + " "
				+ matrix[3][3] + " |";
	}

	public Matrix4x4 identity() {
		for (int i = 0; i < 4; i++)
			for (int j = 0; j < 4; j++)
				matrix[i][j] = 0;
		matrix[0][0] = 1;
		matrix[1][1] = 1;
		matrix[2][2] = 1;
		matrix[3][3] = 1;
		return this;
	}

	public Matrix4x4 add(Matrix4x4 rhs) {
		Matrix4x4 retval = new Matrix4x4();

		for (int i = 0; i < 4; i++)
			for (int j = 0; j < 4; j++)
				retval.matrix[i][j] = matrix[i][j] + rhs.matrix[i][j];

		return retval;
	}

	// Matrix-Matrix Subtraction
	public Matrix4x4 sub(Matrix4x4 rhs) {
		Matrix4x4 retval = new Matrix4x4();
		for (int i = 0; i < 4; i++)
			for (int j = 0; j < 4; j++)
				retval.matrix[i][j] = matrix[i][j] - rhs.matrix[i][j];

		return retval;
	}

	// Matrix-Scalar Multiplication
	public Matrix4x4 multiply(double c) {
		Matrix4x4 retval = new Matrix4x4();
		for (int i = 0; i < 4; i++)
			for (int j = 0; j < 4; j++)
				retval.matrix[i][j] = c * matrix[i][j];
		return retval;
	}

	// Matrix-Vector Multiplication (Application)
	public Vec3D apply(Vec3D vec) {
		// Create a new vector and make it 4d homogenous
		double vectorOut[] = new double[4];
		double vectorIn[] = new double[4];
		vectorIn[0] = vec.x;
		vectorIn[1] = vec.y;
		vectorIn[2] = vec.z;
		vectorIn[3] = 1;

		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++)
				vectorOut[i] += vectorIn[j] * matrix[i][j];
		}

		return new Vec3D((float) vectorOut[0], (float) vectorOut[1],
				(float) vectorOut[2]).scaleSelf((float) (1 / vectorOut[3]));
	}

	// Matrix-Matrix Left-multiplication
	// Given matrix this, mat; performs mat*this
	public Matrix4x4 leftMultiply(Matrix4x4 mat) {
		return mat.multiply(this);
	}

	// Matrix-Matrix Right-multiplication
	public Matrix4x4 multiply(Matrix4x4 mat) {
		Matrix4x4 retval = new Matrix4x4();
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				retval.matrix[i][j] = 0;
				for (int k = 0; k < 4; k++) {
					retval.matrix[i][j] += matrix[i][k] * mat.matrix[k][j];
				}
			}
		}
		return retval;
	}

	// Apply Translation to Matrix
	public Matrix4x4 translate(double dx, double dy, double dz) {
		Matrix4x4 trn = new Matrix4x4();
		trn.matrix[0][3] = dx;
		trn.matrix[1][3] = dy;
		trn.matrix[2][3] = dz;
		// return trn.mul(this);
		return trn.leftMultiply(this);
	}

	// Apply Scale to Matrix
	public Matrix4x4 scale(double scaleX, double scaleY, double scaleZ) {
		Matrix4x4 scl = new Matrix4x4();
		scl.matrix[0][0] = scaleX;
		scl.matrix[1][1] = scaleY;
		scl.matrix[2][2] = scaleZ;
		// return scl.mul(this);
		return scl.leftMultiply(this);
	}

	// Apply Rotation about X to Matrix
	public Matrix4x4 rotateX(double theta) {
		Matrix4x4 rot = new Matrix4x4();
		rot.matrix[1][1] = rot.matrix[2][2] = Math.cos(theta);
		rot.matrix[2][1] = Math.sin(theta);
		rot.matrix[1][2] = -rot.matrix[2][1];
		return rot.leftMultiply(this);
	}

	// Apply Rotation about Y to Matrix
	public Matrix4x4 rotateY(double theta) {
		Matrix4x4 rot = new Matrix4x4();
		rot.matrix[0][0] = rot.matrix[2][2] = Math.cos(theta);
		rot.matrix[0][2] = Math.sin(theta);
		rot.matrix[2][0] = -rot.matrix[0][2];
		return rot.leftMultiply(this);
	}

	// Apply Rotation about Z to Matrix
	public Matrix4x4 rotateZ(double theta) {
		Matrix4x4 rot = new Matrix4x4();
		rot.matrix[0][0] = rot.matrix[1][1] = Math.cos(theta);
		rot.matrix[1][0] = Math.sin(theta);
		rot.matrix[0][1] = -rot.matrix[1][0];
		return rot.leftMultiply(this);
	}

	// Apply Rotation about arbitrary axis to Matrix
	public Matrix4x4 rotate(Vec3D axis, double theta) {
		double x, y, z, s, c, t;
		x = axis.x;
		y = axis.y;
		z = axis.z;
		s = Math.sin(theta);
		c = Math.cos(theta);
		// c = Math.sqrt(1 - s * s); // this may be faster than the previous
		// line
		t = 1 - c;

		return (new Matrix4x4(t * x * x + c, t * x * y + s * z, t * x * z - s
				* y, 0, t * x * y - s * z, t * y * y + c, t * y * z + s * x, 0,
				t * x * z + s * y, t * y * z - s * x, t * z * z + c, 0, 0, 0,
				0, 1)).leftMultiply(this);
	}

	// Matrix Transpose
	public Matrix4x4 transpose() {
		return new Matrix4x4(matrix[0][0], matrix[1][0], matrix[2][0],
				matrix[3][0], matrix[0][1], matrix[1][1], matrix[2][1],
				matrix[3][1], matrix[0][2], matrix[1][2], matrix[2][2],
				matrix[3][2], matrix[0][3], matrix[1][3], matrix[2][3],
				matrix[3][3]);
	}

	// Matrix Inversion using Cramer's Method
	// Computes Adjoint matrix divided by determinant
	// Code modified from
	// http://www.intel.com/design/pentiumiii/sml/24504301.pdf
	// Turns out we don't need this after all
	public Matrix4x4 inverse() {
		double[] mat = new double[16];
		double[] dst = new double[16];

		// Copy all of the elements into the linear array
		for (int i = 0, k = 0; i < 4; i++)
			for (int j = 0; j < 4; j++)
				mat[k++] = matrix[i][j];

		double[] tmp = new double[12];
		double src[] = new double[16];
		double det;

		for (int i = 0; i < 4; i++) {
			int i4 = i << 2;
			src[i] = mat[i4];
			src[i + 4] = mat[i4 + 1];
			src[i + 8] = mat[i4 + 2];
			src[i + 12] = mat[i4 + 3];
		}

		/* calculatepairsforfirst8elements(cofactors) */
		tmp[0] = src[10] * src[15];
		tmp[1] = src[11] * src[14];
		tmp[2] = src[9] * src[15];
		tmp[3] = src[11] * src[13];
		tmp[4] = src[9] * src[14];
		tmp[5] = src[10] * src[13];
		tmp[6] = src[8] * src[15];
		tmp[7] = src[11] * src[12];
		tmp[8] = src[8] * src[14];
		tmp[9] = src[10] * src[12];
		tmp[10] = src[8] * src[13];
		tmp[11] = src[9] * src[12];

		/* calculatefirst8elements(cofactors) */
		dst[0] = tmp[0] * src[5] + tmp[3] * src[6] + tmp[4] * src[7];
		dst[0] -= tmp[1] * src[5] + tmp[2] * src[6] + tmp[5] * src[7];
		dst[1] = tmp[1] * src[4] + tmp[6] * src[6] + tmp[9] * src[7];
		dst[1] -= tmp[0] * src[4] + tmp[7] * src[6] + tmp[8] * src[7];
		dst[2] = tmp[2] * src[4] + tmp[7] * src[5] + tmp[10] * src[7];
		dst[2] -= tmp[3] * src[4] + tmp[6] * src[5] + tmp[11] * src[7];
		dst[3] = tmp[5] * src[4] + tmp[8] * src[5] + tmp[11] * src[6];
		dst[3] -= tmp[4] * src[4] + tmp[9] * src[5] + tmp[10] * src[6];
		dst[4] = tmp[1] * src[1] + tmp[2] * src[2] + tmp[5] * src[3];
		dst[4] -= tmp[0] * src[1] + tmp[3] * src[2] + tmp[4] * src[3];
		dst[5] = tmp[0] * src[0] + tmp[7] * src[2] + tmp[8] * src[3];
		dst[5] -= tmp[1] * src[0] + tmp[6] * src[2] + tmp[9] * src[3];
		dst[6] = tmp[3] * src[0] + tmp[6] * src[1] + tmp[11] * src[3];
		dst[6] -= tmp[2] * src[0] + tmp[7] * src[1] + tmp[10] * src[3];
		dst[7] = tmp[4] * src[0] + tmp[9] * src[1] + tmp[10] * src[2];
		dst[7] -= tmp[5] * src[0] + tmp[8] * src[1] + tmp[11] * src[2];

		/* calculatepairsforsecond8elements(cofactors) */
		tmp[0] = src[2] * src[7];
		tmp[1] = src[3] * src[6];
		tmp[2] = src[1] * src[7];
		tmp[3] = src[3] * src[5];
		tmp[4] = src[1] * src[6];
		tmp[5] = src[2] * src[5];
		tmp[6] = src[0] * src[7];
		tmp[7] = src[3] * src[4];
		tmp[8] = src[0] * src[6];
		tmp[9] = src[2] * src[4];
		tmp[10] = src[0] * src[5];
		tmp[11] = src[1] * src[4];

		/* calculatesecond8elements(cofactors) */
		dst[8] = tmp[0] * src[13] + tmp[3] * src[14] + tmp[4] * src[15];
		dst[8] -= tmp[1] * src[13] + tmp[2] * src[14] + tmp[5] * src[15];
		dst[9] = tmp[1] * src[12] + tmp[6] * src[14] + tmp[9] * src[15];
		dst[9] -= tmp[0] * src[12] + tmp[7] * src[14] + tmp[8] * src[15];
		dst[10] = tmp[2] * src[12] + tmp[7] * src[13] + tmp[10] * src[15];
		dst[10] -= tmp[3] * src[12] + tmp[6] * src[13] + tmp[11] * src[15];
		dst[11] = tmp[5] * src[12] + tmp[8] * src[13] + tmp[11] * src[14];
		dst[11] -= tmp[4] * src[12] + tmp[9] * src[13] + tmp[10] * src[14];
		dst[12] = tmp[2] * src[10] + tmp[5] * src[11] + tmp[1] * src[9];
		dst[12] -= tmp[4] * src[11] + tmp[0] * src[9] + tmp[3] * src[10];
		dst[13] = tmp[8] * src[11] + tmp[0] * src[8] + tmp[7] * src[10];
		dst[13] -= tmp[6] * src[10] + tmp[9] * src[11] + tmp[1] * src[8];
		dst[14] = tmp[6] * src[9] + tmp[11] * src[11] + tmp[3] * src[8];
		dst[14] -= tmp[10] * src[11] + tmp[2] * src[8] + tmp[7] * src[9];
		dst[15] = tmp[10] * src[10] + tmp[4] * src[8] + tmp[9] * src[9];
		dst[15] -= tmp[8] * src[9] + tmp[11] * src[10] + tmp[5] * src[8];

		det = src[0] * dst[0] + src[1] * dst[1] + src[2] * dst[2] + src[3]
				* dst[3];

		det = 1 / det;

		Matrix4x4 ret = new Matrix4x4();
		for (int i = 0, k = 0; i < 4; i++)
			for (int j = 0; j < 4; j++)
				ret.matrix[i][j] = dst[k++] * det;
		return ret;
	}

	private final void init() {
		matrix = new double[4][];
		matrix[0] = new double[4];
		matrix[1] = new double[4];
		matrix[2] = new double[4];
		matrix[3] = new double[4];
	}
}
