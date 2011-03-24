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

import java.util.HashMap;

import toxi.geom.Vec3D;

public class VolumetricHashMap extends VolumetricSpace {

    protected HashMap<Integer, Float> data;
    protected float density;

    public VolumetricHashMap(Vec3D scale, int resX, int resY, int resZ,
            float density) {
        super(scale, resX, resY, resZ);
        this.density = density;
        data = new HashMap<Integer, Float>((int) (resX * resY * resZ * density));
    }

    @Override
    public void clear() {
        data.clear();
        data = new HashMap<Integer, Float>((int) (resX * resY * resZ * density));
    }

    public void closeSides() {
        setVolumeSidesTo(0);
    }

    public HashMap<Integer, Float> getData() {
        return data;
    }

    public float getDensity() {
        return (float) data.size() / numCells;
    }

    @Override
    public final float getVoxelAt(int index) {
        Float voxel = data.get(index);
        if (voxel != null) {
            return voxel;
        }
        return 0;
    }

    public final float getVoxelAt(int x, int y, int z) {
        Float voxel = data.get(x + y * resX + z * sliceRes);
        if (voxel != null) {
            return voxel;
        }
        return 0;
    }

    public void setVolumeSidesTo(float density) {
        // close left/right & top/bottom
        for (int z = resZ1; z > 0; z--) {
            int sliceIdx = z * sliceRes;
            int sliceIdxAlt = sliceIdx + resY1 * resX;
            for (int x = 0; x < resX; x++) {
                data.put(sliceIdx + x, density);
                data.put(sliceIdxAlt + x, density);
            }
            for (int y = 1, rowIndex = sliceIdx + resX; y < resY1; y++) {
                data.put(rowIndex, density);
                data.put(rowIndex + resX1, density);
                rowIndex += resX;
            }
        }
        // front & back
        for (int i = 0, front = 0, back = resZ1 * sliceRes; i < sliceRes; i++) {
            data.put(front++, density);
            data.put(back++, density);
        }
    }

    public final void setVoxelAt(int index, float value) {
        if (index >= 0 && index < numCells) {
            data.put(index, value);
        }
    }

    public final void setVoxelAt(int x, int y, int z, float value) {
        int idx = x + y * resX + z * sliceRes;
        if (idx >= 0 && idx < numCells) {
            data.put(idx, value);
        }
    }
}
