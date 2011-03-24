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

import java.math.BigInteger;
import java.util.Random;

import toxi.math.MathUtils;

/**
 * An extended & customized implementation of 1D Wolfram automata, fully
 * compatible with the classic definition, yet supporting flexible kernel
 * sizes/rule systems and n-ary cell states.
 * 
 * The algorithm can be configured to automatically expire cells when they've
 * reached their maximum state/age. This is a varying behaviour to the
 * traditional CA approach, but can produce very interesting results.
 */
public class CAWolfram1D implements CARule {

    protected boolean[] rules;
    protected int stateCount;
    protected boolean isTiling;
    protected int maxBitValue;
    protected int numBits;
    protected int kernelWidth;
    protected boolean isAutoexpire;

    public CAWolfram1D(int kernelWidth, boolean isTiling) {
        this(kernelWidth, 2, isTiling);
    }

    public CAWolfram1D(int kernelWidth, int states, boolean isTiling) {
        this.isTiling = isTiling;
        this.kernelWidth = kernelWidth;
        this.stateCount = states;
        maxBitValue = (int) Math.pow(4, kernelWidth);
        numBits = maxBitValue * 2;
        rules = new boolean[numBits];
    }

    public void evolve(EvolvableMatrix m) {
        int[] cells = m.getMatrix();
        int[] nextgen = m.getSwapBuffer();
        int maxState = stateCount - 1;
        for (int i = 0; i < cells.length; i++) {
            int sum = 0;
            for (int j = -kernelWidth, k = maxBitValue; j <= kernelWidth; j++) {
                int idx = i + j;
                if (isTiling) {
                    idx %= cells.length;
                    if (idx < 0) {
                        idx += cells.length;
                    }
                    sum += cells[idx] > 0 ? k : 0;
                } else {
                    if (idx >= 0 && idx < cells.length) {
                        sum += cells[idx] > 0 ? k : 0;
                    }
                }
                k >>>= 1;
            }
            if (isAutoexpire) {
                nextgen[i] = rules[sum] ? (cells[i] + 1) % stateCount : 0;
            } else {
                nextgen[i] = rules[sum] ? MathUtils.min(cells[i] + 1, maxState)
                        : 0;
            }
        }
    }

    /**
     * @return the number of rule bits used for the current kernel size.
     */
    public final int getNumRuleBits() {
        return numBits;
    }

    /**
     * @return the rules as boolean array.
     */
    public final boolean[] getRuleArray() {
        return rules;
    }

    /**
     * @return the rules packed into a single {@link BigInteger} value.
     */
    public final BigInteger getRuleAsBigInt() {
        BigInteger r = BigInteger.ZERO;
        for (int i = rules.length - 1; i >= 0; i--) {
            r = r.shiftLeft(1);
            if (rules[i]) {
                r = r.setBit(0);
            }
        }
        return r;
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.sim.automata.CARule#getStateCount()
     */
    public int getStateCount() {
        return stateCount;
    }

    /**
     * @return the isAutoexpire
     */
    public boolean isAutoExpire() {
        return isAutoexpire;
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.sim.automata.CARule#isTiling()
     */
    public boolean isTiling() {
        return isTiling;
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.sim.automata.CARule#randomize()
     */
    public void randomize() {
        setRuleID(new BigInteger(numBits, new Random()));
    }

    /**
     * Sets the auto expiration behaviour.
     * 
     * @param isAutoexpire
     *            true, if cells expire automatically when their max. state is
     *            reached (i.e. state reverts back to 0)
     */
    public void setAutoExpire(boolean isAutoexpire) {
        this.isAutoexpire = isAutoexpire;
    }

    /**
     * Updates the rules using the given array. The new array needs to be of the
     * same size as the length returned by {@link #getNumRuleBits()}.
     * 
     * @param r
     *            new rules
     * @return itself
     */
    public CAWolfram1D setRuleArray(boolean[] r) {
        if (r.length == numBits) {
            System.arraycopy(r, 0, rules, 0, r.length);
        } else {
            throw new IllegalArgumentException(
                    "rule array length needs to be equal to " + numBits
                            + " for the given kernel size");
        }
        return this;
    }

    /**
     * <p>
     * Uses the {@link BigInteger} value to define the new rules. Only the
     * lowest N bits of the value are used. The value of N depends on the
     * current kernel size and can be queried via {@link #getNumRuleBits()}. The
     * top most bit used corresponds with the left most kernel window item.
     * </p>
     * 
     * <p>
     * For example:
     * <ul>
     * <li>kernelWidth = 1 -> number of bits 2*(4^1)=8</li>
     * <li>kernelWidth = 2 -> number of bits 2*(4^2)=32</li>
     * <li>kernelWidth = 3 -> number of bits 2*(4^2)=128</li>
     * 
     * @param id
     * @return itself
     */
    public CAWolfram1D setRuleID(BigInteger id) {
        for (int i = 0; i < rules.length; i++) {
            rules[i] = id.testBit(i);
        }
        return this;
    }

    public CAWolfram1D setRuleID(long id) {
        for (int i = 0; i < rules.length; i++) {
            rules[i] = (id & 1) == 1;
            id >>>= 1;
        }
        return this;
    }

    public void setStateCount(int num) {
        stateCount = num;
    }

    public void setTiling(boolean state) {
        isTiling = state;
    }
}