package toxi.sim.erosion;

/**
 * Applies thermal erosion to the given elevation data. Based on the description
 * here: http://www.m3xbox.com/GPU_blog/?p=37
 */
public class ThermalErosion extends ErosionFunction {

    public void erodeAt(int x, int y) {
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
