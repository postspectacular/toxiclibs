/**
 * RGBColorHistogram demo
 * @author toxi
 *
 * Dependencies: toxiclibscore-0018, colorutils-0006
 * (or newer, available from: http://toxiclibs.org/ )
 */
import toxi.color.*;
import toxi.math.*;

PImage img,workImg;

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
