/**
 * 
 */
package toxi.color.theory;

import toxi.color.Color;
import toxi.color.ColorList;

/**
 * @author toxi
 * 
 */
public class TetradTheoryStrategy implements ColorTheoryStrategy {

	public static final String NAME = "tetrad";

	private int theta = 90;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * toxi.color.ColorTheoryStrategy#createListFromColour(toxi.color.Color)
	 */
	public ColorList createListFromColour(Color src) {
		ColorList colors = new ColorList(src.copy());
		Color c = src.getRotatedRYB(theta);
		if (c.brightness() < 0.5)
			c.lighten(0.2f);
		else
			c.darken(0.2f);
		colors.add(c);

		c = src.getRotatedRYB(theta * 2);
		if (c.brightness() < 0.5)
			c.lighten(0.1f);
		else
			c.darken(0.1f);
		colors.add(c);

		colors.add(src.getRotatedRYB(theta * 3).lighten(0.1f));
		return colors;
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
