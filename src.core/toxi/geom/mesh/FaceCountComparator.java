package toxi.geom.mesh;

import java.util.Comparator;

public class FaceCountComparator implements Comparator<WingedEdge> {

    public int compare(WingedEdge e1, WingedEdge e2) {
        return -(e1.faces.size() - e2.faces.size());
    }
}
