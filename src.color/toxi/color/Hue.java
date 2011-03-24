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
import java.util.HashMap;

import toxi.math.MathUtils;

/**
 * This class defines color hues and allows them to be access by name. There are
 * also methods to check if a hue is one of the 7 primary hues (rainbow) or to
 * find the closest defined hue for a given color.
 */
public class Hue {

    protected static final HashMap<String, Hue> namedHues = new HashMap<String, Hue>();
    protected static final ArrayList<Hue> primaryHues = new ArrayList<Hue>();

    public static final Hue RED = new Hue("red", 0, true);
    public static final Hue ORANGE = new Hue("orange", 30 / 360.0f, true);
    public static final Hue YELLOW = new Hue("yellow", 60 / 360.0f, true);
    public static final Hue LIME = new Hue("lime", 90 / 360.0f);
    public static final Hue GREEN = new Hue("green", 120 / 360.0f, true);
    public static final Hue TEAL = new Hue("teal", 150 / 360.0f);
    public static final Hue CYAN = new Hue("cyan", 180 / 360.0f);
    public static final Hue AZURE = new Hue("azure", 210 / 360.0f);
    public static final Hue BLUE = new Hue("blue", 240 / 360.0f, true);
    public static final Hue INDIGO = new Hue("indigo", 270 / 360.0f);
    public static final Hue PURPLE = new Hue("purple", 300 / 360.0f, true);
    public static final Hue PINK = new Hue("pink", 330 / 360.0f, true);

    /**
     * Tolerance value for checking if a given hue is primary (default 0.01)
     */
    public static float PRIMARY_VARIANCE = 0.01f;

    /**
     * Finds the closest defined & named Hue for the given hue value.
     * Optionally, the search can be limited to primary hues only.
     * 
     * @param hue
     *            normalized hue (0.0 ... 1.0) will be automatically wrapped
     * @param primaryOnly
     *            only consider the 7 primary hues
     * @return closest Hue instance
     */
    public static final Hue getClosest(float hue, boolean primaryOnly) {
        hue %= 1;
        float dist = Float.MAX_VALUE;
        Hue closest = null;
        Iterable<Hue> hues = (primaryOnly ? primaryHues : namedHues.values());
        for (Hue h : hues) {
            float d = MathUtils.min(MathUtils.abs(h.hue - hue),
                    MathUtils.abs(1 + h.hue - hue));
            if (d < dist) {
                dist = d;
                closest = h;
            }
        }
        return closest;
    }

    public static final Hue getForName(String name) {
        return namedHues.get(name.toLowerCase());
    }

    public static boolean isPrimary(float hue) {
        return isPrimary(hue, PRIMARY_VARIANCE);
    }

    public static boolean isPrimary(float hue, float variance) {
        boolean isPrimary = false;
        for (Hue h : primaryHues) {
            if (MathUtils.abs(hue - h.hue) < variance) {
                isPrimary = true;
                break;
            }
        }
        return isPrimary;
    }

    protected String name;

    protected float hue;

    protected boolean isPrimary;

    public Hue(String name, float hue) {
        this(name, hue, false);
    }

    public Hue(String name, float hue, boolean isPrimary) {
        this.name = name;
        this.hue = hue;
        this.isPrimary = isPrimary;
        namedHues.put(name, this);
        if (isPrimary) {
            primaryHues.add(this);
        }
    }

    public float getHue() {
        return hue;
    }

    public String getName() {
        return name;
    }

    public boolean isPrimary() {
        return isPrimary;
    }

    @Override
    public String toString() {
        return "Hue: ID:" + name + " @ " + (int) (hue * 360) + " degrees";
    }
}