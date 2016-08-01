import geomerative.*;    // import geomerative library


import toxi.physics2d.constraints.*;
import toxi.physics2d.behaviors.*;
import toxi.physics2d.*;
import toxi.geom.*;

import java.util.List;

VerletPhysics2D physics;
VerletParticle2D selectedParticle;
 
RShape font;
String input = "Hello!";
 
void setup() {
    size( 1280, 720, P3D ); 
    initPhysics();
    fill(255);
    smooth();
}
 
void draw() {
  physics.update();
  background( 0 );
  stroke(255);
  for(VerletSpring2D s : physics.springs) {
    line(s.a.x,s.a.y,s.b.x,s.b.y);
  }
  for(VerletParticle2D p : physics.particles) {
    ellipse(p.x,p.y,3,3);
  }
}

void initPhysics() {
  
  physics=new VerletPhysics2D();
  // set screen bounds as bounds for physics sim
  physics.setWorldBounds(new Rect(0,0,width,height));
  // add gravity along positive Y axis
  physics.addBehavior(new GravityBehavior2D(new Vec2D(0,0.1)));
  
  RG.init( this ); 
  RFont fnt = RG.loadFont("ReplicaBold.ttf");  // file name
  RG.textFont( fnt, 330 );   // RFont object, size
  font = RG.getText( input );  // String to RShape
  
  RG.setPolygonizer(RG.UNIFORMLENGTH);
  RG.setPolygonizerLength(10);    // length of segment
  
  RPoint[][] paths = font.getPointsInPaths();  // multidimensional array of x and y coordinates
  
  Vec2D offset=new Vec2D(200,250);
  
  if ( paths != null ) {
      for ( int ii = 0; ii < paths.length; ii++ ) {
          RPoint[] points = paths[ii];
          List<VerletParticle2D> pathParticles=new ArrayList<VerletParticle2D>(points.length);
          for ( int i = 0; i < points.length; i++ ) {
              VerletParticle2D p=new VerletParticle2D(points[i].x+offset.x,points[i].y+offset.y);
              physics.addParticle(p);
              pathParticles.add(p);
              if (i>0) {
                physics.addSpring(new VerletSpring2D(pathParticles.get(i-1),p,pathParticles.get(i-1).distanceTo(p),1));
              }
          }
          VerletParticle2D first=pathParticles.get(0);
          VerletParticle2D last=pathParticles.get(points.length-1);
          physics.addSpring(new VerletSpring2D(first,last,first.distanceTo(last),1));
          first.lock();
      }
  }
}
