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

import toxi.geom.IsectDataD3D;
import toxi.geom.RayD3D;
import toxi.geom.TriangleD3D;
import toxi.geom.TriangleDIntersector;
import toxi.geom.VecD2D;
import toxi.geom.VecD3D;
import toxi.math.InterpolationD2D;
import toxi.math.MathUtils;

/**
 * Implementation of a 2D grid based heightfield with basic intersection
 * features and conversion to {@link TriangleMesh}. The terrain is always
 * located in the XZ plane with the positive Y axis as up vector.
 */
public class TerrainD {

    protected double[] elevation;
    protected VecD3D[] vertices;

    protected int width;

    protected int depth;
    protected VecD2D scale;

    /**
     * Constructs a new and initially flat terrain of the given size in the XZ
     * plane, centred around the world origin.
     * 
     * @param width
     * @param depth
     * @param scale
     */
    public TerrainD(int width, int depth, double scale) {
        this(width, depth, new VecD2D(scale, scale));
    }

    public TerrainD(int width, int depth, VecD2D scale) {
        this.width = width;
        this.depth = depth;
        this.scale = scale;
        this.elevation = new double[width * depth];
        this.vertices = new VecD3D[elevation.length];
        VecD3D offset = new VecD3D(width / 2, 0, depth / 2);
        VecD3D scaleXZ = scale.toD3DXZ();
        for (int z = 0, i = 0; z < depth; z++) {
            for (int x = 0; x < width; x++) {
                vertices[i++] = new VecD3D(x, 0, z).subSelf(offset).scaleSelf(
                        scaleXZ);
            }
        }
    }

    public TerrainD clear() {
        for (int i = 0; i < elevation.length; i++) {
            elevation[i] = 0;
        }
        return updateElevation();
    }

    /**
     * @return number of grid cells along the Z axis.
     */
    public int getDepth() {
        return depth;
    }

    public double[] getElevation() {
        return elevation;
    }

    /**
     * @param x
     * @param z
     * @return the elevation at grid point
     */
    public double getHeightAtCell(int x, int z) {
        return elevation[getIndex(x, z)];
    }

    /**
     * Computes the elevation of the terrain at the given 2D world coordinate
     * (based on current terrain scale).
     * 
     * @param x
     *            scaled world coord x
     * @param z
     *            scaled world coord z
     * @return interpolated elevation
     */
    public double getHeightAtPoint(double x, double z) {
        double xx = x / scale.x + width * 0.5f;
        double zz = z / scale.y + depth * 0.5f;
        double y = 0;
        if (xx >= 0 && xx < width && zz >= 0 && zz < depth) {
            int x2 = (int) MathUtils.min(xx + 1, width - 1);
            int z2 = (int) MathUtils.min(zz + 1, depth - 1);
            double a = getHeightAtCell((int) xx, (int) zz);
            double b = getHeightAtCell(x2, (int) zz);
            double c = getHeightAtCell((int) xx, z2);
            double d = getHeightAtCell(x2, z2);
            y = InterpolationD2D.bilinear(xx, zz, (int) xx, (int) zz, x2, z2, a,
                    b, c, d);
        }
        return y;
    }

    /**
     * Computes the array index for the given cell coords & checks if they're in
     * bounds. If not an {@link IndexOutOfBoundsException} is thrown.
     * 
     * @param x
     * @param z
     * @return array index
     */
    protected final int getIndex(int x, int z) {
        int idx = z * width + x;
        if (idx < 0 || idx > elevation.length) {
            throw new IndexOutOfBoundsException(
                    "the given terrain cell is invalid: " + x + ";" + z);
        }
        return idx;
    }

    /**
     * @return the scale
     */
    public VecD2D getScale() {
        return scale;
    }

    protected VecD3D getVertexAtCell(int x, int z) {
        return vertices[getIndex(x, z)];
    }

    /**
     * @return number of grid cells along the X axis.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Computes the 3D position (with elevation) and normal vector at the given
     * 2D location in the terrain. The position is in scaled world coordinates
     * based on the given terrain scale. The returned data is encapsulated in a
     * {@link toxi.geom.IsectData3D} instance.
     * 
     * @param x
     * @param z
     * @return intersection data parcel
     */
    public IsectDataD3D intersectAtPoint(double x, double z) {
        double xx = x / scale.x + width * 0.5f;
        double zz = z / scale.y + depth * 0.5f;
        IsectDataD3D isec = new IsectDataD3D();
        if (xx >= 0 && xx < width && zz >= 0 && zz < depth) {
            int x2 = (int) MathUtils.min(xx + 1, width - 1);
            int z2 = (int) MathUtils.min(zz + 1, depth - 1);
            VecD3D a = getVertexAtCell((int) xx, (int) zz);
            VecD3D b = getVertexAtCell(x2, (int) zz);
            VecD3D c = getVertexAtCell(x2, z2);
            VecD3D d = getVertexAtCell((int) xx, z2);
            RayD3D r = new RayD3D(new VecD3D(x, 10000, z), new VecD3D(0, -1, 0));
            TriangleDIntersector i = new TriangleDIntersector(new TriangleD3D(a,
                    b, d));
            if (i.intersectsRayD(r)) {
                isec = i.getIntersectionDataD();
            } else {
                i.setTriangleD(new TriangleD3D(b, c, d));
                i.intersectsRayD(r);
                isec = i.getIntersectionDataD();
            }
        }
        return isec;
    }

    /**
     * Sets the elevation of all cells to those of the given array values.
     * 
     * @param elevation
     *            array of height values
     * @return itself
     */
    public TerrainD setElevation(double[] elevation) {
        if (this.elevation.length == elevation.length) {
            this.elevation = elevation;
            updateElevation();
        } else {
            throw new IllegalArgumentException(
                    "the given elevation array size does not match existing terrain size");
        }
        return this;
    }

    /**
     * Sets the elevation for a single given grid cell.
     * 
     * @param x
     * @param z
     * @param h
     *            new elevation value
     * @return itself
     */
    public TerrainD setHeightAtCell(int x, int z, double h) {
        int index = getIndex(x, z);
        elevation[index] = h;
        vertices[index].y = h;
        return this;
    }

    public void setScale(double scale) {
        setScale(new VecD2D(scale, scale));
    }

    /**
     * @param scale
     *            the scale to set
     */
    public void setScale(VecD2D scale) {
        this.scale.set(scale);
        VecD3D offset = new VecD3D(width / 2, 0, depth / 2);
        for (int z = 0, i = 0; z < depth; z++) {
            for (int x = 0; x < width; x++, i++) {
                vertices[i].set((x - offset.x) * scale.x, vertices[i].y,
                        (z - offset.z) * scale.y);
            }
        }
    }

    public MeshD3D toMesh() {
        return toMesh(null);
    }

    public MeshD3D toMesh(double groundLevel) {
        return toMeshD(null, groundLevel);
    }

    /**
     * Creates a {@link TriangleMesh} instance of the terrain surface or adds
     * its geometry to an existing mesh.
     * 
     * @param mesh
     * @return mesh instance
     */
    public MeshD3D toMesh(MeshD3D mesh) {
        return toMeshD(mesh, 0, 0, width, depth);
    }

    /**
     * Creates a {@link TriangleMesh} instance of the terrain and constructs
     * side panels and a bottom plane to form a fully enclosed mesh volume, e.g.
     * suitable for CNC fabrication or 3D printing. The bottom plane will be
     * created at the given ground level (can also be negative) and the sides
     * are extended downward to that level too.
     * 
     * @param mesh
     *            existing mesh or null
     * @param groundLevel
     * @return mesh
     */
    public MeshD3D toMeshD(MeshD3D mesh, double groundLevel) {
        return toMeshD(mesh, 0, 0, width, depth, groundLevel);
    }

    public MeshD3D toMeshD(MeshD3D mesh, int minX, int minZ, int maxX, int maxZ) {
        if (mesh == null) {
            mesh = new TriangleMeshD("terrain", vertices.length,
                    vertices.length * 2);
        }
        minX = MathUtils.clip(minX, 0, width - 1);
        maxX = MathUtils.clip(maxX, 0, width);
        minZ = MathUtils.clip(minZ, 0, depth - 1);
        maxZ = MathUtils.clip(maxZ, 0, depth);
        minX++;
        minZ++;
        for (int z = minZ, idx = minX * width; z < maxZ; z++, idx += width) {
            for (int x = minX; x < maxX; x++) {
                mesh.addFaceD(vertices[idx - width + x - 1], vertices[idx
                        - width + x], vertices[idx + x - 1]);
                mesh.addFaceD(vertices[idx - width + x], vertices[idx + x],
                        vertices[idx + x - 1]);
            }
        }
        return mesh;
    }

    public MeshD3D toMeshD(MeshD3D mesh, int mix, int miz, int mxx, int mxz,
            double groundLevel) {
        mesh = toMeshD(mesh, mix, miz, mxx, mxz);
        mix = MathUtils.clip(mix, 0, width - 1);
        mxx = MathUtils.clip(mxx, 0, width);
        miz = MathUtils.clip(miz, 0, depth - 1);
        mxz = MathUtils.clip(mxz, 0, depth);
        VecD3D offset = new VecD3D(width, 0, depth).scaleSelf(0.5f);
        double minX = (mix - offset.x) * scale.x;
        double minZ = (miz - offset.z) * scale.y;
        double maxX = (mxx - offset.x) * scale.x;
        double maxZ = (mxz - offset.z) * scale.y;
        for (int z = miz + 1; z < mxz; z++) {
            VecD3D a = new VecD3D(minX, groundLevel, (z - 1 - offset.z) * scale.y);
            VecD3D b = new VecD3D(minX, groundLevel, (z - offset.z) * scale.y);
            // left
            mesh.addFaceD(getVertexAtCell(mix, z - 1), getVertexAtCell(mix, z),
                    a);
            mesh.addFaceD(getVertexAtCell(mix, z), b, a);
            // right
            a.x = b.x = maxX - scale.x;
            mesh.addFaceD(getVertexAtCell(mxx - 1, z),
                    getVertexAtCell(mxx - 1, z - 1), b);
            mesh.addFaceD(getVertexAtCell(mxx - 1, z - 1), a, b);
        }
        for (int x = mix + 1; x < mxx; x++) {
            VecD3D a = new VecD3D((x - 1 - offset.x) * scale.x, groundLevel, minZ);
            VecD3D b = new VecD3D((x - offset.x) * scale.x, groundLevel, minZ);
            // back
            mesh.addFaceD(getVertexAtCell(x, miz), getVertexAtCell(x - 1, miz),
                    b);
            mesh.addFaceD(getVertexAtCell(x - 1, miz), a, b);
            // front
            a.z = b.z = maxZ - scale.y;
            mesh.addFaceD(getVertexAtCell(x - 1, mxz - 1),
                    getVertexAtCell(x, mxz - 1), a);
            mesh.addFaceD(getVertexAtCell(x, mxz - 1), b, a);
        }
        // bottom plane
        for (int z = miz + 1; z < mxz; z++) {
            for (int x = mix + 1; x < mxx; x++) {
                VecD3D a = getVertexAtCell(x - 1, z - 1).copy();
                VecD3D b = getVertexAtCell(x, z - 1).copy();
                VecD3D c = getVertexAtCell(x - 1, z).copy();
                VecD3D d = getVertexAtCell(x, z).copy();
                a.y = groundLevel;
                b.y = groundLevel;
                c.y = groundLevel;
                d.y = groundLevel;
                mesh.addFaceD(a, c, d);
                mesh.addFaceD(a, d, b);
            }
        }
        return mesh;
    }

    public TerrainD updateElevation() {
        for (int i = 0; i < elevation.length; i++) {
            vertices[i].y = elevation[i];
        }
        return this;
    }
}
