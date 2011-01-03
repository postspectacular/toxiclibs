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
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301, USA
 */

package toxi.sim.erosion;

/**
 * Abstract parent class for various 2D erosion simulations, implemented as
 * sub-classes.
 */
public abstract class ErosionFunction {

    protected float[] elevation;
    protected int width;
    protected int height;

    protected float[] d = new float[9];
    protected float[] h = new float[9];
    protected int[] off;

    /**
     * Destructively erodes the given array.
     * 
     * @param elevation
     * @param width
     * @param height
     */
    public void erode(float[] elevation, int width, int height) {
        this.elevation = elevation;
        this.width = width;
        this.height = height;
        off =
                new int[] { -width - 1, -width, -width + 1, -1, 0, 1,
                        width - 1, width, width + 1 };
        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                erodeAt(x, y);
            }
        }
    }

    public abstract void erodeAt(int x, int y);
}
