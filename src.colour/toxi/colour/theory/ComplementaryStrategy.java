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
 * Creates 5 additional colours in relation to the given base colour:
 * <ul>
 * <li>a contrasting colour: much darker or lighter than the original.</li>
 * <li>a soft supporting colour: lighter and less saturated.</li>
 * <li>a contrasting complement: very dark or very light.</li>
 * <li>the complement and</li>
 * <li>a light supporting variant.</li>
 * </ul>
 * 
 * @author toxi
 * 
 */
public class ComplementaryStrategy implements ColorTheoryStrategy {

	public static final String NAME = "complementary";

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * toxi.colour.ColorTheoryStrategy#createListFromColour(toxi.colour.Colour)
	 */
	public ColourList createListFromColour(Colour src) {
		ColourList colors = new ColourList(src.copy());
		// # A contrasting colour: much darker or lighter than the original.
		Colour c = src.copy();
		if (c.brightness() > 0.4) {
			c.setBrightness(0.1f + c.brightness() * 0.25f);
		} else {
			c.setBrightness(1.0f - c.brightness() * 0.25f);
		}
		colors.add(c);

		// A soft supporting colour: lighter and less saturated.
		c = src.copy();
		c.lighten(0.3f);
		c.setSaturation(0.1f + c.saturation() * 0.3f);
		colors.add(c);

		// A contrasting complement: very dark or very light.
		c = src.getComplement();
		if (c.brightness() > 0.3)
			c.setBrightness(0.1f + c.brightness() * 0.25f);
		else
			c.setBrightness(1.0f - c.brightness() * 0.25f);
		colors.add(c);

		// The complement and a light supporting variant.
		colors.add(src.getComplement());

		c = src.getComplement();
		c.lighten(0.3f);
		c.setSaturation(0.1f + c.saturation() * 0.25f);
		colors.add(c);
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
