// merge all mesh instances into a single one
// convert into voxelized representation
// perforate volumetric space (to make more light weight)
// compute continuous threshold surface
// apply laplacian smooth and export as STL file in sketch folder
void saveVoxelized() {
  WETriangleMesh combined=new WETriangleMesh();
  for(ParticleMesh m : meshes) {
    combined.addMesh(m.mesh);
  }
  int res=160;
  float iso=0.1;
  int wall=2;
  VolumetricSpace volume=new MeshVoxelizer(res).setWallThickness(wall).voxelizeMesh(combined);
  perforateVolume(volume);
  // make volume water tight
  volume.closeSides();
  // compute threshold surface mesh
  IsoSurface surface=new ArrayIsoSurface(volume);
  surface.computeSurfaceMesh(combined,iso);
  // smooth voxelized mesh
  new LaplacianSmooth().filter(combined,2);
  combined.saveAsSTL(sketchPath("voxelized-"+res+"-"+iso+"-"+wall+".stl"));
}

// create interleaved holes in XY plane of voxel space (holes along Z axis)
// holes are in "+" shape
void perforateVolume(VolumetricSpace volume) {
  boolean isEven=true;
  for(int y=2; y<volume.resY; y+=4) {
    for(int x=(isEven ? 2 : 4); x<volume.resX; x+=4) {
      for(int z=0;z<volume.resZ; z++) {
        volume.setVoxelAt(x-1,y,z,0);
        volume.setVoxelAt(x,y,z,0);
        volume.setVoxelAt(x+1,y,z,0);
        volume.setVoxelAt(x,y-1,z,0);
        volume.setVoxelAt(x,y+1,z,0);
      }
    }
    isEven=!isEven;
  }
}
