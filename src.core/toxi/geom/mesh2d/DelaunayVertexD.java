package toxi.geom.mesh2d;

import toxi.geom.VecD2D;

/*
 * Copyright (c) 2005, 2007 by L. Paul Chew.
 *
 * Permission is hereby granted, without written agreement and without
 * license or royalty fees, to use, copy, modify, and distribute this
 * software and its documentation for any purpose, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
 * OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */

/**
 * Points in Euclidean space, implemented as double[].
 * 
 * Includes simple geometric operations. Uses matrices; a matrix is represented
 * as an array of Pnts. Uses simplices; a simplex is represented as an array of
 * Pnts.
 * 
 * @author Paul Chew Created July 2005. Derived from an earlier, messier
 *         version. Modified Novemeber 2007. Minor clean up.
 */
public class DelaunayVertexD {

    /**
     * Circumcenter of a simplex.
     * 
     * @param simplex
     *            the simplex (as an array of Pnts)
     * @return the circumcenter (a DelaunayVertexD) of simplex
     */
    public static DelaunayVertexD circumcenter(DelaunayVertexD[] simplex) {
        int dim = simplex[0].dimension();
        if (simplex.length - 1 != dim) {
            throw new IllegalArgumentException("Dimension mismatch");
        }
        DelaunayVertexD[] matrix = new DelaunayVertexD[dim];
        for (int i = 0; i < dim; i++) {
            matrix[i] = simplex[i].bisector(simplex[i + 1]);
        }
        DelaunayVertexD hCenter = cross(matrix); // Center in homogeneous
                                                // coordinates
        double last = hCenter.coordinates[dim];
        double[] result = new double[dim];
        for (int i = 0; i < dim; i++) {
            result[i] = hCenter.coordinates[i] / last;
        }
        return new DelaunayVertexD(result);
    }

    /**
     * Determine the signed content (i.e., area or volume, etc.) of a simplex.
     * 
     * @param simplex
     *            the simplex (as an array of Pnts)
     * @return the signed content of the simplex
     */
    public static double content(DelaunayVertexD[] simplex) {
        DelaunayVertexD[] matrix = new DelaunayVertexD[simplex.length];
        for (int i = 0; i < matrix.length; i++) {
            matrix[i] = simplex[i].extend(1);
        }
        int fact = 1;
        for (int i = 1; i < matrix.length; i++) {
            fact = fact * i;
        }
        return determinant(matrix) / fact;
    }

    /**
     * Compute generalized cross-product of the rows of a matrix. The result is
     * a DelaunayVertexD perpendicular (as a vector) to each row of the matrix.
     * This is not an efficient implementation, but should be adequate for low
     * dimension.
     * 
     * @param matrix
     *            the matrix of Pnts (one less row than the DelaunayVertexD
     *            dimension)
     * @return a DelaunayVertexD perpendicular to each row DelaunayVertexD
     * @throws IllegalArgumentException
     *             if matrix is wrong shape
     */
    public static DelaunayVertexD cross(DelaunayVertexD[] matrix) {
        int len = matrix.length + 1;
        if (len != matrix[0].dimension()) {
            throw new IllegalArgumentException("Dimension mismatch");
        }
        boolean[] columns = new boolean[len];
        for (int i = 0; i < len; i++) {
            columns[i] = true;
        }
        double[] result = new double[len];
        int sign = 1;
        try {
            for (int i = 0; i < len; i++) {
                columns[i] = false;
                result[i] = sign * determinant(matrix, 0, columns);
                columns[i] = true;
                sign = -sign;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new IllegalArgumentException("Matrix is wrong shape");
        }
        return new DelaunayVertexD(result);
    }

    /**
     * Compute the determinant of a matrix (array of Pnts). This is not an
     * efficient implementation, but should be adequate for low dimension.
     * 
     * @param matrix
     *            the matrix as an array of Pnts
     * @return the determinnant of the input matrix
     * @throws IllegalArgumentException
     *             if dimensions are wrong
     */
    public static double determinant(DelaunayVertexD[] matrix) {
        if (matrix.length != matrix[0].dimension()) {
            throw new IllegalArgumentException("Matrix is not square");
        }
        boolean[] columns = new boolean[matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            columns[i] = true;
        }
        try {
            return determinant(matrix, 0, columns);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new IllegalArgumentException("Matrix is wrong shape");
        }
    }

    /**
     * Compute the determinant of a submatrix specified by starting row and by
     * "active" columns.
     * 
     * @param matrix
     *            the matrix as an array of Pnts
     * @param row
     *            the starting row
     * @param columns
     *            a boolean array indicating the "active" columns
     * @return the determinant of the specified submatrix
     * @throws ArrayIndexOutOfBoundsException
     *             if dimensions are wrong
     */
    private static double determinant(DelaunayVertexD[] matrix, int row,
            boolean[] columns) {
        if (row == matrix.length) {
            return 1;
        }
        double sum = 0;
        int sign = 1;
        for (int col = 0; col < columns.length; col++) {
            if (!columns[col]) {
                continue;
            }
            columns[col] = false;
            sum += sign * matrix[row].coordinates[col]
                    * determinant(matrix, row + 1, columns);
            columns[col] = true;
            sign = -sign;
        }
        return sum;
    }

    /**
     * Create a String for a matrix.
     * 
     * @param matrix
     *            the matrix (an array of Pnts)
     * @return a String represenation of the matrix
     */
    public static String toString(DelaunayVertexD[] matrix) {
        StringBuilder buf = new StringBuilder("{");
        for (DelaunayVertexD row : matrix) {
            buf.append(" " + row);
        }
        buf.append(" }");
        return buf.toString();
    }

    private double[] coordinates; // The point's coordinates

    /**
     * Constructor.
     * 
     * @param coords
     *            the coordinates
     */
    public DelaunayVertexD(double... coords) {
        // Copying is done here to ensure that DelaunayVertexD's coords cannot be
        // altered.
        // This is necessary because the double... notation actually creates a
        // constructor with double[] as its argument.
        coordinates = new double[coords.length];
        System.arraycopy(coords, 0, coordinates, 0, coords.length);
    }

    /**
     * Add.
     * 
     * @param p
     *            the other DelaunayVertexD
     * @return a new DelaunayVertexD = this + p
     */
    public DelaunayVertexD add(DelaunayVertexD p) {
        int len = dimCheck(p);
        double[] coords = new double[len];
        for (int i = 0; i < len; i++) {
            coords[i] = this.coordinates[i] + p.coordinates[i];
        }
        return new DelaunayVertexD(coords);
    }

    /**
     * Angle (in radians) between two Pnts (treated as vectors).
     * 
     * @param p
     *            the other DelaunayVertexD
     * @return the angle (in radians) between the two Pnts
     */
    public double angle(DelaunayVertexD p) {
        return Math.acos(this.dot(p) / (this.magnitude() * p.magnitude()));
    }

    /**
     * Perpendicular bisector of two Pnts. Works in any dimension. The
     * coefficients are returned as a DelaunayVertexD of one higher dimension
     * (e.g., (A,B,C,D) for an equation of the form Ax + By + Cz + D = 0).
     * 
     * @param point
     *            the other point
     * @return the coefficients of the perpendicular bisector
     */
    public DelaunayVertexD bisector(DelaunayVertexD point) {
        dimCheck(point);
        DelaunayVertexD diff = this.subtract(point);
        DelaunayVertexD sum = this.add(point);
        double dot = diff.dot(sum);
        return diff.extend(-dot / 2);
    }

    /**
     * @return the specified coordinate of this DelaunayVertexD
     * @throws ArrayIndexOutOfBoundsException
     *             for bad coordinate
     */
    public double coord(int i) {
        return this.coordinates[i];
    }

    /**
     * Check that dimensions match.
     * 
     * @param p
     *            the DelaunayVertexD to check (against this DelaunayVertexD)
     * @return the dimension of the Pnts
     * @throws IllegalArgumentException
     *             if dimension fail to match
     */
    public int dimCheck(DelaunayVertexD p) {
        int len = this.coordinates.length;
        if (len != p.coordinates.length) {
            throw new IllegalArgumentException("Dimension mismatch");
        }
        return len;
    }

    /**
     * @return this DelaunayVertexD's dimension.
     */
    public int dimension() {
        return coordinates.length;
    }

    /* Pnts as matrices */

    /**
     * Dot product.
     * 
     * @param p
     *            the other DelaunayVertexD
     * @return dot product of this DelaunayVertexD and p
     */
    public double dot(DelaunayVertexD p) {
        int len = dimCheck(p);
        double sum = 0;
        for (int i = 0; i < len; i++) {
            sum += this.coordinates[i] * p.coordinates[i];
        }
        return sum;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof DelaunayVertexD)) {
            return false;
        }
        DelaunayVertexD p = (DelaunayVertexD) other;
        if (this.coordinates.length != p.coordinates.length) {
            return false;
        }
        for (int i = 0; i < this.coordinates.length; i++) {
            if (this.coordinates[i] != p.coordinates[i]) {
                return false;
            }
        }
        return true;
    }

    /**
     * Create a new DelaunayVertexD by adding additional coordinates to this
     * DelaunayVertexD.
     * 
     * @param coords
     *            the new coordinates (added on the right end)
     * @return a new DelaunayVertexD with the additional coordinates
     */
    public DelaunayVertexD extend(double... coords) {
        double[] result = new double[coordinates.length + coords.length];
        System.arraycopy(coordinates, 0, result, 0, coordinates.length);
        System.arraycopy(coords, 0, result, coordinates.length, coords.length);
        return new DelaunayVertexD(result);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        for (double c : this.coordinates) {
            long bits = Double.doubleToLongBits(c);
            hash = (31 * hash) ^ (int) (bits ^ (bits >> 32));
        }
        return hash;
    }

    /* Pnts as simplices */

    /**
     * Test if this DelaunayVertexD is inside a simplex.
     * 
     * @param simplex
     *            the simplex (an arary of Pnts)
     * @return true iff this DelaunayVertexD is inside simplex.
     */
    public boolean isInside(DelaunayVertexD[] simplex) {
        int[] result = this.relation(simplex);
        for (int r : result) {
            if (r >= 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Test if this DelaunayVertexD is on a simplex.
     * 
     * @param simplex
     *            the simplex (an array of Pnts)
     * @return the simplex DelaunayVertexD that "witnesses" on-ness (or null if
     *         not on)
     */
    public DelaunayVertexD isOn(DelaunayVertexD[] simplex) {
        int[] result = this.relation(simplex);
        DelaunayVertexD witness = null;
        for (int i = 0; i < result.length; i++) {
            if (result[i] == 0) {
                witness = simplex[i];
            } else if (result[i] > 0) {
                return null;
            }
        }
        return witness;
    }

    /**
     * Test if this DelaunayVertexD is outside of simplex.
     * 
     * @param simplex
     *            the simplex (an array of Pnts)
     * @return simplex DelaunayVertexD that "witnesses" outsideness (or null if
     *         not outside)
     */
    public DelaunayVertexD isOutside(DelaunayVertexD[] simplex) {
        int[] result = this.relation(simplex);
        for (int i = 0; i < result.length; i++) {
            if (result[i] > 0) {
                return simplex[i];
            }
        }
        return null;
    }

    /**
     * Magnitude (as a vector).
     * 
     * @return the Euclidean length of this vector
     */
    public double magnitude() {
        return Math.sqrt(this.dot(this));
    }

    /**
     * Relation between this DelaunayVertexD and a simplex (represented as an
     * array of Pnts). Result is an array of signs, one for each vertex of the
     * simplex, indicating the relation between the vertex, the vertex's
     * opposite facet, and this DelaunayVertexD.
     * 
     * <pre>
     *   -1 means DelaunayVertexD is on same side of facet
     *    0 means DelaunayVertexD is on the facet
     *   +1 means DelaunayVertexD is on opposite side of facet
     * </pre>
     * 
     * @param simplex
     *            an array of Pnts representing a simplex
     * @return an array of signs showing relation between this DelaunayVertexD
     *         and simplex
     * @throws IllegalArgumentExcpetion
     *             if the simplex is degenerate
     */
    public int[] relation(DelaunayVertexD[] simplex) {
        /*
         * In 2D, we compute the cross of this matrix: 1 1 1 1 p0 a0 b0 c0 p1 a1
         * b1 c1 where (a, b, c) is the simplex and p is this DelaunayVertexD.
         * The result is a vector in which the first coordinate is the signed
         * area (all signed areas are off by the same constant factor) of the
         * simplex and the remaining coordinates are the *negated* signed areas
         * for the simplices in which p is substituted for each of the vertices.
         * Analogous results occur in higher dimensions.
         */
        int dim = simplex.length - 1;
        if (this.dimension() != dim) {
            throw new IllegalArgumentException("Dimension mismatch");
        }

        /* Create and load the matrix */
        DelaunayVertexD[] matrix = new DelaunayVertexD[dim + 1];
        /* First row */
        double[] coords = new double[dim + 2];
        for (int j = 0; j < coords.length; j++) {
            coords[j] = 1;
        }
        matrix[0] = new DelaunayVertexD(coords);
        /* Other rows */
        for (int i = 0; i < dim; i++) {
            coords[0] = this.coordinates[i];
            for (int j = 0; j < simplex.length; j++) {
                coords[j + 1] = simplex[j].coordinates[i];
            }
            matrix[i + 1] = new DelaunayVertexD(coords);
        }

        /* Compute and analyze the vector of areas/volumes/contents */
        DelaunayVertexD vector = cross(matrix);
        double content = vector.coordinates[0];
        int[] result = new int[dim + 1];
        for (int i = 0; i < result.length; i++) {
            double value = vector.coordinates[i + 1];
            if (Math.abs(value) <= 1.0e-6 * Math.abs(content)) {
                result[i] = 0;
            } else if (value < 0) {
                result[i] = -1;
            } else {
                result[i] = 1;
            }
        }
        if (content < 0) {
            for (int i = 0; i < result.length; i++) {
                result[i] = -result[i];
            }
        }
        if (content == 0) {
            for (int i = 0; i < result.length; i++) {
                result[i] = Math.abs(result[i]);
            }
        }
        return result;
    }

    /**
     * Subtract.
     * 
     * @param p
     *            the other DelaunayVertexD
     * @return a new DelaunayVertexD = this - p
     */
    public DelaunayVertexD subtract(DelaunayVertexD p) {
        int len = dimCheck(p);
        double[] coords = new double[len];
        for (int i = 0; i < len; i++) {
            coords[i] = this.coordinates[i] - p.coordinates[i];
        }
        return new DelaunayVertexD(coords);
    }

    @Override
    public String toString() {
        if (coordinates.length == 0) {
            return "DelaunayVertexD()";
        }
        String result = "DelaunayVertexD(" + coordinates[0];
        for (int i = 1; i < coordinates.length; i++) {
            result = result + "," + coordinates[i];
        }
        result = result + ")";
        return result;
    }

    public VecD2D toVecD2D() {
        return new VecD2D(coordinates[0], coordinates[1]);
    }

    /**
     * Test relation between this DelaunayVertexD and circumcircle of a simplex.
     * 
     * @param simplex
     *            the simplex (as an array of Pnts)
     * @return -1, 0, or +1 for inside, on, or outside of circumcircle
     */
    public int vsCircumcircle(DelaunayVertexD[] simplex) {
        DelaunayVertexD[] matrix = new DelaunayVertexD[simplex.length + 1];
        for (int i = 0; i < simplex.length; i++) {
            matrix[i] = simplex[i].extend(1, simplex[i].dot(simplex[i]));
        }
        matrix[simplex.length] = this.extend(1, this.dot(this));
        double d = determinant(matrix);
        int result = (d < 0) ? -1 : ((d > 0) ? +1 : 0);
        if (content(simplex) < 0) {
            result = -result;
        }
        return result;
    }
}