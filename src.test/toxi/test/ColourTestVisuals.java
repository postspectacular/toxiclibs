package toxi.test;

import java.util.Iterator;

import processing.core.PApplet;
import toxi.colour.AccessCriteria;
import toxi.colour.CMYKDistanceProxy;
import toxi.colour.Colour;
import toxi.colour.ColourList;
import toxi.colour.ColourRange;
import toxi.colour.NamedColour;
import toxi.colour.ProximityComparator;
import toxi.colour.RGBDistanceProxy;
import toxi.colour.theory.ColorTheoryRegistry;
import toxi.colour.theory.ColorTheoryStrategy;
import toxi.math.MathUtils;

public class ColourTestVisuals extends PApplet {

	private static final float SWATCH_HEIGHT = 40;
	private static final float SWATCH_WIDTH = 5;
	private static final int SWATCH_GAP = 1;

	@Override
	public void draw() {
		background(0);
		ColourList list = new ColourList();
		for (int i = 0; i < 100; i++) {
			list.add(Colour.newRandom());
		}
		int yoff = 10;
		swatches(list, 10, yoff);
		yoff += SWATCH_HEIGHT + 10;
		ColourList sorted = null;
		sorted = list.clusterSort(AccessCriteria.HUE,
				AccessCriteria.BRIGHTNESS, 12, true);
		sorted = list.sortByComparator(new ProximityComparator(
				NamedColour.BLUE, new RGBDistanceProxy()), false);
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

		Colour col = Colour.newHSV(MathUtils.random(1f), MathUtils.random(
				0.75f, 1f), MathUtils.random(1f));
		int idx = 0;
		yoff = 10;
		for (Iterator i = ColorTheoryRegistry.getRegisteredStrategies()
				.iterator(); i.hasNext();) {
			ColorTheoryStrategy strategy = (ColorTheoryStrategy) i.next();
			System.out.println(strategy);
			sorted = ColourList.createUsingStrategy(strategy, col);
			sorted = sorted.sortByDistance(false);
			swatches(sorted, 900, yoff);
			yoff += SWATCH_HEIGHT + 10;
			idx++;
		}
		yoff = 260;
		col = Colour.newHSV(MathUtils.random(1f), MathUtils.random(1f),
				MathUtils.random(1f));
		for (Iterator i = ColourRange.PRESETS.values().iterator(); i.hasNext();) {
			ColourRange range = (ColourRange) i.next();
			sorted = range.getColors(col, 100, 0.1f);
			sorted = sorted.sortByCriteria(AccessCriteria.BRIGHTNESS, false);
			swatches(sorted, 10, yoff);
			fill(255);
			text(range.getName(), 15 + 100 * (SWATCH_WIDTH + SWATCH_GAP), yoff
					+ SWATCH_HEIGHT);
			yoff += SWATCH_HEIGHT + 10;
		}
		ColourRange range = ColourRange.FRESH.getSum(ColourRange.BRIGHT).add(
				ColourRange.LIGHT).add(Colour.WHITE);
		sorted = range.getColors(Colour.MAGENTA, 100, 0.35f);
		sorted = sorted.sortByDistance(false);
		swatches(sorted, 10, yoff);
		yoff += SWATCH_HEIGHT + 10;
		range = new ColourRange(ColorTheoryRegistry.SPLIT_COMPLEMENTARY
				.createListFromColour(Colour.YELLOW));
		sorted = range.getColors(100).sortByDistance(false);
		swatches(sorted, 10, yoff);
		yoff += SWATCH_HEIGHT + 10;
	}

	@Override
	public void keyPressed() {
		redraw();
	}

	@Override
	public void setup() {
		size(1024, 768);
		noLoop();
		textFont(createFont("arial", 12));
	}

	private void swatch(Colour c, int x, int y) {
		fill(c.toARGB());
		rect(x, y, SWATCH_WIDTH, SWATCH_HEIGHT);
	}

	private void swatches(ColourList sorted, int x, int y) {
		noStroke();
		for (Iterator i = sorted.iterator(); i.hasNext();) {
			Colour c = (Colour) i.next();
			swatch(c, x, y);
			x += SWATCH_WIDTH + SWATCH_GAP;
		}
	}
}
