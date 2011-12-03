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

/**
 * This class overrides {@link Ellipse} to define a 2D circle and provides
 * several utility methods for it, including factory methods to construct
 * circles from points.
 */
public class Circle extends Ellipse {

    /**
     * Factory method to construct a circle which has the two given points lying
     * on its perimeter. If the points are coincident, the circle will have a
     * radius of zero.
     * 
     * @param p1
     * @param p2
     * @return new circle instance
     */
    public static Circle from2Points(Vec2D p1, Vec2D p2) {
        Vec2D m = p1.interpolateTo(p2, 0.5f);
        return new Circle(m, m.distanceTo(p1));
    }

    /**
     * Factory method to construct a circle which has the three given points
     * lying on its perimeter. The function returns null, if the 3 points are
     * co-linear (in which case it's impossible to find a circle).
     * 
     * Based on CPP code by Paul Bourke:
     * http://local.wasp.uwa.edu.au/~pbourke/geometry/circlefrom3/
     * 
     * @param p1
     * @param p2
     * @param p3
     * @return new circle instance or null
     */
    public static Circle from3Points(Vec2D p1, Vec2D p2, Vec2D p3) {
        Circle circle = null;
        Vec2D deltaA = p2.sub(p1);
        Vec2D deltaB = p3.sub(p2);
        if (MathUtils.abs(deltaA.x) <= 0.0000001f
                && MathUtils.abs(deltaB.y) <= 0.0000001f) {
            Vec2D centroid = new Vec2D(p2.x + p3.x, p1.y + p2.y)
                    .scaleSelf(0.5f);
            float radius = centroid.distanceTo(p1);
            circle = new Circle(centroid, radius);
        } else {
            float aSlope = deltaA.y / deltaA.x;
            float bSlope = deltaB.y / deltaB.x;
            if (MathUtils.abs(aSlope - bSlope) > 0.0000001f) {
                float x = (aSlope * bSlope * (p1.y - p3.y) + bSlope
                        * (p1.x + p2.x) - aSlope * (p2.x + p3.x))
                        / (2 * (bSlope - aSlope));
                float y = -(x - (p1.x + p2.x) / 2) / aSlope + (p1.y + p2.y) / 2;
                Vec2D centroid = new Vec2D(x, y);
                float radius = centroid.distanceTo(p1);
                circle = new Circle(centroid, radius);
            }
        }
        return circle;
    }

    public static Circle newBoundingCircle(List<Vec2D> vertices) {
        Vec2D origin = new Vec2D();
        float maxD = 0;
        for (Vec2D v : vertices) {
            origin.addSelf(v);
        }
        origin.scaleSelf(1f / vertices.size());
        for (Vec2D v : vertices) {
            float d = origin.distanceToSquared(v);
            if (d > maxD) {
                maxD = d;
            }
        }
        return new Circle(origin, (float) Math.sqrt(maxD));
    }

    public Circle() {
        this(new Vec2D(), 1);
    }

    public Circle(Circle c) {
        this(c, c.radius.x);
    }

    public Circle(float radius) {
        this(0, 0, radius);
    }

    public Circle(float x, float y, float radius) {
        super(x, y, radius, radius);
    }

    public Circle(ReadonlyVec2D origin, float radius) {
        super(origin, radius);
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.geom.Ellipse#containsPoint(toxi.geom.Vec2D)
     */
    @Override
    public boolean containsPoint(ReadonlyVec2D p) {
        return distanceToSquared(p) <= radius.x * radius.x;
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.geom.Ellipse#getCircumference()
     */
    @Override
    public float getCircumference() {
        return MathUtils.TWO_PI * radius.x;
    }

    public float getRadius() {
        return radius.x;
    }

    public Vec2D[] getTangentPoints(ReadonlyVec2D p) {
        Vec2D m = interpolateTo(p, 0.5f);
        return intersectsCircle(new Circle(m, m.distanceTo(p)));
    }

    public Vec2D[] intersectsCircle(Circle c) {
        Vec2D[] res = null;
        Vec2D delta = c.sub(this);
        float d = delta.magnitude();
        float r1 = radius.x;
        float r2 = c.radius.x;
        if (d <= r1 + r2 && d >= Math.abs(r1 - r2)) {
            float a = (r1 * r1 - r2 * r2 + d * d) / (2.0f * d);
            d = 1 / d;
            Vec2D p = add(delta.scale(a * d));
            float h = (float) Math.sqrt(r1 * r1 - a * a);
            delta.perpendicular().scaleSelf(h * d);
            Vec2D i1 = p.add(delta);
            Vec2D i2 = p.sub(delta);
            res = new Vec2D[] {
                    i1, i2
            };
        }
        return res;
    }

    public Circle setRadius(float r) {
        super.setRadii(r, r);
        return this;
    }
}
