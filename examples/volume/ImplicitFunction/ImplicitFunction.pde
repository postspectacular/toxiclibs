/**
 * <p>This example implements a custom VolumetricSpace using an implicit function
 * to calculate each voxel. This is slower than the default array or HashMap
 * based implementations, but also has much less memory requirements and so might
 * be an interesting and more viable approach for very highres voxel spaces
 * (e.g. >32 million voxels). This implementation here also demonstrates how to
 * achieve an upper boundary on the iso value (in addition to the one given and
 * acting as lower threshold when computing the iso surface).</p>
 *
 * <p><strong>Usage:</strong><ul>
 * <li>move mouse to rotate camera</li>
 * <li>w: toggle wireframe on/off</li>
 * <li>-/=: zoom in/out</li>
 * <li>l: apply laplacian mesh smooth</li>
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

import processing.opengl.*;

import toxi.geom.*;
import toxi.geom.mesh.*;
import toxi.math.*;
import toxi.volume.*;
import toxi.processing.*;

int RES = 64;
float ISO = 0.2;
float MAX_ISO=0.66;

WETriangleMesh mesh;

ToxiclibsSupport gfx;
float currZoom = 1;
boolean isWireframe;

void setup() {
  size(1280,720, OPENGL);
  gfx = new ToxiclibsSupport(this);
  VolumetricSpace vol = new EvaluatingVolume(new Vec3D(400,400,400), RES, MAX_ISO);
  IsoSurface surface = new HashIsoSurface(vol);
  mesh = new WETriangleMesh();
  surface.computeSurfaceMesh(mesh, ISO);
}

void draw() {
  background(0);
  translate(width / 2, height / 2, 0);
  rotateX(mouseY * 0.01f);
  rotateY(mouseX * 0.01f);
  scale(currZoom);
  if (isWireframe) {
    noFill();
    stroke(255);
  } 
  else {
    fill(255);
    noStroke();
    lights();
  }
  gfx.mesh(mesh, true);
}

void keyPressed() {
  if (key == 'w') {
    isWireframe = !isWireframe;
  }
  if (key == '-') {
    currZoom -= 0.1;
  }
  if (key == '=') {
    currZoom += 0.1;
  }
  if (key == 'l') {
    new LaplacianSmooth().filter(mesh, 1);
  }
}

