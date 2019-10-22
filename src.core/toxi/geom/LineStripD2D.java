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

import toxi.geom.LineD2D.LineIntersection;
import toxi.geom.LineD2D.LineIntersection.Type;
import toxi.math.MathUtils;

public class LineStripD2D implements Iterable<VecD2D> {

    @XmlElement(name = "v")
    protected List<VecD2D> vertices = new ArrayList<VecD2D>();

    protected double[] arcLenIndex;

    public LineStripD2D() {
    }

    public LineStripD2D(Collection<? extends VecD2D> vertices) {
        this.vertices = new ArrayList<VecD2D>(vertices);
    }

    public LineStripD2D add(double x, double y) {
        vertices.add(new VecD2D(x, y));
        return this;
    }

    public LineStripD2D add(ReadonlyVecD2D p) {
        vertices.add(p.copy());
        return this;
    }

    public LineStripD2D add(VecD2D p) {
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
    public VecD2D get(int i) {
        if (i < 0) {
            i += vertices.size();
        }
        return vertices.get(i);
    }

    public CircleD getBoundingCircleD() {
        return CircleD.newBoundingCircleD(vertices);
    }

    public RectD getBounds() {
        return RectD.getBoundingRectD(vertices);
    }

    public VecD2D getCentroid() {
        int num = vertices.size();
        if (num > 0) {
            VecD2D centroid = new VecD2D();
            for (VecD2D v : vertices) {
                centroid.addSelf(v);
            }
            return centroid.scaleSelf(1f / num);
        }
        return null;
    }

    /**
     * Computes a list of points along the spline which are uniformly separated
     * by the given step distance.
     * 
     * @param step
     * @return point list
     */
    public List<VecD2D> getDecimatedVertices(double step) {
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
    public List<VecD2D> getDecimatedVertices(double step, boolean doAddFinalVertex) {
        ArrayList<VecD2D> uniform = new ArrayList<VecD2D>();
        if (vertices.size() < 3) {
            if (vertices.size() == 2) {
                new LineD2D(vertices.get(0), vertices.get(1)).splitIntoSegments(
                        uniform, step, true);
                if (!doAddFinalVertex) {
                    uniform.remove(uniform.size() - 1);
                }
            } else {
                return null;
            }
        }
        double arcLen = getLength();
        if (arcLen > 0) {
            double delta = step / arcLen;
            int currIdx = 0;
            for (double t = 0; t < 1.0; t += delta) {
                double currT = t * arcLen;
                while (currT >= arcLenIndex[currIdx]) {
                    currIdx++;
                }
                ReadonlyVecD2D p = vertices.get(currIdx - 1);
                ReadonlyVecD2D q = vertices.get(currIdx);
                double frac = ((currT - arcLenIndex[currIdx - 1]) / (arcLenIndex[currIdx] - arcLenIndex[currIdx - 1]));
                VecD2D i = p.interpolateTo(q, frac);
                uniform.add(i);
            }
            if (doAddFinalVertex) {
                uniform.add(vertices.get(vertices.size() - 1).copy());
            }
        }
        return uniform;
    }

    /**
     * Returns a list of {@link Line2D} segments representing the segments
     * between the vertices of this strip.
     * 
     * @return list of lines
     */
    public List<LineD2D> getEdges() {
        int num = vertices.size();
        List<LineD2D> edges = new ArrayList<LineD2D>(num - 1);
        for (int i = 1; i < num; i++) {
            edges.add(new LineD2D(vertices.get(i - 1), vertices.get(i)));
        }
        return edges;
    }

    public double getLength() {
        if (arcLenIndex == null
                || (arcLenIndex != null && arcLenIndex.length != vertices
                        .size())) {
            arcLenIndex = new double[vertices.size()];
        }
        double arcLen = 0;
        for (int i = 1; i < arcLenIndex.length; i++) {
            ReadonlyVecD2D p = vertices.get(i - 1);
            ReadonlyVecD2D q = vertices.get(i);
            arcLen += p.distanceTo(q);
            arcLenIndex[i] = arcLen;
        }
        return arcLen;
    }

    /**
     * Computes point at position t, where t is the normalized position along
     * the strip. If t&lt;0 then the first vertex of the strip is returned. If
     * t&gt;=1.0 the last vertex is returned. If the strip contains less than 2
     * vertices, this method returns null.
     * 
     * @param t
     * @return
     */
    public VecD2D getPointAt(double t) {
        int num = vertices.size();
        if (num > 1) {
            if (t <= 0.0) {
                return vertices.get(0);
            } else if (t >= 1.0) {
                return vertices.get(num - 1);
            }
            double totalLength = this.getLength();
            double offp = 0, offq = 0;
            for (int i = 1; i < num; i++) {
                VecD2D p = vertices.get(i - 1);
                VecD2D q = vertices.get(i);
                offq += q.distanceTo(p) / totalLength;
                if (offp <= t && offq >= t) {
                    return p.interpolateTo(q, MathUtils.mapInterval(t,
                            offp, offq, 0.0, 1.0));
                }
                offp = offq;
            }
        }
        return null;
    }

    public List<LineD2D> getSegments() {
        final int num = vertices.size();
        List<LineD2D> segments = new ArrayList<LineD2D>(num - 1);
        for (int i = 1; i < num; i++) {
            segments.add(new LineD2D(vertices.get(i - 1), vertices.get(i)));
        }
        return segments;
    }

    /**
     * @return the vertices
     */
    public List<VecD2D> getVertices() {
        return vertices;
    }

    public LineIntersection intersectLine(LineD2D line) {
        LineD2D l = new LineD2D(new VecD2D(), new VecD2D());
        for (int i = 1, num = vertices.size(); i < num; i++) {
            l.set(vertices.get(i - 1), vertices.get(i));
            LineIntersection isec = l.intersectLine(line);
            if (isec.getType() == Type.INTERSECTING
                    || isec.getType() == Type.COINCIDENT) {
                return isec;
            }
        }
        return null;
    }

    public Iterator<VecD2D> iterator() {
        return vertices.iterator();
    }

    public LineStripD2D rotate(double theta) {
        for (VecD2D v : vertices) {
            v.rotate(theta);
        }
        return this;
    }

    public LineStripD2D scale(double scale) {
        return scale(scale, scale);
    }

    public LineStripD2D scale(double x, double y) {
        for (VecD2D v : vertices) {
            v.scaleSelf(x, y);
        }
        return this;
    }

    public LineStripD2D scale(ReadonlyVecD2D scale) {
        return scale(scale.x(), scale.y());
    }

    public LineStripD2D scaleSize(double scale) {
        return scaleSize(scale, scale);
    }

    public LineStripD2D scaleSize(double x, double y) {
        VecD2D centroid = getCentroid();
        for (VecD2D v : vertices) {
            v.subSelf(centroid).scaleSelf(x, y).addSelf(centroid);
        }
        return this;
    }

    public LineStripD2D scaleSize(ReadonlyVecD2D scale) {
        return scaleSize(scale.x(), scale.y());
    }

    /**
     * @param vertices
     *            the vertices to set
     */
    public void setVertices(List<VecD2D> vertices) {
        this.vertices = vertices;
    }

    public LineStripD2D translate(double x, double y) {
        for (VecD2D v : vertices) {
            v.addSelf(x, y);
        }
        return this;
    }

    public LineStripD2D translate(ReadonlyVecD2D offset) {
        return translate(offset.x(), offset.y());
    }
}
