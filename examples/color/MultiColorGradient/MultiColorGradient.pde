/**
 * MultiColorGradient demonstrates the basic functionality of the ColorGradient
 * class of the toxi.color package.
 *
 * <p><strong>Usage:</strong>
 * <ul>
 * <li>mouse mouse horizontally to position red & yellow gradient points.</li>
 * <li>click mouse button to switch to alternative interpolation function</li>
 * </ul>
 * </p>
 */
/* 
 * Copyright (c) 2008-2009 Karsten Schmidt
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

import toxi.math.*;
import toxi.color.*;

void setup() {
  size(1000,200);
}

void draw() {
  ColorGradient grad=new ColorGradient();
  // use alternative interpolation function when mouse is pressed
  if (!mousePressed) {
    grad.setInterpolator(new CosineInterpolation());
  }
  grad.addColorAt(0,TColor.BLACK);
  grad.addColorAt(width,TColor.BLUE);
  grad.addColorAt(mouseX,TColor.RED);
  grad.addColorAt(width-mouseX,TColor.YELLOW);
  ColorList l=grad.calcGradient(0,width);
  int x=0;
  for(Iterator i=l.iterator(); i.hasNext();) {
    TColor c=(TColor)i.next();
    stroke(c.toARGB());
    line(x,0,x,height);
    x++;
  }
}

