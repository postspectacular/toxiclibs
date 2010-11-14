class SpineSplineBlob extends SplineBlob {

  SpineSplineBlob(float w, float h, int sd) {
    super(w,h,sd);
    init();
  }

  SpineSplineBlob(SplineBlob b) {
    super(b.width,b.height,b.subDiv);
    spline=new Spline3D(b.points);
    spline.updateCoefficients();
    spline.delta[0].set(b.points[1].x*0.75,0,0);
    spline.delta[b.numP-1].set(-b.points[numP-2].x,0,0);
    spline.computeVertices(b.subDiv);
    init();
  }

  void init() {
    spine=new Spine(width*.5,height);
    bodyTemplate=new ArrayList();
    bodyTemplate.addAll(spline.vertices);
    bodySpine=new float[spline.vertices.size()];
    Iterator it=bodyTemplate.iterator();
    int idx=0;
    while(it.hasNext()) {
      float offX=spine.getOffsetAt(((Vec3D)it.next()).y/height);
      bodySpine[idx++]=offX;
    }
    for(int i=bodyTemplate.size()-1; i>=0; i--) {
      float offX=bodySpine[i];
      Vec3D p=(Vec3D)bodyTemplate.get(i);
      Vec3D q=new Vec3D(p);
      q.x=offX-p.x;
      p.x+=offX;
      bodyTemplate.add(q);
    }
  }

  void draw() {
    noFill();
    stroke(0,0,255);
    beginShape();
    Iterator i=bodyTemplate.iterator();
    while(i.hasNext()) {
      Vec3D v=(Vec3D)i.next();
      vertex(v.x,v.y,v.z);
    }
    endShape();
    spine.draw();
  }
}
