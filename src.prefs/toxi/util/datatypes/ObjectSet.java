package toxi.util.datatypes;

import java.util.ArrayList;
import java.util.Random;

import toxi.math.MathUtils;

public class ObjectSet {

	public ArrayList items;
	public int currID = -1;
	public Object current;

	protected Random random = new Random();

	public ObjectSet(ArrayList items) {
		this.items = new ArrayList(items);
		pickRandom();
	}

	public ObjectSet(Object obj) {
		items = new ArrayList();
		items.add(obj);
		current = obj;
	}

	public void setRandom(Random rnd) {
		random = rnd;
	}

	public Object pickRandom() {
		currID = MathUtils.random(random, items.size());
		current = items.get(currID);
		return current;
	}

	public Object pickRandomUnique() {
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

	public Object getCurrent() {
		return current;
	}

	public boolean contains(Object obj) {
		return items.contains(obj);
	}

	public ObjectSet copy() {
		ObjectSet set = new ObjectSet(items);
		set.current = current;
		set.currID = currID;
		set.random = random;
		return set;
	}
}
