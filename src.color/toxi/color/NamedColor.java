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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Defines a list of named colors across the spectrum and provides a means to
 * access them by name (strings) dynamically (e.g. using config files or when
 * building {@link ColorTheme}s)
 * 
 * @author toxi
 * 
 */
public class NamedColor {

	public static final TColor ALICEBLUE = TColor.newRGB(0.94f, 0.97f, 1.00f);
	public static final TColor ANTIQUEWHITE = TColor
			.newRGB(0.98f, 0.92f, 0.84f);
	public static final TColor AQUA = TColor.newRGB(0.00f, 1.00f, 1.00f);
	public static final TColor AQUAMARINE = TColor.newRGB(0.50f, 1.00f, 0.83f);
	public static final TColor AZURE = TColor.newRGB(0.94f, 1.00f, 1.00f);
	public static final TColor BARK = TColor.newRGB(0.25f, 0.19f, 0.13f);
	public static final TColor BEIGE = TColor.newRGB(0.96f, 0.96f, 0.86f);
	public static final TColor BISQUE = TColor.newRGB(1.00f, 0.89f, 0.77f);
	public static final TColor BLACK = TColor.newRGB(0.00f, 0.00f, 0.00f);
	public static final TColor BLANCHEDALMOND = TColor.newRGB(1.00f, 0.92f,
			0.80f);
	public static final TColor BLUE = TColor.newRGB(0.00f, 0.00f, 1.00f);
	public static final TColor BLUEVIOLET = TColor.newRGB(0.54f, 0.17f, 0.89f);
	public static final TColor BROWN = TColor.newRGB(0.65f, 0.16f, 0.16f);
	public static final TColor BURLYWOOD = TColor.newRGB(0.87f, 0.72f, 0.53f);
	public static final TColor CADETBLUE = TColor.newRGB(0.37f, 0.62f, 0.63f);
	public static final TColor CHARTREUSE = TColor.newRGB(0.50f, 1.00f, 0.00f);
	public static final TColor CHOCOLATE = TColor.newRGB(0.82f, 0.41f, 0.12f);
	public static final TColor CORAL = TColor.newRGB(1.00f, 0.50f, 0.31f);
	public static final TColor CORNFLOWERBLUE = TColor.newRGB(0.39f, 0.58f,
			0.93f);
	public static final TColor CORNSILK = TColor.newRGB(1.00f, 0.97f, 0.86f);
	public static final TColor CRIMSON = TColor.newRGB(0.86f, 0.08f, 0.24f);
	public static final TColor CYAN = TColor.newRGB(0.00f, 0.68f, 0.94f);
	public static final TColor DARKBLUE = TColor.newRGB(0.00f, 0.00f, 0.55f);
	public static final TColor DARKCYAN = TColor.newRGB(0.00f, 0.55f, 0.55f);
	public static final TColor DARKGOLDENROD = TColor.newRGB(0.72f, 0.53f,
			0.04f);
	public static final TColor DARKGRAY = TColor.newRGB(0.66f, 0.66f, 0.66f);
	public static final TColor DARKGREEN = TColor.newRGB(0.00f, 0.39f, 0.00f);
	public static final TColor DARKKHAKI = TColor.newRGB(0.74f, 0.72f, 0.42f);
	public static final TColor DARKMAGENTA = TColor.newRGB(0.55f, 0.00f, 0.55f);
	public static final TColor DARKOLIVEGREEN = TColor.newRGB(0.33f, 0.42f,
			0.18f);
	public static final TColor DARKORANGE = TColor.newRGB(1.00f, 0.55f, 0.00f);
	public static final TColor DARKORCHID = TColor.newRGB(0.60f, 0.20f, 0.80f);
	public static final TColor DARKRED = TColor.newRGB(0.55f, 0.00f, 0.00f);
	public static final TColor DARKSALMON = TColor.newRGB(0.91f, 0.59f, 0.48f);
	public static final TColor DARKSEAGREEN = TColor
			.newRGB(0.56f, 0.74f, 0.56f);
	public static final TColor DARKSLATEBLUE = TColor.newRGB(0.28f, 0.24f,
			0.55f);
	public static final TColor DARKSLATEGRAY = TColor.newRGB(0.18f, 0.31f,
			0.31f);
	public static final TColor DARKTURQUOISE = TColor.newRGB(0.00f, 0.81f,
			0.82f);
	public static final TColor DARKVIOLET = TColor.newRGB(0.58f, 0.00f, 0.83f);
	public static final TColor DEEPPINK = TColor.newRGB(1.00f, 0.08f, 0.58f);
	public static final TColor DEEPSKYBLUE = TColor.newRGB(0.00f, 0.75f, 1.00f);
	public static final TColor DIMGRAY = TColor.newRGB(0.41f, 0.41f, 0.41f);
	public static final TColor DIMGREY = TColor.newRGB(0.41f, 0.41f, 0.41f);
	public static final TColor DODGERBLUE = TColor.newRGB(0.12f, 0.56f, 1.00f);
	public static final TColor FIREBRICK = TColor.newRGB(0.70f, 0.13f, 0.13f);
	public static final TColor FLORALWHITE = TColor.newRGB(1.00f, 0.98f, 0.94f);
	public static final TColor FORESTGREEN = TColor.newRGB(0.13f, 0.55f, 0.13f);
	public static final TColor FUCHSIA = TColor.newRGB(1.00f, 0.00f, 1.00f);
	public static final TColor GAINSBORO = TColor.newRGB(0.86f, 0.86f, 0.86f);
	public static final TColor GHOSTWHITE = TColor.newRGB(0.97f, 0.97f, 1.00f);
	public static final TColor GOLD = TColor.newRGB(1.00f, 0.84f, 0.00f);
	public static final TColor GOLDENROD = TColor.newRGB(0.85f, 0.65f, 0.13f);
	public static final TColor GRAY = TColor.newRGB(0.50f, 0.50f, 0.50f);
	public static final TColor GREEN = TColor.newRGB(0.00f, 0.50f, 0.00f);
	public static final TColor GREENYELLOW = TColor.newRGB(0.68f, 1.00f, 0.18f);
	public static final TColor GREY = TColor.newRGB(0.50f, 0.50f, 0.50f);
	public static final TColor HONEYDEW = TColor.newRGB(0.94f, 1.00f, 0.94f);
	public static final TColor HOTPINK = TColor.newRGB(1.00f, 0.41f, 0.71f);
	public static final TColor INDIANRED = TColor.newRGB(0.80f, 0.36f, 0.36f);
	public static final TColor INDIGO = TColor.newRGB(0.29f, 0.00f, 0.51f);
	public static final TColor IVORY = TColor.newRGB(1.00f, 1.00f, 0.94f);
	public static final TColor KHAKI = TColor.newRGB(0.94f, 0.90f, 0.55f);
	public static final TColor LAVENDER = TColor.newRGB(0.90f, 0.90f, 0.98f);
	public static final TColor LAVENDERBLUSH = TColor.newRGB(1.00f, 0.94f,
			0.96f);
	public static final TColor LAWNGREEN = TColor.newRGB(0.49f, 0.99f, 0.00f);
	public static final TColor LEMONCHIFFON = TColor
			.newRGB(1.00f, 0.98f, 0.80f);
	public static final TColor LIGHTBLUE = TColor.newRGB(0.68f, 0.85f, 0.90f);
	public static final TColor LIGHTCORAL = TColor.newRGB(0.94f, 0.50f, 0.50f);
	public static final TColor LIGHTCYAN = TColor.newRGB(0.88f, 1.00f, 1.00f);
	public static final TColor LIGHTGOLDENRODYELLOW = TColor.newRGB(0.98f,
			0.98f, 0.82f);
	public static final TColor LIGHTGREEN = TColor.newRGB(0.56f, 0.93f, 0.56f);
	public static final TColor LIGHTGREY = TColor.newRGB(0.83f, 0.83f, 0.83f);
	public static final TColor LIGHTPINK = TColor.newRGB(1.00f, 0.71f, 0.76f);
	public static final TColor LIGHTSALMON = TColor.newRGB(1.00f, 0.63f, 0.48f);
	public static final TColor LIGHTSEAGREEN = TColor.newRGB(0.13f, 0.70f,
			0.67f);
	public static final TColor LIGHTSKYBLUE = TColor
			.newRGB(0.53f, 0.81f, 0.98f);
	public static final TColor LIGHTSLATEGRAY = TColor.newRGB(0.47f, 0.53f,
			0.60f);
	public static final TColor LIGHTSTEELBLUE = TColor.newRGB(0.69f, 0.77f,
			0.87f);
	public static final TColor LIGHTYELLOW = TColor.newRGB(1.00f, 1.00f, 0.88f);
	public static final TColor LIME = TColor.newRGB(0.00f, 1.00f, 0.00f);
	public static final TColor LIMEGREEN = TColor.newRGB(0.20f, 0.80f, 0.20f);
	public static final TColor LINEN = TColor.newRGB(0.98f, 0.94f, 0.90f);
	public static final TColor MAROON = TColor.newRGB(0.50f, 0.00f, 0.00f);
	public static final TColor MEDIUMAQUAMARINE = TColor.newRGB(0.40f, 0.80f,
			0.67f);
	public static final TColor MEDIUMBLUE = TColor.newRGB(0.00f, 0.00f, 0.80f);
	public static final TColor MEDIUMORCHID = TColor
			.newRGB(0.73f, 0.33f, 0.83f);
	public static final TColor MEDIUMPURPLE = TColor
			.newRGB(0.58f, 0.44f, 0.86f);
	public static final TColor MEDIUMSEAGREEN = TColor.newRGB(0.24f, 0.70f,
			0.44f);
	public static final TColor MEDIUMSLATEBLUE = TColor.newRGB(0.48f, 0.41f,
			0.93f);
	public static final TColor MEDIUMSPRINGGREEN = TColor.newRGB(0.00f, 0.98f,
			0.60f);
	public static final TColor MEDIUMTURQUOISE = TColor.newRGB(0.28f, 0.82f,
			0.80f);
	public static final TColor MEDIUMVIOLETRED = TColor.newRGB(0.78f, 0.08f,
			0.52f);
	public static final TColor MIDNIGHTBLUE = TColor
			.newRGB(0.10f, 0.10f, 0.44f);
	public static final TColor MINTCREAM = TColor.newRGB(0.96f, 1.00f, 0.98f);
	public static final TColor MISTYROSE = TColor.newRGB(1.00f, 0.89f, 0.88f);
	public static final TColor MOCCASIN = TColor.newRGB(1.00f, 0.89f, 0.71f);
	public static final TColor NAVAJOWHITE = TColor.newRGB(1.00f, 0.87f, 0.68f);
	public static final TColor NAVY = TColor.newRGB(0.00f, 0.00f, 0.50f);
	public static final TColor OLDLACE = TColor.newRGB(0.99f, 0.96f, 0.90f);
	public static final TColor OLIVE = TColor.newRGB(0.50f, 0.50f, 0.00f);
	public static final TColor OLIVEDRAB = TColor.newRGB(0.42f, 0.56f, 0.14f);
	public static final TColor ORANGE = TColor.newRGB(1.00f, 0.65f, 0.00f);
	public static final TColor ORANGERED = TColor.newRGB(1.00f, 0.27f, 0.00f);
	public static final TColor ORCHID = TColor.newRGB(0.85f, 0.44f, 0.84f);
	public static final TColor PALEGOLDENROD = TColor.newRGB(0.93f, 0.91f,
			0.67f);
	public static final TColor PALEGREEN = TColor.newRGB(0.60f, 0.98f, 0.60f);
	public static final TColor PALETURQUOISE = TColor.newRGB(0.69f, 0.93f,
			0.93f);
	public static final TColor PALEVIOLETRED = TColor.newRGB(0.86f, 0.44f,
			0.58f);
	public static final TColor PAPAYAWHIP = TColor.newRGB(1.00f, 0.94f, 0.84f);
	public static final TColor PEACHPUFF = TColor.newRGB(1.00f, 0.85f, 0.73f);
	public static final TColor PERU = TColor.newRGB(0.80f, 0.52f, 0.25f);
	public static final TColor PINK = TColor.newRGB(1.00f, 0.75f, 0.80f);
	public static final TColor PLUM = TColor.newRGB(0.87f, 0.63f, 0.87f);
	public static final TColor POWDERBLUE = TColor.newRGB(0.69f, 0.88f, 0.90f);
	public static final TColor PURPLE = TColor.newRGB(0.50f, 0.00f, 0.50f);
	public static final TColor RED = TColor.newRGB(1.00f, 0.00f, 0.00f);
	public static final TColor ROSYBROWN = TColor.newRGB(0.74f, 0.56f, 0.56f);
	public static final TColor ROYALBLUE = TColor.newRGB(0.25f, 0.41f, 0.88f);
	public static final TColor SADDLEBROWN = TColor.newRGB(0.55f, 0.27f, 0.07f);
	public static final TColor SALMON = TColor.newRGB(0.98f, 0.50f, 0.45f);
	public static final TColor SANDYBROWN = TColor.newRGB(0.96f, 0.64f, 0.38f);
	public static final TColor SEAGREEN = TColor.newRGB(0.18f, 0.55f, 0.34f);
	public static final TColor SEASHELL = TColor.newRGB(1.00f, 0.96f, 0.93f);
	public static final TColor SIENNA = TColor.newRGB(0.63f, 0.32f, 0.18f);
	public static final TColor SILVER = TColor.newRGB(0.75f, 0.75f, 0.75f);
	public static final TColor SKYBLUE = TColor.newRGB(0.53f, 0.81f, 0.92f);
	public static final TColor SLATEBLUE = TColor.newRGB(0.42f, 0.35f, 0.80f);
	public static final TColor SLATEGRAY = TColor.newRGB(0.44f, 0.50f, 0.56f);
	public static final TColor SNOW = TColor.newRGB(1.00f, 0.98f, 0.98f);
	public static final TColor SPRINGGREEN = TColor.newRGB(0.00f, 1.00f, 0.50f);
	public static final TColor STEELBLUE = TColor.newRGB(0.27f, 0.51f, 0.71f);
	public static final TColor TAN = TColor.newRGB(0.82f, 0.71f, 0.55f);
	public static final TColor TEAL = TColor.newRGB(0.00f, 0.50f, 0.50f);
	public static final TColor THISTLE = TColor.newRGB(0.85f, 0.75f, 0.85f);
	public static final TColor TOMATO = TColor.newRGB(1.00f, 0.39f, 0.28f);
	public static final TColor TRANSPARENT = TColor.newRGBA(0.00f, 0.00f,
			0.00f, 0.00f);
	public static final TColor TURQUOISE = TColor.newRGB(0.25f, 0.88f, 0.82f);
	public static final TColor VIOLET = TColor.newRGB(0.93f, 0.51f, 0.93f);
	public static final TColor WHEAT = TColor.newRGB(0.96f, 0.87f, 0.07f);
	public static final TColor WHITE = TColor.newRGB(1.00f, 1.00f, 1.00f);
	public static final TColor WHITESMOKE = TColor.newRGB(0.96f, 0.96f, 0.96f);
	public static final TColor YELLOW = TColor.newRGB(1.00f, 1.00f, 0.00f);
	public static final TColor YELLOWGREEN = TColor.newRGB(0.60f, 0.80f, 0.20f);

	protected static final HashMap<String, TColor> namedColorMap = new HashMap<String, TColor>();

	static {
		Field[] fields = NamedColor.class.getDeclaredFields();
		try {
			for (Field f : fields) {
				if (f.getType() == TColor.class) {
					String id = f.getName();
					namedColorMap.put(id, (TColor) f.get(null));
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
	public static final TColor getForName(String name) {
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
