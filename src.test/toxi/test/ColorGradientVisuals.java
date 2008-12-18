package toxi.test;

import processing.core.PApplet;
import toxi.color.ColorGradient;
import toxi.color.ColorList;
import toxi.color.NamedColor;

public class ColorGradientVisuals extends PApplet {

	private static final boolean USE_DITHER = false;

	public void draw() {
		ColorGradient gradient = new ColorGradient();
		if (USE_DITHER) {
			gradient.setMaxDither(abs((height * 0.5f - mouseY)
					/ (height * 0.5f)) * 0.1f);
		}
		gradient.addColorAt(mouseX, NamedColor.CRIMSON);
		gradient.addColorAt(width * 0.25f, NamedColor.ANTIQUEWHITE);
		gradient.addColorAt(width * 0.5f, NamedColor.YELLOW);
		gradient.addColorAt(width * 0.75f, NamedColor.SPRINGGREEN);
		gradient.addColorAt(width, NamedColor.BLUEVIOLET);
		ColorList cols = gradient.calcGradient(0, width);
		for (int x = 0; x < width; x++) {
			stroke(cols.get(x).toARGB());
			line(x, 0, x, height);
		}
	}

	public void setup() {
		size(1024, 256);
	}

}
