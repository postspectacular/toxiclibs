package toxi.math.conversion;

public class UnitTranslator {

	static final double INCH_MM = 25.4;

	static final double POINT_POSTSCRIPT = 72.0;

	double pixelsToInch(int pix, int dpi) {
		return (double) pix / dpi;
	}

	double pixelsToPoints(int pix, int dpi) {
		return pixelsToInch(pix, dpi) * POINT_POSTSCRIPT;
	}

	double pixelsToMillis(int pix, int dpi) {
		return pixelsToInch(pix, dpi) * INCH_MM;
	}

	double millisToPoints(double mm) {
		return mm / INCH_MM * POINT_POSTSCRIPT;
	}

	double pointsToMillis(double pt) {
		return pt / POINT_POSTSCRIPT * INCH_MM;
	}

	int pointsToPixels(double pt, int dpi) {
		return millisToPixels(pointsToMillis(pt), dpi);
	}

	int millisToPixels(double mm, int dpi) {
		return (int) (mm / INCH_MM * dpi);
	}

}
