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
import toxi.math.MathUtils;

public class AudioSource extends Vec3D {

	protected AL al;

	protected AudioBuffer buffer;

	protected int id;
	protected int size;

	protected float[] pos = { 0.0f, 0.0f, 0.0f };
	protected float[] vel = { 0.0f, 0.0f, 0.0f };
	protected float[] dir = { 0.0f, 0.0f, 0.0f };

	protected boolean isLooping;

	public AudioSource(AL al, int id) {
		this(al, id, null);
	}

	public AudioSource(AL al, int id, AudioBuffer buf) {
		super();
		this.al = al;
		this.id = id;
		setBuffer(buf);
	}

	/**
	 * Gets the buffer associated with this source.
	 * 
	 * @return the buffer associated with this source
	 */
	public AudioBuffer getBuffer() {
		return buffer;
	}

	/**
	 * Gets the number of buffers already processed on this source.
	 * 
	 * @return the number of buffers already processed on this source.
	 */
	public int getBuffersProcessed() {
		int[] result = new int[1];
		al.alGetSourcei(id, AL.AL_BUFFERS_PROCESSED, result, 0);
		return result[0];
	}

	public float[] getDirection() {
		return dir;
	}

	public int getOffset() {
		int[] t = new int[1];
		al.alGetSourcei(id, AL.AL_SAMPLE_OFFSET, t, 0);
		return t[0];
	}

	public float[] getPosition() {
		return pos;
	}

	public float[] getVelocity() {
		return vel;
	}

	public boolean isLooping() {
		return isLooping;
	}

	public int length() {
		return size;
	}

	public void play() {
		al.alSourcePlay(id);
	}

	public void rewind() {
		al.alSourceRewind(id);
	}

	/**
	 * Sets the buffer associated with this source.
	 * 
	 * @param buffer
	 *            the buffer associated with this source
	 */
	public void setBuffer(AudioBuffer buffer) {
		this.buffer = buffer;
		if (buffer != null) {
			al.alSourcei(id, AL.AL_BUFFER, buffer.getID());
			this.size = buffer.getSampleSize();
		} else {
			size = 0;
		}
	}

	public void setDirection(float xx, float yy, float zz) {
		dir[0] = xx;
		dir[1] = yy;
		dir[2] = zz;
		setDirection(dir);
	}

	private final void setDirection(float[] d) {
		dir = d;
		al.alSourcefv(id, AL.AL_DIRECTION, dir, 0);
	}

	public void setGain(float gain) {
		al.alSourcef(id, AL.AL_GAIN, gain);
	}

	public void setLooping(boolean state) {
		isLooping = state;
		al.alSourcei(id, AL.AL_LOOPING, (state ? AL.AL_TRUE : AL.AL_FALSE));
	}

	public void setOffset(int off) {
		off = MathUtils.clip(off, 0, size - 1);
		al.alSourcei(id, AL.AL_SAMPLE_OFFSET, off);
	}

	public void setPitch(float pitch) {
		al.alSourcef(id, AL.AL_PITCH, pitch);
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
		al.alSourcefv(id, AL.AL_POSITION, pos, 0);
	}

	public final void setPosition(Vec3D p) {
		setPosition(p.x, p.y, p.z);
	}

	public void setReferenceDistance(float d) {
		al.alSourcef(id, AL.AL_REFERENCE_DISTANCE, d);
	}

	public void setVelocity(float xx, float yy, float zz) {
		vel[0] = xx;
		vel[1] = yy;
		vel[2] = zz;
		setVelocity(vel);
	}

	private final void setVelocity(float[] v) {
		vel = v;
		al.alSourcefv(id, AL.AL_VELOCITY, vel, 0);
	}

	public final void setVelocity(Vec3D p) {
		setVelocity(p.x, p.y, p.z);
	}

	public void stop() {
		al.alSourceStop(id);
	}
}