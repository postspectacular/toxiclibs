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
