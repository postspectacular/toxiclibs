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

import java.util.Stack;

/**
 * Abstract wave oscillator type which needs to be subclassed to implement
 * different waveforms. Please note that the frequency unit is radians, but
 * conversion methods to & from Hertz ({@link #hertzToRadians(float, float)})
 * are included in this base class.
 */
public abstract class AbstractWave {

    public static final float PI = 3.14159265358979323846f;
    public static final float TWO_PI = 2 * PI;

    /**
     * Converts a frequency in Hertz into radians.
     * 
     * @param hz
     *            frequency to convert (in Hz)
     * @param sampleRate
     *            sampling rate in Hz (equals period length @ 1 Hz)
     * @return frequency in radians
     */
    public static final float hertzToRadians(float hz, float sampleRate) {
        return hz / sampleRate * TWO_PI;
    }

    /**
     * Converts a frequency from radians to Hertz.
     * 
     * @param f
     *            frequency in radians
     * @param sampleRate
     *            sampling rate in Hz (equals period length @ 1 Hz)
     * @return freq in Hz
     */
    public static final float radiansToHertz(float f, float sampleRate) {
        return f / TWO_PI * sampleRate;
    }

    /**
     * Current wave phase
     */
    public float phase;
    public float frequency;
    public float amp;
    public float offset;
    public float value;

    protected float origPhase;
    protected Stack<WaveState> stateStack;

    public AbstractWave() {
    }

    /**
     * @param phase
     */
    public AbstractWave(float phase) {
        this(phase, 0, 1, 0);
    }

    /**
     * 
     * @param phase
     * @param freq
     */
    public AbstractWave(float phase, float freq) {
        this(phase, freq, 1, 0);
    }

    /**
     * @param phase
     * @param freq
     * @param amp
     * @param offset
     */
    public AbstractWave(float phase, float freq, float amp, float offset) {
        setPhase(phase);
        this.frequency = freq;
        this.amp = amp;
        this.offset = offset;
    }

    /**
     * Ensures phase remains in the 0...TWO_PI interval.
     * 
     * @return current phase
     */
    public final float cyclePhase() {
        phase %= TWO_PI;
        if (phase < 0) {
            phase += TWO_PI;
        }
        return phase;
    }

    /**
     * Progresses phase and ensures it remains in the 0...TWO_PI interval.
     * 
     * @param freq
     *            normalized progress frequency
     * @return update phase value
     */
    public final float cyclePhase(float freq) {
        phase = (phase + freq) % TWO_PI;
        if (phase < 0) {
            phase += TWO_PI;
        }
        return phase;
    }

    public void pop() {
        if (stateStack == null || (stateStack != null && stateStack.empty())) {
            throw new IllegalStateException("no wave states on stack");
        }
        WaveState s = stateStack.pop();
        phase = s.phase;
        frequency = s.frequency;
        amp = s.amp;
        offset = s.offset;
    }

    public void push() {
        if (stateStack == null) {
            stateStack = new Stack<WaveState>();
        }
        stateStack.push(new WaveState(phase, frequency, amp, offset));
    }

    /**
     * Resets the wave phase to the last set phase value (via
     * {@link #setPhase(float)}.
     */
    public void reset() {
        phase = origPhase;
    }

    /**
     * Starts the wave from a new phase. The new phase position will also be
     * used for any later call to {{@link #reset()}
     * 
     * @param phase
     *            new phase
     */
    public void setPhase(float phase) {
        this.phase = phase;
        cyclePhase();
        this.origPhase = phase;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(this.getClass().getName()).append(" phase: ").append(phase);
        sb.append(" frequency: ").append(frequency);
        sb.append(" amp: ").append(amp);
        sb.append(" offset: ").append(offset);
        return sb.toString();
    }

    /**
     * Updates the wave and returns new value. Implementing classes should
     * manually ensure the phase remains in the 0...TWO_PI interval or by
     * calling {@link #cyclePhase()}.
     * 
     * @return current (newly calculated) wave value
     */
    public abstract float update();
}