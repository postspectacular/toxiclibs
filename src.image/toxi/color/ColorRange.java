/**
 * 
 */
package toxi.color;

import toxi.math.MathUtils;
import toxi.util.datatypes.FloatRange;
import toxi.util.datatypes.FloatRangeSet;

/**
 * 
 */
public class ColorRange extends ColorList {

	public static final float DEFAULT_VARIANCE = 0.035f;

	public FloatRangeSet hueConstraint;
	public FloatRangeSet saturationConstraint;
	public FloatRangeSet brightnessConstraint;
	public FloatRangeSet alphaConstraint;

	public ColorRange white;
	public ColorRange black;

	protected String name = "";

	protected boolean isGreyscale;

	/**
	 * 
	 */
	public ColorRange() {
		this(false);
	}

	/**
	 * @param isGreyscale
	 */
	public ColorRange(boolean isGreyscale) {
		super();
		hueConstraint = new FloatRangeSet(new FloatRange(0, 1));
		saturationConstraint = new FloatRangeSet(new FloatRange(0, 1));
		brightnessConstraint = new FloatRangeSet(new FloatRange(0, 1));
		alphaConstraint = new FloatRangeSet(new FloatRange(1, 1));
		this.isGreyscale = isGreyscale;
		init();
	}

	/**
	 * @param hue
	 * @param sat
	 * @param bri
	 * @param isGreyscale
	 */
	public ColorRange(FloatRange hue, FloatRange sat, FloatRange bri,
			boolean isGreyscale) {
		this(hue, sat, bri, new FloatRange(1, 1), isGreyscale);
	}

	public ColorRange(FloatRange hue, FloatRange sat, FloatRange bri,
			FloatRange alpha, boolean isGreyscale) {
		this(hue, sat, bri, alpha, isGreyscale, "");
	}

	public ColorRange(FloatRange hue, FloatRange sat, FloatRange bri,
			FloatRange alpha, boolean isGreyscale, String name) {
		super();
		hueConstraint = new FloatRangeSet(hue);
		saturationConstraint = new FloatRangeSet(sat);
		brightnessConstraint = new FloatRangeSet(bri);
		alphaConstraint = new FloatRangeSet(alpha);
		;
		this.isGreyscale = isGreyscale;
		this.name = name;
		init();
	}

	private void init() {
		if (!isGreyscale) {
			white = new ColorRange(new FloatRange(0, 1), new FloatRange(0, 0),
					new FloatRange(1, 1), new FloatRange(1, 1), true, name);
			black = new ColorRange(new FloatRange(0, 1), new FloatRange(0, 0),
					new FloatRange(1, 1), new FloatRange(1, 1), true, name);
		}
	}

	public ColorRange addHueConstraint(FloatRange hue) {
		hueConstraint.add(hue);
		return this;
	}

	public ColorRange addSaturationConstraint(FloatRange sat) {
		saturationConstraint.add(sat);
		return this;
	}

	public ColorRange addBrightnessConstraint(FloatRange bri) {
		brightnessConstraint.add(bri);
		return this;
	}

	public ColorRange copy() {
		return copy(null, 0);
	}

	public ColorRange copy(Color c, float variance) {
		ColorRange range = new ColorRange();
		range.name = name;

		if (c != null) {
			float hue = c.hue() + variance * MathUtils.random(-1f, 1f);
			range.hueConstraint = new FloatRangeSet(new FloatRange(hue, hue));
			range.alphaConstraint = new FloatRangeSet(new FloatRange(c.alpha,
					c.alpha));
		} else {
			range.hueConstraint = hueConstraint.copy();
			range.alphaConstraint = alphaConstraint.copy();
		}
		range.saturationConstraint = saturationConstraint.copy();
		range.brightnessConstraint = brightnessConstraint.copy();

		range.isGreyscale = isGreyscale;
		if (!isGreyscale) {
			range.black = black.copy();
			range.white = white.copy();
		}
		return range;
	}

	public Color getColor() {
		return getColor(null, DEFAULT_VARIANCE);
	}

	public Color getColor(Color c, float variance) {
		float h, s, b, a;
		if (c != null) {
			if (!isGreyscale) {
				if (c.isBlack())
					return black.getColor(c, variance);
				if (c.isWhite())
					return white.getColor(c, variance);
				if (c.isGrey())
					return (MathUtils.random(1f) < 0.5 ? black.getColor(c,
							variance) : white.getColor(c, variance));
			}
			h = c.hue() + variance * MathUtils.random(-1f, 1f);
			a = c.alpha();
		} else {
			h = hueConstraint.pickRandom().pickRandom();
			a = alphaConstraint.pickRandom().pickRandom();
		}
		s = !isGreyscale ? saturationConstraint.pickRandom().pickRandom() : 0;
		b = brightnessConstraint.pickRandom().pickRandom();
		return Color.newHSVA(h, s, b, a);
	}

	public ColorList getColors(int num) {
		return getColors(null, num, DEFAULT_VARIANCE);
	}

	public ColorList getColors(Color c, int num, float variance) {
		ColorList list = new ColorList();
		for (int i = 0; i < num; i++) {
			list.add(getColor(c, variance));
		}
		return list;
	}

	public void setGreyscale(boolean b) {
		isGreyscale = b;
	}

}
