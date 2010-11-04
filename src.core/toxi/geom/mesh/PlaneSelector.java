package toxi.geom.mesh;

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
    public VertexSelector selectVertices() {
        clearSelection();
        for (Vertex v : mesh.getVertices()) {
            if (plane.classifyPoint(v, tolerance) == classifier) {
                selection.add(v);
            }
        }
        return this;
    }
}
