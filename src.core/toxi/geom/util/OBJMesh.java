package toxi.geom.util;

import java.util.ArrayList;
import java.util.Iterator;

import toxi.geom.Vec3D;

public class OBJMesh {
	public ArrayList<Vec3D> vertices = new ArrayList<Vec3D>();
	public ArrayList<int[]> faces = new ArrayList<int[]>();
	public String name;

	public OBJMesh(String n) {
		name = n;
	}

	public void addFace(Vec3D a, Vec3D b, Vec3D c) {
		int aID = vertices.indexOf(a) + 1;
		int bID = vertices.indexOf(b) + 1;
		int cID = vertices.indexOf(c) + 1;
		if (aID == 0) {
			vertices.add(a);
			aID = vertices.size();
		}
		if (bID == 0) {
			vertices.add(b);
			bID = vertices.size();
		}
		if (cID == 0) {
			vertices.add(c);
			cID = vertices.size();
		}
		faces.add(new int[] { aID, bID, cID });
	}

	public void write(OBJWriter obj) {
		int vOffset = obj.getCurrVertexOffset();
		obj.newObject(name);
		for (Iterator<Vec3D> i = vertices.iterator(); i.hasNext();) {
			obj.vertex(i.next());
		}
		// faces
		for (Iterator<int[]> i = faces.iterator(); i.hasNext();) {
			int[] f = i.next();
			obj.face(f[0] + vOffset, f[1] + vOffset, f[2] + vOffset);
		}
	}
}
