package toxi.sim.automata;

import toxi.math.MathUtils;

public class Wolfram1D implements CARule {

    protected boolean[] rules;
    protected int stateCount;
    protected boolean isTiling;
    protected int maxBitValue;
    protected int numBits;
    protected int kernelWidth;

    public Wolfram1D(int kernelWidth, boolean isTiling) {
        this.isTiling = isTiling;
        this.kernelWidth = kernelWidth;
        maxBitValue = (int) Math.pow(4, kernelWidth);
        numBits = maxBitValue * 2;
        rules = new boolean[numBits];
    }

    public void apply(CAMatrix m) {
        int[] cells = m.getMatrix();
        int[] nextgen = m.getSwapBuffer();
        for (int i = 0; i < cells.length; i++) {
            int sum = 0;
            for (int j = -kernelWidth, k = maxBitValue; j <= kernelWidth; j++) {
                int idx = i + j;
                if (isTiling) {
                    idx %= cells.length;
                    if (idx < 0) {
                        idx += cells.length;
                    }
                    sum = cells[idx] > 0 ? k : 0;
                } else {
                    if (idx >= 0 && idx < cells.length) {
                        sum += cells[idx] > 0 ? k : 0;
                    }
                }
                k >>>= 1;
            }
            nextgen[i] = rules[sum] ? (cells[i] + 1) % stateCount : 0;
        }
    }

    public int getStateCount() {
        return stateCount;
    }

    public boolean isTiling() {
        return isTiling;
    }

    public void randomize() {
        for (int i = 0; i < rules.length; i++) {
            rules[i] = MathUtils.random(2) > 0;
        }
    }

    public void setRuleArray(boolean[] r) {
        rules = r;
    }

    public void setRuleID(int id) {
        for (int i = rules.length - 1; i >= 0; i--) {
            rules[i] = (id & 1) == 1;
            id >>>= 1;
        }
    }

    public void setStateCount(int num) {
        stateCount = num;
    }

    public void setTiling(boolean state) {
        isTiling = state;
    }
}