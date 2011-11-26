/**
 * This demo uses a custom attraction behavior to deform a 3D grid plane mesh.
 * The mesh itself is linked to a VerletPhysics simulation to realize the
 * deformation.
 *
 * (c) 2011 Karsten Schmidt // LGPLv2 licensed
 */

import toxi.geom.*;
import toxi.geom.mesh.*;
import toxi.physics3d.*;
import toxi.physics3d.behaviors.*;
import toxi.processing.*;

// grid size
int RESX=50;
int RESZ=30;
int SCALE=10;

// attractor settings
float ATT_RADIUS=20;
float ATT_ELEVATION=50;

VerletPhysics3D phys;
ToxiclibsSupport gfx;
WETriangleMesh mesh;

AttractionBehavior3D attractor;

// mesh render flags
boolean isSmooth=true;
boolean isWireframe=false;

void setup() {
  size(680, 382, P3D);
  phys=new VerletPhysics3D();
  // create & attach a custom attraction behavior (see below)
  attractor=new YAxisAttractor(new Vec3D(0, ATT_ELEVATION, 0), ATT_RADIUS, 4, 0.01);
  phys.addBehavior(attractor);
  gfx=new ToxiclibsSupport(this);
  // create grid mesh in XZ plane
  mesh=new WETriangleMesh();
  for (int z=0; z<RESZ; z++) {
    for (int x=0; x<RESX; x++) {
      mesh.addFace(new Vec3D(x-1, 0, z-1), new Vec3D(x, 0, z-1), new Vec3D(x, 0, z));
      mesh.addFace(new Vec3D(x-1, 0, z-1), new Vec3D(x, 0, z), new Vec3D(x-1, 0, z));
    }
  }
  // center & scale mesh
  mesh.center(null);
  mesh.scale(SCALE);
  // create a physics particle for each mesh vertex
  for (Vec3D v : mesh.getVertices()) {
    phys.addParticle(new VerletParticle3D(v));
  }
  // lock the 4 corners of the grid plane
  Vec3D min=mesh.getBoundingBox().getMin();
  Vec3D max=mesh.getBoundingBox().getMax();
  phys.particles.get(mesh.getClosestVertexToPoint(min).id).lock();
  phys.particles.get(mesh.getClosestVertexToPoint(max).id).lock();
  phys.particles.get(mesh.getClosestVertexToPoint(new Vec3D(max.x, min.y, min.z)).id).lock();
  phys.particles.get(mesh.getClosestVertexToPoint(new Vec3D(min.x, max.y, max.z)).id).lock();
  // connect all particles by creating a spring for each edge in the mesh
  for (WingedEdge e : mesh.getEdges()) {
    VerletParticle3D a=phys.particles.get(((WEVertex)e.a).id);
    VerletParticle3D b=phys.particles.get(((WEVertex)e.b).id);
    phys.addSpring(new VerletSpring3D(a, b, a.distanceTo(b), 0.1));
  }
}

void draw() {
  // update attractor position
  float x=map(mouseX, 0, width, -0.5, 0.5)*SCALE*RESX;
  float z=map(mouseY, 0, height, 0.5, -0.5)*SCALE*RESZ;
  attractor.getAttractor().set(x, ATT_ELEVATION, z);
  // update physics
  phys.update();
  // update mesh vertices by moving them to the current position
  // of their associated particles
  for (int i=0, num=phys.particles.size(); i<num; i++) {
    mesh.getVertexForID(i).set(phys.particles.get(i));
  }
  // update mesh normals
  mesh.computeFaceNormals();
  if (isSmooth) {
    mesh.computeVertexNormals();
  }

  background(230, 248, 255);
  lights();
  directionalLight(255, 255, 255, 500, 1000, -100);
  shininess(2);
  translate(width/2, height/2, 0);
  rotateX(PI*0.66);
  rotateY(PI*0.15);
  gfx.origin(300);
  // draw mesh
  if (isWireframe) {
    stroke(80, 0, 96);
    noFill();
  } 
  else {
    fill(80, 0, 96);
    noStroke();
  }
  gfx.mesh(mesh, isSmooth);
  // draw attractor
  stroke(255, 255, 0);
  noFill();
  gfx.sphere(new Sphere(attractor.getAttractor(), attractor.getRadius()), 10);
}

void keyPressed() {
  // toggle smooth shading
  if (key=='s') {
    isSmooth=!isSmooth;
  }
  if (key=='w') {
    isWireframe=!isWireframe;
  }
}

// custom attractor only taking distance in XZ plane into account
// (everything else inherited from standard AttractionBehavior)
class YAxisAttractor extends AttractionBehavior3D {

  public YAxisAttractor(Vec3D attractor, float radius, float strength, float jitter) {
    super(attractor, radius, strength, jitter);
  }

  public void apply(VerletParticle3D p) {
    // compute 2D distance in XZ plane
    Vec2D delta = attractor.to2DXZ().sub(p.to2DXZ());
    float dist = delta.magSquared();
    if (dist < radiusSquared) {
      // compute attraction force in 3D
      Vec3D f = attractor.sub(p).normalizeTo((1.0f - dist / radiusSquared))
        .jitter(jitter).scaleSelf(attrStrength);
      p.addForce(f);
    }
  }
}

