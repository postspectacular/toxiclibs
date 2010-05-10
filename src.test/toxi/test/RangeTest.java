package toxi.test;

import junit.framework.TestCase;
import toxi.util.datatypes.BiasedFloatRange;
import toxi.util.datatypes.BiasedIntegerRange;
import toxi.util.datatypes.FloatRange;

public class RangeTest extends TestCase {

    private void dumpArray(Float[] range) {
        for (float i : range) {
            System.out.print(i + ",");
        }
        System.out.println("");
    }

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

    public void testRangeArray() {
        Float[] r = new FloatRange(0, 10).toArray(0.1f);
        dumpArray(r);
    }
}
