package toxi.color;

import java.util.Comparator;

public class HSVComparator implements Comparator {

	private final int component;

	public HSVComparator(int comp) {
		component = comp;
	}

	public int compare(Object a, Object b) {
		Color ca = (Color) a;
		Color cb = (Color) b;
		if (ca.hsv[component] < cb.hsv[component])
			return -1;
		if (ca.hsv[component] > cb.hsv[component])
			return 1;
		else
			return 0;
	}

}
