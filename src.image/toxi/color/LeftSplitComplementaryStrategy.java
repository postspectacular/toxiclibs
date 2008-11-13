/**
 * 
 */
package toxi.color;

/**
 * @author toxi
 * 
 */
public class LeftSplitComplementaryStrategy implements ColorTheoryStrategy {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * toxi.color.ColorTheoryStrategy#createListFromColour(toxi.color.Color)
	 */
	public static final String NAME = "leftSplitComplementary";

	public ColorList createListFromColour(Color src) {
		Color left = src.getComplement().rotateRYB(-30).lighten(0.1f);
		ColorList colors = ColorTheoryFactory.getInstance().getStrategyForName(
				ColorTheoryFactory.COMPLEMENTARY).createListFromColour(src);
		for (int i = 3; i < 6; i++) {
			Color c = colors.get(i);
			c.setHue(left.hue());
		}
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
