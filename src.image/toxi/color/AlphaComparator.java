package toxi.color;

import java.util.Comparator;

public class AlphaComparator implements Comparator {

	public int compare(Object a, Object b) {
		Color ca = (Color) a;
		Color cb = (Color) b;
		if (ca.alpha < cb.alpha)
			return -1;
		if (ca.alpha > cb.alpha)
			return 1;
		else
			return 0;
	}

}
