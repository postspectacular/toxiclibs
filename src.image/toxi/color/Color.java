package toxi.color;

import toxi.geom.Vec2D;
import toxi.geom.Vec3D;
import toxi.math.MathUtils;

public class Color {

	protected static final Vec2D[] RYB_WHEEL = new Vec2D[] { new Vec2D(0, 0),
			new Vec2D(15, 8), new Vec2D(30, 17), new Vec2D(45, 26),
			new Vec2D(60, 34), new Vec2D(75, 41), new Vec2D(90, 48),
			new Vec2D(105, 54), new Vec2D(120, 60), new Vec2D(135, 81),
			new Vec2D(150, 103), new Vec2D(165, 123), new Vec2D(180, 138),
			new Vec2D(195, 155), new Vec2D(210, 171), new Vec2D(225, 187),
			new Vec2D(240, 204), new Vec2D(255, 219), new Vec2D(270, 234),
			new Vec2D(285, 251), new Vec2D(300, 267), new Vec2D(315, 282),
			new Vec2D(330, 298), new Vec2D(345, 329), new Vec2D(360, 0) };

	public static final float BLACK_POINT = 0.08f;
	public static final float WHITE_POINT = 1f;
	public static final float GREY_THRESHOLD = 0.01f;

	public static final float INV8BIT = 1f / 255;

	public static final Color RED = newRGB(1, 0, 0);
	public static final Color GREEN = newRGB(0, 1, 0);
	public static final Color BLUE = newRGB(0, 0, 1);
	public static final Color CYAN = newRGB(0, 1, 1);
	public static final Color MAGENTA = newRGB(1, 0, 1);
	public static final Color YELLOW = newRGB(1, 1, 0);
	public static final Color BLACK = newRGB(0, 0, 0);
	public static final Color WHITE = newRGB(1, 1, 1);

	public static final double EPS = .001;

	protected float[] rgb;
	protected float[] cmyk;
	protected float[] hsv;
	public float alpha;

	protected Color() {
		rgb = new float[3];
		hsv = new float[3];
		cmyk = new float[4];
	}

	public Color(Color c) {
		this();
		System.arraycopy(c.rgb, 0, rgb, 0, 3);
		System.arraycopy(c.hsv, 0, hsv, 0, 3);
		System.arraycopy(c.cmyk, 0, cmyk, 0, 4);
		this.alpha = c.alpha;
	}

	public static final Color newHex(String hexRGB) {
		Color c = new Color();
		c.setRGB(hexToRGB(hexRGB));
		c.alpha = 1;
		return c;
	}

	public static final Color newGray(float gray) {
		return newGrayAlpha(gray, 1);
	}

	public static final Color newGrayAlpha(float gray, float alpha) {
		Color c = new Color();
		c.setRGB(new float[] { gray, gray, gray });
		c.alpha = alpha;
		return c;
	}

	public static final Color newRGB(float r, float g, float b) {
		return newRGBA(r, g, b, 1);
	}

	public static final Color newRGBA(float r, float g, float b, float a) {
		Color c = new Color();
		c.setRGB(new float[] { r, g, b });
		c.alpha = MathUtils.clip(a, 0, 1);
		return c;
	}

	public static final Color newARGB(int argb) {
		return newRGBA(((argb >> 16) & 0xff) * INV8BIT, ((argb >> 8) & 0xff)
				* INV8BIT, (argb & 0xff) * INV8BIT, (argb >>> 24) * INV8BIT);
	}

	public static final Color newHSV(float h, float s, float v) {
		return newHSVA(h, s, v, 1);
	}

	public static final Color newHSVA(float h, float s, float v, float a) {
		Color c = new Color();
		c.setHSV(new float[] { h, s, v });
		c.alpha = MathUtils.clip(a, 0, 1);
		return c;
	}

	public static final Color newCMYK(float c, float m, float y, float k) {
		return newCMYKA(c, m, y, k, 1);
	}

	public static final Color newCMYKA(float c, float m, float y, float k,
			float a) {
		Color col = new Color();
		col.setCMYK(new float[] { c, m, y, k });
		col.alpha = MathUtils.clip(a, 0, 1);
		return col;
	}

	public Color copy() {
		return new Color(this);
	}

	public int toARGB() {
		return (int) (rgb[0] * 255) << 16 | (int) (rgb[1] * 255) << 8
				| (int) (rgb[2] * 255) | (int) (alpha * 255) << 24;
	}

	public String toString() {
		return "rgb: " + rgb[0] + "," + rgb[1] + "," + rgb[2] + " hsv: "
				+ hsv[0] + "," + hsv[1] + "," + hsv[2] + " cmyk: " + cmyk[0]
				+ "," + cmyk[1] + "," + cmyk[2] + "," + cmyk[3] + " alpha: "
				+ alpha;
	}

	public Color setRGB(float[] newRGB) {
		rgb[0] = MathUtils.clip(newRGB[0], 0, 1);
		rgb[1] = MathUtils.clip(newRGB[1], 0, 1);
		rgb[2] = MathUtils.clip(newRGB[2], 0, 1);
		rgbToCMYK(rgb[0], rgb[1], rgb[2], cmyk);
		rgbToHSV(rgb[0], rgb[1], rgb[2], hsv);
		return this;
	}

	public Color setCMYK(float[] newCMYK) {
		cmyk[0] = MathUtils.clip(newCMYK[0], 0, 1);
		cmyk[1] = MathUtils.clip(newCMYK[1], 0, 1);
		cmyk[2] = MathUtils.clip(newCMYK[2], 0, 1);
		cmyk[3] = MathUtils.clip(newCMYK[3], 0, 1);
		cmykToRGB(cmyk[0], cmyk[1], cmyk[2], cmyk[3], rgb);
		rgbToHSV(rgb[0], rgb[1], rgb[2], hsv);
		return this;
	}

	public Color setHSV(float[] newHSV) {
		hsv[0] = newHSV[0] % 1;
		if (hsv[0] < 0)
			hsv[0]++;
		hsv[1] = MathUtils.clip(newHSV[1], 0, 1);
		hsv[2] = MathUtils.clip(newHSV[2], 0, 1);
		hsvToRGB(hsv[0], hsv[1], hsv[2], rgb);
		rgbToCMYK(rgb[0], rgb[1], rgb[2], cmyk);
		return this;
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
		rgb[1] = 1.0f - MathUtils.min(1.0f, m + k);
		rgb[2] = 1.0f - MathUtils.min(1.0f, y + k);
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
		if (s == 0.0)
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

		if (v != 0.0)
			s = d / v;

		if (s != 0.0) {
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

		hsv[0] = h;
		hsv[1] = s;
		hsv[2] = v;
		return hsv;
	}

	public static final float[] labToRGB(float l, float a, float b) {
		return labToRGB(l, a, b, new float[3]);
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
		x = rgb[0] * 0.95047f;
		y = rgb[1];
		z = rgb[2] * 1.08883f;

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
		return (rgb[0] >= WHITE_POINT && rgb[0] == rgb[1] && rgb[0] == rgb[2]);
	}

	public boolean isBlack() {
		return (rgb[0] <= BLACK_POINT && rgb[0] == rgb[1] && rgb[0] == rgb[2]);
	}

	public boolean isGrey() {
		return hsv[1] < GREY_THRESHOLD;
	}

	public Color darken(float step) {
		hsv[2] = MathUtils.clip(hsv[2] - step, 0, 1);
		return setHSV(hsv);
	}

	public Color lighten(float step) {
		hsv[2] = MathUtils.clip(hsv[2] + step, 0, 1);
		return setHSV(hsv);
	}

	public Color getDarkened(float step) {
		return new Color(this).darken(step);
	}

	public Color getLightened(float step) {
		return new Color(this).lighten(step);
	}

	public Color saturate(float step) {
		hsv[1] = MathUtils.clip(hsv[1] + step, 0, 1);
		return setHSV(hsv);
	}

	public Color desaturate(float step) {
		hsv[1] = MathUtils.clip(hsv[1] - step, 0, 1);
		return setHSV(hsv);
	}

	public Color getSaturated(float step) {
		return new Color(this).saturate(step);
	}

	public Color getDesaturated(float step) {
		return new Color(this).desaturate(step);
	}

	public Color rotateRYB(float theta) {
		return rotateRYB((int) MathUtils.degrees(theta));
	}

	public Color rotateRYB(int theta) {
		float h = hsv[0] * 360;
		theta %= 360;

		float resultHue = 0;
		for (int i = 0; i < RYB_WHEEL.length - 1; i++) {
			Vec2D p = RYB_WHEEL[i];
			Vec2D q = RYB_WHEEL[i + 1];
			if (q.y < p.y)
				q.y += 360;
			if (p.y <= h && h <= q.y) {
				resultHue = p.x + (q.x - p.x) * (h - p.y) / (q.y - p.y);
				break;
			}
		}

		// And the user-given angle (e.g. complement).
		resultHue = (resultHue + theta) % 360;

		// For the given angle, find out what hue is
		// located there on the artistic color wheel.
		for (int i = 0; i < RYB_WHEEL.length - 1; i++) {
			Vec2D p = RYB_WHEEL[i];
			Vec2D q = RYB_WHEEL[i + 1];
			if (q.y < p.y)
				q.y += 360;
			if (p.x <= resultHue && resultHue <= q.x) {
				h = p.y + (q.y - p.y) * (resultHue - p.x) / (q.x - p.x);
				break;
			}
		}

		hsv[0] = (h % 360) / 360.0f;
		return setHSV(hsv);
	}

	public Color getRotatedRYB(int angle) {
		return new Color(this).rotateRYB(angle);
	}

	public Color complement() {
		return rotateRYB(180);
	}

	public Color getComplement() {
		return new Color(this).complement();
	}

	public Color invert() {
		rgb[0] = 1 - rgb[0];
		rgb[1] = 1 - rgb[1];
		rgb[2] = 1 - rgb[2];
		return setRGB(rgb);
	}

	public Color analog(float angle, float delta) {
		rotateRYB((int) (angle * MathUtils.random(-1f, 1f)));
		hsv[1] += delta * MathUtils.random(-1f, 1f);
		hsv[2] += delta * MathUtils.random(-1f, 1f);
		return setHSV(hsv);
	}

	public Color getAnalog(float angle, float delta) {
		return new Color(this).analog(angle, delta);
	}

	public Color setSaturation(float saturation) {
		hsv[1] = MathUtils.clip(saturation, 0, 1);
		return setHSV(hsv);
	}

	public Color setBrightness(float brightness) {
		hsv[2] = MathUtils.clip(brightness, 0, 1);
		return setHSV(hsv);
	}

	public Color blend(Color c, float t) {
		rgb[0] += (c.rgb[0] - rgb[0]) * t;
		rgb[1] += (c.rgb[1] - rgb[1]) * t;
		rgb[2] += (c.rgb[2] - rgb[2]) * t;
		alpha += (c.alpha - alpha) * t;
		return setRGB(rgb);
	}

	public Color getBlended(Color c, float t) {
		return new Color(this).blend(c, t);
	}

	public float distanceTo(Color c) {
		float hue = hsv[0] * MathUtils.TWO_PI;
		float hue2 = c.hsv[0] * MathUtils.TWO_PI;
		Vec3D v1 = new Vec3D((float) Math.cos(hue) * hsv[1], (float) Math
				.sin(hue)
				* hsv[1], hsv[2]);
		Vec3D v2 = new Vec3D((float) Math.cos(hue2) * c.hsv[1], (float) Math
				.sin(hue2)
				* c.hsv[1], c.hsv[2]);
		return v1.distanceTo(v2);
	}

	public float getComponentValue(ColorAccessCriteria criteria) {
		switch (criteria.getMode()) {
		case ColorAccessCriteria.HSV_MODE:
			return hsv[criteria.getComponent()];
		case ColorAccessCriteria.RGB_MODE:
			return rgb[criteria.getComponent()];
		case ColorAccessCriteria.CMYK_MODE:
			return cmyk[criteria.getComponent()];
		case ColorAccessCriteria.ALPHA_MODE:
			return alpha;
		}
		return 0;
	}

	public void setHue(float hue) {
		hsv[0] = MathUtils.clip(hue, 0, 1);
		setHSV(hsv);
	}

	public Color adjustConstrast(float amount) {
		if (hsv[2] < 0.5)
			return darken(amount);
		else
			return lighten(amount);
	}

	public Color adjustRGB(float r, float g, float b) {
		return setRGB(new float[] { rgb[0] + r, rgb[1] + g, rgb[2] + b });
	}

	public Color adjustHSV(float h, float s, float v) {
		return setHSV(new float[] { hsv[0] + h, hsv[1] + s, hsv[2] + v });
	}

	public boolean equals(Object o) {
		if (o != null) {
			try {
				Color c = (Color) o;
				float dr = c.rgb[0] - rgb[0];
				float dg = c.rgb[1] - rgb[1];
				float db = c.rgb[2] - rgb[2];
				float da = c.alpha - alpha;
				double d = Math.sqrt(dr * dr + dg * dg + db * db + da * da);
				return d < EPS;
			} catch (ClassCastException e) {
			}
		}
		return false;
	}

	public int hashCode() {
		return (int) (rgb[0] * 1000000 + rgb[1] * 100000 + rgb[2] * 10000 + alpha * 1000);
	}
}