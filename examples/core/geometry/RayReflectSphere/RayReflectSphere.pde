/**
 * <p>Processing demo use case for various features of the Vec3D class and
 * the sphere intersector & reflector util of the toxi.geom package.
 * The demo also makes use of the ToxiclibsSupport class from the toxi.processing
 * package which is available separately.</p>
 * 
 * <p>Use the mouse to rotate view, click+drag to change incident ray direction
 * and see its reflection changing.</p>
 * 
 * <p>You can build your own reflectors by implementing the
 * toxi.geom.Reflector interface</p>
 */

/* 
 * Copyright (c) 2006 Karsten Schmidt
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


import toxi.geom.*;
import toxi.processing.*;

Vec3D pos=new Vec3D(0,200,0);  // ray start position/origin
Vec3D target=new Vec3D(0,0,0); // ray end point / target position
Vec3D dir=target.sub(pos).normalize(); // resulting ray direction

Vec3D sOrigin = new Vec3D(0,0,0); // centre of sphere
float sRadius= 100;  // sphere radius

// Create a new reflector
// The Reflector interface extends the Intersector interface
// That way you can choose to only implement a subset of features
// if no reflection is required
SphereIntersectorReflector reflector=new SphereIntersectorReflector(sOrigin,sRadius);

// This vector holds the view rotation
Vec3D camRot = new Vec3D();

ToxiclibsSupport gfx;

void setup() {
  size(500,500,P3D);
  gfx=new ToxiclibsSupport(this);
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
    IsectData3D isec=reflector.getIntersectionData();
    // get the intersection point
    ReadonlyVec3D isectPos=isec.pos;
    // calc the mirrored point
    Vec3D posMirrored=reflectedRay.getPointAtDistance(isec.dist);
    // show the intersection point & sphere's normal vector at intersection
    ReadonlyVec3D sphereNormal=isec.normal;

    stroke(0,255,0);
    gfx.box(new AABB(isectPos,2));
    gfx.line(new Line3D(new Vec3D(),isectPos));

    // reflected point
    stroke(255,160,0);        
    gfx.box(new AABB(posMirrored,2));

    // reflected ray
    stroke(255,0,0);
    gfx.line(pos,isectPos);
    stroke(255,160,0);
    gfx.line(isectPos,posMirrored);
  } 
  else {
    stroke(255,0,0);
    gfx.line(pos,target);
  }

  // show sphere
  stroke(0,20);
  gfx.sphere(reflector.getSphere(),20);

  // ray origin
  stroke(255,0,0);
  gfx.box(new AABB(pos,2));

  // ray target
  stroke(0,255,255);
  gfx.box(new AABB(target,2));
}
