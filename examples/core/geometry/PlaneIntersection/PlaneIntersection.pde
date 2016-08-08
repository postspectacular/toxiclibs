import toxi.geom.*;
import toxi.util.*;
import toxi.processing.*;


Plane p1, p2;

ToxiclibsSupport gfx;
boolean doSave;

void setup() {
  size(1280, 720, P3D);
  gfx = new ToxiclibsSupport(this);
  p1 = new Plane(new Vec3D(100, 100, 100), new Vec3D(0, 1, 0));
  p2 = new Plane(new Vec3D(100, 200, 100), Vec3D.randomVector());
}


void draw() {
  background(255);
  lights();
  strokeWeight(1);
  translate(width / 2, height / 2);
  rotateX(mouseY * 0.01f);
  rotateY(mouseX * 0.01f);
  gfx.origin(300);
  stroke(0);
  fill(128);
  gfx.plane(p1, 300);
  fill(192);
  gfx.plane(p2, 300);
  stroke(255, 0, 255);
  strokeWeight(2);
  Ray3D ray = p1.intersectsPlane(p2);
  if (ray != null) {
    gfx.ray(ray, 300);
    gfx.ray(ray, -300);
  }
  if (doSave) {
    saveFrame("PlaneIsecTest-" + DateUtils.timeStamp() + ".png");
    doSave = false;
  }
}

void keyPressed() {
  switch (key) {
  case ' ':
    doSave = true;
    break;
  case 'r':
    p2.normal = Vec3D.randomVector();
    break;
  }
}


