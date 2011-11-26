class SpineSplineBlob extends SplineBlob {

  SpineSplineBlob(float w, float h, int sd) {
    super(w,h,sd);
    init();
  }

  SpineSplineBlob(SplineBlob b) {
    super(b.width,b.height,b.subDiv);
    spline=new Spline2D(b.points);
    spline.updateCoefficients();
    spline.delta[0].set(b.points[1].x*0.75,0);
    spline.delta[b.numP-1].set(-b.points[numP-2].x,0);
    init();
  }

  void init() {
    spine=new Spine(width*.5,height);
    bodyTemplate=new ArrayList<Vec2D>();
    bodyTemplate.addAll(spline.toLineStrip2D(subDiv).getVertices());
    bodySpine=new float[bodyTemplate.size()];
    Iterator<Vec2D> it=bodyTemplate.iterator();
    int idx=0;
    while(it.hasNext()) {
      float offX=spine.getOffsetAt(it.next().y/height);
      bodySpine[idx++]=offX;
    }
    for(int i=bodyTemplate.size()-1; i>=0; i--) {
      float offX=bodySpine[i];
      Vec2D p=bodyTemplate.get(i);
      Vec2D q=new Vec2D(p);
      q.x=offX-p.x;
      p.x+=offX;
      bodyTemplate.add(q);
    }
  }

  void draw() {
    noFill();
    stroke(0,0,255);
    beginShape();
    Iterator<Vec2D> i=bodyTemplate.iterator();
    while(i.hasNext()) {
      Vec2D v=(Vec2D)i.next();
      vertex(v.x,v.y);
    }
    endShape();
    spine.draw();
  }
}
