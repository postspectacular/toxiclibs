/**
 * <p>The toxi.util.datatypes package contains classes to work with ranges of
 * numbers instead of fixed values. These classes are very helpful for picking
 * randomized parameters throughout a larger application. There're also biased
 * versions of these range types which impose a bell curve distribution of the
 * randomly picked values, centred around the bias value. This demo interactively
 * visualizes the behaviour of such a biased range.</p>
 *
 * <p><strong>Usage:</strong>
 * <ul>
 * <li>move mouse horizontally to change bias value</li>
 * <li>move mouse vertically to adjust the standard deviation/strength of the bias</li>
 * </ul></p>
 */

/* 
 * Copyright (c) 2010 Karsten Schmidt
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
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

import toxi.util.datatypes.*;

// standard deviation amplifier
float SD_AMP = 0.5;

// number of iterations for histogram
int NUM_ITERATIONS = 10000;

// histogram accumulator
float[] hist;

void setup() {
  size(1000,200);
  hist=new float[width];
}

void draw() {
  background(255);
  // create a new range with bias around horizontal mouse position
  // and standard deviation based on vertical mouse pos
  int bias=mouseX;
  float sd=mouseY*SD_AMP/height;
  BiasedIntegerRange range=new BiasedIntegerRange(0,width,bias,sd);
  float peak=0;
  // clear histogram
  for(int i=0; i<hist.length; i++) {
    hist[i]=0;
  }
  // pick random values and update histogram
  for(int i=0; i<NUM_ITERATIONS; i++) {
    int v=range.pickRandom();
    hist[v]++;
    // update peak value if necessary
    if (hist[v]>peak) {
      peak=hist[v];
    }
  }
  // compute scale factor for display
  float amp=height/peak;
  // draw histogram
  for(int i=0; i<width; i++) {
    line(i,height,i,height-hist[i]*amp);
  }
}

