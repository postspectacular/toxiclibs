package toxi.color;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class ColorTheme {

	public ArrayList<ColorRange> ranges = new ArrayList<ColorRange>();

	public ColorTheme(String descriptor) {
		StringTokenizer st = new StringTokenizer(descriptor, " ,");
		while (st.hasMoreTokens()) {
			String item = st.nextToken();
			if (ColorRange.isPresetName(item)) {
				ranges.add(ColorRange.getPresetForName(item));
			} else if (ColorHue.getForName(item) != null) {
				ranges.add(new ColorRange(ColorHue.getForName(item)));
			}
		}
	}
}
