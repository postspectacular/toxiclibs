/**
 * <p>
 * This example demonstrates the construction of a simple bezier patch surface
 * and how to obtain its polygon mesh representation at different resolutions.
 * </p>
 *
 * <p>Further information:
 * http://en.wikipedia.org/wiki/Bezier_patch
 * </p>
 *
 * <p><strong>Usage:</strong><ul>
 * <li>move mouse to rotate camera</li>
 * <li>hold down mouse button to view low res version</li>
 * <li>press 'w' to toggle wireframe on/off
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
import toxi.math.noise.*;

import toxi.processing.*;

float NS = 0.05f;
float SIZE = 100;
float AMP = SIZE*4;

Mesh3D mesh;
ToxiclibsSupport gfx;

boolean isWireframe=true;

void setup() {
  size(680, 382, P3D);
  gfx = new ToxiclibsSupport(this);
}

void draw() {
  updateMesh();
  background(0);
  translate(width / 2, height / 2, 0);
  float rx = mouseY * 0.01f;
  float ry = mouseX * 0.01f;
  rotateX(rx);
  rotateY(ry);
  gfx.origin(100);
  if (isWireframe) {
    noFill();
    stroke(255);
  } 
  else {
    fill(255);
    noStroke();
    lights();
  }
  gfx.mesh(mesh,!isWireframe);
}

void updateMesh() {
  float phase=frameCount*NS*0.1;
  BezierPatch patch = new BezierPatch();
  for (int y = 0; y < 4; y++) {
    for (int x = 0; x < 4; x++) {
      float xx = x * SIZE;
      float yy = y * SIZE;
      float zz = (float) (SimplexNoise.noise(xx * NS, yy * NS,phase) * AMP);
      patch.set(x, y, new Vec3D(xx, yy, zz));
    }
  }
  mesh=patch.toMesh(!mousePressed ? 32 : 4);
  mesh.center(null);
}

void keyPressed() {
  if (key == 'w') {
    isWireframe = !isWireframe;
  }
}

