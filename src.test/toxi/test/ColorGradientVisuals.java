package toxi.test;

import processing.core.PApplet;
import toxi.colour.ColourGradient;
import toxi.colour.ColourList;
import toxi.colour.NamedColour;

public class ColorGradientVisuals extends PApplet {

	private static final boolean USE_DITHER = false;

	public void setup() {
		size(1024, 256);
	}

	public void draw() {
		ColourGradient gradient = new ColourGradient();
		if (USE_DITHER) {
			gradient.setMaxDither(abs((height * 0.5f - mouseY)
					/ (height * 0.5f)) * 0.1f);
		}
		gradient.addColorAt(mouseX, NamedColour.CRIMSON);
		gradient.addColorAt(width * 0.25f, NamedColour.ANTIQUEWHITE);
		gradient.addColorAt(width * 0.5f, NamedColour.YELLOW);
		gradient.addColorAt(width * 0.75f, NamedColour.SPRINGGREEN);
		gradient.addColorAt(width, NamedColour.BLUEVIOLET);
		ColourList cols = gradient.calcGradient(0, width);
		for (int x = 0; x < width; x++) {
			stroke(cols.get(x).toARGB());
			line(x, 0, x, height);
		}
	}

}
