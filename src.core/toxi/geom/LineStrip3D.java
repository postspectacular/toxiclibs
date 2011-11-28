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
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class LineStrip3D implements Iterable<Vec3D> {

    @XmlElement(name = "v")
    protected List<Vec3D> vertices = new ArrayList<Vec3D>();

    protected float[] arcLenIndex;

    public LineStrip3D() {
    }

    public LineStrip3D(Collection<? extends Vec3D> vertices) {
        this.vertices = new ArrayList<Vec3D>(vertices);
    }

    public LineStrip3D add(float x, float y, float z) {
        vertices.add(new Vec3D(x, y, z));
        return this;
    }

    public LineStrip3D add(ReadonlyVec3D p) {
        vertices.add(p.copy());
        return this;
    }

    public LineStrip3D add(Vec3D p) {
        vertices.add(p);
        return this;
    }

    /**
     * Returns the vertex at the given index. This function follows Python
     * convention, in that if the index is negative, it is considered relative
     * to the list end. Therefore the vertex at index -1 is the last vertex in
     * the list.
     * 
     * @param i
     *            index
     * @return vertex
     */
    public Vec3D get(int i) {
        if (i < 0) {
            i += vertices.size();
        }
        return vertices.get(i);
    }

    /**
     * Computes a list of points along the spline which are uniformly separated
     * by the given step distance.
     * 
     * @param step
     * @return point list
     */
    public List<Vec3D> getDecimatedVertices(float step) {
        return getDecimatedVertices(step, true);
    }

    /**
     * Computes a list of points along the spline which are close to uniformly
     * separated by the given step distance. The uniform distribution is only an
     * approximation and is based on the estimated arc length of the polyline.
     * The distance between returned points might vary in places, especially if
     * there're sharp angles between line segments.
     * 
     * @param step
     * @param doAddFinalVertex
     *            true, if the last vertex computed should be added regardless
     *            of its distance.
     * @return point list
     */
    public List<Vec3D> getDecimatedVertices(float step, boolean doAddFinalVertex) {
        ArrayList<Vec3D> uniform = new ArrayList<Vec3D>();
        if (vertices.size() < 3) {
            if (vertices.size() == 2) {
                new Line3D(vertices.get(0), vertices.get(1)).splitIntoSegments(
                        uniform, step, true);
                if (!doAddFinalVertex) {
                    uniform.remove(uniform.size() - 1);
                }
            } else {
                return null;
            }
        }
        float arcLen = getEstimatedArcLength();
        double delta = (double) step / arcLen;
        int currIdx = 0;
        for (double t = 0; t < 1.0; t += delta) {
            double currT = t * arcLen;
            while (currT >= arcLenIndex[currIdx]) {
                currIdx++;
            }
            ReadonlyVec3D p = vertices.get(currIdx - 1);
            ReadonlyVec3D q = vertices.get(currIdx);
            float frac = (float) ((currT - arcLenIndex[currIdx - 1]) / (arcLenIndex[currIdx] - arcLenIndex[currIdx - 1]));
            Vec3D i = p.interpolateTo(q, frac);
            uniform.add(i);
        }
        if (doAddFinalVertex) {
            uniform.add(vertices.get(vertices.size() - 1).copy());
        }
        return uniform;
    }

    public float getEstimatedArcLength() {
        if (arcLenIndex == null
                || (arcLenIndex != null && arcLenIndex.length != vertices
                        .size())) {
            arcLenIndex = new float[vertices.size()];
        }
        float arcLen = 0;
        for (int i = 1; i < arcLenIndex.length; i++) {
            ReadonlyVec3D p = vertices.get(i - 1);
            ReadonlyVec3D q = vertices.get(i);
            arcLen += p.distanceTo(q);
            arcLenIndex[i] = arcLen;
        }
        return arcLen;
    }

    public List<Line3D> getSegments() {
        final int num = vertices.size();
        List<Line3D> segments = new ArrayList<Line3D>(num - 1);
        for (int i = 1; i < num; i++) {
            segments.add(new Line3D(vertices.get(i - 1), vertices.get(i)));
        }
        return segments;
    }

    /**
     * @return the vertices
     */
    public List<Vec3D> getVertices() {
        return vertices;
    }

    public Iterator<Vec3D> iterator() {
        return vertices.iterator();
    }

    /**
     * @param vertices
     *            the vertices to set
     */
    public void setVertices(List<Vec3D> vertices) {
        this.vertices = vertices;
    }
}
