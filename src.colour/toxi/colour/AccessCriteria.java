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

import java.util.Comparator;

/**
 * Defines colour component access criterias and associated {@link Comparator}s
 * used to sort colours based on component values.
 * 
 * @author toxi
 * 
 */
public class AccessCriteria {

	public static final AccessCriteria HUE = new AccessCriteria(AccessMode.HSV,
			0);
	public static final AccessCriteria SATURATION = new AccessCriteria(
			AccessMode.HSV, 1);
	public static final AccessCriteria BRIGHTNESS = new AccessCriteria(
			AccessMode.HSV, 2);
	public static final AccessCriteria RED = new AccessCriteria(AccessMode.RGB,
			0);
	public static final AccessCriteria GREEN = new AccessCriteria(
			AccessMode.RGB, 1);
	public static final AccessCriteria BLUE = new AccessCriteria(
			AccessMode.RGB, 2);
	public static final AccessCriteria CYAN = new AccessCriteria(
			AccessMode.CMYK, 0);
	public static final AccessCriteria MAGENTA = new AccessCriteria(
			AccessMode.CMYK, 1);
	public static final AccessCriteria YELLOW = new AccessCriteria(
			AccessMode.CMYK, 2);
	public static final AccessCriteria BLACK = new AccessCriteria(
			AccessMode.CMYK, 3);

	public static final AccessCriteria ALPHA = new AccessCriteria(
			AccessMode.ALPHA, 0);

	public static final AccessCriteria LUMINANCE = new AccessCriteria(
			AccessMode.DIRECT, 0, new LuminanceComparator());

	protected final AccessMode mode;
	protected final int component;
	protected Comparator<Colour> comparator;

	protected AccessCriteria(AccessMode mode, int compID) {
		this(mode, compID, null);
	}

	protected AccessCriteria(AccessMode mode, int compID,
			Comparator<Colour> comparator) {
		this.mode = mode;
		this.component = compID;
		this.comparator = comparator;
	}

	/**
	 * @return the comparator associated with the criteria.
	 */
	public Comparator<Colour> getComparator() {
		if (comparator == null) {
			switch (mode) {
			case HSV:
				comparator = new HSVComparator(component);
				break;
			case RGB:
				comparator = new RGBComparator(component);
				break;
			case CMYK:
				comparator = new CMYKComparator(component);
				break;
			case ALPHA:
				comparator = new AlphaComparator();
				break;
			}
		}
		return comparator;
	}

	public int getComponent() {
		return component;
	}

	public AccessMode getMode() {
		return mode;
	}
}
