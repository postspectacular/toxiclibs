package toxi.test;

import processing.core.PApplet;
import toxi.color.AccessCriteria;
import toxi.color.ColorList;
import toxi.color.ColorRange;
import toxi.color.ColorTheme;
import toxi.color.TColor;
import toxi.math.MathUtils;

public class ColorThemeVisuals extends PApplet {

	private static final float SWATCH_HEIGHT = 40;
	private static final float SWATCH_WIDTH = 5;
	private static final int SWATCH_GAP = 1;
	private static final float MAX_SIZE = 150;

	private void discs(ColorList list) {
		float numCols = list.size();
		for (int i = 0; i < 300; i++) {
			TColor c = list.get((int) random(numCols)).copy();
			c.alpha = random(0.5f, 1);
			fill(c.toARGB());
			c = list.get((int) random(numCols));
			stroke(c.toARGB());
			strokeWeight(random(10));
			float r = random(MAX_SIZE);
			ellipse(random(width), random(height), r, r);
		}
	}

	public void draw() {
		ColorTheme t = new ColorTheme("test");
		t.addRange("soft ivory", 0.5f);
		t.addRange(ColorRange.BRIGHT, TColor.newRandom(), MathUtils.random(
				0.02f, 0.05f));
		t.addRange("intense goldenrod", 0.25f);
		t.addRange("warm saddlebrown", 0.15f);
		t.addRange("fresh teal", 0.05f);
		t.addRange("bright yellow", 0.05f);
		ColorList list = t.getColors(160);
		list.sortByCriteria(AccessCriteria.LUMINANCE, false);
		if (!mousePressed) {
			background(list.getLightest().toARGB());
			discs(list);
		} else {
			background(0);
			list.sortByDistance(false);
			swatches(list, 20, 60);
		}
	}

	public void keyPressed() {
		redraw();
	}

	public void setup() {
		size(1024, 768);
		smooth();
		noLoop();
		noStroke();
		textFont(createFont("arial", 12));
	}

	private void swatches(ColorList sorted, int x, int y) {
		noStroke();
		for (Object element : sorted) {
			TColor c = (TColor) element;
			fill(c.toARGB());
			rect(x, y, SWATCH_WIDTH, SWATCH_HEIGHT);
			x += SWATCH_WIDTH + SWATCH_GAP;
		}
	}

}
