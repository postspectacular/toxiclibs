/**
 * 
 */
package toxi.color;

/**
 * @author toxi
 * 
 */
public class ComplementaryStrategy implements ColorTheoryStrategy {

	public static final String NAME = "complementary";

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * toxi.color.ColorTheoryStrategy#createListFromColour(toxi.color.Color)
	 */
	public ColorList createListFromColour(Color src) {
		ColorList colors = new ColorList(src.copy());
		// # A contrasting color: much darker or lighter than the original.
		Color c = src.copy();
		if (c.brightness() > 0.4)
			c.setBrightness(0.1f + c.brightness() * 0.25f);
		else
			c.setBrightness(1.0f - c.brightness() * 0.25f);
		colors.add(c);

		// A soft supporting color: lighter and less saturated.
		c = src.copy();
		c.lighten(0.3f);
		c.setSaturation(0.1f + c.saturation() * 0.3f);
		colors.add(c);

		// A contrasting complement: very dark or very light.
		c = src.getComplement();
		if (c.brightness() > 0.3)
			c.setBrightness(0.1f + c.brightness() * 0.25f);
		else
			c.setBrightness(1.0f - c.brightness() * 0.25f);
		colors.add(c);

		// The complement and a light supporting variant.
		colors.add(src.getComplement());

		c = src.getComplement();
		c.lighten(0.3f);
		c.setSaturation(0.1f + c.saturation() * 0.25f);
		colors.add(c);
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
