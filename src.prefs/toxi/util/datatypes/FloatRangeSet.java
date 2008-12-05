package toxi.util.datatypes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import toxi.math.MathUtils;

public class FloatRangeSet implements Iterable<FloatRange> {

	public ArrayList<FloatRange> items;
	public int currID = -1;
	public FloatRange current;

	protected Random random = new Random();

	public FloatRangeSet(ArrayList<FloatRange> items) {
		this.items = new ArrayList<FloatRange>(items);
		pickRandom();
	}

	public FloatRangeSet(FloatRange r) {
		items = new ArrayList<FloatRange>();
		items.add(r);
		current = r;
	}

	public void setRandom(Random rnd) {
		random = rnd;
	}

	public boolean contains(FloatRange obj) {
		return items.contains(obj);
	}

	public FloatRangeSet copy() {
		FloatRangeSet set = new FloatRangeSet(items);
		set.current = current;
		set.currID = currID;
		set.random = random;
		return set;
	}

	public FloatRange getCurrent() {
		return current;
	}

	public FloatRange pickRandom() {
		currID = MathUtils.random(random, items.size());
		current = items.get(currID);
		return current;
	}

	public FloatRange pickRandomUnique() {
		int size = items.size();
		if (size > 1) {
			int newID = currID;
			while (newID == currID) {
				newID = MathUtils.random(random, size);
			}
			currID = newID;
		} else {
			currID = 0;
		}
		current = items.get(currID);
		return current;
	}

	public void add(FloatRange hues) {
		items.add(hues);
	}

	public void addAll(FloatRangeSet set) {
		for (FloatRange r : set) {
			if (!items.contains(r)) {
				items.add(r);
			}
		}
	}

	public Iterator<FloatRange> iterator() {
		return items.iterator();
	}
}
