package toxi.geom.mesh.subdiv;

/**
 * Abstract parent class for all displacement subdivision strategies. It adds
 * common support for the displacement amplification value, which subclasses can
 * utilize.
 */
public abstract class DisplacementSubdivision extends SubdivisionStrategy {

    protected float amp;

    public DisplacementSubdivision(float amp) {
        this.amp = amp;
    }

    /**
     * @return the amp
     */
    public float getAmp() {
        return amp;
    }

    public DisplacementSubdivision invertAmp() {
        this.amp *= -1;
        return this;
    }

    public DisplacementSubdivision scaleAmp(float scale) {
        this.amp *= scale;
        return this;
    }

    /**
     * @param amp
     *            the amp to set
     * @return itself
     */
    public DisplacementSubdivision setAmp(float amp) {
        this.amp = amp;
        return this;
    }
}
