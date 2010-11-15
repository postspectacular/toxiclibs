void computeVolume() {
  float cellSize=(float)DIM*2/GRID;
  Vec3D pos=new Vec3D();
  Vec3D offset=physics.getWorldBounds().getMin();
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
          float mag=pos.distanceToSquared(p)+0.00001;
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
    Face f=mesh.faces.get(i);
    Vec3D col=f.a.add(colAmp).scaleSelf(0.5);
    fill(col.x,col.y,col.z);
    normal(f.a.normal);
    vertex(f.a);
    col=f.b.add(colAmp).scaleSelf(0.5);
    fill(col.x,col.y,col.z);
    normal(f.b.normal);
    vertex(f.b);
    col=f.c.add(colAmp).scaleSelf(0.5);
    fill(col.x,col.y,col.z);
    normal(f.c.normal);
    vertex(f.c);
  }
}

void drawWireMesh() {
  noFill();
  int num=mesh.getNumFaces();
  for(int i=0; i<num; i++) {
    Face f=mesh.faces.get(i);
    Vec3D col=f.a.add(colAmp).scaleSelf(0.5);
    stroke(col.x,col.y,col.z);
    vertex(f.a);
    col=f.b.add(colAmp).scaleSelf(0.5);
    stroke(col.x,col.y,col.z);
    vertex(f.b);
    col=f.c.add(colAmp).scaleSelf(0.5);
    stroke(col.x,col.y,col.z);
    vertex(f.c);
  }
}

void normal(Vec3D v) {
  normal(v.x,v.y,v.z);
}

void vertex(Vec3D v) {
  vertex(v.x,v.y,v.z);
}
