/*
 * jgeom: Geometry Library fo Java
 * 
 * Copyright (C) 2005  Samuel Gerber
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package toxi.geom.nurbs;

import toxi.geom.Polygon2D;
import toxi.geom.Vec3D;
import toxi.geom.Vec4D;

/**
 * A Basic implementation of a NurbsCurve.
 * 
 * @author sg
 * @version 1.0
 */
public class BasicNurbsCurve implements NurbsCurve, Cloneable {

    private Vec4D[] cpoly;
    private KnotVector uKnots;

    /**
     * Create a Nurbs Curve from the given Controlpoints, Knots and degree.<br>
     * [TODO Validate Input, part of it is done by creating the KnotVector]
     * 
     * @param cps
     *            Array of Controlpoints
     * @param uK
     *            Knot values
     * @param degree
     *            Degree of the Nurbs Curve
     */
    public BasicNurbsCurve(Vec4D[] cps, float[] uK, int degree) {
        this(cps, new KnotVector(uK, degree));
    }

    /**
     * Generate a Nurbs from the given Controlpoints and the given Knotvector.<br>
     * [TODO validate input]
     * 
     * @param cps
     *            Array of Controlpoints
     * @param uKnots
     *            Knotvector of the Nurbs
     */
    public BasicNurbsCurve(Vec4D[] cps, KnotVector uKnots) {
        cpoly = cps;
        this.uKnots = uKnots;
        if (uKnots.length() != uKnots.getDegree() + cpoly.length + 1) {
            throw new IllegalArgumentException(
                    "Nurbs Curve has wrong knot number");
        }
    }

    public Vec4D[][] curveDerivCpts(int d, int r1, int r2) {

        Vec4D[][] result = new Vec4D[d + 1][r2 - r1 + 1];

        int degree = uKnots.getDegree();

        // k=0 => control points
        int r = r2 - r1;
        for (int i = 0; i <= r; i++) {
            result[0][i] = cpoly[i];
        }

        // k=1 => 1st derivative, k=2 => 2nd derivative, etc...
        for (int k = 1; k <= d; k++) {
            int tmp = degree - k + 1;
            for (int i = 0; i <= (r - k); i++) {
                Vec4D cw = new Vec4D(result[k - 1][i + 1]);
                cw.subSelf(result[k - 1][i]);
                cw.scaleSelf(tmp);
                cw.scaleSelf(1 / (uKnots.get(r1 + i + degree + 1) - uKnots
                        .get(r1 + i + k)));
                result[k][i] = cw;
            }
        }
        return result;
    }

    public Vec3D[] derivativesOnCurve(float u, int grade) {
        return derivativesOnCurve(u, grade, new Vec3D[grade + 1]);
    }

    public Vec3D[] derivativesOnCurve(float u, int grade, Vec3D[] derivs) {

        int span = uKnots.findSpan(u);
        int degree = uKnots.getDegree();

        // TODO: compute derivatives also for NURBS
        // currently supports only non-rational B-Splines
        float derivVals[][] = uKnots.derivBasisFunctions(span, u, grade);

        // Zero values
        for (int k = (degree + 1); k <= grade; k++) {
            derivs[k] = new Vec3D();
        }

        for (int k = 0; k <= grade; k++) {
            Vec3D d = new Vec3D();
            for (int j = 0; j <= degree; j++) {
                Vec4D v = cpoly[(span - degree) + j];
                float s = derivVals[k][j];
                d.addSelf(v.x * s, v.y * s, v.z * s);
            }
            derivs[k] = d;
        }
        return derivs;
    }

    public Vec4D[] getControlPoints() {
        return cpoly;
    }

    public int getDegree() {
        return uKnots.getDegree();
    }

    public float[] getKnots() {
        return uKnots.getArray();
    }

    public KnotVector getKnotVector() {
        return uKnots;
    }

    public Vec3D pointOnCurve(float u) {
        return pointOnCurve(u, new Vec3D());
    }

    public Vec3D pointOnCurve(float u, Vec3D out) {
        int span = uKnots.findSpan(u);
        int degree = uKnots.getDegree();

        // for periodic knot vectors the usable parameter range is
        // span >= degree and span <= no control points (n+1)
        if (span < degree) {
            return out;
        }

        if (span > uKnots.getN()) {
            return out;
        }

        double[] bf = uKnots.basisFunctions(span, u);
        Vec4D cw = new Vec4D();
        for (int i = 0; i <= degree; i++) {
            cw.addSelf(cpoly[(span - degree) + i].getWeighted().scaleSelf(
                    (float) bf[i]));
        }
        return cw.unweightInto(out);
    }

    public Polygon2D toPolygon2D(int res) {
        float delta = 1f / (res - 1);
        Polygon2D poly = new Polygon2D();
        for (int i = 0; i < res; i++) {
            poly.add(pointOnCurve(i * delta).to2DXY());
        }
        return poly;
    }
}
