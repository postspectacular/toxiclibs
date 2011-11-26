/**
 * Loads a B&W image and extrudes black pixels brighter than a given threshold into
 * a watertight 3D mesh.
 *
 * @author Karsten Schmidt // LGPL2 licensed
 *
 * Dependencies: toxiclibscore-0020
 * (or newer, available from: http://toxiclibs.org/ )
 */
import toxi.geom.*;
import toxi.geom.mesh.*;
import toxi.volume.*;

int SCALE=8;
int DEPTH=6;
int THRESH=32;
boolean FIXED_DEPTH=true;

PImage img=loadImage("hand.png");

// setup volumetric space
Vec3D worldSize = new Vec3D(img.width, img.height, DEPTH).scale(SCALE);
VolumetricSpace volume = new VolumetricHashMap(worldSize, img.width, img.height, DEPTH, 0.33);
VolumetricBrush brush = new RoundBrush(volume, SCALE);
brush.setMode(VolumetricBrush.MODE_PEAK);

// parse the image
for (int y = 0; y < img.height; y ++) {
  for (int x = 0; x < img.width; x ++) {
    int col=img.pixels[y * img.width + x] & 0xff;
    if (col > THRESH) {
      for (int z = 0, d=FIXED_DEPTH ? DEPTH : (int)(col/255.0*DEPTH); z < d; z++) {
        brush.drawAtGridPos(x, y, z, 1);
      }
    }
  }
}

// make volume watertight
volume.closeSides();

// compute mesh
WETriangleMesh mesh = new WETriangleMesh();
new HashIsoSurface(volume).computeSurfaceMesh(mesh, 1f);

// apply vertex smoothing
new LaplacianSmooth().filter(mesh,4);

mesh.saveAsSTL(sketchPath("test.stl"), true);
exit();

