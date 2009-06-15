/*
 * Copyright (c) 2006-2008 Karsten Schmidt
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

/**
 * A wrapper for {@link AudioBuffer}s and similar to the built in JOAL
 * net.java.games.sound3d.Source, though less restrictive. The class extends
 * {@link Vec3D} and so can be used to position the sound in 3D space (provided
 * the underlying audio hardware does support 3D audio). Unfortunately due to
 * OpenAL's limitations only mono samples can be positioned in that way. Stereo
 * samples will can only manipulated in terms of gain/volume.
 * 
 * <p>
 * If the position of an AudioSource is changed via the public x,y,z vector
 * components, the <code>updatePosition()</code> method needs to be called
 * afterwards in order to reflect the changes in the OpenAL context.
 * 
 * @author toxi
 * 
 */
public class AudioSource extends Vec3D {

	protected AL al;

	protected AudioBuffer buffer;

	protected int id;
	protected int size;

	protected float[] position = { 0.0f, 0.0f, 0.0f };
	protected float[] velocity = { 0.0f, 0.0f, 0.0f };
	protected float[] direction = { 0.0f, 0.0f, 0.0f };

	protected int[] alResult = new int[1];

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

	public void delete() {
		stop();
		al.alDeleteSources(1, new int[] { id }, 0);
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
		al.alGetSourcei(id, AL.AL_BUFFERS_PROCESSED, alResult, 0);
		return alResult[0];
	}

	public float[] getDirection() {
		return direction;
	}

	public int getID() {
		return id;
	}

	public int getOffset() {
		al.alGetSourcei(id, AL.AL_SAMPLE_OFFSET, alResult, 0);
		return alResult[0];
	}

	public float[] getPosition() {
		return position;
	}

	public float[] getVelocity() {
		return velocity;
	}

	public boolean isLooping() {
		return isLooping;
	}

	public int length() {
		return size;
	}

	public void play() {
		if (buffer != null) {
			al.alSourcePlay(id);
		}
	}

	public void rewind() {
		if (buffer != null) {
			al.alSourceRewind(id);
		}
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
		direction[0] = xx;
		direction[1] = yy;
		direction[2] = zz;
		setDirection(direction);
	}

	private final void setDirection(float[] dir) {
		if (dir.length >= 3) {
			direction[0] = dir[0];
			direction[1] = dir[1];
			direction[2] = dir[2];
			al.alSourcefv(id, AL.AL_DIRECTION, direction, 0);
		}
	}

	public void setDirection(Vec3D dir) {
		direction = dir.toArray();
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
		position[0] = xx;
		position[1] = yy;
		position[2] = zz;
		setPosition(position);
	}

	private final void setPosition(float[] p) {
		position = p;
		x = position[0];
		y = position[1];
		z = position[2];
		al.alSourcefv(id, AL.AL_POSITION, position, 0);
	}

	public final void setPosition(Vec3D p) {
		setPosition(p.x, p.y, p.z);
	}

	public void setReferenceDistance(float d) {
		al.alSourcef(id, AL.AL_REFERENCE_DISTANCE, d);
	}

	public void setVelocity(float xx, float yy, float zz) {
		velocity[0] = xx;
		velocity[1] = yy;
		velocity[2] = zz;
		setVelocity(velocity);
	}

	private final void setVelocity(float[] v) {
		velocity = v;
		al.alSourcefv(id, AL.AL_VELOCITY, velocity, 0);
	}

	public final void setVelocity(Vec3D p) {
		setVelocity(p.x, p.y, p.z);
	}

	public void stop() {
		al.alSourceStop(id);
	}

	public final void updatePosition() {
		setPosition(x, y, z);
	}
}