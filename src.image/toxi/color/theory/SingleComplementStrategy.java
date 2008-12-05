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
public class SingleComplementStrategy implements ColorTheoryStrategy {

	public static final String NAME = "complement";

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * toxi.color.ColorTheoryStrategy#createListFromColour(toxi.color.Color)
	 */
	public ColorList createListFromColour(Color src) {
		ColorList list = new ColorList(new Color(src));
		list.add(src.getComplement());
		return list;
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
