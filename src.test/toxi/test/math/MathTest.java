package toxi.test.math;

import junit.framework.TestCase;
import toxi.math.MathUtils;

public class MathTest extends TestCase {

    public void testFastSin() {
        float maxErr = Float.MIN_VALUE;
        for (float i = 0; i <= 8 * 360; i++) {
            float theta = i * MathUtils.DEG2RAD * 0.25f;
            float sin = (float) Math.cos(theta);
            float fs = MathUtils.cos(theta);
            float err = (fs - sin);
            maxErr = MathUtils.max(MathUtils.abs(err), maxErr);
            System.out.println(i + ": sin=" + sin + " fastsin=" + fs + " err="
                    + err);
        }
        System.out.println("max err: " + maxErr);
    }
}
