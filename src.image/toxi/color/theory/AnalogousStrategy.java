/**
 * 
 */
package toxi.color.theory;

import toxi.color.Color;
import toxi.color.ColorList;
import toxi.geom.Vec2D;
import toxi.math.MathUtils;

/**
 * Creates a new palette of 4 similar (slightly paler) colors in addition to the
 * given start color. The hue variance and contrast can be adjusted.
 */
public class AnalogousStrategy implements ColorTheoryStrategy {

	public static final String NAME = "analogous";

	protected static final Vec2D[] tones = new Vec2D[] { new Vec2D(1, 2.2f),
			new Vec2D(2, 1), new Vec2D(-1, -0.5f), new Vec2D(-2, 1) };

	protected float contrast = 0.25f;
	protected float theta = 10 * MathUtils.DEG2RAD;

	/**
	 * Creates a new instance with default contrast (25%) and 10
	 */
	public AnalogousStrategy() {
	}

	/**
	 * @param theta
	 *            variance angle in radians
	 * @param contrast
	 */
	public AnalogousStrategy(float theta, float contrast) {
		this.contrast = contrast;
		this.theta = theta;
	}

	/**
	 * @param theta
	 *            variance angle in degrees
	 * @param contrast
	 */
	public AnalogousStrategy(int theta, float contrast) {
		this.contrast = contrast;
		this.theta = MathUtils.radians(theta);
	}

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
		for (Vec2D currTone : tones) {
			Color c = clr.getRotatedRYB((int) (theta * currTone.x));
			float t = 0.44f - currTone.y * 0.1f;
			if (clr.brightness() - contrast * currTone.y < t) {
				c.setBrightness(t);
			} else {
				c.setBrightness(clr.brightness() - contrast * currTone.y);
			}
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return NAME + " contrast: " + contrast + " theta: "
				+ MathUtils.degrees(theta);
	}
}
