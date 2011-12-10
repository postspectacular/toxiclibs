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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * <p>
 * This is a generic 3D B-Spline class for curves of arbitrary length, control
 * handles and patches are created and joined automatically as described here:
 * <a
 * href="http://www.ibiblio.org/e-notes/Splines/Bint.htm">ibiblio.org/e-notes/
 * Splines/Bint.htm</a>
 * </p>
 * 
 * <p>
 * Thanks to a bug report by Aaron Meyers (http://universaloscillation.com) the
 * {@linkplain #toLineStrip3D(int)} method has a slightly changed behaviour from
 * version 0014 onwards. In earlier versions erroneous duplicate points would be
 * added near each given control point, which lead to various weird results.
 * </p>
 * 
 * <p>
 * The new behaviour of the curve interpolation/computation is described in the
 * docs for the {@linkplain #toLineStrip3D(int)} method below.
 * </p>
 * 
 * @version 0014 Added user adjustable curve tightness control
 * @version 0015 Added JAXB annotations and List support for dynamic building of
 *          spline
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Spline3D {

    public static final float DEFAULT_TIGHTNESS = 0.25f;

    @XmlTransient
    protected Vec3D[] points;

    @XmlElement(name = "p")
    public List<Vec3D> pointList = new ArrayList<Vec3D>();

    @XmlTransient
    public BernsteinPolynomial bernstein;

    @XmlTransient
    public Vec3D[] delta;

    @XmlTransient
    public Vec3D[] coeffA;

    @XmlTransient
    public float[] bi;

    @XmlAttribute
    protected float tightness;

    @XmlTransient
    protected float invTightness;

    /**
     * Constructs an empty spline container with default curve tightness. You
     * need to populate the spline manually by using {@link #add(ReadonlyVec3D)}
     * .
     */
    public Spline3D() {
        setTightness(DEFAULT_TIGHTNESS);
    }

    /**
     * @param rawPoints
     *            list of control point vectors
     */
    public Spline3D(List<Vec3D> rawPoints) {
        this(rawPoints, null, DEFAULT_TIGHTNESS);
    }

    /**
     * @param rawPoints
     *            list of control point vectors
     * @param b
     *            predefined Bernstein polynomial (good for reusing)
     * @param tightness
     *            default curve tightness used for the interpolated vertices
     *            {@linkplain #setTightness(float)}
     */
    public Spline3D(List<Vec3D> rawPoints, BernsteinPolynomial b,
            float tightness) {
        pointList.addAll(rawPoints);
        bernstein = b;
        setTightness(tightness);
    }

    /**
     * @param pointArray
     *            array of control point vectors
     */
    public Spline3D(Vec3D[] pointArray) {
        this(pointArray, null, DEFAULT_TIGHTNESS);
    }

    /**
     * @param pointArray
     *            array of control point vectors
     * @param b
     *            predefined Bernstein polynomial (good for reusing)
     * @param tightness
     *            default curve tightness used for the interpolated vertices
     *            {@linkplain #setTightness(float)}
     */
    public Spline3D(Vec3D[] pointArray, BernsteinPolynomial b, float tightness) {
        this(Arrays.asList(pointArray), b, tightness);
    }

    public Spline3D add(float x, float y, float z) {
        return add(new Vec3D(x, y, z));
    }

    /**
     * Adds the given point to the list of control points.
     * 
     * @param p
     * @return itself
     */
    public Spline3D add(ReadonlyVec3D p) {
        pointList.add(p.copy());
        return this;
    }

    protected void findCPoints() {
        bi[1] = -tightness;
        coeffA[1].set((points[2].x - points[0].x - delta[0].x) * tightness,
                (points[2].y - points[0].y - delta[0].y) * tightness,
                (points[2].z - points[0].z - delta[0].z) * tightness);
        final int numP = getNumPoints();
        for (int i = 2; i < numP - 1; i++) {
            bi[i] = -1 / (invTightness + bi[i - 1]);
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
     * Returns the number of key points.
     * 
     * @return the numP
     */
    public final int getNumPoints() {
        return pointList.size();
    }

    /**
     * @return the pointList
     */
    public List<Vec3D> getPointList() {
        return pointList;
    }

    /**
     * @see #setTightness(float)
     * @return the spline3d tightness
     * @since 0014 (rev.216)
     */
    public float getTightness() {
        return tightness;
    }

    /**
     * Overrides the current control points with the given list.
     * 
     * @param plist
     *            the pointList to set
     * @return itself
     */
    public Spline3D setPointList(List<Vec3D> plist) {
        pointList.clear();
        for (ReadonlyVec3D p : plist) {
            pointList.add(p.copy());
        }
        return this;
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
     *            {@link #toLineStrip3D(int)}
     * @since 0014 (rev. 216)
     * @return itself
     */
    public Spline3D setTightness(float tightness) {
        this.tightness = tightness;
        this.invTightness = 1f / tightness;
        return this;
    }

    /**
     * <p>
     * Computes all curve vertices based on the resolution/number of
     * subdivisions requested. The higher, the more vertices are computed:
     * </p>
     * <p>
     * <strong>(number of control points - 1) * resolution + 1</strong>
     * </p>
     * <p>
     * Since version 0014 the automatic placement of the curve handles can also
     * be manipulated via the {@linkplain #setTightness(float)} method.
     * </p>
     * 
     * @param res
     *            the number of vertices to be computed per segment between
     *            original control points (incl. control point always at the
     *            start of each segment)
     * @return list of Vec3D vertices along the curve
     */
    public LineStrip3D toLineStrip3D(int res) {
        updateCoefficients();
        if (res < 1) {
            res = 1;
        }
        res++;
        if (bernstein == null || bernstein.resolution != res) {
            bernstein = new BernsteinPolynomial(res);
        }
        LineStrip3D strip = new LineStrip3D();
        findCPoints();
        Vec3D deltaP = new Vec3D();
        Vec3D deltaQ = new Vec3D();
        res--;
        for (int i = 0, numP = getNumPoints(); i < numP - 1; i++) {
            Vec3D p = points[i];
            Vec3D q = points[i + 1];
            deltaP.set(delta[i]).addSelf(p);
            deltaQ.set(q).subSelf(delta[i + 1]);
            for (int k = 0; k < res; k++) {
                float x = p.x * bernstein.b0[k] + deltaP.x * bernstein.b1[k]
                        + deltaQ.x * bernstein.b2[k] + q.x * bernstein.b3[k];
                float y = p.y * bernstein.b0[k] + deltaP.y * bernstein.b1[k]
                        + deltaQ.y * bernstein.b2[k] + q.y * bernstein.b3[k];
                float z = p.z * bernstein.b0[k] + deltaP.z * bernstein.b1[k]
                        + deltaQ.z * bernstein.b2[k] + q.z * bernstein.b3[k];
                strip.add(new Vec3D(x, y, z));
            }
        }
        strip.add(points[points.length - 1].copy());
        return strip;
    }

    public void updateCoefficients() {
        final int numP = getNumPoints();
        if (points == null || (points != null && points.length != numP)) {
            coeffA = new Vec3D[numP];
            delta = new Vec3D[numP];
            bi = new float[numP];
            for (int i = 0; i < numP; i++) {
                coeffA[i] = new Vec3D();
                delta[i] = new Vec3D();
            }
        }
        setTightness(tightness);
        points = pointList.toArray(new Vec3D[numP]);
    }
}