void computeVolume() {
  float cellSize=(float)DIM*2/GRID;
  Vec3D pos=new Vec3D();
  Vec3D offset=physics.worldBounds.getMin();
  float[] volumeData=volume.getData();
  for(int z=0,index=0; z<GRID; z++) {
    pos.z=z*cellSize+offset.z;
    for(int y=0; y<GRID; y++) {
      pos.y=y*cellSize+offset.y;
      for(int x=0; x<GRID; x++) {
        pos.x=x*cellSize+offset.x;
        float val=0;
        for(int i=0; i<numP; i++) {
          Vec3D p=(Vec3D)physics.particles.get(i);
          float mag=pos.sub(p).magSquared()+0.00001;
          val+=1/mag;
        }
        volumeData[index++]=val;
      }
    }
  }
  if (isClosed) {
    volume.closeSides();
  }
  surface.reset();
  surface.computeSurfaceMesh(mesh,isoThreshold*0.001);
}

void drawFilledMesh() {
  int num=mesh.getNumFaces();
  mesh.computeVertexNormals();
  for(int i=0; i<num; i++) {
    TriangleMesh.Face f=mesh.faces.get(i);
    Vec3D n=f.a.normal;
    fill((f.a.x + colAmp.x) * 0.5f, (f.a.y + colAmp.y) * 0.5f, (f.a.z + colAmp.z) * 0.5f);
    normal(n.x,n.y,n.z);
    vertex(f.a.x,f.a.y,f.a.z);
    n=f.b.normal;
    fill((f.b.x + colAmp.x) * 0.5f, (f.b.y + colAmp.y) * 0.5f, (f.b.z + colAmp.z) * 0.5f);
    normal(n.x,n.y,n.z);
    vertex(f.b.x,f.b.y,f.b.z);
    n=f.c.normal;
    fill((f.c.x + colAmp.x) * 0.5f, (f.c.y + colAmp.y) * 0.5f, (f.c.z + colAmp.z) * 0.5f);
    normal(n.x,n.y,n.z);
    vertex(f.c.x,f.c.y,f.c.z);
  }
}

void drawWireMesh() {
  noFill();
  int num=mesh.getNumFaces();
  for(int i=0; i<num; i++) {
    TriangleMesh.Face f=mesh.faces.get(i);
    stroke((f.a.x + colAmp.x) * 0.5f, (f.a.y + colAmp.y) * 0.5f, (f.a.z + colAmp.z) * 0.5f);
    vertex(f.a.x,f.a.y,f.a.z);
    stroke((f.b.x + colAmp.x) * 0.5f, (f.b.y + colAmp.y) * 0.5f, (f.b.z + colAmp.z) * 0.5f);
    vertex(f.b.x,f.b.y,f.b.z);
    stroke((f.c.x + colAmp.x) * 0.5f, (f.c.y + colAmp.y) * 0.5f, (f.c.z + colAmp.z) * 0.5f);
    vertex(f.c.x,f.c.y,f.c.z);
  }
}

