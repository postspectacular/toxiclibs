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
package toxi.audio;

import toxi.math.MathUtils;
import toxi.math.waves.AbstractWave;

import net.java.games.sound3d.Source;
import net.java.games.sound3d.Vec3f;

public class DynamicSoundSource {

	private int minDuration = 6000;

	private int maxDuration = minDuration * 6;

	private long activeDuration;

	private int minActiveTime = -1;

	private Source source;

	private float currGain = 0;

	private float maxGain = 1;

	private boolean isActive = false;

	private boolean isExpired = false;

	private long activeTime;

	private float decay;

	private boolean usePanning = false;

	private AbstractWave panning;

	private boolean isIdle = false;

	/**
	 * @param source
	 */
	public DynamicSoundSource(Source source) {
		this.source = source;
		source.setLooping(true);
		randomDecay();
		update();
	}

	/**
	 * 
	 */
	private void randomDecay() {
		decay = MathUtils.random(0.95f, 0.995f);
	}

	/**
	 * @return the maxGain
	 */
	public float getMaxGain() {
		return maxGain;
	}

	/**
	 * @param maxGain
	 *            the maxGain to set
	 */
	public void setMaxGain(float maxGain) {
		this.maxGain = maxGain;
	}

	/**
	 * @return the isActive
	 */
	public boolean isActive() {
		return isActive;
	}

	/**
	 * @param isActive
	 *            the isActive to set
	 */
	public void setActive(boolean state) {
		if (state != isActive) {
			this.isActive = state;
			System.out.println(this + ": " + isActive);
			if (isActive) {
				activeTime = System.currentTimeMillis();
				activeDuration = MathUtils.random(minDuration, maxDuration);
				isExpired = false;
				System.out.println("new duration: " + activeDuration);
			}
		}
	}

	public void update() {
		setExpired(System.currentTimeMillis() - activeTime > activeDuration);
		if (System.currentTimeMillis() - activeTime < minActiveTime
				|| (isActive && !isExpired)) {
			currGain += (maxGain - currGain) * 0.05f;
		} else {
			currGain *= decay;
		}
		source.setGain(currGain);
		if (usePanning) {
			Vec3f pos = source.getPosition();
			source.setPosition(panning.update(), pos.v2, pos.v3);
		}
	}

	/**
	 * @param b
	 */
	private void setExpired(boolean b) {
		if (b != isExpired) {
			isExpired = b;
			if (!isExpired)
				System.out.println(this + " expired");
		}
	}

	public boolean isExpired() {
		return isExpired;
	}

	/**
	 * 
	 */
	public Source getSource() {
		return source;
	}

	/**
	 * @return the usePanning
	 */
	public boolean isPanning() {
		return usePanning;
	}

	/**
	 * @param usePanning
	 *            the usePanning to set
	 */
	public void usePanning(AbstractWave panning) {
		this.usePanning = panning != null;
		this.panning = panning;
	}

	/**
	 * @return the minActiveTime
	 */
	public int getMinActiveTime() {
		return minActiveTime;
	}

	/**
	 * @param minActiveTime
	 *            the minActiveTime to set
	 */
	public void setMinActiveTime(int minActiveTime) {
		this.minActiveTime = minActiveTime;
	}

	/**
	 * 
	 */
	public void play() {
		source.play();
	}
}
