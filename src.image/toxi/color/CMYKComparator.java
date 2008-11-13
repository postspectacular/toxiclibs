package toxi.color;

import java.util.Comparator;

public class CMYKComparator implements Comparator {

	private final int component;

	public CMYKComparator(int comp) {
		component = comp;
	}

	public int compare(Object a, Object b) {
		Color ca = (Color) a;
		Color cb = (Color) b;
		if (ca.cmyk[component] < cb.cmyk[component])
			return -1;
		if (ca.cmyk[component] > cb.cmyk[component])
			return 1;
		else
			return 0;
	}

}
