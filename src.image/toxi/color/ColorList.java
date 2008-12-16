package toxi.color;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import toxi.color.theory.ColorTheoryFactory;
import toxi.color.theory.ColorTheoryStrategy;
import toxi.math.MathUtils;

public class ColorList implements Iterable<Color> {

	protected ArrayList<Color> colors = new ArrayList<Color>();

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
		for (Color c : list) {
			this.colors.add(c.copy());
		}
	}

	/**
	 * Creates a ColorList by wrapping the given ArrayList of colours.
	 * 
	 * @param colours
	 */
	public ColorList(ArrayList<Color> colours) {
		this.colors.addAll(colours);
	}

	/**
	 * Creates new ColorList from the given array of colors.
	 * 
	 * @param colourArray
	 */
	public ColorList(Color[] colourArray) {
		for (Color c : colourArray)
			colors.add(c);
	}

	/**
	 * Creates a new colour list from the array of ARGB values.
	 * 
	 * @param argbArray
	 */
	public ColorList(int[] argbArray) {
		for (int c : argbArray) {
			colors.add(Color.newARGB(c));
		}
	}

	/**
	 * Returns an iterator over the internal list.
	 * 
	 * @return
	 */
	public Iterator<Color> iterator() {
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
	 * Add the all entries of the Color collection to the list.
	 * 
	 * @param collection
	 * @return itself
	 */
	public ColorList addAll(Collection<Color> collection) {
		colors.addAll(collection);
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
		for (Color c : colors) {
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
		for (Color c : colors) {
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
		for (Color c : colors) {
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
			Color c = colors.get(i > 0 ? i - 1 : clrs.length - 1);
			clrs[i] = colors.get(i).getBlended(c, amount);
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
		ArrayList<Color> stack = new ArrayList<Color>(colors);
		stack.remove(root);
		ArrayList<Color> sorted = new ArrayList<Color>(colors.size());
		sorted.add(root);

		// Now find the color in the stack closest to that color.
		// Take this color from the stack and add it to the sorted list.
		// Now find the color closest to that color, etc.
		int sortedCount = 0;
		while (stack.size() > 1) {
			Color closest = stack.get(0);
			Color lastSorted = sorted.get(sortedCount);
			float distance = closest.distanceTo(lastSorted);
			for (int i = stack.size() - 1; i >= 0; i--) {
				Color c = stack.get(i);
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
	public ColorList sortByComparator(Comparator<Color> comp, boolean isReversed) {
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
		Comparator<Color> comparator = criteria.getComparator();
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
		ArrayList<Color> sorted = new ArrayList<Color>(colors);
		Collections.sort(sorted, clusterCriteria.getComparator());
		Collections.reverse(sorted);
		ArrayList<Color> clusters = new ArrayList<Color>();

		float d = 1;
		int i = 0;
		int num = sorted.size();
		for (int j = 0; j < num; j++) {
			Color c = sorted.get(j);
			if (c.getComponentValue(clusterCriteria) < d) {
				ArrayList<Color> slice = new ArrayList<Color>();
				slice.addAll(sorted.subList(i, j));
				Collections.sort(slice, subClusterCriteria.getComparator());
				clusters.addAll(slice);
				d -= 1.0f / numClusters;
				i = j;
			}
		}
		ArrayList<Color> slice = new ArrayList<Color>();
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
		return colors.get(i);
	}

	/**
	 * Checks if the given color is part of the list. Check is done by value,
	 * not instance.
	 * 
	 * @param col
	 * @return true, if the color is present.
	 */
	public boolean contains(Color col) {
		for (Color c : colors) {
			if (c.equals(col))
				return true;
		}
		return false;
	}
}
