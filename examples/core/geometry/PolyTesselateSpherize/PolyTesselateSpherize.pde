/**
 * This example demonstrates how to map a 2D polygon onto a the surface of a sphere
 * using purely geometry (no texture mapping). The process involves tesselating the polygon
 * using Delaunay triangulation, for which additional points within the polygon are added.
 * The resulting 2D triangles are then transferred onto the sphere and connected into a
 * 3D mesh. The demo also has some key controls to adjust rendering modes and resolution to
 * observe the effect of the additional inside points.
 *
 * Usage:
 *
 * 0-9: adjust sample grid resolution
 * r: randomize position of polygon on sphere
 * m: toggle between mesh display (3D) and flat 2D rendering
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
import toxi.geom.mesh2d.*;
import toxi.geom.mesh.*;
import toxi.processing.*;
import toxi.math.*;
import java.util.List;



// radius of the root delaunay triangle which encompasses (MUST) all other points
float DELAUNAY_SIZE = 10000;
// sphere radius
float EARTH_RADIUS=300;

ToxiclibsSupport gfx;

// Perimeter polygon
Polygon2D poly;

// list of 2D triangles (intermediate result of Delaunay triangulation)
List<Triangle2D> tesselated;

// 3D mesh
TriangleMesh mesh;

// grid resolution for adding inliers
float res=6;

void setup() {
  size(1024, 768, P3D);
  smooth();
  gfx = new ToxiclibsSupport(this);

  // create a regular polygon by sampling a circle
  poly=new Circle(60).toPolygon2D(20);
  tesselated=tesselatePolygon(poly, res);
}

void draw() {
  background(255);
  // switch between 2D/3D modes
  if (mesh==null) {
    translate(width/2, height/2);
    noFill();
    // draw original poly
    stroke(255, 0, 0);
    gfx.polygon2D(poly);
    // then tesselated version to compare
    stroke(0, 0, 255);
    beginShape(TRIANGLES);
    for (Triangle2D t : tesselated) {
      gfx.triangle(t);
    }  
    endShape();
  } 
  else {
    lights();
    translate(width/2, height/2, 0);
    rotateX(mouseY*0.01);
    rotateY(mouseX*0.01);
    fill(255);
    noStroke();
    sphere(EARTH_RADIUS*0.99);
    fill(255,0,64);
    stroke(0);
    gfx.mesh(mesh, true, 20);
  }
}

void keyPressed() {
  if (key>='0' && key<='9') {
    res=map((key-'0'),0,9,30,4);
    println("res: "+res);
    // resample polygon with new resolution
    tesselated=tesselatePolygon(poly, res);
    if (mesh!=null) {
      mesh=buildSurfaceMesh(tesselated, EARTH_RADIUS);
    }
  }
  if (key=='r') {
    // randomize position of polygon (coords are in lon/lat space)
    poly=new Circle(new Vec2D(random(-40, 40), random(-30, 30)), 60).toPolygon2D(20);
    tesselated=tesselatePolygon(poly, res);
    // update mesh, if needed...
    if (mesh!=null) {
      mesh=buildSurfaceMesh(tesselated, EARTH_RADIUS);
    }
  }
  if (key=='m') {
    // toggle between 2d/3d
    if (mesh==null) {
      mesh=buildSurfaceMesh(tesselated, EARTH_RADIUS);
    } 
    else {
      mesh=null;
    }
  }
}

// tesselates polygon with additional/optional inliers
List<Triangle2D> tesselatePolygon(Polygon2D poly, float res) {
  List<Triangle2D> result=new ArrayList<Triangle2D>();
  // a Voronoi diagram relies on a Delaunay triangulation behind the scenes
  Voronoi voronoi = new Voronoi(DELAUNAY_SIZE);
  // add perimeter points
  for (Vec2D v : poly.vertices) {
    voronoi.addPoint(v);
  }
  // add random inliers
  for (Vec2D v : createInsidePoints(poly,res)) {
    voronoi.addPoint(v);
  }
  // get filtered delaunay triangles:
  // ignore any triangles which share a vertex with the initial root triangle
  // or whose centroid is outside the polygon
  for (Triangle2D t : voronoi.getTriangles()) {
    if (abs(t.a.x)!=DELAUNAY_SIZE && abs(t.a.y)!=DELAUNAY_SIZE) {
      if (poly.containsPoint(t.computeCentroid())) {
        result.add(t);
      }
    }
  }
  return result;
}

// compute bounding rect of a polygon
// (will be unnecessary from toxiclibs-0021 onwards)
Rect computeBounds(Polygon2D poly) {
  Vec2D min=Vec2D.MAX_VALUE.copy();
  Vec2D max=Vec2D.MIN_VALUE.copy();
  for (Vec2D v : poly.vertices) {
    min.minSelf(v);
    max.maxSelf(v);
  }
  return new Rect(min, max);
}

// create list of grid points within polygon
List<Vec2D> createInsidePoints(Polygon2D poly, float res) {
  List<Vec2D> points=new ArrayList<Vec2D>();
  Rect bounds=computeBounds(poly);
  for (float y=bounds.y; y<bounds.getBottom(); y+=res) {
    float yy=MathUtils.roundTo(y,res);
    for (float x=bounds.x; x<bounds.getRight(); x+=res) {
      Vec2D p=new Vec2D(MathUtils.roundTo(x,res), yy);
      if (poly.containsPoint(p)) {
        points.add(p);
      }
    }
  }
  return points;
}

// converts a list of 2D triangles into a 3D mesh on a sphere
TriangleMesh buildSurfaceMesh(List<Triangle2D> tris, float radius) {
  TriangleMesh mesh=new TriangleMesh();
  for (Triangle2D t : tris) {
    // ensure all triangles have same orientation
    if (!t.isClockwise()) {
      t.flipVertexOrder();
    }
    // lat/lon => spherical => cartesian mapping
    Vec3D a = new Vec3D(radius, radians(t.a.x), radians(t.a.y)).toCartesian();
    Vec3D b = new Vec3D(radius, radians(t.b.x), radians(t.b.y)).toCartesian();
    Vec3D c = new Vec3D(radius, radians(t.c.x), radians(t.c.y)).toCartesian();
    // add 3D triangle to mesh
    mesh.addFace(a, b, c);
  }
  // needed for smooth shading
  mesh.computeVertexNormals();
  return mesh;
}
