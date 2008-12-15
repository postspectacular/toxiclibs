package toxi.color;

import java.util.ArrayList;
import java.util.HashMap;

import toxi.math.MathUtils;
import toxi.util.datatypes.ArrayUtil;

public class ColorHue {

	protected static final HashMap<String, ColorHue> namedHues = new HashMap<String, ColorHue>();

	public static final ColorHue RED = new ColorHue("red", 0);
	public static final ColorHue ORANGE = new ColorHue("orange", 30 / 360.0f);
	public static final ColorHue YELLOW = new ColorHue("yellow", 60 / 360.0f);
	public static final ColorHue LIME = new ColorHue("lime", 90 / 360.0f);
	public static final ColorHue GREEN = new ColorHue("green", 120 / 360.0f);
	public static final ColorHue TEAL = new ColorHue("teal", 150 / 360.0f);
	public static final ColorHue CYAN = new ColorHue("cyan", 180 / 360.0f);
	public static final ColorHue AZURE = new ColorHue("azure", 210 / 360.0f);
	public static final ColorHue BLUE = new ColorHue("blue", 240 / 360.0f);
	public static final ColorHue INDIGO = new ColorHue("indigo", 270 / 360.0f);
	public static final ColorHue PURPLE = new ColorHue("purple", 300 / 360.0f);
	public static final ColorHue PINK = new ColorHue("pink", 330 / 360.0f);

	protected static final ArrayList<ColorHue> primaryHues = ArrayUtil
			.arrayToList(new ColorHue[] { RED, ORANGE, YELLOW, GREEN, BLUE,
					PURPLE, PINK });

	public static final float PRIMARY_VARIANCE = 0.01f;

	protected String name;
	protected float hue;

	public ColorHue(String name, float hue) {
		this.name = name;
		this.hue = hue;
		namedHues.put(name, this);
	}

	public static final ColorHue getForName(String name) {
		return namedHues.get(name);
	}

	public boolean isPrimary() {
		return isPrimary(hue);
	}

	public float getHue() {
		return hue;
	}

	public static boolean isPrimary(float hue) {
		boolean isPrimary = false;
		for (ColorHue h : primaryHues) {
			if (MathUtils.abs(hue - h.hue) < PRIMARY_VARIANCE) {
				isPrimary = true;
				break;
			}
		}
		return isPrimary;
	}
}