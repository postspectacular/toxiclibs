package toxi.color;

import java.util.Comparator;

public class HSVComparator implements Comparator<Color> {

	private final int component;

	public HSVComparator(int comp) {
		component = comp;
	}

	public int compare(Color a, Color b) {
		if (a.hsv[component] < b.hsv[component])
			return -1;
		if (a.hsv[component] > b.hsv[component])
			return 1;
		else
			return 0;
	}

}
