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

import toxi.math.MathUtils;
import toxi.util.datatypes.BiasedFloatRange;

/**
 * This class defines a 2D ellipse and provides several utility methods for it.
 */
public class Ellipse extends Vec2D implements Shape2D {

    public static int DEFAULT_RES = 20;

    protected Vec2D radius = new Vec2D();
    protected float focus;

    public Ellipse() {
        this(0, 0, 1);

    }

    public Ellipse(float rx, float ry) {
        this(0, 0, rx, ry);
    }

    public Ellipse(float x, float y, float r) {
        this(x, y, r, r);

    }

    public Ellipse(float x, float y, float rx, float ry) {
        super(x, y);
        setRadii(rx, ry);
    }

    public Ellipse(ReadonlyVec2D v, float r) {
        this(v.x(), v.y(), r, r);
    }

    public Ellipse(ReadonlyVec2D v, ReadonlyVec2D r) {
        this(v.x(), v.y(), r.x(), r.y());
    }

    public boolean containsPoint(ReadonlyVec2D p) {
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

    public Circle getBoundingCircle() {
        return new Circle(x, y, MathUtils.max(radius.x, radius.y));
    }

    /**
     * Returns the ellipse's bounding rect.
     * 
     * @return bounding rect
     * @see toxi.geom.Shape2D#getBounds()
     */
    public Rect getBounds() {
        return new Rect(sub(radius), add(radius));
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

    public List<Line2D> getEdges() {
        return toPolygon2D().getEdges();
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
     * Creates a random point within the ellipse using a
     * {@link BiasedFloatRange} to create a more uniform distribution.
     * 
     * @return Vec2D
     */
    public Vec2D getRandomPoint() {
        float theta = MathUtils.random(MathUtils.TWO_PI);
        BiasedFloatRange rnd = new BiasedFloatRange(0f, 1f, 1f, MathUtils.SQRT2);
        return Vec2D.fromTheta(theta).scaleSelf(radius.scale(rnd.pickRandom()))
                .addSelf(this);
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
    public Ellipse setRadii(ReadonlyVec3D r) {
        return setRadii(r.x(), r.y());
    }

    public Polygon2D toPolygon2D() {
        return toPolygon2D(DEFAULT_RES);
    }

    /**
     * Creates a {@link Polygon2D} instance of the ellipse sampling it at the
     * given resolution.
     * 
     * @param res
     *            number of steps
     * @return ellipse as polygon
     */
    public Polygon2D toPolygon2D(int res) {
        Polygon2D poly = new Polygon2D();
        float step = MathUtils.TWO_PI / res;
        for (int i = 0; i < res; i++) {
            poly.add(Vec2D.fromTheta(i * step).scaleSelf(radius).addSelf(this));
        }
        return poly;
    }
}
