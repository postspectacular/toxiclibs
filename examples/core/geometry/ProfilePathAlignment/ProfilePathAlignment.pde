/**
 * <p>In this example a 2D polygon profile is being swept along a
 * 3D spline path and aligned to the path direction using quaternions.
 * The example demonstrates both the usage of the alignment quaternion
 * in combination with a 4x4 transformation matrix, as well as the use
 * of the toAxisAngle() method to compute a rotation axis from a quat.
 * </p>
 *
 * <p><strong>Usage:</strong><ul>
 * <li>l: toggle between line/dot rendering</li>
 * <li>r: restart animation</li>
 * </ul></p>
 */

/* 
 * Copyright (c) 2011 Karsten Schmidt
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
import toxi.processing.*;
import java.util.List;

ToxiclibsSupport gfx;

Polygon2D profile=new Circle(40).toPolygon2D(60);

List<Vec3D> path;
ArrayList<Vec3D> tubeVertices=new ArrayList<Vec3D>();

int pathID=0;
boolean doRenderPoints;

void setup() {
  size(680,382,P3D);
  gfx=new ToxiclibsSupport(this);
  // compute spiral key points (every 45 degrees)
  ArrayList points = new ArrayList();
  for (float theta=-TWO_PI, r=100; theta<8*TWO_PI; theta+=QUARTER_PI) {
    Vec3D p=Vec3D.fromXYTheta(theta).scale(r).add(200,0,0).rotateY(theta/9);
    points.add(p);
  }
  // use points to compute a spline and sample at regular interval
  Spline3D s=new Spline3D(points);
  path=s.toLineStrip3D(10).getDecimatedVertices(4);
}

void draw() {
  background(51);
  lights();
  translate(width / 2, height / 2, 0);
  rotateX(mouseY * 0.01f);
  rotateY(mouseX * 0.01f);
  noStroke();
  gfx.origin(300);
  stroke(255,0,255);
  gfx.lineStrip3D(path);
  if (pathID<path.size()-1) {
    // compute current curve direction
    Vec3D dir=path.get(pathID+1).sub(path.get(pathID)).normalize();
    // calculate alignment orientation for direction
    // our profile shape is in XY plane (2D) and
    // so its "forward" direction is the positive Z axis
    Quaternion alignment=Quaternion.getAlignmentQuat(dir,Vec3D.Z_AXIS);
    // construct a matrix to move shape to current curve position
    Matrix4x4 mat=new Matrix4x4().translateSelf(path.get(pathID));
    // then combine with alignment matrix
    mat.multiplySelf(alignment.toMatrix4x4());
    // then apply matrix to (copies of) all profile shape vertices
    // and append them to global vertex list
    for(Vec2D p : profile) {
      tubeVertices.add(mat.applyToSelf(p.to3DXY()));
    }
    pathID++;
  }
  stroke(255);
  if (!doRenderPoints) {
    gfx.points3D(tubeVertices);
  } else {
    gfx.lineStrip3D(tubeVertices);
  }
  
  // draw coordinate system for current spline direction
  int id=constrain(pathID-2,0,path.size()-2);
  // current curve direction
  Vec3D dir=path.get(id+1).sub(path.get(id)).normalize();
  // compute rotation axis and angle
  float[] axis=Quaternion.getAlignmentQuat(dir,Vec3D.Z_AXIS).toAxisAngle();
  // move to curr/last curve point
  gfx.translate(path.get(id+1));
  // rotate around computed axis
  rotate(axis[0],axis[1],axis[2],axis[3]);
  // draw rotated coordinate system
  gfx.origin(new Vec3D(),100);
}

void keyPressed() {
  if (key=='r') {
    tubeVertices.clear();
    pathID=0;
  }
  if (key=='l') {
    doRenderPoints=!doRenderPoints;
  }
}
