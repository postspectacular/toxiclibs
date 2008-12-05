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
public class SplitComplementaryStrategy implements ColorTheoryStrategy {

	public static final String NAME = "splitComplementary";

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * toxi.color.ColorTheoryStrategy#createListFromColour(toxi.color.Color)
	 */
	public ColorList createListFromColour(Color src) {
		Color clr = src.copy();
		ColorList colors = new ColorList(clr);
		clr = clr.getComplement();
		colors.add(clr.getRotatedRYB(-30).lighten(0.1f));
		colors.add(clr.getRotatedRYB(30).lighten(0.1f));
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
