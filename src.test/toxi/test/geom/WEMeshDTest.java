package toxi.test.geom;

import junit.framework.TestCase;
import toxi.geom.TriangleD3D;
import toxi.geom.VecD3D;
import toxi.geom.mesh.FaceD;
import toxi.geom.mesh.WEFaceD;
import toxi.geom.mesh.WETriangleMeshD;
import toxi.geom.mesh.WEVertexD;
import toxi.geom.mesh.WingedEdgeD;
import toxi.geom.mesh.subdiv.MidpointSubdivisionD;

public class WEMeshDTest extends TestCase {

    private WETriangleMeshD m;

    /*
     * (non-Javadoc)
     * 
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        m = new WETriangleMeshD("plane", 4, 2);
        m.addFaceD(new VecD3D(), new VecD3D(100, 0, 0), new VecD3D(100, 100, 0));
        m.addFaceD(new VecD3D(), new VecD3D(100, 100, 0), new VecD3D(0, 100, 0));
        super.setUp();
    }

    public void testAddFaceD() {
        assertEquals(5, m.edges.size());
        System.out.println("mesh edges:");
        for (WingedEdgeD e : m.edges.values()) {
            System.out.println(e);
        }
        WEVertexD v = (WEVertexD) m.vertices.get(new VecD3D());
        assertEquals(3, v.edges.size());
        assertEquals(1, v.edges.get(0).faces.size());
        assertEquals(2, v.edges.get(1).faces.size());
        System.out.println("vertex edges:");
        for (WingedEdgeD e : v.edges) {
            System.out.println(e);
        }
    }

    public void testFaceDEdgeCount() {
        for (FaceD f : m.faces) {
            assertEquals(3, ((WEFaceD) f).edges.size());
        }
    }

    public void testPerforate() {
        m.removeFaceD(m.getFaceDs().get(0));
        WEFaceD f = (WEFaceD) m.getFaceDs().get(0);
        m.perforateFaceD(f, 0.5f);
        System.out.println(m.edges.size() + " edges");
    }

    public void testRemoveFaceD() {
        assertEquals(5, m.edges.size());
        WEFaceD f = (WEFaceD) m.getFaceDs().get(0);
        m.removeFaceD(f);
        assertEquals(3, m.edges.size());
        assertEquals(3, m.vertices.size());
    }

    public void testSplitEdge() {
        WingedEdgeD e = ((WEVertexD) m.vertices.get(new VecD3D())).edges.get(1);
        m.splitEdge(e, new MidpointSubdivisionD());
        assertEquals(4, m.faces.size());
        assertEquals(8, m.edges.size());
        m.computeVertexDNormals();
        for (FaceD f : m.faces) {
            System.out.println(TriangleD3D.isClockwiseInXY(f.a, f.b, f.c) + " "
                    + f);
        }
        assertEquals(3, ((WEVertexD) m.faces.get(0).a).edges.size());
        assertEquals(3, ((WEVertexD) m.faces.get(0).b).edges.size());
        assertEquals(4, ((WEVertexD) m.faces.get(0).c).edges.size());
    }

    public void testSubdivide() {
        m.subdivide();
        assertEquals(8, m.faces.size());
    }
}