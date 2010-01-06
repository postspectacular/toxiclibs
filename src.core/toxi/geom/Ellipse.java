package toxi.geom;

import toxi.math.MathUtils;

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
        setRadius(rx, ry);
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
     * @return the radius
     */
    public Vec2D getRadius() {
        return radius.copy();
    }

    public void setRadius(float rx, float ry) {
        radius.set(rx, ry);
        focus = radius.magnitude();
    }
}
