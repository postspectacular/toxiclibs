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
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import toxi.colour.theory.ColorTheoryRegistry;
import toxi.colour.theory.ColorTheoryStrategy;
import toxi.math.MathUtils;

/**
 * A container class of concrete colours. ColorLists can be built manually and
 * are also created when working with {@link ColourRange}s. The class has
 * various methods to manipulate all colours in the list in parallel, as well as
 * sort them by various criteria.
 * 
 * @see ColourRange
 * @see AccessCriteria
 * 
 * @author toxi
 * 
 */
public class ColourList implements Iterable<Colour> {

	protected ArrayList<Colour> colours = new ArrayList<Colour>();

	/**
	 * Factory method. Creates a new ColourList of colours sampled from the
	 * given ARGB image array.
	 * 
	 * @param pixels
	 *            int array of ARGB pixels
	 * @param num
	 *            number of colours samples (clipped automatically to number of
	 *            pixels in the image)
	 * @param uniqueOnly
	 *            flag if only unique samples are to be taken (doesn't guarantee
	 *            unique colours though)
	 * @return new colour list
	 */
	public static final ColourList createFromARGBArray(int[] pixels, int num,
			boolean uniqueOnly) {
		num = MathUtils.min(num, pixels.length);
		int[] colors = new int[num];
		int[] index = new int[num];
		for (int i = 0; i < num; i++) {
			int idx;
			if (uniqueOnly) {
				boolean isUnique = true;
				do {
					idx = MathUtils.random(pixels.length);
					for (int j = 0; j < i; j++) {
						if (index[j] == idx) {
							isUnique = false;
							break;
						}
					}
				} while (!isUnique);
			} else {
				idx = MathUtils.random(pixels.length);
			}
			index[i] = idx;
			colors[i] = pixels[idx];
		}
		return new ColourList(colors);
	}

	/**
	 * Factory method. Creates a new ColourList based on the given
	 * {@link ColorTheoryStrategy} instance and the given source colour.
	 * 
	 * @param strategy
	 * @param c
	 * @return new list
	 */
	public static final ColourList createUsingStrategy(
			ColorTheoryStrategy strategy, Colour c) {
		return strategy.createListFromColour(c);
	}

	/**
	 * Factory method. Creates a ColourList based on the
	 * {@link ColorTheoryStrategy} name and the given source colour.
	 * 
	 * @param name
	 * @param c
	 * @return new colour list or null, if the supplied strategy name is not
	 *         mapped to a registered implementation.
	 */
	public static final ColourList createUsingStrategy(String name, Colour c) {
		ColorTheoryStrategy strategy = ColorTheoryRegistry
				.getStrategyForName(name);
		if (strategy != null) {
			return strategy.createListFromColour(c);
		}
		return null;
	}

	/**
	 * Creates an empty list.
	 */
	public ColourList() {

	}

	/**
	 * Creates a ColourList by wrapping the given ArrayList of colours. No
	 * copies of the given colours are created (shallow copy only).
	 * 
	 * @param colours
	 */
	public ColourList(ArrayList<Colour> colours) {
		this.colours.addAll(colours);
	}

	/**
	 * Creates a colour list with the supplied colour as first entry.
	 * 
	 * @param c
	 *            colour
	 */
	public ColourList(Colour c) {
		this.colours.add(c.copy());
	}

	/**
	 * Creates new ColourList from the given array of colours No copies of the
	 * given colours are created (shallow copy only).
	 * 
	 * @param colourArray
	 */
	public ColourList(Colour[] colourArray) {
		for (Colour c : colourArray)
			colours.add(c);
	}

	/**
	 * Creates a deep copy of the given ColourList. Manipulating the new list
	 * does NOT change the colours of the original.
	 * 
	 * @param list
	 *            source list
	 */
	public ColourList(ColourList list) {
		for (Colour c : list) {
			this.colours.add(c.copy());
		}
	}

	/**
	 * Creates a new colour list from the array of ARGB int values.
	 * 
	 * @param argbArray
	 */
	public ColourList(int[] argbArray) {
		for (int c : argbArray) {
			colours.add(Colour.newARGB(c));
		}
	}

	/**
	 * Adds the given colour to the list
	 * 
	 * @param c
	 * @return itself
	 */
	public ColourList add(Colour c) {
		colours.add(c);
		return this;
	}

	/**
	 * Adds all entries of the Colour collection to the list (shallow copy only,
	 * manipulating the new list will modify the original colours).
	 * 
	 * @param collection
	 * @return itself
	 */
	public ColourList addAll(Collection<Colour> collection) {
		colours.addAll(collection);
		return this;
	}

	/**
	 * Adjusts the brightness component of all list colours by the given amount.
	 * 
	 * @param step
	 *            adjustment value
	 * @return itself
	 */
	public ColourList adjustBrightness(float step) {
		for (Colour c : colours) {
			c.lighten(step);
		}
		return this;
	}

	/**
	 * Adjusts the saturation component of all list colours by the given amount.
	 * 
	 * @param step
	 *            adjustment value
	 * @return itself
	 */
	public ColourList adjustSaturation(float step) {
		for (Colour c : colours) {
			c.saturate(step);
		}
		return this;
	}

	/**
	 * Sorts the list based on two criteria to create clusters/segments within
	 * the list.
	 * 
	 * @param clusterCriteria
	 *            main sort criteria
	 * @param subClusterCriteria
	 *            secondary sort criteria
	 * @param numClusters
	 *            number of clusters
	 * @param isReversed
	 *            true, if reversed sort
	 * @return itself
	 */
	public ColourList clusterSort(AccessCriteria clusterCriteria,
			AccessCriteria subClusterCriteria, int numClusters,
			boolean isReversed) {
		ArrayList<Colour> sorted = new ArrayList<Colour>(colours);
		Collections.sort(sorted, clusterCriteria.getComparator());
		Collections.reverse(sorted);
		ArrayList<Colour> clusters = new ArrayList<Colour>();

		float d = 1;
		int i = 0;
		int num = sorted.size();
		for (int j = 0; j < num; j++) {
			Colour c = sorted.get(j);
			if (c.getComponentValue(clusterCriteria) < d) {
				ArrayList<Colour> slice = new ArrayList<Colour>();
				slice.addAll(sorted.subList(i, j));
				Collections.sort(slice, subClusterCriteria.getComparator());
				clusters.addAll(slice);
				d -= 1.0f / numClusters;
				i = j;
			}
		}
		ArrayList<Colour> slice = new ArrayList<Colour>();
		slice.addAll(sorted.subList(i, sorted.size()));
		Collections.sort(slice, subClusterCriteria.getComparator());
		clusters.addAll(slice);
		if (isReversed) {
			Collections.reverse(clusters);
		}
		colours = clusters;
		return this;
	}

	/**
	 * Switches all list colours to their complementary colour.
	 * 
	 * @return itself
	 */
	public ColourList complement() {
		for (Colour c : colours) {
			c.complement();
		}
		return this;
	}

	/**
	 * Checks if the given colour is part of the list. Check is done by value,
	 * not instance.
	 * 
	 * @param colour
	 * @return true, if the colour is present.
	 */
	public boolean contains(Colour colour) {
		for (Colour c : colours) {
			if (c.equals(colour))
				return true;
		}
		return false;
	}

	/**
	 * Returns the colour at the given index.
	 * 
	 * @param i
	 * @return colour
	 */
	public Colour get(int i) {
		return colours.get(i);
	}

	/**
	 * Calculates and returns the average colour of the list.
	 * 
	 * @return average colour or null, if there're no entries yet.
	 */
	public Colour getAverage() {
		float r = 0;
		float g = 0;
		float b = 0;
		float a = 0;
		for (Colour c : colours) {
			r += c.rgb[0];
			g += c.rgb[1];
			b += c.rgb[2];
			a += c.alpha;
		}
		int num = colours.size();
		if (num > 0) {
			return Colour.newRGBA(r / num, g / num, b / num, a / num);
		} else {
			return null;
		}
	}

	/**
	 * Creates a new ColourList by blending all colours in the list with each
	 * other (successive indices only)
	 * 
	 * @param amount
	 *            blend amount
	 * @return new colour list
	 */
	public ColourList getBlended(float amount) {
		Colour[] clrs = new Colour[colours.size()];
		for (int i = 0; i < clrs.length; i++) {
			Colour c = colours.get(i > 0 ? i - 1 : clrs.length - 1);
			clrs[i] = colours.get(i).getBlended(c, amount);
		}
		return new ColourList(clrs);
	}

	/**
	 * Finds and returns the darkest colour of the list.
	 * 
	 * @return darkest colour or null if there're no entries yet.
	 */
	public Colour getDarkest() {
		Colour darkest = null;
		float minBrightness = Float.MAX_VALUE;
		for (Colour c : colours) {
			float luma = c.luminance();
			if (luma < minBrightness) {
				darkest = c;
				minBrightness = luma;
			}
		}
		return darkest;
	}

	/**
	 * Finds and returns the lightest (luminance) colour of the list.
	 * 
	 * @return lightest colour or null, if there're no entries yet.
	 */
	public Colour getLightest() {
		Colour lightest = null;
		float maxBrightness = Float.MIN_VALUE;
		for (Colour c : colours) {
			float luma = c.luminance();
			if (luma > maxBrightness) {
				lightest = c;
				maxBrightness = luma;
			}
		}
		return lightest;
	}

	/**
	 * Returns a reversed copy of the current list.
	 * 
	 * @return reversed copy of the list
	 */
	public ColourList getReverse() {
		return new ColourList(colours).reverse();
	}

	/**
	 * Inverts all colours in the list.
	 * 
	 * @return itself
	 */
	public ColourList invert() {
		for (Colour c : colours) {
			c.invert();
		}
		return this;
	}

	/**
	 * Returns an iterator over the internal list. This means the list can be
	 * accessed via standard Iterator loops.
	 * 
	 * @return list iterator
	 */
	public Iterator<Colour> iterator() {
		return colours.iterator();
	}

	/**
	 * Reverses the current order of the list.
	 * 
	 * @return itself
	 */
	public ColourList reverse() {
		Collections.reverse(colours);
		return this;
	}

	/**
	 * Rotates the hues of all colours in the list by the given amount.
	 * 
	 * @param theta
	 *            rotation angle in radians
	 * @return itself
	 */
	public ColourList rotateRYB(float theta) {
		return rotateRYB(MathUtils.degrees(theta));
	}

	/**
	 * Rotates the hues of all colours in the list by the given amount.
	 * 
	 * @param angle
	 *            rotation angle in degrees
	 * @return itself
	 */
	public ColourList rotateRYB(int angle) {
		for (Colour c : colours) {
			c.rotateRYB(angle);
		}
		return this;
	}

	/**
	 * @return the number of colours in the list
	 */
	public float size() {
		return colours.size();
	}

	/**
	 * Convenience method. Sorts the list by hue.
	 * 
	 * @return itself
	 */
	public ColourList sort() {
		return sortByCriteria(AccessCriteria.HUE, false);
	}

	/**
	 * Sorts the list using the given comparator.
	 * 
	 * @param comp
	 *            comparator
	 * @param isReversed
	 *            true, if reversed sort
	 * @return itself
	 */
	public ColourList sortByComparator(Comparator<Colour> comp,
			boolean isReversed) {
		Collections.sort(colours, comp);
		if (isReversed) {
			Collections.reverse(colours);
		}
		return this;
	}

	/**
	 * Sorts the list using the given {@link AccessCriteria}.
	 * 
	 * @param criteria
	 *            sort criteria
	 * @param isReversed
	 *            true, if reversed sort
	 * @return itself
	 */
	public ColourList sortByCriteria(AccessCriteria criteria, boolean isReversed) {
		Comparator<Colour> comparator = criteria.getComparator();
		if (comparator != null) {
			return sortByComparator(comparator, isReversed);
		} else {
			return this;
		}
	}

	/**
	 * Sorts the list by relative distance to each predecessor, starting with
	 * the darkest colour in the list.
	 * 
	 * @param isReversed
	 *            true, if list is to be sorted in reverse.
	 * @return itself
	 */
	public ColourList sortByDistance(boolean isReversed) {
		return sortByDistance(new HSVDistanceProxy(), isReversed);
	}

	/**
	 * Sorts the list by relative distance to each predecessor, starting with
	 * the darkest colour in the list.
	 * 
	 * @param isReversed
	 *            true, if list is to be sorted in reverse.
	 * @return itself
	 */
	public ColourList sortByDistance(DistanceProxy proxy, boolean isReversed) {
		if (colours.size() == 0)
			return this;

		Colour root = getDarkest();

		// Remove the darkest colour from the stack,
		// put it in the sorted list as starting element.
		ArrayList<Colour> stack = new ArrayList<Colour>(colours);
		stack.remove(root);
		ArrayList<Colour> sorted = new ArrayList<Colour>(colours.size());
		sorted.add(root);

		// Now find the colour in the stack closest to that colour.
		// Take this colour from the stack and add it to the sorted list.
		// Now find the colour closest to that colour, etc.
		int sortedCount = 0;
		while (stack.size() > 1) {
			Colour closest = stack.get(0);
			Colour lastSorted = sorted.get(sortedCount);
			float distance = proxy.distanceBetween(closest, lastSorted);
			for (int i = stack.size() - 1; i >= 0; i--) {
				Colour c = stack.get(i);
				float d = proxy.distanceBetween(c, lastSorted);
				if (d < distance) {
					closest = c;
					distance = d;
				}
			}
			stack.remove(closest);
			sorted.add(closest);
			sortedCount++;
		}
		sorted.add(stack.get(0));
		if (isReversed) {
			Collections.reverse(sorted);
		}
		colours = sorted;
		return this;
	}

	/**
	 * Sorts the list by proximity to the given target colour (using RGB
	 * distance metrics).
	 * 
	 * @see #sortByProximityTo(Colour, DistanceProxy, boolean)
	 * @param target
	 *            colour
	 * @param isReversed
	 *            true, if reverse sorted
	 * @return sorted list
	 */
	public ColourList sortByProximityTo(Colour target, boolean isReversed) {
		return sortByProximityTo(target, new RGBDistanceProxy(), isReversed);
	}

	/**
	 * Sorts the list by proximity to the given target colour using the given
	 * {@link DistanceProxy} implementation.
	 * 
	 * @param target
	 *            colour
	 * @param proxy
	 *            distance metrics
	 * @param isReversed
	 *            true, if reverse sorted
	 * @return sorted list
	 */
	public ColourList sortByProximityTo(Colour target, DistanceProxy proxy,
			boolean isReversed) {
		return sortByComparator(new ProximityComparator(target, proxy),
				isReversed);
	}
}
