/**
 * 
 */
package toxi.color;

import toxi.math.MathUtils;

/**
 * @author toxi
 * 
 */
public class CompoundTheoryStrategy implements ColorTheoryStrategy {

	public static final String NAME = "compound";

	private boolean isFlipped = false;

	public CompoundTheoryStrategy() {

	}

	public CompoundTheoryStrategy(boolean flipped) {
		isFlipped = flipped;
	}

	public boolean isFlipped() {
		return isFlipped;
	}

	public void setFlipped(boolean state) {
		isFlipped = state;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * toxi.color.ColorTheoryStrategy#createListFromColour(toxi.color.Color)
	 */
	public ColorList createListFromColour(Color src) {
		ColorList colors = new ColorList(src.copy());
		int direction = isFlipped ? -1 : 1;

		Color c = src.getRotatedRYB(30 * direction);
		c.setBrightness(wrap(c.brightness(), 0.25f, 0.6f, 0.25f));
		colors.add(c);

		c = src.getRotatedRYB(30 * direction);
		c.setSaturation(wrap(c.saturation(), 0.4f, 0.1f, 0.4f));
		c.setBrightness(wrap(c.brightness(), 0.4f, 0.2f, 0.4f));
		colors.add(c);

		c = src.getRotatedRYB(160 * direction);
		c.setSaturation(wrap(c.saturation(), 0.25f, 0.1f, 0.25f));
		c.setBrightness(MathUtils.max(0.2f, c.brightness()));
		colors.add(c);

		c = src.getRotatedRYB(150 * direction);
		c.setSaturation(wrap(c.saturation(), 0.1f, 0.8f, 0.1f));
		c.setBrightness(wrap(c.brightness(), 0.3f, 0.6f, 0.3f));
		colors.add(c);

		c = src.getRotatedRYB(150 * direction);
		c.setSaturation(wrap(c.saturation(), 0.1f, 0.8f, 0.1f));
		c.setBrightness(wrap(c.brightness(), 0.4f, 0.2f, 0.4f));
		// colors.add(c);

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
		return NAME + (isFlipped ? "_flipped" : "");
	}
}
