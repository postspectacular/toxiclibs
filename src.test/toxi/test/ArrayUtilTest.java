package toxi.test;

import java.util.Random;

import junit.framework.TestCase;
import toxi.util.datatypes.ArrayUtil;
import toxi.util.datatypes.IntegerRange;

public class ArrayUtilTest extends TestCase {

    private void dumpArray(Integer[] range) {
        for (int i : range) {
            System.out.print(i + ",");
        }
        System.out.println("");
    }

    public void testShuffle() {
        Integer[] range = new IntegerRange(0, 10).toArray();
        dumpArray(range);
        ArrayUtil.shuffle(range, new Random(23));
        dumpArray(range);
    }
}
