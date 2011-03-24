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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Defines a list of named colors across the spectrum and provides a means to
 * access them by name (strings) dynamically (e.g. using config files or when
 * building {@link ColorTheme}s)
 */
public class NamedColor {

    public static final ReadonlyTColor ALICEBLUE = TColor.newRGB(0.94f, 0.97f,
            1.00f);
    public static final ReadonlyTColor ANTIQUEWHITE = TColor.newRGB(0.98f,
            0.92f, 0.84f);
    public static final ReadonlyTColor AQUA = TColor
            .newRGB(0.00f, 1.00f, 1.00f);
    public static final ReadonlyTColor AQUAMARINE = TColor.newRGB(0.50f, 1.00f,
            0.83f);
    public static final ReadonlyTColor AZURE = TColor.newRGB(0.94f, 1.00f,
            1.00f);
    public static final ReadonlyTColor BARK = TColor
            .newRGB(0.25f, 0.19f, 0.13f);
    public static final ReadonlyTColor BEIGE = TColor.newRGB(0.96f, 0.96f,
            0.86f);
    public static final ReadonlyTColor BISQUE = TColor.newRGB(1.00f, 0.89f,
            0.77f);
    public static final ReadonlyTColor BLACK = TColor.newRGB(0.00f, 0.00f,
            0.00f);
    public static final ReadonlyTColor BLANCHEDALMOND = TColor.newRGB(1.00f,
            0.92f, 0.80f);
    public static final ReadonlyTColor BLUE = TColor
            .newRGB(0.00f, 0.00f, 1.00f);
    public static final ReadonlyTColor BLUEVIOLET = TColor.newRGB(0.54f, 0.17f,
            0.89f);
    public static final ReadonlyTColor BROWN = TColor.newRGB(0.65f, 0.16f,
            0.16f);
    public static final ReadonlyTColor BURLYWOOD = TColor.newRGB(0.87f, 0.72f,
            0.53f);
    public static final ReadonlyTColor CADETBLUE = TColor.newRGB(0.37f, 0.62f,
            0.63f);
    public static final ReadonlyTColor CHARTREUSE = TColor.newRGB(0.50f, 1.00f,
            0.00f);
    public static final ReadonlyTColor CHOCOLATE = TColor.newRGB(0.82f, 0.41f,
            0.12f);
    public static final ReadonlyTColor CORAL = TColor.newRGB(1.00f, 0.50f,
            0.31f);
    public static final ReadonlyTColor CORNFLOWERBLUE = TColor.newRGB(0.39f,
            0.58f, 0.93f);
    public static final ReadonlyTColor CORNSILK = TColor.newRGB(1.00f, 0.97f,
            0.86f);
    public static final ReadonlyTColor CRIMSON = TColor.newRGB(0.86f, 0.08f,
            0.24f);
    public static final ReadonlyTColor CYAN = TColor
            .newRGB(0.00f, 0.68f, 0.94f);
    public static final ReadonlyTColor DARKBLUE = TColor.newRGB(0.00f, 0.00f,
            0.55f);
    public static final ReadonlyTColor DARKCYAN = TColor.newRGB(0.00f, 0.55f,
            0.55f);
    public static final ReadonlyTColor DARKGOLDENROD = TColor.newRGB(0.72f,
            0.53f, 0.04f);
    public static final ReadonlyTColor DARKGRAY = TColor.newRGB(0.66f, 0.66f,
            0.66f);
    public static final ReadonlyTColor DARKGREEN = TColor.newRGB(0.00f, 0.39f,
            0.00f);
    public static final ReadonlyTColor DARKKHAKI = TColor.newRGB(0.74f, 0.72f,
            0.42f);
    public static final ReadonlyTColor DARKMAGENTA = TColor.newRGB(0.55f,
            0.00f, 0.55f);
    public static final ReadonlyTColor DARKOLIVEGREEN = TColor.newRGB(0.33f,
            0.42f, 0.18f);
    public static final ReadonlyTColor DARKORANGE = TColor.newRGB(1.00f, 0.55f,
            0.00f);
    public static final ReadonlyTColor DARKORCHID = TColor.newRGB(0.60f, 0.20f,
            0.80f);
    public static final ReadonlyTColor DARKRED = TColor.newRGB(0.55f, 0.00f,
            0.00f);
    public static final ReadonlyTColor DARKSALMON = TColor.newRGB(0.91f, 0.59f,
            0.48f);
    public static final ReadonlyTColor DARKSEAGREEN = TColor.newRGB(0.56f,
            0.74f, 0.56f);
    public static final ReadonlyTColor DARKSLATEBLUE = TColor.newRGB(0.28f,
            0.24f, 0.55f);
    public static final ReadonlyTColor DARKSLATEGRAY = TColor.newRGB(0.18f,
            0.31f, 0.31f);
    public static final ReadonlyTColor DARKTURQUOISE = TColor.newRGB(0.00f,
            0.81f, 0.82f);
    public static final ReadonlyTColor DARKVIOLET = TColor.newRGB(0.58f, 0.00f,
            0.83f);
    public static final ReadonlyTColor DEEPPINK = TColor.newRGB(1.00f, 0.08f,
            0.58f);
    public static final ReadonlyTColor DEEPSKYBLUE = TColor.newRGB(0.00f,
            0.75f, 1.00f);
    public static final ReadonlyTColor DIMGRAY = TColor.newRGB(0.41f, 0.41f,
            0.41f);
    public static final ReadonlyTColor DIMGREY = TColor.newRGB(0.41f, 0.41f,
            0.41f);
    public static final ReadonlyTColor DODGERBLUE = TColor.newRGB(0.12f, 0.56f,
            1.00f);
    public static final ReadonlyTColor FIREBRICK = TColor.newRGB(0.70f, 0.13f,
            0.13f);
    public static final ReadonlyTColor FLORALWHITE = TColor.newRGB(1.00f,
            0.98f, 0.94f);
    public static final ReadonlyTColor FORESTGREEN = TColor.newRGB(0.13f,
            0.55f, 0.13f);
    public static final ReadonlyTColor FUCHSIA = TColor.newRGB(1.00f, 0.00f,
            1.00f);
    public static final ReadonlyTColor GAINSBORO = TColor.newRGB(0.86f, 0.86f,
            0.86f);
    public static final ReadonlyTColor GHOSTWHITE = TColor.newRGB(0.97f, 0.97f,
            1.00f);
    public static final ReadonlyTColor GOLD = TColor
            .newRGB(1.00f, 0.84f, 0.00f);
    public static final ReadonlyTColor GOLDENROD = TColor.newRGB(0.85f, 0.65f,
            0.13f);
    public static final ReadonlyTColor GRAY = TColor
            .newRGB(0.50f, 0.50f, 0.50f);
    public static final ReadonlyTColor GREEN = TColor.newRGB(0.00f, 0.50f,
            0.00f);
    public static final ReadonlyTColor GREENYELLOW = TColor.newRGB(0.68f,
            1.00f, 0.18f);
    public static final ReadonlyTColor GREY = TColor
            .newRGB(0.50f, 0.50f, 0.50f);
    public static final ReadonlyTColor HONEYDEW = TColor.newRGB(0.94f, 1.00f,
            0.94f);
    public static final ReadonlyTColor HOTPINK = TColor.newRGB(1.00f, 0.41f,
            0.71f);
    public static final ReadonlyTColor INDIANRED = TColor.newRGB(0.80f, 0.36f,
            0.36f);
    public static final ReadonlyTColor INDIGO = TColor.newRGB(0.29f, 0.00f,
            0.51f);
    public static final ReadonlyTColor IVORY = TColor.newRGB(1.00f, 1.00f,
            0.94f);
    public static final ReadonlyTColor KHAKI = TColor.newRGB(0.94f, 0.90f,
            0.55f);
    public static final ReadonlyTColor LAVENDER = TColor.newRGB(0.90f, 0.90f,
            0.98f);
    public static final ReadonlyTColor LAVENDERBLUSH = TColor.newRGB(1.00f,
            0.94f, 0.96f);
    public static final ReadonlyTColor LAWNGREEN = TColor.newRGB(0.49f, 0.99f,
            0.00f);
    public static final ReadonlyTColor LEMONCHIFFON = TColor.newRGB(1.00f,
            0.98f, 0.80f);
    public static final ReadonlyTColor LIGHTBLUE = TColor.newRGB(0.68f, 0.85f,
            0.90f);
    public static final ReadonlyTColor LIGHTCORAL = TColor.newRGB(0.94f, 0.50f,
            0.50f);
    public static final ReadonlyTColor LIGHTCYAN = TColor.newRGB(0.88f, 1.00f,
            1.00f);
    public static final ReadonlyTColor LIGHTGOLDENRODYELLOW = TColor.newRGB(
            0.98f, 0.98f, 0.82f);
    public static final ReadonlyTColor LIGHTGREEN = TColor.newRGB(0.56f, 0.93f,
            0.56f);
    public static final ReadonlyTColor LIGHTGREY = TColor.newRGB(0.83f, 0.83f,
            0.83f);
    public static final ReadonlyTColor LIGHTPINK = TColor.newRGB(1.00f, 0.71f,
            0.76f);
    public static final ReadonlyTColor LIGHTSALMON = TColor.newRGB(1.00f,
            0.63f, 0.48f);
    public static final ReadonlyTColor LIGHTSEAGREEN = TColor.newRGB(0.13f,
            0.70f, 0.67f);
    public static final ReadonlyTColor LIGHTSKYBLUE = TColor.newRGB(0.53f,
            0.81f, 0.98f);
    public static final ReadonlyTColor LIGHTSLATEGRAY = TColor.newRGB(0.47f,
            0.53f, 0.60f);
    public static final ReadonlyTColor LIGHTSTEELBLUE = TColor.newRGB(0.69f,
            0.77f, 0.87f);
    public static final ReadonlyTColor LIGHTYELLOW = TColor.newRGB(1.00f,
            1.00f, 0.88f);
    public static final ReadonlyTColor LIME = TColor
            .newRGB(0.00f, 1.00f, 0.00f);
    public static final ReadonlyTColor LIMEGREEN = TColor.newRGB(0.20f, 0.80f,
            0.20f);
    public static final ReadonlyTColor LINEN = TColor.newRGB(0.98f, 0.94f,
            0.90f);
    public static final ReadonlyTColor MAROON = TColor.newRGB(0.50f, 0.00f,
            0.00f);
    public static final ReadonlyTColor MEDIUMAQUAMARINE = TColor.newRGB(0.40f,
            0.80f, 0.67f);
    public static final ReadonlyTColor MEDIUMBLUE = TColor.newRGB(0.00f, 0.00f,
            0.80f);
    public static final ReadonlyTColor MEDIUMORCHID = TColor.newRGB(0.73f,
            0.33f, 0.83f);
    public static final ReadonlyTColor MEDIUMPURPLE = TColor.newRGB(0.58f,
            0.44f, 0.86f);
    public static final ReadonlyTColor MEDIUMSEAGREEN = TColor.newRGB(0.24f,
            0.70f, 0.44f);
    public static final ReadonlyTColor MEDIUMSLATEBLUE = TColor.newRGB(0.48f,
            0.41f, 0.93f);
    public static final ReadonlyTColor MEDIUMSPRINGGREEN = TColor.newRGB(0.00f,
            0.98f, 0.60f);
    public static final ReadonlyTColor MEDIUMTURQUOISE = TColor.newRGB(0.28f,
            0.82f, 0.80f);
    public static final ReadonlyTColor MEDIUMVIOLETRED = TColor.newRGB(0.78f,
            0.08f, 0.52f);
    public static final ReadonlyTColor MIDNIGHTBLUE = TColor.newRGB(0.10f,
            0.10f, 0.44f);
    public static final ReadonlyTColor MINTCREAM = TColor.newRGB(0.96f, 1.00f,
            0.98f);
    public static final ReadonlyTColor MISTYROSE = TColor.newRGB(1.00f, 0.89f,
            0.88f);
    public static final ReadonlyTColor MOCCASIN = TColor.newRGB(1.00f, 0.89f,
            0.71f);
    public static final ReadonlyTColor NAVAJOWHITE = TColor.newRGB(1.00f,
            0.87f, 0.68f);
    public static final ReadonlyTColor NAVY = TColor
            .newRGB(0.00f, 0.00f, 0.50f);
    public static final ReadonlyTColor OLDLACE = TColor.newRGB(0.99f, 0.96f,
            0.90f);
    public static final ReadonlyTColor OLIVE = TColor.newRGB(0.50f, 0.50f,
            0.00f);
    public static final ReadonlyTColor OLIVEDRAB = TColor.newRGB(0.42f, 0.56f,
            0.14f);
    public static final ReadonlyTColor ORANGE = TColor.newRGB(1.00f, 0.65f,
            0.00f);
    public static final ReadonlyTColor ORANGERED = TColor.newRGB(1.00f, 0.27f,
            0.00f);
    public static final ReadonlyTColor ORCHID = TColor.newRGB(0.85f, 0.44f,
            0.84f);
    public static final ReadonlyTColor PALEGOLDENROD = TColor.newRGB(0.93f,
            0.91f, 0.67f);
    public static final ReadonlyTColor PALEGREEN = TColor.newRGB(0.60f, 0.98f,
            0.60f);
    public static final ReadonlyTColor PALETURQUOISE = TColor.newRGB(0.69f,
            0.93f, 0.93f);
    public static final ReadonlyTColor PALEVIOLETRED = TColor.newRGB(0.86f,
            0.44f, 0.58f);
    public static final ReadonlyTColor PAPAYAWHIP = TColor.newRGB(1.00f, 0.94f,
            0.84f);
    public static final ReadonlyTColor PEACHPUFF = TColor.newRGB(1.00f, 0.85f,
            0.73f);
    public static final ReadonlyTColor PERU = TColor
            .newRGB(0.80f, 0.52f, 0.25f);
    public static final ReadonlyTColor PINK = TColor
            .newRGB(1.00f, 0.75f, 0.80f);
    public static final ReadonlyTColor PLUM = TColor
            .newRGB(0.87f, 0.63f, 0.87f);
    public static final ReadonlyTColor POWDERBLUE = TColor.newRGB(0.69f, 0.88f,
            0.90f);
    public static final ReadonlyTColor PURPLE = TColor.newRGB(0.50f, 0.00f,
            0.50f);
    public static final ReadonlyTColor RED = TColor.newRGB(1.00f, 0.00f, 0.00f);
    public static final ReadonlyTColor ROSYBROWN = TColor.newRGB(0.74f, 0.56f,
            0.56f);
    public static final ReadonlyTColor ROYALBLUE = TColor.newRGB(0.25f, 0.41f,
            0.88f);
    public static final ReadonlyTColor SADDLEBROWN = TColor.newRGB(0.55f,
            0.27f, 0.07f);
    public static final ReadonlyTColor SALMON = TColor.newRGB(0.98f, 0.50f,
            0.45f);
    public static final ReadonlyTColor SANDYBROWN = TColor.newRGB(0.96f, 0.64f,
            0.38f);
    public static final ReadonlyTColor SEAGREEN = TColor.newRGB(0.18f, 0.55f,
            0.34f);
    public static final ReadonlyTColor SEASHELL = TColor.newRGB(1.00f, 0.96f,
            0.93f);
    public static final ReadonlyTColor SIENNA = TColor.newRGB(0.63f, 0.32f,
            0.18f);
    public static final ReadonlyTColor SILVER = TColor.newRGB(0.75f, 0.75f,
            0.75f);
    public static final ReadonlyTColor SKYBLUE = TColor.newRGB(0.53f, 0.81f,
            0.92f);
    public static final ReadonlyTColor SLATEBLUE = TColor.newRGB(0.42f, 0.35f,
            0.80f);
    public static final ReadonlyTColor SLATEGRAY = TColor.newRGB(0.44f, 0.50f,
            0.56f);
    public static final ReadonlyTColor SNOW = TColor
            .newRGB(1.00f, 0.98f, 0.98f);
    public static final ReadonlyTColor SPRINGGREEN = TColor.newRGB(0.00f,
            1.00f, 0.50f);
    public static final ReadonlyTColor STEELBLUE = TColor.newRGB(0.27f, 0.51f,
            0.71f);
    public static final ReadonlyTColor TAN = TColor.newRGB(0.82f, 0.71f, 0.55f);
    public static final ReadonlyTColor TEAL = TColor
            .newRGB(0.00f, 0.50f, 0.50f);
    public static final ReadonlyTColor THISTLE = TColor.newRGB(0.85f, 0.75f,
            0.85f);
    public static final ReadonlyTColor TOMATO = TColor.newRGB(1.00f, 0.39f,
            0.28f);
    public static final ReadonlyTColor TRANSPARENT = TColor.newRGBA(0.00f,
            0.00f, 0.00f, 0.00f);
    public static final ReadonlyTColor TURQUOISE = TColor.newRGB(0.25f, 0.88f,
            0.82f);
    public static final ReadonlyTColor VIOLET = TColor.newRGB(0.93f, 0.51f,
            0.93f);
    public static final ReadonlyTColor WHEAT = TColor.newRGB(0.96f, 0.87f,
            0.07f);
    public static final ReadonlyTColor WHITE = TColor.newRGB(1.00f, 1.00f,
            1.00f);
    public static final ReadonlyTColor WHITESMOKE = TColor.newRGB(0.96f, 0.96f,
            0.96f);
    public static final ReadonlyTColor YELLOW = TColor.newRGB(1.00f, 1.00f,
            0.00f);
    public static final ReadonlyTColor YELLOWGREEN = TColor.newRGB(0.60f,
            0.80f, 0.20f);

    protected static final HashMap<String, ReadonlyTColor> namedColorMap = new HashMap<String, ReadonlyTColor>();

    static {
        Field[] fields = NamedColor.class.getDeclaredFields();
        try {
            for (Field f : fields) {
                if (f.getType() == ReadonlyTColor.class) {
                    String id = f.getName();
                    namedColorMap.put(id, (ReadonlyTColor) f.get(null));
                }
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns a color for the given name.
     * 
     * @param name
     * @return color or null, if name not found.
     */
    public static final ReadonlyTColor getForName(String name) {
        return namedColorMap.get(name.toUpperCase());
    }

    /**
     * Returns the names of all defined colors.
     * 
     * @return list of name
     */
    public static final ArrayList<String> getNames() {
        return new ArrayList<String>(namedColorMap.keySet());
    }
}
