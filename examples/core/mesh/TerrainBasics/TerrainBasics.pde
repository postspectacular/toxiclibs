import toxi.geom.*;
import toxi.geom.mesh.*;
import toxi.processing.*;

int DIM=20;
float NOISE_SCALE=0.15;

TriangleMesh mesh;
ToxiclibsSupport gfx;

void setup() {
  size(640, 480, P3D);
  Terrain terrain = new Terrain(DIM, DIM, 16);
  // populate elevation data
  float[] el = new float[DIM*DIM];
  noiseSeed(23);
  for (int z = 0, i = 0; z < DIM; z++) {
    for (int x = 0; x < DIM; x++) {
      el[i++] = noise(x * NOISE_SCALE, z * NOISE_SCALE) * 100;
    }
  }
  terrain.setElevation(el);
  // create mesh
  mesh = new TriangleMesh();
  terrain.toMesh(mesh,-10);
  // attach drawing utils
  gfx = new ToxiclibsSupport(this);
}

void draw() {
  background(0);
  lights();
  translate(width/2,height/2,0);
  rotateX(mouseY*0.01);
  rotateY(mouseX*0.01);
  noStroke();
  gfx.mesh(mesh);
}
