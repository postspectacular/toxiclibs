package toxi.geom.volume;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Logger;

import toxi.geom.Vec3D;
import toxi.geom.util.OBJWriter;
import toxi.geom.util.STLWriter;

public class IsoSurface {

	protected static final Logger logger = Logger.getLogger(IsoSurface.class
			.getName());

	public float isovalue;

	protected int res, res2;
	protected float step;
	public float[] volumeData;
	public int numCells;

	public Vec3D[] edgeVertices;
	public int numVertices;

	public int[] faces;
	public int numFaces;

	// TODO make volume not strictly cubic, but allow different sizes for XYZ
	public IsoSurface(int res, float iso, float[] data) {
		this.res = res;
		this.res2 = res * res;
		this.isovalue = iso;

		step = 1.0f / (res - 1);

		numCells = res * res * res;
		volumeData = data;

		edgeVertices = new Vec3D[3 * numCells];
		numVertices = 0;

		faces = new int[3 * numCells];
		numFaces = 0;

		for (int z = 0, index = 0; z < res; z++) {
			for (int y = 0; y < res; y++) {
				for (int x = 0; x < res; x++) {
					Vec3D v = new Vec3D(x * step - 0.5f, y * step - 0.5f, z
							* step - 0.5f);
					edgeVertices[index] = v;
					edgeVertices[index + 1] = v.copy();
					edgeVertices[index + 2] = v.copy();
					index += 3;
				}
			}
		}
	}

	/**
	 * Computes the surface mesh for the given volumetric data and iso value.
	 */
	public void computeSurface() {
		float vx, vy, vz;
		int r1 = res - 1;
		vz = -0.5f;
		for (int z = 0; z < r1; z++) {
			vy = -0.5f;
			for (int y = 0; y < r1; y++) {
				vx = -0.5f;
				int sliceOffset = res * y + res2 * z;
				for (int x = 0; x < r1; x++) {
					int offset = x + sliceOffset;
					int cellIndex = getCellIndex(x, y, z);
					int n = 0;
					int edgeIndex;
					while ((edgeIndex = MarchingCubesIndex.cellTriangles[cellIndex][n++]) != -1) {
						int[] edgeOffsetInfo = MarchingCubesIndex.edgeOffsets[edgeIndex];
						faces[numFaces++] = ((x + edgeOffsetInfo[0]) + res
								* (y + edgeOffsetInfo[1]) + res2
								* (z + edgeOffsetInfo[2]))
								* 3 + edgeOffsetInfo[3];
					}
					int edgeFlags = MarchingCubesIndex.edgesToCompute[cellIndex];
					if (edgeFlags > 0) {
						int edgeOffsetIndex = offset * 3;
						float isoDiff = isovalue - volumeData[offset];
						if ((edgeFlags & 1) > 0) {
							float t = isoDiff
									/ (volumeData[offset + 1] - volumeData[offset]);
							edgeVertices[edgeOffsetIndex].x = vx + t * step;
						}
						if ((edgeFlags & 2) > 0) {
							float t = isoDiff
									/ (volumeData[offset + res] - volumeData[offset]);
							edgeVertices[edgeOffsetIndex + 1].y = vy + t * step;
						}
						if ((edgeFlags & 4) > 0) {
							float t = isoDiff
									/ (volumeData[offset + res2] - volumeData[offset]);
							edgeVertices[edgeOffsetIndex + 2].z = vz + t * step;
						}
					}
					vx += step;
				}
				vy += step;
			}
			vz += step;
		}
		numVertices = numFaces;
		numFaces /= 3;
	}

	protected int getCellIndex(int x, int y, int z) {
		int cellIndex = 0;
		int rz = res2 * z;
		int rz1 = res2 * (z + 1);
		int ry = res * y;
		int ry1 = res * (y + 1);
		if (volumeData[x + ry + rz] < isovalue) {
			cellIndex |= 0x01;
		}
		if (volumeData[x + 1 + ry + rz] < isovalue) {
			cellIndex |= 0x02;
		}
		if (volumeData[x + 1 + ry + rz1] < isovalue) {
			cellIndex |= 0x04;
		}
		if (volumeData[x + ry + rz1] < isovalue) {
			cellIndex |= 0x08;
		}
		if (volumeData[x + ry1 + rz] < isovalue) {
			cellIndex |= 0x10;
		}
		if (volumeData[x + 1 + ry1 + rz] < isovalue) {
			cellIndex |= 0x20;
		}
		if (volumeData[x + 1 + ry1 + rz1] < isovalue) {
			cellIndex |= 0x40;
		}
		if (volumeData[x + ry1 + rz1] < isovalue) {
			cellIndex |= 0x80;
		}
		return cellIndex;
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
		if (faceID > faces.length - 3) {
			return null;
		}
		if (vertices == null) {
			vertices = new Vec3D[3];
		}
		vertices[0] = edgeVertices[faces[faceID]];
		vertices[1] = edgeVertices[faces[faceID + 1]];
		vertices[2] = edgeVertices[faces[faceID + 2]];
		return vertices;
	}

	protected int indexInArray(int needle, int[] stack, int maxLen) {
		for (int i = 0; i < maxLen; i++) {
			if (stack[i] == needle) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Resets mesh vertices to default positions and clears face index. Needs to
	 * be called inbetween successive calls to {@link #computeSurface()}.
	 */
	public void resetVertices() {
		for (int z = 0, index = 0; z < res; z++) {
			for (int y = 0; y < res; y++) {
				for (int x = 0; x < res; x++) {
					edgeVertices[index].set(x * step - 0.5f, y * step - 0.5f, z
							* step - 0.5f);
					edgeVertices[index + 1].set(edgeVertices[index]);
					edgeVertices[index + 2].set(edgeVertices[index]);
					index += 3;
				}
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
			int idx = indexInArray(faces[i], faceIndex, numUniqueVertices);
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
			int a = indexInArray(faces[i], faceIndex, numUniqueVertices);
			int b = indexInArray(faces[i + 1], faceIndex, numUniqueVertices);
			int c = indexInArray(faces[i + 2], faceIndex, numUniqueVertices);
			obj.face(a + 1, b + 1, c + 1);
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
			stl.face(a, b, c);
			if ((i % 10000) == 0) {
				logger.fine((i * 100.0 / numFaces) + "%");
			}
		}
		stl.endSave();
	}

	/**
	 * Saves volume data float array in raw binary format.
	 * 
	 * @param fn
	 *            absolute path/filename to save to
	 */
	public void saveVolumeData(String fn) {
		logger.info("saving volume data...");
		try {
			DataOutputStream ds = new DataOutputStream(new FileOutputStream(fn));
			// ds.writeInt(volumeData.length);
			for (float element : volumeData) {
				ds.writeFloat(element);
			}
			ds.flush();
			ds.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
