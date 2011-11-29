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

/**
 * KnotVector, assembles the knots values of a NURBS and its degree.
 * 
 * @author sg
 * @version 1.0
 */
public class KnotVector {

    private boolean isOpen;
    private float knots[];
    private int degree;
    private int n;

    /**
     * Create a Knotvector from the given knot values of the desired degree.
     * 
     * @param knots
     *            knot values
     * @param degree
     *            degree of Nurbs
     */
    public KnotVector(float knots[], int degree)
            throws IllegalArgumentException {
        this.knots = knots;
        this.degree = degree;
        n = knots.length - degree - 2;
        for (int i = 1; i < knots.length; i++) {
            if (knots[i - 1] > knots[i]) {
                throw new IllegalArgumentException("Knots not valid knot["
                        + (i - 1) + "] > knot[" + i + "]: knot[" + (i - 1)
                        + "]=" + knots[i - 1] + " > knot[" + i + "]="
                        + knots[i]);
            }
        }

        int m = knots.length - 1;

        // Check if it is an open knot vector
        isOpen = true;
        for (int k = 0; k < degree && isOpen; k++) {
            if (knots[k] != knots[k + 1]) {
                isOpen = false;
            }
        }
        for (int k = m; k > m - degree && isOpen; k--) {
            if (knots[k] != knots[k - 1]) {
                isOpen = false;
            }
        }

    }

    /**
     * Gets the basis function values for the given u value. This function
     * calculates firstly the span which is needed in order to calculate the
     * basis functions values.
     * 
     * @param u
     *            Value to calculate basis functions for.
     * @return basis function values
     */
    public double[] basisFunctions(float u) {
        return basisFunctions(findSpan(u), u);
    }

    /**
     * Calculates the basis function values for the given u value, when it's
     * already known in which span u lies.
     * 
     * @param span
     *            The span u lies in
     * @param u
     *            Value to calculate basis functions for.
     * @return basis function values
     */
    public double[] basisFunctions(int span, float u) {
        final int d1 = degree + 1;
        double res[] = new double[d1];
        double left[] = new double[d1];
        double right[] = new double[d1];
        res[0] = 1;
        for (int j = 1; j < d1; j++) {
            left[j] = u - knots[span + 1 - j];
            right[j] = knots[span + j] - u;
            double saved = 0;
            for (int r = 0; r < j; r++) {
                double tmp = res[r] / (right[r + 1] + left[j - r]);
                res[r] = saved + right[r + 1] * tmp;
                saved = left[j - r] * tmp;
            }
            res[j] = saved;
        }
        return res;
    }

    /**
     * Calculates the basis functions and its derivatives up to the given grade.
     * 
     * @param u
     *            Value to calculate basis functions and derivatives for.
     * @param grade
     *            grade to calculate derivations for.
     * @return an array of basis function values or derivated basis functions
     *         values. The first array is the degree of dderivation in the
     *         second array rhe values are stored. <br>
     *         Example: <br>
     *         <code>
     * float[]][] f=dersBasisFuns(0.1f, 3);
     * float value=f[0][1]; //In value is know the second value of the basis function derived 0 times stored.
     * </code>
     * 
     */
    public float[][] derivBasisFunctions(float u, int grade) {
        int span = findSpan(u);
        return derivBasisFunctions(span, u, grade);
    }

    /**
     * Calculates the basis functions and its derivatives up to the given grade.
     * 
     * @param span
     *            Span the given value lies in.
     * @param u
     *            Value to calculate basis functions and derivatives for.
     * @param grade
     *            grade to calculate derivations for.
     * @return an array of basis function values or derivated basis functions
     *         values
     * @see KnotVector#derivBasisFunctions(float, int)
     */
    public float[][] derivBasisFunctions(int span, float u, int grade) {
        float[][] ders = new float[grade + 1][degree + 1];
        float[][] ndu = new float[degree + 1][degree + 1];
        ndu[0][0] = 1.0f;
        float[] left = new float[degree + 1];
        float[] right = new float[degree + 1];
        int j1, j2;
        for (int j = 1; j <= degree; j++) {
            left[j] = u - knots[span + 1 - j];
            right[j] = knots[span + j] - u;
            float saved = 0.0f;
            for (int r = 0; r < j; r++) {
                ndu[j][r] = right[r + 1] + left[j - r];
                float temp = ndu[r][j - 1] / ndu[j][r];
                ndu[r][j] = saved + right[r + 1] * temp;
                saved = left[j - r] * temp;
            }
            ndu[j][j] = saved;
        }
        for (int j = 0; j <= degree; j++) {
            ders[0][j] = ndu[j][degree];
        }
        for (int r = 0; r <= degree; r++) {
            int s1 = 0;
            int s2 = 1;
            float[][] a = new float[2][degree + 1];
            a[0][0] = 1.0f;
            for (int k = 1; k <= grade; k++) {
                float d = 0.0f;
                final int rk = r - k;
                final int pk = degree - k;
                final float[] as1 = a[s1];
                final float[] as2 = a[s2];
                if (r >= k) {
                    as2[0] = d = as1[0] / ndu[pk + 1][rk];
                    d *= ndu[rk][pk];
                }
                if (rk >= -1) {
                    j1 = 1;
                } else {
                    j1 = -rk;
                }
                if (r - 1 <= pk) {
                    j2 = k - 1;
                } else {
                    j2 = degree - r;
                }
                for (int j = j1; j <= j2; j++) {
                    as2[j] = (as1[j] - as1[j - 1]) / ndu[pk + 1][rk + j];
                    d += as2[j] * ndu[rk + j][pk];
                }
                if (r <= pk) {
                    as2[k] = -as1[k - 1] / ndu[pk + 1][r];
                    d += as2[k] * ndu[r][pk];
                }
                ders[k][r] = d;
                int j = s1;
                s1 = s2;
                s2 = j;
            }
        }
        int r = degree;
        for (int k = 1; k <= grade; k++) {
            for (int j = 0; j <= degree; j++) {
                ders[k][j] *= r;
            }
            r *= (degree - k);
        }
        return ders;
    }

    /**
     * Finds the span (Position of corresponding knot values in knot vector) a
     * given value belongs to.
     * 
     * @param u
     *            value to find span for
     * @return Position of span.
     */
    public int findSpan(float u) {
        if (u >= knots[n + 1]) {
            return n;
        }
        int low = degree;
        int high = n + 1;
        int mid = (low + high) / 2;
        while ((u < knots[mid] || u >= knots[mid + 1]) && low < high) {
            if (u < knots[mid]) {
                high = mid;
            } else {
                low = mid;
            }
            mid = (low + high) / 2;
        }
        return mid;
    }

    /**
     * Get the knot value at a specific index.
     * 
     * @param i
     *            Index to get knot value for
     * @return the knot value
     */
    public float get(int i) {
        return knots[i];
    }

    /**
     * get the knot values as float array
     * 
     * @return the knot values
     */
    public float[] getArray() {
        return knots;
    }

    /**
     * Get the degree of the KnotVector
     * 
     * @return Degree of the Knotvector
     */
    public int getDegree() {
        return degree;
    }

    /**
     * Return the nu
     * 
     * @return Length of the KnotVector
     */
    public int getN() {
        return n;
    }

    public int getNumberOfSegments() {
        int seg = 0;
        float u = knots[0];
        for (int i = 1; i < knots.length; i++) {
            if (u != knots[i]) {
                seg++;
                u = knots[i];
            }
        }
        return seg;
    }

    public synchronized boolean isOpen() {
        return isOpen;
    }

    public int length() {
        return knots.length;
    }

    /**
     * Set the knot value at a specific index. After this operation a call to
     * isValid may be needed if one is not sure if the KnotVector with the
     * changed value is valid for a Nurbs.
     * 
     * @param i
     *            Index to set knot value
     * @param val
     *            value to set the knot too
     */
    public void set(int i, float val) {
        knots[i] = val;
    }

}
