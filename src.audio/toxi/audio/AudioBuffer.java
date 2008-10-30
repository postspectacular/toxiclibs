package toxi.audio;

import java.nio.ByteBuffer;

import net.java.games.joal.AL;

public class AudioBuffer {

	public final static int FORMAT_MONO8 = AL.AL_FORMAT_MONO8;
	public final static int FORMAT_MONO16 = AL.AL_FORMAT_MONO16;
	public final static int FORMAT_STEREO8 = AL.AL_FORMAT_STEREO8;
	public final static int FORMAT_STEREO16 = AL.AL_FORMAT_STEREO16;

	protected final int bufferID;

	protected ByteBuffer data;

	protected boolean isConfigured = false;

	protected final AL al;

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
		if (!isConfigured) {
			this.data = data;
			al.alBufferData(bufferID, format, data, data.capacity(), freq);
		}
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
		int[] i = new int[1];
		al.alGetBufferi(bufferID, AL.AL_BITS, i, 0);

		return i[0];
	}

	/**
	 * Get the number of channels of the data (1-Mono, 2-Stereo)
	 * 
	 * @return the number of audio channels.
	 */
	public int getNumChannels() {
		int[] i = new int[1];
		al.alGetBufferi(bufferID, AL.AL_CHANNELS, i, 0);

		return i[0];
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
		int[] i = new int[1];
		al.alGetBufferi(bufferID, AL.AL_FREQUENCY, i, 0);

		return i[0];
	}

	/**
	 * Gets the size (in bytes) of the raw data contained in this buffer.
	 * 
	 * @return the size of the data.
	 */
	public int getByteSize() {
		int[] i = new int[1];
		al.alGetBufferi(bufferID, AL.AL_SIZE, i, 0);

		return i[0];
	}

	/**
	 * Gets the size (in samples) of the raw data contained in this buffer.
	 * 
	 * @return sample size.
	 */
	public int getSampleSize() {
		return getByteSize() / (getBitDepth() / 8);
	}

	public int getID() {
		return bufferID;
	}
}
