/* 
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

import toxi.math.MathUtils;

/**
 * A colour palette made up of a set of base colours and a second one defining
 * accent colours
 */
public class AccentPalette {

	Palette baseCols;
	Palette accentCols;

	float baseAccentRatio;
	
	/**
	 * @param baseCols
	 * @param accentCols
	 * @param ratio
	 */
	public AccentPalette(Palette baseCols, Palette accentCols, float ratio) {
		this.baseCols = baseCols;
		this.accentCols = accentCols;
		baseAccentRatio=ratio;
	}

	public int pickRandomColour() {
		if (MathUtils.random(1f)<baseAccentRatio) {
			return baseCols.pickRandomColour();
		} else {
			return accentCols.pickRandomColour();
		}
	}
	
	/**
	 * @return ARGB value of last colour picked
	 */
	public int getLastPickedBaseColour() {
		return baseCols.getLastPickedColour();
	}

	/**
	 * @return ARGB value of last colour picked
	 */
	public int getLastPickedAccentColour() {
		return accentCols.getLastPickedColour();
	}	
}