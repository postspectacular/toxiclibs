package toxi.util.datatypes;

/**
 * This class encapsulates a single element within a {@link WeightedRandomSet}.
 * 
 * @param <T>
 */
public class WeightedRandomEntry<T> implements
        Comparable<WeightedRandomEntry<T>> {

    protected T item;
    protected int weight;

    public WeightedRandomEntry(T item, int weight) {
        this.item = item;
        this.weight = weight;
    }

    public int compareTo(WeightedRandomEntry<T> e) {
        return (e.weight - weight);
    }

    public String toString() {
        return item.toString() + ": " + weight;
    }
}