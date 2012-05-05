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

/**
 * Interface description of common operations supported by 2D geometry types.
 */
public interface Shape2D {

    /**
     * Checks if the point is within the given shape.
     * 
     * @return true, if inside
     */
    boolean containsPoint(ReadonlyVec2D p);

    /**
     * Computes the area of the shape.
     * 
     * @return area
     */
    float getArea();

    /**
     * Computes the bounding circle of the shape.
     * 
     * @return circle
     */
    Circle getBoundingCircle();

    /**
     * Returns the shape's axis-aligned bounding rect.
     * 
     * @return bounding rect
     */
    Rect getBounds();

    /**
     * Computes the shape's circumference.
     * 
     * @return circumference
     */
    float getCircumference();

    /**
     * Returns a list of the shape's perimeter edges.
     * 
     * @return list of {@link Line2D} elements
     */
    List<Line2D> getEdges();

    /**
     * Computes a random point within the shape's perimeter.
     * 
     * @return Vec2D
     */
    Vec2D getRandomPoint();

    /**
     * Converts the shape into a {@link Polygon2D} instance (possibly via a
     * default resolution, e.g. for circles/ellipses)
     * 
     * @return shape as polygon
     */
    Polygon2D toPolygon2D();
}
