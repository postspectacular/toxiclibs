package toxi.geom.volume;

import toxi.geom.Vec3D;
import toxi.math.MathUtils;

public abstract class VolumetricBrush {

	public static final int MODE_ADDITIVE = 1;
	public static final int MODE_MULTIPLY = 2;
	public static final int MODE_REPLACE = 3;

	protected VolumetricSpace volume;
	protected int cellRadiusX, cellRadiusY, cellRadiusZ;
	protected float stretchY, stretchZ;

	protected int brushMode = MODE_ADDITIVE;

	public VolumetricBrush(VolumetricSpace volume) {
		this.volume = volume;
	}

	public void drawAtAbsolutePos(Vec3D pos, float density) {
		float cx = MathUtils.clip((pos.x + volume.halfScale.x) / volume.scale.x
				* volume.resX, 0, volume.resX - 1);
		float cy = MathUtils.clip((pos.y + volume.halfScale.y) / volume.scale.y
				* volume.resY, 0, volume.resY - 1);
		float cz = MathUtils.clip((pos.z + volume.halfScale.z) / volume.scale.z
				* volume.resZ, 0, volume.resZ - 1);
		drawAtGridPos(cx, cy, cz, density);
	}

	public abstract void drawAtGridPos(float cx, float cy, float cz,
			float density);

	public void setMode(int mode) {
		brushMode = mode;
	}

	public abstract void setSize(float radius);
}