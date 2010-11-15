/**
 * <p>An adaptation of the SoftBodySquare demo to create a 3D cloth in a
 * space with spherical obstacles and a solid ground box. This example was
 * created during the Kurye Video Festival workshop on 2010-05-15 in Istanbul.</p>
 *
 * <p><strong>Usage:</strong>
 * <ul>
 * <li>r: reset simulation</li>
 * <li>g: turn gouraud shading on/off</li>
 * <li>s: turn sphere display on/off</li>
 * <li>x: export current mesh as STL (only offline)</li>
 * </ul>
 * </p>
 */

/* 
 * Copyright (c) 2010 Karsten Schmidt
 * 
 * This demo & library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * http://creativecommons.org/licenses/LGPL/2.1/
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

import toxi.geom.*;
import toxi.geom.mesh.*;
import toxi.math.*;
import toxi.physics.*;
import toxi.physics.behaviors.*;
import toxi.physics.constraints.*;
import toxi.processing.*;

int DIM=30;
int REST_LENGTH=20;
float STRENGTH=1;

VerletPhysics physics;
ToxiclibsSupport gfx;
TriangleMesh mesh;
BoxConstraint ground;
ArrayList spheres=new ArrayList();

boolean isGouraudShaded=true;
boolean showSpheres=false;

void setup() {
  size(680,382,P3D);
  gfx=new ToxiclibsSupport(this);
  sphereDetail(8);
  initPhysics();
}

void draw() {
  background(0);
  lights();
  directionalLight(255,255,255,-500,200,300);
  specular(255);
  shininess(32);
  translate(width/2,height/2,0);
  rotateY(PI+frameCount*0.01);
  scale(0.66);
  // update simulation
  physics.update();
  // update cloth mesh
  updateMesh();
  // draw mesh either flat shaded or smooth
  fill(255,160,0);
  noStroke();
  gfx.mesh(mesh,isGouraudShaded);
  // draw ground box
  fill(128);
  gfx.box(ground.getBox());
  if (showSpheres) {
    fill(255);
    for(Iterator<SphereConstraint> i=spheres.iterator(); i.hasNext();) {
      // create a copy of the sphere and reduce its radius
      // in order to avoid rendering artifacts
      Sphere s=new Sphere(i.next().sphere);
      s.radius*=0.99;
      gfx.sphere(s,12);
    }
  }
}

void keyPressed() {
  if (key=='x') {
    mesh.saveAsSTL(sketchPath("cloth.stl"));
  }
  if (key=='g') {
    isGouraudShaded=!isGouraudShaded;
  }
  if (key=='s') {
    showSpheres=!showSpheres;
  }
  if (key=='r') {
    initPhysics();
  }
}

// iterates over all particles in the grid order
// they were created and constructs triangles
void updateMesh() {
  mesh=new TriangleMesh();
  for(int y=0; y<DIM-1; y++) {
    for(int x=0; x<DIM-1; x++) {
      int i=y*DIM+x;
      VerletParticle a=physics.particles.get(i);
      VerletParticle b=physics.particles.get(i+1);
      VerletParticle c=physics.particles.get(i+1+DIM);
      VerletParticle d=physics.particles.get(i+DIM);
      mesh.addFace(a,d,c);
      mesh.addFace(a,c,b);
    }
  }
}

