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
import toxi.geom.Vec2D;
import toxi.math.MathUtils;

/**
 * Creates a new palette of 4 similar (slightly paler) colors in addition to the
 * given start color. The hue variance and contrast can be adjusted.
 */
public class AnalogousStrategy implements ColorTheoryStrategy {

    public static final String NAME = "analogous";

    protected static final Vec2D[] tones = new Vec2D[] {
            new Vec2D(1, 2.2f), new Vec2D(2, 1), new Vec2D(-1, -0.5f),
            new Vec2D(-2, 1)
    };

    protected float contrast = 0.25f;
    protected float theta = 10 * MathUtils.DEG2RAD;

    /**
     * Creates a new instance with default contrast (25%) and 10
     */
    public AnalogousStrategy() {
    }

    /**
     * @param theta
     *            variance angle in radians
     * @param contrast
     */
    public AnalogousStrategy(float theta, float contrast) {
        this.contrast = contrast;
        this.theta = theta;
    }

    /**
     * @param theta
     *            variance angle in degrees
     * @param contrast
     */
    public AnalogousStrategy(int theta, float contrast) {
        this.contrast = contrast;
        this.theta = MathUtils.radians(theta);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * toxi.color.ColorTheoryStrategy#createListFromcolor(toxi.color.TColor)
     */
    public ColorList createListFromColor(ReadonlyTColor src) {
        contrast = MathUtils.clipNormalized(contrast);
        ColorList colors = new ColorList(src);
        for (Vec2D currTone : tones) {
            TColor c = src.getRotatedRYB(theta * currTone.x);
            float t = 0.44f - currTone.y * 0.1f;
            if (src.brightness() - contrast * currTone.y < t) {
                c.setBrightness(t);
            } else {
                c.setBrightness(src.brightness() - contrast * currTone.y);
            }
            c.desaturate(0.05f);
            colors.add(c);
        }
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

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return NAME + " contrast: " + contrast + " theta: "
                + MathUtils.degrees(theta);
    }
}
