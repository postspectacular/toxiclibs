import toxi.geom.*;
import toxi.geom.mesh.*;
import toxi.processing.*;



Triangle3D tri;
TriangleMesh mesh;
ToxiclibsSupport gfx;

void setup() {
  size(400, 400, P3D);
  gfx=new ToxiclibsSupport(this);
  randomize();
}

void draw() {
  background(0);
  lights();
  translate(width/2, height/2, 0);
  rotateX(mouseY*0.01);
  rotateY(mouseX*0.01);
  // draw world space axes
  gfx.origin(300);
  // get triangle center and visualize normal vector
  Vec3D c=tri.computeCentroid();
  stroke(255, 0, 255);
  gfx.line(c, c.add(tri.computeNormal().scale(300)));
  noStroke();
  // draw triangle & mesh
  fill(255, 255, 0);
  gfx.triangle(tri);
  fill(0, 255, 255);
  gfx.mesh(mesh);
}

void randomize() {
  // create random triangle
  tri=new Triangle3D(
  Vec3D.randomVector().scale(100), 
  Vec3D.randomVector().scale(100), 
  Vec3D.randomVector().scale(100)
    );
  // create box mesh around origin
  mesh = (TriangleMesh)new AABB(50).toMesh();
    // get triangle normal
  Vec3D n=tri.computeNormal();
  // rotate mesh such that the +Z axis is aligned with the triangle normal
  mesh.pointTowards(n);
  // move box in the normal direction 100 units relative from the triangle center
  mesh.translate(tri.computeCentroid().add(n.scale(100)));
}

void keyPressed() {
  if (key=='r') randomize();
}
