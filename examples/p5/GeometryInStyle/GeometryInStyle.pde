/**
 * <p>The ToxiclibsSupport class of the toxi.processing package provides various
 * shortcuts to directly use toxiclibs geometry datatypes with Processing style
 * drawing operations. Most of these are demonstrated in this example.</p>
 *
 * <p>UPDATES:
 * <ul>
 * <li>2010-12-30: added sphere/cylinder resolution modulation</li>
 * </ul></p>
 */
 
/* 
 * Copyright (c) 2010 Karsten Schmidt
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
import toxi.geom.mesh.*;
import toxi.math.waves.*;
import toxi.processing.*;

import processing.opengl.*;

ToxiclibsSupport gfx;

AbstractWave sphereRes=new SineWave(0,0.02,15,18);

void setup() {
  size(680,382,OPENGL);
  gfx=new ToxiclibsSupport(this);
}

void draw() {
  AABB cube;
  AxisAlignedCylinder cyl;
  Cone cone,cone2;
  Sphere ball;
  TriangleMesh mesh;

  int res=(int)sphereRes.update();
  
  background(0);
  lights();
  translate(width/2,height/2,0);
  rotateX(mouseY*0.01);
  rotateY(mouseX*0.01);
  noStroke();

  cone=new Cone(new Vec3D(0,-50,0), new Vec3D(0,1,0), 50, 100, 50);
  gfx.cone(cone,20,false);
  cone2=new Cone(new Vec3D(0,50,0), new Vec3D(0,-1,0), 50, 100, 50);
  gfx.cone(cone2,20,true);

  cyl=new XAxisCylinder(new Vec3D(200,0,0),20,100);
  gfx.cylinder(cyl,res,false);

  SurfaceFunction f=new SuperEllipsoid(0.3,0.3);
  mesh=(TriangleMesh)new SurfaceMeshBuilder(f).createMesh(null,40,50);
  mesh.computeVertexNormals();
  mesh.transform(new Matrix4x4().translate(0,0,200));
  gfx.mesh(mesh,true,10);

  cube=new AABB(new Vec3D(0,0,-200),new Vec3D(50,50,50));
  gfx.box(cube);

  ball=new Sphere(new Vec3D(-200,0,0),50);
  gfx.sphere(ball,res);
  
  stroke(255,255,0);
  Ray3D ray=new Ray3D(new Vec3D(),Vec3D.Y_AXIS);
  gfx.ray(ray,200);
  
  stroke(0,255,0);
  gfx.line(new Vec3D(),cube);
  
  stroke(255,0,255);
  Line3D line=new Line3D(new Vec3D(),ball);
  gfx.points3D(line.splitIntoSegments(null,10,true));
  
  stroke(0,255,255);
  Spline3D spline=new Spline3D();
  spline.add(cube).add(ball).add(cone).add(cyl.getPosition()).add(mesh.computeCentroid());
  gfx.lineStrip3D(spline.computeVertices(16));
}

