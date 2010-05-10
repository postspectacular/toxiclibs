package toxi.audio.scale;

import toxi.math.MathUtils;

public abstract class AbstractScale {

    public static final int OCTAVE_RANGE = 2;

    public static final float HALFTONE_STEP = (float) Math.pow(2, 1 / 12.0);
    public static final float INV_HALFTONE = 1 / 12.0f;

    public byte[] tones;
    protected String name;

    public AbstractScale(String name, byte[] tones) {
        this.name = name;
        this.tones = tones;
    }

    public String getName() {
        return name;
    }

    public float getPitchForScaleTone(int st) {
        return getPitchForScaleTone(st, tones.length, 0);
    }

    public float getPitchForScaleTone(int semiTone, int limit, int transpose) {
        limit = MathUtils.min(limit, tones.length);
        int octave = semiTone / limit;
        semiTone %= limit;
        if (semiTone < 0) {
            semiTone += limit;
            octave--;
        }
        return (float) Math.pow(2,
                (octave * 12 + tones[semiTone] + transpose) / 12.0);
    }

    public float getPitchForSemitone(int st) {
        return (float) Math.pow(2, st / 12.0);
    }
}
