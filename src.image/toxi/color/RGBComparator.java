package toxi.color;

import java.util.Comparator;

public class RGBComparator implements Comparator<Color> {

	private final int component;

	public RGBComparator(int comp) {
		component = comp;
	}

	public int compare(Color a, Color b) {
		if (a.rgb[component] < b.rgb[component])
			return -1;
		if (a.rgb[component] > b.rgb[component])
			return 1;
		else
			return 0;
	}

}
