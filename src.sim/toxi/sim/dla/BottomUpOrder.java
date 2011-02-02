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

package toxi.sim.dla;

import java.util.Comparator;

import toxi.geom.Line3D;

/**
 * This comparator sorts segments based on their midpoint's Y position. Positive
 * Y axis is assumed the default up direction, but can be changed via the
 * constructor.
 */
public class BottomUpOrder implements Comparator<Line3D> {

    public boolean isFlipped;

    public BottomUpOrder(boolean isFlipped) {
        this.isFlipped = isFlipped;
    }

    public int compare(Line3D a, Line3D b) {
        float ya = a.getMidPoint().y;
        float yb = b.getMidPoint().y;
        if (isFlipped) {
            ya *= -1;
            yb *= -1;
        }
        if (ya < yb) {
            return -1;
        }
        if (ya > yb) {
            return 1;
        }
        return 0;
    }
}
