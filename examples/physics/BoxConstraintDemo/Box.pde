class BoxConstraint implements ParticleConstraint {

  public AABB box;
  protected Ray3D intersectRay;

  public BoxConstraint(Vec3D min, Vec3D max) {
    this(AABB.fromMinMax(min,max));
  }

  public BoxConstraint(AABB box) {
    this.box=box;
    this.intersectRay=new Ray3D(box,new Vec3D());
  }

  public void apply(VerletParticle p) {
    if (p.isInAABB(box)) {
      intersectRay.setDirection(box.sub(p).normalize());
      p.set(box.intersectsRay(intersectRay,0,Float.MAX_VALUE));
    }
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


