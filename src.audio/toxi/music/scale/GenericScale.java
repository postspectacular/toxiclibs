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

package toxi.music.scale;

public class GenericScale extends AbstractScale {

    public GenericScale(String name, byte[] tones) {
        super(name, tones);
    }

    /**
     * Implements a scale based on a 12 bit integer value, where all 1 bits are
     * used as possible tones for this scale. This way any possible scale can be
     * implemented and easily experimented with.
     * 
     * @param name
     * @param seed
     */
    public GenericScale(String name, int seed) {
        super(name, new byte[0]);
        byte[] tmp = new byte[12];
        int j = 11;
        for (byte i = 11; i >= 0; i--) {
            if ((seed & 1) != 0) {
                tmp[j--] = i;
            }
            seed >>>= 1;
        }
        tones = new byte[11 - j];
        System.arraycopy(tmp, j + 1, tones, 0, tones.length);
    }
}
