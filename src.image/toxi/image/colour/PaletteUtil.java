/**
 * PaletteUtil is a collection of utilities for sorting and manipulating
 * colour palettes in (A)RGB format.
 *
 * @author Karsten Schmidt < i n f o [ a t ] t o x i . co . u k >
 * @version 0.1
 * 
 * Copyright (c) 2006 Karsten Schmidt
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

package toxi.image.colour;

import java.util.Arrays;
import java.util.Hashtable;

public class PaletteUtil {

	/**
	 * Sorts a given palette of packed (A)RGB integers by saturation. The alpha
	 * channel is ignored.
	 * 
	 * @param cols
	 *            array of integers in standard packed (A)RGB format
	 * @return sorted version of array with element at last index containing the
	 *         most saturated item of the palette
	 */
	public static int[] sortBySaturation(int[] cols) {
		int[] sorted = new int[cols.length];
		Hashtable ht = new Hashtable();
		for (int i = 0; i < cols.length; i++) {
			int r = (cols[i] >> 16) & 0xff;
			int g = (cols[i] >> 8) & 0xff;
			int b = cols[i] & 0xff;
			int maxComponent = max(r, g, b);
			if (maxComponent > 0) {
				sorted[i] = (int) ((maxComponent - min(r, g, b))
						/ (float) maxComponent * 0x7fffffff);
			} else
				sorted[i] = 0;
			ht.put(new Integer(sorted[i]), new Integer(cols[i]));
		}
		Arrays.sort(sorted);
		for (int i = 0; i < sorted.length; i++) {
			sorted[i] = ((Integer) ht.get(new Integer(sorted[i]))).intValue();
		}
		return sorted;
	}

	/**
	 * Sorts a given palette of packed (A)RGB integers by luminance. The alpha
	 * channel is ignored.
	 * 
	 * @param cols
	 *            array of integers in standard packed (A)RGB format
	 * @return sorted version of array with element at last index containing the
	 *         "brightest" item of the palette
	 */

	public static int[] sortByLuminance(int[] cols) {
		int[] sorted = new int[cols.length];
		Hashtable ht = new Hashtable();
		for (int i = 0; i < cols.length; i++) {
			// luminance = 0.3*red + 0.59*green + 0.11*blue
			// same equation in fixed point math...
			sorted[i] = (77 * (cols[i] >> 16 & 0xff) + 151
					* (cols[i] >> 8 & 0xff) + 28 * (cols[i] & 0xff));
			ht.put(new Integer(sorted[i]), new Integer(cols[i]));
		}
		Arrays.sort(sorted);
		for (int i = 0; i < sorted.length; i++) {
			sorted[i] = ((Integer) ht.get(new Integer(sorted[i]))).intValue();
		}
		return sorted;
	}

	/**
	 * Sorts a given palette of packed (A)RGB integers by proximity to a colour.
	 * The alpha channel is ignored.
	 * 
	 * @param cols
	 *            array of integers in standard packed (A)RGB format
	 * @param basecol
	 *            colour to which proximity of all palette items is calculated
	 * @return sorted version of array with element at first index containing
	 *         the "closest" item of the palette
	 */

	public static int[] sortByProximity(int[] cols, int basecol) {
		int[] sorted = new int[cols.length];
		Hashtable ht = new Hashtable();
		int br = (basecol >> 16) & 0xff;
		int bg = (basecol >> 8) & 0xff;
		int bb = basecol & 0xff;
		for (int i = 0; i < cols.length; i++) {
			int r = (cols[i] >> 16) & 0xff;
			int g = (cols[i] >> 8) & 0xff;
			int b = cols[i] & 0xff;
			sorted[i] = (br - r) * (br - r) + (bg - g) * (bg - g) + (bb - b)
					* (bb - b);
			ht.put(new Integer(sorted[i]), new Integer(cols[i]));
		}
		Arrays.sort(sorted);
		for (int i = 0; i < sorted.length; i++) {
			sorted[i] = ((Integer) ht.get(new Integer(sorted[i]))).intValue();
		}
		return sorted;
	}

	/**
	 * Minimum of three
	 * 
	 * @param a
	 * @param b
	 * @param c
	 * @return the smallest of 3 numbers
	 */
	private static final int min(int a, int b, int c) {
		return (a < b) ? ((a < c) ? a : c) : ((b < c) ? b : c);
	}

	/**
	 * Maximum of three
	 * 
	 * @param a
	 * @param b
	 * @param c
	 * @return the largest of 3 numbers
	 */
	private static final int max(int a, int b, int c) {
		return (a > b) ? ((a > c) ? a : c) : ((b > c) ? b : c);
	}
}