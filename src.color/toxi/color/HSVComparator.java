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
package toxi.color;

import java.util.Comparator;

/**
 * Compares 2 colors by one of their HSV component values.
 * 
 * @author toxi
 * 
 */
public class HSVComparator implements Comparator<ReadonlyTColor> {

	private final int component;

	public HSVComparator(int comp) {
		component = comp;
	}

	public int compare(ReadonlyTColor a, ReadonlyTColor b) {
		float ca, cb;
		switch (component) {
		case 0:
			ca = a.hue();
			cb = b.hue();
			break;
		case 1:
			ca = a.saturation();
			cb = b.saturation();
			break;
		case 2:
		default:
			ca = a.brightness();
			cb = b.brightness();
		}
		return Float.compare(ca, cb);
	}

}
