/*
 * Some classes in this package have been partly inspired by & bits ported from
 * Python code written by Tom De Smedt & Frederik De Bleser for the "colors" library
 * of Nodebox.net.
 * 
 * http://nodebox.net/code/index.php/Colors
 *
 * Copyright (c) 2006-2011 Karsten Schmidt
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
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301, USA
 */

package toxi.color;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import toxi.color.theory.ColorTheoryRegistry;
import toxi.color.theory.ColorTheoryStrategy;
import toxi.math.MathUtils;

/**
 * A container class of concrete colors. ColorLists can be built manually and
 * are also created when working with {@link ColorRange}s. The class has various
 * methods to manipulate all colors in the list in parallel, as well as sort
 * them by various criteria.
 * 
 * @see ColorRange
 * @see AccessCriteria
 */
public class ColorList implements Iterable<TColor> {

    /**
     * Factory method. Creates a new ColorList of colors sampled from the given
     * ARGB image array. If the number of samples equals or exceeds the number
     * of pixels in the image and no unique colors are required, the function
     * will simply return the same as {@link #ColorList(int[])}.
     * 
     * @param pixels
     *            int array of ARGB pixels
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
        return createFromARGBArray(pixels, num, uniqueOnly, 100);
    }

    /**
     * Factory method. Creates a new ColorList of colors randomly sampled from
     * the given ARGB image array. If the number of samples equals or exceeds
     * the number of pixels in the source image and no unique colors are
     * required, the function will simply return the same as
     * {@link #ColorList(int[])}.
     * 
     * @param pixels
     *            int array of ARGB pixels
     * @param num
     *            number of colors samples (clipped automatically to number of
     *            pixels in the image)
     * @param uniqueOnly
     *            flag if only unique samples are to be taken (doesn't guarantee
     *            unique colors though)
     * @param maxIterations
     *            max number of attempts to find a unique color. If no more
     *            unique colors can be found the search is terminated.
     * @return new color list of samples
     */
    public static final ColorList createFromARGBArray(int[] pixels, int num,
            boolean uniqueOnly, int maxIterations) {
        num = MathUtils.min(num, pixels.length);
        if (!uniqueOnly && num == pixels.length) {
            return new ColorList(pixels);
        }
        List<TColor> colors = new ArrayList<TColor>();
        TColor temp = TColor.BLACK.copy();
        for (int i = 0; i < num; i++) {
            int idx;
            if (uniqueOnly) {
                boolean isUnique = true;
                int numTries = 0;
                do {
                    idx = MathUtils.random(pixels.length);
                    temp.setARGB(pixels[idx]);
                    isUnique = !colors.contains(temp);
                } while (!isUnique && ++numTries < maxIterations);
                if (numTries < maxIterations) {
                    colors.add(temp.copy());
                } else {
                    break;
                }
            } else {
                idx = MathUtils.random(pixels.length);
                colors.add(TColor.newARGB(pixels[idx]));
            }
        }
        return new ColorList(colors);
    }

    /**
     * Factory method. Creates a new ColorList based on the given
     * {@link ColorTheoryStrategy} instance and the given source color. The
     * number of colors returned will vary with the strategy chosen.
     * 
     * @param strategy
     * @param c
     * @return new list
     */
    public static final ColorList createUsingStrategy(
            ColorTheoryStrategy strategy, TColor c) {
        return strategy.createListFromColor(c);
    }

    /**
     * Factory method. Creates a ColorList based on the name of a
     * {@link ColorTheoryStrategy} and the given source color.
     * 
     * @param name
     *            strategy name
     * @param c
     * @return new color list or null, if the supplied strategy name is not
     *         mapped to a registered implementation.
     */
    public static final ColorList createUsingStrategy(String name, TColor c) {
        ColorTheoryStrategy strategy = ColorTheoryRegistry
                .getStrategyForName(name);
        ColorList list = null;
        if (strategy != null) {
            list = strategy.createListFromColor(c);
        }
        return list;
    }

    @XmlElement(name = "col")
    @XmlJavaTypeAdapter(TColorAdapter.class)
    protected List<TColor> colors = new ArrayList<TColor>();

    /**
     * Creates an empty list.
     */
    public ColorList() {

    }

    /**
     * Creates a ColorList by wrapping the given ArrayList of colors. No copies
     * of the given colors are created (shallow copy only).
     * 
     * @param colors
     */
    public ColorList(Collection<TColor> colors) {
        this.colors.addAll(colors);
    }

    /**
     * Creates a deep copy of the given ColorList. Manipulating the new list or
     * its color entries does NOT change the colors of the original.
     * 
     * @param list
     *            source list to copy
     */
    public ColorList(ColorList list) {
        for (TColor c : list) {
            this.colors.add(c.copy());
        }
    }

    /**
     * Creates a new color list from the array of ARGB int values. In most cases
     * this will be the pixel buffer of an image.
     * 
     * @param argbArray
     */
    public ColorList(int[] argbArray) {
        for (int c : argbArray) {
            colors.add(TColor.newARGB(c));
        }
    }

    /**
     * Creates new ColorList from the given colors. Copies of the given colors
     * are created. This is a varargs constructor allowing these two parameter
     * formats:
     * 
     * <pre>
     * // individual parameters
     * ColorList cols=new ColorList(TColor.BLACK,TColor.WHITE,TColor.newRGB(1,0,0));
     * 
     * // or array of colors
     * ReadonlyTColor[] colArray=new ReadonlyTColor[] {
     *   TColor.BLACK,TColor.WHITE,TColor.newRGB(1,0,0);
     * };
     * ColorList cols=new ColorList(colArray);
     * </pre>
     * 
     * @param colorArray
     */
    public ColorList(ReadonlyTColor... colorArray) {
        for (ReadonlyTColor c : colorArray) {
            colors.add(c.copy());
        }
    }

    /**
     * Adds a copy of the given color to the list
     * 
     * @param c
     * @return itself
     */
    public ColorList add(ReadonlyTColor c) {
        colors.add(c.copy());
        return this;
    }

    /**
     * Adds all entries of the TColor collection to the list (shallow copy only,
     * manipulating the new list will modify the original colors).
     * 
     * @param collection
     * @return itself
     */
    public ColorList addAll(Collection<TColor> collection) {
        colors.addAll(collection);
        return this;
    }

    /**
     * Adjusts the brightness component of all list colors by the given amount.
     * 
     * @param step
     *            adjustment value
     * @return itself
     */
    public ColorList adjustBrightness(float step) {
        for (TColor c : colors) {
            c.lighten(step);
        }
        return this;
    }

    /**
     * Adjusts the saturation component of all list colors by the given amount.
     * 
     * @param step
     *            adjustment value
     * @return itself
     */
    public ColorList adjustSaturation(float step) {
        for (TColor c : colors) {
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
    public ColorList clusterSort(AccessCriteria clusterCriteria,
            AccessCriteria subClusterCriteria, int numClusters,
            boolean isReversed) {
        ArrayList<TColor> sorted = new ArrayList<TColor>(colors);
        Collections.sort(sorted, clusterCriteria);
        Collections.reverse(sorted);
        ArrayList<TColor> clusters = new ArrayList<TColor>();

        float d = 1;
        int i = 0;
        int num = sorted.size();
        for (int j = 0; j < num; j++) {
            ReadonlyTColor c = sorted.get(j);
            if (c.getComponentValue(clusterCriteria) < d) {
                ArrayList<TColor> slice = new ArrayList<TColor>();
                slice.addAll(sorted.subList(i, j));
                Collections.sort(slice, subClusterCriteria);
                clusters.addAll(slice);
                d -= 1.0f / numClusters;
                i = j;
            }
        }
        ArrayList<TColor> slice = new ArrayList<TColor>();
        slice.addAll(sorted.subList(i, sorted.size()));
        Collections.sort(slice, subClusterCriteria);
        clusters.addAll(slice);
        if (isReversed) {
            Collections.reverse(clusters);
        }
        colors = clusters;
        return this;
    }

    /**
     * Switches all list colors to their complementary color.
     * 
     * @return itself
     */
    public ColorList complement() {
        for (TColor c : colors) {
            c.complement();
        }
        return this;
    }

    /**
     * Checks if the given color is part of the list. Check is done by value,
     * not instance.
     * 
     * @param color
     * @return true, if the color is present.
     */
    public boolean contains(ReadonlyTColor color) {
        for (ReadonlyTColor c : colors) {
            if (c.equals(color)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the color at the given index. This function follows Python
     * convention, in that if the index is negative, it is considered relative
     * to the list end. Therefore the color at index -1 is the last color in the
     * list.
     * 
     * @param i
     *            index
     * @return color
     */
    public TColor get(int i) {
        if (i < 0) {
            i += colors.size();
        }
        return colors.get(i);
    }

    /**
     * Calculates and returns the average color of the list.
     * 
     * @return average color or null, if there're no entries yet.
     */
    public ReadonlyTColor getAverage() {
        float r = 0;
        float g = 0;
        float b = 0;
        float a = 0;
        for (TColor c : colors) {
            r += c.rgb[0];
            g += c.rgb[1];
            b += c.rgb[2];
            a += c.alpha;
        }
        int num = colors.size();
        if (num > 0) {
            return TColor.newRGBA(r / num, g / num, b / num, a / num);
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
        TColor[] clrs = new TColor[colors.size()];
        for (int i = 0; i < clrs.length; i++) {
            TColor c = colors.get(i > 0 ? i - 1 : clrs.length - 1);
            clrs[i] = colors.get(i).getBlended(c, amount);
        }
        return new ColorList(clrs);
    }

    /**
     * Finds and returns the darkest color of the list.
     * 
     * @return darkest color or null if there're no entries yet.
     */
    public TColor getDarkest() {
        TColor darkest = null;
        float minBrightness = Float.MAX_VALUE;
        for (TColor c : colors) {
            float luma = c.luminance();
            if (luma < minBrightness) {
                darkest = c;
                minBrightness = luma;
            }
        }
        return darkest;
    }

    /**
     * Finds and returns the lightest (luminance) color of the list.
     * 
     * @return lightest color or null, if there're no entries yet.
     */
    public ReadonlyTColor getLightest() {
        ReadonlyTColor lightest = null;
        float maxBrightness = Float.MIN_VALUE;
        for (ReadonlyTColor c : colors) {
            float luma = c.luminance();
            if (luma > maxBrightness) {
                lightest = c;
                maxBrightness = luma;
            }
        }
        return lightest;
    }

    public TColor getRandom() {
        return colors.get(MathUtils.random(colors.size()));
    }

    /**
     * Returns a reversed copy of the current list.
     * 
     * @return reversed copy of the list
     */
    public ColorList getReverse() {
        return new ColorList(colors).reverse();
    }

    /**
     * Inverts all colors in the list.
     * 
     * @return itself
     */
    public ColorList invert() {
        for (TColor c : colors) {
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
    public Iterator<TColor> iterator() {
        return colors.iterator();
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
     * Rotates the hues of all colors in the list by the given amount.
     * 
     * @param theta
     *            rotation angle in radians
     * @return itself
     */
    public ColorList rotateRYB(float theta) {
        return rotateRYB(MathUtils.degrees(theta));
    }

    /**
     * Rotates the hues of all colors in the list by the given amount.
     * 
     * @param angle
     *            rotation angle in degrees
     * @return itself
     */
    public ColorList rotateRYB(int angle) {
        for (TColor c : colors) {
            c.rotateRYB(angle);
        }
        return this;
    }

    /**
     * @return the number of colors in the list
     */
    public int size() {
        return colors.size();
    }

    /**
     * Convenience method. Sorts the list by hue.
     * 
     * @return itself
     */
    public ColorList sort() {
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
    public ColorList sortByComparator(Comparator<ReadonlyTColor> comp,
            boolean isReversed) {
        Collections.sort(colors, comp);
        if (isReversed) {
            Collections.reverse(colors);
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
    public ColorList sortByCriteria(AccessCriteria criteria, boolean isReversed) {
        return sortByComparator(criteria, isReversed);
    }

    /**
     * Sorts the list by relative distance to each predecessor, starting with
     * the darkest color in the list.
     * 
     * @param isReversed
     *            true, if list is to be sorted in reverse.
     * @return itself
     */
    public ColorList sortByDistance(boolean isReversed) {
        return sortByDistance(new HSVDistanceProxy(), isReversed);
    }

    /**
     * Sorts the list by relative distance to each predecessor, starting with
     * the darkest color in the list.
     * 
     * @param isReversed
     *            true, if list is to be sorted in reverse.
     * @return itself
     */
    public ColorList sortByDistance(DistanceProxy proxy, boolean isReversed) {
        if (colors.size() == 0) {
            return this;
        }

        TColor root = getDarkest();

        // Remove the darkest color from the stack,
        // put it in the sorted list as starting element.
        ArrayList<TColor> stack = new ArrayList<TColor>(colors);
        stack.remove(root);
        ArrayList<TColor> sorted = new ArrayList<TColor>(colors.size());
        sorted.add(root);

        // Now find the color in the stack closest to that color.
        // Take this color from the stack and add it to the sorted list.
        // Now find the color closest to that color, etc.
        int sortedCount = 0;
        while (stack.size() > 1) {
            TColor closest = stack.get(0);
            TColor lastSorted = sorted.get(sortedCount);
            float distance = proxy.distanceBetween(closest, lastSorted);
            for (int i = stack.size() - 1; i >= 0; i--) {
                TColor c = stack.get(i);
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
        colors = sorted;
        return this;
    }

    /**
     * Sorts the list by proximity to the given target color (using RGB distance
     * metrics).
     * 
     * @see #sortByProximityTo(ReadonlyTColor, DistanceProxy, boolean)
     * @param target
     *            color
     * @param isReversed
     *            true, if reverse sorted
     * @return sorted list
     */
    public ColorList sortByProximityTo(ReadonlyTColor target, boolean isReversed) {
        return sortByProximityTo(target, new RGBDistanceProxy(), isReversed);
    }

    /**
     * Sorts the list by proximity to the given target color using the given
     * {@link DistanceProxy} implementation.
     * 
     * @param target
     *            color
     * @param proxy
     *            distance metrics
     * @param isReversed
     *            true, if reverse sorted
     * @return sorted list
     */
    public ColorList sortByProximityTo(ReadonlyTColor target,
            DistanceProxy proxy, boolean isReversed) {
        return sortByComparator(new ProximityComparator(target, proxy),
                isReversed);
    }

    /**
     * Creates an ARGB integer array of the list items.
     * 
     * @return all list colors as ARGB values
     */
    public int[] toARGBArray() {
        int[] array = new int[colors.size()];
        int i = 0;
        for (ReadonlyTColor c : colors) {
            array[i++] = c.toARGB();
        }
        return array;
    }
}
