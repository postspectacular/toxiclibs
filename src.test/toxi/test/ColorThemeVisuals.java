package toxi.test;

import java.util.Iterator;

import processing.core.PApplet;
import toxi.colour.Colour;
import toxi.colour.AccessCriteria;
import toxi.colour.ColourList;
import toxi.colour.ColourRange;
import toxi.colour.ColourTheme;
import toxi.math.MathUtils;

public class ColorThemeVisuals extends PApplet {

	private static final float SWATCH_HEIGHT = 40;
	private static final float SWATCH_WIDTH = 5;
	private static final int SWATCH_GAP = 1;
	private static final float MAX_SIZE = 150;

	public void setup() {
		size(1024, 768);
		smooth();
		noLoop();
		noStroke();
		textFont(createFont("arial", 12));
	}

	public void draw() {
		ColourTheme t = new ColourTheme("test");
		t.addRange("soft ivory", 0.5f);
		t.addRange(ColourRange.BRIGHT, Colour.newRandom(), MathUtils.random(
				0.02f, 0.05f));
		t.addRange("intense goldenrod", 0.25f);
		t.addRange("warm saddlebrown", 0.15f);
		t.addRange("fresh teal", 0.05f);
		t.addRange("bright yellow", 0.05f);
		ColourList list = t.getColors(160);
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

	private void swatches(ColourList sorted, int x, int y) {
		noStroke();
		for (Iterator i = sorted.iterator(); i.hasNext();) {
			Colour c = (Colour) i.next();
			fill(c.toARGB());
			rect(x, y, SWATCH_WIDTH, SWATCH_HEIGHT);
			x += SWATCH_WIDTH + SWATCH_GAP;
		}
	}

	private void discs(ColourList list) {
		float numCols = list.size();
		for (int i = 0; i < 300; i++) {
			Colour c = list.get((int) random(numCols)).copy();
			c.alpha = random(0.5f, 1);
			fill(c.toARGB());
			c = list.get((int) random(numCols));
			stroke(c.toARGB());
			strokeWeight(random(10));
			float r = random(MAX_SIZE);
			ellipse(random(width), random(height), r, r);
		}
	}

}
