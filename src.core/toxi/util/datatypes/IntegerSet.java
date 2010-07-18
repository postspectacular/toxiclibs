package toxi.util.datatypes;

import java.util.Random;

import toxi.math.MathUtils;

public class IntegerSet {

    public int[] items;
    public int currID = -1;
    public int current;

    private Random random = new Random();

    public IntegerSet(int[] items) {
        this.items = items;
        pickRandom();
    }

    public IntegerSet(Integer... items) {
        this.items = new int[items.length];
        for (int i = 0; i < items.length; i++) {
            this.items[i] = items[i];
        }
        pickRandom();
    }

    public boolean contains(int value) {
        for (int i = 0; i < items.length; i++) {
            if (items[i] == value) {
                return true;
            }
        }
        return false;
    }

    public int getCurrent() {
        return current;
    }

    public int pickRandom() {
        currID = MathUtils.random(random, items.length);
        current = items[currID];
        return current;
    }

    public int pickRandomUnique() {
        if (items.length > 1) {
            int newID = currID;
            while (newID == currID) {
                newID = MathUtils.random(random, items.length);
            }
            currID = newID;
        } else {
            currID = 0;
        }
        current = items[currID];
        return current;
    }

    public IntegerSet seed(long seed) {
        random.setSeed(seed);
        return this;
    }

    public void setRandom(Random rnd) {
        random = rnd;
    }
}
