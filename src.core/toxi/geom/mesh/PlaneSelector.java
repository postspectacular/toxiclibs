package toxi.geom.mesh;

import toxi.geom.Plane;
import toxi.geom.Plane.Classifier;

/**
 * A {@link VertexSelector} implementation for selecting vertices in relation to
 * a given {@link Plane}. Using a plane {@link Classifier} vertices can be
 * selected either: on the plane, in front or behind. A tolerance for this check
 * can be given too.
 * 
 */
public class PlaneSelector extends VertexSelector {

    public Plane plane;
    public float tolerance;
    public Classifier classifier;

    public PlaneSelector(Mesh3D mesh, Plane plane, Plane.Classifier classifier) {
        this(mesh, plane, classifier, 0.0001f);
    }

    public PlaneSelector(Mesh3D mesh, Plane plane, Plane.Classifier classifier,
            float tolerance) {
        super(mesh);
        this.plane = plane;
        this.classifier = classifier;
        this.tolerance = tolerance;
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
