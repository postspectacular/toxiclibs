class VisibleBoxConstraint extends BoxConstraint {

  public VisibleBoxConstraint(Vec3D min, Vec3D max) {
    super(min,max);
  }
  
  public void draw() {
    Vec3D m=box.getMin();
    Vec3D n=box.getMax();
    beginShape(QUAD_STRIP);
    stroke(0);
    vertex(m.x,m.y,m.z); vertex(n.x,m.y,m.z);
    vertex(m.x,n.y,m.z); vertex(n.x,n.y,m.z);
    vertex(m.x,n.y,n.z); vertex(n.x,n.y,n.z);
    vertex(m.x,m.y,n.z); vertex(n.x,m.y,n.z);
    vertex(m.x,m.y,m.z); vertex(n.x,m.y,m.z);
    endShape();
  }
}


