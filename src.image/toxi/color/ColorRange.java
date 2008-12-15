/**
 * 
 */
package toxi.color;

import java.util.HashMap;

import toxi.math.MathUtils;
import toxi.util.datatypes.FloatRange;
import toxi.util.datatypes.GenericSet;

/**
 * 
 */
public class ColorRange {

	/**
	 * Default hue variance for {@link #getColor(Color, float)}.
	 */
	public static final float DEFAULT_VARIANCE = 0.035f;

	public static final ColorRange LIGHT = new ColorRange(null, new FloatRange(
			0.3f, 0.7f), new FloatRange(0.9f, 1.0f), null, new FloatRange(
			0.15f, 0.30f), null, "light");

	public static final ColorRange DARK = new ColorRange(null, new FloatRange(
			0.7f, 1.0f), new FloatRange(0.15f, 0.4f), null, null,
			new FloatRange(0.5f, 0.75f), "dark");

	public static final ColorRange BRIGHT = new ColorRange(null,
			new FloatRange(0.8f, 1.0f), new FloatRange(0.8f, 1.0f), "bright");

	public static final ColorRange WEAK = new ColorRange(null, new FloatRange(
			0.15f, 0.3f), new FloatRange(0.7f, 1.0f), null, new FloatRange(
			0.2f, 0.2f), null, "weak");

	public static final ColorRange NEUTRAL = new ColorRange(null,
			new FloatRange(0.25f, 0.35f), new FloatRange(0.3f, 0.7f), null,
			new FloatRange(0.15f, 0.15f), new FloatRange(0.9f, 1), "neutral");

	public static final ColorRange FRESH = new ColorRange(null, new FloatRange(
			0.4f, 0.8f), new FloatRange(0.8f, 1.0f), null, new FloatRange(
			0.05f, 0.3f), new FloatRange(0.8f, 1.0f), "fresh");

	public static final ColorRange SOFT = new ColorRange(null, new FloatRange(
			0.2f, 0.3f), new FloatRange(0.6f, 0.9f), null, new FloatRange(
			0.05f, 0.15f), new FloatRange(0.6f, 0.9f), "soft");

	public static final ColorRange HARD = new ColorRange(null, new FloatRange(
			0.9f, 1.0f), new FloatRange(0.4f, 1.0f), "hard");

	public static final ColorRange WARM = new ColorRange(null, new FloatRange(
			0.6f, 0.9f), new FloatRange(0.4f, 0.9f), null, new FloatRange(0.2f,
			0.2f), new FloatRange(0.8f, 1.0f), "warm");

	public static final ColorRange COOL = new ColorRange(null, new FloatRange(
			0.05f, 0.2f), new FloatRange(0.9f, 1.0f), null, null,
			new FloatRange(0.95f, 1.0f), "cool");

	public static final ColorRange INTENSE = new ColorRange(null,
			new FloatRange(0.9f, 1.0f), new FloatRange(0.2f, 0.35f), "intense")
			.addBrightnessRange(new FloatRange(0.8f, 1.0f));

	/**
	 * List of ColorRange presets.
	 */
	public static final HashMap<String, ColorRange> PRESETS = new HashMap<String, ColorRange>();

	static {
		PRESETS.put(BRIGHT.getName(), BRIGHT);
		PRESETS.put(DARK.getName(), DARK);
		PRESETS.put(LIGHT.getName(), LIGHT);
		PRESETS.put(FRESH.getName(), FRESH);
		PRESETS.put(HARD.getName(), HARD);
		PRESETS.put(SOFT.getName(), SOFT);
		PRESETS.put(WEAK.getName(), WEAK);
		PRESETS.put(NEUTRAL.getName(), NEUTRAL);
		PRESETS.put(WARM.getName(), WARM);
		PRESETS.put(COOL.getName(), COOL);
		PRESETS.put(INTENSE.getName(), INTENSE);
	}

	protected GenericSet<FloatRange> hueConstraint;
	protected GenericSet<FloatRange> saturationConstraint;
	protected GenericSet<FloatRange> brightnessConstraint;
	protected GenericSet<FloatRange> alphaConstraint;

	protected FloatRange white;
	protected FloatRange black;

	protected String name;

	public ColorRange() {
		this(null, null, null, null, null, null, null);
	}

	public ColorRange(Color c) {
		this(new FloatRange(c.hue(), c.hue()), null, null, null, null, null,
				null);
	}

	public ColorRange(ColorHue hue) {
		this(new FloatRange(hue.getHue(), hue.getHue()), null, null, null, null,
				null, null);
	}

	public ColorRange(ColorList list) {
		this();
		hueConstraint.clear();
		for (Color c : list) {
			hueConstraint.add(new FloatRange(c.hue(), c.hue()));
			saturationConstraint.add(new FloatRange(c.saturation(), c
					.saturation()));
			brightnessConstraint.add(new FloatRange(c.brightness(), c
					.brightness()));
		}
	}

	/**
	 * @param hue
	 * @param sat
	 * @param bri
	 * @param isGreyscale
	 */
	public ColorRange(FloatRange hue, FloatRange sat, FloatRange bri,
			String name) {
		this(hue, sat, bri, null, null, null, name);
	}

	public ColorRange(FloatRange hue, FloatRange sat, FloatRange bri,
			FloatRange alpha, String name) {
		this(hue, sat, bri, alpha, null, null, name);
	}

	public ColorRange(FloatRange hue, FloatRange sat, FloatRange bri,
			FloatRange alpha, FloatRange black, FloatRange white, String name) {
		super();
		hueConstraint = new GenericSet<FloatRange>(hue != null ? hue
				: new FloatRange(0, 1));
		saturationConstraint = new GenericSet<FloatRange>(sat != null ? sat
				: new FloatRange(0, 1));
		brightnessConstraint = new GenericSet<FloatRange>(bri != null ? bri
				: new FloatRange(0, 1));
		alphaConstraint = new GenericSet<FloatRange>(alpha != null ? alpha
				: new FloatRange(0, 1));
		this.name = name != null ? name : "untitled";
		if (white == null) {
			this.white = new FloatRange(1, 1);
		} else {
			this.white = white;
		}
		if (black == null) {
			this.black = new FloatRange(0, 0);
		} else {
			this.black = black;
		}
	}

	public ColorRange addHueRange(FloatRange hue) {
		hueConstraint.add(hue);
		return this;
	}

	public ColorRange addSaturationRange(FloatRange sat) {
		saturationConstraint.add(sat);
		return this;
	}

	public ColorRange addBrightnessRange(FloatRange bri) {
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
			float hue = c.hue() + variance * MathUtils.normalizedRandom();
			range.hueConstraint = new GenericSet<FloatRange>(new FloatRange(
					hue, hue));
			range.alphaConstraint = new GenericSet<FloatRange>(new FloatRange(
					c.alpha, c.alpha));
		} else {
			range.hueConstraint = hueConstraint.copy();
			range.alphaConstraint = alphaConstraint.copy();
		}
		range.saturationConstraint = saturationConstraint.copy();
		range.brightnessConstraint = brightnessConstraint.copy();

		range.black = black.copy();
		range.white = white.copy();
		return range;
	}

	public Color getColor() {
		return getColor(null, DEFAULT_VARIANCE);
	}

	public Color getGrayscale(float brightness, float variance) {
		return getColor(Color.newGray(brightness), variance);
	}

	public Color getColor(Color c, float variance) {
		float h, s, b, a;
		if (c != null) {
			if (c.isBlack()) {
				return Color.newHSVA(c.hue(), 0, black.pickRandom(), c.alpha());
			} else if (c.isWhite()) {
				return Color.newHSVA(c.hue(), 0, white.pickRandom(), c.alpha());
			}
			if (c.isGrey()) {
				return Color.newHSVA(c.hue(), 0, MathUtils.flipCoin() ? black
						.pickRandom() : white.pickRandom(), c.alpha());
			}
			h = c.hue() + variance * MathUtils.normalizedRandom();
			a = c.alpha();
		} else {
			h = hueConstraint.pickRandom().pickRandom();
			a = alphaConstraint.pickRandom().pickRandom();
		}
		s = saturationConstraint.pickRandom().pickRandom();
		b = brightnessConstraint.pickRandom().pickRandom();
		return Color.newHSVA(h, s, b, a);
	}

	public Color getColor(ColorHue hue) {
		return Color.newHSVA(hue.getHue(), saturationConstraint.pickRandom()
				.pickRandom(), brightnessConstraint.pickRandom().pickRandom(),
				alphaConstraint.pickRandom().pickRandom());
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

	public ColorRange add(Color c) {
		hueConstraint.add(new FloatRange(c.hue(), c.hue()));
		saturationConstraint
				.add(new FloatRange(c.saturation(), c.saturation()));
		brightnessConstraint
				.add(new FloatRange(c.brightness(), c.brightness()));
		return this;
	}

	public ColorRange add(ColorHue hue) {
		hueConstraint.add(new FloatRange(hue.getHue(), hue.getHue()));
		saturationConstraint.add(new FloatRange(0, 1));
		brightnessConstraint.add(new FloatRange(0, 1));
		return this;
	}

	public ColorRange add(ColorRange range) {
		hueConstraint.addAll(range.hueConstraint.getItems());
		saturationConstraint.addAll(range.saturationConstraint.getItems());
		brightnessConstraint.addAll(range.brightnessConstraint.getItems());
		alphaConstraint.addAll(range.alphaConstraint.getItems());
		black.min = MathUtils.min(black.min, range.black.min);
		black.max = MathUtils.max(black.max, range.black.max);
		white.min = MathUtils.min(white.min, range.white.min);
		white.max = MathUtils.max(white.max, range.white.max);
		return this;
	}

	public ColorRange getSum(ColorRange range) {
		return copy().add(range);
	}

	public boolean contains(Color c) {
		boolean isInRange = isValueInConstraint(c.hue(), hueConstraint);
		isInRange &= isValueInConstraint(c.saturation(), saturationConstraint);
		isInRange &= isValueInConstraint(c.brightness(), brightnessConstraint);
		isInRange &= isValueInConstraint(c.alpha(), alphaConstraint);
		return isInRange;
	}

	protected boolean isValueInConstraint(float val,
			GenericSet<FloatRange> rangeSet) {
		boolean isValid = false;
		for (FloatRange r : rangeSet) {
			isValid |= r.isValueInRange(val);
		}
		return isValid;
	}

	public String getName() {
		return name;
	}

	public static boolean isPresetName(String item) {
		return PRESETS.get(item) != null;
	}

	public static ColorRange getPresetForName(String item) {
		return PRESETS.get(item);
	}
}