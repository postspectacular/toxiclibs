package toxi.test;

import java.util.Iterator;

import processing.core.PApplet;
import toxi.color.Color;
import toxi.color.ColorAccessCriteria;
import toxi.color.ColorList;
import toxi.color.ColorRange;
import toxi.color.theory.ColorTheoryFactory;
import toxi.color.theory.ColorTheoryStrategy;
import toxi.math.MathUtils;

public class ColourTestVisuals extends PApplet {

	private static final float SWATCH_HEIGHT = 40;
	private static final float SWATCH_WIDTH = 5;
	private static final int SWATCH_GAP = 1;

	public void setup() {
		size(1024, 768);
		noLoop();
		textFont(createFont("arial", 12));
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
		yoff = 210;
		col = Color.newHSV(MathUtils.random(1f), MathUtils.random(1f),
				MathUtils.random(1f));
		for (Iterator i = ColorRange.PRESETS.values().iterator(); i.hasNext();) {
			ColorRange range = (ColorRange) i.next();
			sorted = range.getColors(col, 100, 0.1f);
			sorted = sorted.sortByCriteria(ColorAccessCriteria.BRIGHTNESS,
					false);
			swatches(sorted, 10, yoff);
			fill(255);
			text(range.getName(), 15 + 100 * (SWATCH_WIDTH + SWATCH_GAP), yoff
					+ SWATCH_HEIGHT);
			yoff += SWATCH_HEIGHT + 10;
		}
		ColorRange range = ColorRange.FRESH.getSum(ColorRange.BRIGHT).add(
				ColorRange.LIGHT).add(Color.WHITE);
		sorted = range.getColors(Color.MAGENTA, 100, 0.35f);
		sorted = sorted.sortByDistance(false);
		swatches(sorted, 10, yoff);
		yoff += SWATCH_HEIGHT + 10;
		range = new ColorRange(ColorList.createUsingStrategy(ColorTheoryFactory
				.getInstance().getStrategyForName(
						ColorTheoryFactory.SPLIT_COMPLEMENTARY), Color.YELLOW));
		sorted = range.getColors(100).sortByDistance(false);
		swatches(sorted, 10, yoff);
		yoff += SWATCH_HEIGHT + 10;
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
