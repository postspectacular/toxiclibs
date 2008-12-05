package toxi.color;

import java.util.Comparator;

public class AlphaComparator implements Comparator<Color> {

	public int compare(Color a, Color b) {
		if (a.alpha < b.alpha)
			return -1;
		if (a.alpha > b.alpha)
			return 1;
		else
			return 0;
	}

}
