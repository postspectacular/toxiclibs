package toxi.color;

import java.util.Comparator;

public class RGBComparator implements Comparator {

	private final int component;

	public RGBComparator(int comp) {
		component = comp;
	}

	public int compare(Object a, Object b) {
		Color ca = (Color) a;
		Color cb = (Color) b;
		if (ca.rgb[component] < cb.rgb[component])
			return -1;
		if (ca.rgb[component] > cb.rgb[component])
			return 1;
		else
			return 0;
	}

}
