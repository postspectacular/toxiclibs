package toxi.processing;

import toxi.geom.ReadonlyVec2D;
import toxi.geom.Vec2D;
import toxi.math.MathUtils;

/**
 * {@link Line2DRenderModifier} implementation to draw a line with arrow heads
 * (or alternatively ONLY draw the heads). The size of the arrow heads can be
 * customized as well as which ends they should be drawn at.
 */
public class ArrowModifier implements Line2DRenderModifier {

    /**
     * Arrow head type.
     */
    public enum Type {
        START,
        END,
        BOTH;
    }

    public float length;
    public float theta;
    public Type type;
    public boolean doDrawLine;

    /**
     * Constructs a new modifier instance. The line itself is drawn as well.
     * 
     * @param length
     *            length of arrow head
     * @param theta
     *            spread (in radians)
     * @param type
     *            type of arrow head(s)
     */
    public ArrowModifier(float length, float theta, Type type) {
        this(length, theta, type, true);
    }

    /**
     * Constructs a new modifier instance. If drawLine is set to true, the line
     * itself is drawn as well. If false, only the arrow heads are drawn.
     * 
     * @param length
     *            length of arrow head
     * @param theta
     *            spread (in radians)
     * @param type
     *            type of arrow head(s)
     * @param drawLine
     *            If false, only heads are drawn.
     */
    public ArrowModifier(float length, float theta, Type type, boolean drawLine) {
        super();
        this.length = length;
        this.theta = theta;
        this.type = type;
        this.doDrawLine = drawLine;
    }

    public void apply(ToxiclibsSupport gfx, ReadonlyVec2D a, ReadonlyVec2D b) {
        if (doDrawLine) {
            gfx.line(a, b);
        }
        if (type == Type.START || type == Type.BOTH) {
            Vec2D dir = b.sub(a).rotate(theta).normalizeTo(length);
            gfx.line(a, a.add(dir));
            dir.rotate(-theta * 2);
            gfx.line(a, a.add(dir));
        }
        if (type == Type.END || type == Type.BOTH) {
            Vec2D dir = b.sub(a).rotate(MathUtils.PI - theta)
                    .normalizeTo(length);
            gfx.line(b, b.add(dir));
            dir.rotate(theta * 2);
            gfx.line(b, b.add(dir));
        }
    }

}
