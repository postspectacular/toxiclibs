package toxi.geom.mesh.subdiv;

import java.util.Comparator;

import toxi.geom.mesh.WingedEdge;

public class FaceCountComparator implements Comparator<WingedEdge> {

    public int compare(WingedEdge e1, WingedEdge e2) {
        return -(e1.faces.size() - e2.faces.size());
    }
}
