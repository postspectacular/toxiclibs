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

package toxi.util.datatypes;

import java.util.Random;

import toxi.math.MathUtils;

public class IntegerSet {

    public int[] items;
    public int currID = -1;
    public int current;

    private Random random = new Random();

    public IntegerSet(int[] items) {
        this.items = items;
        pickRandom();
    }

    public IntegerSet(Integer... items) {
        if (items.length > 0) {
            this.items = new int[items.length];
            for (int i = 0; i < items.length; i++) {
                this.items[i] = items[i];
            }
            pickRandom();
        } else {
            throw new IllegalArgumentException("can't create empty IntegerSet");
        }
    }

    public boolean contains(int value) {
        for (int i = 0; i < items.length; i++) {
            if (items[i] == value) {
                return true;
            }
        }
        return false;
    }

    public int getCurrent() {
        return current;
    }

    public int pickRandom() {
        currID = MathUtils.random(random, items.length);
        current = items[currID];
        return current;
    }

    public int pickRandomUnique() {
        if (items.length > 1) {
            int newID = currID;
            while (newID == currID) {
                newID = MathUtils.random(random, items.length);
            }
            currID = newID;
        } else {
            currID = 0;
        }
        current = items[currID];
        return current;
    }

    public IntegerSet seed(long seed) {
        random.setSeed(seed);
        return this;
    }

    public void setRandom(Random rnd) {
        random = rnd;
    }
}
