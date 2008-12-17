/*
 * The classes in this package have been partly inspired by & bits ported from
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
package toxi.colour.theory;

import toxi.colour.Colour;
import toxi.colour.ColourList;

/**
 * Implements the <a href="http://www.tigercolor.com/color-lab/color-theory/color-theory-intro.htm#split-complementary"
 * >split-complementary colour scheme</a> to create 2 compatible colours for the
 * given one.
 * 
 * @author toxi
 * 
 */
public class SplitComplementaryStrategy implements ColorTheoryStrategy {

	public static final String NAME = "splitComplementary";

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * toxi.colour.ColorTheoryStrategy#createListFromColour(toxi.colour.Colour)
	 */
	public ColourList createListFromColour(Colour src) {
		Colour clr = src.copy();
		ColourList colors = new ColourList(clr);
		clr = clr.getComplement();
		colors.add(clr.getRotatedRYB(-30).lighten(0.1f));
		colors.add(clr.getRotatedRYB(30).lighten(0.1f));
		return colors;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see toxi.colour.ColorTheoryStrategy#getName()
	 */
	public String getName() {
		return NAME;
	}

	@Override
	public String toString() {
		return NAME;
	}
}
