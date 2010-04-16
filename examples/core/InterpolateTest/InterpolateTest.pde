/**
 * <p>This demo shows the effect of various InterpolateStrategy implementations available.
 * For the more interactive ZoomLensInterpolation please see the ZoomLens demo.</p>
 *
 * <p><strong>Usage:</strong> Move mouse to adjust parameters for
 * sigmoid & exponential interpolation</p>
 */

/* 
 * Copyright (c) 2006 Karsten Schmidt
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

import toxi.math.*;

void setup() {
  size(200,200);
}

void draw() {
  background(0);

  InterpolateStrategy linear=new LinearInterpolation();
  InterpolateStrategy circular=new CircularInterpolation();
  InterpolateStrategy invCircular=new CircularInterpolation(true);
  InterpolateStrategy sigmoid=new SigmoidInterpolation((float)mouseX/width*4);
  InterpolateStrategy cosine=new CosineInterpolation();
  InterpolateStrategy expo=new ExponentialInterpolation((float)mouseX/width*4);

  for(float x=0; x<width; x++) {
    float t=x/width;
    //linear
    float y=linear.interpolate(0,height,t);
    stroke(255,0,0);
    point(x,y);
    // circular (ease out)
    y=circular.interpolate(0,height,t);
    stroke(0,255,0);
    point(x,y);
    // circular flipped (ease in)
    y=invCircular.interpolate(0,height,t);
    stroke(0,255,255);
    point(x,y);
    // sigmoid (try setting sharpness in constructor)
    y=sigmoid.interpolate(0,height,t);
    stroke(255,0,255);
    point(x,y);
    // sigmoid (try setting sharpness in constructor)
    y=expo.interpolate(0,height,t);
    stroke(0,0,255);
    point(x,y);
    // cosine
    y=cosine.interpolate(0,height,t);
    stroke(255,255,0);
    point(x,y);
  }
}

