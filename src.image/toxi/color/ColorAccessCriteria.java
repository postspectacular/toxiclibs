package toxi.color;

import java.util.Comparator;

public class ColorAccessCriteria {

	public static final int HSV_MODE = 0;
	public static final int RGB_MODE = 1;
	public static final int CMYK_MODE = 2;
	public static final int ALPHA_MODE = 3;

	private Comparator comparator;

	public static final ColorAccessCriteria HUE = new ColorAccessCriteria(
			HSV_MODE, 0);
	public static final ColorAccessCriteria SATURATION = new ColorAccessCriteria(
			HSV_MODE, 1);
	public static final ColorAccessCriteria BRIGHTNESS = new ColorAccessCriteria(
			HSV_MODE, 2);
	public static final ColorAccessCriteria RED = new ColorAccessCriteria(
			RGB_MODE, 0);
	public static final ColorAccessCriteria GREEN = new ColorAccessCriteria(
			RGB_MODE, 1);
	public static final ColorAccessCriteria BLUE = new ColorAccessCriteria(
			RGB_MODE, 2);
	public static final ColorAccessCriteria CYAN = new ColorAccessCriteria(
			CMYK_MODE, 0);
	public static final ColorAccessCriteria MAGENTA = new ColorAccessCriteria(
			CMYK_MODE, 1);
	public static final ColorAccessCriteria YELLOW = new ColorAccessCriteria(
			CMYK_MODE, 2);
	public static final ColorAccessCriteria BLACK = new ColorAccessCriteria(
			CMYK_MODE, 3);

	public static final ColorAccessCriteria ALPHA = new ColorAccessCriteria(
			ALPHA_MODE, 0);

	private final int mode, component;

	private ColorAccessCriteria(int mode, int compID) {
		this.mode = mode;
		this.component = compID;
	}

	protected int getMode() {
		return mode;
	}

	protected int getComponent() {
		return component;
	}

	public Comparator<Color> getComparator() {
		if (comparator == null) {
			switch (mode) {
			case HSV_MODE:
				comparator = new HSVComparator(component);
				break;
			case RGB_MODE:
				comparator = new RGBComparator(component);
				break;
			case CMYK_MODE:
				comparator = new CMYKComparator(component);
				break;
			case ALPHA_MODE:
				comparator = new AlphaComparator();
				break;
			}
		}
		return comparator;
	}
}
