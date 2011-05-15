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

public class LineStrip2D implements Iterable<Vec2D> {

    @XmlElement(name = "v")
    public List<Vec2D> vertices = new ArrayList<Vec2D>();

    protected float[] arcLenIndex;

    public LineStrip2D() {
    }

    public LineStrip2D(Collection<? extends Vec2D> vertices) {
        this.vertices = new ArrayList<Vec2D>(vertices);
    }

    public LineStrip2D add(ReadonlyVec2D p) {
        vertices.add(p.copy());
        return this;
    }

    public LineStrip2D add(Vec2D p) {
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
    public Vec2D get(int i) {
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
    public List<Vec2D> getDecimatedVertices(float step) {
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
    public List<Vec2D> getDecimatedVertices(float step, boolean doAddFinalVertex) {
        ArrayList<Vec2D> uniform = new ArrayList<Vec2D>();
        if (vertices.size() < 3) {
            if (vertices.size() == 2) {
                new Line2D(vertices.get(0), vertices.get(1)).splitIntoSegments(
                        uniform, step, true);
                if (!doAddFinalVertex) {
                    uniform.remove(uniform.size() - 1);
                }
            } else {
                return null;
            }
        }
        float arcLen = getEstimatedArcLength();
        double delta = step / arcLen;
        int currIdx = 0;
        for (double t = 0; t < 1.0; t += delta) {
            double currT = t * arcLen;
            while (currT >= arcLenIndex[currIdx]) {
                currIdx++;
            }
            ReadonlyVec2D p = vertices.get(currIdx - 1);
            ReadonlyVec2D q = vertices.get(currIdx);
            float frac = (float) ((currT - arcLenIndex[currIdx - 1]) / (arcLenIndex[currIdx] - arcLenIndex[currIdx - 1]));
            Vec2D i = p.interpolateTo(q, frac);
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
            ReadonlyVec2D p = vertices.get(i - 1);
            ReadonlyVec2D q = vertices.get(i);
            arcLen += p.distanceTo(q);
            arcLenIndex[i] = arcLen;
        }
        return arcLen;
    }

    public List<Line2D> getSegments() {
        final int num = vertices.size();
        List<Line2D> segments = new ArrayList<Line2D>(num - 1);
        for (int i = 1; i < num; i++) {
            segments.add(new Line2D(vertices.get(i - 1), vertices.get(i)));
        }
        return segments;
    }

    public Iterator<Vec2D> iterator() {
        return vertices.iterator();
    }
}
