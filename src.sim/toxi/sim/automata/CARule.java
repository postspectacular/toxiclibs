package toxi.sim.automata;

/**
 * This interface defines the required API for a {@link CAMatrix} compatible
 * cellular automata rule implementation.
 */
public interface CARule extends MatrixEvolver {

    public int getStateCount();

    public boolean isAutoExpire();

    public boolean isTiling();

    public void randomize();

    public void setAutoExpire(boolean isAutoExpire);

    public void setStateCount(int num);

    public void setTiling(boolean state);
}
