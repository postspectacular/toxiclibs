package toxi.geom.mesh;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import toxi.geom.AABB;
import toxi.geom.Intersector3D;
import toxi.geom.IsectData3D;
import toxi.geom.Line3D;
import toxi.geom.Matrix4x4;
import toxi.geom.Quaternion;
import toxi.geom.Ray3D;
import toxi.geom.ReadonlyVec3D;
import toxi.geom.Sphere;
import toxi.geom.Triangle;
import toxi.geom.TriangleIntersector;
import toxi.geom.Vec2D;
import toxi.geom.Vec3D;
import toxi.math.MathUtils;

/**
 * A class to dynamically build, manipulate & export triangle meshes. Meshes are
 * build face by face. The class automatically re-uses existing vertices and can
 * create smooth vertex normals. Vertices and faces are directly accessible for
 * speed & convenience.
 */
public class WETriangleMesh implements Intersector3D {

    protected static final Logger logger =
            Logger.getLogger(WETriangleMesh.class.getName());

    /**
     * WEVertex buffer & lookup index when adding new faces
     */
    public LinkedHashMap<Vec3D, WEVertex> vertices;
    public LinkedHashMap<Line3D, WingedEdge> edges;

    /**
     * WEFace list
     */
    public final ArrayList<WEFace> faces;

    protected AABB bounds;
    protected Vec3D centroid = new Vec3D();
    protected int numVertices;
    protected int numFaces;
    protected int numEdges;

    private Matrix4x4 matrix = new Matrix4x4();
    protected TriangleIntersector intersector = new TriangleIntersector();

    private String name;

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
        this.name = name;
        faces = new ArrayList<WEFace>(numF);
        vertices = new LinkedHashMap<Vec3D, WEVertex>(numV, 1.75f, false);
        edges = new LinkedHashMap<Line3D, WingedEdge>();
    }

    /**
     * Adds the given 3 points as triangle face to the mesh. The assumed vertex
     * order is anti-clockwise.
     * 
     * @param a
     * @param b
     * @param c
     */
    public WETriangleMesh addFace(Vec3D a, Vec3D b, Vec3D c) {
        return addFace(a, b, c, null);
    }

    /**
     * Adds the given 3 points as triangle face to the mesh. The assumed vertex
     * order is anti-clockwise.
     * 
     * @param a
     * @param b
     * @param c
     */
    public WETriangleMesh addFace(Vec3D a, Vec3D b, Vec3D c, Vec2D uvA,
            Vec2D uvB, Vec2D uvC) {
        WEVertex va = checkVertex(a);
        WEVertex vb = checkVertex(b);
        WEVertex vc = checkVertex(c);
        if (va.id == vb.id || va.id == vc.id || vb.id == vc.id) {
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("ignorning invalid face: " + a + "," + b + "," + c);
            }
        } else {
            WEFace f = new WEFace(va, vb, vc, uvA, uvB, uvC);
            faces.add(f);
            numFaces++;
            updateEdge(va, vb, f);
            updateEdge(vb, vc, f);
            updateEdge(vc, va, f);
        }
        return this;
    }

    public WETriangleMesh addFace(Vec3D a, Vec3D b, Vec3D c, Vec3D n) {
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
            WEFace f = new WEFace(va, vb, vc);
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
        for (TriangleMesh.Face f : m.faces) {
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
        for (WEFace f : m.faces) {
            addFace(f.a, f.b, f.c, f.uvA, f.uvB, f.uvC);
        }
        return this;
    }

    /**
     * Centers the mesh around the given pivot point (the centroid of its AABB).
     * Method also updates & returns the new bounding box.
     * 
     * @param origin
     *            new centroid or null (defaults to {0,0,0})
     */
    public AABB center(ReadonlyVec3D origin) {
        getCentroid();
        Vec3D delta =
                origin != null ? origin.sub(centroid) : centroid.getInverted();
        for (WEVertex v : vertices.values()) {
            v.addSelf(delta);
        }
        getBoundingBox();
        return bounds;
    }

    private WEVertex checkVertex(Vec3D v) {
        WEVertex vertex = vertices.get(v);
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
        vertices.clear();
        faces.clear();
        bounds = null;
        numVertices = 0;
        numFaces = 0;
        return this;
    }

    /**
     * Re-calculates all face normals.
     */
    public WETriangleMesh computeFaceNormals() {
        for (WEFace f : faces) {
            f.normal = f.a.sub(f.c).crossSelf(f.a.sub(f.b)).normalize();
        }
        return this;
    }

    /**
     * Computes the smooth vertex normals for the entire mesh.
     */
    public WETriangleMesh computeVertexNormals() {
        for (WEVertex v : vertices.values()) {
            v.clearNormal();
        }
        for (WEFace f : faces) {
            f.a.addFaceNormal(f.normal);
            f.b.addFaceNormal(f.normal);
            f.c.addFaceNormal(f.normal);
        }
        for (WEVertex v : vertices.values()) {
            v.computeNormal();
        }
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
        for (WEFace f : faces) {
            m.addFace(f.a, f.b, f.c, f.uvA, f.uvB, f.uvC);
        }
        return m;
    }

    protected WEVertex createVertex(Vec3D v, int id) {
        return new WEVertex(v, id);
    }

    public void faceOutwards() {
        getCentroid();
        for (WEFace f : faces) {
            Vec3D n = f.getCentroid().sub(centroid).normalize();
            float dot = n.dot(f.normal);
            if (dot < 0) {
                f.flipVertexOrder();
            }
        }
    }

    /**
     * Flips the vertex ordering between clockwise and anti-clockwise. WEFace
     * normals are updated automatically too.
     * 
     * @return itself
     */
    public WETriangleMesh flipVertexOrder() {
        for (WEFace f : faces) {
            f.flipVertexOrder();
        }
        return this;
    }

    /**
     * Flips all vertices along the Y axis and reverses the vertex ordering of
     * all faces to compensate and keep the direction of normals intact.
     * 
     * @return itself
     */
    public WETriangleMesh flipYAxis() {
        transform(new Matrix4x4().scaleSelf(1, -1, 1));
        flipVertexOrder();
        return this;
    }

    /**
     * Computes & returns the axis-aligned bounding box of the mesh.
     * 
     * @return bounding box
     */
    public AABB getBoundingBox() {
        final Vec3D minBounds = Vec3D.MAX_VALUE.copy();
        final Vec3D maxBounds = Vec3D.MIN_VALUE.copy();
        for (WEVertex v : vertices.values()) {
            minBounds.minSelf(v);
            maxBounds.maxSelf(v);
        }
        bounds = AABB.fromMinMax(minBounds, maxBounds);
        return bounds;
    }

    /**
     * Computes & returns the bounding sphere of the mesh. The origin of the
     * sphere is the mesh's centroid.
     * 
     * @return bounding sphere
     */
    public Sphere getBoundingSphere() {
        float radius = 0;
        getCentroid();
        for (WEVertex v : vertices.values()) {
            radius = MathUtils.max(radius, v.distanceToSquared(centroid));
        }
        return new Sphere(centroid, (float) Math.sqrt(radius));
    }

    /**
     * Computes the mesh centroid, the average position of all vertices.
     * 
     * @return centre point
     */
    public Vec3D getCentroid() {
        centroid.clear();
        for (Vec3D v : vertices.values()) {
            centroid.addSelf(v);
        }
        return centroid.scaleSelf(1f / numVertices).copy();
    }

    public WEVertex getClosestVertexToPoint(ReadonlyVec3D p) {
        WEVertex closest = null;
        float minDist = Float.MAX_VALUE;
        for (WEVertex v : vertices.values()) {
            float d = v.distanceToSquared(p);
            if (d < minDist) {
                closest = v;
                minDist = d;
            }
        }
        return closest;
    }

    /**
     * @return
     */
    public float[] getFaceNormalsAsArray() {
        return getFaceNormalsAsArray(null, 0, 3);
    }

    /**
     * Creates an array of unravelled normal coordinates. For each vertex the
     * normal vector of its parent face is used. This method can be used to
     * translate the internal mesh data structure into a format suitable for
     * OpenGL WEVertex Buffer Objects (by choosing stride=4). For more detail,
     * please see {@link #getMeshAsVertexArray(float[], int, int)}
     * 
     * @see #getMeshAsVertexArray(float[], int, int)
     * 
     * @param normals
     *            existing float array or null to automatically create one
     * @param offset
     *            start index in array to place normals
     * @param stride
     *            stride/alignment setting for individual coordinates (min value
     *            = 3)
     * @return array of xyz normal coords
     */
    public float[] getFaceNormalsAsArray(float[] normals, int offset, int stride) {
        stride = MathUtils.max(stride, 3);
        if (normals == null) {
            normals = new float[faces.size() * 3 * stride];
        }
        int i = offset;
        for (WEFace f : faces) {
            normals[i] = f.normal.x;
            normals[i + 1] = f.normal.y;
            normals[i + 2] = f.normal.z;
            i += stride;
            normals[i] = f.normal.x;
            normals[i + 1] = f.normal.y;
            normals[i + 2] = f.normal.z;
            i += stride;
            normals[i] = f.normal.x;
            normals[i + 1] = f.normal.y;
            normals[i + 2] = f.normal.z;
            i += stride;
        }
        return normals;
    }

    /**
     * Builds an array of vertex indices of all faces. Each vertex ID
     * corresponds to its position in the {@link #vertices} HashMap. The
     * resulting array will be 3 times the face count.
     * 
     * @return array of vertex indices
     */
    public int[] getFacesAsArray() {
        int[] faceList = new int[faces.size() * 3];
        int i = 0;
        for (WEFace f : faces) {
            faceList[i++] = f.a.id;
            faceList[i++] = f.b.id;
            faceList[i++] = f.c.id;
        }
        return faceList;
    }

    public IsectData3D getIntersectionData() {
        return intersector.getIntersectionData();
    }

    /**
     * Creates an array of unravelled vertex coordinates for all faces using a
     * stride setting of 3, resulting in a gap-less serialized version of all
     * mesh vertex coordinates.
     * 
     * @see #getMeshAsVertexArray(float[], int, int)
     * @return float array of vertex coordinates
     */
    public float[] getMeshAsVertexArray() {
        return getMeshAsVertexArray(null, 0, 3);
    }

    /**
     * Creates an array of unravelled vertex coordinates for all faces. This
     * method can be used to translate the internal mesh data structure into a
     * format suitable for OpenGL WEVertex Buffer Objects (by choosing
     * stride=4). The order of the array will be as follows:
     * 
     * <ul>
     * <li>WEFace 1:
     * <ul>
     * <li>WEVertex #1
     * <ul>
     * <li>x</li>
     * <li>y</li>
     * <li>z</li>
     * <li>[optional empty indices to match stride setting]</li>
     * </ul>
     * </li>
     * <li>WEVertex #2
     * <ul>
     * <li>x</li>
     * <li>y</li>
     * <li>z</li>
     * <li>[optional empty indices to match stride setting]</li>
     * </ul>
     * </li>
     * <li>WEVertex #3
     * <ul>
     * <li>x</li>
     * <li>y</li>
     * <li>z</li>
     * <li>[optional empty indices to match stride setting]</li>
     * </ul>
     * </li>
     * </ul>
     * <li>WEFace 2:
     * <ul>
     * <li>WEVertex #1</li>
     * <li>...etc.</li>
     * </ul>
     * </ul>
     * 
     * @param verts
     *            an existing target array or null to automatically create one
     * @param offset
     *            start index in arrtay to place vertices
     * @param stride
     *            stride/alignment setting for individual coordinates
     * @return array of xyz vertex coords
     */
    public float[] getMeshAsVertexArray(float[] verts, int offset, int stride) {
        stride = MathUtils.max(stride, 3);
        if (verts == null) {
            verts = new float[faces.size() * 3 * stride];
        }
        int i = offset;
        for (WEFace f : faces) {
            verts[i] = f.a.x;
            verts[i + 1] = f.a.y;
            verts[i + 2] = f.a.z;
            i += stride;
            verts[i] = f.b.x;
            verts[i + 1] = f.b.y;
            verts[i + 2] = f.b.z;
            i += stride;
            verts[i] = f.c.x;
            verts[i + 1] = f.c.y;
            verts[i + 2] = f.c.z;
            i += stride;
        }
        return verts;
    }

    /**
     * Returns the number of triangles used.
     * 
     * @return face count
     */
    public int getNumFaces() {
        return numFaces;
    }

    /**
     * Returns the number of actual vertices used (unique vertices).
     * 
     * @return vertex count
     */
    public int getNumVertices() {
        return numVertices;
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

    public float[] getUniqueVerticesAsArray() {
        float[] verts = new float[numVertices * 3];
        int i = 0;
        for (WEVertex v : vertices.values()) {
            verts[i++] = v.x;
            verts[i++] = v.y;
            verts[i++] = v.z;
        }
        return verts;
    }

    public WEVertex getVertexAtPoint(Vec3D v) {
        return vertices.get(v);
    }

    public WEVertex getVertexForID(int id) {
        WEVertex vertex = null;
        for (WEVertex v : vertices.values()) {
            if (v.id == id) {
                vertex = v;
                break;
            }
        }
        return vertex;
    }

    /**
     * @see #getVertexNormalsAsArray(float[], int, int)
     * @return array of xyz normal coords
     */
    public float[] getVertexNormalsAsArray() {
        return getVertexNormalsAsArray(null, 0, 3);
    }

    /**
     * Creates an array of unravelled vertex normal coordinates for all faces.
     * This method can be used to translate the internal mesh data structure
     * into a format suitable for OpenGL WEVertex Buffer Objects (by choosing
     * stride=4). For more detail, please see
     * {@link #getMeshAsVertexArray(float[], int, int)}
     * 
     * @see #getMeshAsVertexArray(float[], int, int)
     * 
     * @param normals
     *            existing float array or null to automatically create one
     * @param offset
     *            start index in array to place normals
     * @param stride
     *            stride/alignment setting for individual coordinates (min value
     *            = 3)
     * @return array of xyz normal coords
     */
    public float[] getVertexNormalsAsArray(float[] normals, int offset,
            int stride) {
        stride = MathUtils.max(stride, 3);
        if (normals == null) {
            normals = new float[faces.size() * 3 * stride];
        }
        int i = offset;
        for (WEFace f : faces) {
            normals[i] = f.a.normal.x;
            normals[i + 1] = f.a.normal.y;
            normals[i + 2] = f.a.normal.z;
            i += stride;
            normals[i] = f.b.normal.x;
            normals[i + 1] = f.b.normal.y;
            normals[i + 2] = f.b.normal.z;
            i += stride;
            normals[i] = f.c.normal.x;
            normals[i + 1] = f.c.normal.y;
            normals[i + 2] = f.c.normal.z;
            i += stride;
        }
        return normals;
    }

    protected void handleSaveAsSTL(STLWriter stl, boolean useFlippedY) {
        if (useFlippedY) {
            stl.setScale(new Vec3D(1, -1, 1));
            for (WEFace f : faces) {
                stl.face(f.a, f.b, f.c, f.normal, STLWriter.DEFAULT_RGB);
            }
        } else {
            for (WEFace f : faces) {
                stl.face(f.b, f.a, f.c, f.normal, STLWriter.DEFAULT_RGB);
            }
        }
        stl.endSave();
        logger.info(numFaces + " faces written");
    }

    public boolean intersectsRay(Ray3D ray) {
        Triangle tri = intersector.getTriangle();
        for (WEFace f : faces) {
            tri.a = f.a;
            tri.b = f.b;
            tri.c = f.c;
            if (intersector.intersectsRay(ray)) {
                return true;
            }
        }
        return false;
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
        LinkedHashMap<Vec3D, WEVertex> newV =
                new LinkedHashMap<Vec3D, WEVertex>(vertices.size());
        for (WEVertex v : vertices.values()) {
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

    public WETriangleMesh rotateAroundAxis(Vec3D axis, float theta) {
        return transform(matrix.identity().rotateAroundAxis(axis, theta));
    }

    public WETriangleMesh rotateX(float theta) {
        return transform(matrix.identity().rotateX(theta));
    }

    public WETriangleMesh rotateY(float theta) {
        return transform(matrix.identity().rotateY(theta));
    }

    public WETriangleMesh rotateZ(float theta) {
        return transform(matrix.identity().rotateZ(theta));
    }

    /**
     * Saves the mesh as OBJ format by appending it to the given mesh
     * {@link OBJWriter} instance.
     * 
     * @param obj
     */
    public void saveAsOBJ(OBJWriter obj) {
        int vOffset = obj.getCurrVertexOffset() + 1;
        int nOffset = obj.getCurrNormalOffset() + 1;
        logger.info("writing OBJMesh: " + this.toString());
        obj.newObject(name);
        // vertices
        for (WEVertex v : vertices.values()) {
            obj.vertex(v);
        }
        // normals
        for (WEVertex v : vertices.values()) {
            obj.normal(v.normal);
        }
        // faces
        for (WEFace f : faces) {
            obj.faceWithNormals(f.b.id + vOffset, f.a.id + vOffset, f.c.id
                    + vOffset, f.b.id + nOffset, f.a.id + nOffset, f.c.id
                    + nOffset);
        }
    }

    /**
     * Saves the mesh as OBJ format to the given {@link OutputStream}. Currently
     * no texture coordinates are supported or written.
     * 
     * @param stream
     */
    public void saveAsOBJ(OutputStream stream) {
        OBJWriter obj = new OBJWriter();
        obj.beginSave(stream);
        saveAsOBJ(obj);
        obj.endSave();
    }

    /**
     * Saves the mesh as OBJ format to the given file path. Existing files will
     * be overwritten.
     * 
     * @param path
     */
    public void saveAsOBJ(String path) {
        OBJWriter obj = new OBJWriter();
        obj.beginSave(path);
        saveAsOBJ(obj);
        obj.endSave();
    }

    /**
     * Saves the mesh as binary STL format to the given {@link OutputStream}.
     * 
     * @param stream
     * @see #saveAsSTL(OutputStream, boolean)
     */
    public final void saveAsSTL(OutputStream stream) {
        saveAsSTL(stream, false);
    }

    /**
     * Saves the mesh as binary STL format to the given {@link OutputStream}.
     * The exported mesh can optionally have it's Y axis flipped by setting the
     * useFlippedY flag to true.
     * 
     * @param stream
     * @param useFlippedY
     */
    public final void saveAsSTL(OutputStream stream, boolean useFlippedY) {
        STLWriter stl = new STLWriter();
        stl.beginSave(stream, numFaces);
        handleSaveAsSTL(stl, useFlippedY);
    }

    /**
     * Saves the mesh as binary STL format to the given {@link OutputStream} and
     * using the supplied {@link STLWriter} instance. Use this method to export
     * data in a custom {@link STLColorModel}. The exported mesh can optionally
     * have it's Y axis flipped by setting the useFlippedY flag to true.
     * 
     * @param stream
     * @param stl
     * @param useFlippedY
     */
    public final void saveAsSTL(OutputStream stream, STLWriter stl,
            boolean useFlippedY) {
        stl.beginSave(stream, numFaces);
        handleSaveAsSTL(stl, useFlippedY);
    }

    /**
     * Saves the mesh as binary STL format to the given file path. Existing
     * files will be overwritten.
     * 
     * @param fileName
     */
    public final void saveAsSTL(String fileName) {
        saveAsSTL(fileName, false);
    }

    /**
     * Saves the mesh as binary STL format to the given file path. The exported
     * mesh can optionally have it's Y axis flipped by setting the useFlippedY
     * flag to true. Existing files will be overwritten.
     * 
     * @param fileName
     * @param useFlippedY
     */
    public final void saveAsSTL(String fileName, boolean useFlippedY) {
        saveAsSTL(fileName, new STLWriter(), useFlippedY);
    }

    public final void saveAsSTL(String fileName, STLWriter stl,
            boolean useFlippedY) {
        stl.beginSave(fileName, numFaces);
        handleSaveAsSTL(stl, useFlippedY);
    }

    public WETriangleMesh scale(float scale) {
        return transform(matrix.identity().scaleSelf(scale));
    }

    public WETriangleMesh scale(Vec3D scale) {
        return transform(matrix.identity().scaleSelf(scale));
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
        for (WEVertex v : vertices.values()) {
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
            // System.out.println("updating edge: " + e);
        } else {
            e = new WingedEdge(va, vb, f, edges.size());
            edges.put(e, e);
            va.addEdge(e);
            vb.addEdge(e);
            // System.out.println("new edge: " + e);
        }
        f.addEdge(e);
    }
}
