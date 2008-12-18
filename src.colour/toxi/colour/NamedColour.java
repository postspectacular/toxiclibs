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
package toxi.colour;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Defines a list of named colours across the spectrum and provides a means to
 * access them by name (strings) dynamically (e.g. using config files or when
 * building {@link ColourTheme}s)
 * 
 * @author toxi
 * 
 */
public class NamedColour {

	public static final Colour ALICEBLUE = Colour.newRGB(0.94f, 0.97f, 1.00f);
	public static final Colour ANTIQUEWHITE = Colour
			.newRGB(0.98f, 0.92f, 0.84f);
	public static final Colour AQUA = Colour.newRGB(0.00f, 1.00f, 1.00f);
	public static final Colour AQUAMARINE = Colour.newRGB(0.50f, 1.00f, 0.83f);
	public static final Colour AZURE = Colour.newRGB(0.94f, 1.00f, 1.00f);
	public static final Colour BARK = Colour.newRGB(0.25f, 0.19f, 0.13f);
	public static final Colour BEIGE = Colour.newRGB(0.96f, 0.96f, 0.86f);
	public static final Colour BISQUE = Colour.newRGB(1.00f, 0.89f, 0.77f);
	public static final Colour BLACK = Colour.newRGB(0.00f, 0.00f, 0.00f);
	public static final Colour BLANCHEDALMOND = Colour.newRGB(1.00f, 0.92f,
			0.80f);
	public static final Colour BLUE = Colour.newRGB(0.00f, 0.00f, 1.00f);
	public static final Colour BLUEVIOLET = Colour.newRGB(0.54f, 0.17f, 0.89f);
	public static final Colour BROWN = Colour.newRGB(0.65f, 0.16f, 0.16f);
	public static final Colour BURLYWOOD = Colour.newRGB(0.87f, 0.72f, 0.53f);
	public static final Colour CADETBLUE = Colour.newRGB(0.37f, 0.62f, 0.63f);
	public static final Colour CHARTREUSE = Colour.newRGB(0.50f, 1.00f, 0.00f);
	public static final Colour CHOCOLATE = Colour.newRGB(0.82f, 0.41f, 0.12f);
	public static final Colour CORAL = Colour.newRGB(1.00f, 0.50f, 0.31f);
	public static final Colour CORNFLOWERBLUE = Colour.newRGB(0.39f, 0.58f,
			0.93f);
	public static final Colour CORNSILK = Colour.newRGB(1.00f, 0.97f, 0.86f);
	public static final Colour CRIMSON = Colour.newRGB(0.86f, 0.08f, 0.24f);
	public static final Colour CYAN = Colour.newRGB(0.00f, 0.68f, 0.94f);
	public static final Colour DARKBLUE = Colour.newRGB(0.00f, 0.00f, 0.55f);
	public static final Colour DARKCYAN = Colour.newRGB(0.00f, 0.55f, 0.55f);
	public static final Colour DARKGOLDENROD = Colour.newRGB(0.72f, 0.53f,
			0.04f);
	public static final Colour DARKGRAY = Colour.newRGB(0.66f, 0.66f, 0.66f);
	public static final Colour DARKGREEN = Colour.newRGB(0.00f, 0.39f, 0.00f);
	public static final Colour DARKKHAKI = Colour.newRGB(0.74f, 0.72f, 0.42f);
	public static final Colour DARKMAGENTA = Colour.newRGB(0.55f, 0.00f, 0.55f);
	public static final Colour DARKOLIVEGREEN = Colour.newRGB(0.33f, 0.42f,
			0.18f);
	public static final Colour DARKORANGE = Colour.newRGB(1.00f, 0.55f, 0.00f);
	public static final Colour DARKORCHID = Colour.newRGB(0.60f, 0.20f, 0.80f);
	public static final Colour DARKRED = Colour.newRGB(0.55f, 0.00f, 0.00f);
	public static final Colour DARKSALMON = Colour.newRGB(0.91f, 0.59f, 0.48f);
	public static final Colour DARKSEAGREEN = Colour
			.newRGB(0.56f, 0.74f, 0.56f);
	public static final Colour DARKSLATEBLUE = Colour.newRGB(0.28f, 0.24f,
			0.55f);
	public static final Colour DARKSLATEGRAY = Colour.newRGB(0.18f, 0.31f,
			0.31f);
	public static final Colour DARKTURQUOISE = Colour.newRGB(0.00f, 0.81f,
			0.82f);
	public static final Colour DARKVIOLET = Colour.newRGB(0.58f, 0.00f, 0.83f);
	public static final Colour DEEPPINK = Colour.newRGB(1.00f, 0.08f, 0.58f);
	public static final Colour DEEPSKYBLUE = Colour.newRGB(0.00f, 0.75f, 1.00f);
	public static final Colour DIMGRAY = Colour.newRGB(0.41f, 0.41f, 0.41f);
	public static final Colour DIMGREY = Colour.newRGB(0.41f, 0.41f, 0.41f);
	public static final Colour DODGERBLUE = Colour.newRGB(0.12f, 0.56f, 1.00f);
	public static final Colour FIREBRICK = Colour.newRGB(0.70f, 0.13f, 0.13f);
	public static final Colour FLORALWHITE = Colour.newRGB(1.00f, 0.98f, 0.94f);
	public static final Colour FORESTGREEN = Colour.newRGB(0.13f, 0.55f, 0.13f);
	public static final Colour FUCHSIA = Colour.newRGB(1.00f, 0.00f, 1.00f);
	public static final Colour GAINSBORO = Colour.newRGB(0.86f, 0.86f, 0.86f);
	public static final Colour GHOSTWHITE = Colour.newRGB(0.97f, 0.97f, 1.00f);
	public static final Colour GOLD = Colour.newRGB(1.00f, 0.84f, 0.00f);
	public static final Colour GOLDENROD = Colour.newRGB(0.85f, 0.65f, 0.13f);
	public static final Colour GRAY = Colour.newRGB(0.50f, 0.50f, 0.50f);
	public static final Colour GREEN = Colour.newRGB(0.00f, 0.50f, 0.00f);
	public static final Colour GREENYELLOW = Colour.newRGB(0.68f, 1.00f, 0.18f);
	public static final Colour GREY = Colour.newRGB(0.50f, 0.50f, 0.50f);
	public static final Colour HONEYDEW = Colour.newRGB(0.94f, 1.00f, 0.94f);
	public static final Colour HOTPINK = Colour.newRGB(1.00f, 0.41f, 0.71f);
	public static final Colour INDIANRED = Colour.newRGB(0.80f, 0.36f, 0.36f);
	public static final Colour INDIGO = Colour.newRGB(0.29f, 0.00f, 0.51f);
	public static final Colour IVORY = Colour.newRGB(1.00f, 1.00f, 0.94f);
	public static final Colour KHAKI = Colour.newRGB(0.94f, 0.90f, 0.55f);
	public static final Colour LAVENDER = Colour.newRGB(0.90f, 0.90f, 0.98f);
	public static final Colour LAVENDERBLUSH = Colour.newRGB(1.00f, 0.94f,
			0.96f);
	public static final Colour LAWNGREEN = Colour.newRGB(0.49f, 0.99f, 0.00f);
	public static final Colour LEMONCHIFFON = Colour
			.newRGB(1.00f, 0.98f, 0.80f);
	public static final Colour LIGHTBLUE = Colour.newRGB(0.68f, 0.85f, 0.90f);
	public static final Colour LIGHTCORAL = Colour.newRGB(0.94f, 0.50f, 0.50f);
	public static final Colour LIGHTCYAN = Colour.newRGB(0.88f, 1.00f, 1.00f);
	public static final Colour LIGHTGOLDENRODYELLOW = Colour.newRGB(0.98f,
			0.98f, 0.82f);
	public static final Colour LIGHTGREEN = Colour.newRGB(0.56f, 0.93f, 0.56f);
	public static final Colour LIGHTGREY = Colour.newRGB(0.83f, 0.83f, 0.83f);
	public static final Colour LIGHTPINK = Colour.newRGB(1.00f, 0.71f, 0.76f);
	public static final Colour LIGHTSALMON = Colour.newRGB(1.00f, 0.63f, 0.48f);
	public static final Colour LIGHTSEAGREEN = Colour.newRGB(0.13f, 0.70f,
			0.67f);
	public static final Colour LIGHTSKYBLUE = Colour
			.newRGB(0.53f, 0.81f, 0.98f);
	public static final Colour LIGHTSLATEGRAY = Colour.newRGB(0.47f, 0.53f,
			0.60f);
	public static final Colour LIGHTSTEELBLUE = Colour.newRGB(0.69f, 0.77f,
			0.87f);
	public static final Colour LIGHTYELLOW = Colour.newRGB(1.00f, 1.00f, 0.88f);
	public static final Colour LIME = Colour.newRGB(0.00f, 1.00f, 0.00f);
	public static final Colour LIMEGREEN = Colour.newRGB(0.20f, 0.80f, 0.20f);
	public static final Colour LINEN = Colour.newRGB(0.98f, 0.94f, 0.90f);
	public static final Colour MAROON = Colour.newRGB(0.50f, 0.00f, 0.00f);
	public static final Colour MEDIUMAQUAMARINE = Colour.newRGB(0.40f, 0.80f,
			0.67f);
	public static final Colour MEDIUMBLUE = Colour.newRGB(0.00f, 0.00f, 0.80f);
	public static final Colour MEDIUMORCHID = Colour
			.newRGB(0.73f, 0.33f, 0.83f);
	public static final Colour MEDIUMPURPLE = Colour
			.newRGB(0.58f, 0.44f, 0.86f);
	public static final Colour MEDIUMSEAGREEN = Colour.newRGB(0.24f, 0.70f,
			0.44f);
	public static final Colour MEDIUMSLATEBLUE = Colour.newRGB(0.48f, 0.41f,
			0.93f);
	public static final Colour MEDIUMSPRINGGREEN = Colour.newRGB(0.00f, 0.98f,
			0.60f);
	public static final Colour MEDIUMTURQUOISE = Colour.newRGB(0.28f, 0.82f,
			0.80f);
	public static final Colour MEDIUMVIOLETRED = Colour.newRGB(0.78f, 0.08f,
			0.52f);
	public static final Colour MIDNIGHTBLUE = Colour
			.newRGB(0.10f, 0.10f, 0.44f);
	public static final Colour MINTCREAM = Colour.newRGB(0.96f, 1.00f, 0.98f);
	public static final Colour MISTYROSE = Colour.newRGB(1.00f, 0.89f, 0.88f);
	public static final Colour MOCCASIN = Colour.newRGB(1.00f, 0.89f, 0.71f);
	public static final Colour NAVAJOWHITE = Colour.newRGB(1.00f, 0.87f, 0.68f);
	public static final Colour NAVY = Colour.newRGB(0.00f, 0.00f, 0.50f);
	public static final Colour OLDLACE = Colour.newRGB(0.99f, 0.96f, 0.90f);
	public static final Colour OLIVE = Colour.newRGB(0.50f, 0.50f, 0.00f);
	public static final Colour OLIVEDRAB = Colour.newRGB(0.42f, 0.56f, 0.14f);
	public static final Colour ORANGE = Colour.newRGB(1.00f, 0.65f, 0.00f);
	public static final Colour ORANGERED = Colour.newRGB(1.00f, 0.27f, 0.00f);
	public static final Colour ORCHID = Colour.newRGB(0.85f, 0.44f, 0.84f);
	public static final Colour PALEGOLDENROD = Colour.newRGB(0.93f, 0.91f,
			0.67f);
	public static final Colour PALEGREEN = Colour.newRGB(0.60f, 0.98f, 0.60f);
	public static final Colour PALETURQUOISE = Colour.newRGB(0.69f, 0.93f,
			0.93f);
	public static final Colour PALEVIOLETRED = Colour.newRGB(0.86f, 0.44f,
			0.58f);
	public static final Colour PAPAYAWHIP = Colour.newRGB(1.00f, 0.94f, 0.84f);
	public static final Colour PEACHPUFF = Colour.newRGB(1.00f, 0.85f, 0.73f);
	public static final Colour PERU = Colour.newRGB(0.80f, 0.52f, 0.25f);
	public static final Colour PINK = Colour.newRGB(1.00f, 0.75f, 0.80f);
	public static final Colour PLUM = Colour.newRGB(0.87f, 0.63f, 0.87f);
	public static final Colour POWDERBLUE = Colour.newRGB(0.69f, 0.88f, 0.90f);
	public static final Colour PURPLE = Colour.newRGB(0.50f, 0.00f, 0.50f);
	public static final Colour RED = Colour.newRGB(1.00f, 0.00f, 0.00f);
	public static final Colour ROSYBROWN = Colour.newRGB(0.74f, 0.56f, 0.56f);
	public static final Colour ROYALBLUE = Colour.newRGB(0.25f, 0.41f, 0.88f);
	public static final Colour SADDLEBROWN = Colour.newRGB(0.55f, 0.27f, 0.07f);
	public static final Colour SALMON = Colour.newRGB(0.98f, 0.50f, 0.45f);
	public static final Colour SANDYBROWN = Colour.newRGB(0.96f, 0.64f, 0.38f);
	public static final Colour SEAGREEN = Colour.newRGB(0.18f, 0.55f, 0.34f);
	public static final Colour SEASHELL = Colour.newRGB(1.00f, 0.96f, 0.93f);
	public static final Colour SIENNA = Colour.newRGB(0.63f, 0.32f, 0.18f);
	public static final Colour SILVER = Colour.newRGB(0.75f, 0.75f, 0.75f);
	public static final Colour SKYBLUE = Colour.newRGB(0.53f, 0.81f, 0.92f);
	public static final Colour SLATEBLUE = Colour.newRGB(0.42f, 0.35f, 0.80f);
	public static final Colour SLATEGRAY = Colour.newRGB(0.44f, 0.50f, 0.56f);
	public static final Colour SNOW = Colour.newRGB(1.00f, 0.98f, 0.98f);
	public static final Colour SPRINGGREEN = Colour.newRGB(0.00f, 1.00f, 0.50f);
	public static final Colour STEELBLUE = Colour.newRGB(0.27f, 0.51f, 0.71f);
	public static final Colour TAN = Colour.newRGB(0.82f, 0.71f, 0.55f);
	public static final Colour TEAL = Colour.newRGB(0.00f, 0.50f, 0.50f);
	public static final Colour THISTLE = Colour.newRGB(0.85f, 0.75f, 0.85f);
	public static final Colour TOMATO = Colour.newRGB(1.00f, 0.39f, 0.28f);
	public static final Colour TRANSPARENT = Colour.newRGBA(0.00f, 0.00f,
			0.00f, 0.00f);
	public static final Colour TURQUOISE = Colour.newRGB(0.25f, 0.88f, 0.82f);
	public static final Colour VIOLET = Colour.newRGB(0.93f, 0.51f, 0.93f);
	public static final Colour WHEAT = Colour.newRGB(0.96f, 0.87f, 0.07f);
	public static final Colour WHITE = Colour.newRGB(1.00f, 1.00f, 1.00f);
	public static final Colour WHITESMOKE = Colour.newRGB(0.96f, 0.96f, 0.96f);
	public static final Colour YELLOW = Colour.newRGB(1.00f, 1.00f, 0.00f);
	public static final Colour YELLOWGREEN = Colour.newRGB(0.60f, 0.80f, 0.20f);

	protected static final HashMap<String, Colour> namedColorMap = new HashMap<String, Colour>();

	static {
		Field[] fields = NamedColour.class.getDeclaredFields();
		try {
			for (Field f : fields) {
				if (f.getType() == Colour.class) {
					String id = f.getName();
					namedColorMap.put(id, (Colour) f.get(null));
				}
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns a colour for the given name.
	 * 
	 * @param name
	 * @return colour or null, if name not found.
	 */
	public static final Colour getForName(String name) {
		return namedColorMap.get(name.toUpperCase());
	}

	/**
	 * Returns the names of all defined colours.
	 * 
	 * @return list of name
	 */
	public static final ArrayList<String> getNames() {
		return new ArrayList<String>(namedColorMap.keySet());
	}
}
