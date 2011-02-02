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

import toxi.math.MathUtils;

public abstract class AbstractScale {

    public static final int OCTAVE_RANGE = 2;

    public static final float HALFTONE_STEP = (float) Math.pow(2, 1 / 12.0);
    public static final float INV_HALFTONE = 1 / 12.0f;

    public byte[] tones;
    protected String name;

    public AbstractScale(String name, byte[] tones) {
        this.name = name;
        this.tones = tones;
    }

    public String getName() {
        return name;
    }

    public float getPitchForScaleTone(int st) {
        return getPitchForScaleTone(st, tones.length, 0);
    }

    public float getPitchForScaleTone(int semiTone, int limit, int transpose) {
        limit = MathUtils.min(limit, tones.length);
        int octave = semiTone / limit;
        semiTone %= limit;
        if (semiTone < 0) {
            semiTone += limit;
            octave--;
        }
        return (float) Math.pow(2,
                (octave * 12 + tones[semiTone] + transpose) / 12.0);
    }

    public float getPitchForSemitone(int st) {
        return (float) Math.pow(2, st / 12.0);
    }
}
