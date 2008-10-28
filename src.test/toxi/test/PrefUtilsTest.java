package toxi.test;

import junit.framework.TestCase;
import toxi.util.datatypes.TypedProperties;

public class PrefUtilsTest extends TestCase {

	TypedProperties config = new TypedProperties();

	protected void setUp() throws Exception {
		assertTrue(config.load("test/test.properties"));
	}

	protected void tearDown() throws Exception {
		config = null;
	}

	public void testIntArray() {
		int[] items = config.getIntArray("test.intarray");
		assertEquals(23, items[0]);
		assertEquals(42, items[1]);
		assertEquals(88, items[2]);
		assertEquals(-12, items[3]);
	}

	public void testFloatArray() {
		float[] items = config.getFloatArray("test.floatarray");
		assertEquals(3.1415926f, items[0]);
		// 2nd item is NaN, so should be ignored
		// so 23.42 becomes 2nd array item
		assertEquals(23.42f, items[1]);
	}

	public void testEmptyArray() {
		int[] items = config.getIntArray("test.emptyarray");
		assertEquals(0, items.length);
	}
}
