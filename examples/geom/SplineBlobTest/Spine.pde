class Spine {
  Vec3D[] vertices;
  float bodyHeight;

  Spine(float maxOffset, float bh) {
    bodyHeight=bh;
    vertices=new Vec3D[4];
    vertices[0]=new Vec3D(random(-1,1)*maxOffset,0,0);
    vertices[1]=new Vec3D(random(-1,1)*maxOffset,0.25*bodyHeight,0);
    vertices[2]=new Vec3D(random(-1,1)*maxOffset,0.75*bodyHeight,0);
    vertices[3]=new Vec3D(random(-1,1)*maxOffset,bodyHeight,0);
  }

  float getOffsetAt(float t) {
    return bezierPoint(vertices[0].x,vertices[1].x,vertices[2].x,vertices[3].x,t);
  }

  void draw() {
    noFill();
    bezier(vertices[0].x,vertices[0].y,vertices[1].x,vertices[1].y,vertices[2].x,vertices[2].y,vertices[3].x,vertices[3].y);
    for(float t=0; t<=1.001; t+=0.05) {
      float x=getOffsetAt(t);
      ellipse(x,t*bodyHeight,5,5);
    }
  }
}
