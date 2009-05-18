package toxi.geom;

import java.util.ArrayList;

/**
 * This is a generic 2D B-Spline class for curves of arbitrary length, control
 * handles and patches are created and joined automatically as described here:
 * http://www.ibiblio.org/e-notes/Splines/Bint.htm
 * 
 * @version 0014 Added user adjustable curve tightness control
 */
public class Spline2D {

	public Vec2D[] points;
	public Vec2D[] delta;

	public ArrayList<Vec2D> vertices;
	public BernsteinPolynomial bernstein;

	protected Vec2D[] coeffA;
	protected float[] bi;

	protected float tightness, invTightness;

	private final int numP;

	/**
	 * @param p
	 *            array of control point vectors
	 */
	public Spline2D(Vec2D[] p) {
		this(p, null, 0.25f);
	}

	/**
	 * @param p
	 *            array of control point vectors
	 * @param b
	 *            predefined Bernstein polynomial (good for reusing)
	 * @param tightness
	 *            default curve tightness used for the interpolated vertices (
	 *            {@linkplain #setTightness(float)}
	 */
	public Spline2D(Vec2D[] p, BernsteinPolynomial b, float tightness) {
		points = p;
		numP = points.length;
		coeffA = new Vec2D[numP];
		delta = new Vec2D[numP];
		bi = new float[numP];
		for (int i = 0; i < numP; i++) {
			coeffA[i] = new Vec2D();
			delta[i] = new Vec2D();
			bi[i] = 0;
		}
		bernstein = b;
		setTightness(tightness);
	}

	/**
	 * Computes all curve vertices based on the resolution/number of
	 * subdivisions requested. The higher, the more vertices are computed:
	 * (number of control points - 1) * resolution
	 * 
	 * @param res
	 *            resolution
	 * @return list of Vec2D vertices along the curve
	 */
	public ArrayList<Vec2D> computeVertices(int res) {
		if (bernstein == null || bernstein.resolution != res) {
			bernstein = new BernsteinPolynomial(res);
		}
		if (vertices == null) {
			vertices = new ArrayList<Vec2D>();
		} else {
			vertices.clear();
		}
		findCPoints();
		Vec2D deltaP = new Vec2D();
		Vec2D deltaQ = new Vec2D();
		for (int i = 0; i < numP - 1; i++) {
			Vec2D p = points[i];
			Vec2D q = points[i + 1];
			deltaP.set(delta[i]).addSelf(p);
			deltaQ.set(q).subSelf(delta[i + 1]);
			for (int k = 0; k < bernstein.resolution; k++) {
				float x = p.x * bernstein.b0[k] + deltaP.x * bernstein.b1[k]
						+ deltaQ.x * bernstein.b2[k] + q.x * bernstein.b3[k];
				float y = p.y * bernstein.b0[k] + deltaP.y * bernstein.b1[k]
						+ deltaQ.y * bernstein.b2[k] + q.y * bernstein.b3[k];
				vertices.add(new Vec2D(x, y));
			}
		}
		return vertices;
	}

	protected void findCPoints() {
		bi[1] = -tightness;
		coeffA[1].set((points[2].x - points[0].x - delta[0].x) * tightness,
				(points[2].y - points[0].y - delta[0].y) * tightness);
		for (int i = 2; i < numP - 1; i++) {
			bi[i] = -1 / (invTightness + bi[i - 1]);
			coeffA[i].set(
					-(points[i + 1].x - points[i - 1].x - coeffA[i - 1].x)
							* bi[i],
					-(points[i + 1].y - points[i - 1].y - coeffA[i - 1].y)
							* bi[i]);
		}
		for (int i = numP - 2; i > 0; i--) {
			delta[i].set(coeffA[i].x + delta[i + 1].x * bi[i], coeffA[i].y
					+ delta[i + 1].y * bi[i]);
		}
	}

	/**
	 * @see #setTightness(float)
	 * @return the spline tightness
	 * @since 0014 (rev.216)
	 */
	public float getTightness() {
		return tightness;
	}

	/**
	 * Sets the tightness for future curve interpolation calls. Default value is
	 * 0.25. A value of 0.0 equals linear interpolation between the given curve
	 * input points. Curve behaviour for values outside the 0.0 .. 0.5 interval
	 * is unspecified and becomes increasingly less intuitive. Negative values
	 * are possible too and create interesting results (in some cases).
	 * 
	 * @param tightness
	 *            the tightness value used for the next call to
	 *            {@link #computeVertices(int)}
	 * @since 0014 (rev. 216)
	 */
	public Spline2D setTightness(float tightness) {
		this.tightness = tightness;
		this.invTightness = 1f / tightness;
		return this;
	}
}