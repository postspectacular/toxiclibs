/**
 * <p>The ExtendedWolfram demo shows the extended features of the 1D cellular automata implementation
 * in combination with a tone map to render its current state. The CA supports flexible sized evolution
 * kernels and cell states. When using cell states other than binary, the algorithm can also be configured
 * to expire/kill cells again automatically once their maximum state/age has been reached. Rules can be
 * set through multiple means, incl. boolean arrays, long seed and BigInteger values (if kernel size requires
 * more than 64 bits).</p>
 * 
 * <p>Uncomment some of the lines in setup() to try out different, interesting presets I've collected.</p>
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

import java.math.BigInteger;

CAMatrix ca;
CAWolfram1D wolfram;
ToneMap toneMap;

int y;

void setup() {
  size(1024, 256);
  // setup cellular automata matrix
  ca = new CAMatrix(width);
  //wolfram = new CAWolfram1D(2, 64, true).setRuleID(0xd88d951b);
  //wolfram = new CAWolfram1D(2, 64, false).setRuleID(0xf9f327);
  //wolfram = new CAWolfram1D(3, 64, true).setRuleID(0x12a6d44e5d4132a0l);
  //wolfram = new CAWolfram1D(3, 64, true).setRuleID(0xd3b4d60379115903l);
  //wolfram = new CAWolfram1D(3, 64, true).setRuleID(new BigInteger("225d6860faadc2bb",16));
  wolfram = new CAWolfram1D(3, 64, true).setRuleID(new BigInteger("bf84ad5bb384155868430599fa5f0ecb",16));
  //wolfram = new CAWolfram1D(3, 64, true).setRuleID(new BigInteger("ffd2c2662b56188989f57b309df74a55",16));
  //wolfram = new CAWolfram1D(3, 64, true).setRuleID(new BigInteger("f685d79c2bf29178d5d9f01aede49424",16));
  wolfram.setAutoExpire(true);
  println(wolfram.getNumRuleBits());
  ca.setRule(wolfram);
  seedPattern();
  ColorGradient grad = new ColorGradient();
  grad.addColorAt(0, NamedColor.BLACK);
  grad.addColorAt(1, NamedColor.CYAN);
  grad.addColorAt(3, NamedColor.BLUE);
  grad.addColorAt(wolfram.getStateCount() - 1, NamedColor.WHITE);
  toneMap = new ToneMap(0, wolfram.getStateCount() - 1, grad);
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
    ca.addNoise(0.9f);
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
  for (int i = 0; i < width; i += 5) {
    ca.setStateAt(i, 0, 1);
  }
}
