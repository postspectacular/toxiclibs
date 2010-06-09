package toxi.sim.erosion;

public class ThermalErosion {

    /**
     * Applies thermal erosion to the given elevation data. Based on the
     * description here: http://www.m3xbox.com/GPU_blog/?p=37
     * 
     * @param elevation
     *            height data
     * @param width
     * @param height
     * @return eroded version of the array
     */
    public static void erodeArray(float[] elevation, int width, int height) {
        float[] h = new float[9];
        float[] d = new float[9];
        int[] off =
                { -width - 1, -width, -width + 1, -1, 0, 1, width - 1, width,
                        width + 1 };
        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                int idx = y * width + x;
                float minD = Float.MAX_VALUE;
                float sumD = 0;
                int n = 0;
                for (int i = 0; i < 9; i++) {
                    h[i] = elevation[idx + off[i]];
                    d[i] = elevation[idx] - h[i];
                    if (d[i] > 0) {
                        if (d[i] < minD) {
                            minD = d[i];
                        }
                        sumD += d[i];
                        n++;
                    }
                }
                float hOut = n * minD / (n + 1.0f);
                elevation[idx] = elevation[idx] - hOut;
                for (int i = 0; i < 9; i++) {
                    if (d[i] > 0) {
                        elevation[idx + off[i]] = h[i] + hOut * (d[i] / sumD);
                    }
                }
            }
        }
    }

    /**
     * For each neighbour it’s computed the difference between the processed
     * cell and the neighbour:
     * 
     * d[i] = h - h[i];
     * 
     * the maximum positive difference is stored in d_max, and the sum of all
     * the positive differences that are bigger than T (this numer is n), the
     * talus angle, is stored in d_tot.
     * 
     * Now it’s possible to update all the n cells (where d[i] is bigger than T)
     * using this formula:
     * 
     * h[i] = h[i] + c * (d_max - T) * (d[i] / d_tot);
     * 
     * and the main cell with this other formula:
     * 
     * h = h - (d_max - (n * d_max * T / d_tot));
     * 
     * The Talus angle T is a threshold that determines which slopes are
     * affected by the erosion, instead the c constant determines how much
     * material is eroded.
     * 
     * @param elevation
     * @param width
     * @param height
     * @return
     */
    public static void erodeWithTalusAngle(float[] elevation, int width,
            int height, float T, float c) {
        float[] d = new float[9];
        float[] h = new float[9];
        int[] off =
                { -width - 1, -width, -width + 1, -1, 0, 1, width - 1, width,
                        width + 1 };
        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                int idx = y * width + x;
                float maxD = T;
                float sumD = 0;
                int n = 0;
                for (int i = 0; i < 9; i++) {
                    h[i] = elevation[idx + off[i]];
                    d[i] = elevation[idx] - h[i];
                    if (d[i] > T) {
                        sumD += d[i];
                        n++;
                        if (d[i] > maxD) {
                            maxD = d[i];
                        }
                    }
                }
                if (sumD > 0) {
                    elevation[idx] -= (maxD - (n * maxD * T / sumD));
                    for (int i = 0; i < 9; i++) {
                        if (d[i] > T) {
                            elevation[idx + off[i]] =
                                    h[i] + c * (maxD - T) * (d[i] / sumD);
                        }
                    }
                }
            }
        }
    }
}
