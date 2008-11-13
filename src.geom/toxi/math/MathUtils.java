/* 
 * Copyright (c) 2006, 2007 Karsten Schmidt
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
package toxi.math;

import java.util.Random;

public class MathUtils {

	public static final float LOG2 = (float) Math.log(2);

	public static final float PI = 3.14159265358979323846f;

	public static final float HALF_PI = PI / 2;

	public static final float THIRD_PI = PI / 3;

	public static final float QUARTER_PI = PI / 4;

	public static final float TWO_PI = PI * 2;

	public static final float EPS = 1.1920928955078125E-7f;

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

	public static final float random(Random rnd, float max) {
		return (float) rnd.nextDouble() * max;
	}

	public static final float random(Random rnd, float min, float max) {
		return (float) rnd.nextDouble() * (max - min) + min;
	}

	public static final int random(Random rnd, int max) {
		return (int) (rnd.nextDouble() * max);
	}

	public static final int random(Random rnd, int min, int max) {
		return (int) (rnd.nextDouble() * (max - min)) + min;
	}

	public static final int abs(int x) {
		int y = x >> 31;
		return (x ^ y) - y;
	}

	public static final float abs(float x) {
		return x < 0 ? -x : x;
	}

	public static final float fastInverseSqrt(float x) {
		float half = 0.5F * x;
		int i = Float.floatToIntBits(x);
		i = 0x5f375a86 - (i >> 1);
		x = Float.intBitsToFloat(i);
		return x * (1.5F - half * x * x);
	}

	public static final float sqrt(float x) {
		x = fastInverseSqrt(x);
		if (x != 0.0f) {
			return 1.0f / x;
		} else
			return 0;
	}

	/**
	 * Computes a fast approximation to <code>Math.pow(a, b)</code>. Adapted
	 * from <url>http://www.dctsystems.co.uk/Software/power.html</url>.
	 * 
	 * @param a
	 *            a positive number
	 * @param b
	 *            a number
	 * @return a^b
	 */
	public static final float fastPow(float a, float b) {
		float x = Float.floatToRawIntBits(a);
		x *= 1.0f / (1 << 23);
		x = x - 127;
		float y = x - (int) Math.floor(x);
		b *= x + (y - y * y) * 0.346607f;
		y = b - (int) Math.floor(b);
		y = (y - y * y) * 0.33971f;
		return Float.intBitsToFloat((int) ((b + 127 - y) * (1 << 23)));
	}

	public static final int min(int a, int b) {
		return a < b ? a : b;
	}

	public static final int max(int a, int b) {
		return a > b ? a : b;
	}

	public static final float min(float a, float b) {
		return a < b ? a : b;
	}

	public static final float max(float a, float b) {
		return a > b ? a : b;
	}

	public static final float clip(float a, float min, float max) {
		return a < min ? min : (a > max ? max : a);
	}

	public static final int clip(int a, int min, int max) {
		return a < min ? min : (a > max ? max : a);
	}

	public static final float clipNormalized(float a) {
		if (a < 0)
			return 0;
		if (a > 1)
			return 1;
		return a;
	}

	/**
	 * This method is a *lot* faster than using (int)Math.floor(x).
	 * 
	 * @param x
	 *            value to be floored
	 * @return floored value as integer
	 */
	public static final int fastFloor(float x) {
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

	/**
	 * Rounds up the value to the nearest higher power^2 value.
	 * 
	 * @param x
	 * @return power^2 value
	 */
	public static final int ceilPowerOf2(int x) {
		return (int) Math.pow(2, (int) (Math.log(x) / LOG2 + 0.5));
	}

	public static final int min(int a, int b, int c) {
		return (a < b) ? ((a < c) ? a : c) : ((b < c) ? b : c);
	}

	public static final float min(float a, float b, float c) {
		return (a < b) ? ((a < c) ? a : c) : ((b < c) ? b : c);
	}

	public static final int max(int a, int b, int c) {
		return (a > b) ? ((a > c) ? a : c) : ((b > c) ? b : c);
	}

	public static final float max(float a, float b, float c) {
		return (a > b) ? ((a > c) ? a : c) : ((b > c) ? b : c);
	}
}
