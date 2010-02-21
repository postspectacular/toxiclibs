package toxi.sim.dla;

import java.util.Comparator;

import toxi.geom.Line3D;

/**
 * This comparator sorts segments based on their midpoint's Y position. Positive
 * Y axis is assumed the default up direction, but can be changed via the
 * constructor.
 */
public class BottomUpOrder implements Comparator<Line3D> {

    public boolean isFlipped;

    public BottomUpOrder(boolean isFlipped) {
        this.isFlipped = isFlipped;
    }

    public int compare(Line3D a, Line3D b) {
        float ya = a.getMidPoint().y;
        float yb = b.getMidPoint().y;
        if (isFlipped) {
            ya *= -1;
            yb *= -1;
        }
        if (ya < yb) {
            return -1;
        }
        if (ya > yb) {
            return 1;
        }
        return 0;
    }
}
