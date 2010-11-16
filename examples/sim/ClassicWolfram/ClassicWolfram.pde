/**
 * <p>ClassicWolfram demo shows the basic usage pattern for the 1D cellular automata implementation
 * in combination with a duo-tone color map to render its current state. The CA rule is fully compatible
 * with the standard Wolfram rule set, but can also be expanded to work with larger kernels and has a
 * flexible number of cell states. These features are explained in the ExtentedWolfram demo.</p>
 *
 * <p>Uncomment some of the lines in setup() to try out different, interesting presets I've collected.</p>
 * 
 * <p>More information:<ul>
 * <li>http://en.wikipedia.org/wiki/Cellular_automaton</li>
 * <li>http://atlas.wolfram.com/01/01/</li>
 * </ul</p>
 *
 * <p><strong>Usage:</strong><ul>
 * <li>r: randomize rule</li>
 * <li>p: reset seed pattern</li>
 * <li>n: add noise to pattern</li>
 * </ul></p>
 */

/* 
 * Copyright (c) 2010 Karsten Schmidt
 * 
 * This demo & library is free software; you can redistribute it and/or
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

import toxi.sim.automata.*;
import toxi.math.*;
import toxi.color.*;

CAMatrix ca;
CAWolfram1D wolfram;
ToneMap toneMap;

int y;

void setup() {
  size(256, 256);
  // setup cellular automata matrix
  ca = new CAMatrix(width);
  // create a classic Wolfram automata
  wolfram = new CAWolfram1D(1, 2, true).setRuleID(0x5a);
  //wolfram = new CAWolfram1D(1, 2, true).setRuleID(0x49);
  //wolfram = new CAWolfram1D(1, 2, true).setRuleID(0x96);
  //wolfram = new CAWolfram1D(1, 2, true).setRuleID(0x65);
  //wolfram = new CAWolfram1D(1, 2, true).setRuleID(0x89);
  //wolfram = new CAWolfram1D(1, 2, true).setRuleID(0x6d);
  //wolfram = new CAWolfram1D(1, 2, true).setRuleID(0x78);
  ca.setRule(wolfram);
  seedPattern();
  toneMap = new ToneMap(0, 1, TColor.BLACK, TColor.WHITE);
}

void draw() {
  loadPixels();
  ca.update();
  int[] m = ca.getMatrix();
  for (int i = 0, idx = y * width; i < m.length; i++) {
    pixels[idx + i] = toneMap.getARGBToneFor(m[i]);
  }
  updatePixels();
  y = (y + 1) % height;
}

void keyPressed() {
  if (key == 'n') {
    ca.reset();
    ca.addNoise(0.9f,0,wolfram.getStateCount());
  }
  if (key == 'p') {
    ca.reset();
    seedPattern();
  }
  if (key == 'r') {
    ca.reset();
    seedPattern();
    wolfram.randomize();
    println(wolfram.getRuleAsBigInt().toString(16));
  }
  y = 0;
}

void seedPattern() {
  ca.setStateAt(width/2,0,1);
}

