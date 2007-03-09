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

package toxi.util.datatypes;

public class SinCosLUT {

	// set table precision to 0.5 degrees
	public static final float SC_PRECISION = 0.5f;

	// caculate reciprocal for conversions
	public static final float SC_INV_PREC = 1 / SC_PRECISION;

	// compute required table length
	public static final int SC_PERIOD = (int) (360f * SC_INV_PREC);

	public static final float[] sinLUT = new float[SC_PERIOD];

	public static final float[] cosLUT = new float[SC_PERIOD];

	public static final float DEG_TO_RAD = (float)(Math.PI / 180.0f);

	public static final float RAD_TO_DEG = (float)(180.0f / Math.PI);

	// init sin/cos tables with values
	// should be called from setup()
	static {
		for (int i = 0; i < SC_PERIOD; i++) {
			sinLUT[i] = (float) Math.sin(i * DEG_TO_RAD * SC_PRECISION);
			cosLUT[i] = (float) Math.cos(i * DEG_TO_RAD * SC_PRECISION);
		}
	}
	
}
