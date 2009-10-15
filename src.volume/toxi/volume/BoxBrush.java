package toxi.volume;

import java.util.logging.Logger;

import toxi.math.MathUtils;

/**
 *
 */
public class BoxBrush extends VolumetricBrush {

	protected static final Logger logger = Logger.getLogger(BoxBrush.class
			.getName());

	public BoxBrush(VolumetricSpace volume, float size) {
		super(volume);
		setSize(size);
	}

	@Override
	public void drawAtGridPos(float cx, float cy, float cz, float density) {
		int minX = MathUtils.max((int) (cx - cellRadiusX), 0);
		int minY = MathUtils.max((int) (cy - cellRadiusY), 0);
		int minZ = MathUtils.max((int) (cz - cellRadiusZ), 0);
		int maxX = MathUtils.min((int) (cx + cellRadiusX), volume.resX);
		int maxY = MathUtils.min((int) (cy + cellRadiusY), volume.resY);
		int maxZ = MathUtils.min((int) (cz + cellRadiusZ), volume.resZ);
		for (int z = minZ; z < maxZ; z++) {
			for (int y = minY; y < maxY; y++) {
				for (int x = minX; x < maxX; x++) {
					int idx = volume.getIndexFor(x, y, z);
					switch (brushMode) {
					case MODE_ADDITIVE:
					default:
						volume.data[idx] += density;
						break;
					case MODE_MULTIPLY:
						volume.data[idx] *= density;
						break;
					case MODE_REPLACE:
						volume.data[idx] = density;
						break;
					}
				}
			}
		}
	}

	@Override
	public void setSize(float size) {
		setSize(size, size, size);
	}

	public void setSize(float sizeX, float sizeY, float sizeZ) {
		this.cellRadiusX = (int) (sizeX * 0.5f / volume.scale.x * volume.resX + 1);
		this.cellRadiusY = (int) (sizeY * 0.5f / volume.scale.y * volume.resY + 1);
		this.cellRadiusZ = (int) (sizeZ * 0.5f / volume.scale.z * volume.resZ + 1);
		stretchY = (float) cellRadiusX / cellRadiusY;
		stretchZ = (float) cellRadiusX / cellRadiusZ;
		logger.fine("new brush size: " + cellRadiusX + "x" + cellRadiusY + "x"
				+ cellRadiusZ);
	}

}
