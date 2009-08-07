package toxi.test;

import processing.core.PApplet;
import toxi.color.AccessCriteria;
import toxi.color.CMYKDistanceProxy;
import toxi.color.ColorList;
import toxi.color.ColorRange;
import toxi.color.NamedColor;
import toxi.color.ProximityComparator;
import toxi.color.RGBDistanceProxy;
import toxi.color.ReadonlyTColor;
import toxi.color.TColor;
import toxi.color.theory.ColorTheoryRegistry;
import toxi.color.theory.ColorTheoryStrategy;
import toxi.math.MathUtils;

public class ColorTestVisuals extends PApplet {

	private static final float SWATCH_HEIGHT = 40;
	private static final float SWATCH_WIDTH = 5;
	private static final int SWATCH_GAP = 1;

	@Override
	public void draw() {
		background(0);
		ColorList list = new ColorList();
		for (int i = 0; i < 100; i++) {
			list.add(TColor.newRandom());
		}
		int yoff = 10;
		swatches(list, 10, yoff);
		yoff += SWATCH_HEIGHT + 10;
		ColorList sorted = null;
		sorted = list.clusterSort(AccessCriteria.HUE,
				AccessCriteria.BRIGHTNESS, 12, true);
		sorted = list.sortByComparator(new ProximityComparator(NamedColor.BLUE,
				new RGBDistanceProxy()), false);
		swatches(sorted, 10, yoff);
		yoff += SWATCH_HEIGHT + 10;
		sorted = list.sortByDistance(false);
		swatches(sorted, 10, yoff);
		yoff += SWATCH_HEIGHT + 10;
		sorted = list.sortByDistance(new RGBDistanceProxy(), false);
		swatches(sorted, 10, yoff);
		yoff += SWATCH_HEIGHT + 10;
		sorted = list.sortByDistance(new CMYKDistanceProxy(), false);
		swatches(sorted, 10, yoff);
		yoff += SWATCH_HEIGHT + 10;

		TColor col = TColor.newHSV(MathUtils.random(1f), MathUtils.random(
				0.75f, 1f), MathUtils.random(1f));
		int idx = 0;
		yoff = 10;
		for (Object element : ColorTheoryRegistry.getRegisteredStrategies()) {
			ColorTheoryStrategy strategy = (ColorTheoryStrategy) element;
			System.out.println(strategy);
			sorted = ColorList.createUsingStrategy(strategy, col);
			sorted = sorted.sortByDistance(false);
			swatches(sorted, 900, yoff);
			yoff += SWATCH_HEIGHT + 10;
			idx++;
		}
		yoff = 260;
		col = TColor.newHSV(MathUtils.random(1f), MathUtils.random(1f),
				MathUtils.random(1f));
		for (Object element : ColorRange.PRESETS.values()) {
			ColorRange range = (ColorRange) element;
			sorted = range.getColors(col, 100, 0.1f);
			sorted = sorted.sortByCriteria(AccessCriteria.BRIGHTNESS, false);
			swatches(sorted, 10, yoff);
			fill(255);
			text(range.getName(), 15 + 100 * (SWATCH_WIDTH + SWATCH_GAP), yoff
					+ SWATCH_HEIGHT);
			yoff += SWATCH_HEIGHT + 10;
		}
		ColorRange range = ColorRange.FRESH.getSum(ColorRange.BRIGHT).add(
				ColorRange.LIGHT).add(TColor.WHITE);
		sorted = range.getColors(TColor.MAGENTA, 100, 0.35f);
		sorted = sorted.sortByDistance(false);
		swatches(sorted, 10, yoff);
		yoff += SWATCH_HEIGHT + 10;
		range = new ColorRange(ColorTheoryRegistry.SPLIT_COMPLEMENTARY
				.createListFromColor(TColor.YELLOW));
		sorted = range.getColors(100).sortByDistance(false);
		swatches(sorted, 10, yoff);
		yoff += SWATCH_HEIGHT + 10;
	}

	public void keyPressed() {
		redraw();
	}

	public void setup() {
		size(1024, 768);
		noLoop();
		textFont(createFont("arial", 12));
	}

	private void swatch(ReadonlyTColor c, int x, int y) {
		fill(c.toARGB());
		rect(x, y, SWATCH_WIDTH, SWATCH_HEIGHT);
	}

	private void swatches(ColorList sorted, int x, int y) {
		noStroke();
		for (ReadonlyTColor c : sorted) {
			swatch(c, x, y);
			x += SWATCH_WIDTH + SWATCH_GAP;
		}
	}
}
