/*
 * Some classes in this package have been partly inspired by & bits ported from
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
package toxi.colour;

import java.util.ArrayList;
import java.util.StringTokenizer;

import toxi.math.MathUtils;

/**
 * A ColourTheme is a weighted collection of {@link ColourRange}s used to define
 * custom palettes with a certain balance between individual colours/shades. New
 * theme parts can be added via textual descriptors referring to one of the
 * preset {@link ColourRange}s and/or {@link NamedColour}s: e.g.
 * "warm springgreen". For each theme part a weight has to be specified. The
 * magnitude of the weight value is irrelevant and is only important in relation
 * to the weights of other theme parts. For example: Theme part A with a weight
 * of 0.5 will only have 1/20 of the weight of theme part B with a weight of
 * 5.0...
 * 
 * @author toxi
 * 
 */
public class ColourTheme {

	class ThemePart {
		ColourRange range;
		Colour col;
		float weight;

		ThemePart(ColourRange range, Colour col, float weight) {
			this.range = range;
			this.col = col;
			this.weight = weight;
		}

		public Colour getColor() {
			return range.getColor(col, ColourRange.DEFAULT_VARIANCE);
		}
	}

	protected String name;
	protected ArrayList<ThemePart> parts = new ArrayList<ThemePart>();

	protected float weightedSum;

	public ColourTheme(String name) {
		this.name = name;
	}

	public ColourTheme addRange(ColourRange range, Colour col, float weight) {
		parts.add(new ThemePart(range, col, weight));
		weightedSum += weight;
		return this;
	}

	public ColourTheme addRange(String descriptor, float weight) {
		StringTokenizer st = new StringTokenizer(descriptor, " ,");
		Colour col = null;
		ColourRange range = null;
		while (st.hasMoreTokens()) {
			String item = st.nextToken();
			if (ColourRange.getPresetForName(item) != null) {
				range = ColourRange.getPresetForName(item);
			} else if (NamedColour.getForName(item) != null) {
				col = NamedColour.getForName(item);
			}
		}
		if (range != null) {
			addRange(range, col, weight);
		}
		return this;
	}

	public Colour getColor() {
		float rnd = MathUtils.random(1f);
		for (ThemePart t : parts) {
			if (t.weight / weightedSum >= rnd) {
				return t.getColor();
			}
			rnd -= t.weight / weightedSum;
		}
		return null;
	}

	public ColourList getColors(int num) {
		ColourList list = new ColourList();
		for (int i = 0; i < num; i++) {
			list.add(getColor());
		}
		return list;
	}

}
