package toxi.volume;

import java.util.logging.Logger;

import toxi.geom.Vec3D;
import toxi.math.MathUtils;

public abstract class VolumetricSpace {

    protected static final Logger logger = Logger
            .getLogger(VolumetricSpace.class.getName());

    public final int resX, resY, resZ;
    public final int resX1, resY1, resZ1;

    public final int sliceRes;

    public final Vec3D scale = new Vec3D();
    public final Vec3D halfScale = new Vec3D();

    public final int numCells;

    public VolumetricSpace(Vec3D scale, int resX, int resY, int resZ) {
        setScale(scale);
        this.resX = resX;
        this.resY = resY;
        this.resZ = resZ;
        resX1 = resX - 1;
        resY1 = resY - 1;
        resZ1 = resZ - 1;
        sliceRes = resX * resY;
        numCells = sliceRes * resZ;
        logger.info("new space of " + resX + "x" + resY + "x" + resZ
                + " cells: " + numCells);
    }

    public final int getIndexFor(int x, int y, int z) {
        x = MathUtils.clip(x, 0, resX1);
        y = MathUtils.clip(y, 0, resY1);
        z = MathUtils.clip(z, 0, resZ1);
        return x + y * resX + z * sliceRes;
    }

    public final Vec3D getResolution() {
        return new Vec3D(resX, resY, resZ);
    }

    /**
     * @return the scale
     */
    public final Vec3D getScale() {
        return scale.copy();
    }

    public abstract float getVoxelAt(int index);

    public abstract float getVoxelAt(int x, int y, int z);

    /**
     * @param scale
     *            the scale to set
     */
    public final void setScale(Vec3D scale) {
        this.scale.set(scale);
        this.halfScale.set(scale.scale(0.5f));
    }
}
