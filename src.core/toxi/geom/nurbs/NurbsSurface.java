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

import toxi.geom.Vec3D;
import toxi.geom.Vec4D;

/**
 * Interface for Nurbs Surfaces.
 * 
 * @author sg
 * @version 1.2
 */
public interface NurbsSurface {

    /**
     * Get the Contol points of the NurbsSurface
     * 
     * @return ControlNet of the Nurbs
     */
    ControlNet getControlNet();

    /**
     * Get a List of all TrimCurves asociated with this Nurbs Surface
     * 
     * @return List of TrimCurves
     */
    // List<TrimCurve> getTrimCurves();

    /**
     * Get the degree in u direction
     * 
     * @return degree in u direction
     */
    int getUDegree();

    /**
     * Get the knot values in u direction
     * 
     * @return knot values in u direction
     */
    float[] getUKnots();

    public abstract KnotVector getUKnotVector();

    /**
     * Get the degree in v direction
     * 
     * @return degree in v direction
     */
    int getVDegree();

    /**
     * Get the knot values in v direction
     * 
     * @return knot values in v direction
     */
    float[] getVKnots();

    public abstract KnotVector getVKnotVector();

    public abstract Vec3D pointOnSurface(double u, double v);

    /**
     * Calculate point on surface for the given u and v values
     * 
     * @param u
     *            u value to caculate point from
     * @param v
     *            v value to caculate point from
     * @return calculated point
     */
    Vec3D pointOnSurface(float u, float v);

    /**
     * Add a TrimCurve to this Nurbs Surface
     * 
     * @param tc
     *            TrimCurve to add.
     */
    // void addTrimCurve(TrimCurve tc);

    /**
     * Calculate point on surface for the given u and v values
     * 
     * @param u
     *            u value to caculate point from
     * @param v
     *            v value to caculate point from
     * @param out
     *            point to place result in.
     */
    Vec3D pointOnSurface(float u, float v, Vec3D out);

    /**
     * Computes control points of dth derivative<br />
     * Piegel, L. The Nurbs Book, Algorithm A3.7 -> Page 114<br />
     * 
     * @param d
     *            - dth derivative (0<=k+l<=d)<br />
     * @param r1
     *            - from control point (u direction; 0 for all control points)<br />
     * @param r2
     *            - to control point (u direction; n for all control points)<br />
     * @param s1
     *            - from control point (v direction; 0 for all control points)<br />
     * @param s2
     *            - to control point (v direction; n for all control points)<br />
     * @return ControlPoint4f[k][l][i][j] i,jth control point, differentiated k
     *         times<br />
     *         with respect to u and l times with respect to v
     */
    public Vec4D[][][][] surfaceDerivCpts(int d, int r1, int r2, int s1, int s2);

}
