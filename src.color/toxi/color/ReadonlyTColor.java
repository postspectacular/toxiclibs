package toxi.color;

public interface ReadonlyTColor {

	/**
	 * @return the color's alpha component
	 */
	public float alpha();

	/**
	 * @return the color's black component
	 */

	public float black();

	/**
	 * @return the color's blue component
	 */

	public float blue();

	/**
	 * @return color HSV brightness (not luminance!)
	 */
	public float brightness();

	public TColor copy();

	/**
	 * @return the color's cyan component
	 */

	public float cyan();

	/**
	 * Calculates the CMYK distance to the given color.
	 * 
	 * @param c
	 *            target color
	 * @return distance
	 */
	public float distanceToCMYK(ReadonlyTColor c);

	/**
	 * Calculates the HSV distance to the given color.
	 * 
	 * @param c
	 *            target color
	 * @return distance
	 */
	public float distanceToHSV(ReadonlyTColor c);

	/**
	 * Calculates the RGB distance to the given color.
	 * 
	 * @param c
	 *            target color
	 * @return distance
	 */
	public float distanceToRGB(ReadonlyTColor c);

	public TColor getAnalog(float theta, float delta);

	public TColor getAnalog(int angle, float delta);

	public TColor getBlended(ReadonlyTColor c, float t);

	/**
	 * @return an instance of the closest named hue to this color.
	 */
	public Hue getClosestHue();

	/**
	 * @param primaryOnly
	 *            if true, only primary color hues are considered
	 * @return an instance of the closest named (primary) hue to this color.
	 */
	public Hue getClosestHue(boolean primaryOnly);

	public TColor getComplement();

	public float getComponentValue(AccessCriteria criteria);

	/**
	 * @param step
	 * @return a darkened copy
	 */
	public TColor getDarkened(float step);

	/**
	 * @param step
	 * @return a desaturated copy
	 */
	public TColor getDesaturated(float step);

	/**
	 * @return an inverted copy
	 */
	public TColor getInverted();

	/**
	 * @param step
	 * @return a lightened copy
	 */
	public TColor getLightened(float step);

	/**
	 * @param theta
	 *            rotation angle in radians
	 * @return a RYB rotated copy
	 */
	public TColor getRotatedRYB(float theta);

	/**
	 * @param angle
	 *            rotation angle in degrees
	 * @return a RYB rotated copy
	 */
	public TColor getRotatedRYB(int angle);

	/**
	 * @param step
	 * @return a saturated copy
	 */
	public TColor getSaturated(float step);

	/**
	 * @return the color's green component
	 */

	public float green();

	/**
	 * @return the color's hue
	 */
	public float hue();

	/**
	 * @return true, if all rgb component values are equal and less than
	 *         {@link #BLACK_POINT}
	 */
	public boolean isBlack();

	/**
	 * @return true, if the saturation component value is less than
	 *         {@link #GREY_THRESHOLD}
	 */
	public boolean isGrey();

	/**
	 * @return true, if this colors hue is matching one of the 7 defined primary
	 *         hues.
	 */
	public boolean isPrimary();

	/**
	 * @return true, if all rgb component values are equal and greater than
	 *         {@link #WHITE_POINT}
	 */
	public boolean isWhite();

	/**
	 * Computes the color's luminance using this formula: lum=0.299*red +
	 * 0.587*green + 0.114 *blue
	 * 
	 * @return luminance
	 */
	public float luminance();

	/**
	 * @return the color's magenta component
	 */

	public float magenta();

	/**
	 * @return the color's red component
	 */

	public float red();

	public float saturation();

	/**
	 * Converts the color into a packed ARGB int.
	 * 
	 * @return color as int
	 */
	public int toARGB();

	/**
	 * Copies the current CMYKA values into the given array (or constucts a new
	 * one with these values).
	 * 
	 * @param cmyka
	 *            result array (or null)
	 * @return array in this order: c,m,y,k,a
	 */
	public float[] toCMYKAArray(float[] cmyka);

	/**
	 * @return Copies the the current HSV values into the given array (or
	 *         constucts a new one with these values).
	 * @param hsva
	 *            result array (or null)
	 * @return array in this order: h,s,v,a
	 */
	public float[] toHSVAArray(float[] hsva);

	/**
	 * Copies the current RGBA value into the given array (or constucts a new
	 * one with these values).
	 * 
	 * @param rgba
	 *            result array (or null)
	 * @return array in this order: r,g,b,a
	 */
	public float[] toRGBAArray(float[] rgba);

	/**
	 * @return the color's yellow component
	 */
	public float yellow();

}