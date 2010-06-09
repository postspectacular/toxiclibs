package toxi.sim.erosion;

/**
 * Abstract parent class for various 2D erosion simulations, implemented as
 * sub-classes.
 */
public abstract class ErosionFunction {

    protected float[] elevation;
    protected int width;
    protected int height;

    protected float[] d = new float[9];
    protected float[] h = new float[9];
    protected int[] off;

    /**
     * Destructively erodes the given array.
     * 
     * @param elevation
     * @param width
     * @param height
     */
    public void erode(float[] elevation, int width, int height) {
        this.elevation = elevation;
        this.width = width;
        this.height = height;
        off =
                new int[] { -width - 1, -width, -width + 1, -1, 0, 1,
                        width - 1, width, width + 1 };
        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                erodeAt(x, y);
            }
        }
    }

    public abstract void erodeAt(int x, int y);
}
