/*
 * Some classes in this package have been partly inspired by & bits ported from
 * Python code written by Tom De Smedt & Frederik De Bleser for the "colors" library
 * of Nodebox.net.
 * 
 * http://nodebox.net/code/index.php/Colors
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

package toxi.color.theory;

import toxi.color.ColorList;
import toxi.color.ReadonlyTColor;
import toxi.color.TColor;

/**
 * Implements the <a href=
 * "http://www.tigercolor.com/color-lab/color-theory/color-theory-intro.htm#rectangle"
 * >tetradic color scheme</a> to create 4 compatible colors for the given one.
 */
public class TetradTheoryStrategy implements ColorTheoryStrategy {

    public static final String NAME = "tetrad";

    protected int theta = 90;

    public TetradTheoryStrategy() {
    }

    /**
     * Constructs a new instance with the given color offset angle.
     * 
     * @param theta
     *            hue rotation angle in degrees
     */
    public TetradTheoryStrategy(int theta) {
        this.theta = theta;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * toxi.color.ColorTheoryStrategy#createListFromcolor(toxi.color.TColor)
     */
    public ColorList createListFromColor(ReadonlyTColor src) {
        ColorList colors = new ColorList(src);
        TColor c = src.getRotatedRYB(theta);
        if (c.brightness() < 0.5) {
            c.lighten(0.2f);
        } else {
            c.darken(0.2f);
        }
        colors.add(c);

        c = src.getRotatedRYB(theta * 2);
        if (c.brightness() < 0.5) {
            c.lighten(0.1f);
        } else {
            c.darken(0.1f);
        }
        colors.add(c);

        colors.add(src.getRotatedRYB(theta * 3).lighten(0.1f));
        return colors;
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.color.ColorTheoryStrategy#getName()
     */
    public String getName() {
        return NAME;
    }

    @Override
    public String toString() {
        return NAME;
    }

}
