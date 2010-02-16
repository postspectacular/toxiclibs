package toxi.sim.automata;

public interface EvolvableMatrix {

    /**
     * @return the current generation of the simulation.
     */
    public int getGeneration();

    /**
     * @return the matrix height
     */
    public int getHeight();

    /**
     * @return the simulation matrix
     */
    public int[] getMatrix();

    /**
     * @return the temporary buffer used to compute the next generation
     */
    public int[] getSwapBuffer();

    /**
     * @return the matrix width
     */
    public int getWidth();

}