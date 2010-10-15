package toxi.geom.mesh;

import java.util.ArrayList;
import java.util.List;

import toxi.geom.Polygon2D;
import toxi.geom.Vec3D;
import toxi.geom.Vec3D.Axis;
import toxi.math.MathUtils;

public class PlaneSelector implements VertexSelector {

    public Vec3D.Axis axis;
    public float axisValue;
    public float tolerance;

    public PlaneSelector(Axis axis, float axisValue, float tolerance) {
        this.axis = axis;
        this.axisValue = axisValue;
        this.tolerance = tolerance;
    }

    public List<Polygon2D> getConnectedShapes() {
        List<Polygon2D> shapes = new ArrayList<Polygon2D>();
        return shapes;
    }

    public List<Vertex> selectVertices(Mesh3D mesh, List<Vertex> selection) {
        if (selection == null) {
            selection = new ArrayList<Vertex>();
        }
        for (Vertex v : mesh.getVertices()) {
            if (MathUtils.abs(v.getComponent(axis) - axisValue) < tolerance) {
                selection.add(v);
            }
        }
        return selection;
    }
}
