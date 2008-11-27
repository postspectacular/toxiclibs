package toxi.color;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import toxi.math.MathUtils;

public class ColorList {

	protected ArrayList colors = new ArrayList();

	/**
	 * Creates an empty list.
	 */
	public ColorList() {

	}

	/**
	 * Creates a color list with the supplied color as first entry.
	 * 
	 * @param c
	 *            color
	 */
	public ColorList(Color c) {
		this.colors.add(c.copy());
	}

	/**
	 * Creates a deep copy of the given ColorList.
	 * 
	 * @param list
	 *            source list
	 */
	public ColorList(ColorList list) {
		for (Iterator i = list.iterator(); i.hasNext();) {
			this.colors.add(((Color) i.next()).copy());
		}
	}

	/**
	 * Creates a ColorList by wrapping the given ArrayList of colours.
	 * 
	 * @param colours
	 */
	public ColorList(ArrayList colours) {
		this.colors.addAll(colours);
	}

	/**
	 * Creates new ColorList from the given array of colors.
	 * 
	 * @param colourArray
	 */
	public ColorList(Color[] colourArray) {
		for (int i = 0; i < colourArray.length; i++)
			colors.add(colourArray[i]);
	}

	/**
	 * Creates a new colour list from the array of ARGB values.
	 * 
	 * @param argbArray
	 */
	public ColorList(int[] argbArray) {
		for (int i = 0; i < argbArray.length; i++) {
			colors.add(Color.newARGB(argbArray[i]));
		}
	}

	/**
	 * Returns an iterator over the internal list.
	 * 
	 * @return
	 */
	public Iterator iterator() {
		return colors.iterator();
	}

	/**
	 * Creates a ColorList based on the {@link ColorTheoryStrategy} instance and
	 * the given source color.
	 * 
	 * @param strategy
	 * @param c
	 * @return
	 */
	public static final ColorList createUsingStrategy(
			ColorTheoryStrategy strategy, Color c) {
		return strategy.createListFromColour(c);
	}

	/**
	 * Creates a ColorList based on the {@link ColorTheoryStrategy} name and the
	 * given source color.
	 * 
	 * @param name
	 * @param c
	 * @return new color list of null, if the supplied strategy name is not
	 *         mapped to a registered implementation.
	 */
	public static final ColorList createUsingStrategy(String name, Color c) {
		ColorTheoryStrategy strategy = ColorTheoryFactory.getInstance()
				.getStrategyForName(name);
		if (strategy != null) {
			strategy.createListFromColour(c);
		}
		return null;
	}

	/**
	 * Creates a new ColorList of colors sampled from the given ARGB image
	 * array.
	 * 
	 * @param pixels
	 * @param num
	 *            number of colors samples (clipped automatically to number of
	 *            pixels in the image)
	 * @param uniqueOnly
	 *            flag if only unique samples are to be taken (doesn't guarantee
	 *            unique colors though)
	 * @return new color list
	 */
	public static final ColorList createFromARGBArray(int[] pixels, int num,
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
		return new ColorList(colors);
	}

	/**
	 * Adds the given color to the list
	 * 
	 * @param c
	 * @return itself
	 */
	public ColorList add(Color c) {
		colors.add(c);
		return this;
	}

	/**
	 * Finds and returns the darkest color of the list.
	 * 
	 * @return darkest color or null if there're no entries yet.
	 */
	public Color getDarkest() {
		Color darkest = null;
		float minBrightness = Float.MAX_VALUE;
		for (Iterator i = colors.iterator(); i.hasNext();) {
			Color c = (Color) i.next();
			if (c.hsv[2] < minBrightness) {
				darkest = c;
				minBrightness = c.hsv[2];
			}
		}
		return darkest;
	}

	/**
	 * Finds and returns the lightest color of the list.
	 * 
	 * @return lightest color or null, if there're no entries yet.
	 */
	public Color getLightest() {
		Color lightest = null;
		float maxBrightness = Float.MIN_VALUE;
		for (Iterator i = colors.iterator(); i.hasNext();) {
			Color c = (Color) i.next();
			if (c.hsv[2] > maxBrightness) {
				lightest = c;
				maxBrightness = c.hsv[2];
			}
		}
		return lightest;
	}

	/**
	 * Calculates and returns the average color of the list.
	 * 
	 * @return average color or null, if there're no entries yet.
	 */
	public Color getAverage() {
		float r = 0;
		float g = 0;
		float b = 0;
		float a = 0;
		for (Iterator i = colors.iterator(); i.hasNext();) {
			Color c = (Color) i.next();
			r += c.rgb[0];
			g += c.rgb[1];
			b += c.rgb[2];
			a += c.alpha;
		}
		int num = colors.size();
		if (num > 0) {
			return Color.newRGBA(r / num, g / num, b / num, a / num);
		} else {
			return null;
		}
	}

	/**
	 * Creates a new ColorList by blending all colors in the list with each
	 * other (successive indices only)
	 * 
	 * @param amount
	 *            blend amount
	 * @return new color list
	 */
	public ColorList getBlended(float amount) {
		Color[] clrs = new Color[colors.size()];
		for (int i = 0; i < clrs.length; i++) {
			Color c = (Color) colors.get(i > 0 ? i - 1 : clrs.length - 1);
			clrs[i] = ((Color) colors.get(i)).getBlended(c, amount);
		}
		return new ColorList(clrs);
	}

	/**
	 * Sorts the list by relative distance to each predecessor, starting with
	 * the darkest colour in the list.
	 * 
	 * @param isReversed
	 *            true, if list is to be sorted in reverse.
	 * @return itself
	 */
	public ColorList sortByDistance(boolean isReversed) {
		if (colors.size() == 0)
			return this;

		Color root = getDarkest();

		// Remove the darkest color from the stack,
		// put it in the sorted list as starting element.
		ArrayList stack = new ArrayList(colors);
		stack.remove(root);
		ArrayList sorted = new ArrayList(colors.size());
		sorted.add(root);

		// Now find the color in the stack closest to that color.
		// Take this color from the stack and add it to the sorted list.
		// Now find the color closest to that color, etc.
		int sortedCount = 0;
		while (stack.size() > 1) {
			Color closest = (Color) stack.get(0);
			Color lastSorted = (Color) sorted.get(sortedCount);
			float distance = closest.distanceTo(lastSorted);
			for (int i = stack.size() - 1; i >= 0; i--) {
				Color c = (Color) stack.get(i);
				float d = c.distanceTo(lastSorted);
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
		colors = sorted;
		return this;
	}

	/**
	 * Sorts the list using the given comparator.
	 * 
	 * @param comp
	 * @param isReversed
	 * @return itself
	 */
	protected ColorList sortByComparator(Comparator comp, boolean isReversed) {
		Collections.sort(colors, comp);
		if (isReversed) {
			Collections.reverse(colors);
		}
		return this;
	}

	/**
	 * Sorts the list using the given {@link ColorAccessCriteria}.
	 * 
	 * @param criteria
	 * @param isReversed
	 * @return itself
	 */
	public ColorList sortByCriteria(ColorAccessCriteria criteria,
			boolean isReversed) {
		Comparator comparator = criteria.getComparator();
		if (comparator != null) {
			return sortByComparator(comparator, isReversed);
		} else {
			return this;
		}
	}

	/**
	 * Convenience method. Sorts the list by hue.
	 * 
	 * @return itself
	 */
	public ColorList sort() {
		return sortByCriteria(ColorAccessCriteria.HUE, false);
	}

	/**
	 * Sorts the list based on two criteria to create clusters/segments within
	 * the list.
	 * 
	 * @param clusterCriteria
	 * @param subClusterCriteria
	 * @param numClusters
	 * @param isReversed
	 * @return
	 */
	public ColorList clusterSort(ColorAccessCriteria clusterCriteria,
			ColorAccessCriteria subClusterCriteria, int numClusters,
			boolean isReversed) {
		ArrayList sorted = new ArrayList(colors);
		Collections.sort(sorted, clusterCriteria.getComparator());
		Collections.reverse(sorted);
		ArrayList clusters = new ArrayList();

		float d = 1;
		int i = 0;
		int num = sorted.size();
		for (int j = 0; j < num; j++) {
			Color c = (Color) sorted.get(j);
			if (c.getComponentValue(clusterCriteria) < d) {
				ArrayList slice = new ArrayList();
				slice.addAll(sorted.subList(i, j));
				Collections.sort(slice, subClusterCriteria.getComparator());
				clusters.addAll(slice);
				d -= 1.0f / numClusters;
				i = j;
			}
		}
		ArrayList slice = new ArrayList();
		slice.addAll(sorted.subList(i, sorted.size()));
		Collections.sort(slice, subClusterCriteria.getComparator());
		clusters.addAll(slice);
		if (isReversed) {
			Collections.reverse(clusters);
		}
		colors = clusters;
		return this;
	}

	/**
	 * Reverses the current order of the list.
	 * 
	 * @return itself
	 */
	public ColorList reverse() {
		Collections.reverse(colors);
		return this;
	}

	/**
	 * Returns a reversed copy of the current list.
	 * 
	 * @return
	 */
	public ColorList getReverse() {
		return new ColorList(colors).reverse();
	}

	/**
	 * Returns the color at the given index.
	 * 
	 * @param i
	 * @return
	 */
	public Color get(int i) {
		return (Color) colors.get(i);
	}

	/**
	 * Checks if the given color is part of the list. Check is done by value,
	 * not instance.
	 * 
	 * @param c
	 * @return true, if the color is present.
	 */
	public boolean contains(Color c) {
		for (Iterator i = colors.iterator(); i.hasNext();) {
			if (i.next().equals(c))
				return true;
		}
		return false;
	}
}
