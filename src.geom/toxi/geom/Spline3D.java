package toxi.geom;

import java.util.ArrayList;

import toxi.math.MathUtils;

/**
 * This is a generic 3D B-Spline class for curves of arbitrary length.
 * <b>Status: initial import from an external project, currently not functional.</b>
 *
 */
public class Spline3D {

	public ArrayList points;
	public Vec3D[] coeffA, delta;
	public float[] bi;

	private int numP;
	private int subDiv;

	ArrayList vertices = new ArrayList();

	BernsteinPolynomial bernstein;

	public Spline3D(ArrayList points, int sd) {
		
	}

	class BernsteinPolynomial {
		float[] b0, b1, b2, b3;
		int resolution;

		BernsteinPolynomial(int res) {
			resolution = res;
			b0 = new float[res];
			b1 = new float[res];
			b2 = new float[res];
			b3 = new float[res];
			float t = 0;
			for (int i = 0; i < res; i++) {
				float t1 = 1 - t, t12 = t1 * t1, t2 = t * t;
				b0[i] = t1 * t12;
				b1[i] = 3 * t * t12;
				b2[i] = 3 * t2 * t1;
				b3[i] = t * t2;
				t += 1.0 / (res - 1);
			}
		}
	}

}
