package toxi.geom;

import toxi.math.MathUtils;

/**
 * This class defines a 2D ellipse and provides several utility methods for it.
 */
public class Ellipse extends Vec2D {

    protected Vec2D radius = new Vec2D();
    protected float focus;

    public Ellipse() {
        this(0, 0, 1);

    }

    public Ellipse(float x, float y, float r) {
        this(x, y, r, r);

    }

    public Ellipse(float x, float y, float rx, float ry) {
        super(x, y);
        setRadii(rx, ry);
    }

    public Ellipse(Vec2D v, float r) {
        this(v.x, v.y, r, r);
    }

    public Ellipse(Vec2D v, Vec2D r) {
        this(v.x, v.y, r.x, r.y);
    }

    public boolean containsPoint(Vec2D p) {
        Vec2D[] foci = getFoci();
        return p.distanceTo(foci[0]) + p.distanceTo(foci[1]) < 2 * MathUtils
                .max(radius.x, radius.y);
    }

    /**
     * Computes the area covered by the ellipse.
     * 
     * @return area
     */
    public float getArea() {
        return MathUtils.PI * radius.x * radius.y;
    }

    /**
     * Computes the approximate circumference of the ellipse, using this
     * equation: <code>2 * PI * sqrt(1/2 * (rx*rx+ry*ry))</code>.
     * 
     * The precise value is an infinite series elliptical integral, but the
     * approximation comes sufficiently close. See Wikipedia for more details:
     * 
     * http://en.wikipedia.org/wiki/Ellipse
     * 
     * @return circumference
     */
    public float getCircumference() {
        // wikipedia solution:
        // return (float) (MathUtils.PI * (3 * (radius.x + radius.y) - Math
        // .sqrt((3 * radius.x + radius.y) * (radius.x + 3 * radius.y))));
        return (float) Math.sqrt(0.5 * radius.magSquared()) * MathUtils.TWO_PI;
    }

    /**
     * @return the focus
     */
    public Vec2D[] getFoci() {
        Vec2D[] foci = new Vec2D[2];
        if (radius.x > radius.y) {
            foci[0] = sub(focus, 0);
            foci[1] = add(focus, 0);
        } else {
            foci[0] = sub(0, focus);
            foci[1] = add(0, focus);
        }
        return foci;
    }

    /**
     * @return the 2 radii of the ellipse as a Vec2D
     */
    public Vec2D getRadii() {
        return radius.copy();
    }

    /**
     * Sets the radii of the ellipse to the new values.
     * 
     * @param rx
     * @param ry
     * @return itself
     */
    public Ellipse setRadii(float rx, float ry) {
        radius.set(rx, ry);
        focus = radius.magnitude();
        return this;
    }

    /**
     * Sets the radii of the ellipse to the values given by the vector.
     * 
     * @param r
     * @return itself
     */
    public Ellipse setRadii(Vec2D r) {
        return setRadii(r.x, r.y);
    }
}
