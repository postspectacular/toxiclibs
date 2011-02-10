package toxi.volume;

public class MultiplyBrush implements BrushMode {

    public final float apply(float orig, float brush) {
        return orig * brush;
    }

}
