/**
 * <p>The demo uses a 2D cellular automata to slowly deform stroke marks
 * left by the user in the simulation space. The CA tends to slowly fill
 * and thicken curved strokes, but shapes eventually turn into blobs...</p>
 *
 * <p><strong>Usage:</strong><ul>
 * <li>click + drag mouse to draw</li>
 * <li>press any key to restart simulation</li>
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
ToneMap toneMap;

void setup() {
  size(256,256);
  // the birth rules specify options for when a cell becomes active
  // the numbers refer to the amount of ACTIVE neighbour cells allowed,
  // their order is irrelevant
  byte[] birthRules=new byte[] { 
    4,5,6,7,8 };
  // survival rules specify the possible numbers of allowed or required
  // ACTIVE neighbour cells in order for a cell to stay alive
  byte[] survivalRules=new byte[] { 
    3,4,6,7,8 };
  // setup cellular automata matrix
  ca=new CAMatrix(width,height);
  // assign the rules to the matrix
  // unlike traditional CA's only supporting binary cell states
  // this implementation supports a flexible number of states (cell age)
  // in this demo cell states reach from 0 - 128
  CARule rule=new CARule2D(birthRules,survivalRules,128,true);
  ca.setRule(rule);
  // create a gradient for rendering/mapping the CA
  ColorGradient grad=new ColorGradient();
  // NamedColors are preset colors, but any TColor can be added
  // see javadocs for list of names:
  // http://toxiclibs.org/docs/colorutils/toxi/color/NamedColor.html
  grad.addColorAt(0,NamedColor.WHITE);
  grad.addColorAt(rule.getStateCount(),NamedColor.RED);
  // the tone map will map cell states/ages to a gradient color
  toneMap=new ToneMap(0,rule.getStateCount()-1,grad);
  frameRate(999);
}

void draw() {
  loadPixels();
  if (mousePressed) {
    ca.drawBoxAt(mouseX,mouseY,19,1);
  }
  ca.update();
  int[] m=ca.getMatrix();
  for(int i=0; i<m.length; i++) {
    pixels[i]=toneMap.getARGBToneFor(m[i]);
  }
  updatePixels();
}

void keyPressed() {
  ca.reset();
}
