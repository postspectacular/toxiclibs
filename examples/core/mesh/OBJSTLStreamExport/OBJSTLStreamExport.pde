/**
 * <p>OBJSTLStreamExport demonstrates how to save a model as OBJ and STL formats
 * to a generic Java OutputStream, e.g. for saving models to a server backend.
 * This demo simply uses the SuperEllipsoid mesh builder class to generate a
 * rounded cube and then immediately exports the mesh as both OBJ and STL to files
 * inside the sketch folder.</p>
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
import java.io.FileOutputStream;

void setup() {
  // define a rounded cube using the SuperEllipsoid surface function
  SurfaceFunction functor=new SuperEllipsoid(0.3,0.3);
  SurfaceMeshBuilder b = new SurfaceMeshBuilder(functor);
  // execute the mesh (resolution=80, radius=100)
  TriangleMesh mesh = (TriangleMesh)b.createMesh(null, 80, 100);
  // attempt to create a FileOutputStream and save to it 
  try {
    String fileID="superellipsoid-"+(System.currentTimeMillis()/1000);
    FileOutputStream fs;
    fs=new FileOutputStream(sketchPath(fileID+".stl"));
    mesh.saveAsSTL(fs);
    fs=new FileOutputStream(sketchPath(fileID+".obj"));
    mesh.saveAsOBJ(fs);
  } 
  catch(Exception e) {
    e.printStackTrace();
  }
  exit();
}
