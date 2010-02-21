package toxi.sim.dla;

import java.util.Comparator;

import toxi.geom.Line3D;
import toxi.geom.Vec3D;

/**
 * This comparator sorts segments based on their mid-point's distance to a given
 * origin point. This creates a circular growth. The order can be reversed via a
 * constructor flag and then causes the DLA system to grow from the outside
 * towards the given origin point.
 */
public class RadialDistanceOrder implements Comparator<Line3D> {

    public Vec3D origin;
    public boolean isFlipped;

    public RadialDistanceOrder() {
        this(new Vec3D(), false);
    }

    public RadialDistanceOrder(Vec3D origin, boolean isFlipped) {
        this.origin = origin;
        this.isFlipped = isFlipped;
    }

    public int compare(Line3D a, Line3D b) {
        float da = origin.sub(a.getMidPoint()).magSquared();
        float db = origin.sub(b.getMidPoint()).magSquared();
        if (isFlipped) {
            da *= -1;
            db *= -1;
        }
        if (da < db) {
            return -1;
        }
        if (da > db) {
            return 1;
        }
        return 0;
    }
}