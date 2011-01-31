/*
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
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import toxi.geom.Line2D.LineIntersection;
import toxi.geom.Line2D.LineIntersection.Type;
import toxi.geom.mesh.Mesh3D;
import toxi.geom.mesh.TriangleMesh;
import toxi.math.MathUtils;

/**
 * Container type for convex polygons. Implements {@link Shape2D}.
 */
public class Polygon2D implements Shape2D {

    public List<Vec2D> vertices = new ArrayList<Vec2D>();

    public Polygon2D() {
    }

    public Polygon2D(List<Vec2D> points) {
        for (Vec2D p : points) {
            add(p.copy());
        }
    }

    public Polygon2D(Vec2D... points) {
        for (Vec2D p : points) {
            add(p.copy());
        }
    }

    /**
     * Adds a new vertex to the polygon (builder pattern).
     * 
     * @param p
     *            vertex point to add
     * @return itself
     */
    public Polygon2D add(Vec2D p) {
        if (!vertices.contains(p)) {
            vertices.add(p);
        }
        return this;
    }

    public boolean containsPoint(ReadonlyVec2D p) {
        int num = vertices.size();
        int i, j = num - 1;
        boolean oddNodes = false;
        float px = p.x();
        float py = p.y();
        for (i = 0; i < num; i++) {
            Vec2D vi = vertices.get(i);
            Vec2D vj = vertices.get(j);
            if (vi.y < py && vj.y >= py || vj.y < py && vi.y >= py) {
                if (vi.x + (py - vi.y) / (vj.y - vi.y) * (vj.x - vi.x) < px) {
                    oddNodes = !oddNodes;
                }
            }
            j = i;
        }
        return oddNodes;
    }

    /**
     * Flips the ordering of the polygon's vertices.
     * 
     * @return itself
     */
    public Polygon2D flipVertexOrder() {
        Collections.reverse(vertices);
        return this;
    }

    /**
     * Computes the area of the polygon, provided it isn't self intersecting.
     * Code ported from:
     * http://local.wasp.uwa.edu.au/~pbourke/geometry/polyarea/
     * 
     * @return polygon area
     */
    public float getArea() {
        float area = 0;
        int numPoints = vertices.size();
        for (int i = 0; i < numPoints; i++) {
            Vec2D a = vertices.get(i);
            Vec2D b = vertices.get((i + 1) % numPoints);
            area += a.x * b.y;
            area -= a.y * b.x;
        }
        area *= 0.5f;
        return area;
    }

    /**
     * Computes the polygon's centre of mass. Code ported from:
     * http://local.wasp.uwa.edu.au/~pbourke/geometry/polyarea/
     * 
     * @return centroid point
     */
    public Vec2D getCentroid() {
        Vec2D res = new Vec2D();
        int numPoints = vertices.size();
        for (int i = 0; i < numPoints; i++) {
            Vec2D a = vertices.get(i);
            Vec2D b = vertices.get((i + 1) % numPoints);
            float factor = a.x * b.y - b.x * a.y;
            res.x += (a.x + b.x) * factor;
            res.y += (a.y + b.y) * factor;
        }
        return res.scale(1f / (getArea() * 6));
    }

    public float getCircumference() {
        float circ = 0;
        for (int i = 0, num = vertices.size(); i < num; i++) {
            circ += vertices.get(i).distanceTo(vertices.get((i + 1) % num));
        }
        return circ;
    }

    /**
     * Returns the number of polygon vertices.
     * 
     * @return vertex count
     */
    public int getNumPoints() {
        return vertices.size();
    }

    /**
     * Checks if the vertices of this polygon are in clockwise ordering by
     * examining the first 3.
     * 
     * @return true, if clockwise
     */
    public boolean isClockwise() {
        if (vertices.size() > 2) {
            return Triangle2D.isClockwise(vertices.get(0), vertices.get(1),
                    vertices.get(2));
        }
        return false;
    }

    /**
     * Given the sequentially connected points p1, p2, p3, this function returns
     * a bevel-offset replacement for point p2.
     * 
     * Note: If vectors p1->p2 and p2->p3 are exactly 180 degrees opposed, or if
     * either segment is zero then no offset will be applied.
     * 
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param x3
     * @param y3
     * @param distance
     * @param out
     * 
     * @see http://alienryderflex.com/polygon_inset/
     */
    protected void offsetCorner(float x1, float y1, float x2, float y2,
            float x3, float y3, float distance, Vec2D out) {

        float c1 = x2, d1 = y2, c2 = x2, d2 = y2;
        float dx1, dy1, dist1, dx2, dy2, dist2, insetX, insetY;

        dx1 = x2 - x1;
        dy1 = y2 - y1;
        dist1 = (float) Math.sqrt(dx1 * dx1 + dy1 * dy1);
        dx2 = x3 - x2;
        dy2 = y3 - y2;
        dist2 = (float) Math.sqrt(dx2 * dx2 + dy2 * dy2);

        if (dist1 < MathUtils.EPS || dist2 < MathUtils.EPS) {
            return;
        }
        dist1 = distance / dist1;
        dist2 = distance / dist2;

        insetX = dy1 * dist1;
        insetY = -dx1 * dist1;
        x1 += insetX;
        c1 += insetX;
        y1 += insetY;
        d1 += insetY;
        insetX = dy2 * dist2;
        insetY = -dx2 * dist2;
        x3 += insetX;
        c2 += insetX;
        y3 += insetY;
        d2 += insetY;

        if (c1 == c2 && d1 == d2) {
            out.set(c1, d1);
            return;
        }

        Line2D l1 = new Line2D(new Vec2D(x1, y1), new Vec2D(c1, d1));
        Line2D l2 = new Line2D(new Vec2D(c2, d2), new Vec2D(x3, y3));
        LineIntersection isec = l1.intersectLine(l2);
        final Vec2D ipos = isec.getPos();
        if (ipos != null) {
            out.set(ipos);
        }
    }

    /**
     * Moves each line segment of the polygon in/outward perpendicular by the
     * given distance. New line segments and polygon vertices are created by
     * computing the intersection points of the displaced segments. Choosing an
     * too large displacement amount will result in deformation/undefined
     * behavior with various self intersections. Should that happen, please try
     * to clean up the shape using the {@link #toOutline()} method.
     * 
     * @param distance
     *            offset/inset distance (negative for inset)
     * @return itself
     */
    public Polygon2D offsetShape(float distance) {
        int corners = vertices.size();
        if (corners > 2) {
            int i;
            float a, b, c, d, e, f;
            float startX = vertices.get(0).x;
            float startY = vertices.get(0).y;

            c = vertices.get(corners - 1).x;
            d = vertices.get(corners - 1).y;
            e = startX;
            f = startY;
            for (i = 0; i < corners - 1; i++) {
                a = c;
                b = d;
                c = e;
                d = f;
                e = vertices.get(i + 1).x;
                f = vertices.get(i + 1).y;
                offsetCorner(a, b, c, d, e, f, distance, vertices.get(i));
            }
            offsetCorner(c, d, e, f, startX, startY, distance, vertices.get(i));
        }
        return this;
    }

    /**
     * Applies a laplacian-style smooth operation to all polygon vertices,
     * causing sharp corners/angles to widen and results in a general smoother
     * shape. Let the current vertex be A and its neighbours P and Q, then A
     * will be moved by a specified amount into the direction given by
     * (P-A)+(Q-A). Additionally, and to avoid shrinking of the shape through
     * repeated iteration of this procedure, the vector A - C (Polygon centroid)
     * is added as counter force and a weight for its impact can be specified.
     * To keep the average size of the polygon stable, this weight value should
     * be ~1/2 of the smooth amount.
     * 
     * @param amount
     *            smooth amount (between 0 < x < 0.5)
     * @param baseWeight
     *            counter weight (0 <= x < 1/2 * smooth amount)
     * @return itself
     */
    public Polygon2D smooth(float amount, float baseWeight) {
        Vec2D centroid = getCentroid();
        int num = vertices.size();
        List<Vec2D> filtered = new ArrayList<Vec2D>(num);
        for (int i = 0, j = num - 1, k = 1; i < num; i++) {
            Vec2D a = vertices.get(i);
            Vec2D dir =
                    vertices.get(j).sub(a).addSelf(vertices.get(k).sub(a))
                            .addSelf(a.sub(centroid).scaleSelf(baseWeight));
            filtered.add(a.add(dir.scaleSelf(amount)));
            j++;
            if (j == num) {
                j = 0;
            }
            k++;
            if (k == num) {
                k = 0;
            }
        }
        vertices.clear();
        vertices.addAll(filtered);
        return this;
    }

    public Mesh3D toMesh(Mesh3D mesh) {
        if (mesh == null) {
            mesh = new TriangleMesh();
        }
        final int num = vertices.size();
        final Vec3D centroid = getCentroid().to3DXY();
        for (int i = 1; i <= num; i++) {
            mesh.addFace(centroid, vertices.get(i % num).to3DXY(), vertices
                    .get(i - 1).to3DXY());
        }
        return mesh;
    }

    /**
     * Attempts to remove all internal self-intersections and creates a new
     * polygon only consisting of perimeter vertices.
     * 
     * @return true, if process completed succcessfully.
     * 
     * @see http://alienryderflex.com/polygon_perimeter/
     */
    public boolean toOutline() {
        List<Vec2D> newVerts = new ArrayList<Vec2D>();
        int corners = vertices.size();
        int maxSegs = corners * 3;
        Vec2D[] segments = new Vec2D[maxSegs];
        Vec2D[] segEnds = new Vec2D[maxSegs];
        float[] segAngles = new float[maxSegs];
        Vec2D start = vertices.get(0).copy();
        float lastAngle = MathUtils.PI;
        float a, b, c, d, e, f, angleDif, bestAngleDif;
        int i, j = corners - 1, segs = 0;

        if (corners > maxSegs) {
            return false;
        }

        // 1,3. Reformulate the polygon as a set of line segments, and choose a
        // starting point that must be on the perimeter.
        for (i = 0; i < corners; i++) {
            Vec2D pi = vertices.get(i);
            Vec2D pj = vertices.get(j);
            if (!pi.equals(pj)) {
                segments[segs] = pi;
                segEnds[segs++] = pj;
            }
            j = i;
            if (pi.y > start.y || (pi.y == start.y && pi.x < start.x)) {
                start.set(pi);
            }
        }
        if (segs == 0) {
            return false;
        }

        // 2. Break the segments up at their intersection points.
        for (i = 0; i < segs - 1; i++) {
            for (j = i + 1; j < segs; j++) {
                Line2D li = new Line2D(segments[i], segEnds[i]);
                Line2D lj = new Line2D(segments[j], segEnds[j]);
                LineIntersection isec = li.intersectLine(lj);
                if (isec.getType() == Type.INTERSECTING) {
                    Vec2D ipos = isec.getPos();
                    if (!ipos.equals(segments[i]) && !ipos.equals(segEnds[i])) {
                        if (segs == maxSegs) {
                            return false;
                        }
                        segments[segs] = segments[i].copy();
                        segEnds[segs++] = ipos.copy();
                        segments[i] = ipos.copy();
                    }
                    if (!ipos.equals(segments[j]) && !ipos.equals(segEnds[j])) {
                        if (segs == maxSegs) {
                            return false;
                        }
                        segments[segs] = segments[j].copy();
                        segEnds[segs++] = ipos.copy();
                        segments[j] = ipos.copy();
                    }
                }
            }
        }

        // Calculate the angle of each segment.
        for (i = 0; i < segs; i++) {
            segAngles[i] = segEnds[i].sub(segments[i]).positiveHeading();
        }

        // 4. Build the perimeter polygon.
        c = start.x;
        d = start.y;
        a = c - 1;
        b = d;
        e = 0;
        f = 0;
        newVerts.add(new Vec2D(c, d));
        corners = 1;
        while (true) {
            bestAngleDif = MathUtils.TWO_PI;
            for (i = 0; i < segs; i++) {
                if (segments[i].x == c && segments[i].y == d
                        && (segEnds[i].x != a || segEnds[i].y != b)) {
                    angleDif = lastAngle - segAngles[i];
                    while (angleDif >= MathUtils.TWO_PI) {
                        angleDif -= MathUtils.TWO_PI;
                    }
                    while (angleDif < 0) {
                        angleDif += MathUtils.TWO_PI;
                    }
                    if (angleDif < bestAngleDif) {
                        bestAngleDif = angleDif;
                        e = segEnds[i].x;
                        f = segEnds[i].y;
                    }
                }
                if (segEnds[i].x == c && segEnds[i].y == d
                        && (segments[i].x != a || segments[i].y != b)) {
                    angleDif = lastAngle - segAngles[i] + MathUtils.PI;
                    while (angleDif >= MathUtils.TWO_PI) {
                        angleDif -= MathUtils.TWO_PI;
                    }
                    while (angleDif < 0) {
                        angleDif += MathUtils.TWO_PI;
                    }
                    if (angleDif < bestAngleDif) {
                        bestAngleDif = angleDif;
                        e = segments[i].x;
                        f = segments[i].y;
                    }
                }
            }
            if (corners > 1 && c == newVerts.get(0).x && d == newVerts.get(0).y
                    && e == newVerts.get(1).x && f == newVerts.get(1).y) {
                corners--;
                vertices = newVerts;
                return true;
            }
            if (bestAngleDif == MathUtils.TWO_PI || corners == maxSegs) {
                return false;
            }
            lastAngle -= bestAngleDif + MathUtils.PI;
            newVerts.add(new Vec2D(e, f));
            corners++;
            a = c;
            b = d;
            c = e;
            d = f;
        }
    }

    public String toString() {
        StringBuilder buf = new StringBuilder();
        for (Iterator<Vec2D> i = vertices.iterator(); i.hasNext();) {
            buf.append(i.next().toString());
            if (i.hasNext()) {
                buf.append(", ");
            }
        }
        return buf.toString();
    }
}
