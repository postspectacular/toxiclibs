package toxi.sim.automata;

import toxi.math.MathUtils;

public class CAMatrix {

    protected int width, height;
    protected int[] matrix, swap;

    protected CARule rule;

    protected int generation;

    public CAMatrix(int width) {
        this(width, 1);
    }

    public CAMatrix(int w, int h) {
        width = w;
        height = h;
        matrix = new int[w * h];
        swap = new int[w * h];
    }

    public void addNoise(float perc) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (Math.random() < perc) {
                    int idx = y * width + x;
                    swap[idx] = matrix[idx] = 1;
                }
            }
        }
    }

    public void drawBoxAt(int x, int y, int w, int state) {
        for (int i = y - w / 2; i < y + w / 2; i++) {
            for (int j = x - w / 2; j < x + w / 2; j++) {
                if (j >= 0 && j < height && i >= 0 && i < width) {
                    int idx = j + i * width;
                    swap[idx] = matrix[idx] = state;
                }
            }
        }
    }

    /**
     * @return the generation
     */
    public final int getGeneration() {
        return generation;
    }

    /**
     * @return the height
     */
    public final int getHeight() {
        return height;
    }

    public final int getIndexFor(int x, int y) {
        return x + y * width;
    }

    /**
     * @return the matrix
     */
    public final int[] getMatrix() {
        return matrix;
    }

    /**
     * @return the rule
     */
    public final CARule getRule() {
        return rule;
    }

    /**
     * @return the temporary buffer used to compute the next generation
     */
    public final int[] getSwapBuffer() {
        return swap;
    }

    /**
     * @return the matrix width
     */
    public final int getWidth() {
        return width;
    }

    /**
     * Clears the matrix and resets the generation counter.
     * 
     * @return itself
     */
    public CAMatrix reset() {
        for (int i = 0; i < matrix.length; i++) {
            matrix[i] = 0;
            swap[i] = 0;
        }
        generation = 0;
        return this;
    }

    public void seedFromLong(long id) {
        for (int i = MathUtils.min(63, matrix.length); i >= 0; i--) {
            swap[i] = matrix[i] = (byte) (id & 1);
            id >>>= 1;
        }
    }

    public void seedImage(int[] pixels, int imgWidth, int imgHeight) {
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

    public void setRule(CARule r) {
        rule = r;
    }

    public void setStateAt(int x, int y, int state) {
        int idx = x + y * width;
        if (idx >= 0 && idx < matrix.length) {
            swap[idx] = matrix[idx] = state;
        } else {
            throw new ArrayIndexOutOfBoundsException("given coordinates: " + x
                    + ";" + y + " are out of bounds");
        }
    }

    public void update() {
        if (rule != null) {
            rule.apply(this);
            System.arraycopy(swap, 0, matrix, 0, matrix.length);
        }
    }
}