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
