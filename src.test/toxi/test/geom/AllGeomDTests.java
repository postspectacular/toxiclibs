package toxi.test.geom;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllGeomDTests {

    public static Test suite() {
        TestSuite suite = new TestSuite(AllGeomDTests.class.getName());
        // $JUnit-BEGIN$
        suite.addTestSuite(AABBDTest.class);
        suite.addTestSuite(CircleDTest.class);
        suite.addTestSuite(LineD2DTest.class);
        suite.addTestSuite(LineD3DTest.class);
        suite.addTestSuite(MatrixDTest.class);
        suite.addTestSuite(OriginD3DTest.class);
        suite.addTestSuite(PlaneDTest.class);
        suite.addTestSuite(PolygonDTest.class);
        suite.addTestSuite(QuaternionDTest.class);
        suite.addTestSuite(RectDTest.class);
        suite.addTestSuite(SphereDTest.class);
        suite.addTestSuite(TreeDTest.class);
        suite.addTestSuite(TriangleD2DTest.class);
        suite.addTestSuite(TriangleMeshDTest.class);
        suite.addTestSuite(TriangleDTest.class);
        suite.addTestSuite(VecD3DTest.class);
        suite.addTestSuite(WEMeshDTest.class);
        // $JUnit-END$
        return suite;
    }

}
