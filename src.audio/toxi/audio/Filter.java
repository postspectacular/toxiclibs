package toxi.audio;

import toxi.math.MathUtils;
import toxi.math.SinCosLUT;

public class Filter {

	public enum Type {
		LOWPASS, HIGHPASS, BANDPASS;
	}

	protected static final SinCosLUT sinTable = new SinCosLUT(0.08789f);

	protected final Type type;

	protected float b0, b1, b2, a0, a1, a2, alpha;
	protected float out1, out2, in1, in2;

	protected final float sampleRate;
	protected final float sampleRateRadians;

	protected float decay = 0.999f;

	public Filter(Type type, float sampleRate) {
		this.type = type;
		this.sampleRate = sampleRate;
		sampleRateRadians = MathUtils.TWO_PI / sampleRate;
	}

	/**
	 * Just calculates the amplitude, doesn't actually process the signal.
	 * 
	 * @param ins
	 * @return
	 */
	public float calculateAmplitude(float[] ins) {
		float amp = 0;
		for (int i = 0; i < ins.length; i++) {
			final float yn = (b0 * ins[i] + b1 * in1 + b2 * in2 - a1 * out1 - a2
					* out2)
					/ a0;
			in2 = in1;
			in1 = ins[i];
			out2 = out1;
			out1 = yn;

			if (amp < MathUtils.abs(yn)) {
				amp = yn;
			} else {
				amp *= decay;
			}
		}
		return amp;
	}

	public Filter clear() {
		in1 = in2 = 0;
		out1 = out2 = 0;
		return this;
	};

	public float filter(float in0) {
		final float yn = (b0 * in0 + b1 * in1 + b2 * in2 - a1 * out1 - a2
				* out2)
				/ a0;
		in2 = in1;
		in1 = in0;
		out2 = out1;
		out1 = yn;
		return yn;
	}

	/**
	 * Filters a whole buffer of signal.
	 */
	public float filter(float[] ins) {
		float amp = 0;
		for (int i = 0; i < ins.length; i++) {
			final float yn = (b0 * ins[i] + b1 * in1 + b2 * in2 - a1 * out1 - a2
					* out2)
					/ a0;
			in2 = in1;
			in1 = ins[i];
			out2 = out1;
			out1 = yn;
			ins[i] = yn;

			if (amp < MathUtils.abs(ins[i])) {
				amp = ins[i];
			} else {
				amp *= decay;
			}
		}
		return amp;
	}

	public Filter init(final float frequency, float q) {
		float omega = sampleRateRadians * frequency;
		float tsin = sinTable.sin(omega);
		float tcos = sinTable.cos(omega);

		alpha = tsin / q;
		a0 = 1f + alpha;

		switch (type) {
		case LOWPASS:
			b0 = b2 = (1.0f - tcos) * 0.5f;
			b1 = 1.0f - tcos;
			a1 = -2.0f * tcos;
			a2 = 1.0f - alpha;
			break;
		case HIGHPASS:
			b0 = b2 = (1.0f + tcos) * 0.5f;
			b1 = -(1.0f + tcos);
			a1 = -2.0f * tcos;
			a2 = 1.0f - alpha;
			break;
		case BANDPASS:
			b0 = tsin * 0.5f;
			b1 = 0.0f;
			b2 = -tsin / 2;
			a1 = -2.0f * tcos;
			a2 = 1.0f - alpha;
			break;
		}
		return this;
	}
}
