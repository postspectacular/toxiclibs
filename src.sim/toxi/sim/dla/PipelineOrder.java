package toxi.sim.dla;

import java.util.Comparator;

import toxi.geom.Line3D;

/**
 * A dummy comparator enforcing a FIFO order of segments, i.e. the order they
 * were added to the set.
 */
public class PipelineOrder implements Comparator<Line3D> {

    public int compare(Line3D a, Line3D b) {
        return 1;
    }
}