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

import toxi.geom.Line2D.LineIntersection;
import toxi.geom.Line2D.LineIntersection.Type;
import toxi.math.MathUtils;

public class LineStrip2D implements Iterable<Vec2D> {

    @XmlElement(name = "v")
    protected List<Vec2D> vertices = new ArrayList<Vec2D>();

    protected float[] arcLenIndex;

    public LineStrip2D() {
    }

    public LineStrip2D(Collection<? extends Vec2D> vertices) {
        this.vertices = new ArrayList<Vec2D>(vertices);
    }

    public LineStrip2D add(float x, float y) {
        vertices.add(new Vec2D(x, y));
        return this;
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

    public Circle getBoundingCircle() {
        return Circle.newBoundingCircle(vertices);
    }

    public Rect getBounds() {
        return Rect.getBoundingRect(vertices);
    }

    public Vec2D getCentroid() {
        int num = vertices.size();
        if (num > 0) {
            Vec2D centroid = new Vec2D();
            for (Vec2D v : vertices) {
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
        float arcLen = getLength();
        if (arcLen > 0) {
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
        }
        return uniform;
    }

    /**
     * Returns a list of {@link Line2D} segments representing the segments
     * between the vertices of this strip.
     * 
     * @return list of lines
     */
    public List<Line2D> getEdges() {
        int num = vertices.size();
        List<Line2D> edges = new ArrayList<Line2D>(num - 1);
        for (int i = 1; i < num; i++) {
            edges.add(new Line2D(vertices.get(i - 1), vertices.get(i)));
        }
        return edges;
    }

    public float getLength() {
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

    /**
     * Computes point at position t, where t is the normalized position along
     * the strip. If t&lt;0 then the first vertex of the strip is returned. If
     * t&gt;=1.0 the last vertex is returned. If the strip contains less than 2
     * vertices, this method returns null.
     * 
     * @param t
     * @return
     */
    public Vec2D getPointAt(float t) {
        int num = vertices.size();
        if (num > 1) {
            if (t <= 0.0) {
                return vertices.get(0);
            } else if (t >= 1.0) {
                return vertices.get(num - 1);
            }
            float totalLength = this.getLength();
            double offp = 0, offq = 0;
            for (int i = 1; i < num; i++) {
                Vec2D p = vertices.get(i - 1);
                Vec2D q = vertices.get(i);
                offq += q.distanceTo(p) / totalLength;
                if (offp <= t && offq >= t) {
                    return p.interpolateTo(q, (float) MathUtils.mapInterval(t,
                            offp, offq, 0.0, 1.0));
                }
                offp = offq;
            }
        }
        return null;
    }

    public List<Line2D> getSegments() {
        final int num = vertices.size();
        List<Line2D> segments = new ArrayList<Line2D>(num - 1);
        for (int i = 1; i < num; i++) {
            segments.add(new Line2D(vertices.get(i - 1), vertices.get(i)));
        }
        return segments;
    }

    /**
     * @return the vertices
     */
    public List<Vec2D> getVertices() {
        return vertices;
    }

    public LineIntersection intersectLine(Line2D line) {
        Line2D l = new Line2D(new Vec2D(), new Vec2D());
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

    public Iterator<Vec2D> iterator() {
        return vertices.iterator();
    }

    public LineStrip2D rotate(float theta) {
        for (Vec2D v : vertices) {
            v.rotate(theta);
        }
        return this;
    }

    public LineStrip2D scale(float scale) {
        return scale(scale, scale);
    }

    public LineStrip2D scale(float x, float y) {
        for (Vec2D v : vertices) {
            v.scaleSelf(x, y);
        }
        return this;
    }

    public LineStrip2D scale(ReadonlyVec2D scale) {
        return scale(scale.x(), scale.y());
    }

    public LineStrip2D scaleSize(float scale) {
        return scaleSize(scale, scale);
    }

    public LineStrip2D scaleSize(float x, float y) {
        Vec2D centroid = getCentroid();
        for (Vec2D v : vertices) {
            v.subSelf(centroid).scaleSelf(x, y).addSelf(centroid);
        }
        return this;
    }

    public LineStrip2D scaleSize(ReadonlyVec2D scale) {
        return scaleSize(scale.x(), scale.y());
    }

    /**
     * @param vertices
     *            the vertices to set
     */
    public void setVertices(List<Vec2D> vertices) {
        this.vertices = vertices;
    }

    public LineStrip2D translate(float x, float y) {
        for (Vec2D v : vertices) {
            v.addSelf(x, y);
        }
        return this;
    }

    public LineStrip2D translate(ReadonlyVec2D offset) {
        return translate(offset.x(), offset.y());
    }
}
