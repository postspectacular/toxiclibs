/* 
 * Copyright (c) 2006, 2007 Karsten Schmidt
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

package toxi.math.waves;

/**
 * <p>
 * Frequency modulated bandwidth unlimited pure sawtooth wave. Uses a secondary
 * wave to modulate the frequency of the main wave.
 * </p>
 * 
 * <p>
 * <strong>Note:</strong> You must NEVER call the update() method on the
 * modulating wave.
 * </p>
 */
public class FMSawtoothWave extends AbstractWave {

    public AbstractWave fmod;

    public FMSawtoothWave(float phase, float freq, AbstractWave fmod) {
        super(phase, freq);
        this.fmod = fmod;
    }

    /**
     * Convenience constructor to create a non frequency modulated sawtooth.
     * 
     * @param phase
     * @param freq
     *            base frequency (in radians)
     * @param amp
     * @param offset
     */
    public FMSawtoothWave(float phase, float freq, float amp, float offset) {
        this(phase, freq, amp, offset, new ConstantWave(0));
    }

    public FMSawtoothWave(float phase, float freq, float amp, float offset,
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

    /**
     * Progresses the wave and updates the result value. You must NEVER call the
     * update() method on the modulating wave since this is handled
     * automatically by this method.
     * 
     * @see toxi.math.waves.AbstractWave#update()
     */
    public float update() {
        value = ((phase / TWO_PI) * 2 - 1) * amp + offset;
        cyclePhase(frequency + fmod.update());
        return value;
    }

}
