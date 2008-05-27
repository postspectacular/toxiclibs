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

/**
 * Lookup table for fast sine & cosine computations. The table currently has a
 * fixed precision of 0.25 degrees to which input angles will be rounded to. All
 * methods are static and can be used with both positive and negative input
 * angles.
 */
public class SinCosLUT {

	/**
	 * set table precision to 0.25 degrees
	 */
	public static final float SC_PRECISION = 0.25f;

	/**
	 * calculate reciprocal for conversions
	 */
	public static final float SC_INV_PREC = 1.0f / SC_PRECISION;

	/**
	 * compute required table length
	 */
	public static final int SC_PERIOD = (int) (360f * SC_INV_PREC);

	/**
	 * LUT for sine values
	 */
	public static final float[] sinLUT = new float[SC_PERIOD];

	/**
	 * LUT for cosine values
	 */
	public static final float[] cosLUT = new float[SC_PERIOD];

	/**
	 * Pre-multiplied degrees -> radians
	 */
	private static final float DEG_TO_RAD = (float) (Math.PI / 180.0)
			* SC_PRECISION;

	/**
	 * Pre-multiplied radians - degrees
	 */
	private static final float RAD_TO_DEG = (float) (180.0 / Math.PI)
			/ SC_PRECISION;

	// init sin/cos tables with values
	static {
		for (int i = 0; i < SC_PERIOD; i++) {
			sinLUT[i] = (float) Math.sin(i * DEG_TO_RAD);
			cosLUT[i] = (float) Math.cos(i * DEG_TO_RAD);
		}
	}

	/**
	 * Calculates sine for the passed angle in radians.
	 * 
	 * @param theta
	 * @return sine value for theta
	 */
	public static final float sin(float theta) {
		while (theta < 0) {
			theta += MathUtils.TWO_PI;
		}
		return sinLUT[(int) (theta * RAD_TO_DEG) % SC_PERIOD];
	}

	/**
	 * Calculate cosine for the passed in angle in radians.
	 * 
	 * @param theta
	 * @return cosine value for theta
	 */
	public static final float cos(float theta) {
		while (theta < 0) {
			theta += MathUtils.TWO_PI;
		}
		return cosLUT[(int) (theta * RAD_TO_DEG) % SC_PERIOD];
	}
}
