package toxi.test.geom;

import java.util.ArrayList;

import junit.framework.TestCase;
import toxi.geom.VecD3D;
import toxi.geom.mesh.FaceD;
import toxi.geom.mesh.STLReader;
import toxi.geom.mesh.TriangleMeshD;
import toxi.geom.mesh.VertexD;

public class TriangleMeshDTest extends TestCase {

    TriangleMeshD mesh;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mesh = new TriangleMeshD("foo");
        mesh.addFaceD(new VecD3D(), new VecD3D(100, 100, 0), new VecD3D(100, 0, 0));
        mesh.addFaceD(new VecD3D(100, 100, 0), new VecD3D(100, 0, -100),
                new VecD3D(100, 0, 0));
        mesh.addFaceD(new VecD3D(100f, 0, -100), new VecD3D(0, 100, -100),
                new VecD3D(0, 0, -100));
    }

    public void testFaceDNormals() {
        assertEquals(new VecD3D(0, 0, 1), mesh.faces.get(0).normal);
        assertEquals(new VecD3D(1, 0, 0), mesh.faces.get(1).normal);
        assertEquals(new VecD3D(0, 0, -1), mesh.faces.get(2).normal);
    }

    public void testSTLImport() {
        double total = 0;
        int numIter = 100;
        for (int i = 0; i < numIter; i++) {
            long t = System.nanoTime();
            mesh = (TriangleMeshD) new STLReader().loadBinary("test/test.stl",
                    STLReader.TRIANGLEMESH);
            total += (System.nanoTime() - t);
        }
        System.out.println("avg. mesh construction time: " + total * 1e-6
                / numIter);
        assertNotNull(mesh);
        assertEquals(714, mesh.getNumVertices());
        assertEquals(1424, mesh.getNumFaceDs());
        System.out.println(mesh);
    }

    public void testUniqueVertices() {
        ArrayList<VertexD> verts = new ArrayList<VertexD>(mesh.vertices.values());
        assertEquals(6, mesh.vertices.size());
        for (FaceD f : mesh.faces) {
            assertEquals(verts.get(f.a.id), f.a);
            assertEquals(verts.get(f.b.id), f.b);
            assertEquals(verts.get(f.c.id), f.c);
        }
    }

    public void testVertexDNormals() {
        mesh.computeVertexDNormals();
        VertexD[] verts = null;
        for (FaceD f : mesh.faces) {
            verts = f.getVertices(verts);
            System.out.println(f);
        }
    }
}
