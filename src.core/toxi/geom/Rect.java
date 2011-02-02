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

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

import toxi.math.MathUtils;

@XmlAccessorType(XmlAccessType.FIELD)
public class Rect implements Shape2D {

    /**
     * Factory method, constructs a new rectangle from a center point and extent
     * vector.
     * 
     * @param center
     * @param extent
     * @return new rect
     */
    public static final Rect fromCenterExtent(ReadonlyVec2D center, Vec2D extent) {
        return new Rect(center.sub(extent), center.add(extent));
    }

    /**
     * Factory method, computes & returns the bounding rect for the given list
     * of points.
     * 
     * @param points
     * @return bounding rect
     */
    public static final Rect getBoundingRect(List<? extends Vec2D> points) {
        final Vec2D first = points.get(0);
        final Rect bounds = new Rect(first.x, first.y, 0, 0);
        for (int i = 1, num = points.size(); i < num; i++) {
            bounds.growToContainPoint(points.get(i));
        }
        return bounds;
    }

    @XmlAttribute(required = true)
    public float x, y, width, height;

    /**
     * Constructs an empty rectangle at point 0,0 with no dimensions.
     */
    public Rect() {

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
    public Rect(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * Constructs a new rectangle defined by its top left and bottom right
     * points.
     * 
     * @param topLeft
     * @param bottomRight
     */
    public Rect(ReadonlyVec2D topLeft, ReadonlyVec2D bottomRight) {
        x = topLeft.x();
        y = topLeft.y();
        width = bottomRight.x() - x;
        height = bottomRight.y() - y;
    }

    /**
     * Checks if the given point is within the rectangle's bounds.
     * 
     * @param p
     *            point to check
     * @return true, if point is contained
     */
    public boolean containsPoint(ReadonlyVec2D p) {
        float px = p.x();
        float py = p.y();
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
    public Rect copy() {
        return new Rect(x, y, width, height);
    }

    public final float getArea() {
        return width * height;
    }

    /**
     * Computes the aspect ratio of the rect as width over height.
     * 
     * @return aspect ratio
     */
    public final float getAspect() {
        return width / height;
    }

    public float getBottom() {
        return y + height;
    }

    public Vec2D getBottomLeft() {
        return new Vec2D(x, y + height);
    }

    public final Vec2D getBottomRight() {
        return new Vec2D(x + width, y + height);
    }

    /**
     * Returns the centroid of the rectangle.
     * 
     * @return centroid vector
     */
    public final Vec2D getCentroid() {
        return new Vec2D(x + width * 0.5f, y + height * 0.5f);
    }

    public final float getCircumference() {
        return 2 * width + 2 * height;
    }

    /**
     * Returns a vector containing the width and height of the rectangle.
     * 
     * @return dimension vector
     */
    public final Vec2D getDimensions() {
        return new Vec2D(width, height);
    }

    /**
     * Returns one of the rectangles edges as {@link Line2D}. The edge IDs are:
     * <ul>
     * <li>0 - top</li>
     * <li>1 - right</li>
     * <li>2 - bottom</li>
     * <li>3 - left</li>
     * </ul>
     * 
     * @param id
     *            edge ID
     * @return edge as Line2D
     */
    public Line2D getEdge(int id) {
        Line2D edge = null;
        switch (id) {
            // top
            case 0:
                edge = new Line2D(new Vec2D(x, y), new Vec2D(x + width, y));
                break;
            // right
            case 1:
                edge =
                        new Line2D(new Vec2D(x + width, y), new Vec2D(
                                x + width, y + height));
                break;
            // bottom
            case 2:
                edge =
                        new Line2D(new Vec2D(x, y + height), new Vec2D(x
                                + width, y + height));
                break;
            // left
            case 3:
                edge = new Line2D(new Vec2D(x, y), new Vec2D(x, y + height));
                break;
            default:
                throw new IllegalArgumentException("edge ID needs to be 0...3");
        }
        return edge;
    }

    public float getLeft() {
        return x;
    }

    public float getRight() {
        return x + width;
    }

    public float getTop() {
        return y;
    }

    public final Vec2D getTopLeft() {
        return new Vec2D(x, y);
    }

    public Vec2D getTopRight() {
        return new Vec2D(x + width, y);
    }

    public Rect growToContainPoint(ReadonlyVec2D p) {
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
    public ReadonlyVec2D intersectsRay(Ray2D ray, float minDist, float maxDist) {
        Vec2D invDir = ray.getDirection().reciprocal();
        boolean signDirX = invDir.x < 0;
        boolean signDirY = invDir.y < 0;
        Vec2D min = getTopLeft();
        Vec2D max = getBottomRight();
        Vec2D bbox = signDirX ? max : min;
        float tmin = (bbox.x - ray.x) * invDir.x;
        bbox = signDirX ? min : max;
        float tmax = (bbox.x - ray.x) * invDir.x;
        bbox = signDirY ? max : min;
        float tymin = (bbox.y - ray.y) * invDir.y;
        bbox = signDirY ? min : max;
        float tymax = (bbox.y - ray.y) * invDir.y;
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
    public boolean intersectsRect(Rect r) {
        return !(x > r.x + r.width || x + width < r.x || y > r.y + r.height || y
                + height < r.y);
    }

    public Rect scale(float s) {
        Vec2D c = getCentroid();
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
    public final Rect set(float x, float y, float w, float h) {
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;
        return this;
    }

    public final Rect set(Rect r) {
        x = r.x;
        y = r.y;
        width = r.width;
        height = r.height;
        return this;
    }

    public final Rect setDimension(Vec2D dim) {
        width = dim.x;
        height = dim.y;
        return this;
    }

    public final Rect setPosition(Vec2D pos) {
        x = pos.x;
        y = pos.y;
        return this;
    }

    /**
     * Creates a {@link Polygon2D} instance of the rect.
     * 
     * @return rect as polygon
     */
    public Polygon2D toPolygon2D() {
        Polygon2D poly = new Polygon2D();
        poly.add(new Vec2D(x, y));
        poly.add(new Vec2D(x + width, y));
        poly.add(new Vec2D(x + width, y + height));
        poly.add(new Vec2D(x, y + height));
        return poly;
    }

    @Override
    public String toString() {
        return "rect: {x:" + x + ", y:" + y + ", width:" + width + ", height:"
                + height + "}";
    }

    /**
     * Updates the bounds of this rectangle by forming an union with the given
     * rect. If the rects are not overlapping, the resulting bounds will be
     * inclusive of both.
     * 
     * @param r
     * @return itself
     */
    public final Rect union(Rect r) {
        float tmp = MathUtils.max(x + width, r.x + r.width);
        x = MathUtils.min(x, r.x);
        width = tmp - x;
        tmp = MathUtils.max(y + height, r.y + r.height);
        y = MathUtils.min(y, r.y);
        height = tmp - y;
        return this;
    }
}
