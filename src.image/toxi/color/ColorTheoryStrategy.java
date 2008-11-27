package toxi.color;

public interface ColorTheoryStrategy {

	/**
	 * Returns the unique name of the strategy.
	 * 
	 * @return name
	 */
	String getName();

	/**
	 * Creates a new {@link ColorList} of colors for the supplied source color
	 * based on the strategy. The number of colors returned is unspecified and
	 * depends on the strategy.
	 * 
	 * @param src
	 *            source color
	 * @return list of matching colors created by the strategy.
	 */
	ColorList createListFromColour(Color src);
}
