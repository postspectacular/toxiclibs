/**
 * <p>Hello Gray-Scott is a very basic demonstration of the underlying
 * reaction-diffusion simulation. This model can be used to produce
 * a wide variety of patterns, both static and animated and is therefore
 * well suited for being combined with other generative techniques.</p>
 *
 * <p><strong>usage:</strong></p>
 * <ul>
 * <li>click + drag mouse to draw dots used as simulation seed</li>
 * <li>press any key to reset</li>
 * </ul>
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

import toxi.sim.grayscott.*;
import toxi.math.*;

GrayScott gs;

void setup() {
  size(256,256);
  gs=new GrayScott(width,height,false);
  gs.setCoefficients(0.023,0.074,0.095,0.03);
}

void draw() {
  if (mousePressed) {
    gs.setRect(mouseX, mouseY,20,20);
  }
  loadPixels();
  for(int i=0; i<10; i++) gs.update(1);
  for(int i=0; i<gs.v.length; i++) {
    float cellValue=gs.v[i];
    int col=255-(int)(min(255,cellValue*768));
    pixels[i]=col<<16|col<<8|col|0xff000000;
  }
  updatePixels();
}

void keyPressed() {
  gs.reset();
}
