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
 * Abstract wave oscillator type which needs to be subclassed to implement
 * different waveforms.
 */
public abstract class AbstractWave {

	protected float phase, orig;

	protected float freq;

	protected float amp;

	protected float offset;

	protected float value = 0;

	/**
	 * @param phase
	 */
	public AbstractWave(float phase) {
		this(phase, 1, 1, 0);
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
		this.phase = this.orig = phase;
		this.freq = freq;
		this.amp = amp;
		this.offset = offset;
	}

	public void setAmp(float amp) {
		this.amp = amp;
	}

	public float getAmp() {
		return amp;
	}

	public abstract float update();

	public float getValue() {
		return value;
	}

	public float getPhase() {
		return phase;
	}

	public void setPhase(float phase) {
		this.phase = this.orig = phase;
	}

	public void reset() {
		phase = orig;
	}

	/**
	 * Returns the wave's frequency.
	 * 
	 * @return frequency, default is 1.
	 */
	public float getFrequency() {
		return freq;
	}

	/**
	 * 
	 * @param freq
	 */
	public void setFrequency(float freq) {
		this.freq = freq;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(this.getClass().getName()).append(" phase: ").append(phase);
		sb.append(" freq: ").append(freq);
		sb.append(" amp: ").append(amp);
		return sb.toString();
	}
}