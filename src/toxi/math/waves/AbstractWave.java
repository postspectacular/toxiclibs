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

public abstract class AbstractWave {

	protected float theta, orig;

	protected float freq;

	protected float amp = 1;

	protected float offset = 0;

	protected float value = 0;

	/**
	 * @param theta2
	 * @param delta2
	 */
	public AbstractWave(float theta, float freq) {
		this.theta = this.orig = theta;
		this.freq = freq;
	}

	public AbstractWave(float theta, float freq, float amp, float offset) {
		this(theta, freq);
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

	public float getTheta() {
		return theta;
	}

	public void setTheta(float theta) {
		this.theta = this.orig = theta;
	}

	public void resetTheta() {
		theta = orig;
	}

	/**
	 * Returns the wave's frequency.
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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer sb=new StringBuffer();
		sb.append(this.getClass().getName()).append(" theta: ").append(theta);
		sb.append(" freq: ").append(freq);
		sb.append(" amp: ").append(amp);
		return sb.toString();
	}
}