package toxi.geom.mesh;

import java.util.Collection;
import java.util.HashSet;

import toxi.geom.Plane;
import toxi.geom.Plane.Classifier;

public class PlaneSelector extends VertexSelector {

    public Plane plane;
    public float tolerance;
    public Classifier classifier;

    public PlaneSelector(Mesh3D mesh, Plane plane, float tolerance,
            Plane.Classifier classifier) {
        super(mesh);
        this.plane = plane;
        this.tolerance = tolerance;
        this.classifier = classifier;
    }

    @Override
    public Collection<Vertex> selectVertices() {
        if (selection == null) {
            selection = new HashSet<Vertex>();
        }
        for (Vertex v : mesh.getVertices()) {
            if (plane.classifyPoint(v, tolerance) == classifier) {
                selection.add(v);
            }
        }
        return selection;
    }
}
