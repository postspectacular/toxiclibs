package toxi.test.geom;

import junit.framework.TestCase;
import toxi.geom.Vec3D;
import toxi.geom.util.TriangleMesh;

public class TriangleMeshTest extends TestCase {

	TriangleMesh mesh;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		mesh = new TriangleMesh("foo");
		mesh.addFace(new Vec3D(), new Vec3D(100, 100, 0), new Vec3D(100, 0, 0));
		mesh.addFace(new Vec3D(100, 100, 0), new Vec3D(100, 0, -100),
				new Vec3D(100, 0, 0));
		mesh.addFace(new Vec3D(100f, 0, -100), new Vec3D(0, 100, -100),
				new Vec3D(0, 0, -100));
	}

	public void testFaceNormals() {
		mesh.computeFaceNormals();
		assertEquals(new Vec3D(0, 0, 1), mesh.faces.get(0).normal);
		assertEquals(new Vec3D(1, 0, 0), mesh.faces.get(1).normal);
		assertEquals(new Vec3D(0, 0, -1), mesh.faces.get(2).normal);
	}

	public void testUniqueVertices() {
		assertEquals(6, mesh.vertices.size());
	}

	public void testVertexNormals() {
		mesh.computeVertexNormals();
		TriangleMesh.Vertex[] verts = null;
		for (TriangleMesh.Face f : mesh.faces) {
			verts = f.getVertices(verts);
			System.out.println(f);
		}
	}
}
