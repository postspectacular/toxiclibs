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

import java.nio.ByteBuffer;

import net.java.games.joal.AL;

/**
 * A wrapper for the actual sample data byte buffer in memory. The buffer can be
 * queried to find out more information about the underlying audio data.
 * 
 * @author toxi
 * 
 */
public class AudioBuffer {

	public final static int FORMAT_MONO8 = AL.AL_FORMAT_MONO8;
	public final static int FORMAT_MONO16 = AL.AL_FORMAT_MONO16;
	public final static int FORMAT_STEREO8 = AL.AL_FORMAT_STEREO8;
	public final static int FORMAT_STEREO16 = AL.AL_FORMAT_STEREO16;

	protected final AL al;
	protected ByteBuffer data;

	protected final int bufferID;

	protected int[] alResult = new int[1];

	public AudioBuffer(AL al, int bufferID) {
		this.bufferID = bufferID;
		this.al = al;
	}

	/**
	 * Configure the audio buffer
	 * 
	 * @param data
	 *            the raw audio data
	 * @param format
	 *            the format of the data: <code>FORMAT_MONO8, FORMAT_MONO16,
	 *        FORMAT_STEREO8</code> and <code>FORMAT_STEREO16</code>
	 * @param freq
	 *            the frequency of the data
	 */
	public void configure(ByteBuffer data, int format, int freq) {
		this.data = data;
		al.alBufferData(bufferID, format, data, data.capacity(), freq);
	}

	/**
	 * Delete this buffer, and free its resources.
	 */
	public void delete() {
		data = null;
		al.alDeleteBuffers(1, new int[] { bufferID }, 0);
	}

	/**
	 * Get the bit-depth of the data, (8 or 16)
	 * 
	 * @return the bit-depth of the data
	 */
	public int getBitDepth() {
		al.alGetBufferi(bufferID, AL.AL_BITS, alResult, 0);
		return alResult[0];
	}

	/**
	 * Get the number of channels of the data (1-Mono, 2-Stereo)
	 * 
	 * @return the number of audio channels.
	 */
	public int getNumChannels() {
		al.alGetBufferi(bufferID, AL.AL_CHANNELS, alResult, 0);

		return alResult[0];
	}

	/**
	 * Gets the raw data contained in this buffer.
	 * 
	 * @return the raw buffer data.
	 */
	public ByteBuffer getData() {
		return data;
	}

	/**
	 * Gets the audio frequency of the data contained in this buffer.
	 * 
	 * @return the frequency of the data
	 */
	public int getFrequency() {
		al.alGetBufferi(bufferID, AL.AL_FREQUENCY, alResult, 0);
		return alResult[0];
	}

	/**
	 * Gets the size (in bytes) of the raw data contained in this buffer.
	 * 
	 * @return the size of the data.
	 */
	public int getByteSize() {
		al.alGetBufferi(bufferID, AL.AL_SIZE, alResult, 0);
		return alResult[0];
	}

	/**
	 * Gets the size (in samples) of the raw data contained in this buffer.
	 * 
	 * @return sample size.
	 */
	public int getSampleSize() {
		return getByteSize() * 8 / getBitDepth();
	}

	/**
	 * Returns the OpenAL reference ID for this buffer.
	 * 
	 * @return id
	 */
	public int getID() {
		return bufferID;
	}
}
