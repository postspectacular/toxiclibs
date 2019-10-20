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

/**
 * This class handles CircleD-RayD2D intersections by implementing the
 * {@link Intersector2D} interface.
 * 
 */
public class CircleDIntersector implements IntersectorD2D {

    private IsectDataD2D isec = new IsectDataD2D();
    private CircleD circle;

    public CircleDIntersector(CircleD circle) {
        this.circle = circle;
    }

    public CircleD getCircleD() {
        return circle;
    }

    public IsectDataD2D getIntersectionDataD() {
        return isec;
    }

    public boolean intersectsRayD(RayD2D ray) {
        isec.clear();
        VecD2D q = circle.sub(ray);
        double distSquared = q.magSquared();
        double v = q.dot(ray.getDirection());
        double r = circle.getRadius();
        double d = r * r - (distSquared - v * v);
        if (d >= 0.0) {
            isec.isIntersection = true;
            isec.dist = v -  Math.sqrt(d);
            isec.pos = ray.getPointAtDistance(isec.dist);
            isec.normal = isec.pos.sub(circle).normalize();
        }
        return isec.isIntersection;
    }

    public void setCircleD(CircleD circle) {
        this.circle = circle;
    }
}
