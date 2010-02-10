package toxi.sim.automata;

import toxi.math.MathUtils;

public class CAMatrix {

    protected int width, height;
    protected int[] matrix, temp;

    protected AbstractCARule rule;

    public CAMatrix(int w, int h) {
        width = w;
        height = h;
        matrix = new int[w * h];
        temp = new int[w * h];
    }

    public void addNoise(float perc) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (MathUtils.random(1f) < perc) {
                    int idx = y * width + x;
                    temp[idx] = matrix[idx] = MathUtils.random(1, 2);// rule.stateCount);
                }
            }
        }
    }

    public void drawAt(int x, int y, int state) {
        int idx = x + y * width;
        temp[idx] = matrix[idx] = state;
    }

    public void drawBoxAt(int x, int y, int w, int state) {
        for (int i = y - w / 2; i < y + w / 2; i++) {
            for (int j = x - w / 2; j < x + w / 2; j++) {
                if (j >= 0 && j < height && i >= 0 && i < width) {
                    int idx = j + i * width;
                    temp[idx] = matrix[idx] = state;
                }
            }
        }
    }

    public void drawImage(int[] pixels, int imgWidth, int imgHeight) {
        int xo = MathUtils.clip((width - imgWidth) / 2, 0, width - 1);
        int yo = MathUtils.clip((height - imgHeight) / 2, 0, height - 1);
        imgWidth = MathUtils.min(imgWidth, width);
        imgHeight = MathUtils.min(imgHeight, height);
        for (int y = 0; y < imgHeight; y++) {
            int i = y * imgWidth;
            for (int x = 0; x < imgWidth; x++) {
                if (0 < (pixels[i + x] & 0xff)) {
                    int idx = (yo + y) * width + xo + x;
                    matrix[idx] = 1;
                }
            }
        }
    }

    /**
     * @return the height
     */
    public int getHeight() {
        return height;
    }

    public final int getIndexFor(int x, int y) {
        return x + y * width;
    }

    /**
     * @return the matrix
     */
    public int[] getMatrix() {
        return matrix;
    }

    /**
     * @return the rule
     */
    public AbstractCARule getRule() {
        return rule;
    }

    /**
     * @return the temp
     */
    public int[] getTempBuffer() {
        return temp;
    }

    /**
     * @return the width
     */
    public int getWidth() {
        return width;
    }

    public void reset() {
        for (int i = 0; i < matrix.length; i++) {
            matrix[i] = 0;
            temp[i] = 0;
        }
    }

    public void setRule(AbstractCARule r) {
        rule = r;
    }

    public void update() {
        if (rule != null) {
            rule.apply(this);
            System.arraycopy(temp, 0, matrix, 0, matrix.length);
        }
    }
}