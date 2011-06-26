package toxi.test.geom;

import junit.framework.TestCase;
import toxi.geom.Triangle3D;
import toxi.geom.Vec3D;
import toxi.geom.mesh.Face;
import toxi.geom.mesh.WEFace;
import toxi.geom.mesh.WETriangleMesh;
import toxi.geom.mesh.WEVertex;
import toxi.geom.mesh.WingedEdge;
import toxi.geom.mesh.subdiv.MidpointSubdivision;

public class WEMeshTest extends TestCase {

    private WETriangleMesh m;

    /*
     * (non-Javadoc)
     * 
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        m = new WETriangleMesh("plane", 4, 2);
        m.addFace(new Vec3D(), new Vec3D(100, 0, 0), new Vec3D(100, 100, 0));
        m.addFace(new Vec3D(), new Vec3D(100, 100, 0), new Vec3D(0, 100, 0));
        super.setUp();
    }

    public void testAddFace() {
        assertEquals(5, m.edges.size());
        System.out.println("mesh edges:");
        for (WingedEdge e : m.edges.values()) {
            System.out.println(e);
        }
        WEVertex v = (WEVertex) m.vertices.get(new Vec3D());
        assertEquals(3, v.edges.size());
        assertEquals(1, v.edges.get(0).faces.size());
        assertEquals(2, v.edges.get(1).faces.size());
        System.out.println("vertex edges:");
        for (WingedEdge e : v.edges) {
            System.out.println(e);
        }
    }

    public void testFaceEdgeCount() {
        for (Face f : m.faces) {
            assertEquals(3, ((WEFace) f).edges.size());
        }
    }

    public void testPerforate() {
        m.removeFace(m.getFaces().get(0));
        WEFace f = (WEFace) m.getFaces().get(0);
        m.perforateFace(f, 0.5f);
        System.out.println(m.edges.size() + " edges");
    }

    public void testRemoveFace() {
        assertEquals(5, m.edges.size());
        WEFace f = (WEFace) m.getFaces().get(0);
        m.removeFace(f);
        assertEquals(3, m.edges.size());
        assertEquals(3, m.vertices.size());
    }

    public void testSplitEdge() {
        WingedEdge e = ((WEVertex) m.vertices.get(new Vec3D())).edges.get(1);
        m.splitEdge(e, new MidpointSubdivision());
        assertEquals(4, m.faces.size());
        assertEquals(8, m.edges.size());
        m.computeVertexNormals();
        for (Face f : m.faces) {
            System.out.println(Triangle3D.isClockwiseInXY(f.a, f.b, f.c) + " "
                    + f);
        }
        assertEquals(3, ((WEVertex) m.faces.get(0).a).edges.size());
        assertEquals(3, ((WEVertex) m.faces.get(0).b).edges.size());
        assertEquals(4, ((WEVertex) m.faces.get(0).c).edges.size());
    }

    public void testSubdivide() {
        m.subdivide();
        assertEquals(8, m.faces.size());
    }
}