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

package toxi.sim.erosion;

/**
 * Applies thermal erosion to the given elevation data. Based on the description
 * here: http://www.m3xbox.com/GPU_blog/?p=37
 */
public class ThermalErosion extends ErosionFunction {

    @Override
    public void erodeAt(int x, int y) {
        int idx = y * width + x;
        float minD = Float.MAX_VALUE;
        float sumD = 0;
        int n = 0;
        for (int i = 0; i < 9; i++) {
            h[i] = elevation[idx + off[i]];
            d[i] = elevation[idx] - h[i];
            if (d[i] > 0) {
                if (d[i] < minD) {
                    minD = d[i];
                }
                sumD += d[i];
                n++;
            }
        }
        float hOut = n * minD / (n + 1.0f);
        elevation[idx] = elevation[idx] - hOut;
        for (int i = 0; i < 9; i++) {
            if (d[i] > 0) {
                elevation[idx + off[i]] = h[i] + hOut * (d[i] / sumD);
            }
        }
    }

    public String toString() {
        return getClass().getName();
    }
}
