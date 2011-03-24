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

import toxi.math.MathUtils;

/**
 * A 1D/2D Cellular Automata simulation matrix with flexible support of automata
 * rules. The class provides accessors to the backing matrix arrays and utility
 * methods to manipulate them.
 */
public class CAMatrix implements EvolvableMatrix {

    protected int width, height;
    protected int[] matrix, swap;

    protected CARule rule;

    protected int generation;

    /**
     * Creates a new 1D instance of the given width. Technically this matrix is
     * still 2D, only its height = 1.
     * 
     * @param width
     */
    public CAMatrix(int width) {
        this(width, 1);
    }

    /**
     * Creates a new 2D instance of the given width & height.
     * 
     * @param w
     * @param h
     */
    public CAMatrix(int w, int h) {
        width = w;
        height = h;
        matrix = new int[w * h];
        swap = new int[w * h];
    }

    public CAMatrix addNoise(float probability) {
        return addNoise(probability, 0, rule != null ? rule.getStateCount() : 2);
    }

    /**
     * Adds noise to the matrix. Cell states are modified with the given
     * probability and within the given interval of possible target states.
     * 
     * @param probability
     * @param minState
     * @param maxState
     * @return itself
     */
    public CAMatrix addNoise(float probability, int minState, int maxState) {
        if (rule != null) {
            minState = MathUtils.clip(minState, 0, rule.getStateCount());
            maxState = MathUtils.clip(maxState, 0, rule.getStateCount());
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    if (MathUtils.randomChance(probability)) {
                        int idx = y * width + x;
                        swap[idx] = matrix[idx] = MathUtils.random(minState,
                                maxState);
                    }
                }
            }
            return this;
        }
        throw new IllegalStateException("CA rule not yet initialized");
    }

    /**
     * Sets all matrix cells in a square around the given x,y coordinates to the
     * requested state.
     * 
     * @param x
     *            box center x
     * @param y
     *            box center y
     * @param w
     *            box width
     * @param state
     *            target state
     * @return itself
     */
    public CAMatrix drawBoxAt(int x, int y, int w, int state) {
        for (int i = y - w / 2; i < y + w / 2; i++) {
            for (int j = x - w / 2; j < x + w / 2; j++) {
                if (j >= 0 && j < width && i >= 0 && i < height) {
                    int idx = j + i * width;
                    swap[idx] = matrix[idx] = state;
                }
            }
        }
        return this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.sim.automata.EvolvableMatrix#getGeneration()
     */
    public final int getGeneration() {
        return generation;
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.sim.automata.EvolvableMatrix#getHeight()
     */
    public final int getHeight() {
        return height;
    }

    /**
     * Computes the array index for the cell at x,y.
     * 
     * @param x
     * @param y
     * @return index
     */
    public final int getIndexFor(int x, int y) {
        return x + y * width;
    }

    public final int[] getMatrix() {
        return matrix;
    }

    /**
     * @return the rule instance
     */
    public final MatrixEvolver getRule() {
        return rule;
    }

    public final int[] getSwapBuffer() {
        return swap;
    }

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

    /**
     * Uses the given ARGB pixel array as seed mask for the matrix. The image is
     * placed centered and if bigger. Only the blue channel (lowest 8 bit of an
     * int) is used to determine if a cell is set to be alive or dead.
     * 
     * @param pixels
     * @param imgWidth
     * @param imgHeight
     * @return itself
     */
    public CAMatrix seedImage(int[] pixels, int imgWidth, int imgHeight) {
        final int xo = MathUtils.clip((width - imgWidth) / 2, 0, width - 1);
        final int yo = MathUtils.clip((height - imgHeight) / 2, 0, height - 1);
        imgWidth = MathUtils.min(imgWidth, width);
        imgHeight = MathUtils.min(imgHeight, height);
        for (int y = 0; y < imgHeight; y++) {
            final int i = y * imgWidth;
            final int yoIndex = (yo + y) * width;
            for (int x = 0; x < imgWidth; x++) {
                if (0 < (pixels[i + x] & 0xff)) {
                    int idx = yoIndex + xo + x;
                    matrix[idx] = 1;
                }
            }
        }
        return this;
    }

    /**
     * Assigns the given rule as evaluator for this matrix.
     * 
     * @param r
     *            rule implementation
     * @return itself
     */
    public CAMatrix setRule(CARule r) {
        rule = r;
        return this;
    }

    /**
     * Sets the cell state at x,y. If the coordinates are outside the matrix an
     * {@link ArrayIndexOutOfBoundsException} is thrown.
     * 
     * @param x
     * @param y
     * @param state
     * @return itself
     */
    public CAMatrix setStateAt(int x, int y, int state) {
        int idx = x + y * width;
        if (idx >= 0 && idx < matrix.length) {
            swap[idx] = matrix[idx] = state;
        } else {
            throw new ArrayIndexOutOfBoundsException("given coordinates: " + x
                    + ";" + y + " are out of bounds");
        }
        return this;
    }

    /**
     * Evolves the matrix to the next generation by applying one iteration of
     * the assigned {@link CARule} implementation. If no rule is assigned, the
     * method does nothing.
     * 
     * @return itself
     */
    public CAMatrix update() {
        if (rule != null) {
            rule.evolve(this);
            System.arraycopy(swap, 0, matrix, 0, matrix.length);
            generation++;
        }
        return this;
    }
}