package toxi.geom.mesh;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Level;

import toxi.geom.Intersector3D;
import toxi.geom.IsectData3D;
import toxi.geom.Line3D;
import toxi.geom.Matrix4x4;
import toxi.geom.Quaternion;
import toxi.geom.ReadonlyVec3D;
import toxi.geom.Vec2D;
import toxi.geom.Vec3D;

/**
 * A class to dynamically build, manipulate & export triangle meshes. Meshes are
 * build face by face. The class automatically re-uses existing vertices and can
 * create smooth vertex normals. Vertices and faces are directly accessible for
 * speed & convenience.
 */
public class WETriangleMesh extends TriangleMesh implements Intersector3D {

    /**
     * WEVertex buffer & lookup index when adding new faces
     */
    public LinkedHashMap<Line3D, WingedEdge> edges;

    private Line3D edgeCheck = new Line3D(new Vec3D(), new Vec3D());

    public WETriangleMesh() {
        this("untitled");
    }

    /**
     * Creates a new mesh instance with an initial buffer size of 1000 vertices
     * & 3000 faces.
     * 
     * @param name
     *            mesh name
     */
    public WETriangleMesh(String name) {
        this(name, 1000, 3000);
    }

    /**
     * Creates a new mesh instance with the given initial buffer sizes. These
     * numbers are no limits and the mesh can be smaller or grow later on.
     * They're only used to initialise the underlying collections.
     * 
     * @param name
     *            mesh name
     * @param numV
     *            initial vertex buffer size
     * @param numF
     *            initial face list size
     */
    public WETriangleMesh(String name, int numV, int numF) {
        super(name, numV, numF);
        edges = new LinkedHashMap<Line3D, WingedEdge>(numV);
    }

    public WETriangleMesh addFace(Vec3D a, Vec3D b, Vec3D c) {
        return addFace(a, b, c, null, null, null, null);
    }

    public WETriangleMesh addFace(Vec3D a, Vec3D b, Vec3D c, Vec2D uvA,
            Vec2D uvB, Vec2D uvC) {
        return addFace(a, b, c, null, uvA, uvB, uvC);
    }

    public WETriangleMesh addFace(Vec3D a, Vec3D b, Vec3D c, Vec3D n) {
        return addFace(a, b, c, n, null, null, null);
    }

    public WETriangleMesh addFace(Vec3D a, Vec3D b, Vec3D c, Vec3D n,
            Vec2D uvA, Vec2D uvB, Vec2D uvC) {
        WEVertex va = checkVertex(a);
        WEVertex vb = checkVertex(b);
        WEVertex vc = checkVertex(c);
        if (va.id == vb.id || va.id == vc.id || vb.id == vc.id) {
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("ignorning invalid face: " + a + "," + b + "," + c);
            }
        } else {
            if (n != null) {
                Vec3D nc = va.sub(vc).crossSelf(va.sub(vb)).normalize();
                if (n.dot(nc) < 0) {
                    WEVertex t = va;
                    va = vb;
                    vb = t;
                }
            }
            WEFace f = new WEFace(va, vb, vc, uvA, uvB, uvC);
            faces.add(f);
            numFaces++;
            updateEdge(va, vb, f);
            updateEdge(vb, vc, f);
            updateEdge(vc, va, f);
        }
        return this;
    }

    /**
     * Adds all faces from the given mesh to this one.
     * 
     * @param m
     *            source mesh instance
     */
    public WETriangleMesh addMesh(TriangleMesh m) {
        for (Face f : m.faces) {
            addFace(f.a, f.b, f.c, f.uvA, f.uvB, f.uvC);
        }
        return this;
    }

    /**
     * Adds all faces from the given mesh to this one.
     * 
     * @param m
     *            source mesh instance
     */
    public WETriangleMesh addMesh(WETriangleMesh m) {
        for (Face f : m.faces) {
            addFace(f.a, f.b, f.c, f.uvA, f.uvB, f.uvC);
        }
        return this;
    }

    private final WEVertex checkVertex(Vec3D v) {
        WEVertex vertex = (WEVertex) vertices.get(v);
        if (vertex == null) {
            vertex = createVertex(v, numVertices);
            vertices.put(vertex, vertex);
            numVertices++;
        }
        return vertex;
    }

    /**
     * Clears all counters, and vertex & face buffers.
     */
    public WETriangleMesh clear() {
        super.clear();
        edges.clear();
        return this;
    }

    /**
     * Creates a deep clone of the mesh. The new mesh name will have "-copy" as
     * suffix.
     * 
     * @return new mesh instance
     */
    public WETriangleMesh copy() {
        WETriangleMesh m =
                new WETriangleMesh(name + "-copy", numVertices, numFaces);
        for (Face f : faces) {
            m.addFace(f.a, f.b, f.c, f.uvA, f.uvB, f.uvC);
        }
        return m;
    }

    protected WEVertex createVertex(Vec3D v, int id) {
        return new WEVertex(v, id);
    }

    /**
     * Flips the vertex ordering between clockwise and anti-clockwise. WEFace
     * normals are updated automatically too.
     * 
     * @return itself
     */
    public WETriangleMesh flipVertexOrder() {
        super.flipVertexOrder();
        return this;
    }

    public WETriangleMesh flipYAxis() {
        super.flipYAxis();
        return this;
    }

    public WEVertex getClosestVertexToPoint(ReadonlyVec3D p) {
        return (WEVertex) super.getClosestVertexToPoint(p);
    }

    public IsectData3D getIntersectionData() {
        return intersector.getIntersectionData();
    }

    public WETriangleMesh getRotatedAroundAxis(Vec3D axis, float theta) {
        return copy().rotateAroundAxis(axis, theta);
    }

    public WETriangleMesh getRotatedX(float theta) {
        return copy().rotateX(theta);
    }

    public WETriangleMesh getRotatedY(float theta) {
        return copy().rotateY(theta);
    }

    public WETriangleMesh getRotatedZ(float theta) {
        return copy().rotateZ(theta);
    }

    public WETriangleMesh getScaled(float scale) {
        return copy().scale(scale);
    }

    public WETriangleMesh getScaled(Vec3D scale) {
        return copy().scale(scale);
    }

    public WETriangleMesh getTranslated(Vec3D trans) {
        return copy().translate(trans);
    }

    public WEVertex getVertexAtPoint(Vec3D v) {
        return (WEVertex) vertices.get(v);
    }

    public WEVertex getVertexForID(int id) {
        Vertex vertex = null;
        for (Vertex v : vertices.values()) {
            if (v.id == id) {
                vertex = v;
                break;
            }
        }
        return (WEVertex) vertex;
    }

    /**
     * Rotates the mesh in such a way so that its "forward" axis is aligned with
     * the given direction. This version uses the positive Z-axis as default
     * forward direction.
     * 
     * @param dir
     *            new target direction to point in
     * @return itself
     */
    public WETriangleMesh pointTowards(ReadonlyVec3D dir) {
        return transform(Quaternion.getAlignmentQuat(dir, Vec3D.Z_AXIS)
                .toMatrix4x4(matrix), true);
    }

    /**
     * Rotates the mesh in such a way so that its "forward" axis is aligned with
     * the given direction. This version allows to specify the forward
     * direction.
     * 
     * @param dir
     *            new target direction to point in
     * @param forward
     *            current forward axis
     * @return itself
     */
    public WETriangleMesh pointTowards(ReadonlyVec3D dir, ReadonlyVec3D forward) {
        return transform(Quaternion.getAlignmentQuat(dir, forward).toMatrix4x4(
                matrix), true);
    }

    public void rebuildIndex() {
        LinkedHashMap<Vec3D, Vertex> newV =
                new LinkedHashMap<Vec3D, Vertex>(vertices.size());
        for (Vertex v : vertices.values()) {
            newV.put(v, v);
        }
        vertices = newV;
        LinkedHashMap<Line3D, WingedEdge> newE =
                new LinkedHashMap<Line3D, WingedEdge>(edges.size());
        for (WingedEdge e : edges.values()) {
            newE.put(e, e);
        }
        edges = newE;
    }

    private void removeEdge(WingedEdge e) {
        e.remove();
        for (WEFace f : e.faces) {
            removeFace(f);
        }
        if (edges.remove(edgeCheck.set(e.a, e.b)) == e) {
            logger.fine("removed edge: " + e);
        } else {
            throw new IllegalStateException("can't remove edge");
        }
    }

    public void removeFace(WEFace f) {
        faces.remove(f);
        for (WingedEdge e : f.edges) {
            e.faces.remove(f);
            if (e.faces.size() == 0) {
                removeEdge(e);
            }
        }
    }

    @Override
    public WETriangleMesh rotateAroundAxis(Vec3D axis, float theta) {
        return rotateAroundAxis(axis, theta);
    }

    @Override
    public WETriangleMesh rotateX(float theta) {
        return rotateX(theta);
    }

    @Override
    public WETriangleMesh rotateY(float theta) {
        return rotateY(theta);
    }

    @Override
    public WETriangleMesh rotateZ(float theta) {
        return rotateZ(theta);
    }

    @Override
    public WETriangleMesh scale(float scale) {
        return scale(scale);
    }

    @Override
    public WETriangleMesh scale(Vec3D scale) {
        return scale(scale);
    }

    public void splitEdge(ReadonlyVec3D a, ReadonlyVec3D b,
            SubdivisionStrategy subDiv) {
        WingedEdge e = edges.get(edgeCheck.set(a, b));
        if (e != null) {
            splitEdge(e, subDiv);
        }
    }

    public void splitEdge(WingedEdge e, SubdivisionStrategy subDiv) {
        List<Vec3D> mid = subDiv.computeSplitPoints(e);
        splitFace(e.faces.get(0), e, mid);
        if (e.faces.size() > 1) {
            splitFace(e.faces.get(1), e, mid);
        }
        removeEdge(e);
    }

    protected void splitFace(WEFace f, WingedEdge e, List<Vec3D> midPoints) {
        Vec3D p = null;
        for (int i = 0; i < 3; i++) {
            WingedEdge ec = f.edges.get(i);
            if (!ec.equals(e)) {
                if (ec.a.equals(e.a) || ec.a.equals(e.b)) {
                    p = ec.b;
                } else {
                    p = ec.a;
                }
                break;
            }
        }
        Vec3D prev = null;
        for (int i = 0, num = midPoints.size(); i < num; i++) {
            Vec3D mid = midPoints.get(i);
            if (i == 0) {
                addFace(p, e.a, mid, f.normal);
            } else {
                addFace(p, prev, mid, f.normal);
            }
            if (i == num - 1) {
                addFace(p, mid, e.b, f.normal);
            }
            prev = mid;
        }
    }

    public void subdivide() {
        subdivide(0);
    }

    public void subdivide(float minLength) {
        subdivide(new MidpointSubdivision(), minLength * minLength);
    }

    public void subdivide(SubdivisionStrategy subDiv, float minLength) {
        List<WingedEdge> origEdges = new ArrayList<WingedEdge>(edges.values());
        Collections.sort(origEdges, subDiv.getEdgeOrdering());
        for (WingedEdge e : origEdges) {
            if (edges.containsKey(e)) {
                if (e.getLengthSquared() >= minLength) {
                    splitEdge(e, subDiv);
                }
            }
        }
    }

    @Override
    public String toString() {
        return "WETriangleMesh: " + name + " vertices: " + getNumVertices()
                + " faces: " + getNumFaces();
    }

    /**
     * Applies the given matrix transform to all mesh vertices and updates all
     * face normals.
     * 
     * @param mat
     * @return itself
     */
    public WETriangleMesh transform(Matrix4x4 mat) {
        return transform(mat, true);
    }

    /**
     * Applies the given matrix transform to all mesh vertices. If the
     * updateNormals flag is true, all face normals are updated automatically,
     * however vertex normals need a manual update.
     * 
     * @param mat
     * @param updateNormals
     * @return itself
     */
    public WETriangleMesh transform(Matrix4x4 mat, boolean updateNormals) {
        for (Vertex v : vertices.values()) {
            mat.applyToSelf(v);
        }
        // FIXME need to figure out why transform breaks edge lookups
        List<WingedEdge> origEdges = new ArrayList<WingedEdge>(edges.values());
        edges.clear();
        for (WingedEdge e : origEdges) {
            edges.put(e, e);
        }
        origEdges = null;
        if (updateNormals) {
            computeFaceNormals();
        }
        return this;
    }

    public WETriangleMesh translate(Vec3D trans) {
        return transform(matrix.identity().translateSelf(trans));
    }

    protected void updateEdge(WEVertex va, WEVertex vb, WEFace f) {
        edgeCheck.set(va, vb);
        WingedEdge e = edges.get(edgeCheck);
        if (e != null) {
            e.addFace(f);
        } else {
            e = new WingedEdge(va, vb, f, edges.size());
            edges.put(e, e);
            va.addEdge(e);
            vb.addEdge(e);
        }
        f.addEdge(e);
    }
}
