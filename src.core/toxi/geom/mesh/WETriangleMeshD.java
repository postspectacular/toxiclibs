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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Level;

import toxi.geom.AABBD;
import toxi.geom.LineD3D;
import toxi.geom.Matrix4x4;
import toxi.geom.QuaternionD;
import toxi.geom.ReadonlyVecD3D;
import toxi.geom.VecD2D;
import toxi.geom.VecD3D;
import toxi.geom.mesh.WEVertexD;
import toxi.geom.mesh.WingedEdgeD;

import toxi.geom.mesh.subdiv.MidpointSubdivisionD;
import toxi.geom.mesh.subdiv.SubdivisionStrategyD;

/**
 * A class to dynamically build, manipulate & export triangle meshes. Meshes are
 * build face by face. The class automatically re-uses existing vertices and can
 * create smooth vertex normals. Vertices and faces are directly accessible for
 * speed & convenience.
 */
public class WETriangleMeshD extends TriangleMeshD {

    /**
     * WEVertexD buffer & lookup index when adding new faces
     */
    public LinkedHashMap<LineD3D, WingedEdgeD> edges;

    private final LineD3D edgeCheck = new LineD3D(new VecD3D(), new VecD3D());

    private int uniqueEdgeID;

    public WETriangleMeshD() {
        this("untitled");
    }

    /**
     * Creates a new mesh instance with initial default buffer sizes.
     * 
     * @param name
     *            mesh name
     */
    public WETriangleMeshD(String name) {
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
    public WETriangleMeshD(String name, int numV, int numF) {
        super(name, numV, numF);
    }

    public WETriangleMeshD addFaceD(VecD3D a, VecD3D b, VecD3D c) {
        return addFaceD(a, b, c, null, null, null, null);
    }

    public WETriangleMeshD addFaceD(VecD3D a, VecD3D b, VecD3D c, VecD2D uvA,
            VecD2D uvB, VecD2D uvC) {
        return addFaceD(a, b, c, null, uvA, uvB, uvC);
    }

    public WETriangleMeshD addFaceD(VecD3D a, VecD3D b, VecD3D c, VecD3D n) {
        return addFaceD(a, b, c, n, null, null, null);
    }

    public WETriangleMeshD addFaceD(VecD3D a, VecD3D b, VecD3D c, VecD3D n,
            VecD2D uvA, VecD2D uvB, VecD2D uvC) {
        WEVertexD va = checkVertexD(a);
        WEVertexD vb = checkVertexD(b);
        WEVertexD vc = checkVertexD(c);
        if (va.id == vb.id || va.id == vc.id || vb.id == vc.id) {
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("ignorning invalid face: " + a + "," + b + "," + c);
            }
        } else {
            if (n != null) {
                VecD3D nc = va.sub(vc).crossSelf(va.sub(vb));
                if (n.dot(nc) < 0) {
                    WEVertexD t = va;
                    va = vb;
                    vb = t;
                }
            }
            WEFaceD f = new WEFaceD(va, vb, vc, uvA, uvB, uvC);
            faces.add(f);
            numFaceDs++;
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
    public WETriangleMeshD addMesh(MeshD3D m) {
        super.addMeshD(m);
        return this;
    }

    @Override
    public AABBD center(ReadonlyVecD3D origin) {
        super.center(origin);
        rebuildIndex();
        return bounds;
    }

    private final WEVertexD checkVertexD(VecD3D v) {
        WEVertexD vertex = (WEVertexD) vertices.get(v);
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
    public WETriangleMeshD clear() {
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
    public WETriangleMeshD copy() {
        WETriangleMeshD m = new WETriangleMeshD(name + "-copy", numVertices,
                numFaceDs);
        for (FaceD f : faces) {
            m.addFaceD(f.a, f.b, f.c, f.normal, f.uvA, f.uvB, f.uvC);
        }
        return m;
    }

    protected WEVertexD createVertexD(VecD3D v, int id) {
        return new WEVertexD(v, id);
    }

    /**
     * Flips the vertex ordering between clockwise and anti-clockwise. WEFaceD
     * normals are updated automatically too.
     * 
     * @return itself
     */
    public WETriangleMeshD flipVertexDOrder() {
        super.flipVertexDOrder();
        return this;
    }

    public WETriangleMeshD flipYAxis() {
        super.flipYAxis();
        return this;
    }

    public WEVertexD getClosestVertexDToPoint(ReadonlyVecD3D p) {
        return (WEVertexD) super.getClosestVertexDToPoint(p);
    }

    public Collection<WingedEdgeD> getEdges() {
        return edges.values();
    }

    public int getNumEdges() {
        return edges.size();
    }

    public WETriangleMeshD getRotatedAroundAxis(VecD3D axis, double theta) {
        return copy().rotateAroundAxis(axis, theta);
    }

    public WETriangleMeshD getRotatedX(double theta) {
        return copy().rotateX(theta);
    }

    public WETriangleMeshD getRotatedY(double theta) {
        return copy().rotateY(theta);
    }

    public WETriangleMeshD getRotatedZ(double theta) {
        return copy().rotateZ(theta);
    }

    public WETriangleMeshD getScaled(double scale) {
        return copy().scale(scale);
    }

    public WETriangleMeshD getScaled(VecD3D scale) {
        return copy().scale(scale);
    }

    public WETriangleMeshD getTranslated(VecD3D trans) {
        return copy().translate(trans);
    }

    public WEVertexD getVertexDAtPoint(VecD3D v) {
        return (WEVertexD) vertices.get(v);
    }

    public WEVertexD getVertexDForID(int id) {
        return (WEVertexD) super.getVertexDForID(id);
    }

    public WETriangleMeshD init(String name, int numV, int numF) {
        super.init(name, numV, numF);
        edges = new LinkedHashMap<LineD3D, WingedEdgeD>(numV, 1.5f, false);
        return this;
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
    public WETriangleMeshD pointTowards(ReadonlyVecD3D dir) {
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
    public WETriangleMeshD pointTowards(ReadonlyVecD3D dir, ReadonlyVecD3D forward) {
        return transform(
                QuaternionD.getAlignmentQuat(dir, forward).toMatrix4x4(matrix),
                true);
    }

    public void rebuildIndex() {
        LinkedHashMap<VecD3D, VertexD> newV = new LinkedHashMap<VecD3D, VertexD>(
                vertices.size());
        for (VertexD v : vertices.values()) {
            newV.put(v, v);
        }
        vertices = newV;
        LinkedHashMap<LineD3D, WingedEdgeD> newE = new LinkedHashMap<LineD3D, WingedEdgeD>(
                edges.size());
        for (WingedEdgeD e : edges.values()) {
            newE.put(e, e);
        }
        edges = newE;
    }

    protected void removeEdge(WingedEdgeD e) {
        e.remove();
        WEVertexD v = (WEVertexD) e.a;
        if (v.edges.size() == 0) {
            vertices.remove(v);
        }
        v = (WEVertexD) e.b;
        if (v.edges.size() == 0) {
            vertices.remove(v);
        }
        for (WEFaceD f : e.faces) {
            removeFaceD(f);
        }
        WingedEdgeD removed = edges.remove(edgeCheck.set(e.a, e.b));
        if (removed != e) {
            throw new IllegalStateException("can't remove edge");
        }
    }

    @Override
    public void removeFaceD(FaceD f) {
        faces.remove(f);
        for (WingedEdgeD e : ((WEFaceD) f).edges) {
            e.faces.remove(f);
            if (e.faces.size() == 0) {
                removeEdge(e);
            }
        }
    }

    // FIXME
    public void removeUnusedVertices() {
        for (Iterator<VertexD> i = vertices.values().iterator(); i.hasNext();) {
            VertexD v = i.next();
            boolean isUsed = false;
            for (FaceD f : faces) {
                if (f.a == v || f.b == v || f.c == v) {
                    isUsed = true;
                    break;
                }
            }
            if (!isUsed) {
                logger.info("removing vertex: " + v);
                i.remove();
            }
        }
    }

    public void removeVertices(Collection<VertexD> selection) {
        for (VertexD v : selection) {
            WEVertexD wv = (WEVertexD) v;
            for (WingedEdgeD e : new ArrayList<WingedEdgeD>(wv.edges)) {
                for (FaceD f : new ArrayList<FaceD>(e.faces)) {
                    removeFaceD(f);
                }
            }
        }
        // rebuildIndex();
    }

    public WETriangleMeshD rotateAroundAxis(VecD3D axis, double theta) {
        return transform(matrix.identity().rotateAroundAxis(axis, theta));
    }

    public WETriangleMeshD rotateX(double theta) {
        return transform(matrix.identity().rotateX(theta));
    }

    public WETriangleMeshD rotateY(double theta) {
        return transform(matrix.identity().rotateY(theta));
    }

    public WETriangleMeshD rotateZ(double theta) {
        return transform(matrix.identity().rotateZ(theta));
    }

    public WETriangleMeshD scale(double scale) {
        return transform(matrix.identity().scaleSelf(scale));
    }

    public WETriangleMeshD scale(VecD3D scale) {
        return transform(matrix.identity().scaleSelf(scale));
    }

    public void splitEdge(ReadonlyVecD3D a, ReadonlyVecD3D b,
            SubdivisionStrategyD subDiv) {
        WingedEdgeD e = edges.get(edgeCheck.set(a, b));
        if (e != null) {
            splitEdge(e, subDiv);
        }
    }

    public void splitEdge(WingedEdgeD e, SubdivisionStrategyD subDiv) {
        List<VecD3D> mid = subDiv.computeSplitPointsD(e);
        splitFaceD(e.faces.get(0), e, mid);
        if (e.faces.size() > 1) {
            splitFaceD(e.faces.get(1), e, mid);
        }
        removeEdge(e);
    }

    protected void splitFaceD(WEFaceD f, WingedEdgeD e, List<VecD3D> midPoints) {
        VecD3D p = null;
        for (int i = 0; i < 3; i++) {
            WingedEdgeD ec = f.edges.get(i);
            if (!ec.equals(e)) {
                if (ec.a.equals(e.a) || ec.a.equals(e.b)) {
                    p = ec.b;
                } else {
                    p = ec.a;
                }
                break;
            }
        }
        VecD3D prev = null;
        for (int i = 0, num = midPoints.size(); i < num; i++) {
            VecD3D mid = midPoints.get(i);
            if (i == 0) {
                addFaceD(p, e.a, mid, f.normal);
            } else {
                addFaceD(p, prev, mid, f.normal);
            }
            if (i == num - 1) {
                addFaceD(p, mid, e.b, f.normal);
            }
            prev = mid;
        }
    }

    public void subdivide() {
        subdivide(0);
    }

    public void subdivide(double minLength) {
        subdivide(new MidpointSubdivisionD(), minLength);
    }

    public void subdivide(SubdivisionStrategyD subDiv) {
        subdivide(subDiv, 0);
    }

    public void subdivide(SubdivisionStrategyD subDiv, double minLength) {
        subdivideEdges(new ArrayList<WingedEdgeD>(edges.values()), subDiv,
                minLength);
    }

    protected void subdivideEdges(List<WingedEdgeD> origEdges,
            SubdivisionStrategyD subDiv, double minLength) {
        Collections.sort(origEdges, subDiv.getEdgeOrdering());
        minLength *= minLength;
        for (WingedEdgeD e : origEdges) {
            if (edges.containsKey(e)) {
                if (e.getLengthSquared() >= minLength) {
                    splitEdge(e, subDiv);
                }
            }
        }
    }

    public void subdivideFaceDEdges(List<WEFaceD> faces,
            SubdivisionStrategyD subDiv, double minLength) {
        List<WingedEdgeD> fedges = new ArrayList<WingedEdgeD>();
        for (WEFaceD f : faces) {
            for (WingedEdgeD e : f.edges) {
                if (!fedges.contains(e)) {
                    fedges.add(e);
                }
            }
        }
        subdivideEdges(fedges, subDiv, minLength);
    }

    @Override
    public String toString() {
        return "WETriangleMesh: " + name + " vertices: " + getNumVertices()
                + " faces: " + getNumFaceDs() + " edges:" + getNumEdges();
    }

    /**
     * Applies the given matrix transform to all mesh vertices and updates all
     * face normals.
     * 
     * @param mat
     * @return itself
     */
    public WETriangleMeshD transform(Matrix4x4 mat) {
        return transform(mat, true);
    }

    /**
     * Applies the given matrix transform to all mesh vertices. If the
     * updateNormals flag is true, all face normals are updated automatically,
     * however vertex normals still need a manual update.
     * 
     * @param mat
     * @param updateNormals
     * @return itself
     */
    public WETriangleMeshD transform(Matrix4x4 mat, boolean updateNormals) {
        for (VertexD v : vertices.values()) {
            mat.applyToSelf(v);
        }
        rebuildIndex();
        if (updateNormals) {
            computeFaceDNormals();
        }
        return this;
    }

    public WETriangleMeshD translate(VecD3D trans) {
        return transform(matrix.identity().translateSelf(trans));
    }

    protected void updateEdge(WEVertexD va, WEVertexD vb, WEFaceD f) {
        edgeCheck.set(va, vb);
        WingedEdgeD e = edges.get(edgeCheck);
        if (e != null) {
            e.addFaceD(f);
        } else {
            e = new WingedEdgeD(va, vb, f, uniqueEdgeID++);
            edges.put(e, e);
            va.addEdge(e);
            vb.addEdge(e);
        }
        f.addEdge(e);
    }
}
