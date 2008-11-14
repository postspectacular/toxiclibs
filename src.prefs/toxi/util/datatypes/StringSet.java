package toxi.util.datatypes;

import java.util.Random;

import toxi.math.MathUtils;

public class StringSet {

	public String[] items;
	public String current;
	public int currID = -1;

	private Random random = new Random();

	public StringSet(String[] items) {
		this.items = items;
		pickRandom();
	}

	public void setRandom(Random rnd) {
		random = rnd;
	}

	public String pickRandom() {
		currID = MathUtils.random(random, items.length);
		current = items[currID];
		return current;
	}

	public String pickRandomUnique() {
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

	public String getCurrent() {
		return current;
	}

	public boolean contains(String value) {
		for (int i = 0; i < items.length; i++) {
			if (items[i].equals(value))
				return true;
		}
		return false;
	}
}
