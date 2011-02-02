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

public class IsectData2D {

    public boolean isIntersection;
    public float dist;
    public ReadonlyVec2D pos;
    public ReadonlyVec2D dir;
    public ReadonlyVec2D normal;

    public IsectData2D() {

    }

    public IsectData2D(IsectData2D isec) {
        isIntersection = isec.isIntersection;
        dist = isec.dist;
        pos = isec.pos.copy();
        dir = isec.dir.copy();
        normal = isec.normal.copy();
    }

    public void clear() {
        isIntersection = false;
        dist = 0;
        pos = new Vec2D();
        dir = new Vec2D();
        normal = new Vec2D();
    }

    public String toString() {
        String s = "isec: " + isIntersection;
        if (isIntersection) {
            s += " at:" + pos + " dist:" + dist + "normal:" + normal;
        }
        return s;
    }
}