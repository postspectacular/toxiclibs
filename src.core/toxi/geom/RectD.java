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
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

import toxi.math.MathUtils;

@XmlAccessorType(XmlAccessType.FIELD)
public class RectD implements ShapeD2D {

    /**
     * Factory method, constructs a new rectangle from a center point and extent
     * vector.
     * 
     * @param center
     * @param extent
     * @return new rect
     */
    public static final RectD fromCenterExtent(ReadonlyVecD2D center, VecD2D extent) {
        return new RectD(center.sub(extent), center.add(extent));
    }

    /**
     * Factory method, computes & returns the bounding rect for the given list
     * of points.
     * 
     * @param points
     * @return bounding rect
     * @since 0021
     */
    public static final RectD getBoundingRectD(List<? extends VecD2D> points) {
        final VecD2D first = points.get(0);
        final RectD bounds = new RectD(first.x, first.y, 0, 0);
        for (int i = 1, num = points.size(); i < num; i++) {
            bounds.growToContainPoint(points.get(i));
        }
        return bounds;
    }

    @XmlAttribute(required = true)
    public double x, y, width, height;

    /**
     * Constructs an empty rectangle at point 0,0 with no dimensions.
     */
    public RectD() {

    }

    /**
     * Constructs a new rectangle using a point and dimensions
     * 
     * @param x
     *            x of top left
     * @param y
     *            y of top left
     * @param width
     * @param height
     */
    public RectD(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * Constructs a new rectangle defined by the two points given.
     * 
     * @param p1
     * @param p2
     */
    public RectD(ReadonlyVecD2D p1, ReadonlyVecD2D p2) {
        VecD2D tl = VecD2D.min(p1, p2);
        VecD2D br = VecD2D.max(p1, p2);
        x = tl.x;
        y = tl.y;
        width = br.x - x;
        height = br.y - y;
    }

    /**
     * Checks if the given point is within the rectangle's bounds.
     * 
     * @param p
     *            point to check
     * @return true, if point is contained
     */
    public boolean containsPoint(ReadonlyVecD2D p) {
        double px = p.x();
        double py = p.y();
        if (px < x || px >= x + width) {
            return false;
        }
        if (py < y || py >= y + height) {
            return false;
        }
        return true;
    }

    /**
     * Creates a copy of this rectangle
     * 
     * @return new instance
     */
    public RectD copy() {
        return new RectD(x, y, width, height);
    }

    /**
     * Returns true if the Object o is of type RectD and all of the data members
     * of o are equal to the corresponding data members in this rectangle.
     * 
     * @param o
     *            the Object with which the comparison is made
     * @return true or false
     */
    public boolean equals(Object o) {
        try {
            RectD r = (RectD) o;
            return (x == r.x && y == r.y && width == r.width && height == r.height);
        } catch (NullPointerException e) {
            return false;
        } catch (ClassCastException e) {
            return false;
        }
    }

    public final double getArea() {
        return width * height;
    }

    /**
     * Computes the aspect ratio of the rect as width over height.
     * 
     * @return aspect ratio
     */
    public final double getAspect() {
        return width / height;
    }

    public double getBottom() {
        return y + height;
    }

    public VecD2D getBottomLeft() {
        return new VecD2D(x, y + height);
    }

    public final VecD2D getBottomRight() {
        return new VecD2D(x + width, y + height);
    }

    public CircleD getBoundingCircleD() {
        return new CircleD(getCentroid(),
                new VecD2D(width, height).magnitude() / 2);
    }

    /**
     * Only provided because the RectD class implements {@link Shape2D}. The
     * bounding rect of a rectangle is itself.
     * 
     * @return itself
     * 
     * @see toxi.geom.Shape2D#getBounds()
     */
    public RectD getBounds() {
        return this;
    }

    /**
     * Returns the centroid of the rectangle.
     * 
     * @return centroid vector
     */
    public final VecD2D getCentroid() {
        return new VecD2D(x + width * 0.5f, y + height * 0.5f);
    }

    public final double getCircumference() {
        return 2 * width + 2 * height;
    }

    /**
     * Returns a vector containing the width and height of the rectangle.
     * 
     * @return dimension vector
     */
    public final VecD2D getDimensions() {
        return new VecD2D(width, height);
    }

    /**
     * Returns one of the rectangles edges as {@link LineD2D}. The edge IDs are:
     * <ul>
     * <li>0 - top</li>
     * <li>1 - right</li>
     * <li>2 - bottom</li>
     * <li>3 - left</li>
     * </ul>
     * 
     * @param id
     *            edge ID
     * @return edge as LineD2D
     */
    public LineD2D getEdge(int id) {
        LineD2D edge = null;
        switch (id) {
        // top
            case 0:
                edge = new LineD2D(x, y, x + width, y);
                break;
            // right
            case 1:
                edge = new LineD2D(x + width, y, x + width, y + height);
                break;
            // bottom
            case 2:
                edge = new LineD2D(x, y + height, x + width, y + height);
                break;
            // left
            case 3:
                edge = new LineD2D(x, y, x, y + height);
                break;
            default:
                throw new IllegalArgumentException("edge ID needs to be 0...3");
        }
        return edge;
    }

    public List<LineD2D> getEdges() {
        List<LineD2D> edges = new ArrayList<LineD2D>();
        for (int i = 0; i < 4; i++) {
            edges.add(getEdge(i));
        }
        return edges;
    }

    public double getLeft() {
        return x;
    }

    /**
     * Computes the normalized position of the given point within this
     * rectangle, so that a point at the top-left corner becomes {0,0} and
     * bottom-right {1,1}. The original point is not modified. Together with
     * {@link #getUnmappedPointInRectD(VecD2D)} this function can be used to map a
     * point from one rectangle to another.
     * 
     * @param p
     *            point to be mapped
     * @return mapped VecD2D
     */
    public VecD2D getMappedPointInRectD(VecD2D p) {
        return new VecD2D((p.x - x) / width, (p.y - y) / height);
    }

    /**
     * Creates a random point within the rectangle.
     * 
     * @return VecD2D
     */
    public VecD2D getRandomPoint() {
        return new VecD2D(MathUtils.random(x, x + width), MathUtils.random(y, y
                + height));
    }

    public double getRight() {
        return x + width;
    }

    public double getTop() {
        return y;
    }

    public final VecD2D getTopLeft() {
        return new VecD2D(x, y);
    }

    public VecD2D getTopRight() {
        return new VecD2D(x + width, y);
    }

    /**
     * Inverse operation of {@link #getMappedPointInRectD(VecD2D)}. Given a
     * normalized point it computes the position within this rectangle, so that
     * a point at {0,0} becomes the top-left corner and {1,1} bottom-right. The
     * original point is not modified. Together with
     * {@link #getUnmappedPointInRectD(VecD2D)} this function can be used to map a
     * point from one rectangle to another.
     * 
     * @param p
     *            point to be mapped
     * @return mapped VecD2D
     */
    public VecD2D getUnmappedPointInRectD(VecD2D p) {
        return new VecD2D(p.x * width + x, p.y * height + y);
    }

    public RectD growToContainPoint(ReadonlyVecD2D p) {
        if (!containsPoint(p)) {
            if (p.x() < x) {
                width = getRight() - p.x();
                x = p.x();
            } else if (p.x() > getRight()) {
                width = p.x() - x;
            }
            if (p.y() < y) {
                height = getBottom() - p.y();
                y = p.y();
            } else if (p.y() > getBottom()) {
                height = p.y() - y;
            }
        }
        return this;
    }

    /**
     * Returns a hash code value based on the data values in this object. Two
     * different RectD objects with identical data values (i.e., RectD.equals
     * returns true) will return the same hash code value. Two objects with
     * different data members may return the same hash value, although this is
     * not likely.
     * 
     * @return the integer hash code value
     */
    public int hashCode() {
        return ((Double)x).hashCode()+((Double)y).hashCode()+((Double)width).hashCode()+((Double)height).hashCode();
    }

    /**
     * Creates a new rectangle by forming the intersection of this rectangle and
     * the given other rect. The resulting bounds will be the rectangle of the
     * overlay area or null if the rects do not intersect.
     * 
     * @param r
     *            intersection partner rect
     * @return new RectD or null
     */
    public final RectD intersectionRectDWith(RectD r) {
        RectD isec = null;
        if (intersectsRectD(r)) {
            double x1 = MathUtils.max(x, r.x);
            double y1 = MathUtils.max(y, r.y);
            double x2 = MathUtils.min(getRight(), r.getRight());
            double y2 = MathUtils.min(getBottom(), r.getBottom());
            isec = new RectD(x1, y1, x2 - x1, y2 - y1);
        }
        return isec;
    }

    public boolean intersectsCircleD(VecD2D c, double r) {
        double s, d = 0;
        double x2 = x + width;
        double y2 = y + height;
        if (c.x < x) {
            s = c.x - x;
            d = s * s;
        } else if (c.x > x2) {
            s = c.x - x2;
            d += s * s;
        }
        if (c.y < y) {
            s = c.y - y;
            d += s * s;
        } else if (c.y > y2) {
            s = c.y - y2;
            d += s * s;
        }
        return d <= r * r;
    }

    /**
     * Checks if the rectangle intersects with the given ray and if so computes
     * the first intersection point. The method takes a min/max distance
     * interval along the ray in which the intersection must occur.
     * 
     * @param ray
     *            intersection ray
     * @param minDist
     *            minimum distance
     * @param maxDist
     *            max distance
     * @return intersection point or null if no intersection in the given
     *         interval
     */
    public ReadonlyVecD2D intersectsRayD(RayD2D ray, double minDist, double maxDist) {
        VecD2D invDir = ray.getDirection().reciprocal();
        boolean signDirX = invDir.x < 0;
        boolean signDirY = invDir.y < 0;
        VecD2D min = getTopLeft();
        VecD2D max = getBottomRight();
        VecD2D bbox = signDirX ? max : min;
        double tmin = (bbox.x - ray.x) * invDir.x;
        bbox = signDirX ? min : max;
        double tmax = (bbox.x - ray.x) * invDir.x;
        bbox = signDirY ? max : min;
        double tymin = (bbox.y - ray.y) * invDir.y;
        bbox = signDirY ? min : max;
        double tymax = (bbox.y - ray.y) * invDir.y;
        if ((tmin > tymax) || (tymin > tmax)) {
            return null;
        }
        if (tymin > tmin) {
            tmin = tymin;
        }
        if (tymax < tmax) {
            tmax = tymax;
        }
        if ((tmin < maxDist) && (tmax > minDist)) {
            return ray.getPointAtDistance(tmin);
        }
        return null;
    }

    /**
     * Checks if this rectangle intersects/overlaps the given one.
     * 
     * @param r
     *            another rect
     * @return true, if intersecting
     */
    public boolean intersectsRectD(RectD r) {
        return !(x > r.x + r.width || x + width < r.x || y > r.y + r.height || y
                + height < r.y);
    }

    public RectD scale(double s) {
        VecD2D c = getCentroid();
        width *= s;
        height *= s;
        x = c.x - width * 0.5f;
        y = c.y - height * 0.5f;
        return this;
    }

    /**
     * Sets new bounds for this rectangle.
     * 
     * @param x
     *            x of top left
     * @param y
     *            y of top right
     * @param w
     *            width
     * @param h
     *            height
     * @return itself
     */
    public final RectD set(double x, double y, double w, double h) {
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;
        return this;
    }

    public final RectD set(RectD r) {
        x = r.x;
        y = r.y;
        width = r.width;
        height = r.height;
        return this;
    }

    public final RectD setDimension(VecD2D dim) {
        width = dim.x;
        height = dim.y;
        return this;
    }

    public final RectD setPosition(VecD2D pos) {
        x = pos.x;
        y = pos.y;
        return this;
    }

    /**
     * Adds corner vertices for rounded rectangles polygon construction.
     * 
     * @param poly
     * @param o
     * @param radius
     * @param theta
     * @param res
     */
    private void toPolyArc(PolygonD2D poly, VecD2D o, double radius, double theta,
            int res) {
        for (int i = 0; i <= res; i++) {
            poly.add(o.add(VecD2D.fromTheta(theta + i * MathUtils.HALF_PI / res)
                    .scaleSelf(radius)));
        }
    }

    /**
     * Creates a {@link Polygon2D} instance of the rect.
     * 
     * @return rect as polygon
     */
    public PolygonD2D toPolygonD2D() {
        PolygonD2D poly = new PolygonD2D();
        poly.add(new VecD2D(x, y));
        poly.add(new VecD2D(x + width, y));
        poly.add(new VecD2D(x + width, y + height));
        poly.add(new VecD2D(x, y + height));
        return poly;
    }

    /**
     * Turns this rectangle into a rounded rectangle shaped {@link Polygon2D}
     * instance with the given corner radius. The number of corner vertices to
     * be used, can be specified as well.
     * 
     * @param radius
     *            corner radius
     * @param res
     *            number of vertices per corner
     * @return rounded rect as polygon
     */
    public PolygonD2D toPolygonD2D(double radius, int res) {
        PolygonD2D poly = new PolygonD2D();
        toPolyArc(poly, new VecD2D(x + width - radius, y + radius), radius,
                -MathUtils.HALF_PI, res);
        toPolyArc(poly, new VecD2D(x + width - radius, y + height - radius),
                radius, 0, res);
        toPolyArc(poly, new VecD2D(x + radius, y + height - radius), radius,
                MathUtils.HALF_PI, res);
        toPolyArc(poly, new VecD2D(x + radius, y + radius), radius,
                MathUtils.PI, res);
        return poly;
    }

    @Override
    public String toString() {
        return "rect: {x:" + x + ", y:" + y + ", width:" + width + ", height:"
                + height + "}";
    }

    public RectD translate(double dx, double dy) {
        x += dx;
        y += dy;
        return this;
    }

    public RectD translate(ReadonlyVecD2D offset) {
        x += offset.x();
        y += offset.y();
        return this;
    }

    /**
     * @deprecated use {@link #unionRectDWith(RectD)} instead. Also note, that
     *             {@link #unionRectDWith(RectD)} does NOT modify the original
     *             RectD anymore, but produces a new RectD instance.
     * @param r
     * @return new RectD
     */
    @Deprecated
    public final RectD union(RectD r) {
        return unionRectDWith(r);
    }

    /**
     * Creates a new rectangle by forming an union of this rectangle and the
     * given other Rect. The resulting bounds will be inclusive of both.
     * 
     * @param r
     * @return new RectD
     */
    public final RectD unionRectDWith(RectD r) {
        double x1 = MathUtils.min(x, r.x);
        double x2 = MathUtils.max(x + width, r.x + r.width);
        double w = x2 - x1;
        double y1 = MathUtils.min(y, r.y);
        double y2 = MathUtils.max(y + height, r.y + r.height);
        double h = y2 - y1;
        return new RectD(x1, y1, w, h);
    }
}