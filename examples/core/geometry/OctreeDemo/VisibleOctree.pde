/**
 * Extends the default octree class in order to visualize currently
 * occupied cells in the tree.
 */
class VisibleOctree extends PointOctree {

  VisibleOctree(Vec3D o, float d) {
    super(o,d);
  }

  void draw() {
    drawNode(this);
  }

  void drawNode(PointOctree n) {
    if (n.getNumChildren() > 0) {
      noFill();
      stroke(n.getDepth(), 20);
      pushMatrix(); 
      translate(n.x, n.y, n.z);
      box(n.getNodeSize());
      popMatrix();
      PointOctree[] childNodes=n.getChildren();
      for (int i = 0; i < 8; i++) {
        if(childNodes[i] != null) drawNode(childNodes[i]); 
      }
    }
  }
}
