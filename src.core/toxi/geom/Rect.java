package toxi.geom;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

import toxi.math.MathUtils;

@XmlAccessorType(XmlAccessType.FIELD)
public class Rect {

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
    public Rect(Vec2D topLeft, Vec2D bottomRight) {
        x = topLeft.x;
        y = topLeft.y;
        width = bottomRight.x - x;
        height = bottomRight.y - y;
    }

    /**
     * Checks if the given point is within the rectangle's bounds.
     * 
     * @param p
     *            point to check
     * @return true, if point is contained
     */
    public final boolean containsPoint(Vec2D p) {
        if (p.x < x || p.x >= x + width) {
            return false;
        }
        if (p.y < y || p.y >= y + height) {
            return false;
        }
        return true;
    }

    /**
     * Creates a copy of this rectangle
     * 
     * @return
     */
    public Rect copy() {
        return new Rect(x, y, width, height);
    }

    public final Vec2D getBottomRight() {
        return new Vec2D(x + width, y + height);
    }

    /**
     * Returns the centroid of the rectangle.
     * 
     * @return
     */
    public final Vec2D getCentroid() {
        return new Vec2D(x + width * 0.5f, y + height * 0.5f);
    }

    /**
     * Returns a vector containing the width and height of the rectangle.
     * 
     * @return
     */
    public final Vec2D getDimensions() {
        return new Vec2D(width, height);
    }

    public final Vec2D getTopLeft() {
        return new Vec2D(x, y);
    }

    /**
     * @deprecated use {@link #union(Rect)} instead.
     * @param r
     * @return itself
     */
    @Deprecated
    public final Rect merge(Rect r) {
        return union(r);
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
