package toxi.test;

import java.util.Iterator;

import processing.core.PApplet;
import toxi.color.Color;
import toxi.color.ColorAccessCriteria;
import toxi.color.ColorList;
import toxi.math.MathUtils;

public class ColourTestVisuals extends PApplet {

	public void setup() {
		size(1000, 500);
		background(0);
		ColorList list = new ColorList();
		for (int i = 0; i < 100; i++)
			list.add(Color.newHSV(i * 0.01f, MathUtils.random(1f), MathUtils
					.random(1f)));
		swatches(list, 10, 10);
		ColorAccessCriteria criteria = ColorAccessCriteria.GREEN;
		ColorList sorted = list.sortByCriteria(criteria, false);
		swatches(sorted, 10, 70);
		System.out.println("cluster sort...");
		sorted = list.clusterSort(ColorAccessCriteria.HUE,
				ColorAccessCriteria.BRIGHTNESS, 12, true);
		swatches(sorted, 10, 130);
		sorted = list.sortByDistance(false);
		swatches(sorted, 10, 190);
	}

	private void swatches(ColorList sorted, int x, int y) {
		for (Iterator i = sorted.iterator(); i.hasNext();) {
			Color c = (Color) i.next();
			fill(c.toARGB());
			rect(x, y, 4, 50);
			x += 4;
			// System.out.println(c);
		}
	}

}
