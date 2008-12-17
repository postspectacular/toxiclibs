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

import toxi.geom.Vec2D;
import toxi.geom.Vec3D;
import toxi.math.MathUtils;

/**
 * Floating point colour class with implicit RGB, HSV, CMYK access modes,
 * conversion and colour theory utils. Based on the <a href="">Colors
 * library</a> for <a href="http:/Nodebox.net/">NodeBox</a>
 * 
 */
public class Colour {

	protected static final float INV60DEGREES = 60.0f / 360;
	protected static final float INV8BIT = 1f / 255;
	protected static final double EPS = .001;

	protected static final Vec2D[] RYB_WHEEL = new Vec2D[] { new Vec2D(0, 0),
			new Vec2D(15, 8), new Vec2D(30, 17), new Vec2D(45, 26),
			new Vec2D(60, 34), new Vec2D(75, 41), new Vec2D(90, 48),
			new Vec2D(105, 54), new Vec2D(120, 60), new Vec2D(135, 81),
			new Vec2D(150, 103), new Vec2D(165, 123), new Vec2D(180, 138),
			new Vec2D(195, 155), new Vec2D(210, 171), new Vec2D(225, 187),
			new Vec2D(240, 204), new Vec2D(255, 219), new Vec2D(270, 234),
			new Vec2D(285, 251), new Vec2D(300, 267), new Vec2D(315, 282),
			new Vec2D(330, 298), new Vec2D(345, 329), new Vec2D(360, 0) };

	/**
	 * Maximum rgb component value for a colour to be classified as black.
	 * 
	 * @see #isBlack()
	 */
	public static float BLACK_POINT = 0.08f;

	/**
	 * Minimum rgb component value for a colour to be classified as white.
	 * 
	 * @see #isWhite()
	 */
	public static float WHITE_POINT = 1f;

	/**
	 * Maximum saturations value for a colour to be classified as grey
	 * 
	 * @see #isGrey()
	 */
	public static float GREY_THRESHOLD = 0.01f;

	public static final Colour RED = newRGB(1, 0, 0);
	public static final Colour GREEN = newRGB(0, 1, 0);
	public static final Colour BLUE = newRGB(0, 0, 1);
	public static final Colour CYAN = newRGB(0, 1, 1);
	public static final Colour MAGENTA = newRGB(1, 0, 1);
	public static final Colour YELLOW = newRGB(1, 1, 0);
	public static final Colour BLACK = newRGB(0, 0, 0);
	public static final Colour WHITE = newRGB(1, 1, 1);

	protected float[] rgb;
	protected float[] cmyk;
	protected float[] hsv;
	public float alpha;

	/**
	 * Converts CMYK floats into an RGB array.
	 * 
	 * @param c
	 * @param m
	 * @param y
	 * @param k
	 * @return rgb array
	 */
	public static final float[] cmykToRGB(float c, float m, float y, float k) {
		return cmykToRGB(c, m, y, k, new float[3]);
	}

	/**
	 * Converts CMYK floats into the given RGB array.
	 * 
	 * @param c
	 * @param m
	 * @param y
	 * @param k
	 * @param rgb
	 * @return rgb array
	 */
	public static final float[] cmykToRGB(float c, float m, float y, float k,
			float[] rgb) {
		rgb[0] = 1.0f - MathUtils.min(1.0f, c + k);
		rgb[1] = 1.0f - MathUtils.min(1.0f, m + k);
		rgb[2] = 1.0f - MathUtils.min(1.0f, y + k);
		return rgb;
	}

	/**
	 * Converts hex string into a RGB array.
	 * 
	 * @param hexRGB
	 * @return rgb array
	 */
	public static final float[] hexToRGB(String hexRGB) {
		return hexToRGB(hexRGB, new float[3]);
	}

	public static final float[] hexToRGB(String hexRGB, float[] rgb) {
		try {
			int rgbInt = Integer.parseInt(hexRGB, 16);
			rgb[0] = ((rgbInt >> 16) & 0xff) * INV8BIT;
			rgb[1] = ((rgbInt >> 8) & 0xff) * INV8BIT;
			rgb[2] = (rgbInt & 0xff) * INV8BIT;
		} catch (NumberFormatException e) {
			rgb[0] = rgb[1] = rgb[2] = 0;
		}
		return rgb;
	}

	/**
	 * Converts HSV values into RGB array.
	 * 
	 * @param h
	 * @param s
	 * @param v
	 * @return rgb array
	 */
	public static final float[] hsvToRGB(float h, float s, float v) {
		return hsvToRGB(h, s, v, new float[3]);
	}

	public static final float[] hsvToRGB(float h, float s, float v, float[] rgb) {
		if (Float.compare(s, 0.0f) == 0) {
			rgb[0] = rgb[1] = rgb[2] = v;
		} else {
			h /= INV60DEGREES;
			int i = (int) h;
			float f = h - i;
			float p = v * (1 - s);
			float q = v * (1 - s * f);
			float t = v * (1 - s * (1 - f));

			if (i == 0) {
				rgb[0] = v;
				rgb[1] = t;
				rgb[2] = p;
			} else if (i == 1) {
				rgb[0] = q;
				rgb[1] = v;
				rgb[2] = p;
			} else if (i == 2) {
				rgb[0] = p;
				rgb[1] = v;
				rgb[2] = t;
			} else if (i == 3) {
				rgb[0] = p;
				rgb[1] = q;
				rgb[2] = v;
			} else if (i == 4) {
				rgb[0] = t;
				rgb[1] = p;
				rgb[2] = v;
			} else {
				rgb[0] = v;
				rgb[1] = p;
				rgb[2] = q;
			}
		}
		return rgb;
	}

	public static final float[] labToRGB(float l, float a, float b) {
		return labToRGB(l, a, b, new float[3]);
	}

	/**
	 * Converts CIE Lab to RGB components.
	 * 
	 * First we have to convert to XYZ colour space. Conversion involves using a
	 * white point, in this case D65 which represents daylight illumination.
	 * 
	 * Algorithm adopted from: http://www.easyrgb.com/math.php
	 * 
	 * @param l
	 * @param a
	 * @param b
	 * @param rgb
	 * @return rgb array
	 */
	public static final float[] labToRGB(float l, float a, float b, float[] rgb) {
		float y = (l + 16) / 116.0f;
		float x = a / 500.0f + y;
		float z = y - b / 200.0f;
		rgb[0] = x;
		rgb[1] = y;
		rgb[2] = z;
		for (int i = 0; i < 3; i++) {
			float p = (float) Math.pow(rgb[i], 3);
			if (p > 0.008856) {
				rgb[i] = p;
			} else {
				rgb[i] = (rgb[i] - 16 / 116.0f) / 7.787f;
			}
		}

		// Observer = 2, Illuminant = D65
		x = rgb[0] * 0.95047f;
		y = rgb[1];
		z = rgb[2] * 1.08883f;

		rgb[0] = x * 3.2406f + y * -1.5372f + z * -0.4986f;
		rgb[1] = x * -0.9689f + y * 1.8758f + z * 0.0415f;
		rgb[2] = x * 0.0557f + y * -0.2040f + z * 1.0570f;
		double tpow = 1 / 2.4;
		for (int i = 0; i < 3; i++) {
			if (rgb[i] > 0.0031308) {
				rgb[i] = (float) (1.055 * Math.pow(rgb[i], tpow) - 0.055);
			} else {
				rgb[i] = 12.92f * rgb[i];
			}
		}
		return rgb;
	}

	/**
	 * Factory method. Creates new colour from ARGB int.
	 * 
	 * @param argb
	 * @return new colour
	 */
	public static final Colour newARGB(int argb) {
		return newRGBA(((argb >> 16) & 0xff) * INV8BIT, ((argb >> 8) & 0xff)
				* INV8BIT, (argb & 0xff) * INV8BIT, (argb >>> 24) * INV8BIT);
	}

	/**
	 * Factory method. Creates new colour from CMYK values.
	 * 
	 * @param c
	 * @param m
	 * @param y
	 * @param k
	 * @return new colour
	 */
	public static final Colour newCMYK(float c, float m, float y, float k) {
		return newCMYKA(c, m, y, k, 1);
	}

	/**
	 * Factory method. Creates new colour from CMYK + alpha values.
	 * 
	 * @param c
	 * @param m
	 * @param y
	 * @param k
	 * @param a
	 * @return new colour
	 */
	public static final Colour newCMYKA(float c, float m, float y, float k,
			float a) {
		Colour col = new Colour();
		col.setCMYK(new float[] { c, m, y, k });
		col.alpha = MathUtils.clip(a, 0, 1);
		return col;
	}

	/**
	 * Factory method. Creates a new shade of gray + alpha.
	 * 
	 * @param gray
	 * @return new colour.
	 */
	public static final Colour newGray(float gray) {
		return newGrayAlpha(gray, 1);
	}

	public static final Colour newGrayAlpha(float gray, float alpha) {
		Colour c = new Colour();
		c.setRGB(new float[] { gray, gray, gray });
		c.alpha = alpha;
		return c;
	}

	/**
	 * Factory method. New colour from hex string.
	 * 
	 * @param hexRGB
	 * @return new colour
	 */
	public static final Colour newHex(String hexRGB) {
		Colour c = new Colour();
		c.setRGB(hexToRGB(hexRGB));
		c.alpha = 1;
		return c;
	}

	/**
	 * Factory method. New colour from hsv values.
	 * 
	 * @param h
	 * @param s
	 * @param v
	 * @return new colour
	 */
	public static final Colour newHSV(float h, float s, float v) {
		return newHSVA(h, s, v, 1);
	}

	public static Colour newHSV(Hue h, float s, float v) {
		return newHSV(h.getHue(), s, v);
	}

	public static final Colour newHSVA(float h, float s, float v, float a) {
		Colour c = new Colour();
		c.setHSV(new float[] { h, s, v });
		c.alpha = MathUtils.clip(a, 0, 1);
		return c;
	}

	/**
	 * Factory method. Creates new random colour.
	 * 
	 * @return random colour
	 */
	public static final Colour newRandom() {
		return newRGBA(MathUtils.random(1f), MathUtils.random(1f), MathUtils
				.random(1f), 1);
	}

	/**
	 * Factory method. Creates new colour from RGB values.
	 * 
	 * @param r
	 * @param g
	 * @param b
	 * @return new colour
	 */
	public static final Colour newRGB(float r, float g, float b) {
		return newRGBA(r, g, b, 1);
	}

	public static final Colour newRGBA(float r, float g, float b, float a) {
		Colour c = new Colour();
		c.setRGB(new float[] { r, g, b });
		c.alpha = MathUtils.clip(a, 0, 1);
		return c;
	}

	/**
	 * Converts the RGB values into a CMYK array.
	 * 
	 * @param r
	 * @param g
	 * @param b
	 * @return cmyk array
	 */
	public static final float[] rgbToCMYK(float r, float g, float b) {
		return rgbToCMYK(r, g, b, new float[4]);
	}

	public static final float[] rgbToCMYK(float r, float g, float b,
			float[] cmyk) {
		cmyk[0] = 1 - r;
		cmyk[1] = 1 - g;
		cmyk[2] = 1 - b;
		cmyk[3] = MathUtils.min(cmyk[0], cmyk[1], cmyk[2]);
		cmyk[0] = MathUtils.clip(cmyk[0] - cmyk[3], 0, 1);
		cmyk[1] = MathUtils.clip(cmyk[1] - cmyk[3], 0, 1);
		cmyk[2] = MathUtils.clip(cmyk[2] - cmyk[3], 0, 1);
		cmyk[3] = MathUtils.clip(cmyk[3], 0, 1);
		return cmyk;
	}

	/**
	 * Formats the RGB float values into hex integers.
	 * 
	 * @param r
	 * @param g
	 * @param b
	 * @return hex string
	 */
	public static final String rgbToHex(float r, float g, float b) {
		String hex = Integer.toHexString((int) (r * 0xff))
				+ Integer.toHexString((int) (g * 0xff))
				+ Integer.toHexString((int) (b * 0xff));
		return hex;
	}

	/**
	 * Converts the RGB values into an HSV array.
	 * 
	 * @param r
	 * @param g
	 * @param b
	 * @return hsv array
	 */
	public static final float[] rgbToHSV(float r, float g, float b) {
		return rgbToHSV(r, g, b, new float[3]);
	}

	public static final float[] rgbToHSV(float r, float g, float b, float[] hsv) {
		float h = 0, s = 0;
		float v = MathUtils.max(r, g, b);
		float d = v - MathUtils.min(r, g, b);

		if (v != 0.0) {
			s = d / v;
		}
		if (s != 0.0) {
			if (Float.compare(r, v) == 0) {
				h = (g - b) / d;
			} else if (Float.compare(g, v) == 0) {
				h = 2 + (b - r) / d;
			} else {
				h = 4 + (r - g) / d;
			}
		}

		h *= INV60DEGREES;

		if (h < 0) {
			h += 1.0f;
		}

		hsv[0] = h;
		hsv[1] = s;
		hsv[2] = v;
		return hsv;
	}

	protected Colour() {
		rgb = new float[3];
		hsv = new float[3];
		cmyk = new float[4];
	}

	/**
	 * Creates a deep copy of the given colour.
	 * 
	 * @param c
	 */
	public Colour(Colour c) {
		this();
		System.arraycopy(c.rgb, 0, rgb, 0, 3);
		System.arraycopy(c.hsv, 0, hsv, 0, 3);
		System.arraycopy(c.cmyk, 0, cmyk, 0, 4);
		this.alpha = c.alpha;
	}

	/**
	 * Changes the brightness of the colour by the given amount in the direction
	 * towards either the black or white point (depending on if current
	 * brightness >= 50%)
	 * 
	 * @param amount
	 * @return itself
	 */
	public Colour adjustConstrast(float amount) {
		return hsv[2] < 0.5 ? darken(amount) : lighten(amount);
	}

	/**
	 * Adds the given HSV values as offsets to the current colour. Hue will
	 * automatically wrap.
	 * 
	 * @param h
	 * @param s
	 * @param v
	 * @return itself
	 */
	public Colour adjustHSV(float h, float s, float v) {
		return setHSV(new float[] { hsv[0] + h, hsv[1] + s, hsv[2] + v });
	}

	/**
	 * Adds the given RGB values as offsets to the current colour. Colour will
	 * clip at black or white.
	 * 
	 * @param r
	 * @param g
	 * @param b
	 * @return itself
	 */
	public Colour adjustRGB(float r, float g, float b) {
		return setRGB(new float[] { rgb[0] + r, rgb[1] + g, rgb[2] + b });
	}

	/**
	 * @return the colour's alpha component
	 */
	public float alpha() {
		return alpha;
	}

	/**
	 * Rotates this colour by a random amount (not exceeding the one specified)
	 * and creates variations in saturation and brightness based on the 2nd
	 * parameter.
	 * 
	 * @param theta
	 *            max. rotation angle (in radians)
	 * @param delta
	 *            max. sat/bri variance
	 * @return itself
	 */
	public Colour analog(float theta, float delta) {
		return analog(MathUtils.degrees(theta), delta);
	}

	public Colour analog(int angle, float delta) {
		rotateRYB((int) (angle * MathUtils.normalizedRandom()));
		hsv[1] += delta * MathUtils.normalizedRandom();
		hsv[2] += delta * MathUtils.normalizedRandom();
		return setHSV(hsv);
	}

	/**
	 * @return the colour's black component
	 */

	public float black() {
		return cmyk[0];
	}

	/**
	 * Blends the colour with the given one by the stated amount
	 * 
	 * @param c
	 *            target colour
	 * @param t
	 *            interpolation factor
	 * @return itself
	 */
	public Colour blend(Colour c, float t) {
		rgb[0] += (c.rgb[0] - rgb[0]) * t;
		rgb[1] += (c.rgb[1] - rgb[1]) * t;
		rgb[2] += (c.rgb[2] - rgb[2]) * t;
		alpha += (c.alpha - alpha) * t;
		return setRGB(rgb);
	}

	/**
	 * @return the colour's blue component
	 */

	public float blue() {
		return rgb[2];
	}

	/**
	 * @return colour HSV brightness (not luminance!)
	 */
	public float brightness() {
		return hsv[2];
	}

	/**
	 * @return ifself, as complementary colour
	 */
	public Colour complement() {
		return rotateRYB(180);
	}

	public Colour copy() {
		return new Colour(this);
	}

	/**
	 * @return the colour's cyan component
	 */

	public float cyan() {
		return cmyk[0];
	}

	public Colour darken(float step) {
		hsv[2] = MathUtils.clip(hsv[2] - step, 0, 1);
		return setHSV(hsv);
	}

	public Colour desaturate(float step) {
		hsv[1] = MathUtils.clip(hsv[1] - step, 0, 1);
		return setHSV(hsv);
	}

	/**
	 * Calculates the CMYK distance to the given colour.
	 * 
	 * @param c
	 *            target colour
	 * @return distance
	 */
	public float distanceToCMYK(Colour c) {
		float dc = cmyk[0] - c.cmyk[0];
		float dm = cmyk[1] - c.cmyk[1];
		float dy = cmyk[2] - c.cmyk[2];
		float dk = cmyk[3] - c.cmyk[3];
		return (float) Math.sqrt(dc * dc + dm * dm + dy * dy + dk * dk);
	}

	/**
	 * Calculates the HSV distance to the given colour.
	 * 
	 * @param c
	 *            target colour
	 * @return distance
	 */
	public float distanceToHSV(Colour c) {
		float hue = hsv[0] * MathUtils.TWO_PI;
		float hue2 = c.hsv[0] * MathUtils.TWO_PI;
		Vec3D v1 = new Vec3D((float) (Math.cos(hue) * hsv[1]), (float) (Math
				.sin(hue) * hsv[1]), hsv[2]);
		Vec3D v2 = new Vec3D((float) (Math.cos(hue2) * c.hsv[1]), (float) (Math
				.sin(hue2) * c.hsv[1]), c.hsv[2]);
		return v1.distanceTo(v2);
	}

	/**
	 * Calculates the RGB distance to the given colour.
	 * 
	 * @param c
	 *            target colour
	 * @return distance
	 */
	public float distanceToRGB(Colour c) {
		float dr = rgb[0] - c.rgb[0];
		float dg = rgb[1] - c.rgb[1];
		float db = rgb[2] - c.rgb[2];
		return (float) Math.sqrt(dr * dr + dg * dg + db * db);
	}

	@Override
	public boolean equals(Object o) {
		if (o != null && o instanceof Colour) {
			Colour c = (Colour) o;
			float dr = c.rgb[0] - rgb[0];
			float dg = c.rgb[1] - rgb[1];
			float db = c.rgb[2] - rgb[2];
			float da = c.alpha - alpha;
			double d = Math.sqrt(dr * dr + dg * dg + db * db + da * da);
			return d < EPS;
		}
		return false;
	}

	public Colour getAnalog(float theta, float delta) {
		return new Colour(this).analog(theta, delta);
	}

	public Colour getAnalog(int angle, float delta) {
		return new Colour(this).analog(angle, delta);
	}

	public Colour getBlended(Colour c, float t) {
		return new Colour(this).blend(c, t);
	}

	/**
	 * @return an instance of the closest named hue to this colour.
	 */
	public Hue getClosestHue() {
		return Hue.getClosest(hsv[0], false);
	}

	/**
	 * @param primaryOnly
	 *            if true, only primary colour hues are considered
	 * @return an instance of the closest named (primary) hue to this colour.
	 */
	public Hue getClosestHue(boolean primaryOnly) {
		return Hue.getClosest(hsv[0], primaryOnly);
	}

	public Colour getComplement() {
		return new Colour(this).complement();
	}

	public float getComponentValue(AccessCriteria criteria) {
		switch (criteria.getMode()) {
		case HSV:
			return hsv[criteria.getComponent()];
		case RGB:
			return rgb[criteria.getComponent()];
		case CMYK:
			return cmyk[criteria.getComponent()];
		case ALPHA:
			return alpha;
		}
		return 0;
	}

	/**
	 * @param step
	 * @return a darkened copy
	 */
	public Colour getDarkened(float step) {
		return new Colour(this).darken(step);
	}

	/**
	 * @param step
	 * @return a desaturated copy
	 */
	public Colour getDesaturated(float step) {
		return new Colour(this).desaturate(step);
	}

	/**
	 * @param step
	 * @return a lightened copy
	 */
	public Colour getLightened(float step) {
		return new Colour(this).lighten(step);
	}

	/**
	 * @param theta
	 *            rotation angle in radians
	 * @return a RYB rotated copy
	 */
	public Colour getRotatedRYB(float theta) {
		return new Colour(this).rotateRYB(theta);
	}

	/**
	 * @param angle
	 *            rotation angle in degrees
	 * @return a RYB rotated copy
	 */
	public Colour getRotatedRYB(int angle) {
		return new Colour(this).rotateRYB(angle);
	}

	/**
	 * @param step
	 * @return a saturated copy
	 */
	public Colour getSaturated(float step) {
		return new Colour(this).saturate(step);
	}

	/**
	 * @return the colour's green component
	 */

	public float green() {
		return rgb[1];
	}

	@Override
	public int hashCode() {
		return (int) (rgb[0] * 1000000 + rgb[1] * 100000 + rgb[2] * 10000 + alpha * 1000);
	}

	/**
	 * @return the colour's hue
	 */
	public float hue() {
		return hsv[0];
	}

	/**
	 * Inverts the colour.
	 * 
	 * @return itself
	 */
	public Colour invert() {
		rgb[0] = 1 - rgb[0];
		rgb[1] = 1 - rgb[1];
		rgb[2] = 1 - rgb[2];
		return setRGB(rgb);
	}

	/**
	 * @return true, if all rgb component values are equal and less than
	 *         {@link #BLACK_POINT}
	 */
	public boolean isBlack() {
		return (rgb[0] <= BLACK_POINT && Float.compare(rgb[0], rgb[1]) == 0 && Float
				.compare(rgb[0], rgb[2]) == 0);
	}

	/**
	 * @return true, if the saturation component value is less than
	 *         {@link #GREY_THRESHOLD}
	 */
	public boolean isGrey() {
		return hsv[1] < GREY_THRESHOLD;
	}

	/**
	 * @return true, if this colours hue is matching one of the 7 defined
	 *         primary hues.
	 */
	public boolean isPrimary() {
		return Hue.isPrimary(hsv[0]);
	}

	/**
	 * @return true, if all rgb component values are equal and greater than
	 *         {@link #WHITE_POINT}
	 */
	public boolean isWhite() {
		return (rgb[0] >= WHITE_POINT && Float.compare(rgb[0], rgb[1]) == 0 && Float
				.compare(rgb[0], rgb[2]) == 0);
	}

	/**
	 * Lightens the colour by stated amount.
	 * 
	 * @param step
	 *            lighten amount
	 * @return itself
	 */
	public Colour lighten(float step) {
		hsv[2] = MathUtils.clip(hsv[2] + step, 0, 1);
		return setHSV(hsv);
	}

	/**
	 * Computes the colour's luminance using this formula: lum=0.299*red +
	 * 0.587*green + 0.114 *blue
	 * 
	 * @return luminance
	 */
	public float luminance() {
		return rgb[0] * 0.299f + rgb[1] * 0.587f + rgb[2] * 0.114f;
	}

	/**
	 * @return the colour's magenta component
	 */

	public float magenta() {
		return cmyk[0];
	}

	/**
	 * @return the colour's red component
	 */

	public float red() {
		return rgb[0];
	}

	public Colour rotateRYB(float theta) {
		return rotateRYB((int) MathUtils.degrees(theta));
	}

	public Colour rotateRYB(int theta) {
		float h = hsv[0] * 360;
		theta %= 360;

		float resultHue = 0;
		for (int i = 0; i < RYB_WHEEL.length - 1; i++) {
			Vec2D p = RYB_WHEEL[i];
			Vec2D q = RYB_WHEEL[i + 1];
			if (q.y < p.y) {
				q.y += 360;
			}
			if (p.y <= h && h <= q.y) {
				resultHue = p.x + (q.x - p.x) * (h - p.y) / (q.y - p.y);
				break;
			}
		}

		// And the user-given angle (e.g. complement).
		resultHue = (resultHue + theta) % 360;

		// For the given angle, find out what hue is
		// located there on the artistic colour wheel.
		for (int i = 0; i < RYB_WHEEL.length - 1; i++) {
			Vec2D p = RYB_WHEEL[i];
			Vec2D q = RYB_WHEEL[i + 1];
			if (q.y < p.y) {
				q.y += 360;
			}
			if (p.x <= resultHue && resultHue <= q.x) {
				h = p.y + (q.y - p.y) * (resultHue - p.x) / (q.x - p.x);
				break;
			}
		}

		hsv[0] = (h % 360) / 360.0f;
		return setHSV(hsv);
	}

	public Colour saturate(float step) {
		hsv[1] = MathUtils.clip(hsv[1] + step, 0, 1);
		return setHSV(hsv);
	}

	public float saturation() {
		return hsv[1];
	}

	public Colour setAlpha(float alpha) {
		this.alpha = alpha;
		return this;
	}

	public Colour setBlack(float val) {
		cmyk[3] = val;
		return setCMYK(cmyk);
	}

	public Colour setBlue(float blue) {
		rgb[2] = blue;
		return setRGB(rgb);
	}

	public Colour setBrightness(float brightness) {
		hsv[2] = MathUtils.clip(brightness, 0, 1);
		return setHSV(hsv);
	}

	public Colour setCMYK(float[] newCMYK) {
		cmyk[0] = MathUtils.clip(newCMYK[0], 0, 1);
		cmyk[1] = MathUtils.clip(newCMYK[1], 0, 1);
		cmyk[2] = MathUtils.clip(newCMYK[2], 0, 1);
		cmyk[3] = MathUtils.clip(newCMYK[3], 0, 1);
		cmykToRGB(cmyk[0], cmyk[1], cmyk[2], cmyk[3], rgb);
		rgbToHSV(rgb[0], rgb[1], rgb[2], hsv);
		return this;
	}

	public Colour setComponent(AccessCriteria criteria, float val) {
		switch (criteria.getMode()) {
		case RGB:
			rgb[criteria.getComponent()] = val;
			return setRGB(rgb);
		case HSV:
			hsv[criteria.getComponent()] = val;
			return setHSV(hsv);
		case ALPHA:
			return setAlpha(val);
		case CMYK:
			cmyk[criteria.getComponent()] = val;
			return setCMYK(cmyk);
		default:
			throw new IllegalArgumentException(
					"Invalid ColorAccessCriteria mode used");
		}
	}

	public Colour setCyan(float val) {
		cmyk[0] = val;
		return setCMYK(cmyk);
	}

	public Colour setGreen(float green) {
		rgb[1] = green;
		return setRGB(rgb);
	}

	public Colour setHSV(float[] newHSV) {
		hsv[0] = newHSV[0] % 1;
		if (hsv[0] < 0)
			hsv[0]++;
		hsv[1] = MathUtils.clip(newHSV[1], 0, 1);
		hsv[2] = MathUtils.clip(newHSV[2], 0, 1);
		hsvToRGB(hsv[0], hsv[1], hsv[2], rgb);
		rgbToCMYK(rgb[0], rgb[1], rgb[2], cmyk);
		return this;
	}

	public void setHue(float hue) {
		hue %= 1.0;
		if (hue < 0.0) {
			hue++;
		}
		hsv[0] = hue;
		setHSV(hsv);
	}

	public Colour setMagenta(float val) {
		cmyk[1] = val;
		return setCMYK(cmyk);
	}

	public Colour setRed(float red) {
		rgb[0] = red;
		return setRGB(rgb);
	}

	public Colour setRGB(float[] newRGB) {
		rgb[0] = MathUtils.clip(newRGB[0], 0, 1);
		rgb[1] = MathUtils.clip(newRGB[1], 0, 1);
		rgb[2] = MathUtils.clip(newRGB[2], 0, 1);
		rgbToCMYK(rgb[0], rgb[1], rgb[2], cmyk);
		rgbToHSV(rgb[0], rgb[1], rgb[2], hsv);
		return this;
	}

	public Colour setSaturation(float saturation) {
		hsv[1] = MathUtils.clip(saturation, 0, 1);
		return setHSV(hsv);
	}

	public Colour setYellow(float val) {
		cmyk[2] = val;
		return setCMYK(cmyk);
	}

	/**
	 * Converts the colour into a packed ARGB int.
	 * 
	 * @return colour as int
	 */
	public int toARGB() {
		return (int) (rgb[0] * 255) << 16 | (int) (rgb[1] * 255) << 8
				| (int) (rgb[2] * 255) | (int) (alpha * 255) << 24;
	}

	@Override
	public String toString() {
		return "Colour: rgb: " + rgb[0] + "," + rgb[1] + "," + rgb[2]
				+ " hsv: " + hsv[0] + "," + hsv[1] + "," + hsv[2] + " cmyk: "
				+ cmyk[0] + "," + cmyk[1] + "," + cmyk[2] + "," + cmyk[3]
				+ " alpha: " + alpha;
	}

	/**
	 * @return the colour's yellow component
	 */
	public float yellow() {
		return cmyk[0];
	}

}