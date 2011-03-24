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

import toxi.math.MathUtils;
import toxi.math.SinCosLUT;

/**
 * This class provides a simple IIR filter implementation with one of lowpass,
 * highpass or bandpass characteristics. The class can filter individual samples
 * or entire signal buffers. The filter function always has this form:
 * 
 * <pre>
 * y = 1 / a0 * (b0 * x0 + b1 * x1 + b2 * x2 - a1 * q1 - a2 * q2)
 * </pre>
 * 
 * http://en.wikipedia.org/wiki/Infinite_impulse_response
 */
public class IIRFilter {

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
    public IIRFilter(Type type, float sampleRate) {
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
            final float yn = a0
                    * (b0 * in[i] + b1 * in1 + b2 * in2 - a1 * out1 - a2 * out2);
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

    public IIRFilter clear() {
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
        final float yn = a0
                * (b0 * in + b1 * in1 + b2 * in2 - a1 * out1 - a2 * out2);
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
            final float yn = a0
                    * (b0 * in[i] + b1 * in1 + b2 * in2 - a1 * out1 - a2 * out2);
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
    public IIRFilter init(final float freq, float q) {
        float theta = sampleRateRadians * freq;
        float si = sinTable.sin(theta);
        float co = sinTable.cos(theta);
        alpha = si / q;
        a0 = 1f / (1 + alpha);
        a1 = -2 * co;
        a2 = 1 - alpha;
        switch (type) {
            case LOWPASS:
                b0 = b2 = (1f - co) * 0.5f;
                b1 = 1f - co;
                break;
            case HIGHPASS:
                b0 = b2 = (1f + co) * 0.5f;
                b1 = -(1f + co);
                break;
            case BANDPASS:
                b0 = si * 0.5f;
                b1 = 0;
                b2 = -si * 0.5f;
                break;
        }
        return this;
    }

    /**
     * @param decay
     *            the decay to set
     * @return itself
     */
    public IIRFilter setDecay(float decay) {
        this.decay = decay;
        return this;
    }
}
