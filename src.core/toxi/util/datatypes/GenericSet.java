package toxi.util.datatypes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Random;

import toxi.math.MathUtils;

public class GenericSet<T> implements Iterable<T> {

	protected ArrayList<T> items;
	protected int currID = -1;
	protected T current;

	protected Random random = new Random();

	public GenericSet(Collection<T> items) {
		this.items = new ArrayList<T>(items);
		pickRandom();
	}

	public GenericSet(T obj) {
		items = new ArrayList<T>();
		items.add(obj);
		current = obj;
	}

	public void add(T obj) {
		items.add(obj);
	}

	public void addAll(Collection<T> coll) {
		items.addAll(coll);
	}

	public void clear() {
		items.clear();
	}

	public boolean contains(T obj) {
		return items.contains(obj);
	}

	public GenericSet<T> copy() {
		GenericSet<T> set = new GenericSet<T>(items);
		set.current = current;
		set.currID = currID;
		set.random = random;
		return set;
	}

	public T getCurrent() {
		return current;
	}

	public ArrayList<T> getItems() {
		return items;
	}

	public Iterator<T> iterator() {
		return items.iterator();
	}

	public T pickRandom() {
		currID = MathUtils.random(random, items.size());
		current = items.get(currID);
		return current;
	}

	public T pickRandomUnique() {
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

	public void setRandom(Random rnd) {
		random = rnd;
	}
}
