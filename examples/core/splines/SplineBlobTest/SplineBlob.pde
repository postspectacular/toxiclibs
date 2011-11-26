class SplineBlob {

  Spline2D spline;
  Spine spine;

  Vec2D[] points;
  Vec2D[] coeffA,delta;
  float[] bi,b0,b1,b2,b3;

  int numP;
  int subDiv;

  List<Vec2D> bodyTemplate;
  List<Vec2D> outline;
  float[] bodySpine;

  float width,height;

  SplineBlob(float w, float h, int sd) {
    width=w;
    height=h;
    numP=6;
    subDiv=sd;
    points=new Vec2D[numP];
    int idx=0;
    points[idx++]=new Vec2D();
    points[idx++]=new Vec2D(random(0.5,0.85)*w,random(0.25,0.33)*h);
    points[idx++]=new Vec2D(random(0.6,1.2)*points[idx-2].x,random(0.45,0.6)*h);
    points[idx++]=new Vec2D(min(points[1].x*random(1.25,2),w),random(0.66,0.8)*h);
    points[idx++]=new Vec2D(points[idx-2].x*random(1.02,1.1),0.9*h);
    points[idx++]=new Vec2D(0,h);
    spline=new Spline2D(points);
    spline.updateCoefficients();
    spline.delta[0].set(points[1].x*0.75,0);
    spline.delta[numP-1].set(-points[numP-2].x,0);
    outline=spline.toLineStrip2D(subDiv).getVertices();
  }

  void draw() {
    noFill();
    stroke(255,0,0);
    pushMatrix();
    drawShape();
    scale(-1,1);
    drawShape();
    popMatrix();
  }

  void drawShape() {
    
    beginShape();
    Iterator i=outline.iterator();
    while(i.hasNext()) {
      Vec2D v=(Vec2D)i.next();
      vertex(v.x,v.y);
    }
    endShape();
    for (int k = 0; k < numP; k++) {
      ellipse(spline.pointList.get(k).x,spline.pointList.get(k).y,5,5);
    }
  }
}
