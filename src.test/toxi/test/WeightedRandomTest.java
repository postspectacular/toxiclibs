package toxi.test;

import java.util.HashMap;

import junit.framework.TestCase;
import toxi.util.datatypes.WeightedRandomSet;

public class WeightedRandomTest extends TestCase {

    private void checkDistribution(WeightedRandomSet<String> set) {
        HashMap<String, Integer> stats = new HashMap<String, Integer>();
        for (int i = 0; i < 100000; i++) {
            String id = set.getRandom();
            if (stats.get(id) == null) {
                stats.put(id, 1);
            } else {
                stats.put(id, stats.get(id) + 1);
            }
        }
        for (String id : stats.keySet()) {
            System.out.println(id + ": " + stats.get(id));
        }
    }

    public void testEmpty() {
        WeightedRandomSet<String> set = new WeightedRandomSet<String>();
        assertEquals(null, set.getRandom());
    }

    public void testRandom() {
        WeightedRandomSet<String> set = new WeightedRandomSet<String>();
        set.add("bar", 2);
        set.add("toxi", 4);
        set.add("foo", 1);
        checkDistribution(set);
    }

    public void testRemove() {
        WeightedRandomSet<String> set = new WeightedRandomSet<String>();
        set.add("foo", 2);
        set.remove("foo");
        assertEquals(0, set.getElements().size());
    }

    public void testSingle() {
        WeightedRandomSet<String> set = new WeightedRandomSet<String>();
        set.add("foo", 10);
        assertEquals("foo", set.getRandom());
    }
}
