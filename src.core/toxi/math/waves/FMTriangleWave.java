package toxi.math.waves;

import toxi.math.MathUtils;

/**
 * Implements a frequency modulated triangular wave with its peak at PI: "/\"
 */
public class FMTriangleWave extends AbstractWave {

    public AbstractWave fmod;

    public FMTriangleWave(float phase, float freq) {
        this(phase, freq, 1, 0);
    }

    public FMTriangleWave(float phase, float freq, float amp, float offset) {
        this(phase, freq, amp, offset, new ConstantWave(0));
    }

    public FMTriangleWave(float phase, float freq, float amp, float offset,
            AbstractWave fmod) {
        super(phase, freq, amp, offset);
        this.fmod = fmod;
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.math.waves.AbstractWave#pop()
     */
    @Override
    public void pop() {
        super.pop();
        fmod.pop();
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.math.waves.AbstractWave#push()
     */
    @Override
    public void push() {
        super.push();
        fmod.push();
    }

    /**
     * Resets this wave and its modulating wave as well.
     * 
     * @see toxi.math.waves.AbstractWave#reset()
     */
    public void reset() {
        super.reset();
        fmod.reset();
    }

    @Override
    public float update() {
        value =
                2 * amp * (MathUtils.abs(PI - phase) * MathUtils.INV_PI - 0.5f)
                        + offset;
        cyclePhase(frequency + fmod.update());
        return value;
    }
}
