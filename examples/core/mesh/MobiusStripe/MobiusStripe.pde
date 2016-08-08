/**
 * <p>Developable surface follow-up example for MasCAAD ETH Zurich workshop.
 * In this demo we construct a mobius strip with a flexible number of twists.</p>
 *
 * <p><strong>Usage:</strong>
 * <ul>
 * <li>0 - 9 : set number of twists</li>
 * <li>w : toggle wireframe on/off</li>
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
import toxi.processing.*;



// radius of mobius strip
float BASE_RADIUS=150;
// width of strip mesh
float STRIP_WIDTH=100;
// number of twists (a classic mobius has twist=1)
int TWIST=3;
// number of steps/mesh resolution 
int RES=300;

WETriangleMesh stripe;
ToxiclibsSupport gfx;

// rendering vars
float currZoom=1;
boolean isWireframe=false;

void setup() {
  size(680,382,P3D);
  stripe=buildStripe(TWIST);
  //stripe.saveAsSTL(sketchPath("mobius.stl"));
  gfx=new ToxiclibsSupport(this);
}

void draw() {
  background(255);
  lights();
  translate(width/2,height/2,0);
  rotateX(mouseY*0.01);
  rotateY(mouseX*0.01);
  scale(currZoom);
  if (!isWireframe) {
    fill(192);
    noStroke();
  } 
  else {
    noFill();
    stroke(0);
  }
  gfx.mesh(stripe,true);
}

// this function is building the mobius strip mesh
WETriangleMesh buildStripe(int twist) {
  WETriangleMesh mesh=new WETriangleMesh();
  float delta=TWO_PI/RES;
  float rotDelta=twist*PI/RES;
  Vec3D prevA=null,prevB=null;
  for(int i=0; i<=RES; i++) {
    // compute point on circle in XZ plane
    Vec3D p=Vec2D.fromTheta(i*delta).scaleSelf(BASE_RADIUS).to3DXZ();
    // optional y modulation
    // p.y=sin(i*3*delta)*100;
    // compute surface direction at this point
    Vec3D dir=Vec2D.fromTheta(i*rotDelta).scaleSelf(STRIP_WIDTH/2).to3DXY().rotateY(i*delta);
    // calculate 2 points on current surface direction, relative to P
    Vec3D a = p.sub(dir);
    Vec3D b = p.add(dir);
    // build faces for all iterations > 0
    if (i>0) {
      mesh.addFace(prevA,a,prevB);
      mesh.addFace(a,b,prevB);
    }
    prevA=a;
    prevB=b;
  }
  mesh.computeVertexNormals();
  return mesh;
}

void keyPressed() {
  if (key=='w') {
    isWireframe=!isWireframe;
  }
  if (key>='0' && key<='9') {
    stripe=buildStripe(key-'0');
  }
}

