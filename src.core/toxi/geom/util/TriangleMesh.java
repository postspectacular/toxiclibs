package toxi.geom.util;

import java.util.ArrayList;
import java.util.logging.Logger;

import toxi.geom.Vec3D;

public class TriangleMesh {

	public final class Face {
		public final Vertex a, b, c;
		public Vec3D normal;

		Face(int aID, int bID, int cID) {
			a = vertices.get(aID);
			b = vertices.get(bID);
			c = vertices.get(cID);
			a.addFace(this);
			b.addFace(this);
			c.addFace(this);
		}

		public final Vec3D computeNormal() {
			normal = a.sub(c).crossSelf(a.sub(b)).normalize();
			return normal;
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
		private final int id;

		final ArrayList<Face> faces = new ArrayList<Face>(3);
		public final Vec3D normal = new Vec3D();

		Vertex(Vec3D v, int id) {
			super(v);
			this.id = id;
		}

		final void addFace(Face f) {
			faces.add(f);
		}

		final void computeNormal() {
			normal.clear();
			for (Face f : faces) {
				normal.addSelf(f.normal);
			}
			normal.scaleSelf(1f / faces.size());
		}

		public String toString() {
			return id + ": p: " + super.toString() + " n:" + normal.toString();
		}
	}

	protected static final Logger logger = Logger.getLogger(TriangleMesh.class
			.getName());

	public final ArrayList<Vertex> vertices;
	public final ArrayList<Face> faces;
	public String name;

	public final Vec3D minBounds, maxBounds;

	protected Vec3D centroid;

	protected int numVertices;
	protected int numFaces;

	protected boolean isLoggerEnabled = false;

	public TriangleMesh(String name) {
		this(name, 1000, 3000);
	}

	public TriangleMesh(String name, int numV, int numF) {
		this.name = name;
		vertices = new ArrayList<Vertex>(numV);
		faces = new ArrayList<Face>(numF);
		minBounds = Vec3D.MAX_VALUE.copy();
		maxBounds = Vec3D.MIN_VALUE.copy();
	}

	public final void addFace(Vec3D a, Vec3D b, Vec3D c) {
		int aID = vertices.indexOf(a);
		if (aID == -1) {
			aID = numVertices;
			vertices.add(new Vertex(a, aID));
			minBounds.minSelf(a);
			maxBounds.maxSelf(a);
			numVertices++;
		}
		int bID = vertices.indexOf(b);
		if (bID == -1) {
			bID = numVertices;
			vertices.add(new Vertex(b, bID));
			minBounds.minSelf(b);
			maxBounds.maxSelf(b);
			numVertices++;
		}
		int cID = vertices.indexOf(c);
		if (cID == -1) {
			cID = numVertices;
			vertices.add(new Vertex(c, cID));
			minBounds.minSelf(c);
			maxBounds.maxSelf(c);
			numVertices++;
		}
		if (aID == bID || aID == cID || bID == cID) {
			if (isLoggerEnabled) {
				logger.warning("ignorning invalid face: " + a + "," + b + ","
						+ c);
			}
		} else {
			faces.add(new Face(aID, bID, cID));
			numFaces++;
		}
	}

	public final void clear() {
		vertices.clear();
		faces.clear();
		numVertices = 0;
		numFaces = 0;
		minBounds.set(Vec3D.MAX_VALUE);
		maxBounds.set(Vec3D.MIN_VALUE);
	}

	public final void computeFaceNormals() {
		for (Face t : faces) {
			t.computeNormal();
		}
	}

	public final void computeVertexNormals() {
		computeFaceNormals();
		for (Vertex v : vertices) {
			v.computeNormal();
		}
	}

	public void enableLogger(boolean state) {
		this.isLoggerEnabled = state;
	}

	public final Vec3D getCentroid() {
		centroid = new Vec3D();
		for (Vec3D v : vertices) {
			centroid.addSelf(v);
		}
		return centroid.scaleSelf(1f / numVertices);
	}

	public final int getNumFaces() {
		return numFaces;
	}

	public final int getNumVertices() {
		return numVertices;
	}
}
