/* 
 * Copyright (c) 2006-2008 Karsten Schmidt
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
 
import toxi.geom.*;
import toxi.math.*;

void setup() {
  size(200,200);
  background(0);
  InterpolateStrategy is=new LinearInterpolation();
  InterpolateStrategy is2=new CircularInterpolation();
  InterpolateStrategy is2f=new CircularInterpolation(true);
  InterpolateStrategy is3=new SigmoidInterpolation(1.5);
  InterpolateStrategy is4=new CosineInterpolation();
  for(float x=0; x<width; x++) {
    //linear
    float y=is.interpolate(0,height,x/width);
    stroke(255,0,0);
    point(x,y);
    // circular (ease out)
    y=is2.interpolate(0,height,x/width);
    stroke(0,250,0);
    point(x,y);
    // circular flipped (ease in)
    y=is2f.interpolate(0,height,x/width);
    stroke(0,250,255);
    point(x,y);
    // sigmoid (try setting sharpness in constructor)
    y=is3.interpolate(0,height,x/width);
    stroke(255,0,255);
    point(x,y);
    // cosine
    y=is4.interpolate(0,height,x/width);
    stroke(255,255,0);
    point(x,y);
  }
}
