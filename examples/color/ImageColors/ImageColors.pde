/**
 * Creating image based color palettes and color decimation through means
 * of using a histogram.
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
 
import toxi.color.*;
import toxi.math.*;

PImage img, workImg;

float tolerance=0.2;
  
void setup() {
  size(1000,500);
  background(255);
  noStroke();
  img=loadImage("test.jpg");
  workImg=new PImage(img.width,img.height,ARGB);
  // create a color histogram of image, using only 10% of its pixels and the given tolerance
  Histogram hist=Histogram.newFromARGBArray(img.pixels, img.pixels.length/10, tolerance, true);
  // now snap the color of each pixel to the closest color of the histogram palette
  // (that's really a posterization/quantization effect)
  TColor col=TColor.BLACK.copy();
  for(int i=0; i<img.pixels.length; i++) {
    col.setARGB(img.pixels[i]);
    TColor closest=col;
    float minD=1;
    for(HistEntry e : hist) {
      float d=col.distanceToRGB(e.getColor());
      if (d<minD) {
        minD=d;
        closest=e.getColor();
      }
    }
    workImg.pixels[i]=closest.toARGB();
  }
  workImg.updatePixels();
  // display original and posterized images
  image(img,0,0);
  image(workImg,workImg.width,0);
  // display power curve distribution of histogram colors as bar chart
  float x=0;
  int w=width/hist.getEntries().size();
  for(Iterator<HistEntry> i=hist.iterator(); i.hasNext() && x<width;) {
    HistEntry e=i.next();
    println(e.getColor().toHex()+": "+e.getFrequency());
    fill(e.getColor().toARGB());
    float h=e.getFrequency()*height;
    rect(x,height-h,w,h);
    x+=w;
  }  
}
