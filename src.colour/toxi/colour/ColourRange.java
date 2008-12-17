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
import java.util.HashMap;

import toxi.math.MathUtils;
import toxi.util.datatypes.FloatRange;
import toxi.util.datatypes.GenericSet;

/**
 * A ColourRange is a set of contraints to specify possible ranges for hue,
 * saturation, brightness and alpha independently and use these as creation
 * rules for new {@link Colour}s or {@link ColourList}s. The class comes with 11
 * preset ranges reflecting common demands and colour characters. You can also
 * construct new ranges and manually add additional constraints. Unless the
 * constraints in a range are very narrow the class will always create random
 * variations within the constraints. Please see the examples for further
 * details.
 * 
 * {@link ColourRange}s are a key ingredient for defining {@link ColourTheme}s
 * but can also be used individually.
 * 
 * @author toxi
 */
public class ColourRange {

	/**
	 * Default hue variance for {@link #getColor(Colour, float)}.
	 */
	public static final float DEFAULT_VARIANCE = 0.035f;

	/**
	 * Shade definition: saturation 30-70%, brightness: 90-100%
	 */
	public static final ColourRange LIGHT = new ColourRange(null,
			new FloatRange(0.3f, 0.7f), new FloatRange(0.9f, 1.0f), null,
			new FloatRange(0.15f, 0.30f), null, "light");

	/**
	 * Shade definition: saturation 70-100%, brightness: 15-40%
	 */
	public static final ColourRange DARK = new ColourRange(null,
			new FloatRange(0.7f, 1.0f), new FloatRange(0.15f, 0.4f), null,
			null, new FloatRange(0.5f, 0.75f), "dark");

	/**
	 * Shade definition: saturation 80-100%, brightness: 80-100%
	 */
	public static final ColourRange BRIGHT = new ColourRange(null,
			new FloatRange(0.8f, 1.0f), new FloatRange(0.8f, 1.0f), "bright");

	/**
	 * Shade definition: saturation 15-30%, brightness: 70-100%
	 */
	public static final ColourRange WEAK = new ColourRange(null,
			new FloatRange(0.15f, 0.3f), new FloatRange(0.7f, 1.0f), null,
			new FloatRange(0.2f, 0.2f), null, "weak");

	/**
	 * Shade definition: saturation 25-35%, brightness: 30-70%
	 */
	public static final ColourRange NEUTRAL = new ColourRange(null,
			new FloatRange(0.25f, 0.35f), new FloatRange(0.3f, 0.7f), null,
			new FloatRange(0.15f, 0.15f), new FloatRange(0.9f, 1), "neutral");

	/**
	 * Shade definition: saturation 40-80%, brightness: 80-100%
	 */
	public static final ColourRange FRESH = new ColourRange(null,
			new FloatRange(0.4f, 0.8f), new FloatRange(0.8f, 1.0f), null,
			new FloatRange(0.05f, 0.3f), new FloatRange(0.8f, 1.0f), "fresh");

	/**
	 * Shade definition: saturation 20-30%, brightness: 60-90%
	 */
	public static final ColourRange SOFT = new ColourRange(null,
			new FloatRange(0.2f, 0.3f), new FloatRange(0.6f, 0.9f), null,
			new FloatRange(0.05f, 0.15f), new FloatRange(0.6f, 0.9f), "soft");

	/**
	 * Shade definition: saturation 90-100%, brightness: 40-100%
	 */
	public static final ColourRange HARD = new ColourRange(null,
			new FloatRange(0.9f, 1.0f), new FloatRange(0.4f, 1.0f), "hard");

	/**
	 * Shade definition: saturation 60-90%, brightness: 40-90%
	 */
	public static final ColourRange WARM = new ColourRange(null,
			new FloatRange(0.6f, 0.9f), new FloatRange(0.4f, 0.9f), null,
			new FloatRange(0.2f, 0.2f), new FloatRange(0.8f, 1.0f), "warm");

	/**
	 * Shade definition: saturation 5-20%, brightness: 90-100%
	 */
	public static final ColourRange COOL = new ColourRange(null,
			new FloatRange(0.05f, 0.2f), new FloatRange(0.9f, 1.0f), null,
			null, new FloatRange(0.95f, 1.0f), "cool");

	/**
	 * Shade definition: saturation 90-100%, brightness: 20-35% or 80-100%
	 */
	public static final ColourRange INTENSE = new ColourRange(null,
			new FloatRange(0.9f, 1.0f), new FloatRange(0.2f, 0.35f), "intense")
			.addBrightnessRange(new FloatRange(0.8f, 1.0f));

	/**
	 * List of ColourRange presets.
	 */
	public static final HashMap<String, ColourRange> PRESETS = new HashMap<String, ColourRange>();

	private static int UNTITLED_ID = 1;

	protected GenericSet<FloatRange> hueConstraint;

	protected GenericSet<FloatRange> saturationConstraint;
	protected GenericSet<FloatRange> brightnessConstraint;
	protected GenericSet<FloatRange> alphaConstraint;
	protected FloatRange white;

	protected FloatRange black;
	protected String name;

	static {
		Field[] fields = ColourRange.class.getDeclaredFields();
		try {
			for (Field f : fields) {
				if (f.getType() == ColourRange.class) {
					String id = f.getName();
					PRESETS.put(id, (ColourRange) f.get(null));
				}
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Retrieves one of the predefined ranges by name.
	 * 
	 * @param name
	 * @return colour range or null if name not registered
	 */
	public static ColourRange getPresetForName(String name) {
		return PRESETS.get(name.toUpperCase());
	}

	/**
	 * Only used internally by {@link #copy()}, doesn't initialize anything.
	 */
	private ColourRange() {

	}

	/**
	 * Constructs a new range using the hue of the given colour as hue
	 * constraint, but saturation and brightness are fully flexible. The
	 * resulting range will produce any shade of the given colour.
	 * 
	 * @param c
	 *            base colour
	 */
	public ColourRange(Colour c) {
		this(new FloatRange(c.hue(), c.hue()), null, null, null, null, null,
				null);
	}

	/**
	 * Constructs a new range using the given colours as HSV constraints.
	 * 
	 * @param list
	 *            list base colours
	 */
	public ColourRange(ColourList list) {
		this(list.get(0));
		hueConstraint.clear();
		for (Colour c : list) {
			add(c);
		}
	}

	/**
	 * Constructs a new range with the supplied constraints (if an HSV argument
	 * is null, a range of 0.0 ... 1.0 is created automatically for that
	 * constraint). If alpha is left undefined, it'll be initialized to fully
	 * opaque only. You can also specify ranges for possible black and white
	 * points which are used if the range is later applied to a grayscale
	 * colour. The default black point is at 0.0 and white at 1.0.
	 * 
	 * @param hue
	 * @param sat
	 * @param bri
	 * @param alpha
	 * @param black
	 * @param white
	 * @param name
	 */
	public ColourRange(FloatRange hue, FloatRange sat, FloatRange bri,
			FloatRange alpha, FloatRange black, FloatRange white, String name) {
		super();
		hueConstraint = new GenericSet<FloatRange>(hue != null ? hue
				: new FloatRange(0, 1));
		saturationConstraint = new GenericSet<FloatRange>(sat != null ? sat
				: new FloatRange(0, 1));
		brightnessConstraint = new GenericSet<FloatRange>(bri != null ? bri
				: new FloatRange(0, 1));
		alphaConstraint = new GenericSet<FloatRange>(alpha != null ? alpha
				: new FloatRange(1, 1));
		if (black == null) {
			this.black = new FloatRange(0, 0);
		} else {
			this.black = black;
		}
		if (white == null) {
			this.white = new FloatRange(1, 1);
		} else {
			this.white = white;
		}
		this.name = name != null ? name : "untitled" + (UNTITLED_ID++);
	}

	/**
	 * Constructs a new range with the supplied constraints (if an argument is
	 * null, a range of 0.0 ... 1.0 is created automatically for that
	 * constraint).
	 * 
	 * @param hue
	 *            min/max hue range
	 * @param sat
	 *            min/max saturation range
	 * @param bri
	 *            min/max brightness range
	 * @param alpha
	 *            min/max alpha range (if null, initialized to 100% only)
	 * @param name
	 */
	public ColourRange(FloatRange hue, FloatRange sat, FloatRange bri,
			FloatRange alpha, String name) {
		this(hue, sat, bri, alpha, null, null, name);
	}

	/**
	 * Constructs a new range with the supplied constraints (if an argument is
	 * null, a range of 0.0 ... 1.0 is created automatically for that
	 * constraint). Alpha constraint will be set to 100%.
	 * 
	 * @param hue
	 *            min/max hue range
	 * @param sat
	 *            min/max saturation range
	 * @param bri
	 *            min/max brightness range
	 * @param name
	 */
	public ColourRange(FloatRange hue, FloatRange sat, FloatRange bri,
			String name) {
		this(hue, sat, bri, null, null, null, name);
	}

	/**
	 * Constructs a new range using the given hue as constraint, but saturation
	 * and brightness are fully flexible. The resulting range will produce any
	 * shade of the given hue.
	 * 
	 * @param hue
	 *            base hue
	 */
	public ColourRange(Hue hue) {
		this(new FloatRange(hue.getHue(), hue.getHue()), null, null, null,
				null, null, null);
	}

	/**
	 * Adds the HSV colour components as constraints.
	 * 
	 * @param c
	 *            colour to use as constraint
	 * @return itself
	 */
	public ColourRange add(Colour c) {
		hueConstraint.add(new FloatRange(c.hue(), c.hue()));
		saturationConstraint
				.add(new FloatRange(c.saturation(), c.saturation()));
		brightnessConstraint
				.add(new FloatRange(c.brightness(), c.brightness()));
		alphaConstraint.add(new FloatRange(c.alpha, c.alpha));
		return this;
	}

	/**
	 * Adds the contraints of the given range to this range and forms unions for
	 * the black and white point ranges.
	 * 
	 * @param range
	 *            colour range to add
	 * @return itself
	 */
	public ColourRange add(ColourRange range) {
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

	/**
	 * Adds the range between min-max as possible alpha values for this range.
	 * 
	 * @param min
	 * @param max
	 * @return itself
	 */
	public ColourRange addAlphaRange(float min, float max) {
		return addAlphaRange(new FloatRange(min, max));
	}

	/**
	 * Adds an additional alpha constraint.
	 * 
	 * @param alpha
	 *            min/max alpha values
	 * @return itself
	 */
	public ColourRange addAlphaRange(FloatRange alpha) {
		alphaConstraint.add(alpha);
		return this;
	}

	/**
	 * Adds the range between min-max as possible brightness values for this
	 * range.
	 * 
	 * @param min
	 * @param max
	 * @return itself
	 */
	public ColourRange addBrightnessRange(float min, float max) {
		return addBrightnessRange(new FloatRange(min, max));
	}

	/**
	 * Adds an additional brightness constraint.
	 * 
	 * @param bri
	 *            min/max brightness values
	 * @return itself
	 */
	public ColourRange addBrightnessRange(FloatRange bri) {
		brightnessConstraint.add(bri);
		return this;
	}

	/**
	 * Add the given hue as hue constraint.
	 * 
	 * @param hue
	 * @return itself
	 */
	public ColourRange addHue(Hue hue) {
		hueConstraint.add(new FloatRange(hue.getHue(), hue.getHue()));
		return this;
	}

	/**
	 * Adds the range between min-max as possible hue values for this range. If
	 * max < min then two intervals are added: {min ... 1.0} and {0.0 ... max}
	 * 
	 * @param min
	 * @param max
	 * @return itself
	 */
	public ColourRange addHueRange(float min, float max) {
		if (max >= min) {
			addHueRange(new FloatRange(min, max));
		} else {
			addHueRange(new FloatRange(min, 1));
			addHueRange(new FloatRange(0, max));
		}
		return this;
	}

	/**
	 * Adds an additional hue constraint.
	 * 
	 * @param hue
	 *            min/max hue values
	 * @return itself
	 */
	public ColourRange addHueRange(FloatRange hue) {
		hueConstraint.add(hue);
		return this;
	}

	/**
	 * Adds the range between min-max as possible saturation values for this
	 * range.
	 * 
	 * @param min
	 * @param max
	 * @return itself
	 */
	public ColourRange addSaturationRange(float min, float max) {
		return addAlphaRange(new FloatRange(min, max));
	}

	/**
	 * Adds an additional saturation constraint.
	 * 
	 * @param sat
	 *            min/max saturation values
	 * @return itself
	 */
	public ColourRange addSaturationRange(FloatRange sat) {
		saturationConstraint.add(sat);
		return this;
	}

	/**
	 * Checks if all HSVA components of the given colour are within the
	 * constraints defined for this range.
	 * 
	 * @param c
	 *            colour to check
	 * @return true, if colour is contained
	 */
	public boolean contains(Colour c) {
		boolean isInRange = isValueInConstraint(c.hue(), hueConstraint);
		isInRange &= isValueInConstraint(c.saturation(), saturationConstraint);
		isInRange &= isValueInConstraint(c.brightness(), brightnessConstraint);
		isInRange &= isValueInConstraint(c.alpha(), alphaConstraint);
		return isInRange;
	}

	/**
	 * Creates a shallow copy of the range.
	 * 
	 * @return copy
	 */
	public ColourRange copy() {
		return copy(null, 0);
	}

	/**
	 * Creates a copy of the range but overrides the hue and alpha constraints
	 * taken from the given colour (if specified).
	 * 
	 * @param c
	 *            colour, if the new range is to be used to create specific
	 *            shades of that colour only
	 * @param variance
	 *            hue variance (use {@link #DEFAULT_VARIANCE} for default)
	 * @return copy
	 */
	public ColourRange copy(Colour c, float variance) {
		ColourRange range = new ColourRange();
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

	/**
	 * Creates a new colour based on the flexible constraints of the range.
	 * 
	 * @return colour
	 */
	public Colour getColor() {
		return getColor(null, 0);
	}

	/**
	 * Creates a new colour based on the constraints defined in the range. If an
	 * input colour is specified, the method will use the hue of that colour and
	 * the given variance to create a shade of a hue within the tolerance.
	 * 
	 * @param c
	 * @param variance
	 * @return colour
	 */
	public Colour getColor(Colour c, float variance) {
		float h, s, b, a;
		if (c != null) {
			if (c.isBlack()) {
				return Colour
						.newHSVA(c.hue(), 0, black.pickRandom(), c.alpha());
			} else if (c.isWhite()) {
				return Colour
						.newHSVA(c.hue(), 0, white.pickRandom(), c.alpha());
			}
			if (c.isGrey()) {
				return Colour.newHSVA(c.hue(), 0, MathUtils.flipCoin() ? black
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
		return Colour.newHSVA(h, s, b, a);
	}

	/**
	 * Creates a new shade of the given hue based on the other constraints of
	 * the range.
	 * 
	 * @param hue
	 * @return colour
	 */
	public Colour getColor(Hue hue) {
		return Colour.newHSVA(hue.getHue(), saturationConstraint.pickRandom()
				.pickRandom(), brightnessConstraint.pickRandom().pickRandom(),
				alphaConstraint.pickRandom().pickRandom());
	}

	/**
	 * Creates a new {@link ColourList} of shades of the given {@link Colour}
	 * based on the other constraints of the range.
	 * 
	 * @see #getColor(Colour, float)
	 * @param c
	 *            base colour
	 * @param num
	 *            number of colours to create
	 * @param variance
	 *            hue variance
	 * @return colour list
	 */
	public ColourList getColors(Colour c, int num, float variance) {
		ColourList list = new ColourList();
		for (int i = 0; i < num; i++) {
			list.add(getColor(c, variance));
		}
		return list;
	}

	/**
	 * Creates a new {@link ColourList} of colours based on the constraints of
	 * this range.
	 * 
	 * @see #getColor()
	 * @param num
	 *            number of colours to create
	 * @return colour list
	 */
	public ColourList getColors(int num) {
		return getColors(null, num, DEFAULT_VARIANCE);
	}

	/**
	 * Creates a new shade of gray based on the input brightness and the black
	 * and white constraints of the range.
	 * 
	 * @param brightness
	 *            input brightness
	 * @param variance
	 *            hue variance (this might seem irrevelant, but might be
	 *            important if the created colour is being saturated later on)
	 * @return colour/shade of gray
	 */
	public Colour getGrayscale(float brightness, float variance) {
		return getColor(Colour.newGray(brightness), variance);
	}

	/**
	 * @return name of the range
	 */
	public String getName() {
		return name;
	}

	/**
	 * Creates a copy of the current range and adds the given one to it.
	 * 
	 * @see #add(ColourRange)
	 * @param range
	 *            range to add
	 * @return summed copy
	 */
	public ColourRange getSum(ColourRange range) {
		return copy().add(range);
	}

	/**
	 * @param val
	 * @param rangeSet
	 * @return true, if value is within range
	 */
	protected boolean isValueInConstraint(float val,
			GenericSet<FloatRange> rangeSet) {
		boolean isValid = false;
		for (FloatRange r : rangeSet) {
			isValid |= r.isValueInRange(val);
		}
		return isValid;
	}
}