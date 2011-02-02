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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Random;

import toxi.math.MathUtils;

public class GenericSet<T> implements Iterable<T> {

    protected ArrayList<T> items;
    protected int currID = -1;
    protected T current;

    protected Random random = new Random();

    public GenericSet(Collection<T> items) {
        this.items = new ArrayList<T>(items);
        pickRandom();
    }

    public GenericSet(T... obj) {
        items = new ArrayList<T>(obj.length);
        for (int i = 0; i < obj.length; i++) {
            items.add(obj[i]);
        }
        if (items.size() > 0) {
            pickRandom();
        }
    }

    public boolean add(T obj) {
        boolean isAdded = items.add(obj);
        if (items.size() == 1) {
            pickRandom();
        }
        return isAdded;
    }

    public boolean addAll(Collection<T> coll) {
        return items.addAll(coll);
    }

    public void clear() {
        items.clear();
    }

    public boolean contains(T obj) {
        return items.contains(obj);
    }

    public GenericSet<T> copy() {
        GenericSet<T> set = new GenericSet<T>(items);
        set.current = current;
        set.currID = currID;
        set.random = random;
        return set;
    }

    public T getCurrent() {
        return current;
    }

    public ArrayList<T> getItems() {
        return items;
    }

    public Iterator<T> iterator() {
        return items.iterator();
    }

    public T pickRandom() {
        currID = MathUtils.random(random, items.size());
        current = items.get(currID);
        return current;
    }

    public T pickRandomUnique() {
        int size = items.size();
        if (size > 1) {
            int newID = currID;
            while (newID == currID) {
                newID = MathUtils.random(random, size);
            }
            currID = newID;
        } else {
            currID = 0;
        }
        current = items.get(currID);
        return current;
    }

    public GenericSet<T> seed(long seed) {
        random.setSeed(seed);
        return this;
    }

    public void setRandom(Random rnd) {
        random = rnd;
    }

    public int size() {
        return items.size();
    }
}
