/**
 * 
 */
package toxi.color.theory;

import toxi.color.Color;
import toxi.color.ColorList;
import toxi.math.MathUtils;

/**
 * @author toxi
 * 
 */
public class MonochromeTheoryStrategy implements ColorTheoryStrategy {

	public static final String NAME = "monochrome";

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * toxi.color.ColorTheoryStrategy#createListFromColour(toxi.color.Color)
	 */
	public ColorList createListFromColour(Color src) {

		ColorList colors = new ColorList(src.copy());

		Color c = src.copy();
		c.setBrightness(wrap(c.brightness(), 0.5f, 0.2f, 0.3f));
		c.setSaturation(wrap(c.saturation(), 0.3f, 0.1f, 0.3f));
		colors.add(c);

		c = src.copy();
		c.setBrightness(wrap(c.brightness(), 0.2f, 0.2f, 0.6f));
		colors.add(c);

		c = src.copy();
		c.setBrightness(MathUtils.max(0.2f, c.brightness()
				+ (1 - c.brightness()) * 0.2f));
		c.setSaturation(wrap(c.saturation(), 0.3f, 0.1f, 0.3f));
		colors.add(c);

		c = src.copy();
		c.setBrightness(wrap(c.brightness(), 0.5f, 0.2f, 0.3f));
		colors.add(c);

		return colors;
	}

	private static final float wrap(float x, float min, float threshold,
			float plus) {
		if (x - min < threshold)
			return x + plus;
		else
			return x - min;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see toxi.color.ColorTheoryStrategy#getName()
	 */
	public String getName() {
		return NAME;
	}

	public String toString() {
		return NAME;
	}
}
