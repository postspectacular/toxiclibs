package toxi.test;

import processing.core.PApplet;
import toxi.image.util.ARGBGradient;
import junit.framework.TestCase;

public class GradientTest extends TestCase {

	public void testGradient() {
		ARGBGradient grad = new ARGBGradient();
		grad.addColorAt(0, 0xffff0000);
		grad.addColorAt(500, 0x800000ff);
		int[] cols=grad.calcGradient(0, 502);
		grad.setMaxDither(0.0066f);
		int[] dither=grad.calcGradient(0, 502);
		for(int i=0; i<cols.length; i++) {
			System.out.println(i+": "+PApplet.hex(cols[i],8)+" "+PApplet.hex(dither[i],8));
		}
	}
}
