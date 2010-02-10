package toxi.sim.automata;

public abstract class AbstractCARule {

    protected boolean[] surviveRules;
    protected boolean[] birthRules;
    protected int stateCount;

    public abstract void apply(CAMatrix m);
}
