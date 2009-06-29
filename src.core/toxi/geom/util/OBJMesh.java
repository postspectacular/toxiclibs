package toxi.geom.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Logger;

import toxi.geom.Triangle;
import toxi.geom.Vec3D;

/**
 * Basic mesh container for use with {@link OBJWriter} exporter. Meshes are
 * build face by face and normals can be computed automatically in a second
 * step. Please see the OBJMesh example for concrete usage details.
 * 
 * @author toxi
 */
public class OBJMesh {
	protected static final Logger logger = Logger.getLogger(OBJMesh.class
			.getName());

	public ArrayList<Vec3D> vertices = new ArrayList<Vec3D>();
	public ArrayList<Vec3D> faceNormals = new ArrayList<Vec3D>();
	public ArrayList<int[]> faces = new ArrayList<int[]>();
	public String name;

	protected Vec3D centroid;
	protected boolean useNormals = false;

	public OBJMesh(String n) {
		name = n;
	}

	public void addFace(Vec3D a, Vec3D b, Vec3D c) {
		int aID = vertices.indexOf(a);
		if (aID == -1) {
			vertices.add(a);
			aID = vertices.size() - 1;
		}
		int bID = vertices.indexOf(b);
		if (bID == -1) {
			vertices.add(b);
			bID = vertices.size() - 1;
		}
		int cID = vertices.indexOf(c);
		if (cID == -1) {
			vertices.add(c);
			cID = vertices.size() - 1;
		}
		if (aID == bID || aID == cID || bID == cID) {
			logger.warning("ignorning invalid face: " + a + "," + b + "," + c);
		}
		else {
			faces.add(new int[] { aID, bID, cID });
		}
	}

	public Vec3D computeCentroid() {
		centroid = new Vec3D();
		for (Iterator<Vec3D> i = vertices.iterator(); i.hasNext();) {
			centroid.addSelf(i.next());
		}
		centroid.scaleSelf(1f / vertices.size());
		logger.info("mesh \"" + name + "\" centroid @ " + centroid);
		return centroid.copy();
	}

	public void computeNormals() {
		useNormals = true;
		faceNormals.clear();
		Triangle t = null;
		computeCentroid();
		for (Iterator<int[]> i = faces.iterator(); i.hasNext();) {
			int[] f = i.next();
			Vec3D a = vertices.get(f[0]);
			Vec3D b = vertices.get(f[1]);
			Vec3D c = vertices.get(f[2]);
			if (t == null)
				t = new Triangle(a, b, c);
			else {
				t.set(a, b, c);
			}
			Vec3D n = t.computeNormal();
			Vec3D dc = t.computeCentroid().sub(centroid);
			if (n.dot(dc) < 0) {
				logger.info("flipping normal: " + n + " for " + t);
				n.invert();
			}
			faceNormals.add(n);
		}
	}

	public Vec3D getCentroid() {
		if (centroid == null) {
			computeCentroid();
		}
		return centroid.copy();
	}

	public String toString() {
		return "name: " + name + " vertices: " + vertices.size() + " faces: "
				+ faces.size() + " normals: " + useNormals;
	}

	public void write(OBJWriter obj) {
		int vOffset = obj.getCurrVertexOffset() + 1;
		int nOffset = obj.getCurrNormalOffset() + 1;
		logger.info("writing OBJMesh: " + this.toString());
		obj.newObject(name);
		// vertices
		for (Iterator<Vec3D> i = vertices.iterator(); i.hasNext();) {
			obj.vertex(i.next());
		}
		if (useNormals) {
			// normals + faces
			for (Iterator<Vec3D> i = faceNormals.iterator(); i.hasNext();) {
				obj.normal(i.next());
			}
			for (Iterator<int[]> i = faces.iterator(); i.hasNext();) {
				int[] f = i.next();
				obj.faceWithNormals(f[0] + vOffset, f[1] + vOffset, f[2]
						+ vOffset, nOffset, nOffset, nOffset);
				nOffset++;
			}
		}
		else {
			// faces only
			for (Iterator<int[]> i = faces.iterator(); i.hasNext();) {
				int[] f = i.next();
				obj.face(f[0] + vOffset, f[1] + vOffset, f[2] + vOffset);
			}
		}
	}
}
