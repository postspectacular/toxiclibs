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

    /**
     * @param amp
     *            the amp to set
     */
    public void setAmp(float amp) {
        this.amp = amp;
    }

}
