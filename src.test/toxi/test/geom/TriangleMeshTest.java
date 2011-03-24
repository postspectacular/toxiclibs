package toxi.test.geom;

import java.util.ArrayList;

import junit.framework.TestCase;
import toxi.geom.Vec3D;
import toxi.geom.mesh.Face;
import toxi.geom.mesh.STLReader;
import toxi.geom.mesh.TriangleMesh;
import toxi.geom.mesh.Vertex;

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
        assertEquals(new Vec3D(0, 0, 1), mesh.faces.get(0).normal);
        assertEquals(new Vec3D(1, 0, 0), mesh.faces.get(1).normal);
        assertEquals(new Vec3D(0, 0, -1), mesh.faces.get(2).normal);
    }

    public void testSTLImport() {
        double total = 0;
        int numIter = 100;
        for (int i = 0; i < numIter; i++) {
            long t = System.nanoTime();
            mesh = (TriangleMesh) new STLReader().loadBinary("test/test.stl",
                    STLReader.TRIANGLEMESH);
            total += (System.nanoTime() - t);
        }
        System.out.println("avg. mesh construction time: " + total * 1e-6
                / numIter);
        assertNotNull(mesh);
        assertEquals(714, mesh.getNumVertices());
        assertEquals(1424, mesh.getNumFaces());
        System.out.println(mesh);
    }

    public void testUniqueVertices() {
        ArrayList<Vertex> verts = new ArrayList<Vertex>(mesh.vertices.values());
        assertEquals(6, mesh.vertices.size());
        for (Face f : mesh.faces) {
            assertEquals(verts.get(f.a.id), f.a);
            assertEquals(verts.get(f.b.id), f.b);
            assertEquals(verts.get(f.c.id), f.c);
        }
    }

    public void testVertexNormals() {
        mesh.computeVertexNormals();
        Vertex[] verts = null;
        for (Face f : mesh.faces) {
            verts = f.getVertices(verts);
            System.out.println(f);
        }
    }
}
