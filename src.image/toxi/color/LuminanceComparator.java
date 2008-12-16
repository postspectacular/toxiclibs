package toxi.color;

import java.util.Comparator;

public class LuminanceComparator implements Comparator<Color> {

	public int compare(Color a, Color b) {
		float lumA = a.luminance();
		float lumB = b.luminance();
		if (lumA < lumB)
			return -1;
		if (lumA > lumB)
			return 1;
		else
			return 0;
	}

}
