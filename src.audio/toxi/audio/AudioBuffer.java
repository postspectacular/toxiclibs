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

	/**
	 * Format descriptor for 8bit mono samples
	 */
	public final static int FORMAT_MONO8 = AL.AL_FORMAT_MONO8;
	/**
	 * Format descriptor for 16bit mono samples
	 */
	public final static int FORMAT_MONO16 = AL.AL_FORMAT_MONO16;
	/**
	 * Format descriptor for 8bit stereo samples
	 */
	public final static int FORMAT_STEREO8 = AL.AL_FORMAT_STEREO8;
	/**
	 * Format descriptor for 16bit stereo samples
	 */
	public final static int FORMAT_STEREO16 = AL.AL_FORMAT_STEREO16;

	protected final AL al;
	protected ByteBuffer data;

	protected final int id;

	protected final int[] alResult = new int[1];
	private int format;

	public AudioBuffer(AL al, int bufferID) {
		this.id = bufferID;
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
	public AudioBuffer configure(ByteBuffer data, int format, int freq) {
		this.data = data;
		this.format = format;
		al.alBufferData(id, format, data, data.capacity(), freq);
		return this;
	}

	/**
	 * Deletes this buffer, and frees its resources.
	 * 
	 * @return true, if removed successfully
	 */
	public boolean delete() {
		data = null;
		al.alDeleteBuffers(1, new int[] { id }, 0);
		return al.alGetError() == AL.AL_NO_ERROR;
	}

	/**
	 * Get the bit-depth of the data, (8 or 16)
	 * 
	 * @return the bit-depth of the data
	 */
	public final int getBitDepth() {
		al.alGetBufferi(id, AL.AL_BITS, alResult, 0);
		return alResult[0];
	}

	/**
	 * Gets the size (in bytes) of the raw data contained in this buffer.
	 * 
	 * @return the size of the data.
	 */
	public final int getByteSize() {
		al.alGetBufferi(id, AL.AL_SIZE, alResult, 0);
		return alResult[0];
	}

	/**
	 * Gets the raw data contained in this buffer.
	 * 
	 * @return the raw buffer data.
	 */
	public final ByteBuffer getData() {
		return data;
	}

	/**
	 * Gets the audio frequency of the data contained in this buffer.
	 * 
	 * @return the frequency of the data
	 */
	public final int getFrequency() {
		al.alGetBufferi(id, AL.AL_FREQUENCY, alResult, 0);
		return alResult[0];
	}

	/**
	 * Returns the OpenAL reference ID for this buffer.
	 * 
	 * @return buffer id
	 */
	public final int getID() {
		return id;
	}

	/**
	 * Get the number of channels of the data (1-Mono, 2-Stereo)
	 * 
	 * @return the number of audio channels.
	 */
	public final int getNumChannels() {
		al.alGetBufferi(id, AL.AL_CHANNELS, alResult, 0);

		return alResult[0];
	}

	/**
	 * Gets the size (in samples) of the raw data contained in this buffer.
	 * 
	 * @return sample size.
	 */
	public final int getSampleSize() {
		return getByteSize() * 8 / getBitDepth() / getNumChannels();
	}

	public String toString() {
		return "AudioBuffer: id=" + id + " format=" + format;
	}
}
