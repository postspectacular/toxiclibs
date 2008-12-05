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
public class TriadTheoryStrategy implements ColorTheoryStrategy {

	public static final String NAME = "triad";

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * toxi.color.ColorTheoryStrategy#createListFromColour(toxi.color.Color)
	 */
	public ColorList createListFromColour(Color src) {
		ColorList colors = new ColorList(src.copy());
		colors.add(src.getRotatedRYB(120).lighten(0.1f));
		colors.add(src.getRotatedRYB(-120).lighten(0.1f));
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
