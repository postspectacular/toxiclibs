package toxi.math.waves;

/**
 * Implements a constant value as waveform.
 * @author toxi
 */
public class ConstantWave extends AbstractWave {

	public ConstantWave(float value) {
		super(0);
		this.value = value;
	}

	public final float update() {
		return value;
	}
}
