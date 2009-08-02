package toxi.geom.util;

import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.logging.Logger;

import toxi.geom.AABB;
import toxi.geom.Vec3D;

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

		private final int id;
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
			normal.scaleSelf(1f / valence);
		}

		final public int getID() {
			return id;
		}

		final public int getValence() {
			return valence;
		}

		public String toString() {
			return id + ": p: " + super.toString() + " n:" + normal.toString();
		}
	}

	protected static final Logger logger = Logger.getLogger(TriangleMesh.class
			.getName());

	public final ArrayList<Face> faces;
	public String name;

	public AABB bounds;

	protected final Vec3D centroid = new Vec3D();

	protected int numVertices;
	protected int numFaces;

	protected boolean isLoggerEnabled = false;

	public LinkedHashMap<Vec3D, Vertex> vertices;

	public TriangleMesh(String name) {
		this(name, 1000, 3000);
	}

	@SuppressWarnings("serial")
	public TriangleMesh(String name, int numV, int numF) {
		this.name = name;
		faces = new ArrayList<Face>(numF);
		vertices = new LinkedHashMap<Vec3D, Vertex>(numV, 1.75f, false);
	}

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
			if (isLoggerEnabled) {
				logger.warning("ignorning invalid face: " + a + "," + b + ","
						+ c);
			}
		} else {
			Face f = new Face(va, vb, vc);
			faces.add(f);
			numFaces++;
		}
	}

	public final void clear() {
		vertices.clear();
		faces.clear();
		bounds = null;
		numVertices = 0;
		numFaces = 0;
	}

	public final void computeVertexNormals() {
		for (Vertex v : vertices.values()) {
			v.computeNormal();
		}
	}

	public void enableLogger(boolean state) {
		this.isLoggerEnabled = state;
	}

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

	public final Vec3D getCentroid() {
		centroid.clear();
		for (Vec3D v : vertices.values()) {
			centroid.addSelf(v);
		}
		return centroid.scaleSelf(1f / numVertices);
	}

	public final int[] getFacesAsArray() {
		int[] faceList = new int[faces.size() * 3];
		int i = 0;
		for (Face f : faces) {
			faceList[i++] = f.a.id;
			faceList[i++] = f.b.id;
			faceList[i++] = f.c.id;
		}
		return faceList;
	}

	public final float[] getMeshAsVertexArray() {
		float[] verts = new float[faces.size() * 9];
		int i = 0;
		for (Face f : faces) {
			verts[i++] = f.a.x;
			verts[i++] = f.a.y;
			verts[i++] = f.a.z;
			verts[i++] = f.b.x;
			verts[i++] = f.b.y;
			verts[i++] = f.b.z;
			verts[i++] = f.c.x;
			verts[i++] = f.c.y;
			verts[i++] = f.c.z;
		}
		return verts;
	}

	public final float[] getMeshNormalsAsArray() {
		float[] normals = new float[faces.size() * 9];
		int i = 0;
		for (Face f : faces) {
			normals[i++] = f.a.normal.x;
			normals[i++] = f.a.normal.y;
			normals[i++] = f.a.normal.z;
			normals[i++] = f.b.normal.x;
			normals[i++] = f.b.normal.y;
			normals[i++] = f.b.normal.z;
			normals[i++] = f.c.normal.x;
			normals[i++] = f.c.normal.y;
			normals[i++] = f.c.normal.z;
		}
		return normals;
	}

	public final int getNumFaces() {
		return numFaces;
	}

	public final int getNumVertices() {
		return numVertices;
	}

	public final float[] getUniqueVerticesAsArray() {
		float[] verts = new float[vertices.size() * 3];
		int i = 0;
		for (Vertex v : vertices.values()) {
			verts[i++] = v.x;
			verts[i++] = v.y;
			verts[i++] = v.z;
		}
		return verts;
	}

	public final void saveAsRaw(String fileName) {
		try {
			DataOutputStream ds = new DataOutputStream(new FileOutputStream(
					fileName));
			float[] verts = getMeshAsVertexArray();
			ds.writeInt(verts.length);
			for (float f : verts) {
				ds.writeFloat(f);
			}
			verts = getMeshNormalsAsArray();
			for (float f : verts) {
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

	@Override
	public String toString() {
		return "TriangleMesh: " + name + " vertices: " + getNumVertices()
				+ " faces: " + getNumFaces();
	}

	public static final TriangleMesh fromBinarySTL(String fileName) {
		return new STLReader().loadBinary(fileName);
	}
}
