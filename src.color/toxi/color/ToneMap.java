package toxi.color;

import toxi.math.InterpolateStrategy;
import toxi.math.ScaleMap;

public class ToneMap {

	public ScaleMap map;
	public ColorList colors;

	public ToneMap(float a, float b, ColorGradient gradient) {
		this(a, b, gradient.calcGradient());
	}

	public ToneMap(float a, float b, ColorList c) {
		map = new ScaleMap(a, b, 0, c.size() - 1);
		colors = c;
	}

	public int getARGBToneFor(float t) {
		return colors.get((int) map.getClippedValueFor(t)).toARGB();
	}

	public ReadonlyTColor getToneFor(float t) {
		return colors.get((int) map.getClippedValueFor(t));
	}

	public void setMapFunction(InterpolateStrategy func) {
		map.setMapFunction(func);
	}
}
