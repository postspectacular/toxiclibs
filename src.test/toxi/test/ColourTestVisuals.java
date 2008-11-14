package toxi.test;

import java.util.Iterator;

import processing.core.PApplet;
import toxi.color.Color;
import toxi.color.ColorAccessCriteria;
import toxi.color.ColorList;
import toxi.color.ColorRange;
import toxi.color.ColorTheoryFactory;
import toxi.color.ColorTheoryStrategy;
import toxi.math.MathUtils;
import toxi.util.datatypes.FloatRange;

public class ColourTestVisuals extends PApplet {

	private static final float SWATCH_HEIGHT = 40;
	private static final float SWATCH_WIDTH = 5;
	private static final int SWATCH_GAP = 1;

	public void setup() {
		size(1024, 768);
		noLoop();
	}

	public void draw() {
		background(0);
		ColorList list = new ColorList();
		for (int i = 0; i < 100; i++)
			list.add(Color.newHSV(MathUtils.random(1f), MathUtils.random(1f),
					MathUtils.random(1f)));
		int yoff = 10;
		swatches(list, 10, yoff);
		ColorAccessCriteria criteria = ColorAccessCriteria.SATURATION;
		ColorList sorted = list.sortByCriteria(criteria, false);
		yoff += SWATCH_HEIGHT + 10;
		swatches(sorted, 10, yoff);
		System.out.println("cluster sort...");
		sorted = list.clusterSort(ColorAccessCriteria.HUE,
				ColorAccessCriteria.BRIGHTNESS, 12, true);
		yoff += SWATCH_HEIGHT + 10;
		swatches(sorted, 10, yoff);
		sorted = list.sortByDistance(false);
		yoff += SWATCH_HEIGHT + 10;
		swatches(sorted, 10, yoff);
		Color col = Color.newHSV(MathUtils.random(1f), MathUtils.random(0.75f,
				1f), MathUtils.random(1f));
		int idx = 0;
		yoff = 10;
		for (Iterator i = ColorTheoryFactory.getInstance()
				.getRegisteredStrategies().iterator(); i.hasNext();) {
			ColorTheoryStrategy strategy = (ColorTheoryStrategy) i.next();
			System.out.println(strategy);
			sorted = ColorList.createUsingStrategy(strategy, col);
			sorted = sorted.sortByDistance(false);
			swatches(sorted, 900, yoff);
			yoff += SWATCH_HEIGHT + 10;
			idx++;
		}

		ColorRange range = new ColorRange();
		range.hueConstraint.getCurrent().max = 0.2f;
		range.saturationConstraint.getCurrent().min = 0.8f;
		range.brightnessConstraint.getCurrent().min = 0.25f;
		range.addHueConstraint(new FloatRange(.66f, 0.75f));
		range.addSaturationConstraint(new FloatRange(0, 0));
		sorted = range.getColors(100).clusterSort(
				ColorAccessCriteria.BRIGHTNESS, ColorAccessCriteria.SATURATION,
				4, false);
		swatches(sorted, 10, 400);
		swatch(sorted.getDarkest(), 10, 450);
		swatch(sorted.getAverage(), 20, 450);
		swatch(sorted.getLightest(), 30, 450);
	}

	private void swatches(ColorList sorted, int x, int y) {
		noStroke();
		for (Iterator i = sorted.iterator(); i.hasNext();) {
			Color c = (Color) i.next();
			swatch(c, x, y);
			x += SWATCH_WIDTH + SWATCH_GAP;
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
