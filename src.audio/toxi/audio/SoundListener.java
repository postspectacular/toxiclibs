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

import net.java.games.joal.AL;
import toxi.geom.Vec3D;

// TODO add fluid interface and add getters with Vec3D return type
public class SoundListener extends Vec3D {

	private JOALUtil liboal;

	private float[] pos = { 0.0f, 0.0f, 0.0f };

	private float[] vel = { 0.0f, 0.0f, 0.0f };

	private float[] orient = { 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f };

	protected SoundListener(JOALUtil lib) {
		super();
		liboal = lib;
		setGain(1f);
		setPosition(pos);
		setVelocity(vel);
	}

	public void setGain(float gain) {
		liboal.getAL().alListenerf(AL.AL_GAIN, gain);
	}

	public void setPosition(float xx, float yy, float zz) {
		pos[0] = xx;
		pos[1] = yy;
		pos[2] = zz;
		setPosition(pos);
	}

	public final void setPosition(float[] pos) {
		x = pos[0];
		y = pos[1];
		z = pos[2];
		liboal.getAL().alListenerfv(AL.AL_POSITION, pos, 0);
	}

	public float[] getPosition() {
		return pos;
	}

	public void setVelocity(float xx, float yy, float zz) {
		vel[0] = xx;
		vel[1] = yy;
		vel[2] = zz;
		setVelocity(vel);
	}

	public final void setVelocity(float[] vel) {
		liboal.getAL().alListenerfv(AL.AL_VELOCITY, vel, 0);
	}

	public float[] getVelocity() {
		return vel;
	}

	public void setOrientation(float fx, float fy, float fz, float ux,
			float uy, float uz) {
		orient[0] = fx;
		orient[1] = fy;
		orient[2] = fz;
		orient[3] = ux;
		orient[4] = uy;
		orient[5] = uz;
		setOrientation(orient);
	}

	public final void setOrientation(float[] o) {
		orient = o;
		liboal.getAL().alListenerfv(AL.AL_ORIENTATION, vel, 0);
	}

	public float[] getOrientation() {
		return orient;
	}

}
