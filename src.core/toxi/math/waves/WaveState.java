package toxi.math.waves;

public class WaveState {

    public float phase;
    public float frequency;
    public float amp;
    public float offset;

    public WaveState(float phase, float frequency, float amp, float offset) {
        this.phase = phase;
        this.frequency = frequency;
        this.amp = amp;
        this.offset = offset;
    }
}
