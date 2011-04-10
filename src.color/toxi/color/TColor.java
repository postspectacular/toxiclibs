/*
 * Some classes in this package have been partly inspired by & bits ported from
 * Python code written by Tom De Smedt & Frederik De Bleser for the "colors" library
 * of Nodebox.net.
 * 
 * http://nodebox.net/code/index.php/Colors
 *
 * Copyright (c) 2006-2011 Karsten Schmidt
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
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301, USA
 */

package toxi.color;

import toxi.geom.ReadonlyVec3D;
import toxi.geom.Vec2D;
import toxi.geom.Vec3D;
import toxi.math.MathUtils;

/**
 * Floating point color class with implicit RGB, HSV, CMYK access modes,
 * conversion and color theory utils. Based on the <a href="">Colors library</a>
 * for <a href="http:/Nodebox.net/">NodeBox</a>
 * 
 */
public class TColor implements ReadonlyTColor {

    protected static final float INV60DEGREES = 60.0f / 360;
    protected static final float INV8BIT = 1f / 255;
    protected static final double EPS = .001;

    protected static final Vec2D[] RYB_WHEEL = new Vec2D[] {
            new Vec2D(0, 0), new Vec2D(15, 8), new Vec2D(30, 17),
            new Vec2D(45, 26), new Vec2D(60, 34), new Vec2D(75, 41),
            new Vec2D(90, 48), new Vec2D(105, 54), new Vec2D(120, 60),
            new Vec2D(135, 81), new Vec2D(150, 103), new Vec2D(165, 123),
            new Vec2D(180, 138), new Vec2D(195, 155), new Vec2D(210, 171),
            new Vec2D(225, 187), new Vec2D(240, 204), new Vec2D(255, 219),
            new Vec2D(270, 234), new Vec2D(285, 251), new Vec2D(300, 267),
            new Vec2D(315, 282), new Vec2D(330, 298), new Vec2D(345, 329),
            new Vec2D(360, 0)
    };

    /**
     * Maximum rgb component value for a color to be classified as black.
     * 
     * @see #isBlack()
     */
    public static float BLACK_POINT = 0.08f;

    /**
     * Minimum rgb component value for a color to be classified as white.
     * 
     * @see #isWhite()
     */
    public static float WHITE_POINT = 1f;

    /**
     * Maximum saturations value for a color to be classified as grey
     * 
     * @see #isGrey()
     */
    public static float GREY_THRESHOLD = 0.01f;

    public static final ReadonlyTColor RED = newRGB(1, 0, 0);
    public static final ReadonlyTColor GREEN = newRGB(0, 1, 0);
    public static final ReadonlyTColor BLUE = newRGB(0, 0, 1);
    public static final ReadonlyTColor CYAN = newRGB(0, 1, 1);
    public static final ReadonlyTColor MAGENTA = newRGB(1, 0, 1);
    public static final ReadonlyTColor YELLOW = newRGB(1, 1, 0);
    public static final ReadonlyTColor BLACK = newRGB(0, 0, 0);
    public static final ReadonlyTColor WHITE = newRGB(1, 1, 1);

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
            if (hexRGB.length() > 6) {
                hexRGB = hexRGB.substring(hexRGB.length() - 6);
            }
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
     * First we have to convert to XYZ color space. Conversion involves using a
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
     * Factory method. Creates new color from ARGB int.
     * 
     * @param argb
     * @return new color
     */
    public static final TColor newARGB(int argb) {
        return newRGBA(((argb >> 16) & 0xff) * INV8BIT, ((argb >> 8) & 0xff)
                * INV8BIT, (argb & 0xff) * INV8BIT, (argb >>> 24) * INV8BIT);
    }

    /**
     * Factory method. Creates new color from CMYK values.
     * 
     * @param c
     * @param m
     * @param y
     * @param k
     * @return new color
     */
    public static final TColor newCMYK(float c, float m, float y, float k) {
        return newCMYKA(c, m, y, k, 1);
    }

    /**
     * Factory method. Creates new color from CMYK + alpha values.
     * 
     * @param c
     * @param m
     * @param y
     * @param k
     * @param a
     * @return new color
     */
    public static final TColor newCMYKA(float c, float m, float y, float k,
            float a) {
        TColor col = new TColor();
        col.setCMYK(new float[] {
                c, m, y, k
        });
        col.alpha = MathUtils.clip(a, 0, 1);
        return col;
    }

    /**
     * Factory method. Creates a new shade of gray with alpha set to 100%.
     * 
     * @param gray
     * @return new color.
     */
    public static final TColor newGray(float gray) {
        return newGrayAlpha(gray, 1);
    }

    /**
     * Factory method. Creates a new shade of gray + alpha.
     * 
     * @param gray
     * @param alpha
     * @return new color.
     */
    public static final TColor newGrayAlpha(float gray, float alpha) {
        TColor c = new TColor();
        c.setRGB(new float[] {
                gray, gray, gray
        });
        c.alpha = alpha;
        return c;
    }

    /**
     * Factory method. Creates a new color from hex string. Supports standard 6
     * digit RGB colors or 8 digit ARGB.
     * 
     * @param hexRGB
     * @return new color
     */
    public static final TColor newHex(String hexRGB) {
        TColor c = new TColor();
        if (hexRGB.length() <= 6) {
            c.setRGB(hexToRGB(hexRGB));
            c.alpha = 1;
        } else if (hexRGB.length() == 8) {
            c.setRGB(hexToRGB(hexRGB));
            c.setAlpha(Integer.parseInt(hexRGB.substring(0, 2), 16) * INV8BIT);
        }
        return c;
    }

    /**
     * Factory method. New color from hsv values.
     * 
     * @param h
     * @param s
     * @param v
     * @return new color
     */
    public static final TColor newHSV(float h, float s, float v) {
        return newHSVA(h, s, v, 1);
    }

    public static ReadonlyTColor newHSV(Hue h, float s, float v) {
        return newHSV(h.getHue(), s, v);
    }

    public static final TColor newHSVA(float h, float s, float v, float a) {
        TColor c = new TColor();
        c.setHSV(h, s, v);
        c.alpha = MathUtils.clip(a, 0, 1);
        return c;
    }

    /**
     * Factory method. Creates new random color. Alpha is always 1.0.
     * 
     * @return random color
     */
    public static final TColor newRandom() {
        return newRGBA(MathUtils.random(1f), MathUtils.random(1f),
                MathUtils.random(1f), 1);
    }

    /**
     * Factory method. Creates new color from RGB values. Alpha is set to 1.0.
     * 
     * @param r
     * @param g
     * @param b
     * @return new color
     */
    public static final TColor newRGB(float r, float g, float b) {
        return newRGBA(r, g, b, 1);
    }

    public static final TColor newRGBA(float r, float g, float b, float a) {
        TColor c = new TColor();
        c.setRGB(new float[] {
                r, g, b
        });
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
        String hex = Integer
                .toHexString((int) (MathUtils.clip(r, 0, 1) * 0xff))
                + Integer.toHexString((int) (MathUtils.clip(g, 0, 1) * 0xff))
                + Integer.toHexString((int) (MathUtils.clip(b, 0, 1) * 0xff));
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

    protected float[] rgb;
    protected float[] cmyk;
    protected float[] hsv;
    public float alpha;

    /**
     * Only for internal use. Color instances should be created with factory
     * methods.
     */
    protected TColor() {
        rgb = new float[3];
        hsv = new float[3];
        cmyk = new float[4];
    }

    /**
     * Creates a deep copy of the given color.
     * 
     * @param c
     */
    public TColor(ReadonlyTColor c) {
        this();
        float[] buffer = c.toCMYKAArray(null);
        cmyk[0] = buffer[0];
        cmyk[1] = buffer[1];
        cmyk[2] = buffer[2];
        cmyk[3] = buffer[3];
        c.toHSVAArray(buffer);
        hsv[0] = buffer[0];
        hsv[1] = buffer[1];
        hsv[2] = buffer[2];
        c.toRGBAArray(buffer);
        rgb[0] = buffer[0];
        rgb[1] = buffer[1];
        rgb[2] = buffer[2];
        this.alpha = c.alpha();
    }

    public TColor add(TColor c) {
        return copy().addSelf(c);
    }

    public TColor addSelf(TColor c) {
        rgb[0] = MathUtils.min(rgb[0] + c.rgb[0], 1);
        rgb[1] = MathUtils.min(rgb[1] + c.rgb[1], 1);
        rgb[2] = MathUtils.min(rgb[2] + c.rgb[2], 1);
        return setRGB(rgb);
    }

    /**
     * Changes the brightness of the color by the given amount in the direction
     * towards either the black or white point (depending on if current
     * brightness >= 50%)
     * 
     * @param amount
     * @return itself
     */
    public TColor adjustConstrast(float amount) {
        return hsv[2] < 0.5 ? darken(amount) : lighten(amount);
    }

    /**
     * Adds the given HSV values as offsets to the current color. Hue will
     * automatically wrap.
     * 
     * @param h
     * @param s
     * @param v
     * @return itself
     */
    public TColor adjustHSV(float h, float s, float v) {
        return setHSV(new float[] {
                hsv[0] + h, hsv[1] + s, hsv[2] + v
        });
    }

    /**
     * Adds the given RGB values as offsets to the current color. TColor will
     * clip at black or white.
     * 
     * @param r
     * @param g
     * @param b
     * @return itself
     */
    public TColor adjustRGB(float r, float g, float b) {
        return setRGB(new float[] {
                rgb[0] + r, rgb[1] + g, rgb[2] + b
        });
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.color.ReadonlyTColor#alpha()
     */
    public float alpha() {
        return alpha;
    }

    /**
     * Rotates this color by a random amount (not exceeding the one specified)
     * and creates variations in saturation and brightness based on the 2nd
     * parameter.
     * 
     * @param theta
     *            max. rotation angle (in radians)
     * @param delta
     *            max. sat/bri variance
     * @return itself
     */
    public TColor analog(float theta, float delta) {
        return analog((int) MathUtils.degrees(theta), delta);
    }

    public TColor analog(int angle, float delta) {
        rotateRYB((int) (angle * MathUtils.normalizedRandom()));
        hsv[1] += delta * MathUtils.normalizedRandom();
        hsv[2] += delta * MathUtils.normalizedRandom();
        return setHSV(hsv);
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.color.ReadonlyTColor#black()
     */
    public float black() {
        return cmyk[3];
    }

    /**
     * Blends the color with the given one by the stated amount
     * 
     * @param c
     *            target color
     * @param t
     *            interpolation factor
     * @return itself
     */
    public TColor blend(ReadonlyTColor c, float t) {
        float[] crgb = c.toRGBAArray(null);
        rgb[0] += (crgb[0] - rgb[0]) * t;
        rgb[1] += (crgb[1] - rgb[1]) * t;
        rgb[2] += (crgb[2] - rgb[2]) * t;
        alpha += (c.alpha() - alpha) * t;
        return setRGB(rgb);
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.color.ReadonlyTColor#blue()
     */
    public float blue() {
        return rgb[2];
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.color.ReadonlyTColor#brightness()
     */
    public float brightness() {
        return hsv[2];
    }

    /**
     * @return ifself, as complementary color
     */
    public TColor complement() {
        return rotateRYB(180);
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.color.ReadonlyTColor#copy()
     */
    public TColor copy() {
        return new TColor(this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.color.ReadonlyTColor#cyan()
     */
    public float cyan() {
        return cmyk[0];
    }

    /**
     * Reduces the color's brightness by the given amount (e.g 0.1 = 10%
     * darker).
     * 
     * @param step
     * @return itself
     */
    public TColor darken(float step) {
        return setBrightness(hsv[2] - step);
    }

    /**
     * Reduced the color's saturation by the given amount.
     * 
     * @param step
     * @return itself
     */
    public TColor desaturate(float step) {
        return setSaturation(hsv[1] - step);
    }

    public TColor differenceTo(TColor c) {
        return TColor.newRGB(MathUtils.abs(rgb[0] - c.rgb[0]),
                MathUtils.abs(rgb[1] - c.rgb[1]),
                MathUtils.abs(rgb[2] - c.rgb[2]));
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.color.ReadonlyTColor#distanceToCMYK(toxi.color.TColor)
     */
    public float distanceToCMYK(ReadonlyTColor c) {
        float[] ccmyk = c.toCMYKAArray(null);
        float dc = cmyk[0] - ccmyk[0];
        float dm = cmyk[1] - ccmyk[1];
        float dy = cmyk[2] - ccmyk[2];
        float dk = cmyk[3] - ccmyk[3];
        return (float) Math.sqrt(dc * dc + dm * dm + dy * dy + dk * dk);
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.color.ReadonlyTColor#distanceToHSV(toxi.color.TColor)
     */
    public float distanceToHSV(ReadonlyTColor c) {
        float hue = hsv[0] * MathUtils.TWO_PI;
        float hue2 = c.hue() * MathUtils.TWO_PI;
        ReadonlyVec3D v1 = new Vec3D((MathUtils.cos(hue) * hsv[1]),
                (MathUtils.sin(hue) * hsv[1]), hsv[2]);
        Vec3D v2 = new Vec3D((MathUtils.cos(hue2) * c.saturation()),
                (MathUtils.sin(hue2) * c.saturation()), c.brightness());
        return v1.distanceTo(v2);
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.color.ReadonlyTColor#distanceToRGB(toxi.color.TColor)
     */
    public float distanceToRGB(ReadonlyTColor c) {
        float[] crgb = c.toRGBAArray(null);
        float dr = rgb[0] - crgb[0];
        float dg = rgb[1] - crgb[1];
        float db = rgb[2] - crgb[2];
        return (float) Math.sqrt(dr * dr + dg * dg + db * db);
    }

    @Override
    public boolean equals(Object o) {
        if (o != null && o instanceof TColor) {
            TColor c = (TColor) o;
            float dr = c.rgb[0] - rgb[0];
            float dg = c.rgb[1] - rgb[1];
            float db = c.rgb[2] - rgb[2];
            float da = c.alpha - alpha;
            double d = Math.sqrt(dr * dr + dg * dg + db * db + da * da);
            return d < EPS;
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.color.ReadonlyTColor#getAnalog(float, float)
     */
    public TColor getAnalog(float theta, float delta) {
        return new TColor(this).analog(theta, delta);
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.color.ReadonlyTColor#getAnalog(int, float)
     */
    public TColor getAnalog(int angle, float delta) {
        return new TColor(this).analog(angle, delta);
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.color.ReadonlyTColor#getBlended(toxi.color.ReadonlyTColor,
     * float)
     */
    public TColor getBlended(ReadonlyTColor c, float t) {
        return new TColor(this).blend(c, t);
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.color.ReadonlyTColor#getClosestHue()
     */
    public Hue getClosestHue() {
        return Hue.getClosest(hsv[0], false);
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.color.ReadonlyTColor#getClosestHue(boolean)
     */
    public Hue getClosestHue(boolean primaryOnly) {
        return Hue.getClosest(hsv[0], primaryOnly);
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.color.ReadonlyTColor#getComplement()
     */
    public TColor getComplement() {
        return new TColor(this).complement();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * toxi.color.ReadonlyTColor#getComponentValue(toxi.color.AccessCriteria)
     */
    public float getComponentValue(AccessCriteria criteria) {
        return criteria.getComponentValueFor(this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.color.ReadonlyTColor#getDarkened(float)
     */
    public TColor getDarkened(float step) {
        return new TColor(this).darken(step);
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.color.ReadonlyTColor#getDesaturated(float)
     */
    public TColor getDesaturated(float step) {
        return new TColor(this).desaturate(step);
    }

    public TColor getDifferenceTo(TColor c) {
        return copy().differenceTo(c);
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.color.ReadonlyTColor#getInverted()
     */
    public TColor getInverted() {
        return new TColor(this).invert();
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.color.ReadonlyTColor#getLightened(float)
     */
    public TColor getLightened(float step) {
        return new TColor(this).lighten(step);
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.color.ReadonlyTColor#getRotatedRYB(float)
     */
    public TColor getRotatedRYB(float theta) {
        return new TColor(this).rotateRYB(theta);
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.color.ReadonlyTColor#getRotatedRYB(int)
     */
    public TColor getRotatedRYB(int angle) {
        return new TColor(this).rotateRYB(angle);
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.color.ReadonlyTColor#getSaturated(float)
     */
    public TColor getSaturated(float step) {
        return new TColor(this).saturate(step);
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.color.ReadonlyTColor#green()
     */
    public float green() {
        return rgb[1];
    }

    @Override
    public int hashCode() {
        int hash = 37;
        hash += 37 * hash + Float.floatToIntBits(rgb[0]);
        hash += 37 * hash + Float.floatToIntBits(rgb[1]);
        hash += 37 * hash + Float.floatToIntBits(rgb[2]);
        hash += 37 * hash + Float.floatToIntBits(alpha);
        return hash;
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.color.ReadonlyTColor#hue()
     */
    public float hue() {
        return hsv[0];
    }

    /**
     * Inverts the color.
     * 
     * @return itself
     */
    public TColor invert() {
        rgb[0] = 1 - rgb[0];
        rgb[1] = 1 - rgb[1];
        rgb[2] = 1 - rgb[2];
        return setRGB(rgb);
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.color.ReadonlyTColor#isBlack()
     */
    public boolean isBlack() {
        return (rgb[0] <= BLACK_POINT && Float.compare(rgb[0], rgb[1]) == 0 && Float
                .compare(rgb[0], rgb[2]) == 0);
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.color.ReadonlyTColor#isGrey()
     */
    public boolean isGrey() {
        return hsv[1] < GREY_THRESHOLD;
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.color.ReadonlyTColor#isPrimary()
     */
    public boolean isPrimary() {
        return Hue.isPrimary(hsv[0]);
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.color.ReadonlyTColor#isWhite()
     */
    public boolean isWhite() {
        return (rgb[0] >= WHITE_POINT && Float.compare(rgb[0], rgb[1]) == 0 && Float
                .compare(rgb[0], rgb[2]) == 0);
    }

    /**
     * Lightens the color by stated amount.
     * 
     * @param step
     *            lighten amount
     * @return itself
     */
    public TColor lighten(float step) {
        return setBrightness(hsv[2] + step);
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.color.ReadonlyTColor#luminance()
     */
    public float luminance() {
        return rgb[0] * 0.299f + rgb[1] * 0.587f + rgb[2] * 0.114f;
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.color.ReadonlyTColor#magenta()
     */
    public float magenta() {
        return cmyk[1];
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.color.ReadonlyTColor#red()
     */
    public float red() {
        return rgb[0];
    }

    public TColor rotateRYB(float theta) {
        return rotateRYB((int) MathUtils.degrees(theta));
    }

    /**
     * Rotates the color by x degrees along the <a
     * href="http://en.wikipedia.org/wiki/RYB_color_model">RYB color wheel</a>
     * 
     * @param theta
     * @return itself
     */
    public TColor rotateRYB(int theta) {
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
        // located there on the artistic color wheel.
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

    /**
     * Adds the given value to the current saturation component.
     * 
     * @param step
     * @return itself
     */
    public TColor saturate(float step) {
        return setSaturation(hsv[1] + step);
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.color.ReadonlyTColor#saturation()
     */
    public float saturation() {
        return hsv[1];
    }

    public TColor setAlpha(float alpha) {
        this.alpha = alpha;
        return this;
    }

    /**
     * Sets all color components to new values interpreted from the given packed
     * ARGB 32bit color value.
     * 
     * @param argb
     * @return itself
     */
    public TColor setARGB(int argb) {
        setRGB(((argb >> 16) & 0xff) * INV8BIT, ((argb >> 8) & 0xff) * INV8BIT,
                (argb & 0xff) * INV8BIT);
        alpha = (argb >>> 24) * INV8BIT;
        return this;
    }

    public TColor setBlack(float val) {
        cmyk[3] = MathUtils.clip(val, 0f, 1f);
        cmykToRGB(cmyk[0], cmyk[1], cmyk[2], cmyk[3], rgb);
        rgbToHSV(rgb[0], rgb[1], rgb[2], hsv);
        return this;
    }

    public TColor setBlue(float blue) {
        rgb[2] = MathUtils.clip(blue, 0f, 1f);
        rgbToCMYK(rgb[0], rgb[1], rgb[2], cmyk);
        rgbToHSV(rgb[0], rgb[1], rgb[2], hsv);
        return this;
    }

    public TColor setBrightness(float brightness) {
        hsv[2] = MathUtils.clip(brightness, 0, 1);
        hsvToRGB(hsv[0], hsv[1], hsv[2], rgb);
        rgbToCMYK(rgb[0], rgb[1], rgb[2], cmyk);
        return this;
    }

    public TColor setCMYK(float c, float m, float y, float k) {
        cmyk[0] = c;
        cmyk[1] = m;
        cmyk[2] = y;
        cmyk[3] = k;
        return setCMYK(cmyk);
    }

    public TColor setCMYK(float[] newCMYK) {
        cmyk[0] = MathUtils.clip(newCMYK[0], 0, 1);
        cmyk[1] = MathUtils.clip(newCMYK[1], 0, 1);
        cmyk[2] = MathUtils.clip(newCMYK[2], 0, 1);
        cmyk[3] = MathUtils.clip(newCMYK[3], 0, 1);
        cmykToRGB(cmyk[0], cmyk[1], cmyk[2], cmyk[3], rgb);
        rgbToHSV(rgb[0], rgb[1], rgb[2], hsv);
        return this;
    }

    /**
     * Sets a color component by name.
     * 
     * @param criteria
     * @param val
     * @return itself
     * 
     * @see AccessCriteria
     */
    public TColor setComponent(AccessCriteria criteria, float val) {
        criteria.setComponentValueFor(this, val);
        return this;
    }

    public TColor setCyan(float val) {
        cmyk[0] = MathUtils.clip(val, 0f, 1f);
        cmykToRGB(cmyk[0], cmyk[1], cmyk[2], cmyk[3], rgb);
        rgbToHSV(rgb[0], rgb[1], rgb[2], hsv);
        return this;
    }

    public TColor setGreen(float green) {
        rgb[1] = MathUtils.clip(green, 0f, 1f);
        rgbToCMYK(rgb[0], rgb[1], rgb[2], cmyk);
        rgbToHSV(rgb[0], rgb[1], rgb[2], hsv);
        return this;
    }

    public TColor setHSV(float h, float s, float v) {
        hsv[0] = h;
        hsv[1] = s;
        hsv[2] = v;
        return setHSV(hsv);
    }

    public TColor setHSV(float[] newHSV) {
        hsv[0] = newHSV[0] % 1;
        if (hsv[0] < 0) {
            hsv[0]++;
        }
        hsv[1] = MathUtils.clip(newHSV[1], 0, 1);
        hsv[2] = MathUtils.clip(newHSV[2], 0, 1);
        hsvToRGB(hsv[0], hsv[1], hsv[2], rgb);
        rgbToCMYK(rgb[0], rgb[1], rgb[2], cmyk);
        return this;
    }

    public TColor setHue(float hue) {
        hue %= 1.0;
        if (hue < 0.0) {
            hue++;
        }
        hsv[0] = hue;
        hsvToRGB(hsv[0], hsv[1], hsv[2], rgb);
        rgbToCMYK(rgb[0], rgb[1], rgb[2], cmyk);
        return this;
    }

    public TColor setMagenta(float val) {
        cmyk[1] = MathUtils.clip(val, 0f, 1f);
        cmykToRGB(cmyk[0], cmyk[1], cmyk[2], cmyk[3], rgb);
        rgbToHSV(rgb[0], rgb[1], rgb[2], hsv);
        return this;
    }

    public TColor setRed(float red) {
        rgb[0] = MathUtils.clip(red, 0f, 1f);
        rgbToCMYK(rgb[0], rgb[1], rgb[2], cmyk);
        rgbToHSV(rgb[0], rgb[1], rgb[2], hsv);
        return this;
    }

    public TColor setRGB(float r, float g, float b) {
        rgb[0] = r;
        rgb[1] = g;
        rgb[2] = b;
        return setRGB(rgb);
    }

    public TColor setRGB(float[] newRGB) {
        rgb[0] = MathUtils.clip(newRGB[0], 0, 1);
        rgb[1] = MathUtils.clip(newRGB[1], 0, 1);
        rgb[2] = MathUtils.clip(newRGB[2], 0, 1);
        rgbToCMYK(rgb[0], rgb[1], rgb[2], cmyk);
        rgbToHSV(rgb[0], rgb[1], rgb[2], hsv);
        return this;
    }

    public TColor setSaturation(float saturation) {
        hsv[1] = MathUtils.clip(saturation, 0, 1);
        hsvToRGB(hsv[0], hsv[1], hsv[2], rgb);
        rgbToCMYK(rgb[0], rgb[1], rgb[2], cmyk);
        return this;
    }

    public TColor setYellow(float val) {
        cmyk[2] = MathUtils.clip(val, 0f, 1f);
        cmykToRGB(cmyk[0], cmyk[1], cmyk[2], cmyk[3], rgb);
        rgbToHSV(rgb[0], rgb[1], rgb[2], hsv);
        return this;
    }

    public TColor sub(TColor c) {
        return copy().subSelf(c);
    }

    public TColor subSelf(TColor c) {
        rgb[0] = MathUtils.max(rgb[0] - c.rgb[0], 0);
        rgb[1] = MathUtils.max(rgb[1] - c.rgb[1], 0);
        rgb[2] = MathUtils.max(rgb[2] - c.rgb[2], 0);
        return setRGB(rgb);
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.color.ReadonlyTColor#toARGB()
     */
    public int toARGB() {
        return (int) (rgb[0] * 255) << 16 | (int) (rgb[1] * 255) << 8
                | (int) (rgb[2] * 255) | (int) (alpha * 255) << 24;
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.color.ReadonlyTColor#toCMYKAArray(float[])
     */
    public float[] toCMYKAArray(float[] cmyka) {
        if (cmyka == null || cmyka.length < 5) {
            cmyka = new float[5];
        }
        cmyka[0] = cmyk[0];
        cmyka[1] = cmyk[1];
        cmyka[2] = cmyk[2];
        cmyka[3] = cmyk[3];
        cmyka[4] = alpha;
        return cmyka;
    }

    /**
     * Converts the color into a 6 digit hex string.
     * 
     * @return color as hex
     */
    public String toHex() {
        String hex = Integer.toHexString(toARGB());
        if (hex.length() > 6) {
            hex = hex.substring(2);
        }
        return hex;
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.color.ReadonlyTColor#toHSVAArray(float[])
     */
    public float[] toHSVAArray(float[] hsva) {
        if (hsva == null || hsva.length < 4) {
            hsva = new float[4];
        }
        hsva[0] = hsv[0];
        hsva[1] = hsv[1];
        hsva[2] = hsv[2];
        hsva[3] = alpha;
        return hsva;
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.color.ReadonlyTColor#toRGBAArray(float[])
     */
    public float[] toRGBAArray(float[] rgba) {
        return toRGBAArray(rgba, 0);
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.color.ReadonlyTColor#toRGBAArray(float[], int)
     */
    public float[] toRGBAArray(float[] rgba, int offset) {
        if (rgba == null || rgba.length < offset + 4) {
            rgba = new float[4];
            offset = 0;
        }
        rgba[offset++] = rgb[0];
        rgba[offset++] = rgb[1];
        rgba[offset++] = rgb[2];
        rgba[offset] = alpha;
        return rgba;
    }

    @Override
    public String toString() {
        return "TColor: rgb: " + rgb[0] + "," + rgb[1] + "," + rgb[2]
                + " hsv: " + hsv[0] + "," + hsv[1] + "," + hsv[2] + " cmyk: "
                + cmyk[0] + "," + cmyk[1] + "," + cmyk[2] + "," + cmyk[3]
                + " alpha: " + alpha;
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.color.ReadonlyTColor#yellow()
     */
    public float yellow() {
        return cmyk[2];
    }
}