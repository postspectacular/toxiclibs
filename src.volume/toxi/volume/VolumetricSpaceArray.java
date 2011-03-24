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

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import toxi.geom.Vec3D;

public class VolumetricSpaceArray extends VolumetricSpace {

    protected float[] data;

    public VolumetricSpaceArray(Vec3D scale, int resX, int resY, int resZ) {
        super(scale, resX, resY, resZ);
        data = new float[resX * resY * resZ];
    }

    public VolumetricSpaceArray(Vec3D scale, int resX, int resY, int resZ,
            float[] data) {
        super(scale, resX, resY, resZ);
        setData(data);
    }

    @Override
    public void clear() {
        for (int i = 0; i < data.length; i++) {
            data[i] = 0;
        }
    }

    public void closeSides() {
        setVolumeSidesTo(0);
    }

    public float[] getData() {
        return data;
    }

    @Override
    public final float getVoxelAt(int index) {
        return data[index];
    }

    public final float getVoxelAt(int x, int y, int z) {
        return data[x + y * resX + z * sliceRes];
    }

    /**
     * Saves volume data float array in raw binary format.
     * 
     * @param fn
     *            absolute path/filename to save to
     */
    public void saveData(String fn) {
        logger.info("saving volume data...");
        try {
            DataOutputStream ds = new DataOutputStream(new FileOutputStream(fn));
            // ds.writeInt(volumeData.length);
            for (float element : data) {
                ds.writeFloat(element);
            }
            ds.flush();
            ds.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setData(float[] data) {
        if (data != null) {
            if (this.data == null || this.data.length == data.length) {
                this.data = data;
            } else {
                throw new IllegalArgumentException(
                        "new volume data must have same resolution as current data");
            }
        } else {
            throw new IllegalArgumentException(
                    "given data array must not be null");
        }
    }

    public void setVolumeSidesTo(float density) {
        // close left/right & top/bottom
        for (int z = resZ1; z > 0; z--) {
            int sliceIdx = z * sliceRes;
            int sliceIdxAlt = sliceIdx + resY1 * resX;
            for (int x = 0; x < resX; x++) {
                data[sliceIdx + x] = density;
                data[sliceIdxAlt + x] = density;
            }
            for (int y = 1, rowIndex = sliceIdx + resX; y < resY1; y++) {
                data[rowIndex] = density;
                data[rowIndex + resX1] = density;
                rowIndex += resX;
            }
        }
        // front & back
        for (int i = 0, front = 0, back = resZ1 * sliceRes; i < sliceRes; i++) {
            data[front++] = density;
            data[back++] = density;
        }
    }

    @Override
    public final void setVoxelAt(int index, float value) {
        if (index >= 0 && index < data.length) {
            data[index] = value;
        }
    }

    public final void setVoxelAt(int x, int y, int z, float value) {
        int idx = x + y * resX + z * sliceRes;
        if (idx >= 0 && idx < data.length) {
            data[idx] = value;
        }
    }
}
