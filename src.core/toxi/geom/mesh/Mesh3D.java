/*
 *   __               .__       .__  ._____.           
 * _/  |_  _______  __|__| ____ |  | |__\_ |__   ______
 * \   __\/  _ \  \/  /  |/ ___\|  | |  || __ \ /  ___/
 *  |  | (  <_> >    <|  \  \___|  |_|  || \_\ \\___ \ 
 *  |__|  \____/__/\_ \__|\___  >____/__||___  /____  >
 *                   \/       \/             \/     \/ 
 *
 * Copyright (c) 2006-2011 Karsten Schmidt
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * http://creativecommons.org/licenses/LGPL/2.1/
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301, USA
 */

package toxi.geom.mesh;

import java.util.Collection;

import toxi.geom.AABB;
import toxi.geom.ReadonlyVec3D;
import toxi.geom.Sphere;
import toxi.geom.Vec2D;
import toxi.geom.Vec3D;

/**
 * Common interface for 3D (triangle) mesh containers.
 */
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
     * @return itself
     */
    public Mesh3D addFace(Vec3D a, Vec3D b, Vec3D c, Vec2D uvA, Vec2D uvB,
            Vec2D uvC);

    public Mesh3D addFace(Vec3D a, Vec3D b, Vec3D c, Vec3D n);

    public Mesh3D addFace(Vec3D a, Vec3D b, Vec3D c, Vec3D n, Vec2D uvA,
            Vec2D uvB, Vec2D uvC);

    public Mesh3D addMesh(Mesh3D mesh);

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
     * 
     * @return itself
     */
    public Mesh3D computeVertexNormals();

    /**
     * Changes the vertex order of faces such that their normal is facing away
     * from the mesh centroid.
     * 
     * @return itself
     */
    public Mesh3D faceOutwards();

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

    public Collection<Face> getFaces();

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

    public Collection<Vertex> getVertices();

    public Mesh3D init(String name, int numV, int numF);

    public Mesh3D setName(String name);
}
