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

import toxi.geom.Vec2D;
import toxi.math.MathUtils;
import toxi.physics2d.VerletParticle2D;

public class AngularConstraint implements ParticleConstraint2D {

    public Vec2D rootPos;
    public float theta;

    public AngularConstraint(float theta) {
        this.theta = theta;
    }

    public AngularConstraint(int theta) {
        this.theta = MathUtils.radians(theta);
    }

    public AngularConstraint(Vec2D p, int theta) {
        rootPos = new Vec2D(p);
        this.theta = MathUtils.radians(theta);
    }

    public void apply(VerletParticle2D p) {
        Vec2D delta = p.sub(rootPos);
        float heading = MathUtils.floor(delta.heading() / theta) * theta;
        p.set(rootPos.add(Vec2D.fromTheta(heading).scaleSelf(delta.magnitude())));
    }

}
