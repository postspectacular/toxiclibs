import processing.opengl.*;
import toxi.geom.*;

// octree dimensions
float DIM = 100;
float DIM2 = DIM/2;

// sphere clip radius
float RADIUS = 20;

// number of particles to add at once
int NUM = 100;

// show octree debug info
boolean showOctree = false;

// use clip sphere or bounding box
boolean useSphere = false;

// view rotation
float xrot = THIRD_PI;
float zrot = 0.1;

PointOctree octree;
Vec3D cursor = new Vec3D();

// start with one particle
int numParticles = 1;

void setup() {
  size(800,600,OPENGL);
  textFont(loadFont("AudimatMonoLight-18.vlw"));
  // setup empty octree so that it's centered around the world origin
  octree=new PointOctree(new Vec3D(-1,-1,-1).scaleSelf(DIM2),DIM);
  // add an initial particle at the origin
  octree.addPoint(new Vec3D());
}

void draw() {
  background(255);
  pushMatrix();
  lights();
  translate(width/2,height/2,0);
  // rotate view on mouse drag
  if (mousePressed) {
    xrot+=(mouseY*0.01-xrot)*0.1;
    zrot+=(mouseX*0.01-zrot)*0.1;
  } 
  // or move cursor
  else {
    cursor.x=-(width*0.5-mouseX)/(width/2)*DIM2;
    cursor.y=-(height*0.5-mouseY)/(height/2)*DIM2;
  }
  rotateX(xrot);
  rotateZ(zrot);
  scale(3);
  // show debug view of tree
  if (showOctree) octree.draw(this);
  // show crosshair 3D cursor
  stroke(255,0,0);
  noFill();
  beginShape(LINES);
  vertex(cursor.x,-DIM2,0);
  vertex(cursor.x,DIM2,0);
  vertex(-DIM2,cursor.y,0);
  vertex(DIM2,cursor.y,0);
  endShape();
  // show particles within the specific clip radius
  fill(0,255,0);
  noStroke();
  long t0=System.currentTimeMillis();
  ArrayList points=null;
  if (useSphere) {
    points=octree.getPointsWithinSphere(cursor,RADIUS);
  } 
  else {
    points=octree.getPointsWithinBox(new AABB(cursor,new Vec3D(RADIUS,RADIUS,RADIUS)));
  }
  long dt=System.currentTimeMillis()-t0;
  int numClipped=0;
  if (points!=null) {
    numClipped=points.size();
    Iterator iter=points.iterator();
    while(iter.hasNext()) {
      Vec3D p = (Vec3D)iter.next();
      pushMatrix();
      translate(p.x,p.y,p.z);
      box(2);
      popMatrix();
    }
  }
  // show clipping sphere
  fill(0,30);
  translate(cursor.x,cursor.y,0);
  sphere(RADIUS);
  popMatrix();
  fill(0);
  text("total: "+numParticles,10,30);
  text("clipped: "+numClipped+" (time: "+dt+"ms)",10,50);
}

void keyPressed() {
  for(int i=0; i<NUM; i++) octree.addPoint(Vec3D.randomVector().scaleSelf(random(DIM2)));
  numParticles+=NUM;
}
