package toxi.volume;

import toxi.math.MathUtils;

public class PeakBrush implements BrushMode {

    public final float apply(float orig, float brush) {
        return MathUtils.max(orig, brush);
    }
}
