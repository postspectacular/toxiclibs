package toxi.sim.automata;

public interface CARule {

    public void apply(CAMatrix m);

    public int getStateCount();

    public boolean isTiling();

    public void setStateCount(int num);

    public void setTiling(boolean state);
}
