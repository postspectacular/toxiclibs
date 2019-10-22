package toxi.geom.mesh2d;

/*
 * Copyright (c) 2007 by L. Paul Chew.
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

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

import toxi.util.datatypes.ArraySet;

/**
 * A DelaunayTriangleD is an immutable Set of exactly three Pnts.
 * 
 * All Set operations are available. Individual vertices can be accessed via
 * iterator() and also via triangle.get(index).
 * 
 * Note that, even if two triangles have the same vertex set, they are
 * *different* triangles. Methods equals() and hashCode() are consistent with
 * this rule.
 * 
 * @author Paul Chew
 * 
 *         Created December 2007. Replaced general simplices with geometric
 *         triangle.
 * 
 */
public class DelaunayTriangleD extends ArraySet<DelaunayVertexD> {

    private int idNumber; // The id number
    private DelaunayVertexD circumcenter = null; // The triangle's circumcenter

    private static int idGenerator = 0; // Used to create id numbers
    public static boolean moreInfo = false; // True if more info in toString

    /**
     * @param collection
     *            a Collection holding the Simplex vertices
     * @throws IllegalArgumentException
     *             if there are not three distinct vertices
     */
    public DelaunayTriangleD(Collection<? extends DelaunayVertexD> collection) {
        super(collection);
        idNumber = idGenerator++;
        if (this.size() != 3) {
            throw new IllegalArgumentException(
                    "DelaunayTriangleD must have 3 vertices");
        }
    }

    /**
     * @param vertices
     *            the vertices of the DelaunayTriangleD.
     * @throws IllegalArgumentException
     *             if there are not three distinct vertices
     */
    public DelaunayTriangleD(DelaunayVertexD... vertices) {
        this(Arrays.asList(vertices));
    }

    @Override
    public boolean add(DelaunayVertexD vertex) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean equals(Object o) {
        return (this == o);
    }

    /**
     * Report the facet opposite vertex.
     * 
     * @param vertex
     *            a vertex of this DelaunayTriangleD
     * @return the facet opposite vertex
     * @throws IllegalArgumentException
     *             if the vertex is not in triangle
     */
    public ArraySet<DelaunayVertexD> facetOpposite(DelaunayVertexD vertex) {
        ArraySet<DelaunayVertexD> facet = new ArraySet<DelaunayVertexD>(this);
        if (!facet.remove(vertex)) {
            throw new IllegalArgumentException("Vertex not in triangle");
        }
        return facet;
    }

    /**
     * @return the triangle's circumcenter
     */
    public DelaunayVertexD getCircumcenter() {
        if (circumcenter == null) {
            circumcenter = DelaunayVertexD.circumcenter(this
                    .toArray(new DelaunayVertexD[0]));
        }
        return circumcenter;
    }

    /**
     * Get arbitrary vertex of this triangle, but not any of the bad vertices.
     * 
     * @param badVertices
     *            one or more bad vertices
     * @return a vertex of this triangle, but not one of the bad vertices
     * @throws NoSuchElementException
     *             if no vertex found
     */
    public DelaunayVertexD getVertexButNot(DelaunayVertexD... badVertices) {
        Collection<DelaunayVertexD> bad = Arrays.asList(badVertices);
        for (DelaunayVertexD v : this) {
            if (!bad.contains(v)) {
                return v;
            }
        }
        throw new NoSuchElementException("No vertex found");
    }

    /* The following two methods ensure that a DelaunayTriangleD is immutable */

    @Override
    public int hashCode() {
        return (idNumber ^ (idNumber >>> 32));
    }

    /**
     * True iff triangles are neighbors. Two triangles are neighbors if they
     * share a facet.
     * 
     * @param triangle
     *            the other DelaunayTriangleD
     * @return true iff this DelaunayTriangleD is a neighbor of triangle
     */
    public boolean isNeighbor(DelaunayTriangleD triangle) {
        int count = 0;
        for (DelaunayVertexD vertex : this) {
            if (!triangle.contains(vertex)) {
                count++;
            }
        }
        return count == 1;
    }

    /* The following two methods ensure that all triangles are different. */

    @Override
    public Iterator<DelaunayVertexD> iterator() {
        return new Iterator<DelaunayVertexD>() {

            private Iterator<DelaunayVertexD> it = DelaunayTriangleD.super
                    .iterator();

            public boolean hasNext() {
                return it.hasNext();
            }

            public DelaunayVertexD next() {
                return it.next();
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override
    public String toString() {
        if (!moreInfo) {
            return "DelaunayTriangleD" + idNumber;
        }
        return "DelaunayTriangleD" + idNumber + super.toString();
    }

}