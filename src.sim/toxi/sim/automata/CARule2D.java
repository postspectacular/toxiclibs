package toxi.sim.automata;

public class CARule2D extends AbstractCARule {

    public CARule2D(byte[] s, byte[] b, int st) {
        surviveRules = new boolean[9];
        birthRules = new boolean[9];
        for (int i = 0; i < s.length; i++) {
            surviveRules[s[i]] = true;
        }
        for (int i = 0; i < b.length; i++) {
            birthRules[b[i]] = true;
        }
        stateCount = st;
    }

    public void apply(CAMatrix m) {
        int width = m.getWidth();
        int height = m.getHeight();
        int[] matrix = m.getMatrix();
        int[] temp = m.getTempBuffer();
        for (int y = 0; y < height; y++) {
            // determine up and down cells
            int up = ((y > 0) ? y - 1 : height - 1) * width;
            int down = ((y < height - 1) ? y + 1 : 0) * width;
            int centre = y * width;
            for (int x = 0; x < width; x++) {
                // determine left and right cells
                int left = (x > 0) ? x - 1 : width - 1;
                int right = (x < width - 1) ? x + 1 : 0;
                int currVal = matrix[centre + x];
                int newVal = currVal;
                int sum = 0;
                if (currVal > 1) {
                    if (currVal < stateCount - 1) {
                        newVal++;
                    } else {
                        newVal = 0;
                    }
                } else {
                    if (matrix[up + left] == 1) {
                        sum++; // top left
                    }
                    if (matrix[up + x] == 1) {
                        sum++; // top
                    }
                    if (matrix[up + right] == 1) {
                        sum++; // top right
                    }
                    if (matrix[centre + left] == 1) {
                        sum++; // left
                    }
                    if (matrix[centre + right] == 1) {
                        ++sum; // right
                    }
                    if (matrix[down + left] == 1) {
                        sum++; // bottom left
                    }
                    if (matrix[down + x] == 1) {
                        sum++; // bottom
                    }
                    if (matrix[down + right] == 1) {
                        sum++; // bottom right
                    }
                    if (currVal != 0) { // centre
                        if (surviveRules[sum]) {
                            newVal = 1;
                        } else if (currVal < stateCount - 1) {
                            newVal++;
                        } else {
                            newVal = 0;
                        }
                    } else {
                        if (birthRules[sum]) {
                            newVal = 1;
                        }
                    }
                }
                temp[centre + x] = newVal;
            }
        }
    }
}