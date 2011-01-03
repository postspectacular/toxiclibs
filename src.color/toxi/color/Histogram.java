/*
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
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

package toxi.color;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Histogram implements Iterable<HistEntry> {

    /**
     * Creates a new histogram of color samples taken from the given ARGB array
     * in order to identify the primary colors in the image.
     * 
     * @param pixels
     * @param numSamples
     * @param tolerance
     * @param blendCols
     * @return histogram instance
     */
    public static Histogram newFromARGBArray(int[] pixels, int numSamples,
            float tolerance, boolean blendCols) {
        Histogram h =
                new Histogram(ColorList.createFromARGBArray(pixels, numSamples,
                        false));
        h.compute(tolerance, blendCols);
        return h;
    }

    protected ColorList palette;

    protected ArrayList<HistEntry> entries;

    public Histogram(ColorList palette) {
        this.palette = palette;
    }

    /**
     * @param tolerance
     *            color tolerance used to merge similar colors (based on RGB
     *            distance)
     * @param blendCols
     *            switch to enable color blending of binned colors
     * @return sorted histogram as List of HistEntry
     */
    List<HistEntry> compute(float tolerance, boolean blendCols) {
        entries = new ArrayList<HistEntry>(palette.size() / 4);
        float maxFreq = 1;
        for (TColor c : palette) {
            HistEntry existing = null;
            for (HistEntry e : entries) {
                if (e.col.distanceToRGB(c) < tolerance) {
                    if (blendCols) {
                        e.col.blend(c, 1f / (e.freq + 1));
                    }
                    existing = e;
                    break;
                }
            }
            if (existing != null) {
                existing.freq++;
                if (existing.freq > maxFreq) {
                    maxFreq = existing.freq;
                }
            } else {
                entries.add(new HistEntry(c));
            }
        }
        Collections.sort(entries);
        maxFreq = 1f / palette.size();
        for (HistEntry e : entries) {
            e.freq *= maxFreq;
        }
        return entries;
    }

    /**
     * @return the entries
     */
    public List<HistEntry> getEntries() {
        return entries;
    }

    /**
     * @return the palette
     */
    public ColorList getPalette() {
        return palette;
    }

    public Iterator<HistEntry> iterator() {
        return entries.iterator();
    }

    /**
     * @param palette
     *            the palette to set
     */
    public void setPalette(ColorList palette) {
        this.palette = palette;
    }

}
