/*
 *   __               .__       .__  ._____.           
 * _/  |_  _______  __|__| ____ |  | |__\_ |__   ______
 * \   __\/  _ \  \/  /  |/ ___\|  | |  || __ \ /  ___/
 *  |  | (  <_> >    <|  \  \___|  |_|  || \_\ \\___ \ 
 *  |__|  \____/__/\_ \__|\___  >____/__||___  /____  >
 *                   \/       \/             \/     \/ 
 *
 * Copyright (c) 2006-2011 Karsten Schmidt
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * http://creativecommons.org/licenses/LGPL/2.1/
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301, USA
 */

package toxi.sim.automata;

import java.util.ArrayList;
import java.util.List;

import toxi.math.MathUtils;
import toxi.util.datatypes.ArrayUtil;

public class CARule2D implements CARule {

    public static final byte[] booleanToByteArray(boolean[] kernel) {
        List<Byte> buf = new ArrayList<Byte>(kernel.length);
        for (byte i = 0; i < kernel.length; i++) {
            if (kernel[i]) {
                buf.add(i);
            }
        }
        return byteListToArray(buf);
    }

    public static final byte[] byteListToArray(List<Byte> rules) {
        byte[] r = new byte[rules.size()];
        for (int i = rules.size() - 1; i >= 0; i--) {
            r[i] = rules.get(i);
        }
        return r;
    }

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
                if (matrix[up + left] > 0) {
                    sum++; // top left
                }
                if (matrix[up + x] > 0) {
                    sum++; // top
                }
                if (matrix[up + right] > 0) {
                    sum++; // top right
                }
                if (matrix[centre + left] > 0) {
                    sum++; // left
                }
                if (matrix[centre + right] > 0) {
                    sum++; // right
                }
                if (matrix[down + left] > 0) {
                    sum++; // bottom left
                }
                if (matrix[down + x] > 0) {
                    sum++; // bottom
                }
                if (matrix[down + right] > 0) {
                    sum++; // bottom right
                }
                if (currVal > 0) {
                    // if alive, check survival...
                    if (survivalRules[sum]) {
                        if (isAutoExpire) {
                            newVal = (newVal + 1) % stateCount;
                        } else {
                            newVal = MathUtils.min(newVal + 1, maxState);
                        }
                    } else {
                        newVal = 0;
                    }
                } else {
                    // else check birth rules...
                    if (birthRules[sum]) {
                        newVal = 1;
                    }
                }
                temp[centre + x] = newVal;
            }
        }
    }

    public byte[] getBirthRules() {
        return booleanToByteArray(birthRules);
    }

    public int getStateCount() {
        return stateCount;
    }

    public byte[] getSurvivalRules() {
        return booleanToByteArray(survivalRules);
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
            if (MathUtils.randomChance(chance)) {
                rules.add(i);
            }
        }
        if (rules.size() == 0) {
            rules.add((byte) MathUtils.random(9));
        }
        return byteListToArray(rules);
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
        for (int i = 0; i < kernel.length; i++) {
            kernel[i] = false;
        }
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

    public String toString() {
        return "births: " + ArrayUtil.toString(getBirthRules()) + " survivals:"
                + ArrayUtil.toString(getSurvivalRules());
    }
}