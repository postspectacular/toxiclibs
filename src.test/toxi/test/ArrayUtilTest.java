package toxi.test;

import java.util.Random;

import junit.framework.TestCase;
import toxi.util.datatypes.ArrayUtil;
import toxi.util.datatypes.GenericSet;
import toxi.util.datatypes.IntegerRange;

public class ArrayUtilTest extends TestCase {

    private void dumpArray(Integer[] range) {
        for (int i : range) {
            System.out.print(i + ",");
        }
        System.out.println("");
    }

    public void testGenericSet() {
        GenericSet<Integer> set = new GenericSet<Integer>(1, 2, 23, 42, 81);
        assertEquals(5, set.getItems().size());
        int prev = 0;
        for (int i = 0; i < set.size(); i++) {
            int val = set.pickRandomUnique();
            assertTrue(val != prev);
            prev = val;
        }
    }

    public void testShuffle() {
        Integer[] range = new IntegerRange(0, 10).toArray();
        dumpArray(range);
        ArrayUtil.shuffle(range, new Random(23));
        dumpArray(range);
    }
}
