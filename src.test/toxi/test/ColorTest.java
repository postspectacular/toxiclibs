package toxi.test;

import java.util.ArrayList;

import junit.framework.TestCase;
import toxi.colour.AccessCriteria;
import toxi.colour.Colour;
import toxi.colour.ColourList;
import toxi.colour.ColourRange;
import toxi.colour.ColourTheme;
import toxi.colour.Hue;
import toxi.colour.NamedColour;
import toxi.colour.theory.ColorTheoryRegistry;
import toxi.colour.theory.ColorTheoryStrategy;
import toxi.math.MathUtils;

public class ColorTest extends TestCase {

	public void testColor() {
		Colour c = Colour.newHex("00ffff");
		System.out.println(c);
		assertEquals(0.5, c.hue(), 0.001);
		assertEquals(1.0, c.brightness(), 0.001);
		Colour d = Colour.newCMYK(1.0f, 0, 0, 0);
		System.out.println(d);
		float delta = c.distanceToHSV(d);
		assertEquals(0.0f, delta);
	}

	public void testColorList() {
		ColourList list = new ColourList();
		for (int i = 0; i < 10; i++)
			list.add(Colour.newHSV(MathUtils.random(1f), MathUtils.random(1f),
					MathUtils.random(1f)));
		AccessCriteria criteria = AccessCriteria.RED;
		ColourList sorted = list.sortByCriteria(criteria, false);
		Colour prev = null;
		for (Colour c : sorted) {
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
		for (Colour c : sorted) {
			System.out.println(c);
		}
	}

	public void testColorListContains() {
		ColourList list = new ColourList();
		list.add(Colour.RED);
		list.add(Colour.GREEN);
		list.add(Colour.BLUE);
		assertEquals(true, list.contains(Colour.newRGB(0, 0, 1f)));
	}

	public void testHues() {
		assertTrue(Hue.GREEN.isPrimary());
		assertFalse(Hue.LIME.isPrimary());
		Colour hue = Colour.newHSV(Hue.CYAN, 0.5f, 0.2f);
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
		Colour c = NamedColour.getForName("cyan");
		assertEquals(NamedColour.CYAN, c);
	}

	public void testRangeContainment() {
		assertTrue(ColourRange.BRIGHT.contains(Colour.RED));
		assertFalse(ColourRange.DARK.contains(Colour.RED));
		assertFalse(ColourRange.BRIGHT.contains(Colour.WHITE));
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
		ColourTheme t = new ColourTheme("test");
		t.addRange("dark blue", 0.5f);
		t.addRange("soft orange", 0.5f);
		ColourList cols = t.getColors(1000);
		for (Colour c : cols) {
			assertNotNull(c);
		}
	}
}
