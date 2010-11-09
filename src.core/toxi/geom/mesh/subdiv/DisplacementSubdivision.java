package toxi.geom.mesh.subdiv;

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
     * @return
     */
    public DisplacementSubdivision setAmp(float amp) {
        this.amp = amp;
        return this;
    }
}
