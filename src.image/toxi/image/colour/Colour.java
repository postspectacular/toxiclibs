package toxi.image.colour;

import toxi.math.MathUtils;

public class Colour {

	public static final ColourMode RGB = new ColourMode("RGB");
	public static final ColourMode HSV = new ColourMode("HSV");
	public static final ColourMode CMYK = new ColourMode("CMYK");

	private static final float BLACK_POINT = 0.02f;
	private static final float WHITE_POINT = 0.99f;
	private static final float GREY_THRESHOLD = 0.01f;

	public static final float INV8BIT = 1f / 255;

	protected float[] rgb;
	protected float[] cmyk;
	protected float[] hsv;
	public float alpha;

	protected Colour() {
		rgb = new float[3];
		hsv = new float[3];
		cmyk = new float[4];
	}

	public Colour(Colour c) {
		this();
		System.arraycopy(c.rgb, 0, rgb, 0, 3);
		System.arraycopy(c.hsv, 0, hsv, 0, 3);
		System.arraycopy(c.cmyk, 0, cmyk, 0, 4);
		this.alpha = c.alpha;
	}

	public static final Colour newHex(String hexRGB) {
		Colour c = new Colour();
		c.setRGB(hexToRGB(hexRGB));
		c.alpha = 1;
		return c;
	}

	public static final Colour newGray(float gray) {
		return newGrayAlpha(gray, 1);
	}

	public static final Colour newGrayAlpha(float gray, float alpha) {
		Colour c = new Colour();
		c.setRGB(new float[] { gray, gray, gray });
		c.alpha = alpha;
		return c;
	}

	public static final Colour newRGB(float r, float g, float b) {
		return newRGBA(r, g, b, 1);
	}

	public static final Colour newRGBA(float r, float g, float b, float a) {
		Colour c = new Colour();
		c.setRGB(new float[] { r, g, b });
		c.alpha = a;
		return c;
	}

	public static final Colour newHSV(float h, float s, float v) {
		return newHSVA(h, s, v, 1);
	}

	public static final Colour newHSVA(float h, float s, float v, float a) {
		Colour c = new Colour();
		c.setHSV(new float[] { h, s, v });
		c.alpha = a;
		return c;
	}

	public static final Colour newCMYK(float c, float m, float y, float k) {
		return newCMYKA(c, m, y, k, 1);
	}

	public static final Colour newCMYKA(float c, float m, float y, float k,
			float a) {
		Colour col = new Colour();
		col.setCMYK(new float[] { c, m, y, k });
		col.alpha = a;
		return col;
	}

	public void setRGB(float[] newRGB) {
		rgb[0] = newRGB[0];
		rgb[1] = newRGB[1];
		rgb[2] = newRGB[2];
		float[] temp = rgbToCMYK(rgb[0], rgb[1], rgb[2]);
		cmyk[0] = temp[0];
		cmyk[1] = temp[1];
		cmyk[2] = temp[2];
		cmyk[3] = temp[3];
		rgbToHSV(rgb[0], rgb[1], rgb[2], temp);
		hsv[0] = temp[0];
		hsv[1] = temp[1];
		hsv[2] = temp[2];
	}

	public void setCMYK(float[] newCMYK) {
		cmyk[0] = newCMYK[0];
		cmyk[1] = newCMYK[1];
		cmyk[2] = newCMYK[2];
		cmyk[3] = newCMYK[3];
		float[] temp = cmykToRGB(cmyk[0], cmyk[1], cmyk[2], cmyk[3]);
		rgb[0] = temp[0];
		rgb[1] = temp[1];
		rgb[2] = temp[2];
		rgbToHSV(rgb[0], rgb[1], rgb[2], temp);
		hsv[0] = temp[0];
		hsv[1] = temp[1];
		hsv[2] = temp[2];
	}

	public void setHSV(float[] newHSV) {
		hsv[0] = newHSV[0];
		hsv[1] = newHSV[1];
		hsv[2] = newHSV[2];
		float[] temp = hsvToRGB(hsv[0], hsv[1], hsv[2]);
		rgb[0] = temp[0];
		rgb[1] = temp[1];
		rgb[2] = temp[2];
		rgbToCMYK(rgb[0], rgb[1], rgb[2], temp);
		cmyk[0] = temp[0];
		cmyk[1] = temp[1];
		cmyk[2] = temp[2];
		cmyk[3] = temp[3];
	}

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

	public static final String rgbToHex(float r, float g, float b) {
		String hex = Integer.toHexString((int) (r * 0xff))
				+ Integer.toHexString((int) (g * 0xff))
				+ Integer.toHexString((int) (b * 0xff));
		return hex;
	}

	public static final float[] cmykToRGB(float c, float m, float y, float k) {
		return cmykToRGB(c, m, y, k, new float[3]);
	}

	public static final float[] cmykToRGB(float c, float m, float y, float k,
			float[] rgb) {
		rgb[0] = 1.0f - MathUtils.min(1.0f, c + k);
		rgb[0] = 1.0f - MathUtils.min(1.0f, m + k);
		rgb[0] = 1.0f - MathUtils.min(1.0f, y + k);
		return rgb;
	}

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

	public static final float[] hsvToRGB(float h, float s, float v) {
		return hsvToRGB(h, s, v, new float[3]);
	}

	public static final float[] hsvToRGB(float h, float s, float v, float[] rgb) {
		if (s == 0)
			rgb[0] = rgb[1] = rgb[2] = v;
		else {
			h = h / (60.0f / 360);
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

	public static final float[] rgbToHSV(float r, float g, float b) {
		return rgbToHSV(r, g, b, new float[3]);
	}

	public static final float[] rgbToHSV(float r, float g, float b, float[] hsv) {
		float h = 0, s = 0;
		float v = MathUtils.max(r, g, b);
		float d = v - MathUtils.min(r, g, b);

		if (v != 0)
			s = d / v;

		if (s != 0) {
			if (r == v)
				h = (g - b) / d;
			else if (g == v)
				h = 2 + (b - r) / d;
			else
				h = 4 + (r - g) / d;
		}

		h = h * (60.0f / 360);
		if (h < 0)
			h = h + 1.0f;
		return hsv;
	}

	/*
	 * Converts CIE Lab to RGB components.
	 * 
	 * First we have to convert to XYZ color space. Conversion involves using a
	 * white point, in this case D65 which represents daylight illumination.
	 * 
	 * Algorithm adopted from: http://www.easyrgb.com/math.php
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
			if (p > 0.008856)
				rgb[i] = p;
			else
				rgb[i] = (rgb[i] - 16 / 116.0f) / 7.787f;
		}

		// Observer = 2, Illuminant = D65
		x = rgb[0] * 95.047f / 100;
		y = rgb[1] * 100.0f / 100;
		z = rgb[2] * 108.883f / 100;

		rgb[0] = x * 3.2406f + y * -1.5372f + z * -0.4986f;
		rgb[1] = x * -0.9689f + y * 1.8758f + z * 0.0415f;
		rgb[2] = x * 0.0557f + y * -0.2040f + z * 1.0570f;
		double tpow = 1 / 2.4;
		for (int i = 0; i < 3; i++) {
			if (rgb[i] > 0.0031308)
				rgb[i] = (float) (1.055 * Math.pow(rgb[i], tpow) - 0.055);
			else
				rgb[i] = 12.92f * rgb[i];
		}
		return rgb;
	}

	public float alpha() {
		return alpha;
	}

	public float brightness() {
		return hsv[2];
	}

	public float hue() {
		return hsv[0];
	}

	public float saturation() {
		return hsv[1];
	}

	public float red() {
		return rgb[0];
	}

	public float green() {
		return rgb[1];
	}

	public float blue() {
		return rgb[2];
	}

	public float cyan() {
		return cmyk[0];
	}

	public float magenta() {
		return cmyk[0];
	}

	public float yellow() {
		return cmyk[0];
	}

	public float black() {
		return cmyk[0];
	}

	public boolean isWhite() {
		return (rgb[0] > WHITE_POINT && rgb[1] > WHITE_POINT && rgb[2] > WHITE_POINT);
	}

	public boolean isBlack() {
		return (rgb[0] < BLACK_POINT && rgb[1] < BLACK_POINT && rgb[2] < BLACK_POINT);
	}

	public boolean isGrey() {
		return hsv[1] < GREY_THRESHOLD;
	}

	public Colour darken(float step) {
		hsv[2] = MathUtils.clip(hsv[2] - step, 0, 1);
		return this;
	}

	public Colour lighten(float step) {
		hsv[2] = MathUtils.clip(hsv[2] + step, 0, 1);
		return this;
	}

	public Colour getDarkened(float step) {
		return new Colour(this).darken(step);
	}
}