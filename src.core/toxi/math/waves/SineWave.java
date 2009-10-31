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

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Standard Sine wave at fixed frequency and values normalized to the given
 * amplitude.
 */
@XmlRootElement
public class SineWave extends AbstractWave {

	public SineWave() {
		super();
	}

	/**
	 * @param phase
	 *            starting phase
	 * @param freq
	 *            in radians (not Hertz)
	 */
	public SineWave(float phase, float freq) {
		super(phase, freq);
	}

	/**
	 * @param phase
	 *            starting phase
	 * @param freq
	 *            in radians (not Hertz)
	 * @param amp
	 *            amplitude factor
	 * @param offset
	 *            centre oscillation value
	 */
	public SineWave(float phase, float freq, float amp, float offset) {
		super(phase, freq, amp, offset);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see toxi.math.waves.AbstractWave#update()
	 */
	public float update() {
		value = (float) (Math.sin(phase) * amp) + offset;
		cyclePhase(frequency);
		return value;
	}
}
