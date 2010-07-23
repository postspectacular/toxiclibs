/**
 * RGBColorHistogram demo
 * @author toxi
 *
 * Dependencies: toxiclibscore-0018, colorutils-0006
 * (or newer, available from: http://toxiclibs.org/ )
 */
import toxi.color.*;
import toxi.math.*;

void setup() {
  size(1280,640);
  background(255);
  noStroke();
  PImage img=loadImage("test.jpg");
  image(img,0,0);
  ArrayList hist=createHistogramFromImage(img.pixels, img.pixels.length, 0.25, true);
  TColor col=TColor.newGray(0);
  for(int i=0; i<img.pixels.length; i++) {
    col.setARGB(img.pixels[i]);
    TColor closest=col;
    float minD=1;
    for(int j=hist.size()-1; j>=0; j--) {
      HistEntry e=(HistEntry)hist.get(j);
      float d=col.distanceToRGB(e.col);
      if (d<minD) {
        minD=d;
        closest=e.col;
      }
    }
    img.pixels[i]=closest.toARGB();
  }
  img.updatePixels();
  image(img,img.width,0);
  float x=0;
  float w=(float)width/hist.size();
  for(Iterator<HistEntry> i=hist.iterator(); i.hasNext() && x<width;) {
    HistEntry e=i.next();
    println(e.col.toHex()+": "+e.freq);
    fill(e.col.toARGB());
    float h=e.freq*height;
    rect(x,height-h,w,h);
    x+=w;
  }  
}

/**
 * @param img
 *    pixel array to create histogram for
 * @param numSamples
 *    number pixels to be sampled in image
 * @param tolerance
 *    color tolerance used to merge similar colors
 *    (based on RGB distance)
 * @param blendCols
 *    switch to enable color blending of binned colors
 * @return sorted histogram as ArrayList
 */
ArrayList createHistogramFromImage(int[] img, int numSamples, float tolerance, boolean blendCols) {
  ColorList srcCols=ColorList.createFromARGBArray(img,numSamples,false);
  ArrayList hist=new ArrayList();
  float maxFreq=1;
  for(Iterator<TColor> i=srcCols.iterator(); i.hasNext();) {
    TColor c=i.next();
    HistEntry existing=null;
    for(Iterator<HistEntry> j=hist.iterator(); j.hasNext();) {
      HistEntry e=j.next();
      if (e.col.distanceToRGB(c)<tolerance) {
        if (blendCols) e.col.blend(c,1f/(e.freq+1));
        existing=e;
        break;
      }
    }
    if (existing!=null) {
      existing.freq++;
      if (existing.freq>maxFreq) maxFreq=existing.freq;
    } 
    else {
      hist.add(new HistEntry(c));
    }
  }
  Collections.sort(hist);
  maxFreq=1f/srcCols.size();
  for(Iterator i=hist.iterator(); i.hasNext();) {
    HistEntry e=(HistEntry)i.next();
    e.freq*=maxFreq;
  }
  return hist;
}

/**
 * A single histogram entry, a coupling of color & frequency
 * Implements a comparator to sort histogram entries based on freq.
 */
class HistEntry implements Comparable {
  float freq;
  TColor col;

  HistEntry(TColor c) {
    col=c;
    freq=1;
  }

  int compareTo(Object e) {
    return -(int)(freq-((HistEntry)e).freq);
  }
}


