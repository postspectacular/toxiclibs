package toxi.test;

import java.util.ArrayList;

import junit.framework.TestCase;
import toxi.color.AccessCriteria;
import toxi.color.ColorList;
import toxi.color.ColorRange;
import toxi.color.ColorTheme;
import toxi.color.Hue;
import toxi.color.NamedColor;
import toxi.color.ReadonlyTColor;
import toxi.color.TColor;
import toxi.color.theory.ColorTheoryRegistry;
import toxi.color.theory.ColorTheoryStrategy;
import toxi.math.MathUtils;

public class ColorTest extends TestCase {

	public void testColor() {
		ReadonlyTColor c = TColor.newHex("00ffff");
		System.out.println(c);
		assertEquals(0.5, c.hue(), 0.001);
		assertEquals(1.0, c.brightness(), 0.001);
		TColor d = TColor.newCMYK(1.0f, 0, 0, 0);
		System.out.println(d);
		float delta = c.distanceToHSV(d);
		assertEquals(0.0f, delta);
	}

	public void testColorList() {
		ColorList list = new ColorList();
		for (int i = 0; i < 10; i++)
			list.add(TColor.newHSV(MathUtils.random(1f), MathUtils.random(1f),
					MathUtils.random(1f)));
		AccessCriteria criteria = AccessCriteria.RED;
		ColorList sorted = list.sortByCriteria(criteria, false);
		ReadonlyTColor prev = null;
		for (ReadonlyTColor c : sorted) {
			System.out.println(c);
			if (prev != null) {
				assertTrue(prev.getComponentValue(criteria) <= c
						.getComponentValue(criteria));
			}
			prev = c;
		}
		System.out.println("cluster sort...");
		sorted = list.clusterSort(AccessCriteria.HUE,
				AccessCriteria.BRIGHTNESS, 3, false);
		for (ReadonlyTColor c : sorted) {
			System.out.println(c);
		}
	}

	public void testColorListContains() {
		ColorList list = new ColorList();
		list.add(TColor.RED);
		list.add(TColor.GREEN);
		list.add(TColor.BLUE);
		assertEquals(true, list.contains(TColor.newRGB(0, 0, 1f)));
	}

	public void testHues() {
		assertTrue(Hue.GREEN.isPrimary());
		assertFalse(Hue.LIME.isPrimary());
		ReadonlyTColor hue = TColor.newHSV(Hue.CYAN, 0.5f, 0.2f);
		assertFalse(hue.isPrimary());
		String hueName = "pink";
		Hue h = Hue.getForName(hueName);
		assertEquals(hueName, h.getName());
		h = Hue.getClosest(100 / 360.0f, false);
		assertEquals("lime", h.getName());
		h = Hue.getClosest(100 / 360.0f, true);
		assertEquals("green", h.getName());
	}

	public void testNamedColors() {
		ReadonlyTColor c = NamedColor.getForName("cyan");
		assertEquals(NamedColor.CYAN, c);
	}

	public void testRangeContainment() {
		assertTrue(ColorRange.BRIGHT.contains(TColor.RED));
		assertFalse(ColorRange.DARK.contains(TColor.RED));
		assertFalse(ColorRange.BRIGHT.contains(TColor.WHITE));
	}

	public void testStrategyName() {
		ArrayList<String> names = ColorTheoryRegistry.getRegisteredNames();
		for (String name : names) {
			ColorTheoryStrategy strategy = ColorTheoryRegistry
					.getStrategyForName(name);
			System.out.println(name);
			assertNotNull(strategy);
		}
	}

	public void testThemes() {
		ColorTheme t = new ColorTheme("test");
		t.addRange("dark blue", 0.5f);
		t.addRange("soft orange", 0.5f);
		ColorList cols = t.getColors(1000);
		for (ReadonlyTColor c : cols) {
			assertNotNull(c);
		}
	}
}
