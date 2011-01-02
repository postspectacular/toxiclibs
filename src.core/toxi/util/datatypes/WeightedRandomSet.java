package toxi.util.datatypes;

import java.util.ArrayList;
import java.util.List;

import toxi.math.MathUtils;

/**
 * This class provides a generic random-weight distribution of arbitary objects.
 * Add elements with their weight to the set and then use the
 * {@link #getRandom()} method to retrieve objects. The frequency of returned
 * elements is based on their relative weight. This makes it easy to provide
 * biased preferences.
 */
public class WeightedRandomSet<T> {

    protected List<WeightedRandomEntry<T>> elements =
            new ArrayList<WeightedRandomEntry<T>>();

    protected int totalWeight;

    /**
     * Add a new element of type T to the set.
     * 
     * @param item
     * @param weight
     * @return itself
     */
    public WeightedRandomSet<T> add(T item, int weight) {
        WeightedRandomEntry<T> e = new WeightedRandomEntry<T>(item, weight);
        int num = elements.size();
        boolean isInserted = false;
        if (num > 0) {
            for (int i = 0; i < num; i++) {
                if (weight < elements.get(i).weight) {
                    elements.add(i, e);
                    isInserted = true;
                    break;
                }
            }
        }
        if (!isInserted) {
            elements.add(e);
        }
        totalWeight += weight;
        return this;
    }

    /**
     * @return the elements
     */
    public List<WeightedRandomEntry<T>> getElements() {
        return elements;
    }

    /**
     * Returns a randomly picked element from the set. The frequency of
     * occurance depends on the relative weight of each item.
     * 
     * @return picked element
     */
    public T getRandom() {
        int rnd = MathUtils.random(totalWeight);
        T choice = null;
        int sum = totalWeight;
        for (WeightedRandomEntry<T> e : elements) {
            sum -= e.weight;
            if (sum <= rnd) {
                choice = e.item;
                break;
            }
        }
        return choice;
    }

    /**
     * Removes the given item from the set.
     * 
     * @param item
     */
    public void remove(T item) {
        for (WeightedRandomEntry<T> e : elements) {
            if (e.item.equals(item)) {
                elements.remove(e);
                totalWeight -= e.weight;
                return;
            }
        }
    }
}
