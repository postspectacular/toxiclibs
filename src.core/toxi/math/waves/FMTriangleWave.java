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

package toxi.math.waves;

import toxi.math.MathUtils;

/**
 * Implements a frequency modulated triangular wave with its peak at PI: "/\"
 */
public class FMTriangleWave extends AbstractWave {

    public AbstractWave fmod;

    public FMTriangleWave(float phase, float freq) {
        this(phase, freq, 1, 0);
    }

    public FMTriangleWave(float phase, float freq, float amp, float offset) {
        this(phase, freq, amp, offset, new ConstantWave(0));
    }

    public FMTriangleWave(float phase, float freq, float amp, float offset,
            AbstractWave fmod) {
        super(phase, freq, amp, offset);
        this.fmod = fmod;
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.math.waves.AbstractWave#pop()
     */
    @Override
    public void pop() {
        super.pop();
        fmod.pop();
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.math.waves.AbstractWave#push()
     */
    @Override
    public void push() {
        super.push();
        fmod.push();
    }

    /**
     * Resets this wave and its modulating wave as well.
     * 
     * @see toxi.math.waves.AbstractWave#reset()
     */
    public void reset() {
        super.reset();
        fmod.reset();
    }

    @Override
    public float update() {
        value = 2 * amp * (MathUtils.abs(PI - phase) * MathUtils.INV_PI - 0.5f)
                + offset;
        cyclePhase(frequency + fmod.update());
        return value;
    }
}
