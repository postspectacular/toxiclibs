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
import toxi.math.MathUtils;

public class CompoundTheoryStrategy implements ColorTheoryStrategy {

    public static final String NAME = "compound";

    private static final float wrap(float x, float min, float threshold,
            float plus) {
        if (x - min < threshold) {
            return x + plus;
        } else {
            return x - min;
        }
    }

    private boolean isFlipped = false;

    public CompoundTheoryStrategy() {

    }

    public CompoundTheoryStrategy(boolean flipped) {
        isFlipped = flipped;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * toxi.color.ColorTheoryStrategy#createListFromcolor(toxi.color.TColor)
     */
    public ColorList createListFromColor(ReadonlyTColor src) {
        ColorList colors = new ColorList(src);
        int direction = isFlipped ? -1 : 1;

        TColor c = src.getRotatedRYB(30 * direction);
        c.setBrightness(wrap(c.brightness(), 0.25f, 0.6f, 0.25f));
        colors.add(c);

        c = src.getRotatedRYB(30 * direction);
        c.setSaturation(wrap(c.saturation(), 0.4f, 0.1f, 0.4f));
        c.setBrightness(wrap(c.brightness(), 0.4f, 0.2f, 0.4f));
        colors.add(c);

        c = src.getRotatedRYB(160 * direction);
        c.setSaturation(wrap(c.saturation(), 0.25f, 0.1f, 0.25f));
        c.setBrightness(MathUtils.max(0.2f, c.brightness()));
        colors.add(c);

        c = src.getRotatedRYB(150 * direction);
        c.setSaturation(wrap(c.saturation(), 0.1f, 0.8f, 0.1f));
        c.setBrightness(wrap(c.brightness(), 0.3f, 0.6f, 0.3f));
        colors.add(c);

        c = src.getRotatedRYB(150 * direction);
        c.setSaturation(wrap(c.saturation(), 0.1f, 0.8f, 0.1f));
        c.setBrightness(wrap(c.brightness(), 0.4f, 0.2f, 0.4f));
        // colors.add(c);

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

    public boolean isFlipped() {
        return isFlipped;
    }

    public void setFlipped(boolean state) {
        isFlipped = state;
    }

    @Override
    public String toString() {
        return NAME + (isFlipped ? "_flipped" : "");
    }
}
