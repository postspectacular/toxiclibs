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
            DataOutputStream ds =
                    new DataOutputStream(new FileOutputStream(fn));
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
}
