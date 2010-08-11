package toxi.geom.mesh;

import toxi.geom.AABB;
import toxi.geom.ReadonlyVec3D;
import toxi.geom.Sphere;
import toxi.geom.Vec2D;
import toxi.geom.Vec3D;

public interface Mesh3D {

    /**
     * Adds the given 3 points as triangle face to the mesh. The assumed vertex
     * order is anti-clockwise.
     * 
     * @param a
     * @param b
     * @param c
     */
    public Mesh3D addFace(Vec3D a, Vec3D b, Vec3D c);

    /**
     * Adds the given 3 points as triangle face to the mesh and assigns the
     * given texture coordinates to each vertex. The assumed vertex order is
     * anti-clockwise.
     * 
     * @param a
     * @param b
     * @param c
     * @param uvA
     * @param uvB
     * @param uvC
     * @return
     */
    public Mesh3D addFace(Vec3D a, Vec3D b, Vec3D c, Vec2D uvA, Vec2D uvB,
            Vec2D uvC);

    public Mesh3D addFace(Vec3D a, Vec3D b, Vec3D c, Vec3D n);

    public Mesh3D addFace(Vec3D a, Vec3D b, Vec3D c, Vec3D n, Vec2D uvA,
            Vec2D uvB, Vec2D uvC);

    /**
     * Centers the mesh around the given pivot point (the centroid of its AABB).
     * Method also updates & returns the new bounding box.
     * 
     * @param origin
     *            new centroid or null (defaults to {0,0,0})
     */
    public AABB center(ReadonlyVec3D origin);

    /**
     * Clears all counters, and vertex & face buffers.
     */
    public Mesh3D clear();

    /**
     * Computes the mesh centroid, the average position of all vertices.
     * 
     * @return centre point
     */
    public Vec3D computeCentroid();

    /**
     * Re-calculates all face normals.
     */
    public Mesh3D computeFaceNormals();

    /**
     * Computes the smooth vertex normals for the entire mesh.
     */
    public Mesh3D computeVertexNormals();

    /**
     * Flips the vertex ordering between clockwise and anti-clockwise. Face
     * normals are updated automatically too.
     * 
     * @return itself
     */
    public Mesh3D flipVertexOrder();

    /**
     * Flips all vertices along the Y axis and reverses the vertex ordering of
     * all faces to compensate and keep the direction of normals intact.
     * 
     * @return itself
     */
    public Mesh3D flipYAxis();

    /**
     * Computes & returns the axis-aligned bounding box of the mesh.
     * 
     * @return bounding box
     */
    public AABB getBoundingBox();

    /**
     * Computes & returns the bounding sphere of the mesh. The origin of the
     * sphere is the mesh's centroid.
     * 
     * @return bounding sphere
     */
    public Sphere getBoundingSphere();

    public Vertex getClosestVertexToPoint(ReadonlyVec3D p);

    /**
     * Returns the number of triangles used.
     * 
     * @return face count
     */
    public int getNumFaces();

    /**
     * Returns the number of actual vertices used (unique vertices).
     * 
     * @return vertex count
     */
    public int getNumVertices();

}
