/**
 * 
 */
package toxi.color.theory;

import toxi.color.Color;
import toxi.color.ColorList;
import toxi.geom.Vec2D;
import toxi.math.MathUtils;

/**
 * @author toxi
 * 
 */
public class AnalogousStrategy implements ColorTheoryStrategy {

	public static final String NAME = "analogous";

	public float contrast = 0.25f;
	public float theta = 10;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * toxi.color.ColorTheoryStrategy#createListFromColour(toxi.color.Color)
	 */
	public ColorList createListFromColour(Color src) {
		contrast = MathUtils.clipNormalized(contrast);
		Color clr = src.copy();
		ColorList colors = new ColorList(clr);

		Vec2D[] tones = new Vec2D[] { new Vec2D(1, 2.2f), new Vec2D(2, 1),
				new Vec2D(-1, -0.5f), new Vec2D(-2, 1) };
		for (int i = 0; i < tones.length; i++) {
			Vec2D currTone = tones[i];
			Color c = clr.getRotatedRYB((int) (theta * currTone.x));
			float t = 0.44f - currTone.y * 0.1f;
			if (clr.brightness() - contrast * currTone.y < t)
				c.setBrightness(t);
			else
				c.setBrightness(clr.brightness() - contrast * currTone.y);
			c.desaturate(0.05f);
			colors.add(c);
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
		return NAME + " contrast: " + contrast + " theta: " + theta;
	}
}
