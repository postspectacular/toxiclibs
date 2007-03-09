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

package toxi.util.datatypes;

public class AMFMSineWave extends AbstractWave {

	private AbstractWave fmod;
	private AbstractWave amod;

	/**
	 * @param theta
	 * @param freq
	 */
	public AMFMSineWave(float theta, float delta, AbstractWave fmod, AbstractWave amod) {
		super(theta, delta);
		this.amod = amod;
		this.fmod = fmod;
	}

	/**
	 * @param theta
	 * @param freq
	 * @param amp
	 * @param offset
	 */
	public AMFMSineWave(float theta, float delta, float offset, AbstractWave fmod, AbstractWave amod) {
		super(theta, delta, 1, offset);
		this.amod = amod;
		this.fmod = fmod;
	}

	/* (non-Javadoc)
	 * @see toxi.math.datatypes.AbstractWave#update()
	 */
	@Override
	public float update() {
		value = amod.update() * (float)Math.sin(theta * fmod.update()) + offset;
		theta+=freq;
		return value;
	}

	public void setFMod(AbstractWave fmod) {
		this.fmod = fmod;
	}
	
	public void setAMod(AbstractWave amod) {
		this.amod = amod;
	}
	
	public AbstractWave getFMod() {
		return fmod;
	}
	
	public AbstractWave getAMod() {
		return amod;
	}
	
	public void reset() {
		super.reset();
		fmod.reset();
		amod.reset();
	}
}
