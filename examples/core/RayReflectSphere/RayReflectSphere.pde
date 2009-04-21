/**
 * <p>Processing demo use case for various features of the Vec3D class and
 * the sphere intersector & reflector util of the toxi.geom package</p>
 * 
 * <p>Use the mouse to rotate view, click+drag to change incident ray direction
 * and see its reflection changing.</p>
 * 
 * <p>You can build your own reflectors by implementing the
 * toxi.geom.Reflector interface</p>
 */

/* 
 * Copyright (c) 2006-2008 Karsten Schmidt
 * 
 * This library is free software; you can redistribute it and/or
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

import processing.opengl.*;
import toxi.geom.*;

Vec3D pos=new Vec3D(0,200,0);  // ray start position/origin
Vec3D target=new Vec3D(0,0,0); // ray end point / target position
Vec3D dir=target.sub(pos).normalize(); // resulting ray direction

Vec3D sOrigin = new Vec3D(0,0,0); // centre of sphere
float sRadius= 100;  // sphere radius

// Create a new reflector
// The Reflector interface extends the Intersector interface
// That way you can choose to only implement a subset of features
// if no reflection is required
Reflector reflector=new SphereIntersectorReflector(sOrigin,sRadius);

// This vector holds the view rotation
Vec3D camRot = new Vec3D();

void setup() {
  size(500,500,OPENGL);
}

void draw() {
  background(255);
  noFill();
  translate(width/2,height/2,0);

  // update ray target
  if (mousePressed) {
    target.x=(width/2-mouseX)/(width*0.5)*sRadius*2;
    target.y=(height/2-mouseY)/(height*0.5)*sRadius*2;
    dir=target.sub(pos).normalize();
  } 
  else {
    // smoothly interpolate view (using linear interpolation)
    // Although is doesn't make sense in this particular context,
    // you can also use other forms of interpolation
    // by implementing an InterpolationStrategy
    camRot.interpolateToSelf(new Vec3D(mouseY*0.01,mouseX*0.01,0),0.05);
  }
  rotateX(camRot.x);
  rotateY(camRot.y);

  // compute the reflected ray direction
  Ray3D reflectedRay=reflector.reflectRay(new Ray3D(pos,dir));

  // does ray intersect sphere at all?
  if(reflectedRay!=null) {
    // get the intersection point
    Vec3D isectPos=reflector.getIntersectionPoint();

    // calc the mirrored point
    Vec3D posMirrored=reflectedRay.getPointAtDistance(reflector.getIntersectionDistance());

    // show the intersection point & sphere's normal vector at intersection
    Vec3D sphereNormal=reflector.getNormalAtIntersection();

    pushMatrix();
    stroke(0,255,0);
    translate(isectPos);
    box(5);
    beginShape(LINES);
    vertex(0,0,0);
    vertex(sphereNormal.scale(sRadius));
    endShape();
    popMatrix();

    // reflected point
    pushMatrix();
    translate(posMirrored);
    stroke(255,160,0);
    box(5);
    popMatrix();

    // reflected ray
    beginShape(LINES);
    stroke(255,0,0);
    vertex(isectPos);
    vertex(pos);
    stroke(255,160,0);
    vertex(isectPos);
    vertex(posMirrored);
    endShape();
  } 
  else {
    beginShape(LINES);
    stroke(255,0,0);
    vertex(pos);
    vertex(target);
    endShape();
  }

  // show sphere
  pushMatrix();
  stroke(0,20);
  translate(sOrigin);
  scale(sRadius);
  sphere(1);
  popMatrix();

  // ray origin
  stroke(255,0,0);
  pushMatrix();
  translate(pos);
  box(5);
  popMatrix();

  // ray target
  stroke(0,255,255);
  pushMatrix();
  translate(target);
  box(5);
  popMatrix();

}

// it's nicer to just work with vectors
void translate(Vec3D v) {
  translate(v.x, v.y, v.z);
}

void vertex(Vec3D v) {
  vertex(v.x, v.y, v.z);
}

