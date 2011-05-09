package toxi.processing;

import toxi.geom.Line2D;
import toxi.geom.ReadonlyVec2D;
import toxi.geom.Vec2D;

/**
 * {@link Line2DRenderModifier} implementation to draw a dashed line with
 * customizable step length.
 */
public class DashedLineModifier implements Line2DRenderModifier {

    public float length;

    /**
     * Constructs a new instance
     * 
     * @param length
     *            step length
     */
    public DashedLineModifier(float length) {
        this.length = length;
    }

    public void apply(ToxiclibsSupport gfx, ReadonlyVec2D a, ReadonlyVec2D b) {
        int i = 0;
        Vec2D prev = null;
        for (Vec2D p : new Line2D(a, b).splitIntoSegments(null, length, true)) {
            if (i % 2 == 0) {
                prev = p;
            } else {
                gfx.line(prev, p);
            }
            i++;
        }
    }

}
