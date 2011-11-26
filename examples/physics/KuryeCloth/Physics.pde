// create the physical world by constructing all
// obstacles/constraints, particles and connecting them
// in the correct order using springs
void initPhysics() {
  physics=new VerletPhysics3D();
  physics.addBehavior(new GravityBehavior3D(new Vec3D(0,0.1,0)));
  spheres.add(new SphereConstraint(new Sphere(new Vec3D(0,-100,0),100),false));
  spheres.add(new SphereConstraint(new Sphere(new Vec3D(-50,50,0),150),false));
  spheres.add(new SphereConstraint(new Sphere(new Vec3D(-250,200,0),100),false));
  spheres.add(new SphereConstraint(new Sphere(new Vec3D(150,0,-50),100),false));
  ground=new BoxConstraint(new AABB(new Vec3D(0,320,0),new Vec3D(1000,50,1000)));
  ground.setRestitution(0);
  for(int y=0,idx=0; y<DIM; y++) {
    for(int x=0; x<DIM; x++) {
      VerletParticle3D p=new VerletParticle3D(x*REST_LENGTH-(DIM*REST_LENGTH)/2,-200,y*REST_LENGTH-(DIM*REST_LENGTH)/2);
      physics.addParticle(p);
      if (x>0) {
        VerletSpring3D s=new VerletSpring3D(p,physics.particles.get(idx-1),REST_LENGTH,STRENGTH);
        physics.addSpring(s);
      }
      if (y>0) {
        VerletSpring3D s=new VerletSpring3D(p,physics.particles.get(idx-DIM),REST_LENGTH,STRENGTH);
        physics.addSpring(s);
      }
      idx++;
    }
  }
  // add spheres as constraint to all particles
  for(Iterator i=spheres.iterator(); i.hasNext();) {
    SphereConstraint s=(SphereConstraint)i.next();
    VerletPhysics3D.addConstraintToAll(s,physics.particles);
  }
  // add ground as constraint to all particles
  VerletPhysics3D.addConstraintToAll(ground,physics.particles);
}
