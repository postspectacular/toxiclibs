/*
 *   __               .__       .__  ._____.           
 * _/  |_  _______  __|__| ____ |  | |__\_ |__   ______
 * \   __\/  _ \  \/  /  |/ ___\|  | |  || __ \ /  ___/
 *  |  | (  <_> >    <|  \  \___|  |_|  || \_\ \\___ \ 
 *  |__|  \____/__/\_ \__|\___  >____/__||___  /____  >
 *                   \/       \/             \/     \/ 
 *
 * Copyright (c) 2006-2011 Karsten Schmidt
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * http://creativecommons.org/licenses/LGPL/2.1/
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301, USA
 */

package toxi.geom.mesh;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import toxi.geom.AABBD;
import toxi.geom.IntersectorD3D;
import toxi.geom.IsectDataD3D;
import toxi.geom.Matrix4x4;
import toxi.geom.QuaternionD;
import toxi.geom.Ray3D;
import toxi.geom.RayD3D;
import toxi.geom.ReadonlyVecD3D;
import toxi.geom.Sphere;
import toxi.geom.SphereD;
import toxi.geom.TriangleD3D;
import toxi.geom.TriangleDIntersector;
import toxi.geom.mesh.VertexD;
import toxi.geom.mesh.FaceD;
import toxi.geom.VecD2D;
import toxi.geom.VecD3D;
import toxi.math.MathUtils;

/**
 * An extensible class to dynamically build, manipulate & export triangle
 * meshes. Meshes are built face by face. This implementation automatically
 * re-uses existing vertices and can generate smooth vertex normals. Vertice and
 * face lists are directly accessible for speed & convenience.
 */
public class TriangleMeshD implements MeshD3D, IntersectorD3D {

    /**
     * Default size for vertex list
     */
    public static final int DEFAULT_NUM_VERTICES = 1000;

    /**
     * Default size for face list
     */
    public static final int DEFAULT_NUM_FACES = 3000;

    /**
     * Default stride setting used for serializing mesh properties into arrays.
     */
    public static final int DEFAULT_STRIDE = 4;

    protected static final Logger logger = Logger.getLogger(TriangleMeshD.class
            .getName());

    /**
     * Mesh name
     */
    public String name;

    /**
     * VertexD buffer & lookup index when adding new faces
     */
    public LinkedHashMap<VecD3D, VertexD> vertices;

    /**
     * FaceD list
     */
    public ArrayList<FaceD> faces;

    protected AABBD bounds;
    protected VecD3D centroid = new VecD3D();
    protected int numVertices;
    protected int numFaceDs;

    protected Matrix4x4 matrix = new Matrix4x4();
    protected TriangleDIntersector intersector = new TriangleDIntersector();

    protected int uniqueVertexID;

    public TriangleMeshD() {
        this("untitled");
    }

    /**
     * Creates a new mesh instance with initial default buffer sizes.
     * 
     * @param name
     *            mesh name
     */
    public TriangleMeshD(String name) {
        this(name, DEFAULT_NUM_VERTICES, DEFAULT_NUM_FACES);
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
    public TriangleMeshD(String name, int numV, int numF) {
        init(name, numV, numF);
    }

    public TriangleMeshD addFaceD(VecD3D a, VecD3D b, VecD3D c) {
        return addFaceD(a, b, c, null, null, null, null);
    }

    public TriangleMeshD addFaceD(VecD3D a, VecD3D b, VecD3D c, VecD2D uvA, VecD2D uvB, VecD2D uvC) {
        return addFaceD(a, b, c, null, uvA, uvB, uvC);
    }

    public TriangleMeshD addFaceD(VecD3D a, VecD3D b, VecD3D c, VecD3D n) {
        return addFaceD(a, b, c, n, null, null, null);
    }

    public TriangleMeshD addFaceD(VecD3D a, VecD3D b, VecD3D c, VecD3D n, VecD2D uvA, VecD2D uvB, VecD2D uvC) {
        VertexD va = checkVertexD(a);
        VertexD vb = checkVertexD(b);
        VertexD vc = checkVertexD(c);
        if (va.id == vb.id || va.id == vc.id || vb.id == vc.id) {
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("ignorning invalid face: " + a + "," + b + "," + c);
            }
        } else {
            if (n != null) {
                VecD3D nc = va.sub(vc).crossSelf(va.sub(vb));
                if (n.dot(nc) < 0) {
                    VertexD t = va;
                    va = vb;
                    vb = t;
                }
            }
            FaceD f = new FaceD(va, vb, vc, uvA, uvB, uvC);
            faces.add(f);
            numFaceDs++;
        }
        return this;
    }

    /**
     * Adds all faces from the given mesh to this one.
     * 
     * @param m
     *            source mesh instance
     */
    public TriangleMeshD addMeshD(MeshD3D m) {
        for (FaceD f : m.getFaceDs()) {
            addFaceD(f.a, f.b, f.c, f.uvA, f.uvB, f.uvC);
        }
        return this;
    }

    public AABBD center(ReadonlyVecD3D origin) {
        computeCentroid();
        VecD3D delta = origin != null ? origin.sub(centroid) : centroid
                .getInverted();
        for (VertexD v : vertices.values()) {
            v.addSelf(delta);
        }
        getBoundingBox();
        return bounds;
    }

    private final VertexD checkVertexD(VecD3D v) {
        VertexD vertex = vertices.get(v);
        if (vertex == null) {
            vertex = createVertexD(v, uniqueVertexID++);
            vertices.put(vertex, vertex);
            numVertices++;
        }
        return vertex;
    }

    /**
     * Clears all counters, and vertex & face buffers.
     */
    public TriangleMeshD clear() {
        vertices.clear();
        faces.clear();
        bounds = null;
        numVertices = 0;
        numFaceDs = 0;
        uniqueVertexID = 0;
        return this;
    }

    public VecD3D computeCentroid() {
        centroid.clear();
        for (VecD3D v : vertices.values()) {
            centroid.addSelf(v);
        }
        return centroid.scaleSelf(1f / numVertices).copy();
    }

    /**
     * Re-calculates all face normals.
     */
    public TriangleMeshD computeFaceDNormals() {
        for (FaceD f : faces) {
            f.computeNormal();
        }
        return this;
    }

    /**
     * Computes the smooth vertex normals for the entire mesh.
     */
    public TriangleMeshD computeVertexDNormals() {
        for (VertexD v : vertices.values()) {
            v.clearNormal();
        }
        for (FaceD f : faces) {
            f.a.addFaceDNormal(f.normal);
            f.b.addFaceDNormal(f.normal);
            f.c.addFaceDNormal(f.normal);
        }
        for (VertexD v : vertices.values()) {
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
    public TriangleMeshD copy() {
        TriangleMeshD m = new TriangleMeshD(name + "-copy", numVertices, numFaceDs);
        for (FaceD f : faces) {
            m.addFaceD(f.a, f.b, f.c, f.normal, f.uvA, f.uvB, f.uvC);
        }
        return m;
    }

    protected VertexD createVertexD(VecD3D v, int id) {
        return new VertexD(v, id);
    }

    public TriangleMeshD faceOutwards() {
        computeCentroid();
        for (FaceD f : faces) {
            VecD3D n = f.getCentroid().sub(centroid);
            double dot = n.dot(f.normal);
            if (dot < 0) {
                f.flipVertexOrder();
            }
        }
        return this;
    }

    public TriangleMeshD flipVertexDOrder() {
        for (FaceD f : faces) {
            VertexD t = f.a;
            f.a = f.b;
            f.b = t;
            VecD2D tuv = f.uvA;
            f.uvA = f.uvB;
            f.uvB = tuv;
            f.normal.invert();
        }
        return this;
    }

    public TriangleMeshD flipYAxis() {
        transform(new Matrix4x4().scaleSelf(1, -1, 1));
        flipVertexDOrder();
        return this;
    }

    public AABBD getBoundingBox() {
        final VecD3D minBounds = VecD3D.MAX_VALUE.copy();
        final VecD3D maxBounds = VecD3D.NEG_MAX_VALUE.copy();
        for (VertexD v : vertices.values()) {
            minBounds.minSelf(v);
            maxBounds.maxSelf(v);
        }
        bounds = AABBD.fromMinMax(minBounds, maxBounds);
        return bounds;
    }

    public SphereD getBoundingSphereD() {
        double radius = 0;
        computeCentroid();
        for (VertexD v : vertices.values()) {
            radius = MathUtils.max(radius, v.distanceToSquared(centroid));
        }
        return new SphereD(centroid, Math.sqrt(radius));
    }

    public VertexD getClosestVertexDToPoint(ReadonlyVecD3D p) {
        VertexD closest = null;
        double minDist = Double.MAX_VALUE;
        for (VertexD v : vertices.values()) {
            double d = v.distanceToSquared(p);
            if (d < minDist) {
                closest = v;
                minDist = d;
            }
        }
        return closest;
    }

    /**
     * Creates an array of unravelled normal coordinates. For each vertex the
     * normal vector of its parent face is used. This is a convienence
     * invocation of {@link #getFaceDNormalsAsArray(double[], int, int)} with a
     * default stride = 4.
     * 
     * @return array of xyz normal coords
     */
    public double[] getFaceDNormalsAsArray() {
        return getFaceDNormalsAsArray(null, 0, DEFAULT_STRIDE);
    }

    /**
     * Creates an array of unravelled normal coordinates. For each vertex the
     * normal vector of its parent face is used. This method can be used to
     * translate the internal mesh data structure into a format suitable for
     * OpenGL VertexD Buffer Objects (by choosing stride=4). For more detail,
     * please see {@link #getMeshAsVertexDArray(double[], int, int)}
     * 
     * @see #getMeshAsVertexDArray(double[], int, int)
     * 
     * @param normals
     *            existing double array or null to automatically create one
     * @param offset
     *            start index in array to place normals
     * @param stride
     *            stride/alignment setting for individual coordinates (min value
     *            = 3)
     * @return array of xyz normal coords
     */
    public double[] getFaceDNormalsAsArray(double[] normals, int offset, int stride) {
        stride = MathUtils.max(stride, 3);
        if (normals == null) {
            normals = new double[faces.size() * 3 * stride];
        }
        int i = offset;
        for (FaceD f : faces) {
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

    public List<FaceD> getFaceDs() {
        return faces;
    }

    /**
     * Builds an array of vertex indices of all faces. Each vertex ID
     * corresponds to its position in the {@link #vertices} HashMap. The
     * resulting array will be 3 times the face count.
     * 
     * @return array of vertex indices
     */
    public int[] getFaceDsAsArray() {
        int[] faceList = new int[faces.size() * 3];
        int i = 0;
        for (FaceD f : faces) {
            faceList[i++] = f.a.id;
            faceList[i++] = f.b.id;
            faceList[i++] = f.c.id;
        }
        return faceList;
    }

    public IsectDataD3D getIntersectionDataD() {
        return intersector.getIntersectionDataD();
    }

    /**
     * Creates an array of unravelled vertex coordinates for all faces using a
     * stride setting of 4, resulting in a serialized version of all mesh vertex
     * coordinates suitable for VBOs.
     * 
     * @see #getMeshAsVertexDArray(double[], int, int)
     * @return double array of vertex coordinates
     */
    public double[] getMeshAsVertexDArray() {
        return getMeshAsVertexDArray(null, 0, DEFAULT_STRIDE);
    }

    /**
     * Creates an array of unravelled vertex coordinates for all faces. This
     * method can be used to translate the internal mesh data structure into a
     * format suitable for OpenGL VertexD Buffer Objects (by choosing stride=4).
     * The order of the array will be as follows:
     * 
     * <ul>
     * <li>Face 1:
     * <ul>
     * <li>VertexD #1
     * <ul>
     * <li>x</li>
     * <li>y</li>
     * <li>z</li>
     * <li>[optional empty indices to match stride setting]</li>
     * </ul>
     * </li>
     * <li>VertexD #2
     * <ul>
     * <li>x</li>
     * <li>y</li>
     * <li>z</li>
     * <li>[optional empty indices to match stride setting]</li>
     * </ul>
     * </li>
     * <li>VertexD #3
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
     * <li>VertexD #1</li>
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
    public double[] getMeshAsVertexDArray(double[] verts, int offset, int stride) {
        stride = MathUtils.max(stride, 3);
        if (verts == null) {
            verts = new double[faces.size() * 3 * stride];
        }
        int i = offset;
        for (FaceD f : faces) {
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

    public double[] getNormalsForUniqueVerticesAsArray() {
        double[] normals = new double[numVertices * 3];
        int i = 0;
        for (VertexD v : vertices.values()) {
            normals[i++] = v.normal.x;
            normals[i++] = v.normal.y;
            normals[i++] = v.normal.z;
        }
        return normals;
    }

    public int getNumFaceDs() {
        return numFaceDs;
    }

    public int getNumVertices() {
        return numVertices;
    }

    public TriangleMeshD getRotatedAroundAxis(VecD3D axis, double theta) {
        return copy().rotateAroundAxisD(axis, theta);
    }

    public TriangleMeshD getRotatedX(double theta) {
        return copy().rotateX(theta);
    }

    public TriangleMeshD getRotatedY(double theta) {
        return copy().rotateY(theta);
    }

    public TriangleMeshD getRotatedZ(double theta) {
        return copy().rotateZ(theta);
    }

    public TriangleMeshD getScaled(double scale) {
        return copy().scale(scale);
    }

    public TriangleMeshD getScaled(VecD3D scale) {
        return copy().scale(scale);
    }

    public TriangleMeshD getTranslated(VecD3D trans) {
        return copy().translate(trans);
    }

    public double[] getUniqueVerticesAsArray() {
        double[] verts = new double[numVertices * 3];
        int i = 0;
        for (VertexD v : vertices.values()) {
            verts[i++] = v.x;
            verts[i++] = v.y;
            verts[i++] = v.z;
        }
        return verts;
    }

    public VertexD getVertexDAtPoint(VecD3D v) {
        return vertices.get(v);
    }

    public VertexD getVertexDForID(int id) {
        VertexD vertex = null;
        for (VertexD v : vertices.values()) {
            if (v.id == id) {
                vertex = v;
                break;
            }
        }
        return vertex;
    }

    /**
     * Creates an array of unravelled vertex normal coordinates for all faces.
     * Uses default stride = 4.
     * 
     * @see #getVertexDNormalsAsArray(double[], int, int)
     * @return array of xyz normal coords
     */
    public double[] getVertexDNormalsAsArray() {
        return getVertexDNormalsAsArray(null, 0, DEFAULT_STRIDE);
    }

    /**
     * Creates an array of unravelled vertex normal coordinates for all faces.
     * This method can be used to translate the internal mesh data structure
     * into a format suitable for OpenGL VertexD Buffer Objects (by choosing
     * stride=4). For more detail, please see
     * {@link #getMeshAsVertexDArray(double[], int, int)}
     * 
     * @see #getMeshAsVertexDArray(double[], int, int)
     * 
     * @param normals
     *            existing double array or null to automatically create one
     * @param offset
     *            start index in array to place normals
     * @param stride
     *            stride/alignment setting for individual coordinates (min value
     *            = 3)
     * @return array of xyz normal coords
     */
    public double[] getVertexDNormalsAsArray(double[] normals, int offset,
            int stride) {
        stride = MathUtils.max(stride, 3);
        if (normals == null) {
            normals = new double[faces.size() * 3 * stride];
        }
        int i = offset;
        for (FaceD f : faces) {
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

    public Collection<VertexD> getVertices() {
        return vertices.values();
    }

    protected void handleSaveAsSTL(STLWriter stl, boolean useFlippedY) {
        if (useFlippedY) {
            stl.setScaleD(new VecD3D(1, -1, 1));
            for (FaceD f : faces) {
                stl.faceD(f.a, f.b, f.c, f.normal, STLWriter.DEFAULT_RGB);
            }
        } else {
            for (FaceD f : faces) {
                stl.faceD(f.b, f.a, f.c, f.normal, STLWriter.DEFAULT_RGB);
            }
        }
        stl.endSave();
        logger.info(numFaceDs + " faces written");
    }

    public TriangleMeshD init(String name, int numV, int numF) {
        setName(name);
        vertices = new LinkedHashMap<VecD3D, VertexD>(numV, 1.5f, false);
        faces = new ArrayList<FaceD>(numF);
        return this;
    }

    public boolean intersectsRayD(RayD3D ray) {
        TriangleD3D tri = intersector.getTriangleD();
        for (FaceD f : faces) {
            tri.set(f.a, f.b, f.c);
            if (intersector.intersectsRayD(ray)) {
                return true;
            }
        }
        return false;
    }

    public TriangleD3D perforateFaceD(FaceD f, double size) {
        VecD3D centroid = f.getCentroid();
        double d = 1 - size;
        VecD3D a2 = f.a.interpolateTo(centroid, d);
        VecD3D b2 = f.b.interpolateTo(centroid, d);
        VecD3D c2 = f.c.interpolateTo(centroid, d);
        removeFaceD(f);
        addFaceD(f.a, b2, a2);
        addFaceD(f.a, f.b, b2);
        addFaceD(f.b, c2, b2);
        addFaceD(f.b, f.c, c2);
        addFaceD(f.c, a2, c2);
        addFaceD(f.c, f.a, a2);
        return new TriangleD3D(a2, b2, c2);
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
    public TriangleMeshD pointTowards(ReadonlyVecD3D dir) {
        return transform(QuaternionD.getAlignmentQuat(dir, VecD3D.Z_AXIS)
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
    public TriangleMeshD pointTowards(ReadonlyVecD3D dir, ReadonlyVecD3D forward) {
        return transform(
                QuaternionD.getAlignmentQuat(dir, forward).toMatrix4x4(matrix),
                true);
    }

    public void removeFaceD(FaceD f) {
        faces.remove(f);
    }

    public TriangleMeshD rotateAroundAxisD(VecD3D axis, double theta) {
        return transform(matrix.identity().rotateAroundAxis(axis, theta));
    }

    public TriangleMeshD rotateX(double theta) {
        return transform(matrix.identity().rotateX(theta));
    }

    public TriangleMeshD rotateY(double theta) {
        return transform(matrix.identity().rotateY(theta));
    }

    public TriangleMeshD rotateZ(double theta) {
        return transform(matrix.identity().rotateZ(theta));
    }

    /**
     * Saves the mesh as OBJ format by appending it to the given mesh
     * {@link OBJWriter} instance.
     * 
     * @param obj
     */
    public void saveAsOBJ(OBJWriter obj) {
        saveAsOBJ(obj, true);
    }

    public void saveAsOBJ(OBJWriter obj, boolean saveNormals) {
        int vOffset = obj.getCurrVertexOffset() + 1;
        int nOffset = obj.getCurrNormalOffset() + 1;
        logger.info("writing OBJMesh: " + this.toString());
        obj.newObject(name);
        // vertices
        for (VertexD v : vertices.values()) {
            obj.vertex(v);
        }
        // faces
        if (saveNormals) {
            // normals
            for (VertexD v : vertices.values()) {
                obj.normal(v.normal);
            }
            for (FaceD f : faces) {
                obj.faceWithNormals(f.b.id + vOffset, f.a.id + vOffset, f.c.id
                        + vOffset, f.b.id + nOffset, f.a.id + nOffset, f.c.id
                        + nOffset);
            }
        } else {
            for (FaceD f : faces) {
                obj.face(f.b.id + vOffset, f.a.id + vOffset, f.c.id + vOffset);
            }
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
        saveAsOBJ(path, true);
    }

    public void saveAsOBJ(String path, boolean saveNormals) {
        OBJWriter obj = new OBJWriter();
        obj.beginSave(path);
        saveAsOBJ(obj, saveNormals);
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
        stl.beginSave(stream, numFaceDs);
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
        stl.beginSave(stream, numFaceDs);
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
        stl.beginSave(fileName, numFaceDs);
        handleSaveAsSTL(stl, useFlippedY);
    }

    public TriangleMeshD scale(double scale) {
        return transform(matrix.identity().scaleSelf(scale));
    }

    public TriangleMeshD scale(double x, double y, double z) {
        return transform(matrix.identity().scaleSelf(x, y, z));
    }

    public TriangleMeshD scale(VecD3D scale) {
        return transform(matrix.identity().scaleSelf(scale));
    }

    public TriangleMeshD setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public String toString() {
        return "TriangleMesh: " + name + " vertices: " + getNumVertices()
                + " faces: " + getNumFaceDs();
    }

    public WETriangleMeshD toWEMeshD() {
        return new WETriangleMeshD(name, vertices.size(), faces.size())
                .addMesh(this);
    }

    /**
     * Applies the given matrix transform to all mesh vertices and updates all
     * face normals.
     * 
     * @param mat
     * @return itself
     */
    public TriangleMeshD transform(Matrix4x4 mat) {
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
    public TriangleMeshD transform(Matrix4x4 mat, boolean updateNormals) {
        for (VertexD v : vertices.values()) {
            v.set(mat.applyTo(v));
        }
        if (updateNormals) {
            computeFaceDNormals();
        }
        return this;
    }

    public TriangleMeshD translate(double x, double y, double z) {
        return transform(matrix.identity().translateSelf(x, y, z));
    }

    public TriangleMeshD translate(VecD3D trans) {
        return transform(matrix.identity().translateSelf(trans));
    }

    public TriangleMeshD updateVertexD(VecD3D orig, VecD3D newPos) {
        VertexD v = vertices.get(orig);
        if (v != null) {
            vertices.remove(v);
            v.set(newPos);
            vertices.put(v, v);
        }
        return this;
    }

}
