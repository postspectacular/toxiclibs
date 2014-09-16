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
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

import toxi.geom.Line2D.LineIntersection;
import toxi.geom.Line2D.LineIntersection.Type;
import toxi.geom.mesh.Mesh3D;
import toxi.geom.mesh.TriangleMesh;
import toxi.math.MathUtils;

/**
 * Container type for convex polygons. Implements {@link Shape2D}.
 */
public class Polygon2D implements Shape2D, Iterable<Vec2D> {

    /**
     * Constructs a new regular polygon from the given base line/edge.
     * 
     * @param baseA
     *            left point of the base edge
     * @param baseB
     *            right point of the base edge
     * @param res
     *            number of polygon vertices
     * @return polygon
     */
    public static Polygon2D fromBaseEdge(Vec2D baseA, Vec2D baseB, int res) {
        float theta = -(MathUtils.PI - (MathUtils.PI * (res - 2) / res));
        Vec2D dir = baseB.sub(baseA);
        Vec2D prev = baseB;
        Polygon2D poly = new Polygon2D(baseA, baseB);
        for (int i = 1; i < res - 1; i++) {
            Vec2D p = prev.add(dir.getRotated(theta * i));
            poly.add(p);
            prev = p;
        }
        return poly;
    }

    /**
     * Constructs a regular polygon from the given edge length and number of
     * vertices. This automatically computes the radius of the circle the
     * polygon is inscribed in.
     * 
     * <p>
     * More information: http://en.wikipedia.org/wiki/Regular_polygon#Radius
     * </p>
     * 
     * @param len
     *            desired edge length
     * @param res
     *            number of vertices
     * @return polygon
     */
    public static Polygon2D fromEdgeLength(float len, int res) {
        return new Circle(getRadiusForEdgeLength(len, res)).toPolygon2D(res);
    }

    /**
     * Computes the radius of the circle the regular polygon with the desired
     * edge length is inscribed in
     * 
     * @param len
     *            edge length
     * @param res
     *            number of polygon vertices
     * @return radius
     */
    public static float getRadiusForEdgeLength(float len, int res) {
        return len / (2 * MathUtils.sin(MathUtils.PI / res));
    }

    @XmlElement(name = "v")
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
     * @param x
     * @param y
     * @return itself
     */
    public Polygon2D add(float x, float y) {
        return add(new Vec2D(x, y));
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

    /**
     * Centers the polygon around the world origin (0,0).
     * 
     * @return itself
     */
    public Polygon2D center() {
        return center(null);
    }

    /**
     * Centers the polygon so that its new centroid is at the given point.
     * 
     * @param origin
     *            new centroid or null to center around (0,0)
     * @return itself
     */
    public Polygon2D center(ReadonlyVec2D origin) {
        Vec2D centroid = getCentroid();
        Vec2D delta = origin != null ? origin.sub(centroid) : centroid.invert();
        for (Vec2D v : vertices) {
            v.addSelf(delta);
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

    public boolean containsPolygon(Polygon2D poly) {
        for (Vec2D p : poly.vertices) {
            if (!containsPoint(p)) {
                return false;
            }
        }
        return true;
    }

    public Polygon2D copy() {
        return new Polygon2D(vertices);
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
     * Computes the length of this polygon's apothem. This will only be valid if
     * the polygon is regular. More info: http://en.wikipedia.org/wiki/Apothem
     * 
     * @return apothem length
     */
    public float getApothem() {
        return vertices.get(0).interpolateTo(vertices.get(1), 0.5f)
                .distanceTo(getCentroid());
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
        for (int i = 0, num = vertices.size(); i < num; i++) {
            Vec2D a = vertices.get(i);
            Vec2D b = vertices.get((i + 1) % num);
            area += a.x * b.y;
            area -= a.y * b.x;
        }
        area *= 0.5f;
        return area;
    }

    public Circle getBoundingCircle() {
        return Circle.newBoundingCircle(vertices);
    }

    /**
     * Returns the polygon's bounding rect.
     * 
     * @return bounding rect
     * @see toxi.geom.Shape2D#getBounds()
     */
    public Rect getBounds() {
        return Rect.getBoundingRect(vertices);
    }

    /**
     * Computes the polygon's centre of mass. Code ported from:
     * http://local.wasp.uwa.edu.au/~pbourke/geometry/polyarea/
     * 
     * @return centroid point
     */
    public Vec2D getCentroid() {
        Vec2D res = new Vec2D();
        for (int i = 0, num = vertices.size(); i < num; i++) {
            Vec2D a = vertices.get(i);
            Vec2D b = vertices.get((i + 1) % num);
            float crossP = a.x * b.y - b.x * a.y;
            res.x += (a.x + b.x) * crossP;
            res.y += (a.y + b.y) * crossP;
        }
        return res.scale(1f / (6 * getArea()));
    }

    /**
     * Computes the polygon's circumference, the length of its perimeter.
     * 
     * @return perimiter length
     * 
     * @see toxi.geom.Shape2D#getCircumference()
     */
    public float getCircumference() {
        float circ = 0;
        for (int i = 0, num = vertices.size(); i < num; i++) {
            circ += vertices.get(i).distanceTo(vertices.get((i + 1) % num));
        }
        return circ;
    }

    public Vec2D getClosestPointTo(ReadonlyVec2D p) {
        float minD = Float.MAX_VALUE;
        Vec2D q = null;
        for (Line2D l : getEdges()) {
            Vec2D c = l.closestPointTo(p);
            float d = c.distanceToSquared(p);
            if (d < minD) {
                q = c;
                minD = d;
            }
        }
        return q;
    }

    public Vec2D getClosestVertexTo(ReadonlyVec2D p) {
        float minD = Float.MAX_VALUE;
        Vec2D q = null;
        for (Vec2D v : vertices) {
            float d = v.distanceToSquared(p);
            if (d < minD) {
                q = v;
                minD = d;
            }
        }
        return q;
    }

    /**
     * Returns a list of {@link Line2D} segments representing the polygon edges.
     * 
     * @return list of lines
     */
    public List<Line2D> getEdges() {
        int num = vertices.size();
        List<Line2D> edges = new ArrayList<Line2D>(num);
        for (int i = 0; i < num; i++) {
            edges.add(new Line2D(vertices.get(i), vertices.get((i + 1) % num)));
        }
        return edges;
    }

    /**
     * @see #getNumVertices()
     */
    @Deprecated
    public int getNumPoints() {
        return getNumVertices();
    }

    /**
     * Returns the number of polygon vertices.
     * 
     * @return vertex count
     */
    public int getNumVertices() {
        return vertices.size();
    }

    /**
     * Creates a random point within the polygon. This is only guaranteed to
     * work with regular polygons.
     * 
     * @return Vec2D
     */
    public Vec2D getRandomPoint() {
        List<Line2D> edges = getEdges();
        int numEdges = edges.size();
        Line2D ea = edges.get(MathUtils.random(numEdges));
        Line2D eb = null;
        // and another one, making sure it's different
        while (eb == null || eb.equals(ea)) {
            eb = edges.get(MathUtils.random(numEdges));
        }
        // pick a random point on edge A
        Vec2D p = ea.a.interpolateTo(ea.b, MathUtils.random(1f));
        // then randomly interpolate to another random point on edge B
        return p.interpolateToSelf(
                eb.a.interpolateTo(eb.b, MathUtils.random(1f)),
                MathUtils.random(1f));
    }

    /**
     * Repeatedly inserts vertices as mid points of the longest edges until the
     * new vertex count is reached.
     * 
     * @param count
     *            new vertex count
     * @return itself
     */
    public Polygon2D increaseVertexCount(int count) {
        int num = vertices.size();
        while (num < count) {
            // find longest edge
            int longestID = 0;
            float maxD = 0;
            for (int i = 0; i < num; i++) {
                float d = vertices.get(i).distanceToSquared(
                        vertices.get((i + 1) % num));
                if (d > maxD) {
                    longestID = i;
                    maxD = d;
                }
            }
            // insert mid point of longest segment in vertex list
            Vec2D m = vertices.get(longestID)
                    .add(vertices.get((longestID + 1) % num)).scaleSelf(0.5f);
            vertices.add(longestID + 1, m);
            num++;
        }
        return this;
    }

    protected boolean intersectsLine(Line2D l, List<Line2D> edges) {
        for (Line2D e : edges) {
            final Type isec = l.intersectLine(e).getType();
            if (isec == Type.INTERSECTING || isec == Type.COINCIDENT) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the given polygon intersect this one by checking all edges for
     * line intersections.
     * 
     * @param poly
     * @return true, if polygons intersect.
     */
    public boolean intersectsPolygon(Polygon2D poly) {
        List<Line2D> edgesB = poly.getEdges();
        for (Line2D ea : getEdges()) {
            if (intersectsLine(ea, edgesB)) {
                return true;
            }
        }
        return false;
    }

    public boolean intersectsRect(Rect r) {
        List<Line2D> edges = r.getEdges();
        for (Line2D ea : getEdges()) {
            if (intersectsLine(ea, edges)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the vertices of this polygon are in clockwise ordering by
     * examining all vertices as a sequence of triangles. The test relies on the
     * fact that the {@link #getArea()} method will produce negative results for
     * ant-clockwise ordered polygons.
     * 
     * @return true, if clockwise
     */
    public boolean isClockwise() {
        return getArea() > 0;
    }

    /**
     * Checks if the polygon is convex.
     * 
     * @return true, if convex.
     */
    public boolean isConvex() {
        boolean isPositive = false;
        int num = vertices.size();
        for (int i = 0; i < num; i++) {
            int prev = (i == 0) ? num - 1 : i - 1;
            int next = (i == num - 1) ? 0 : i + 1;
            Vec2D d0 = vertices.get(i).sub(vertices.get(prev));
            Vec2D d1 = vertices.get(next).sub(vertices.get(i));
            boolean newIsP = (d0.cross(d1) > 0);
            if (i == 0) {
                isPositive = newIsP;
            } else if (isPositive != newIsP) {
                return false;
            }
        }
        return true;
    }

    public Iterator<Vec2D> iterator() {
        return vertices.iterator();
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
        int num = vertices.size() - 1;
        if (num > 1) {
            float startX = vertices.get(0).x;
            float startY = vertices.get(0).y;
            float c = vertices.get(num).x;
            float d = vertices.get(num).y;
            float e = startX;
            float f = startY;
            for (int i = 0; i < num; i++) {
                float a = c;
                float b = d;
                c = e;
                d = f;
                e = vertices.get(i + 1).x;
                f = vertices.get(i + 1).y;
                offsetCorner(a, b, c, d, e, f, distance, vertices.get(i));
            }
            offsetCorner(c, d, e, f, startX, startY, distance,
                    vertices.get(num));
        }
        return this;
    }

    /**
     * Reduces the number of vertices in the polygon based on the given minimum
     * edge length. Only vertices with at least this distance between them will
     * be kept.
     * 
     * @param minEdgeLen
     * @return itself
     */
    public Polygon2D reduceVertices(float minEdgeLen) {
        minEdgeLen *= minEdgeLen;
        List<Vec2D> reduced = new ArrayList<Vec2D>();
        Vec2D prev = vertices.get(0);
        reduced.add(prev);
        int num = vertices.size() - 1;
        for (int i = 1; i < num; i++) {
            Vec2D v = vertices.get(i);
            if (prev.distanceToSquared(v) >= minEdgeLen) {
                reduced.add(v);
                prev = v;
            }
        }
        if (vertices.get(0).distanceToSquared(vertices.get(num)) >= minEdgeLen) {
            reduced.add(vertices.get(num));
        }
        vertices = reduced;
        return this;
    }

    /**
     * Removes duplicate vertices from the polygon. Only successive points are
     * recognized as duplicates.
     * 
     * @param tolerance
     *            snap distance for finding duplicates
     * @return itself
     */
    public Polygon2D removeDuplicates(float tolerance) {
        Vec2D prev = null;
        for (Iterator<Vec2D> iv = vertices.iterator(); iv.hasNext();) {
            Vec2D p = iv.next();
            if (p.equalsWithTolerance(prev, tolerance)) {
                iv.remove();
            } else {
                prev = p;
            }
        }
        int num = vertices.size();
        if (num > 0) {
            Vec2D last = vertices.get(num - 1);
            if (last.equalsWithTolerance(vertices.get(0), tolerance)) {
                vertices.remove(last);
            }
        }
        return this;
    }

    public Polygon2D rotate(float theta) {
        for (Vec2D v : vertices) {
            v.rotate(theta);
        }
        return this;
    }

    public Polygon2D scale(float scale) {
        return scale(scale, scale);
    }

    public Polygon2D scale(float x, float y) {
        for (Vec2D v : vertices) {
            v.scaleSelf(x, y);
        }
        return this;
    }

    public Polygon2D scale(ReadonlyVec2D scale) {
        return scale(scale.x(), scale.y());
    }

    public Polygon2D scaleSize(float scale) {
        return scaleSize(scale, scale);
    }

    public Polygon2D scaleSize(float x, float y) {
        Vec2D centroid = getCentroid();
        for (Vec2D v : vertices) {
            v.subSelf(centroid).scaleSelf(x, y).addSelf(centroid);
        }
        return this;
    }

    public Polygon2D scaleSize(ReadonlyVec2D scale) {
        return scaleSize(scale.x(), scale.y());
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
            Vec2D dir = vertices.get(j).sub(a).addSelf(vertices.get(k).sub(a))
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
        return toMesh(mesh, null, 0);
    }

    public Mesh3D toMesh(Mesh3D mesh, Vec2D centroid2D, float extrude) {
        if (mesh == null) {
            mesh = new TriangleMesh();
        }
        final int num = vertices.size();
        if (centroid2D == null) {
            centroid2D = getCentroid();
        }
        Vec3D centroid = centroid2D.to3DXY();
        centroid.z = extrude;
        Rect bounds = getBounds();
        Vec2D boundScale = new Vec2D(1f / bounds.width, 1f / bounds.height);
        Vec2D uvC = centroid2D.sub(bounds.getTopLeft()).scaleSelf(boundScale);
        for (int i = 1; i <= num; i++) {
            Vec2D a = vertices.get(i % num);
            Vec2D b = vertices.get(i - 1);
            Vec2D uvA = a.sub(bounds.getTopLeft()).scaleSelf(boundScale);
            Vec2D uvB = b.sub(bounds.getTopLeft()).scaleSelf(boundScale);
            mesh.addFace(centroid, a.to3DXY(), b.to3DXY(), uvC, uvA, uvB);
        }
        return mesh;
    }

    /**
     * Attempts to remove all internal self-intersections and creates a new
     * polygon only consisting of perimeter vertices. Ported from:
     * http://alienryderflex.com/polygon_perimeter/
     * 
     * @return true, if process completed succcessfully.
     */
    public boolean toOutline() {
        int corners = vertices.size();
        int maxSegs = corners * 3;
        List<Vec2D> newVerts = new ArrayList<Vec2D>(corners);
        Vec2D[] segments = new Vec2D[maxSegs];
        Vec2D[] segEnds = new Vec2D[maxSegs];
        float[] segAngles = new float[maxSegs];
        Vec2D start = vertices.get(0).copy();
        float lastAngle = MathUtils.PI;
        float a, b, c, d, e, f;
        double angleDif, bestAngleDif;
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

    /**
     * Only needed for {@link Shape2D} interface. Returns itself.
     * 
     * @return itself
     */
    public Polygon2D toPolygon2D() {
        return this;
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

    public Polygon2D translate(float x, float y) {
        for (Vec2D v : vertices) {
            v.addSelf(x, y);
        }
        return this;
    }

    public Polygon2D translate(ReadonlyVec2D offset) {
        return translate(offset.x(), offset.y());
    }
}
