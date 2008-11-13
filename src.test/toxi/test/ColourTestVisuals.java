package toxi.test;

import java.util.Iterator;

import processing.core.PApplet;
import toxi.color.Color;
import toxi.color.ColorAccessCriteria;
import toxi.color.ColorList;
import toxi.color.ColorTheoryFactory;
import toxi.color.ColorTheoryStrategy;
import toxi.math.MathUtils;

public class ColourTestVisuals extends PApplet {

	public void setup() {
		size(1000, 500);
		noLoop();
	}

	public void draw() {
		background(0);
		ColorList list = new ColorList();
		for (int i = 0; i < 100; i++)
			list.add(Color.newHSV(MathUtils.random(1f), MathUtils.random(1f),
					MathUtils.random(1f)));
		swatches(list, 10, 10);
		ColorAccessCriteria criteria = ColorAccessCriteria.SATURATION;
		ColorList sorted = list.sortByCriteria(criteria, false);
		swatches(sorted, 10, 70);
		System.out.println("cluster sort...");
		sorted = list.clusterSort(ColorAccessCriteria.HUE,
				ColorAccessCriteria.BRIGHTNESS, 12, true);
		swatches(sorted, 10, 130);
		sorted = list.sortByDistance(false);
		swatches(sorted, 10, 190);
		Color col = Color.newHSV(MathUtils.random(1f), MathUtils.random(0.75f,
				1f), MathUtils.random(1f));
		int idx = 0;
		for (Iterator i = ColorTheoryFactory.getInstance()
				.getRegisteredStrategies().iterator(); i.hasNext();) {
			ColorTheoryStrategy strategy = (ColorTheoryStrategy) i.next();
			System.out.println(strategy);
			sorted = ColorList.createUsingStrategy(strategy, col);
			swatches(sorted, 600, 10 + idx * 60);
			idx++;
		}
	}

	private void swatches(ColorList sorted, int x, int y) {
		noStroke();
		for (Iterator i = sorted.iterator(); i.hasNext();) {
			Color c = (Color) i.next();
			fill(c.toARGB());
			rect(x, y, 4, 50);
			x += 5;
			// System.out.println(c);
		}
	}

	public void keyPressed() {
		redraw();
	}
}
