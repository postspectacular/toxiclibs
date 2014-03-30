/**
 * <p>Originally created during MAS CAAD workshop @ ETH Zurich, November 15-17, 2010
 * http://learn.postspectacular.com/Workshop:ETHZ2010</p>
 *
 * <p>Catenary voxel mesh exercise (updated version 2010-11-21).
 * This example is creating a number of joined catenary arc meshes using
 * the verlet physics engine with gravity behaviour. Furthermore, the
 * resulting joined mesh can be exported as STL model in two ways:
 * plain, without any post-processing or volumetric, in which case the
 * mesh is first transformed into a voxel space which is further manipulated
 * before creating the final export mesh.</p>
 *
 * <p><strong>Usage:</strong><ul>
 * <li>w: wireframe on/off</li>
 * <li>n: show normals on/off</li>
 * <li>r: reset mesh</li>
 * <li>u: simulation update on/off</li>
 * <li>space: export joined mesh as STL</li>
 * <li>v: export voxelized mesh as STL</li>
 * </ul></p>
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
 
import toxi.color.*;
import toxi.volume.*;
import toxi.processing.*;
import toxi.geom.*;
import toxi.geom.mesh.*;
import toxi.physics3d.*;
import toxi.physics3d.behaviors.*;

import java.util.List;

VerletPhysics3D physics;

int DIM=40;
int REST_LENGTH=7;
float STRENGTH = 0.9;

int normalLength;
boolean doUpdate=true;
boolean isWireframe=true;

ToxiclibsSupport gfx;

List<ParticleMesh> meshes=new ArrayList<ParticleMesh>();

void setup() {
  size(680,382,P3D);
  gfx=new ToxiclibsSupport(this);
  initPhysics();
}

void draw() {
  if (doUpdate) {
    physics.update();
  }
  background(255);
  noStroke();
  translate(width*0.5,height*0.66,0);
  rotateX(map(mouseY,0,height,-PI,PI));
  rotateY(map(mouseX,0,width,-PI,PI));
  lights();
  for(ParticleMesh m : meshes) {
    if (isWireframe) {
      stroke(m.col.toARGB());
      noFill();
    } else {
      fill(m.col.toARGB());
      noStroke();
    }
    m.buildMesh();
    gfx.mesh(m.mesh,true,normalLength);
  }
}

void keyPressed() {
  switch(key) {
  case ' ':
    TriangleMesh export=new TriangleMesh();
    for(ParticleMesh m : meshes) {
      export.addMesh(m.mesh);
    }
    export.saveAsSTL(sketchPath("catanary.stl"));
    break;
  case 'n':
    normalLength=(normalLength==0) ? 10 : 0;
    break;
  case 'u':
    doUpdate=!doUpdate;
    break;
  case 'v':
    saveVoxelized();
    break;
  case 'r':
    initPhysics();
    break;
  case 'w':
    isWireframe=!isWireframe;
    break;
  }
}

void initPhysics() {
  physics=new VerletPhysics3D();
  physics.addBehavior(new GravityBehavior3D(new Vec3D(0,0,0.5)));
  physics.setWorldBounds(new AABB(new Vec3D(0,0,0),500));
  
  meshes.clear();
  ParticleMesh m1 = new ParticleMesh(DIM,REST_LENGTH,STRENGTH,TColor.CYAN);
  meshes.add(m1);  
  ParticleMesh m2 = new ParticleMesh(DIM/2,REST_LENGTH,STRENGTH,TColor.GREEN);
  meshes.add(m2);
  ParticleMesh m3 = new ParticleMesh(DIM/2,REST_LENGTH,STRENGTH,TColor.YELLOW);
  meshes.add(m3);
  ParticleMesh m4 = new ParticleMesh(DIM/3,REST_LENGTH,STRENGTH,TColor.MAGENTA);
  meshes.add(m4);
  
  // calculate inset position for 2nd & 3rd meshes
  int x=(m1.gridSize-m2.gridSize)/2;
  int y=(m1.gridSize-m2.gridSize)/2;
  
  // join meshes to first one at the grid positions given
  // the joining is now using springs
  m2.joinMeshTo(m1,new Vec2D(x,y), m1, new Vec2D(DIM/2,y), m1, new Vec2D(DIM/2,DIM-1-y), m1, new Vec2D(x,DIM-1-y));
  m3.joinMeshTo(m1,new Vec2D(DIM/2,y), m1, new Vec2D(DIM-1-x,y), m1, new Vec2D(DIM-1-x,DIM-1-y), m1, new Vec2D(DIM/2,DIM-1-y));
  
  // the 4th mesh will be joined to both 2nd & 3rd mesh (2 points on each)
  x=m2.gridSize/2;
  y=m2.gridSize/4;
  m4.joinMeshTo(m2,new Vec2D(x-1,y), m3, new Vec2D(x,y), m3, new Vec2D(x,m3.gridSize-1-y), m2, new Vec2D(x-1,m2.gridSize-1-y));
  
  // pin corners of 1st mesh in space
  m1.getParticleAt(new Vec2D(0,0)).lock();
  m1.getParticleAt(new Vec2D(DIM-1,0)).lock();
  m1.getParticleAt(new Vec2D(DIM-1,DIM-1)).lock();
  m1.getParticleAt(new Vec2D(0,DIM-1)).lock();
  m1.getParticleAt(new Vec2D(DIM/2,DIM/2)).lock();
}
