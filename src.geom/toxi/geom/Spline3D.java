package toxi.geom;

import java.util.ArrayList;

/**
 * This is a generic 3D B-Spline class for curves of arbitrary length, control
 * handles and patches are created and joined automatically as described here:
 * http://www.ibiblio.org/e-notes/Splines/Bint.htm
 */
public class Spline3D {

	public Vec3D[] points;
	public Vec3D[] delta;

	protected Vec3D[] coeffA;
	protected float[] bi;

	private int numP;
	public ArrayList<Vec3D> vertices;

	public BernsteinPolynomial bernstein;

	public Spline3D(Vec3D[] p) {
		points = p;
		numP = points.length;
		coeffA = new Vec3D[numP];
		delta = new Vec3D[numP];
		bi = new float[numP];
		for (int i = 0; i < numP; i++) {
			coeffA[i] = new Vec3D();
			delta[i] = new Vec3D();
			bi[i] = 0;
		}
	}

	protected void findCPoints() {
		bi[1] = -.25f;
		coeffA[1].set((points[2].x - points[0].x - delta[0].x) * 0.25f,
				(points[2].y - points[0].y - delta[0].y) * 0.25f, (points[2].z
						- points[0].z - delta[0].z) * 0.25f);
		for (int i = 2; i < numP - 1; i++) {
			bi[i] = -1 / (4 + bi[i - 1]);
			coeffA[i].set(
					-(points[i + 1].x - points[i - 1].x - coeffA[i - 1].x)
							* bi[i],
					-(points[i + 1].y - points[i - 1].y - coeffA[i - 1].y)
							* bi[i],
					-(points[i + 1].z - points[i - 1].z - coeffA[i - 1].z)
							* bi[i]);
		}
		for (int i = numP - 2; i > 0; i--) {
			delta[i].set(coeffA[i].x + delta[i + 1].x * bi[i], coeffA[i].y
					+ delta[i + 1].y * bi[i], coeffA[i].z + delta[i + 1].z
					* bi[i]);
		}
	}

	/**
	 * Computes all curve vertices based on the resolution/number of
	 * subdivisions requested. The higher, the more vertices are computed:
	 * (number of control points - 1) * resolution
	 * 
	 * @param res
	 *            resolution
	 * @return list of Vec3D vertices along the curve
	 */
	public ArrayList<Vec3D> computeVertices(int res) {
		if (bernstein == null || bernstein.resolution != res) {
			bernstein = new BernsteinPolynomial(res);
		}
		if (vertices == null)
			vertices = new ArrayList<Vec3D>();
		else
			vertices.clear();
		findCPoints();
		Vec3D deltaP = new Vec3D();
		Vec3D deltaQ = new Vec3D();
		for (int i = 0; i < numP - 1; i++) {
			Vec3D p = points[i];
			Vec3D q = points[i + 1];
			deltaP.set(delta[i]).addSelf(p);
			deltaQ.set(q).subSelf(delta[i + 1]);
			for (int k = 0; k < bernstein.resolution; k++) {
				float x = p.x * bernstein.b0[k] + deltaP.x * bernstein.b1[k]
						+ deltaQ.x * bernstein.b2[k] + q.x * bernstein.b3[k];
				float y = p.y * bernstein.b0[k] + deltaP.y * bernstein.b1[k]
						+ deltaQ.y * bernstein.b2[k] + q.y * bernstein.b3[k];
				float z = p.z * bernstein.b0[k] + deltaP.z * bernstein.b1[k]
						+ deltaQ.z * bernstein.b2[k] + q.z * bernstein.b3[k];
				vertices.add(new Vec3D(x, y, z));
			}
		}
		return vertices;
	}

	// FIXME this isn't generic enough & worked just for Nokia Friends project
	public void createSymmetricEnds() {
		delta[0] = points[1].scale(0.75f);
		int lastIdx = numP - 2;
		delta[lastIdx] = points[lastIdx].scale(-0.75f);
	}

	class BernsteinPolynomial {
		float[] b0, b1, b2, b3;
		int resolution;

		public BernsteinPolynomial(int res) {
			resolution = res;
			b0 = new float[res];
			b1 = new float[res];
			b2 = new float[res];
			b3 = new float[res];
			float t = 0;
			float dt = 1.0f / (resolution - 1);
			for (int i = 0; i < resolution; i++) {
				float t1 = 1 - t;
				float t12 = t1 * t1, t2 = t * t;
				b0[i] = t1 * t12;
				b1[i] = 3 * t * t12;
				b2[i] = 3 * t2 * t1;
				b3[i] = t * t2;
				t += dt;
			}
		}
	}
}