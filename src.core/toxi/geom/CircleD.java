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
public class CircleD extends EllipseD {

    /**
     * Factory method to construct a circle which has the two given points lying
     * on its perimeter. If the points are coincident, the circle will have a
     * radius of zero.
     * 
     * @param p1
     * @param p2
     * @return new circle instance
     */
    public static CircleD from2Points(VecD2D p1, VecD2D p2) {
        VecD2D m = p1.interpolateTo(p2, 0.5f);
        return new CircleD(m, m.distanceTo(p1));
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
    public static CircleD from3Points(VecD2D p1, VecD2D p2, VecD2D p3) {
        CircleD circle = null;
        VecD2D deltaA = p2.sub(p1);
        VecD2D deltaB = p3.sub(p2);
        if (MathUtils.abs(deltaA.x) <= 0.0000001f
                && MathUtils.abs(deltaB.y) <= 0.0000001f) {
            VecD2D centroid = new VecD2D(p2.x + p3.x, p1.y + p2.y)
                    .scaleSelf(0.5f);
            double radius = centroid.distanceTo(p1);
            circle = new CircleD(centroid, radius);
        } else {
            double aSlope = deltaA.y / deltaA.x;
            double bSlope = deltaB.y / deltaB.x;
            if (MathUtils.abs(aSlope - bSlope) > 0.0000001f) {
                double x = (aSlope * bSlope * (p1.y - p3.y) + bSlope
                        * (p1.x + p2.x) - aSlope * (p2.x + p3.x))
                        / (2 * (bSlope - aSlope));
                double y = -(x - (p1.x + p2.x) / 2) / aSlope + (p1.y + p2.y) / 2;
                VecD2D centroid = new VecD2D(x, y);
                double radius = centroid.distanceTo(p1);
                circle = new CircleD(centroid, radius);
            }
        }
        return circle;
    }

    public static CircleD newBoundingCircleD(List<VecD2D> vertices) {
        VecD2D origin = new VecD2D();
        double maxD = 0;
        for (VecD2D v : vertices) {
            origin.addSelf(v);
        }
        origin.scaleSelf(1f / vertices.size());
        for (VecD2D v : vertices) {
            double d = origin.distanceToSquared(v);
            if (d > maxD) {
                maxD = d;
            }
        }
        return new CircleD(origin, Math.sqrt(maxD));
    }

    public CircleD() {
        this(new VecD2D(), 1);
    }

    public CircleD(CircleD c) {
        this(c, c.radius.x);
    }
    public CircleD(Circle c) {
        this(new VecD2D(c.x,c.y), c.radius.x);
    }

    public CircleD(double radius) {
        this(0, 0, radius);
    }

    public CircleD(double x, double y, double radius) {
        super(x, y, radius, radius);
    }

    public CircleD(ReadonlyVecD2D origin, double radius) {
        super(origin, radius);
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.geom.Ellipse#containsPoint(toxi.geom.VecD2D)
     */
    @Override
    public boolean containsPoint(ReadonlyVecD2D p) {
        return distanceToSquared(p) <= radius.x * radius.x;
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.geom.Ellipse#getCircumference()
     */
    @Override
    public double getCircumference() {
        return MathUtils.TWO_PI * radius.x;
    }

    public double getRadius() {
        return radius.x;
    }

    public VecD2D[] getTangentPoints(ReadonlyVecD2D p) {
        VecD2D m = interpolateTo(p, 0.5f);
        return intersectsCircleD(new CircleD(m, m.distanceTo(p)));
    }

    public VecD2D[] intersectsCircleD(CircleD c) {
        VecD2D[] res = null;
        VecD2D delta = c.sub(this);
        double d = delta.magnitude();
        double r1 = radius.x;
        double r2 = c.radius.x;
        if (d <= r1 + r2 && d >= Math.abs(r1 - r2)) {
            double a = (r1 * r1 - r2 * r2 + d * d) / (2.0f * d);
            d = 1 / d;
            VecD2D p = add(delta.scale(a * d));
            double h = Math.sqrt(r1 * r1 - a * a);
            delta.perpendicular().scaleSelf(h * d);
            VecD2D i1 = p.add(delta);
            VecD2D i2 = p.sub(delta);
            res = new VecD2D[] {
                    i1, i2
            };
        }
        return res;
    }

    public CircleD setRadius(double r) {
        super.setRadii(r, r);
        return this;
    }
}
