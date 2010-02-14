/**
 * Hello Gray-Scott is a very basic demonstration of the underlying
 * reaction-diffusion simulation. This model can be used to produce
 * a wide variety of patterns, both static and animated and is therefore
 * well suited for being combined with other generative techniques.
 *
 * usage:
 * click + drag mouse to draw dots used as simulation seed,
 * press any key to reset
 */

/* 
 * Copyright (c) 2009 Karsten Schmidt
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
import toxi.color.*;

GrayScott gs;
ToneMap toneMap;
PImage img;

void setup() {
  size(256,256);
  gs=new GrayScott(width,height,true);
  img=loadImage("ti_yong.png");
  toneMap=new ToneMap(0,0.33,0,256,NamedColor.CRIMSON,NamedColor.WHITE);
}

void draw() {
  gs.seedImage(img.pixels,img.width,img.height);
  if (mousePressed) {
    gs.setRect(mouseX, mouseY,20,20);
  }
  loadPixels();
  for(int i=0; i<10; i++) gs.update(1);
  // read out the V result array
  // and use tone map to render colours
  for(int i=0; i<gs.v.length; i++) {
    pixels[i]=toneMap.getARGBToneFor(gs.v[i]);
  }
  updatePixels();
}

void keyPressed() {
  if (key>='1' && key<='9') {
    gs.setF(0.02+(key-'1')*0.001);
  } 
  else {
    gs.reset();
  }
}



