/*
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

import toxi.math.InterpolateStrategy;
import toxi.math.ScaleMap;

public class ToneMap {

    public ScaleMap map;
    public ColorList colors;

    public ToneMap(float min, float max, ColorGradient gradient) {
        this(min, max, gradient.calcGradient());
    }

    public ToneMap(float min, float max, ColorList c) {
        map = new ScaleMap(min, max, 0, c.size() - 1);
        colors = c;
    }

    public ToneMap(float a, float b, ReadonlyTColor colA, ReadonlyTColor colB) {
        this(a, b, new ColorList(colA, colB));
    }

    public ToneMap(float min, float max, ReadonlyTColor colA,
            ReadonlyTColor colB, int res) {
        ColorGradient g = new ColorGradient();
        g.addColorAt(0, colA);
        g.addColorAt(res - 1, colB);
        colors = g.calcGradient(0, res);
        map = new ScaleMap(min, max, 0, colors.size() - 1);
    }

    public int getARGBToneFor(float t) {
        return getToneFor(t).toARGB();
    }

    public ReadonlyTColor getToneFor(float t) {
        int idx;
        if (colors.size() > 2) {
            idx = (int) (map.getClippedValueFor(t) + 0.5);
        } else {
            idx = (t >= map.getInputMedian() ? 1 : 0);
        }
        return colors.get(idx);
    }

    public void setMapFunction(InterpolateStrategy func) {
        map.setMapFunction(func);
    }
}
