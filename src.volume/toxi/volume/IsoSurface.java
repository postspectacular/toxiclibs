package toxi.volume;

import java.util.logging.Logger;

import toxi.geom.Vec3D;
import toxi.geom.util.OBJWriter;
import toxi.geom.util.STLWriter;
import toxi.geom.util.TriangleMesh;
import toxi.util.datatypes.ArrayUtil;

/**
 * IsoSurface class based on C version by Paul Bourke and Lingo version by
 * myself.
 */
public class IsoSurface {

    protected static final Logger logger =
            Logger.getLogger(IsoSurface.class.getName());

    protected Vec3D cellSize;
    protected Vec3D centreOffset;
    protected VolumetricSpace volume;

    public float isoValue;

    protected int resX, resY, resZ;
    protected int resX1, resY1, resZ1;

    protected float[] data;
    protected int sliceRes;
    protected int nextXY;

    protected Vec3D[] edgeVertices;
    protected int numVertices;

    protected int[] faces;
    protected int numFaces;

    public Vec3D[] faceNormals;

    public IsoSurface(VolumetricSpace volume) {
        this.volume = volume;
        cellSize =
                new Vec3D(volume.scale.x / volume.resX1, volume.scale.y
                        / volume.resY1, volume.scale.z / volume.resZ1);

        resX = volume.resX;
        resY = volume.resY;
        resZ = volume.resZ;
        resX1 = volume.resX1;
        resY1 = volume.resY1;
        resZ1 = volume.resZ1;

        sliceRes = volume.sliceRes;
        nextXY = resX + sliceRes;

        data = volume.data;
        centreOffset = volume.halfScale.getInverted();

        faces = new int[3 * volume.numCells];
        edgeVertices = new Vec3D[3 * volume.numCells];

        numVertices = 0;
        numFaces = 0;

        for (int z = 0, index = 0; z < resZ; z++) {
            for (int y = 0; y < resY; y++) {
                for (int x = 0; x < resX; x++) {
                    Vec3D v =
                            new Vec3D(x * cellSize.x + centreOffset.x, y
                                    * cellSize.y + centreOffset.y, z
                                    * cellSize.z + centreOffset.z);
                    edgeVertices[index] = v;
                    edgeVertices[index + 1] = v.copy();
                    edgeVertices[index + 2] = v.copy();
                    index += 3;
                }
            }
        }
    }

    public void computeFaceNormals() {
        if (faceNormals == null || faceNormals.length != numFaces) {
            faceNormals = new Vec3D[numFaces];
        }
        Vec3D[] verts = null;
        for (int i = 0; i < numFaces; i++) {
            verts = getVerticesForFace(i, verts);
            Vec3D n =
                    verts[0].sub(verts[1]).crossSelf(verts[0].sub(verts[2]))
                            .normalize();
            faceNormals[i] = n;
        }
    }

    /**
     * Computes the surface mesh for the given volumetric data and iso value.
     */
    public void computeSurface(final float iso) {
        isoValue = iso;
        float offsetZ = centreOffset.z;
        for (int z = 0; z < resZ1; z++) {
            int sliceOffset = sliceRes * z;
            float offsetY = centreOffset.y;
            for (int y = 0; y < resY1; y++) {
                float offsetX = centreOffset.x;
                int offset = resX * y + sliceOffset;
                for (int x = 0; x < resX1; x++) {
                    final int cellIndex = getCellIndex(offset);
                    if (cellIndex > 0) {
                        int n = 0;
                        int edgeIndex;
                        int[] cellTriangles =
                                MarchingCubesIndex.cellTriangles[cellIndex];
                        while ((edgeIndex = cellTriangles[n++]) != -1) {
                            int[] edgeOffsetInfo =
                                    MarchingCubesIndex.edgeOffsets[edgeIndex];
                            faces[numVertices++] =
                                    ((x + edgeOffsetInfo[0]) + resX
                                            * (y + edgeOffsetInfo[1]) + sliceRes
                                            * (z + edgeOffsetInfo[2]))
                                            * 3 + edgeOffsetInfo[3];
                        }
                        final int edgeFlags =
                                MarchingCubesIndex.edgesToCompute[cellIndex];
                        if (edgeFlags > 0) {
                            int edgeOffsetIndex = offset * 3;
                            float offsetData = data[offset];
                            float isoDiff = isoValue - offsetData;
                            if ((edgeFlags & 1) > 0) {
                                float t =
                                        isoDiff
                                                / (data[offset + 1] - offsetData);
                                edgeVertices[edgeOffsetIndex].x =
                                        offsetX + t * cellSize.x;
                            }
                            if ((edgeFlags & 2) > 0) {
                                float t =
                                        isoDiff
                                                / (data[offset + resX] - offsetData);
                                edgeVertices[edgeOffsetIndex + 1].y =
                                        offsetY + t * cellSize.y;
                            }
                            if ((edgeFlags & 4) > 0) {
                                float t =
                                        isoDiff
                                                / (data[offset + sliceRes] - offsetData);
                                edgeVertices[edgeOffsetIndex + 2].z =
                                        offsetZ + t * cellSize.z;
                            }
                        }
                    }
                    offsetX += cellSize.x;
                    offset++;
                }
                offsetY += cellSize.y;
            }
            offsetZ += cellSize.z;
        }
        numFaces = numVertices / 3;
    }

    public TriangleMesh computeSurfaceMesh(TriangleMesh mesh, final float iso) {
        if (mesh == null) {
            mesh = new TriangleMesh("isosurface-" + iso);
        } else {
            mesh.clear();
        }
        computeSurface(iso);
        Vec3D[] verts = null;
        for (int i = 0; i < numFaces; i++) {
            verts = getVerticesForFace(i, verts);
            mesh.addFace(verts[1], verts[2], verts[0]);
        }
        return mesh;
    }

    protected final int getCellIndex(int idx) {
        int cellIndex = 0;
        if (data[idx] < isoValue) {
            cellIndex |= 0x01;
        }
        if (data[idx + sliceRes] < isoValue) {
            cellIndex |= 0x08;
        }
        if (data[idx + resX] < isoValue) {
            cellIndex |= 0x10;
        }
        if (data[idx + nextXY] < isoValue) {
            cellIndex |= 0x80;
        }
        idx++;
        if (data[idx] < isoValue) {
            cellIndex |= 0x02;
        }
        if (data[idx + sliceRes] < isoValue) {
            cellIndex |= 0x04;
        }
        if (data[idx + resX] < isoValue) {
            cellIndex |= 0x20;
        }
        if (data[idx + nextXY] < isoValue) {
            cellIndex |= 0x40;
        }
        return cellIndex;
    }

    public Vec3D getNormalForFace(int faceID) {
        return faceNormals[faceID];
    }

    public int getNumFaces() {
        return numFaces;
    }

    public int getNumVertices() {
        return numVertices;
    }

    /**
     * Retrieves vertices for the requested mesh face/triangle.
     * 
     * @param faceID
     *            face index
     * @param vertices
     *            Vec3D array to place vertices in or null to create a new array
     *            automatically
     * @return vertices as Vec3D array
     */
    public Vec3D[] getVerticesForFace(int faceID, Vec3D[] vertices) {
        faceID *= 3;
        if (vertices == null) {
            vertices = new Vec3D[3];
        }
        vertices[0] = edgeVertices[faces[faceID]];
        vertices[1] = edgeVertices[faces[faceID + 1]];
        vertices[2] = edgeVertices[faces[faceID + 2]];
        return vertices;
    }

    /**
     * Resets mesh vertices to default positions and clears face index. Needs to
     * be called inbetween successive calls to {@link #computeSurface(float)}.
     */
    public void reset() {
        for (int z = 0, index = 0; z < resZ; z++) {
            for (int y = 0; y < resY; y++) {
                for (int x = 0; x < resX && index < numVertices; x++) {
                    Vec3D v =
                            edgeVertices[index].set(x * cellSize.x
                                    + centreOffset.x, y * cellSize.y
                                    + centreOffset.y, z * cellSize.z
                                    + centreOffset.z);
                    edgeVertices[index + 1].set(v);
                    edgeVertices[index + 2].set(v);
                    index += 3;
                }
            }
            if (index >= numVertices) {
                break;
            }
        }
        numFaces = 0;
        numVertices = 0;
    }

    /**
     * Saves the surface mesh as Wavefront OBJ format and automatically merges
     * shared vertices.
     * 
     * @param fn
     *            absolute path/filename to write mesh to
     */
    public void saveAsOBJ(String fn) {
        logger.info("saving surface as OBJ to: " + fn);
        OBJWriter obj = new OBJWriter();
        obj.beginSave(fn);
        obj.newObject("isosurface");
        // figure out unique used vertices
        int[] faceIndex = new int[numFaces];
        int numUniqueVertices = 0;
        for (int i = 0; i < numVertices; i++) {
            int idx =
                    ArrayUtil.indexInArray(faces[i], faceIndex,
                            numUniqueVertices);
            if (idx == -1) {
                faceIndex[numUniqueVertices++] = faces[i];
            }
            if ((i % 10000) == 0) {
                logger.fine((i * 100.0 / numFaces) + "% unique:"
                        + numUniqueVertices);
            }
        }
        for (int i = 0; i < numUniqueVertices; i++) {
            obj.vertex(edgeVertices[faceIndex[i]]);
        }
        logger.info("creating facelist...");
        obj.faceList();
        for (int i = 0; i < numVertices; i += 3) {
            int a =
                    ArrayUtil.indexInArray(faces[i], faceIndex,
                            numUniqueVertices);
            int b =
                    ArrayUtil.indexInArray(faces[i + 1], faceIndex,
                            numUniqueVertices);
            int c =
                    ArrayUtil.indexInArray(faces[i + 2], faceIndex,
                            numUniqueVertices);
            obj.face(c + 1, b + 1, a + 1);
            if ((i % 10000) == 0) {
                logger.fine((i * 100.0 / numFaces) + "%");
            }
        }
        obj.endSave();
    }

    /**
     * Saves surface mesh as binary STL file.
     * 
     * @param fn
     *            absolute path/filename to save to
     */
    public void saveAsSTL(String fn) {
        logger.info("saving surface as STL to: " + fn);
        STLWriter stl = new STLWriter();
        stl.beginSave(fn, numFaces);
        for (int i = 0, off = 0; i < numFaces; i++) {
            Vec3D a = edgeVertices[faces[off++]];
            Vec3D b = edgeVertices[faces[off++]];
            Vec3D c = edgeVertices[faces[off++]];
            stl.face(c, b, a);
            if ((i % 10000) == 0) {
                logger.fine((i * 100.0 / numFaces) + "%");
            }
        }
        stl.endSave();
    }
}
