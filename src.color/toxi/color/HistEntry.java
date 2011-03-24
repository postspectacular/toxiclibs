/*
 *   __               .__       .__  ._____.           
 * _/  |_  _______  __|__| ____ |  | |__\_ |__   ______
 * \   __\/  _ \  \/  /  |/ ___\|  | |  || __ \ /  ___/
 *  |  | (  <_> >    <|  \  \___|  |_|  || \_\ \\___ \ 
 *  |__|  \____/__/\_ \__|\___  >____/__||___  /____  >
 *                   \/       \/             \/     \/ 
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

/**
 * This class constitutes a single histogram entry, a coupling of color &
 * frequency. Also implements a comparator to sort histogram entries based on
 * frequency.
 */
public class HistEntry implements Comparable<HistEntry> {

    protected float freq;
    protected TColor col;

    public HistEntry(TColor c) {
        col = c;
        freq = 1;
    }

    /**
     * Comparator implementation used to sort entries by frequency (descending
     * order).
     */
    public int compareTo(HistEntry e) {
        return (int) (e.freq - freq);
    }

    /**
     * Returns the color of this histogram entry.
     * 
     * @return color
     */
    public TColor getColor() {
        return col;
    }

    /**
     * Returns the normalized frequency associated with this entry. Values will
     * always be within the 0.0 ... 1.0 range.
     * 
     * @return frequency
     */
    public float getFrequency() {
        return freq;
    }
}