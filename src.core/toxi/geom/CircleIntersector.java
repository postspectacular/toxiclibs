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
 * This class handles Circle-Ray2D intersections by implementing the
 * {@link Intersector2D} interface.
 * 
 */
public class CircleIntersector implements Intersector2D {

    private IsectData2D isec = new IsectData2D();
    private Circle circle;

    public CircleIntersector(Circle circle) {
        this.circle = circle;
    }

    public Circle getCircle() {
        return circle;
    }

    public IsectData2D getIntersectionData() {
        return isec;
    }

    public boolean intersectsRay(Ray2D ray) {
        isec.clear();
        Vec2D q = circle.sub(ray);
        float distSquared = q.magSquared();
        float v = q.dot(ray.getDirection());
        float r = circle.getRadius();
        float d = r * r - (distSquared - v * v);
        if (d >= 0.0) {
            isec.isIntersection = true;
            isec.dist = v - (float) Math.sqrt(d);
            isec.pos = ray.getPointAtDistance(isec.dist);
            isec.normal = isec.pos.sub(circle).normalize();
        }
        return isec.isIntersection;
    }

    public void setCircle(Circle circle) {
        this.circle = circle;
    }
}
