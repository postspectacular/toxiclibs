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

import java.nio.ByteBuffer;

/**
 * This class provides static conversion tools for translating normalized
 * floating point wave data into 16bit PCM.
 */
public class SynthUtil {

    public static AudioBuffer floatArrayTo16bitBuffer(JOALUtil audioSys,
            float[] raw, int rate) {
        byte[] pcm = floatArrayTo16bitPCM(raw);
        AudioBuffer buffer = audioSys.generateBuffers(1)[0];
        buffer.configure(ByteBuffer.wrap(pcm), AudioBuffer.Format.MONO16, rate);
        return buffer;
    }

    public static byte[] floatArrayTo16bitPCM(float[] raw) {
        byte[] sample = new byte[raw.length * 2];
        for (int i = 0, j = 0; i < raw.length; i++) {
            int pcm = (int) (raw[i] * 0x8000);
            sample[j++] = (byte) (pcm & 0xff);
            sample[j++] = (byte) (pcm >> 8 & 0xff);
        }
        return sample;
    }

    public static AudioBuffer floatArrayTo16bitStereoBuffer(JOALUtil audioSys,
            float[] raw, int rate) {
        byte[] pcm = floatArrayTo16bitPCM(raw);
        AudioBuffer buffer = audioSys.generateBuffers(1)[0];
        buffer.configure(ByteBuffer.wrap(pcm), AudioBuffer.Format.STEREO16,
                rate);
        return buffer;
    }

    public static AudioBuffer floatArrayTo8bitBuffer(JOALUtil audioSys,
            float[] raw, int rate) {
        byte[] pcm = floatArrayTo8bitPCM(raw);
        AudioBuffer buffer = audioSys.generateBuffers(1)[0];
        buffer.configure(ByteBuffer.wrap(pcm), AudioBuffer.Format.MONO8, rate);
        return buffer;
    }

    public static byte[] floatArrayTo8bitPCM(float[] raw) {
        byte[] sample = new byte[raw.length];
        for (int i = 0; i < raw.length; i++) {
            sample[i] = (byte) (raw[i] * 0x7f + 0x80);
        }
        return sample;
    }

    public static AudioBuffer floatArrayTo8bitStereoBuffer(JOALUtil audioSys,
            float[] raw, int rate) {
        byte[] pcm = floatArrayTo8bitPCM(raw);
        AudioBuffer buffer = audioSys.generateBuffers(1)[0];
        buffer.configure(ByteBuffer.wrap(pcm), AudioBuffer.Format.STEREO8, rate);
        return buffer;
    }

    /**
     * Merges the two given mono arrays into an interleaved stereo array in
     * left-right order.
     * 
     * @param left
     * @param right
     * @return stereo array
     */
    public static float[] joinMonoFloatArrays(float[] left, float[] right) {
        if (left.length != right.length) {
            throw new IllegalArgumentException(
                    "left & right channels need to be of equal size");
        }
        float[] stereo = new float[left.length * 2];
        for (int i = 0, j = 0; i < left.length; i++, j += 2) {
            stereo[j] = left[i];
            stereo[j + 1] = right[i];
        }
        return stereo;
    }
}
