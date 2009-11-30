/*
 * Copyright (c) 2006, 2007 Karsten Schmidt
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * http://creativecommons.org/licenses/LGPL/2.1/
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA
 */
package toxi.math;

import java.util.Random;

/**
 * Miscellaneous math utilities.
 */
/**
 * @author toxi
 * 
 */
public class MathUtils {

    /**
     * Log(2)
     */
    public static final float LOG2 = (float) Math.log(2);

    /**
     * PI
     */
    public static final float PI = 3.14159265358979323846f;

    /**
     * The reciprocal of PI: (1/PI)
     */
    public static final float INV_PI = 1f / PI;

    /**
     * PI/2
     */
    public static final float HALF_PI = PI / 2;

    /**
     * PI/3
     */
    public static final float THIRD_PI = PI / 3;

    /**
     * PI/4
     */
    public static final float QUARTER_PI = PI / 4;

    /**
     * PI*2
     */
    public static final float TWO_PI = PI * 2;

    /**
     * PI*1.5
     */
    public static final float THREE_HALVES_PI = TWO_PI - HALF_PI;

    /**
     * PI*PI
     */
    public static final float PI_SQUARED = PI * PI;

    /**
     * Epsilon value
     */
    public static final float EPS = 1.2e-7f; // was 1.1920928955078125E-7f;

    /**
     * Degrees to radians conversion factor
     */
    public static final float DEG2RAD = PI / 180;

    /**
     * Radians to degrees conversion factor
     */
    public static final float RAD2DEG = 180 / PI;

    private static final float SHIFT23 = 1 << 23;
    private static final float INV_SHIFT23 = 1.0f / SHIFT23;

    private final static double SIN_A = -4d / (PI * PI);
    private final static double SIN_B = 4d / PI;
    private final static double SIN_P = 9d / 40;

    /**
     * @param x
     * @return absolute value of x
     */
    public static final double abs(double x) {
        return x < 0 ? -x : x;
    }

    /**
     * @param x
     * @return absolute value of x
     */
    public static final float abs(float x) {
        return x < 0 ? -x : x;
    }

    /**
     * @param x
     * @return absolute value of x
     */
    public static final int abs(int x) {
        int y = x >> 31;
        return (x ^ y) - y;
    }

    /**
     * Rounds up the value to the nearest higher power^2 value.
     * 
     * @param x
     * @return power^2 value
     */
    public static final int ceilPowerOf2(int x) {
        int pow2 = 1;
        while (pow2 < x) {
            pow2 <<= 1;
        }
        return pow2;
    }

    public static final float clip(float a, float min, float max) {
        return a < min ? min : (a > max ? max : a);
    }

    public static final int clip(int a, int min, int max) {
        return a < min ? min : (a > max ? max : a);
    }

    /**
     * Clips the value to the 0.0 .. 1.0 interval.
     * 
     * @param a
     * @return clipped value
     * @since 0012
     */
    public static final float clipNormalized(float a) {
        if (a < 0) {
            return 0;
        } else if (a > 1) {
            return 1;
        }
        return a;
    }

    /**
     * Returns fast cosine approximation of a value. Note: code from <a
     * href="http://wiki.java.net/bin/view/Games/JeffGems">wiki posting on
     * java.net by jeffpk</a>
     * 
     * @param theta
     *            angle in radians.
     * @return cosine of theta.
     */
    public static final float cos(final float theta) {
        return sin(theta + HALF_PI);
    }

    public static final float degrees(float radians) {
        return radians * RAD2DEG;
    }

    /**
     * Fast cosine approximation.
     * 
     * @param x
     *            angle in -PI/2 .. +PI/2 interval
     * @return cosine
     */
    public static final double fastCos(final double x) {
        return fastSin(x + ((x > HALF_PI) ? -THREE_HALVES_PI : HALF_PI));
    }

    /**
     * @deprecated renamed into {@link #floor(float)}
     */
    @Deprecated
    public static final int fastFloor(float x) {
        return floor(x);
    }

    /**
     * @deprecated
     */
    @Deprecated
    public static final float fastInverseSqrt(float x) {
        float half = 0.5F * x;
        int i = Float.floatToIntBits(x);
        i = 0x5f375a86 - (i >> 1);
        x = Float.intBitsToFloat(i);
        return x * (1.5F - half * x * x);
    }

    /**
     * Computes a fast approximation to <code>Math.pow(a, b)</code>. Adapted
     * from http://www.dctsystems.co.uk/Software/power.html.
     * 
     * @param a
     *            a positive number
     * @param b
     *            a number
     * @return a^b
     * 
     */
    public static final float fastPow(float a, float b) {
        float x = Float.floatToRawIntBits(a);
        x *= INV_SHIFT23;
        x -= 127;
        float y = x - (x >= 0 ? (int) x : (int) x - 1);
        b *= x + (y - y * y) * 0.346607f;
        y = b - (b >= 0 ? (int) b : (int) b - 1);
        y = (y - y * y) * 0.33971f;
        return Float.intBitsToFloat((int) ((b + 127 - y) * SHIFT23));
    }

    /**
     * Fast sine approximation.
     * 
     * @param x
     *            angle in -PI/2 .. +PI/2 interval
     * @return sine
     */
    public static final double fastSin(double x) {
        x = SIN_A * x * abs(x) + SIN_B * x;
        return SIN_P * (x * abs(x) - x) + x;
    }

    public static final boolean flipCoin() {
        return Math.random() < 0.5;
    }

    public static final boolean flipCoin(Random rnd) {
        return rnd.nextBoolean();
    }

    public static final int floor(double x) {
        return x >= 0 ? (int) x : (int) x - 1;
    }

    /**
     * This method is a *lot* faster than using (int)Math.floor(x).
     * 
     * @param x
     *            value to be floored
     * @return floored value as integer
     * @since 0012
     */
    public static final int floor(float x) {
        return x >= 0 ? (int) x : (int) x - 1;
    }

    /**
     * Rounds down the value to the nearest lower power^2 value.
     * 
     * @param x
     * @return power^2 value
     */
    public static final int floorPowerOf2(int x) {
        return (int) Math.pow(2, (int) (Math.log(x) / LOG2));
    }

    public static final float max(float a, float b) {
        return a > b ? a : b;
    }

    /**
     * Returns the maximum value of three floats.
     * 
     * @param a
     * @param b
     * @param c
     * @return max val
     */
    public static final float max(float a, float b, float c) {
        return (a > b) ? ((a > c) ? a : c) : ((b > c) ? b : c);
    }

    public static final int max(int a, int b) {
        return a > b ? a : b;
    }

    /**
     * Returns the maximum value of three ints.
     * 
     * @param a
     * @param b
     * @param c
     * @return max val
     */
    public static final int max(int a, int b, int c) {
        return (a > b) ? ((a > c) ? a : c) : ((b > c) ? b : c);
    }

    public static final float min(float a, float b) {
        return a < b ? a : b;
    }

    /**
     * Returns the minimum value of three floats.
     * 
     * @param a
     * @param b
     * @param c
     * @return min val
     */
    public static final float min(float a, float b, float c) {
        return (a < b) ? ((a < c) ? a : c) : ((b < c) ? b : c);
    }

    public static final int min(int a, int b) {
        return a < b ? a : b;
    }

    /**
     * Returns the minimum value of three ints.
     * 
     * @param a
     * @param b
     * @param c
     * @return min val
     */
    public static final int min(int a, int b, int c) {
        return (a < b) ? ((a < c) ? a : c) : ((b < c) ? b : c);
    }

    /**
     * Returns a random number in the interval -1 .. +1.
     * 
     * @return random float
     */
    public static final float normalizedRandom() {
        return (float) Math.random() * 2 - 1;
    }

    /**
     * Returns a random number in the interval -1 .. +1 using the {@link Random}
     * instance provided.
     * 
     * @return random float
     */
    public static final float normalizedRandom(Random rnd) {
        return rnd.nextFloat() * 2 - 1;
    }

    public static final float radians(float degrees) {
        return degrees * DEG2RAD;
    }

    public static final float random(float max) {
        return (float) Math.random() * max;
    }

    public static final float random(float min, float max) {
        return (float) Math.random() * (max - min) + min;
    }

    public static final int random(int max) {
        return (int) (Math.random() * max);
    }

    public static final int random(int min, int max) {
        return (int) (Math.random() * (max - min)) + min;
    }

    public static final double random(Random rnd, double max) {
        return rnd.nextDouble() * max;
    }

    public static final double random(Random rnd, double min, double max) {
        return rnd.nextDouble() * (max - min) + min;
    }

    public static final float random(Random rnd, float max) {
        return rnd.nextFloat() * max;
    }

    public static final float random(Random rnd, float min, float max) {
        return rnd.nextFloat() * (max - min) + min;
    }

    public static final int random(Random rnd, int max) {
        return (int) (rnd.nextDouble() * max);
    }

    public static final int random(Random rnd, int min, int max) {
        return (int) (rnd.nextDouble() * (max - min)) + min;
    }

    /**
     * Reduces the given angle into the -PI/4 ... PI/4 interval. This method is
     * use by {@link #sin(float)} & {@link #cos(float)}.
     * 
     * @param theta
     *            angle in radians
     * @return reduced angle
     * @see #sin(float)
     * @see #cos(float)
     */
    public static final float reduceAngle(float theta) {
        theta %= TWO_PI;
        if (abs(theta) > PI) {
            theta = theta - TWO_PI;
        }
        if (abs(theta) > HALF_PI) {
            theta = PI - theta;
        }
        return theta;
    }

    /**
     * Returns a fast sine approximation of a value. Note: code from <a
     * href="http://wiki.java.net/bin/view/Games/JeffGems">wiki posting on
     * java.net by jeffpk</a>
     * 
     * @param theta
     *            angle in radians.
     * @return sine of theta.
     */
    public static final float sin(float theta) {
        theta = reduceAngle(theta);
        if (abs(theta) <= QUARTER_PI) {
            return (float) fastSin(theta);
        }
        return (float) fastCos(HALF_PI - theta);
    }

    /**
     * @deprecated
     */
    @Deprecated
    public static final float sqrt(float x) {
        x = fastInverseSqrt(x);
        if (x > 0) {
            return 1.0f / x;
        } else {
            return 0;
        }
    }
}
