package toxi.audio;

import toxi.math.MathUtils;
import toxi.math.SinCosLUT;

public class FIRFilter {

    public enum Type {
        LOWPASS, HIGHPASS, BANDPASS;
    }

    protected static final SinCosLUT sinTable = new SinCosLUT(0.05f);

    protected final Type type;

    protected float b0, b1, b2, a0, a1, a2, alpha;
    protected float out1, out2, in1, in2;

    protected final float sampleRate;
    protected final float sampleRateRadians;

    protected float decay = 0.999f;

    /**
     * @param type
     * @param sampleRate
     */
    public FIRFilter(Type type, float sampleRate) {
        this.type = type;
        this.sampleRate = sampleRate;
        sampleRateRadians = MathUtils.TWO_PI / sampleRate;
    }

    /**
     * Just calculates the amplitude of the filtered signal, but doesn't
     * actually apply the filter.
     * 
     * @param in
     * @return amplitude
     */
    public float calculateAmplitude(float[] in) {
        float amp = 0;
        for (int i = 0; i < in.length; i++) {
            final float yn =
                    (b0 * in[i] + b1 * in1 + b2 * in2 - a1 * out1 - a2 * out2)
                            / a0;
            in2 = in1;
            in1 = in[i];
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

    public FIRFilter clear() {
        in1 = in2 = 0;
        out1 = out2 = 0;
        return this;
    };

    /**
     * Applies filter to a single sample value.
     * 
     * @param in
     * @return filtered sample
     */
    public float filter(float in) {
        final float yn =
                (b0 * in + b1 * in1 + b2 * in2 - a1 * out1 - a2 * out2) / a0;
        in2 = in1;
        in1 = in;
        out2 = out1;
        out1 = yn;
        return yn;
    }

    /**
     * Destructively filters a the given signal buffer. The original samples are
     * overwritten.
     * 
     * @param in
     * @return amplitude of filtered signal
     */
    public float filter(float[] in) {
        float amp = 0;
        for (int i = 0; i < in.length; i++) {
            final float yn =
                    (b0 * in[i] + b1 * in1 + b2 * in2 - a1 * out1 - a2 * out2)
                            / a0;
            in2 = in1;
            in1 = in[i];
            out2 = out1;
            out1 = yn;
            in[i] = yn;

            if (amp < MathUtils.abs(in[i])) {
                amp = in[i];
            } else {
                amp *= decay;
            }
        }
        return amp;
    }

    /**
     * @return the decay
     */
    public float getDecay() {
        return decay;
    }

    /**
     * Initializes the filter to the given cutoff frequency and Q (resonance)
     * settings. This function needs to be called at least once before the
     * filter can be used.
     * 
     * @param freq
     * @param q
     * @return itself
     */
    public FIRFilter init(final float freq, float q) {
        float theta = sampleRateRadians * freq;
        float si = sinTable.sin(theta);
        float co = sinTable.cos(theta);
        alpha = si / q;
        a0 = 1f + alpha;
        switch (type) {
            case LOWPASS:
                b0 = b2 = (1.0f - co) * 0.5f;
                b1 = 1.0f - co;
                a1 = -2.0f * co;
                a2 = 1.0f - alpha;
                break;
            case HIGHPASS:
                b0 = b2 = (1.0f + co) * 0.5f;
                b1 = -(1.0f + co);
                a1 = -2.0f * co;
                a2 = 1.0f - alpha;
                break;
            case BANDPASS:
                b0 = si * 0.5f;
                b1 = 0.0f;
                b2 = -si / 2;
                a1 = -2.0f * co;
                a2 = 1.0f - alpha;
                break;
        }
        return this;
    }

    /**
     * @param decay
     *            the decay to set
     * @return itself
     */
    public FIRFilter setDecay(float decay) {
        this.decay = decay;
        return this;
    }
}
