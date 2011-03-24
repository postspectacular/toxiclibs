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

package toxi.volume;

import java.util.logging.Logger;

import toxi.geom.Vec3D;
import toxi.geom.mesh.Mesh3D;
import toxi.geom.mesh.TriangleMesh;

/**
 * IsoSurface class based on C version by Paul Bourke and Lingo version by
 * myself.
 */
public class ArrayIsoSurface implements IsoSurface {

    protected static final Logger logger = Logger
            .getLogger(ArrayIsoSurface.class.getName());

    protected Vec3D cellSize;
    protected Vec3D centreOffset;
    protected VolumetricSpace volume;

    public float isoValue;

    protected int resX, resY, resZ;
    protected int resX1, resY1, resZ1;

    protected int sliceRes;
    protected int nextXY;

    protected Vec3D[] edgeVertices;

    public ArrayIsoSurface(VolumetricSpace volume) {
        this.volume = volume;
        cellSize = new Vec3D(volume.scale.x / volume.resX1, volume.scale.y
                / volume.resY1, volume.scale.z / volume.resZ1);

        resX = volume.resX;
        resY = volume.resY;
        resZ = volume.resZ;
        resX1 = volume.resX1;
        resY1 = volume.resY1;
        resZ1 = volume.resZ1;

        sliceRes = volume.sliceRes;
        nextXY = resX + sliceRes;

        centreOffset = volume.halfScale.getInverted();

        edgeVertices = new Vec3D[3 * volume.numCells];
    }

    /**
     * Computes the surface mesh for the given volumetric data and iso value.
     */
    public Mesh3D computeSurfaceMesh(Mesh3D mesh, final float iso) {
        if (mesh == null) {
            mesh = new TriangleMesh("isosurface-" + iso);
        } else {
            mesh.clear();
        }
        isoValue = iso;
        float offsetZ = centreOffset.z;
        for (int z = 0; z < resZ1; z++) {
            int sliceOffset = sliceRes * z;
            float offsetY = centreOffset.y;
            for (int y = 0; y < resY1; y++) {
                float offsetX = centreOffset.x;
                int offset = resX * y + sliceOffset;
                for (int x = 0; x < resX1; x++) {
                    final int cellIndex = getCellIndex(x, y, z);
                    if (cellIndex > 0 && cellIndex < 255) {
                        final int edgeFlags = MarchingCubesIndex.edgesToCompute[cellIndex];
                        if (edgeFlags > 0 && edgeFlags < 255) {
                            int edgeOffsetIndex = offset * 3;
                            float offsetData = volume.getVoxelAt(offset);
                            float isoDiff = isoValue - offsetData;
                            if ((edgeFlags & 1) > 0) {
                                if (edgeVertices[edgeOffsetIndex] == null) {
                                    float t = isoDiff
                                            / (volume.getVoxelAt(offset + 1) - offsetData);
                                    edgeVertices[edgeOffsetIndex] = new Vec3D(
                                            offsetX + t * cellSize.x, y
                                                    * cellSize.y
                                                    + centreOffset.y, z
                                                    * cellSize.z
                                                    + centreOffset.z);
                                }
                            }
                            if ((edgeFlags & 2) > 0) {
                                if (edgeVertices[edgeOffsetIndex + 1] == null) {
                                    float t = isoDiff
                                            / (volume.getVoxelAt(offset + resX) - offsetData);
                                    edgeVertices[edgeOffsetIndex + 1] = new Vec3D(
                                            x * cellSize.x + centreOffset.x,
                                            offsetY + t * cellSize.y, z
                                                    * cellSize.z
                                                    + centreOffset.z);
                                }
                            }
                            if ((edgeFlags & 4) > 0) {
                                if (edgeVertices[edgeOffsetIndex + 2] == null) {
                                    float t = isoDiff
                                            / (volume.getVoxelAt(offset
                                                    + sliceRes) - offsetData);
                                    edgeVertices[edgeOffsetIndex + 2] = new Vec3D(
                                            x * cellSize.x + centreOffset.x, y
                                                    * cellSize.y
                                                    + centreOffset.y, offsetZ
                                                    + t * cellSize.z);
                                }
                            }
                        }
                    }
                    offsetX += cellSize.x;
                    offset++;
                }
                offsetY += cellSize.y;
            }
            offsetZ += cellSize.z;
        }

        final int[] face = new int[16];
        for (int z = 0; z < resZ1; z++) {
            int sliceOffset = sliceRes * z;
            for (int y = 0; y < resY1; y++) {
                int offset = resX * y + sliceOffset;
                for (int x = 0; x < resX1; x++) {
                    final int cellIndex = getCellIndex(x, y, z);
                    if (cellIndex > 0 && cellIndex < 255) {
                        int n = 0;
                        int edgeIndex;
                        final int[] cellTriangles = MarchingCubesIndex.cellTriangles[cellIndex];
                        while ((edgeIndex = cellTriangles[n]) != -1) {
                            int[] edgeOffsetInfo = MarchingCubesIndex.edgeOffsets[edgeIndex];
                            face[n] = ((x + edgeOffsetInfo[0]) + resX
                                    * (y + edgeOffsetInfo[1]) + sliceRes
                                    * (z + edgeOffsetInfo[2]))
                                    * 3 + edgeOffsetInfo[3];
                            n++;
                        }
                        for (int i = 0; i < n; i += 3) {
                            final Vec3D va = edgeVertices[face[i + 1]];
                            final Vec3D vb = edgeVertices[face[i + 2]];
                            final Vec3D vc = edgeVertices[face[i]];
                            if (va != null && vb != null && vc != null) {
                                mesh.addFace(va, vb, vc);
                            }
                        }
                    }
                    offset++;
                }
            }
        }
        return mesh;
    }

    protected final int getCellIndex(int x, int y, int z) {
        int cellIndex = 0;
        int idx = x + y * resX + z * sliceRes;
        if (volume.getVoxelAt(idx) < isoValue) {
            cellIndex |= 0x01;
        }
        if (volume.getVoxelAt(idx + sliceRes) < isoValue) {
            cellIndex |= 0x08;
        }
        if (volume.getVoxelAt(idx + resX) < isoValue) {
            cellIndex |= 0x10;
        }
        if (volume.getVoxelAt(idx + resX + sliceRes) < isoValue) {
            cellIndex |= 0x80;
        }
        idx++;
        if (volume.getVoxelAt(idx) < isoValue) {
            cellIndex |= 0x02;
        }
        if (volume.getVoxelAt(idx + sliceRes) < isoValue) {
            cellIndex |= 0x04;
        }
        if (volume.getVoxelAt(idx + resX) < isoValue) {
            cellIndex |= 0x20;
        }
        if (volume.getVoxelAt(idx + resX + sliceRes) < isoValue) {
            cellIndex |= 0x40;
        }
        return cellIndex;
    }

    /**
     * Resets mesh vertices to default positions and clears face index. Needs to
     * be called inbetween successive calls to
     * {@link #computeSurfaceMesh(Mesh3D, float)}
     */
    public void reset() {
        for (int i = 0; i < edgeVertices.length; i++) {
            edgeVertices[i] = null;
        }
    }
}
