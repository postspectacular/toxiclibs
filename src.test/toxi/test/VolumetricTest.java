package toxi.test;

/**
 * NoiseSurface demo showing how to utilize the IsoSurface class to efficiently
 * visualise volumetric data, in this case using 3D SimplexNoise. The demo also
 * shows how to save the generated mesh as binary STL file (or alternatively in
 * OBJ format) for later use in other 3D tools/digital fabrication.
 * 
 * I've planned further classes for the toxi.geom.volume package to easier draw
 * and manipulate volumetric data.
 * 
 * Controls:
 * Click mouse button to toggle rendering style between shaded/wireframe.
 * Press 's' to save generated mesh as STL file
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
import processing.core.PApplet;
import toxi.geom.Vec3D;
import toxi.geom.mesh.Face;
import toxi.geom.mesh.TriangleMesh;
import toxi.math.noise.SimplexNoise;
import toxi.volume.HashIsoSurface;
import toxi.volume.IsoSurface;
import toxi.volume.VolumetricSpace;
import toxi.volume.VolumetricSpaceArray;

public class VolumetricTest extends PApplet {

    public static void main(String[] args) {
        PApplet.main(new String[] { "toxi.test.VolumetricTest" });
    }

    int DIMX = 320;
    int DIMY = 320;
    int DIMZ = 320;

    float ISO_THRESHOLD = 0.25f;
    float NS = 0.03f;
    Vec3D SCALE = new Vec3D(3, 1, 1).scaleSelf(320);

    IsoSurface surface;
    TriangleMesh mesh;

    boolean isWireframe = false;
    float currScale = 1;

    public void draw() {
        background(128);
        translate(width / 2, height / 2, 0);
        rotateX(mouseY * 0.01f);
        rotateY(mouseX * 0.01f);
        scale(currScale);
        ambientLight(48, 48, 48);
        lightSpecular(230, 230, 230);
        directionalLight(255, 255, 255, 0, -0.5f, -1);
        specular(255, 255, 255);
        shininess(16);
        beginShape(TRIANGLES);
        if (isWireframe) {
            stroke(255);
            noFill();
        } else {
            noStroke();
            fill(255);
        }
        // draw all faces of the computed mesh
        int num = mesh.getNumFaces();
        for (int i = 0; i < num; i++) {
            Face f = mesh.faces.get(i);
            vertex(f.a);
            vertex(f.b);
            vertex(f.c);
        }
        endShape();
    }

    public void keyPressed() {
        if (key == '-') {
            currScale = max(currScale - 0.1f, 0.5f);
        }
        if (key == '=') {
            currScale = min(currScale + 0.1f, 10);
        }
        if (key == 's') {
            // save mesh as STL or OBJ file
            mesh.saveAsSTL(sketchPath("noise.stl"));
        }
    }

    public void mousePressed() {
        isWireframe = !isWireframe;
    }

    void normal(Vec3D v) {
        normal(v.x, v.y, v.z);
    }

    public void setup() {
        size(100, 100);
        // size(1024, 768, OPENGL);
        // hint(ENABLE_OPENGL_4X_SMOOTH);
        strokeWeight(0.5f);
        VolumetricSpace volume = new VolumetricSpaceArray(SCALE, DIMX, DIMY,
                DIMZ);
        // fill volume with noise
        for (int z = 0, index = 0; z < DIMZ; z++) {
            println(z);
            for (int y = 0; y < DIMY; y++) {
                for (int x = 0; x < DIMX; x++) {
                    volume.setVoxelAt(
                            x,
                            y,
                            z,
                            (float) SimplexNoise.noise(x * NS, y * NS, z * NS) * 0.5f);
                }
            }
        }
        println("noise done");
        volume.closeSides();
        long t0 = System.nanoTime();
        // store in IsoSurface and compute surface mesh for the given threshold
        // value
        mesh = new TriangleMesh("iso", 4000000, 12000000);
        surface = new HashIsoSurface(volume, 0.33f);
        // surface.reset();
        surface.computeSurfaceMesh(mesh, ISO_THRESHOLD);
        float timeTaken = (System.nanoTime() - t0) * 1e-6f;
        println(timeTaken + "ms to compute " + mesh.getNumFaces() + " faces");
        mesh.saveAsSTL(sketchPath("noise.stl"));
        System.exit(0);
    }

    void vertex(Vec3D v) {
        vertex(v.x, v.y, v.z);
    }
}