class VisibleOctree extends PointOctree {

  VisibleOctree(Vec3D o, float d) {
    super(o,d);
  }
  
  void draw() {
    drawNode(this);
  }
  
  void drawNode(PointOctree n) {
    if (numChildren > 0) {
      noFill();
      stroke(depth, 50);
      pushMatrix(); 
      translate(x, y, z);
      box(dim);
      popMatrix();
      for (int i = 0; i < 8; i++) {
        if(children[i] != null) drawNode(children[i]); 
      }
    }
  }
}
