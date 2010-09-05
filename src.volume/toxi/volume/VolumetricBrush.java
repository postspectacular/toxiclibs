package toxi.volume;

import java.util.logging.Logger;

import toxi.geom.Vec3D;
import toxi.math.MathUtils;

public abstract class VolumetricBrush {

    protected static final Logger logger =
            Logger.getLogger(VolumetricBrush.class.getName());

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
        float cx =
                MathUtils.clip((pos.x + volume.halfScale.x) / volume.scale.x
                        * volume.resX1, 0, volume.resX1);
        float cy =
                MathUtils.clip((pos.y + volume.halfScale.y) / volume.scale.y
                        * volume.resY1, 0, volume.resY1);
        float cz =
                MathUtils.clip((pos.z + volume.halfScale.z) / volume.scale.z
                        * volume.resZ1, 0, volume.resZ1);
        drawAtGridPos(cx, cy, cz, density);
    }

    public abstract void drawAtGridPos(float cx, float cy, float cz,
            float density);

    public void setMode(int mode) {
        brushMode = mode;
    }

    public abstract void setSize(float radius);

    protected final void updateVoxel(int x, int y, int z, float cellVal) {
        int idx = volume.getIndexFor(x, y, z);
        switch (brushMode) {
            case MODE_ADDITIVE:
            default:
                volume.setVoxelAt(idx, volume.getVoxelAt(idx) + cellVal);
                break;
            case MODE_MULTIPLY:
                volume.setVoxelAt(idx, volume.getVoxelAt(idx) * cellVal);
                break;
            case MODE_REPLACE:
                volume.setVoxelAt(idx, cellVal);
                break;
        }
    }
}