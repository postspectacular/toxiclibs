/**
 * <p>This is the classic Conway's Game Of Life CA, using the generic 2D
 * cellular automata implementation of the simutils package. The CA simulation
 * can be configured with birth and survival rules to create the complete
 * set of rules possible with a 3x3 cell evaluation kernel.</p>
 *
 * http://en.wikipedia.org/wiki/Conway's_Game_of_Life
 *
 * <p><strong>Usage:</strong><ul>
 * <li>click + drag mouse to disturb the CA matrix</li>
 * <li>press 'r' to restart simulation</li>
 * </ul></p>
 */

/* 
 * Copyright (c) 2011 Karsten Schmidt
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

CAMatrix ca;

void setup() {
  size(680,382);
  // setup cellular automata matrix
  ca=new CAMatrix(width,height);

  // the birth rules specify options for when a cell becomes active
  // the numbers refer to the amount of ACTIVE neighbour cells allowed.
  byte[] birthRules=new byte[] { 3 };
  // survival rules specify the possible numbers of required
  // ACTIVE neighbour cells in order for a cell to stay alive
  byte[] survivalRules=new byte[] { 2,3 };
  CARule rule=new CARule2D(birthRules,survivalRules,2,true);
  // assign the rules to the CAMatrix
  ca.setRule(rule);
}

void draw() {
  loadPixels();
  if (mousePressed) {
    ca.drawBoxAt(mouseX,mouseY,19,1);
  }
  ca.update();
  int[] m=ca.getMatrix();
  for(int i=0; i<m.length; i++) {
    pixels[i] = m[i] > 0 ? 0xffff0000 : 0xffffffff;
  }
  updatePixels();
}

void keyPressed() {
  if (key=='r') {
    ca.reset();
  }
}
