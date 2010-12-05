import toxi.math.*;

//InterpolateStrategy tween=new SigmoidInterpolation(1.2);
BezierInterpolation tween=new BezierInterpolation(3,-3);

void setup() {
  size(400,400);
}

void draw() {
  tween.setCoefficients(sin(frameCount*0.05)*0.5+2,-(sin(frameCount*0.03)*0.5+2));
  background(0,128,255);
  noStroke();
  fill(255,100);
  // pythagorean: c^2=a^2+b^2
  float maxDist=sqrt(sq(width/2-0)+sq(height/2));
  for(int y=0; y<=height; y+=20) {
    for(int x=0; x<=width; x+=20) {
      float d=min(dist(width/2,height/2,x,y),maxDist)/maxDist;
      float r=tween.interpolate(1,16,d);
      ellipse(x,y,r,r);
    }
  }
  stroke(255,255,0);
  for(int x=0; x<width; x++) {
    point(x,tween.interpolate(0,height,(float)x/width));
  }
}
