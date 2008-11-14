package toxi.color;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import toxi.math.MathUtils;

public class ColorList {

	protected ArrayList colors = new ArrayList();

	public ColorList() {

	}

	public ColorList(Color c) {
		colors.add(c);
	}

	public ColorList(ArrayList colours) {
		this.colors.addAll(colours);
	}

	public ColorList(Color[] colourArray) {
		for (int i = 0; i < colourArray.length; i++)
			colors.add(colourArray[i]);
	}

	public ColorList(int[] argbArray) {
		for (int i = 0; i < argbArray.length; i++) {
			colors.add(Color.newARGB(argbArray[i]));
		}
	}

	public Iterator iterator() {
		return colors.iterator();
	}

	public static final ColorList createUsingStrategy(
			ColorTheoryStrategy strategy, Color c) {
		return strategy.createListFromColour(c);
	}

	public static final ColorList createUsingStrategy(String name, Color c) {
		ColorTheoryStrategy strategy = ColorTheoryFactory.getInstance()
				.getStrategyForName(name);
		if (strategy != null) {
			strategy.createListFromColour(c);
		}
		return null;
	}

	public static final ColorList createFromARGBImage(int[] pixels, int num,
			boolean uniqueOnly) {
		ArrayList colours = new ArrayList();
		num = MathUtils.min(num, pixels.length);
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
			int col = pixels[idx];
			colours.add(Color.newRGBA(((col >> 16) & 0xff) / 255f,
					((col >> 8) & 0xff) / 255f, (col & 0xff) / 255f,
					(col >>> 24) / 255f));
		}
		return new ColorList(colours);
	}

	public ColorList add(Color c) {
		colors.add(c);
		return this;
	}

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
		return Color.newRGBA(r / num, g / num, b / num, a / num);
	}

	public ColorList getBlended(float amount) {
		Color[] clrs = (Color[]) colors.toArray(new Color[0]);
		for (int i = 0; i < clrs.length; i++) {
			clrs[i] = clrs[i].getBlended(clrs[i > 0 ? i - 1 : clrs.length - 1],
					amount);
		}
		return new ColorList(clrs);
	}

	public ColorList sortByDistance(boolean isReversed) {
		if (colors.size() == 0)
			return new ColorList();

		// Find the darkest color in the list.
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
		if (isReversed)
			Collections.reverse(sorted);
		return new ColorList(sorted);
	}

	protected ColorList sortByComparator(Comparator comp, boolean isReversed) {
		ArrayList sorted = new ArrayList(colors);
		Collections.sort(sorted, comp);
		if (isReversed)
			Collections.reverse(sorted);
		return new ColorList(sorted);
	}

	public ColorList sortByCriteria(ColorAccessCriteria criteria,
			boolean isReversed) {
		Comparator comparator = criteria.getComparator();
		if (comparator != null) {
			return sortByComparator(comparator, isReversed);
		} else
			return null;
	}

	public ColorList sort() {
		return sortByCriteria(ColorAccessCriteria.HUE, false);
	}

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
				System.out.println("adding slice: " + slice.size());
				Collections.sort(slice, subClusterCriteria.getComparator());
				clusters.addAll(slice);
				System.out.println("total: " + clusters.size());
				d -= 1.0f / numClusters;
				i = j;
			}
		}
		ArrayList slice = new ArrayList();
		slice.addAll(sorted.subList(i, sorted.size()));
		Collections.sort(slice, subClusterCriteria.getComparator());
		clusters.addAll(slice);
		if (isReversed)
			Collections.reverse(clusters);
		return new ColorList(clusters);
	}

	public ColorList reverse() {
		Collections.reverse(colors);
		return this;
	}

	public ColorList getReverse() {
		return new ColorList(colors).reverse();
	}

	public Color get(int i) {
		if (i > 0 && i < colors.size())
			return (Color) colors.get(i);
		else
			return null;
	}

	public boolean contains(Color c) {
		for (Iterator i = colors.iterator(); i.hasNext();) {
			if (i.next().equals(c))
				return true;
		}
		return false;
	}
}
