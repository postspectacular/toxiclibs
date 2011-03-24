/*
 *   __               .__       .__  ._____.           
 * _/  |_  _______  __|__| ____ |  | |__\_ |__   ______
 * \   __\/  _ \  \/  /  |/ ___\|  | |  || __ \ /  ___/
 *  |  | (  <_> >    <|  \  \___|  |_|  || \_\ \\___ \ 
 *  |__|  \____/__/\_ \__|\___  >____/__||___  /____  >
 *                   \/       \/             \/     \/ 
 *
 * Copyright (c) 2006-2011 Karsten Schmidt
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
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301, USA
 */

package toxi.audio;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import net.java.games.joal.AL;

/**
 * A wrapper for the actual sample data byte buffer in memory. The buffer can be
 * queried to find out more information about the underlying audio data.
 */
public class AudioBuffer {

    /**
     * Format descriptor
     */
    public enum Format {
        MONO8(AL.AL_FORMAT_MONO8, 8, 1), MONO16(AL.AL_FORMAT_MONO16, 16, 1), STEREO8(
                AL.AL_FORMAT_STEREO8, 8, 2), STEREO16(AL.AL_FORMAT_STEREO16,
                16, 2);

        public static Format getForID(int id) {
            Format format = null;
            for (Format f : values()) {
                if (f.id == id) {
                    format = f;
                    break;
                }
            }
            return format;
        }

        private final int id;
        private final int numBits;
        private final int numChannels;

        private Format(int id, int bits, int channels) {
            this.id = id;
            this.numBits = bits;
            this.numChannels = channels;
        }

        public int getID() {
            return id;
        }

        /**
         * @return the numBits
         */
        public int getNumBits() {
            return numBits;
        }

        /**
         * @return the numChannels
         */
        public int getNumChannels() {
            return numChannels;
        }
    }

    protected final AL al;
    protected ByteBuffer data;

    protected final int id;

    protected final int[] alResult = new int[1];
    protected Format format;

    public AudioBuffer(AL al, int bufferID) {
        this.id = bufferID;
        this.al = al;
    }

    public AudioBuffer configure(ByteBuffer data, Format format, int freq) {
        return configure(data, format.getID(), freq);
    }

    /**
     * Configure the audio buffer
     * 
     * @param data
     *            the raw audio data
     * @param format
     *            the internal format ID of the audio data
     * @param freq
     *            the frequency of the data
     */
    public AudioBuffer configure(ByteBuffer data, int format, int freq) {
        this.data = data;
        this.format = Format.getForID(format);
        al.alBufferData(id, format, data, data.capacity(), freq);
        return this;
    }

    public boolean convertUlawToPCM(boolean isAlaw) {
        byte[] ulaw = new byte[getByteSize()];
        data.rewind();
        data.get(ulaw);
        byte[] pcm = new byte[ulaw.length * 2];
        ByteArrayInputStream bin = new ByteArrayInputStream(ulaw);
        try {
            new DecompressInputStream(bin, isAlaw).read(pcm);
            configure(ByteBuffer.wrap(pcm), Format.MONO16, getFrequency());
            return true;
        } catch (IOException e) {
        }
        return false;
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
        // al.alGetBufferi(id, AL.AL_BITS, alResult, 0);
        // return alResult[0];
        return format.getNumBits();
    }

    /**
     * Gets the size (in bytes) of the raw data contained in this buffer.
     * 
     * @return the size of the data.
     */
    public final int getByteSize() {
        // al.alGetBufferi(id, AL.AL_SIZE, alResult, 0);
        // return alResult[0];
        return data.capacity();
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
        // al.alGetBufferi(id, AL.AL_CHANNELS, alResult, 0);
        // return alResult[0];
        return format.getNumChannels();
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
