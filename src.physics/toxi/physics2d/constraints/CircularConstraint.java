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

package toxi.physics2d.constraints;

import toxi.geom.Circle;
import toxi.geom.Vec2D;
import toxi.physics2d.VerletParticle2D;

public class CircularConstraint implements ParticleConstraint2D {

    public Circle circle;

    public CircularConstraint(Circle circle) {
        this.circle = circle;
    }

    public CircularConstraint(Vec2D origin, float radius) {
        this.circle = new Circle(origin, radius);
    }

    public void apply(VerletParticle2D p) {
        if (circle.containsPoint(p)) {
            p.set(circle.add(p.sub(circle).normalizeTo(circle.getRadius())));
        }
    }

}
