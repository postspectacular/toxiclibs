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

    public ToneMap(float a, float b, float c, float d, ReadonlyTColor colA,
            ReadonlyTColor colB) {
        ColorGradient g = new ColorGradient();
        g.addColorAt(c, colA);
        g.addColorAt(d, colB);
        colors = g.calcGradient();
        map = new ScaleMap(a, b, 0, colors.size() - 1);
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
