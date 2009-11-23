package toxi.geom.util;

import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import toxi.geom.AABB;
import toxi.geom.Sphere;
import toxi.geom.Vec3D;
import toxi.math.MathUtils;

/**
 * A class to dynamically build, manipulate & export triangle meshes. Meshes are
 * build face by face. The class automatically re-uses existing vertices and can
 * create smooth vertex normals. Vertices and faces are directly accessible for
 * speed & convenience.
 */
public class TriangleMesh {

    public final class Face {

        public final Vertex a, b, c;
        public final Vec3D normal;

        Face(Vertex a, Vertex b, Vertex c) {
            this.a = a;
            this.b = b;
            this.c = c;
            normal = a.sub(c).crossSelf(a.sub(b)).normalize();
            a.addFaceNormal(normal);
            b.addFaceNormal(normal);
            c.addFaceNormal(normal);
        }

        public final Vertex[] getVertices(Vertex[] verts) {
            if (verts != null) {
                verts[0] = a;
                verts[1] = b;
                verts[2] = c;
            } else {
                verts = new Vertex[] { a, b, c };
            }
            return verts;
        }

        public String toString() {
            return "TriangleMesh.Face: " + a + ", " + b + ", " + c;
        }
    }

    public final class Vertex extends Vec3D {

        public final Vec3D normal = new Vec3D();

        public final int id;
        private int valence = 0;

        Vertex(Vec3D v, int id) {
            super(v);
            this.id = id;
        }

        final void addFaceNormal(Vec3D n) {
            normal.addSelf(n);
            valence++;
        }

        final void computeNormal() {
            normal.scaleSelf(1f / valence).normalize();
        }

        final public int getValence() {
            return valence;
        }

        public String toString() {
            return id + ": p: " + super.toString() + " n:" + normal.toString();
        }
    }

    protected static final Logger logger =
            Logger.getLogger(TriangleMesh.class.getName());

    /**
     * Mesh name
     */
    public String name;

    /**
     * Vertex buffer & lookup index when adding new faces
     */
    public final LinkedHashMap<Vec3D, Vertex> vertices;

    /**
     * Face list
     */
    public final ArrayList<Face> faces;

    protected AABB bounds;
    protected Vec3D centroid = new Vec3D();
    protected int numVertices;
    protected int numFaces;

    /**
     * Creates a new mesh instance with an initial buffer size of 1000 vertices
     * & 3000 faces.
     * 
     * @param name
     *            mesh name
     */
    public TriangleMesh(String name) {
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
    public TriangleMesh(String name, int numV, int numF) {
        this.name = name;
        faces = new ArrayList<Face>(numF);
        vertices = new LinkedHashMap<Vec3D, Vertex>(numV, 1.75f, false);
    }

    /**
     * Adds the given 3 points as triangle face to the mesh. The assumed vertex
     * order is anti-clockwise.
     * 
     * @param a
     * @param b
     * @param c
     */
    public final void addFace(Vec3D a, Vec3D b, Vec3D c) {
        int aID, bID, cID;
        Vertex va = vertices.get(a);
        if (va != null) {
            aID = va.id;
        } else {
            aID = numVertices;
            va = new Vertex(a, aID);
            vertices.put(va, va);
            numVertices++;
        }
        Vertex vb = vertices.get(b);
        if (vb != null) {
            bID = vb.id;
        } else {
            bID = numVertices;
            vb = new Vertex(b, bID);
            vertices.put(vb, vb);
            numVertices++;
        }
        Vertex vc = vertices.get(c);
        if (vc != null) {
            cID = vc.id;
        } else {
            cID = numVertices;
            vc = new Vertex(c, cID);
            vertices.put(vc, vc);
            numVertices++;
        }
        if (aID == bID || aID == cID || bID == cID) {
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("ignorning invalid face: " + a + "," + b + "," + c);
            }
        } else {
            Face f = new Face(va, vb, vc);
            faces.add(f);
            numFaces++;
        }
    }

    /**
     * Centers the mesh around the given pivot point (the centroid of its AABB).
     * Method also updates & returns the new bounding box.
     * 
     * @param origin
     *            new centroid or null (defaults to {0,0,0})
     */
    public AABB center(Vec3D origin) {
        getCentroid();
        Vec3D delta =
                origin != null ? origin.sub(centroid) : centroid.getInverted();
        for (Vertex v : vertices.values()) {
            v.addSelf(delta);
        }
        getBoundingBox();
        return bounds;
    }

    /**
     * Clears all counters, and vertex & face buffers.
     */
    public void clear() {
        vertices.clear();
        faces.clear();
        bounds = null;
        numVertices = 0;
        numFaces = 0;
    }

    /**
     * Computes the smooth vertex normals for the entire mesh. <strong>This
     * method should only be called once in order to avoid disintegration of the
     * normal vectors.</strong>
     */
    public void computeVertexNormals() {
        for (Vertex v : vertices.values()) {
            v.computeNormal();
        }
    }

    /**
     * Creates a deep clone of the mesh. The new mesh name will have "-copy" as
     * suffix.
     * 
     * @return new mesh instance
     */
    public TriangleMesh copy() {
        TriangleMesh m =
                new TriangleMesh(name + "-copy", numVertices, numFaces);
        for (Face f : faces) {
            m.addFace(f.a, f.b, f.c);
        }
        return m;
    }

    /**
     * Computes & returns the axis-aligned bounding box of the mesh.
     * 
     * @return bounding box
     */
    public AABB getBoundingBox() {
        final Vec3D minBounds = Vec3D.MAX_VALUE.copy();
        final Vec3D maxBounds = Vec3D.MIN_VALUE.copy();
        for (Vertex v : vertices.values()) {
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
        for (Vertex v : vertices.values()) {
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
        return centroid.scaleSelf(1f / numVertices);
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
        for (Face f : faces) {
            faceList[i++] = f.a.id;
            faceList[i++] = f.b.id;
            faceList[i++] = f.c.id;
        }
        return faceList;
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
     * format suitable for OpenGL Vertex Buffer Objects (by choosing stride=4).
     * The order of the array will be as follows:
     * 
     * <ul>
     * <li>Face 1:
     * <ul>
     * <li>Vertex #1
     * <ul>
     * <li>x</li>
     * <li>y</li>
     * <li>z</li>
     * <li>[optional empty indices to match stride setting]</li>
     * </ul>
     * </li>
     * <li>Vertex #2
     * <ul>
     * <li>x</li>
     * <li>y</li>
     * <li>z</li>
     * <li>[optional empty indices to match stride setting]</li>
     * </ul>
     * </li>
     * <li>Vertex #3
     * <ul>
     * <li>x</li>
     * <li>y</li>
     * <li>z</li>
     * <li>[optional empty indices to match stride setting]</li>
     * </ul>
     * </li>
     * </ul>
     * <li>Face 2:
     * <ul>
     * <li>Vertex #1</li>
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
        for (Face f : faces) {
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

    public float[] getUniqueVerticesAsArray() {
        float[] verts = new float[vertices.size() * 3];
        int i = 0;
        for (Vertex v : vertices.values()) {
            verts[i++] = v.x;
            verts[i++] = v.y;
            verts[i++] = v.z;
        }
        return verts;
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
     * into a format suitable for OpenGL Vertex Buffer Objects (by choosing
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
     *            stride/alignment setting for individual coordinates
     * @return array of xyz normal coords
     */
    public float[] getVertexNormalsAsArray(float[] normals, int offset,
            int stride) {
        stride = MathUtils.max(stride, 3);
        if (normals == null) {
            normals = new float[faces.size() * 3 * stride];
        }
        int i = offset;
        for (Face f : faces) {
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
        for (Vertex v : vertices.values()) {
            obj.vertex(v);
        }
        // normals
        for (Vertex v : vertices.values()) {
            obj.normal(v.normal.getInverted());
        }
        // faces
        for (Face f : faces) {
            obj.faceWithNormals(f.a.id + vOffset, f.b.id + vOffset, f.c.id
                    + vOffset, f.a.id + nOffset, f.b.id + nOffset, f.c.id
                    + nOffset);
        }
    }

    /**
     * Saves the mesh as OBJ format to the given file path. Existing files will
     * be overwritten.
     * 
     * @param path
     */
    public void saveAsOBJ(String path) {
        logger.info("saving mesh to: " + path);
        OBJWriter obj = new OBJWriter();
        obj.beginSave(path);
        saveAsOBJ(obj);
        obj.endSave();
    }

    /**
     * Saves the mesh in a simple, proprietary compact binary format written
     * using the standard {@link DataOutputStream} methods. The format is as
     * follows:
     * <ul>
     * <li>int: number of vertex coordinates</li>
     * <li>float: count times vertice coordinates in x,y,z order</li>
     * <li>float: count times vertex normal coordinates in x,y,z order</li>
     * </ul>
     * 
     * @param fileName
     */
    public final void saveAsRaw(String fileName) {
        try {
            DataOutputStream ds =
                    new DataOutputStream(new FileOutputStream(fileName));
            float[] coords = getMeshAsVertexArray();
            ds.writeInt(coords.length);
            for (float f : coords) {
                ds.writeFloat(f);
            }
            coords = getVertexNormalsAsArray();
            for (float f : coords) {
                ds.writeFloat(f);
            }
            ds.flush();
            ds.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves the mesh as binary STL format to the given file path. Existing
     * files will be overwritten.
     * 
     * @param fileName
     */
    public final void saveAsSTL(String fileName) {
        logger.info("saving mesh to: " + fileName);
        STLWriter stl = new STLWriter();
        stl.beginSave(fileName, numFaces);
        for (Face f : faces) {
            stl.face(f.a, f.b, f.c);
        }
        stl.endSave();
        logger.info(numFaces + " written");
    }

    @Override
    public String toString() {
        return "TriangleMesh: " + name + " vertices: " + getNumVertices()
                + " faces: " + getNumFaces();
    }
}
