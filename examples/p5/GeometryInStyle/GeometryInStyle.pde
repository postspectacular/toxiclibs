import toxi.geom.*;
import toxi.geom.mesh.*;

import toxi.processing.*;

ToxiclibsSupport gfx;

void setup() {
  size(600,600,P3D);
  gfx=new ToxiclibsSupport(this);
}

void draw() {
  AABB cube;
  AxisAlignedCylinder cyl;
  Cone cone;
  Sphere ball;
  TriangleMesh mesh;
  
  background(0);
  lights();
  translate(width/2,height/2,0);
  rotateX(mouseY*0.01);
  rotateY(mouseX*0.01);
  noStroke();
  
  cone=new Cone(new Vec3D(0,-50,0), new Vec3D(0,1,0), 50, 100, 50);
  gfx.cone(cone,20,false);
  cone=new Cone(new Vec3D(0,50,0), new Vec3D(0,-1,0), 50, 100, 50);
  gfx.cone(cone,20,false);
  
  cyl=new XAxisCylinder(new Vec3D(200,0,0),20,100);
  gfx.cylinder(cyl,3,false);
  
  SurfaceFunction f=new SuperEllipsoid(0.3,0.3);
  mesh=new SurfaceMeshBuilder(f).createMesh(40,50);
  mesh.transform(new Matrix4x4().translate(0,0,200));
  gfx.mesh(mesh,true,20);
  
  cube=new AABB(new Vec3D(0,0,-200),new Vec3D(50,50,50));
  gfx.box(cube);
  
  ball=new Sphere(new Vec3D(-200,0,0),50);
  gfx.sphere(ball);
}


