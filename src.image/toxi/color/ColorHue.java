package toxi.color;

import java.util.ArrayList;
import java.util.HashMap;

import toxi.math.MathUtils;

public class ColorHue {

	protected static final HashMap<String, ColorHue> namedHues = new HashMap<String, ColorHue>();
	protected static final ArrayList<ColorHue> primaryHues = new ArrayList<ColorHue>();

	public static final ColorHue RED = new ColorHue("red", 0, true);
	public static final ColorHue ORANGE = new ColorHue("orange", 30 / 360.0f,
			true);
	public static final ColorHue YELLOW = new ColorHue("yellow", 60 / 360.0f,
			true);
	public static final ColorHue LIME = new ColorHue("lime", 90 / 360.0f);
	public static final ColorHue GREEN = new ColorHue("green", 120 / 360.0f,
			true);
	public static final ColorHue TEAL = new ColorHue("teal", 150 / 360.0f);
	public static final ColorHue CYAN = new ColorHue("cyan", 180 / 360.0f);
	public static final ColorHue AZURE = new ColorHue("azure", 210 / 360.0f);
	public static final ColorHue BLUE = new ColorHue("blue", 240 / 360.0f, true);
	public static final ColorHue INDIGO = new ColorHue("indigo", 270 / 360.0f);
	public static final ColorHue PURPLE = new ColorHue("purple", 300 / 360.0f,
			true);
	public static final ColorHue PINK = new ColorHue("pink", 330 / 360.0f, true);

	public static float PRIMARY_VARIANCE = 0.01f;

	protected String name;
	protected float hue;
	protected boolean isPrimary;

	protected ColorHue(String name, float hue) {
		this(name, hue, false);
	}

	protected ColorHue(String name, float hue, boolean isPrimary) {
		this.name = name;
		this.hue = hue;
		this.isPrimary = isPrimary;
		namedHues.put(name, this);
		if (isPrimary) {
			primaryHues.add(this);
		}
	}

	public static final ColorHue getForName(String name) {
		return namedHues.get(name.toLowerCase());
	}

	/**
	 * Finds the closest defined & named ColorHue for the given hue value.
	 * Optionally, the search can be limited to primary hues only.
	 * 
	 * @param hue
	 *            normalized hue (0.0 ... 1.0) will be automatically wrapped
	 * @param primaryOnly
	 *            only consider the 7 primary hues
	 * @return closest ColorHue instance
	 */
	public static final ColorHue getClosest(float hue, boolean primaryOnly) {
		hue %= 1;
		float dist = Float.MAX_VALUE;
		ColorHue closest = null;
		Iterable<ColorHue> hues = (primaryOnly ? primaryHues : namedHues
				.values());
		for (ColorHue h : hues) {
			float d = MathUtils.min(MathUtils.abs(h.hue - hue), MathUtils.abs(1
					+ h.hue - hue));
			if (d < dist) {
				dist = d;
				closest = h;
			}
		}
		return closest;
	}

	public boolean isPrimary() {
		return isPrimary;
	}

	public float getHue() {
		return hue;
	}

	public String getName() {
		return name;
	}

	public static boolean isPrimary(float hue) {
		return isPrimary(hue, PRIMARY_VARIANCE);
	}

	public static boolean isPrimary(float hue, float variance) {
		boolean isPrimary = false;
		for (ColorHue h : primaryHues) {
			if (MathUtils.abs(hue - h.hue) < variance) {
				isPrimary = true;
				break;
			}
		}
		return isPrimary;
	}

	public String toString() {
		return "ColorHue: ID:" + name + " @ " + (int) (hue * 360) + " degrees";
	}
}