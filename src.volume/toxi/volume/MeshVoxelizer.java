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

import toxi.geom.AABB;
import toxi.geom.Triangle3D;
import toxi.geom.Vec3D;
import toxi.geom.mesh.Face;
import toxi.geom.mesh.Mesh3D;
import toxi.math.MathUtils;
import toxi.math.ScaleMap;

public class MeshVoxelizer {

    protected VolumetricSpace volume;
    protected int wallThickness = 0;

    public MeshVoxelizer(int res) {
        this(res, res, res);
    }

    public MeshVoxelizer(int resX, int resY, int resZ) {
        volume = new VolumetricHashMap(new Vec3D(1, 1, 1), resX, resY, resZ,
                0.1f);
    }

    public MeshVoxelizer clear() {
        volume.clear();
        return this;
    }

    /**
     * @return the volume
     */
    public VolumetricSpace getVolume() {
        return volume;
    }

    /**
     * @return the wallThickness
     */
    public int getWallThickness() {
        return wallThickness;
    }

    protected void setVoxelAt(int x, int y, int z, float iso) {
        int mix = MathUtils.max(x - wallThickness, 0);
        int miy = MathUtils.max(y - wallThickness, 0);
        int miz = MathUtils.max(z - wallThickness, 0);
        int max = MathUtils.min(x + wallThickness, volume.resX1);
        int may = MathUtils.min(y + wallThickness, volume.resY1);
        int maz = MathUtils.min(z + wallThickness, volume.resZ1);
        for (z = miz; z <= maz; z++) {
            for (y = miy; y <= may; y++) {
                for (x = mix; x <= max; x++) {
                    volume.setVoxelAt(x, y, z, iso);
                }
            }
        }
    }

    /**
     * @param wallThickness
     *            the wallThickness to set
     */
    public MeshVoxelizer setWallThickness(int wallThickness) {
        this.wallThickness = wallThickness;
        return this;
    }

    private VolumetricSpace solidifyVolume(VolumetricSpaceArray volume) {
        for (int z = 0; z < volume.resZ; z++) {
            for (int y = 0; y < volume.resY; y++) {
                boolean isFilled = false;
                int startX = 0;
                for (int x = 0; x < volume.resX; x++) {
                    float val = volume.getVoxelAt(x, y, z);
                    if (val > 0) {
                        if (!isFilled) {
                            startX = x;
                            isFilled = true;
                        } else {
                            for (int i = startX; i <= x; i++) {
                                volume.setVoxelAt(i, y, z, 1);
                            }
                            isFilled = false;
                        }
                    }
                }
            }
        }
        return volume;
    }

    public VolumetricSpace voxelizeMesh(Mesh3D mesh) {
        return voxelizeMesh(mesh, 1f);
    }

    public VolumetricSpace voxelizeMesh(Mesh3D mesh, float iso) {
        AABB box = mesh.getBoundingBox();
        Vec3D bmin = box.getMin();
        Vec3D bmax = box.getMax();
        ScaleMap wx = new ScaleMap(bmin.x, bmax.x, 1, volume.resX - 2);
        ScaleMap wy = new ScaleMap(bmin.y, bmax.y, 1, volume.resY - 2);
        ScaleMap wz = new ScaleMap(bmin.z, bmax.z, 1, volume.resZ - 2);
        ScaleMap gx = new ScaleMap(1, volume.resX - 2, bmin.x, bmax.x);
        ScaleMap gy = new ScaleMap(1, volume.resY - 2, bmin.y, bmax.y);
        ScaleMap gz = new ScaleMap(1, volume.resZ - 2, bmin.z, bmax.z);
        volume.setScale(box.getExtent().scale(2f));
        Triangle3D tri = new Triangle3D();
        AABB voxel = new AABB(new Vec3D(), volume.voxelSize.scale(0.5f));
        for (Face f : mesh.getFaces()) {
            tri.a = f.a;
            tri.b = f.b;
            tri.c = f.c;
            AABB bounds = tri.getBoundingBox();
            Vec3D min = bounds.getMin();
            Vec3D max = bounds.getMax();
            min = new Vec3D((int) wx.getClippedValueFor(min.x),
                    (int) wy.getClippedValueFor(min.y),
                    (int) wz.getClippedValueFor(min.z));
            max = new Vec3D((int) wx.getClippedValueFor(max.x),
                    (int) wy.getClippedValueFor(max.y),
                    (int) wz.getClippedValueFor(max.z));
            for (int z = (int) min.z; z <= max.z; z++) {
                for (int y = (int) min.y; y <= max.y; y++) {
                    for (int x = (int) min.x; x <= max.x; x++) {
                        if (x < volume.resX1 && y < volume.resY1
                                && z < volume.resZ1) {
                            voxel.set((float) gx.getClippedValueFor(x),
                                    (float) gy.getClippedValueFor(y),
                                    (float) gz.getClippedValueFor(z));
                            if (voxel.intersectsTriangle(tri)) {
                                setVoxelAt(x, y, z, iso);
                            }
                        }
                    }
                }
            }
        }
        return volume;
    }
}
