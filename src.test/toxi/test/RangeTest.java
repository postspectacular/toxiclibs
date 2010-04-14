package toxi.test;

import junit.framework.TestCase;
import toxi.util.datatypes.BiasedFloatRange;
import toxi.util.datatypes.BiasedIntegerRange;

public class RangeTest extends TestCase {

    public void testCopy() {
        BiasedFloatRange r = new BiasedFloatRange();
        r.pickRandom();
        BiasedFloatRange c = r.copy();
        assertEquals(r.currValue, c.currValue);
        assertEquals(r.getBias(), c.getBias());
        assertEquals(r.getStandardDeviation(), c.getStandardDeviation());
        BiasedIntegerRange ri = new BiasedIntegerRange();
        ri.pickRandom();
        BiasedIntegerRange ci = ri.copy();
        assertEquals(ri.currValue, ci.currValue);
        assertEquals(ri.getBias(), ci.getBias());
        assertEquals(ri.getStandardDeviation(), ci.getStandardDeviation());
    }

}
