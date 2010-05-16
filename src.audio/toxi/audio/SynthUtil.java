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

    public static byte[] floatArrayTo16bitPCMStereo(float[] raw) {
        byte[] sample = new byte[raw.length * 2];
        for (int i = 0, j = 0; i < raw.length; i += 2) {
            int left = (int) (raw[i] * 0x8000);
            int right = (int) (raw[i + 1] * 0x8000);
            sample[j++] = (byte) (left & 0xff);
            sample[j++] = (byte) (left >> 8 & 0xff);
            sample[j++] = (byte) (right & 0xff);
            sample[j++] = (byte) (right >> 8 & 0xff);
        }
        return sample;
    }

    public static AudioBuffer floatArrayTo16bitStereoBuffer(JOALUtil audioSys,
            float[] raw, int rate) {
        byte[] pcm = floatArrayTo16bitPCMStereo(raw);
        AudioBuffer buffer = audioSys.generateBuffers(1)[0];
        buffer.configure(ByteBuffer.wrap(pcm), AudioBuffer.Format.STEREO16,
                rate);
        return buffer;
    }
}
