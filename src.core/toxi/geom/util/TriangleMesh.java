package toxi.geom.util;

import java.util.ArrayList;
import java.util.logging.Logger;

import toxi.geom.AABB;
import toxi.geom.Vec3D;

public class TriangleMesh {

	public final class Face {
		public final Vertex a, b, c;
		public final Vec3D normal;

		Face(int aID, int bID, int cID) {
			a = vertices.get(aID);
			b = vertices.get(bID);
			c = vertices.get(cID);
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
		private int numFaces = 0;

		Vertex(Vec3D v, int id) {
			super(v);
			this.id = id;
		}

		final void addFaceNormal(Vec3D n) {
			normal.addSelf(n);
			numFaces++;
		}

		final void computeNormal() {
			normal.scaleSelf(1f / numFaces);
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

	public AABB bounds;

	protected final Vec3D centroid = new Vec3D();

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
	}

	public final void addFace(Vec3D a, Vec3D b, Vec3D c) {
		int aID = vertices.indexOf(a);
		if (aID == -1) {
			aID = numVertices;
			vertices.add(new Vertex(a, aID));
			numVertices++;
		}
		int bID = vertices.indexOf(b);
		if (bID == -1) {
			bID = numVertices;
			vertices.add(new Vertex(b, bID));
			numVertices++;
		}
		int cID = vertices.indexOf(c);
		if (cID == -1) {
			cID = numVertices;
			vertices.add(new Vertex(c, cID));
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
		bounds.setExtent(new Vec3D());
		bounds.set(new Vec3D());
		numVertices = 0;
		numFaces = 0;
	}

	public final void computeVertexNormals() {
		for (Vertex v : vertices) {
			v.computeNormal();
		}
	}

	public void enableLogger(boolean state) {
		this.isLoggerEnabled = state;
	}

	public AABB getBoundingBox() {
		final Vec3D minBounds = Vec3D.MAX_VALUE.copy();
		final Vec3D maxBounds = Vec3D.MIN_VALUE.copy();
		for (Vertex v : vertices) {
			minBounds.minSelf(v);
			maxBounds.maxSelf(v);
		}
		bounds = AABB.fromMinMax(minBounds, maxBounds);
		return bounds;
	}

	public final Vec3D getCentroid() {
		centroid.clear();
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
