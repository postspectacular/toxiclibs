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

import toxi.geom.Ray2D;
import toxi.geom.Rect;
import toxi.geom.Vec2D;
import toxi.physics2d.VerletParticle2D;

public class RectConstraint implements ParticleConstraint2D {

    protected Rect rect;
    protected Ray2D intersectRay;
    protected boolean isContainer;

    public RectConstraint(Rect rect) {
        this(rect, false);
    }

    public RectConstraint(Rect rect, boolean isContainer) {
        this.isContainer = isContainer;
        setBox(rect);
    }

    public RectConstraint(Vec2D min, Vec2D max) {
        this(new Rect(min, max), false);
    }

    public void apply(VerletParticle2D p) {
        if (isContainer) {
            if (!rect.containsPoint(p)) {
                p.constrain(rect);
            }
        } else {
            if (rect.containsPoint(p)) {
                p.set(rect.intersectsRay(
                        intersectRay.setDirection(intersectRay.sub(p)), 0,
                        Float.MAX_VALUE));
            }
        }
    }

    public Rect getBox() {
        return rect.copy();
    }

    public void setBox(Rect rect) {
        this.rect = rect.copy();
        this.intersectRay = new Ray2D(rect.getCentroid(), new Vec2D());
    }
}
