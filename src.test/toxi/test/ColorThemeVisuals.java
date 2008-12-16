package toxi.test;

import java.util.Iterator;

import processing.core.PApplet;
import toxi.color.Color;
import toxi.color.ColorAccessCriteria;
import toxi.color.ColorList;
import toxi.color.ColorRange;
import toxi.color.ColorTheme;
import toxi.color.NamedColor;

public class ColorThemeVisuals extends PApplet {

	private static final float SWATCH_HEIGHT = 40;
	private static final float SWATCH_WIDTH = 5;
	private static final int SWATCH_GAP = 1;

	public void setup() {
		size(1024, 768);
		smooth();
		noLoop();
		noStroke();
		textFont(createFont("arial", 12));
	}

	public void draw() {
		background(255);
		ColorTheme t = new ColorTheme("test");
		t.addRange("dark blue", 0.5f);
		t.addRange("bright orange", 1);
		t.addRange(ColorRange.WARM, NamedColor.DARKORCHID, 1);
		t.addRange(ColorRange.COOL, null, 0.25f);
		t.addRange("fresh teal", 0.05f);

		t = new ColorTheme("sand");
		t.addRange("soft ivory", 0.5f);
		t.addRange("dark goldenrod", 0.25f);
		t.addRange("warm saddlebrown", 0.25f);
		t.addRange("fresh teal", 0.05f);

		ColorList list = t.getColors(500);
		list.sortByCriteria(ColorAccessCriteria.LUMINANCE, false);
		discs(list);
		// list.sortByDistance(false);
		// swatches(list, 10, 60);
	}

	private void swatches(ColorList sorted, int x, int y) {
		noStroke();
		for (Iterator i = sorted.iterator(); i.hasNext();) {
			Color c = (Color) i.next();
			swatch(c, x, y);
			x += SWATCH_WIDTH + SWATCH_GAP;
		}
	}

	private void discs(ColorList list) {
		for (Iterator i = list.iterator(); i.hasNext();) {
			Color c = (Color) i.next();
			fill(c.toARGB());
			float r = random(100);
			ellipse(random(width), random(height), r, r);
		}
	}

	private void swatch(Color c, int x, int y) {
		fill(c.toARGB());
		rect(x, y, SWATCH_WIDTH, SWATCH_HEIGHT);
	}

	public void keyPressed() {
		redraw();
	}
}
