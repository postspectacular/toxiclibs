package toxi.sim.automata;

import java.util.ArrayList;
import java.util.List;

import toxi.math.MathUtils;

public class CARule2D implements CARule {

    protected boolean[] survivalRules;
    protected boolean[] birthRules;
    protected int stateCount;
    protected float randomBirthChance = 0.15f;
    protected float randomSurvivalChance = 0.25f;
    protected boolean isTiling;
    protected boolean isAutoExpire;

    public CARule2D(byte[] brules, byte[] srules, int st, boolean tiled) {
        birthRules = new boolean[9];
        setBirthRules(brules);
        survivalRules = new boolean[9];
        setSurvivalRules(srules);
        stateCount = MathUtils.max(1, st);
        setTiling(tiled);
    }

    public void evolve(EvolvableMatrix m) {
        int width = m.getWidth();
        int height = m.getHeight();
        int[] matrix = m.getMatrix();
        int[] temp = m.getSwapBuffer();
        int maxState = stateCount - 1;
        int x1, x2, y1, y2;
        if (isTiling) {
            x1 = 0;
            x2 = width;
            y1 = 0;
            y2 = height;
        } else {
            x1 = 1;
            x2 = width - 1;
            y1 = 1;
            y2 = height - 1;
        }
        for (int y = y1; y < y2; y++) {
            // determine up and down cell indices
            int up = (y > 0 ? y - 1 : height - 1) * width;
            int down = (y < height - 1 ? y + 1 : 0) * width;
            int centre = y * width;
            for (int x = x1; x < x2; x++) {
                // determine left and right cell offsets
                int left = x > 0 ? x - 1 : width - 1;
                int right = x < width - 1 ? x + 1 : 0;
                int currVal = matrix[centre + x];
                int newVal = currVal;
                int sum = 0;
                if (currVal > 1) {
                    if (isAutoExpire) {
                        newVal = (newVal + 1) % stateCount;
                    } else {
                        newVal = MathUtils.min(newVal + 1, maxState);
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
                        sum++; // right
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
                        if (survivalRules[sum]) {
                            newVal = 1;
                        } else {
                            if (isAutoExpire) {
                                newVal = (newVal + 1) % stateCount;
                            } else {
                                newVal = MathUtils.min(newVal + 1, maxState);
                            }
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

    public int getStateCount() {
        return stateCount;
    }

    public boolean isAutoExpire() {
        return isAutoExpire;
    }

    public boolean isTiling() {
        return isTiling;
    }

    protected byte[] randomArray(double chance) {
        List<Byte> rules = new ArrayList<Byte>();
        for (byte i = 0; i < 9; i++) {
            if (Math.random() < chance) {
                rules.add(i);
            }
        }
        byte[] r = new byte[rules.size()];
        for (int i = rules.size() - 1; i >= 0; i--) {
            r[i] = rules.get(i);
        }
        return r;
    }

    public void randomize() {
        setRuleArray(randomArray(randomBirthChance), birthRules);
        setRuleArray(randomArray(randomSurvivalChance), survivalRules);
    }

    public void setAutoExpire(boolean state) {
        this.isAutoExpire = state;
    }

    public void setBirthRules(byte[] b) {
        setRuleArray(b, birthRules);
    }

    public void setRandomProbabilities(float birth, float survival) {
        randomBirthChance = birth;
        randomSurvivalChance = survival;
    }

    protected void setRuleArray(byte[] seed, boolean[] kernel) {
        for (int i = 0; i < seed.length; i++) {
            byte id = seed[i];
            if (id >= 0 && id < kernel.length) {
                kernel[id] = true;
            } else {
                throw new ArrayIndexOutOfBoundsException("invalid rule index: "
                        + id + " (needs to be less than 9 for a 3x3 kernel");
            }
        }
    }

    public void setStateCount(int num) {
        stateCount = num;
    }

    public void setSurvivalRules(byte[] s) {
        setRuleArray(s, survivalRules);
    }

    public void setTiling(boolean state) {
        isTiling = state;
    }
}