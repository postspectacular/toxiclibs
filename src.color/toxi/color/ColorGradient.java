/*
 * Some classes in this package have been partly inspired by & bits ported from
 * Python code written by Tom De Smedt & Frederik De Bleser for the "colors" library
 * of Nodebox.net.
 * 
 * http://nodebox.net/code/index.php/Colors
 * 
 * Copyright (c) 2006-2008 Karsten Schmidt <info at postspectacular.com>
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
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

package toxi.color;

import java.util.Iterator;
import java.util.TreeSet;

import toxi.math.MathUtils;

/**
 * This class can be used to calculate multi-color gradients with colors
 * positioned along an imaginary axis.
 * 
 * @author toxi
 * 
 */
public class ColorGradient {

    protected static final class GradPoint implements Comparable<GradPoint> {

        float pos;
        ReadonlyTColor color;

        GradPoint(float p, ReadonlyTColor c) {
            pos = p;
            color = c;
        }

        public int compareTo(GradPoint p) {
            if (Float.compare(p.pos, pos) == 0) {
                return 0;
            } else {
                return pos < p.pos ? -1 : 1;
            }
        }
    }

    protected TreeSet<GradPoint> gradient;

    protected float maxDither;

    /**
     * Constructs a new empty gradient.
     */
    public ColorGradient() {
        gradient = new TreeSet<GradPoint>();
    }

    /**
     * Adds a new color at specified position.
     * 
     * @param p
     * @param c
     */
    public void addColorAt(float p, ReadonlyTColor c) {
        gradient.add(new GradPoint(p, c));
    }

    /**
     * Calculates the gradient from specified position.
     * 
     * @param pos
     * @param width
     * @return list of interpolated gradient colors
     */
    public ColorList calcGradient(float pos, int width) {
        ColorList result = new ColorList();

        if (gradient.size() == 0) {
            return result;
        }

        float frac = 0;
        GradPoint currPoint = null;
        GradPoint nextPoint = null;
        float endPos = pos + width;
        // find 1st color needed, clamp start position to positive values only
        for (GradPoint gp : gradient) {
            if (gp.pos < pos) {
                currPoint = gp;
            }
        }
        boolean isPremature = currPoint == null;
        TreeSet<GradPoint> activeGradient = null;
        if (!isPremature) {
            activeGradient = (TreeSet<GradPoint>) gradient.tailSet(currPoint);
        } else {
            // start position is before 1st gradient color, so use whole
            // gradient
            activeGradient = gradient;
            currPoint = activeGradient.first();
        }
        float currWidth = 0;
        Iterator<GradPoint> iter = activeGradient.iterator();
        if (currPoint != activeGradient.last()) {
            nextPoint = iter.next();
            if (isPremature) {
                currWidth = 1f / (currPoint.pos - pos);
            } else {
                if (nextPoint.pos - currPoint.pos > 0) {
                    currWidth = 1f / (nextPoint.pos - currPoint.pos);
                }
            }
        }
        while (pos < endPos) {
            if (isPremature) {
                frac = 1 - (currPoint.pos - pos) * currWidth;
            } else {
                frac = (pos - currPoint.pos) * currWidth;
            }
            // switch to next color?
            if (frac > 1.0) {
                currPoint = nextPoint;
                isPremature = false;
                if (iter.hasNext()) {
                    nextPoint = iter.next();
                    if (currPoint != activeGradient.last()) {
                        currWidth = 1f / (nextPoint.pos - currPoint.pos);
                    } else {
                        currWidth = 0;
                    }
                    frac = (pos - currPoint.pos) * currWidth;
                }
            }
            if (currPoint != activeGradient.last()) {
                float ditheredFrac =
                        MathUtils.clip(frac + MathUtils.normalizedRandom()
                                * maxDither, 0f, 1f);
                result.add(currPoint.color.getBlended(nextPoint.color,
                        ditheredFrac));
            } else {
                result.add(currPoint.color.copy());
            }
            pos++;
        }
        return result;
    }

    /**
     * @return the maximum dither amount.
     */
    public float getMaxDither() {
        return maxDither;
    }

    /**
     * Sets the maximum dither amount. Setting this to values >0 will jitter the
     * interpolated colors in the calculated gradient. The value range for this
     * parameter is 0.0 (off) to 1.0 (100%).
     * 
     * @param maxDither
     */
    public void setMaxDither(float maxDither) {
        this.maxDither = MathUtils.clip(maxDither, 0f, 1f);
    }
}
