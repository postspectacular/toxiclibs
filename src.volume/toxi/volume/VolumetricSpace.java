package toxi.volume;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Logger;

import toxi.geom.Vec3D;
import toxi.math.MathUtils;

public class VolumetricSpace {
	protected static final Logger logger = Logger
			.getLogger(VolumetricSpace.class.getName());

	protected int resX, resY, resZ;
	protected int resX1, resY1, resZ1;

	protected int sliceRes;

	protected Vec3D scale;
	protected Vec3D halfScale;

	protected float[] data;
	protected int numCells;

	public VolumetricSpace(Vec3D scale, int resX, int resY, int resZ) {
		setScale(scale);
		this.resX = resX;
		this.resY = resY;
		this.resZ = resZ;
		resX1 = resX - 1;
		resY1 = resY - 1;
		resZ1 = resZ - 1;
		sliceRes = resX * resY;

		data = new float[resX * resY * resZ];
		numCells = data.length;
		logger.info("new space of " + resX + "x" + resY + "x" + resZ
				+ " cells: " + numCells);
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

	public int getIndexFor(int x, int y, int z) {
		x = MathUtils.clip(x, 0, resX1);
		y = MathUtils.clip(y, 0, resY1);
		z = MathUtils.clip(z, 0, resZ1);
		return x + y * resX + z * sliceRes;
	}

	public Vec3D getResolution() {
		return new Vec3D(resX, resY, resZ);
	}

	/**
	 * @return the scale
	 */
	public Vec3D getScale() {
		return scale.copy();
	}

	public float getVoxelAt(int x, int y, int z) {
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

	/**
	 * @param scale
	 *            the scale to set
	 */
	public void setScale(Vec3D scale) {
		this.scale = scale;
		this.halfScale = scale.scale(0.5f);
	}

	public void setVolumeSidesTo(float density) {
		int rx1 = resX - 1;
		int ry1 = resY - 1;
		// close left/right & top/bottom
		for (int z = resZ - 1; z > 0; z--) {
			int sliceIdx = z * sliceRes;
			int sliceIdxAlt = sliceIdx + ry1 * resX;
			for (int x = 0; x < resX; x++) {
				data[sliceIdx + x] = density;
				data[sliceIdxAlt + x] = density;
			}
			for (int y = 1, rowIndex = sliceIdx + resX; y < ry1; y++) {
				data[rowIndex] = density;
				data[rowIndex + rx1] = density;
				rowIndex += resX;
			}
		}
		// front & back
		for (int y = 0, front = 0, back = (resZ - 1) * sliceRes; y < resY; y++) {
			for (int x = 0; x < resX; x++) {
				data[front++] = density;
				data[back++] = density;
			}
		}
	}
}
