package toxi.math.waves;

/**
 * Implements a constant value as waveform.
 */
public class ConstantWave extends AbstractWave {

    public ConstantWave(float value) {
        super();
        this.value = value;
    }

    public final float update() {
        return value;
    }
}
