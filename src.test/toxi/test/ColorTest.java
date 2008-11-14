package toxi.test;

import java.util.Iterator;

import junit.framework.TestCase;
import toxi.color.Color;
import toxi.color.ColorAccessCriteria;
import toxi.color.ColorList;
import toxi.math.MathUtils;

public class ColorTest extends TestCase {

	public void testColor() {
		Color c = Color.newHex("00ffff");
		System.out.println(c);
		assertEquals(0.5, c.hue(), 0.001);
		assertEquals(1, c.brightness(), 0.001);
		Color d = Color.newCMYK(1.0f, 0, 0, 0);
		System.out.println(d);
		float delta = c.distanceTo(d);
		System.out.println(delta);
	}

	public void testColorList() {
		ColorList list = new ColorList();
		for (int i = 0; i < 10; i++)
			list.add(Color.newHSV(MathUtils.random(1f), MathUtils.random(1f),
					MathUtils.random(1f)));
		ColorAccessCriteria criteria = ColorAccessCriteria.RED;
		ColorList sorted = list.sortByCriteria(criteria, false);
		Color prev = null;
		for (Iterator i = sorted.iterator(); i.hasNext();) {
			Color c = (Color) i.next();
			System.out.println(c);
			if (prev != null) {
				assertTrue(prev.getComponentValue(criteria) <= c
						.getComponentValue(criteria));
			}
			prev = c;
		}
		System.out.println("cluster sort...");
		sorted = list.clusterSort(ColorAccessCriteria.HUE,
				ColorAccessCriteria.BRIGHTNESS, 3, false);
		for (Iterator i = sorted.iterator(); i.hasNext();) {
			Color c = (Color) i.next();
			System.out.println(c);
		}
	}

	public void testColorListContains() {
		ColorList list = new ColorList();
		list.add(Color.RED);
		list.add(Color.GREEN);
		list.add(Color.BLUE);
		assertEquals(true, list.contains(Color.newRGB(0, 0, 0.995f)));
	}
}
