package toxi.color;

import java.lang.reflect.Field;
import java.util.HashMap;

public class NamedColor {

	public static final Color ALICEBLUE = Color.newRGB(0.94f, 0.97f, 1.00f);
	public static final Color ANTIQUEWHITE = Color.newRGB(0.98f, 0.92f, 0.84f);
	public static final Color AQUA = Color.newRGB(0.00f, 1.00f, 1.00f);
	public static final Color AQUAMARINE = Color.newRGB(0.50f, 1.00f, 0.83f);
	public static final Color AZURE = Color.newRGB(0.94f, 1.00f, 1.00f);
	public static final Color BARK = Color.newRGB(0.25f, 0.19f, 0.13f);
	public static final Color BEIGE = Color.newRGB(0.96f, 0.96f, 0.86f);
	public static final Color BISQUE = Color.newRGB(1.00f, 0.89f, 0.77f);
	public static final Color BLACK = Color.newRGB(0.00f, 0.00f, 0.00f);
	public static final Color BLANCHEDALMOND = Color
			.newRGB(1.00f, 0.92f, 0.80f);
	public static final Color BLUE = Color.newRGB(0.00f, 0.00f, 1.00f);
	public static final Color BLUEVIOLET = Color.newRGB(0.54f, 0.17f, 0.89f);
	public static final Color BROWN = Color.newRGB(0.65f, 0.16f, 0.16f);
	public static final Color BURLYWOOD = Color.newRGB(0.87f, 0.72f, 0.53f);
	public static final Color CADETBLUE = Color.newRGB(0.37f, 0.62f, 0.63f);
	public static final Color CHARTREUSE = Color.newRGB(0.50f, 1.00f, 0.00f);
	public static final Color CHOCOLATE = Color.newRGB(0.82f, 0.41f, 0.12f);
	public static final Color CORAL = Color.newRGB(1.00f, 0.50f, 0.31f);
	public static final Color CORNFLOWERBLUE = Color
			.newRGB(0.39f, 0.58f, 0.93f);
	public static final Color CORNSILK = Color.newRGB(1.00f, 0.97f, 0.86f);
	public static final Color CRIMSON = Color.newRGB(0.86f, 0.08f, 0.24f);
	public static final Color CYAN = Color.newRGB(0.00f, 0.68f, 0.94f);
	public static final Color DARKBLUE = Color.newRGB(0.00f, 0.00f, 0.55f);
	public static final Color DARKCYAN = Color.newRGB(0.00f, 0.55f, 0.55f);
	public static final Color DARKGOLDENROD = Color.newRGB(0.72f, 0.53f, 0.04f);
	public static final Color DARKGRAY = Color.newRGB(0.66f, 0.66f, 0.66f);
	public static final Color DARKGREEN = Color.newRGB(0.00f, 0.39f, 0.00f);
	public static final Color DARKKHAKI = Color.newRGB(0.74f, 0.72f, 0.42f);
	public static final Color DARKMAGENTA = Color.newRGB(0.55f, 0.00f, 0.55f);
	public static final Color DARKOLIVEGREEN = Color
			.newRGB(0.33f, 0.42f, 0.18f);
	public static final Color DARKORANGE = Color.newRGB(1.00f, 0.55f, 0.00f);
	public static final Color DARKORCHID = Color.newRGB(0.60f, 0.20f, 0.80f);
	public static final Color DARKRED = Color.newRGB(0.55f, 0.00f, 0.00f);
	public static final Color DARKSALMON = Color.newRGB(0.91f, 0.59f, 0.48f);
	public static final Color DARKSEAGREEN = Color.newRGB(0.56f, 0.74f, 0.56f);
	public static final Color DARKSLATEBLUE = Color.newRGB(0.28f, 0.24f, 0.55f);
	public static final Color DARKSLATEGRAY = Color.newRGB(0.18f, 0.31f, 0.31f);
	public static final Color DARKTURQUOISE = Color.newRGB(0.00f, 0.81f, 0.82f);
	public static final Color DARKVIOLET = Color.newRGB(0.58f, 0.00f, 0.83f);
	public static final Color DEEPPINK = Color.newRGB(1.00f, 0.08f, 0.58f);
	public static final Color DEEPSKYBLUE = Color.newRGB(0.00f, 0.75f, 1.00f);
	public static final Color DIMGRAY = Color.newRGB(0.41f, 0.41f, 0.41f);
	public static final Color DIMGREY = Color.newRGB(0.41f, 0.41f, 0.41f);
	public static final Color DODGERBLUE = Color.newRGB(0.12f, 0.56f, 1.00f);
	public static final Color FIREBRICK = Color.newRGB(0.70f, 0.13f, 0.13f);
	public static final Color FLORALWHITE = Color.newRGB(1.00f, 0.98f, 0.94f);
	public static final Color FORESTGREEN = Color.newRGB(0.13f, 0.55f, 0.13f);
	public static final Color FUCHSIA = Color.newRGB(1.00f, 0.00f, 1.00f);
	public static final Color GAINSBORO = Color.newRGB(0.86f, 0.86f, 0.86f);
	public static final Color GHOSTWHITE = Color.newRGB(0.97f, 0.97f, 1.00f);
	public static final Color GOLD = Color.newRGB(1.00f, 0.84f, 0.00f);
	public static final Color GOLDENROD = Color.newRGB(0.85f, 0.65f, 0.13f);
	public static final Color GRAY = Color.newRGB(0.50f, 0.50f, 0.50f);
	public static final Color GREEN = Color.newRGB(0.00f, 0.50f, 0.00f);
	public static final Color GREENYELLOW = Color.newRGB(0.68f, 1.00f, 0.18f);
	public static final Color GREY = Color.newRGB(0.50f, 0.50f, 0.50f);
	public static final Color HONEYDEW = Color.newRGB(0.94f, 1.00f, 0.94f);
	public static final Color HOTPINK = Color.newRGB(1.00f, 0.41f, 0.71f);
	public static final Color INDIANRED = Color.newRGB(0.80f, 0.36f, 0.36f);
	public static final Color INDIGO = Color.newRGB(0.29f, 0.00f, 0.51f);
	public static final Color IVORY = Color.newRGB(1.00f, 1.00f, 0.94f);
	public static final Color KHAKI = Color.newRGB(0.94f, 0.90f, 0.55f);
	public static final Color LAVENDER = Color.newRGB(0.90f, 0.90f, 0.98f);
	public static final Color LAVENDERBLUSH = Color.newRGB(1.00f, 0.94f, 0.96f);
	public static final Color LAWNGREEN = Color.newRGB(0.49f, 0.99f, 0.00f);
	public static final Color LEMONCHIFFON = Color.newRGB(1.00f, 0.98f, 0.80f);
	public static final Color LIGHTBLUE = Color.newRGB(0.68f, 0.85f, 0.90f);
	public static final Color LIGHTCORAL = Color.newRGB(0.94f, 0.50f, 0.50f);
	public static final Color LIGHTCYAN = Color.newRGB(0.88f, 1.00f, 1.00f);
	public static final Color LIGHTGOLDENRODYELLOW = Color.newRGB(0.98f, 0.98f,
			0.82f);
	public static final Color LIGHTGREEN = Color.newRGB(0.56f, 0.93f, 0.56f);
	public static final Color LIGHTGREY = Color.newRGB(0.83f, 0.83f, 0.83f);
	public static final Color LIGHTPINK = Color.newRGB(1.00f, 0.71f, 0.76f);
	public static final Color LIGHTSALMON = Color.newRGB(1.00f, 0.63f, 0.48f);
	public static final Color LIGHTSEAGREEN = Color.newRGB(0.13f, 0.70f, 0.67f);
	public static final Color LIGHTSKYBLUE = Color.newRGB(0.53f, 0.81f, 0.98f);
	public static final Color LIGHTSLATEGRAY = Color
			.newRGB(0.47f, 0.53f, 0.60f);
	public static final Color LIGHTSTEELBLUE = Color
			.newRGB(0.69f, 0.77f, 0.87f);
	public static final Color LIGHTYELLOW = Color.newRGB(1.00f, 1.00f, 0.88f);
	public static final Color LIME = Color.newRGB(0.00f, 1.00f, 0.00f);
	public static final Color LIMEGREEN = Color.newRGB(0.20f, 0.80f, 0.20f);
	public static final Color LINEN = Color.newRGB(0.98f, 0.94f, 0.90f);
	public static final Color MAROON = Color.newRGB(0.50f, 0.00f, 0.00f);
	public static final Color MEDIUMAQUAMARINE = Color.newRGB(0.40f, 0.80f,
			0.67f);
	public static final Color MEDIUMBLUE = Color.newRGB(0.00f, 0.00f, 0.80f);
	public static final Color MEDIUMORCHID = Color.newRGB(0.73f, 0.33f, 0.83f);
	public static final Color MEDIUMPURPLE = Color.newRGB(0.58f, 0.44f, 0.86f);
	public static final Color MEDIUMSEAGREEN = Color
			.newRGB(0.24f, 0.70f, 0.44f);
	public static final Color MEDIUMSLATEBLUE = Color.newRGB(0.48f, 0.41f,
			0.93f);
	public static final Color MEDIUMSPRINGGREEN = Color.newRGB(0.00f, 0.98f,
			0.60f);
	public static final Color MEDIUMTURQUOISE = Color.newRGB(0.28f, 0.82f,
			0.80f);
	public static final Color MEDIUMVIOLETRED = Color.newRGB(0.78f, 0.08f,
			0.52f);
	public static final Color MIDNIGHTBLUE = Color.newRGB(0.10f, 0.10f, 0.44f);
	public static final Color MINTCREAM = Color.newRGB(0.96f, 1.00f, 0.98f);
	public static final Color MISTYROSE = Color.newRGB(1.00f, 0.89f, 0.88f);
	public static final Color MOCCASIN = Color.newRGB(1.00f, 0.89f, 0.71f);
	public static final Color NAVAJOWHITE = Color.newRGB(1.00f, 0.87f, 0.68f);
	public static final Color NAVY = Color.newRGB(0.00f, 0.00f, 0.50f);
	public static final Color OLDLACE = Color.newRGB(0.99f, 0.96f, 0.90f);
	public static final Color OLIVE = Color.newRGB(0.50f, 0.50f, 0.00f);
	public static final Color OLIVEDRAB = Color.newRGB(0.42f, 0.56f, 0.14f);
	public static final Color ORANGE = Color.newRGB(1.00f, 0.65f, 0.00f);
	public static final Color ORANGERED = Color.newRGB(1.00f, 0.27f, 0.00f);
	public static final Color ORCHID = Color.newRGB(0.85f, 0.44f, 0.84f);
	public static final Color PALEGOLDENROD = Color.newRGB(0.93f, 0.91f, 0.67f);
	public static final Color PALEGREEN = Color.newRGB(0.60f, 0.98f, 0.60f);
	public static final Color PALETURQUOISE = Color.newRGB(0.69f, 0.93f, 0.93f);
	public static final Color PALEVIOLETRED = Color.newRGB(0.86f, 0.44f, 0.58f);
	public static final Color PAPAYAWHIP = Color.newRGB(1.00f, 0.94f, 0.84f);
	public static final Color PEACHPUFF = Color.newRGB(1.00f, 0.85f, 0.73f);
	public static final Color PERU = Color.newRGB(0.80f, 0.52f, 0.25f);
	public static final Color PINK = Color.newRGB(1.00f, 0.75f, 0.80f);
	public static final Color PLUM = Color.newRGB(0.87f, 0.63f, 0.87f);
	public static final Color POWDERBLUE = Color.newRGB(0.69f, 0.88f, 0.90f);
	public static final Color PURPLE = Color.newRGB(0.50f, 0.00f, 0.50f);
	public static final Color RED = Color.newRGB(1.00f, 0.00f, 0.00f);
	public static final Color ROSYBROWN = Color.newRGB(0.74f, 0.56f, 0.56f);
	public static final Color ROYALBLUE = Color.newRGB(0.25f, 0.41f, 0.88f);
	public static final Color SADDLEBROWN = Color.newRGB(0.55f, 0.27f, 0.07f);
	public static final Color SALMON = Color.newRGB(0.98f, 0.50f, 0.45f);
	public static final Color SANDYBROWN = Color.newRGB(0.96f, 0.64f, 0.38f);
	public static final Color SEAGREEN = Color.newRGB(0.18f, 0.55f, 0.34f);
	public static final Color SEASHELL = Color.newRGB(1.00f, 0.96f, 0.93f);
	public static final Color SIENNA = Color.newRGB(0.63f, 0.32f, 0.18f);
	public static final Color SILVER = Color.newRGB(0.75f, 0.75f, 0.75f);
	public static final Color SKYBLUE = Color.newRGB(0.53f, 0.81f, 0.92f);
	public static final Color SLATEBLUE = Color.newRGB(0.42f, 0.35f, 0.80f);
	public static final Color SLATEGRAY = Color.newRGB(0.44f, 0.50f, 0.56f);
	public static final Color SNOW = Color.newRGB(1.00f, 0.98f, 0.98f);
	public static final Color SPRINGGREEN = Color.newRGB(0.00f, 1.00f, 0.50f);
	public static final Color STEELBLUE = Color.newRGB(0.27f, 0.51f, 0.71f);
	public static final Color TAN = Color.newRGB(0.82f, 0.71f, 0.55f);
	public static final Color TEAL = Color.newRGB(0.00f, 0.50f, 0.50f);
	public static final Color THISTLE = Color.newRGB(0.85f, 0.75f, 0.85f);
	public static final Color TOMATO = Color.newRGB(1.00f, 0.39f, 0.28f);
	public static final Color TRANSPARENT = Color.newRGBA(0.00f, 0.00f, 0.00f,
			0.00f);
	public static final Color TURQUOISE = Color.newRGB(0.25f, 0.88f, 0.82f);
	public static final Color VIOLET = Color.newRGB(0.93f, 0.51f, 0.93f);
	public static final Color WHEAT = Color.newRGB(0.96f, 0.87f, 0.07f);
	public static final Color WHITE = Color.newRGB(1.00f, 1.00f, 1.00f);
	public static final Color WHITESMOKE = Color.newRGB(0.96f, 0.96f, 0.96f);
	public static final Color YELLOW = Color.newRGB(1.00f, 1.00f, 0.00f);
	public static final Color YELLOWGREEN = Color.newRGB(0.60f, 0.80f, 0.20f);

	protected static final HashMap<String, Color> namedColorMap = new HashMap<String, Color>();

	static {
		Field[] fields = NamedColor.class.getDeclaredFields();
		try {
			for (Field f : fields) {
				if (f.getType() == Color.class) {
					String id = f.getName();
					namedColorMap.put(id, (Color) f.get(null));
				}
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	public static final Color getForName(String name) {
		return namedColorMap.get(name.toUpperCase());
	}
}
