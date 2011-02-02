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
 * Compares 2 colors by their luminance values.
 */
public class LuminanceAccessor extends AccessCriteria {

    public int compare(ReadonlyTColor a, ReadonlyTColor b) {
        float lumA = a.luminance();
        float lumB = b.luminance();
        if (lumA < lumB) {
            return -1;
        }
        if (lumA > lumB) {
            return 1;
        } else {
            return 0;
        }
    }

    public float getComponentValueFor(ReadonlyTColor col) {
        return col.luminance();
    }

    /**
     * The setter for this accessor is not doing anything, since the luminance
     * of a color is a coputed value depending on 3 color channels.
     * 
     * @see toxi.color.AccessCriteria#setComponentValueFor(toxi.color.TColor,
     *      float)
     */
    public void setComponentValueFor(TColor col, float value) {

    }
}
