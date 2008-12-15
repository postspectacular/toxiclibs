package toxi.test;

import java.util.ArrayList;

import junit.framework.TestCase;
import toxi.color.Color;
import toxi.color.ColorAccessCriteria;
import toxi.color.ColorHue;
import toxi.color.ColorList;
import toxi.color.ColorRange;
import toxi.color.ColorTheme;
import toxi.color.theory.ColorTheoryFactory;
import toxi.color.theory.ColorTheoryStrategy;
import toxi.math.MathUtils;

public class ColorTest extends TestCase {

	public void testColor() {
		Color c = Color.newHex("00ffff");
		System.out.println(c);
		assertEquals(0.5, c.hue(), 0.001);
		assertEquals(1.0, c.brightness(), 0.001);
		Color d = Color.newCMYK(1.0f, 0, 0, 0);
		System.out.println(d);
		float delta = c.distanceTo(d);
		assertEquals(0.0f, delta);
	}

	public void testColorList() {
		ColorList list = new ColorList();
		for (int i = 0; i < 10; i++)
			list.add(Color.newHSV(MathUtils.random(1f), MathUtils.random(1f),
					MathUtils.random(1f)));
		ColorAccessCriteria criteria = ColorAccessCriteria.RED;
		ColorList sorted = list.sortByCriteria(criteria, false);
		Color prev = null;
		for (Color c : sorted) {
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
		for (Color c : sorted) {
			System.out.println(c);
		}
	}

	public void testColorListContains() {
		ColorList list = new ColorList();
		list.add(Color.RED);
		list.add(Color.GREEN);
		list.add(Color.BLUE);
		assertEquals(true, list.contains(Color.newRGB(0, 0, 1f)));
	}

	public void testStrategyName() {
		ArrayList<String> names = ColorTheoryFactory.getInstance()
				.getRegisteredNames();
		for (String name : names) {
			ColorTheoryStrategy strategy = ColorTheoryFactory.getInstance()
					.getStrategyForName(name);
			System.out.println(name);
			assertNotNull(strategy);
		}
	}

	public void testRangeContainment() {
		assertTrue(ColorRange.BRIGHT.contains(Color.RED));
		assertFalse(ColorRange.DARK.contains(Color.RED));
		assertFalse(ColorRange.BRIGHT.contains(Color.WHITE));
	}

	public void testHues() {
		assertTrue(ColorHue.GREEN.isPrimary());
		assertFalse(ColorHue.LIME.isPrimary());
		Color hue = Color.newHSV(ColorHue.CYAN, 0.5f, 0.2f);
		assertFalse(hue.isPrimary());
	}

	public void testThemes() {
		ColorTheme t = new ColorTheme("dark blue");
		for (ColorRange r : t.ranges) {
			System.out.println(r.getColor());
		}
	}
}
