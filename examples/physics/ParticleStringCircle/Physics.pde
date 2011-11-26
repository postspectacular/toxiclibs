void initPhysics() {
  physics=new VerletPhysics2D();
  // set screen bounds as bounds for physics sim
  physics.setWorldBounds(new Rect(0,0,width,height));
  // add gravity along positive Y axis
  physics.addBehavior(new GravityBehavior2D(new Vec2D(0,0.1)));
  // compute spacing for string particles
  float delta=(float)width/(STRING_RES-1);
  for(int i=0; i<STRING_RES; i++) {
    // create particles along X axis
    VerletParticle2D p=new VerletParticle2D(i*delta,height/2);
    physics.addParticle(p);
    // define a repulsion field around each particle
    // this is used to push the ball away
    physics.addBehavior(new AttractionBehavior2D(p,delta*1.5,-20));
    // connect each particle to its previous neighbour
    if (i>0) {
      VerletParticle2D q=physics.particles.get(i-1);
      VerletSpring2D s=new VerletSpring2D(p,q,delta*0.5,0.1);
      physics.addSpring(s);
    }
  }
  // lock 1st & last particles
  physics.particles.get(0).lock();
  physics.particles.get(physics.particles.size()-1).lock();
  
  // create ball
  // first create a particle as the ball centre
  VerletParticle2D c=new VerletParticle2D(width/2,100);
  physics.addParticle(c);
  // list to store all ball perimeter particles
  List<VerletParticle2D> cparts=new ArrayList<VerletParticle2D>();
  for(int i=0; i<BALL_RES; i++) {
    // create a rotation vector, scale it to the radius and move relative to ball center
    Vec2D pos=Vec2D.fromTheta(i*TWO_PI/BALL_RES).scaleSelf(BALL_RADIUS).addSelf(c);
    // create particle and add to lists
    VerletParticle2D p = new VerletParticle2D(pos);
    cparts.add(p);
    physics.addParticle(p);
    // connect to ball center for extra stability
    physics.addSpring(new VerletSpring2D(c,p,BALL_RADIUS,0.01));
    // also connect all perimeter particles sequentially
    if (i>0) {
      VerletParticle2D q=cparts.get(i-1);
      physics.addSpring(new VerletSpring2D(p,q,p.distanceTo(q),1));
    }
  }
  // finally close ball perimeter by connecting first & last particle
  VerletParticle2D p=cparts.get(0);
  VerletParticle2D q=cparts.get(BALL_RES-1);
  physics.addSpring(new VerletSpring2D(p,q,p.distanceTo(q),1));
}
