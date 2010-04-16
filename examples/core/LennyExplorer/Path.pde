class Path {
  Vec2D[] p;
  Vec2D last;
  BoundaryCheck bounds;
  Line2D a,b;
  Line2D.LineIntersection cut;
  float theta=0;
  float delta;
  float speed;
  int numSearches;

  public Path(BoundaryCheck bounds, float speed, float delta, int history) {
    this.bounds=bounds;
    this.speed=speed;
    this.delta=delta;
    p=new Vec2D[history];
    a=new Line2D(new Vec2D(),new Vec2D());
    b=new Line2D(new Vec2D(),new Vec2D());
    for(int i=0; i<p.length; i++) p[i]=bounds.getCentroid().copy();
    last=p[0].copy();
  }

  void grow(){
    if(random(1) < 0.1) {
      delta=random(-1,1)*0.2;
    }
    if(!isIntersecting()) {
      move();
    } 
    else {
      search();
    }
  }

  void move() {
    for(int i=p.length-1; i>0; i--) p[i].set(p[i-1]);
    last.set(p[0]);
    theta+=delta;
    p[0]=last.add(new Vec2D(speed,theta).toCartesian());
    numSearches=0;
  }

  void search(){
    theta+=delta;
    p[0]=last.add(new Vec2D(speed,theta).toCartesian());
    numSearches++;
  }

  void render(){
    beginShape();
    for(int i=1; i<p.length; i++) {
      curveVertex(p[i].x,p[i].y);
    }
    endShape();
  }

  boolean isIntersecting() {
    if(!bounds.containsPoint(p[0])) {
      return true;
    }
    if(numSearches<100) {
      a.set(p[0], p[1]);
      for(int i=3; i<p.length; i++){
        b.set(p[i], p[i-1]);
        cut=a.intersectLine(b);
        if(cut.getType() == Line2D.LineIntersection.Type.INTERSECTING) {
          return true;
        }
      }
    }
    return false; 
  }
}

