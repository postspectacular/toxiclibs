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

package toxi.color;

import java.util.ArrayList;
import java.util.StringTokenizer;

import toxi.math.MathUtils;

/**
 * A ColorTheme is a weighted collection of {@link ColorRange}s used to define
 * custom palettes with a certain balance between individual colors/shades. New
 * theme parts can be added via textual descriptors referring to one of the
 * preset {@link ColorRange}s and/or {@link NamedColor}s: e.g.
 * "warm springgreen". For each theme part a weight has to be specified. The
 * magnitude of the weight value is irrelevant and is only important in relation
 * to the weights of other theme parts. For example: Theme part A with a weight
 * of 0.5 will only have 1/20 of the weight of theme part B with a weight of
 * 5.0...
 */
public class ColorTheme {

    static class ThemePart {

        ColorRange range;
        ReadonlyTColor col;
        float weight;

        ThemePart(ColorRange range, ReadonlyTColor col, float weight) {
            this.range = range;
            this.col = col;
            this.weight = weight;
        }

        public TColor getColor() {
            return range.getColor(col, ColorRange.DEFAULT_VARIANCE);
        }
    }

    protected String name;
    protected ArrayList<ThemePart> parts = new ArrayList<ThemePart>();

    protected float weightedSum;

    public ColorTheme(String name) {
        this.name = name;
    }

    public ColorTheme addRange(ColorRange range, ReadonlyTColor col,
            float weight) {
        parts.add(new ThemePart(range, col, weight));
        weightedSum += weight;
        return this;
    }

    public ColorTheme addRange(String descriptor, float weight) {
        StringTokenizer st = new StringTokenizer(descriptor, " ,");
        ReadonlyTColor col = null;
        ColorRange range = null;
        while (st.hasMoreTokens()) {
            String item = st.nextToken();
            if (ColorRange.getPresetForName(item) != null) {
                range = ColorRange.getPresetForName(item);
            } else if (NamedColor.getForName(item) != null) {
                col = NamedColor.getForName(item);
            }
        }
        if (range != null) {
            addRange(range, col, weight);
        }
        return this;
    }

    public TColor getColor() {
        float rnd = MathUtils.random(1f);
        for (ThemePart t : parts) {
            float currWeight = t.weight / weightedSum;
            if (currWeight >= rnd) {
                return t.getColor();
            }
            rnd -= currWeight;
        }
        return null;
    }

    /**
     * Creates a {@link ColorList} of {@link TColor}s based on the theme's
     * ranges and balance defined by their weights
     * 
     * @param num
     *            number of colors to create
     * @return new list
     */
    public ColorList getColors(int num) {
        ColorList list = new ColorList();
        for (int i = 0; i < num; i++) {
            list.add(getColor());
        }
        return list;
    }

    /**
     * @return the theme's name
     */
    public String getName() {
        return name;
    }
}
