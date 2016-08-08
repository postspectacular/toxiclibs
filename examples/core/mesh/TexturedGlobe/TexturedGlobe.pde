import toxi.geom.*;
import toxi.geom.mesh2d.*;
import toxi.geom.mesh.*;
import toxi.processing.*;



float EARTH_RADIUS=300;
Vec2D HOME=new Vec2D(0,51);

TriangleMesh globe;
PImage earthTex;
float texUOffset=180;

ToxiclibsSupport gfx;

void setup() {
  size(1024, 768, P3D);
  gfx = new ToxiclibsSupport(this);
  earthTex = loadImage("earth_1024.jpg");
  globe = (TriangleMesh)new SurfaceMeshBuilder(new SphereFunction()).createMesh(null, 36, EARTH_RADIUS);
  globe.computeVertexNormals();
}

void draw() {
  background(255);
  lights();
  translate(width/2, height/2, 0);
  rotateX(mouseY*0.01);
  rotateY(mouseX*0.01);
  fill(255);
  gfx.origin(400);
  noStroke();
  textureMode(NORMAL);
  gfx.texturedMesh(globe, earthTex, true);
  fill(255, 0, 255);
  gfx.box(new AABB(toCartesianWithOffset(HOME), 4));
}

Vec3D toCartesianWithOffset(Vec2D v) {
  return new Vec3D(EARTH_RADIUS, radians(v.x+texUOffset), radians(v.y)).toCartesian();
}

