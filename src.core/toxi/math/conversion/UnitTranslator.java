package toxi.math.conversion;

public class UnitTranslator {

	/**
	 * Number of millimeters per inch
	 */
	public static final double INCH_MM = 25.4;

	/**
	 * Number of points per inch
	 */
	public static final double POINT_POSTSCRIPT = 72.0;

	/**
	 * Converts millimeters into pixels.
	 * 
	 * @param mm
	 *            millimeters
	 * @param dpi
	 *            DPI resolution
	 * @return number of pixels
	 */
	public static int millisToPixels(double mm, int dpi) {
		return (int) (mm / INCH_MM * dpi);
	}

	/**
	 * Converts millimeters into PostScript points.
	 * 
	 * @param mm
	 *            millimeters
	 * @return number of points
	 */
	public static double millisToPoints(double mm) {
		return mm / INCH_MM * POINT_POSTSCRIPT;
	}

	/**
	 * Converts pixels into inches.
	 * 
	 * @param pix
	 *            pixels
	 * @param dpi
	 *            DPI resolution to use
	 * @return number of inches
	 */
	public static double pixelsToInch(int pix, int dpi) {
		return (double) pix / dpi;
	}

	/**
	 * Converts pixels into millimeters.
	 * 
	 * @param pix
	 *            pixels
	 * @param dpi
	 *            DPI resolution
	 * @return number of millimeters
	 */
	public static double pixelsToMillis(int pix, int dpi) {
		return pixelsToInch(pix, dpi) * INCH_MM;
	}

	/**
	 * Converts pixels into points.
	 * 
	 * @param pix
	 *            pixels
	 * @param dpi
	 *            DPI resolution
	 * @return number of points
	 */
	public static double pixelsToPoints(int pix, int dpi) {
		return pixelsToInch(pix, dpi) * POINT_POSTSCRIPT;
	}

	/**
	 * Converts points into millimeters.
	 * 
	 * @param pt
	 * @return number of millimeters
	 */
	public static double pointsToMillis(double pt) {
		return pt / POINT_POSTSCRIPT * INCH_MM;
	}

	/**
	 * Converts points into pixels.
	 * 
	 * @param pt
	 *            points
	 * @param dpi
	 *            DPI resolution
	 * @return number of pixels
	 */
	public static int pointsToPixels(double pt, int dpi) {
		return millisToPixels(pointsToMillis(pt), dpi);
	}
}
