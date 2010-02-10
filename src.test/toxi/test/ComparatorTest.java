package toxi.test;

import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

public class ComparatorTest {

    static class NonComparator implements Comparator<Float> {

        public int compare(Float a, Float b) {
            return (int) (a - b);
        }

    }

    public static void main(String[] args) {
        SortedSet<Float> set = new TreeSet<Float>(new NonComparator());
        set.add(23f);
        set.add(5f);
        set.add(99f);
        set.add(42f);
        set.add(1f);
        for (Float f : set) {
            System.out.println(f);
        }
    }
}
