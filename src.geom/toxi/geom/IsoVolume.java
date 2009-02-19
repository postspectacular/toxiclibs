package toxi.geom;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import toxi.geom.util.OBJWriter;
import toxi.geom.util.STLWriter;

public class IsoVolume {
	public float isovalue;

	protected int res, res2;
	protected float step;
	public float[] volumeData;
	protected int numCells;

	protected Vec3D[] edgeVertices;
	protected int numVertices;

	protected int[] faces;
	protected int numFaces;

	public IsoVolume(int res, float iso, float[] grid) {
		this.res = res;
		this.res2 = res * res;
		this.isovalue = iso;

		step = 1.0f / (res - 1);

		numCells = res * res * res;
		volumeData = grid;

		edgeVertices = new Vec3D[3 * numCells];
		numVertices = 0;

		faces = new int[3 * numCells];
		numFaces = 0;

		int x, y, z, index;
		index = 0;
		for (z = 0; z < res; z++) {
			for (y = 0; y < res; y++) {
				for (x = 0; x < res; x++) {
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

	int getCellIndex(int x, int y, int z) {
		int cellIndex = 0;
		int rz = res2 * z;
		int rz1 = res2 * (z + 1);
		int ry = res * y;
		int ry1 = res * (y + 1);
		if (volumeData[x + ry + rz] < isovalue)
			cellIndex |= 0x01;
		if (volumeData[x + 1 + ry + rz] < isovalue)
			cellIndex |= 0x02;
		if (volumeData[x + 1 + ry + rz1] < isovalue)
			cellIndex |= 0x04;
		if (volumeData[x + ry + rz1] < isovalue)
			cellIndex |= 0x08;
		if (volumeData[x + ry1 + rz] < isovalue)
			cellIndex |= 0x10;
		if (volumeData[x + 1 + ry1 + rz] < isovalue)
			cellIndex |= 0x20;
		if (volumeData[x + 1 + ry1 + rz1] < isovalue)
			cellIndex |= 0x40;
		if (volumeData[x + ry1 + rz1] < isovalue)
			cellIndex |= 0x80;
		return cellIndex;
	}

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
					int toCompute = MarchingCubesIndex.edgesToCompute[cellIndex];
					if (toCompute > 0) {
						int edgeOffsetIndex = offset * 3;
						if ((toCompute & 1) > 0) {
							float t = (isovalue - volumeData[offset])
									/ (volumeData[offset + 1] - volumeData[offset]);
							edgeVertices[edgeOffsetIndex].x = vx + t * step;
						}
						if ((toCompute & 2) > 0) {
							float t = (isovalue - volumeData[offset])
									/ (volumeData[offset + res] - volumeData[offset]);
							edgeVertices[edgeOffsetIndex + 1].y = vy + t * step;
						}
						if ((toCompute & 4) > 0) {
							float t = (isovalue - volumeData[offset])
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

	void saveAsOBJ(String fn) {
		OBJWriter obj = new OBJWriter();
		obj.beginSave(fn);
		obj.newObject("catype");
		// figure out unique used vertices
		int[] faceIndex = new int[numFaces];
		int numUniqueVertices = 0;
		for (int i = 0; i < numFaces; i++) {
			int idx = indexInArray(faces[i], faceIndex, numUniqueVertices);
			if (idx == -1)
				faceIndex[numUniqueVertices++] = faces[i];
			if ((i % 10000) == 0)
				System.out.println((i * 100.0 / numFaces) + "% unique:"
						+ numUniqueVertices);
		}
		for (int i = 0; i < numUniqueVertices; i++) {
			obj.vertex(edgeVertices[faceIndex[i]]);
		}
		obj.faceList();
		System.out.println("creating facelist...");
		for (int i = 0; i < numFaces; i += 3) {
			int a = indexInArray(faces[i], faceIndex, numUniqueVertices);
			int b = indexInArray(faces[i + 1], faceIndex, numUniqueVertices);
			int c = indexInArray(faces[i + 2], faceIndex, numUniqueVertices);
			obj.face(a, b, c);
			if ((i % 10000) == 0)
				System.out.println((i * 100.0 / numFaces) + "%");
		}
		obj.endSave();
	}

	void saveAsSTL(String fn) {
		STLWriter stl = new STLWriter();
		stl.beginSave(fn, numFaces);
		for (int i = 0, off = 0; i < numFaces; i++) {
			Vec3D a = edgeVertices[faces[off++]];
			Vec3D b = edgeVertices[faces[off++]];
			Vec3D c = edgeVertices[faces[off++]];
			stl.face(a, b, c);
			if ((i % 10000) == 0)
				System.out.println((i * 100.0 / numFaces) + "%");
		}
		stl.endSave();
	}

	int indexInArray(int needle, int[] stack, int maxLen) {
		for (int i = 0; i < maxLen; i++) {
			if (stack[i] == needle)
				return i;
		}
		return -1;
	}

	void saveCloud(String fn) {
		System.out.println("saving cloud...");
		try {
			DataOutputStream ds = new DataOutputStream(new FileOutputStream(fn));
			ds.writeInt(volumeData.length);
			for (int i = 0; i < volumeData.length; i++) {
				ds.writeFloat(volumeData[i]);
			}
			ds.flush();
			ds.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
