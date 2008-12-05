package toxi.color;

import java.util.Comparator;

public class CMYKComparator implements Comparator<Color> {

	private final int component;

	public CMYKComparator(int comp) {
		component = comp;
	}

	public int compare(Color a, Color b) {
		if (a.cmyk[component] < b.cmyk[component])
			return -1;
		if (a.cmyk[component] > b.cmyk[component])
			return 1;
		else
			return 0;
	}

}
