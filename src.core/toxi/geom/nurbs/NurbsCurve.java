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
 * Interface for Nurbs Curves
 * 
 * @author sg
 * @version 1.0
 */
public interface NurbsCurve {

    /**
     * Computes control points of dth derivative<br />
     * Piegel, L. The Nurbs Book<br />
     * Algorithm A3.3 -> Page 98<br />
     * <br />
     * 
     * @param d
     *            - dth derivative<br />
     * @param r1
     *            - from control point (0 for all control points)<br />
     * @param r2
     *            - to control point (n for all control points)<br />
     * @return Vec4D[k][i] kth derivative, ith control point
     */
    public Vec4D[][] curveDerivCpts(int d, int r1, int r2);

    public abstract Vec3D[] derivativesOnCurve(float u, int d);

    public abstract Vec3D[] derivativesOnCurve(float u, int d, Vec3D[] ders);

    /**
     * Get the ControlPoints of this curve
     * 
     * @return the ordered ControlPoints
     */
    Vec4D[] getControlPoints();

    /**
     * Get the Degree of the curve
     * 
     * @return degree of curve
     */
    int getDegree();

    /**
     * Gets the Knot values of the Nurbs curve
     * 
     * @return knot values
     */
    float[] getKnots();

    KnotVector getKnotVector();

    /**
     * Calculate point on surface for the given u value
     * 
     * @param u
     *            value to calculate point of
     * @return calculated Point
     */
    Vec3D pointOnCurve(float u);

    /**
     * Calculate point on surface for the given u value
     * 
     * @param u
     *            value to calculate point of
     * @param out
     *            Point to place result in
     */
    Vec3D pointOnCurve(float u, Vec3D out);

    Polygon2D toPolygon2D(int res);
}
