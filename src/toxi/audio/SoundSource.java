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

import toxi.geom.Vec3D;
import net.java.games.joal.AL;

public class SoundSource extends Vec3D {

	private LibOAL liboal;

	private int id;

	private int index;

	private int size;

	private float[] pos = { 0.0f, 0.0f, 0.0f };

	private float[] vel = { 0.0f, 0.0f, 0.0f };

	private float[] dir = { 0.0f, 0.0f, 0.0f };

	private boolean isLooping;

	protected SoundSource(LibOAL lib, int i, int idx, int s) {
		super();
		liboal = lib;
		id = i;
		index = idx;
		size = s;
		setPitch(1f);
		setGain(1f);
		setPosition(pos);
		setVelocity(vel);
		setDirection(dir);
		setLooping(true);
	}

	public void setGain(float gain) {
		liboal.getAL().alSourcef(id, AL.AL_GAIN, gain);
	}

	public void setPitch(float pitch) {
		liboal.getAL().alSourcef(id, AL.AL_PITCH, pitch);
	}

	public void setPosition(float xx, float yy, float zz) {
		pos[0] = xx;
		pos[1] = yy;
		pos[2] = zz;
		setPosition(pos);
	}

	private final void setPosition(float[] p) {
		pos = p;
		x = pos[0];
		y = pos[1];
		z = pos[2];
		liboal.getAL().alSourcefv(id, AL.AL_POSITION, pos, 0);
	}

	public final void setPosition(Vec3D p) {
		setPosition(p.x,p.y,p.z);
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

	private final void setVelocity(float[] v) {
		vel = v;
		liboal.getAL().alSourcefv(id, AL.AL_VELOCITY, vel, 0);
	}

	public final void setVelocity(Vec3D p) {
		setVelocity(p.x,p.y,p.z);
	}

	public float[] getVelocity() {
		return vel;
	}

	public void setDirection(float xx, float yy, float zz) {
		dir[0] = xx;
		dir[1] = yy;
		dir[2] = zz;
		setDirection(dir);
	}

	private final void setDirection(float[] d) {
		dir = d;
		liboal.getAL().alSourcefv(id, AL.AL_DIRECTION, dir, 0);
	}

	public float[] getDirection() {
		return dir;
	}
	
	public void setLooping(boolean state) {
		isLooping = state;
		liboal.getAL().alSourcei(id, AL.AL_LOOPING,
				(state ? AL.AL_TRUE : AL.AL_FALSE));
	}

	public void play() {
		liboal.getAL().alSourcePlay(id);
	}

	public void stop() {
		liboal.getAL().alSourceStop(id);
	}

	public void rewind() {
		liboal.getAL().alSourceRewind(id);
	}


	public void setReferenceDistance(float d) {
		liboal.getAL().alSourcef(id, AL.AL_REFERENCE_DISTANCE, d);
	}

	public int getOffset() {
		int[] t = new int[1];
		liboal.getAL().alGetSourcei(id, AL.AL_BYTE_OFFSET, t, 0);
		return t[0];
	}

	public int length() {
		return size;
	}

	public void setOffset(int off) {
		if (off >= 0 && off < size) {
			liboal.getAL().alSourcei(id, AL.AL_BYTE_OFFSET, off);
		}
	}
}