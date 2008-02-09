import toxi.geom.*;
import toxi.math.*;

void setup() {
  size(200,200);
  background(0);
  InterpolateStrategy is=new LinearInterpolation();
  InterpolateStrategy is2=new CircularInterpolation();
  InterpolateStrategy is2f=new CircularInterpolation(true);
  for(float x=0; x<width; x++) {
    float y=is.interpolate(0,height,x/width);
    stroke(255,0,0);
    point(x,y);
    y=is2.interpolate(0,height,x/width);
    stroke(0,250,0);
    point(x,y);
    y=is2f.interpolate(0,height,x/width);
    stroke(0,250,255);
    point(x,y);
  }
}
