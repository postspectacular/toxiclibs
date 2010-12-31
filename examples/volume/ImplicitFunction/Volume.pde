class EvaluatingVolume extends VolumetricSpace {

  private final float FREQ = PI * 3.8;
  private float upperBound;
  private SinCosLUT lut;
  
  public EvaluatingVolume(Vec3D scale, int res, float upperBound) {
    this(scale,res,res,res,upperBound);
  }
  
  public EvaluatingVolume(Vec3D scale, int resX, int resY, int resZ, float upperBound) {
    super(scale, resX, resY, resZ);
    this.upperBound = upperBound;
    this.lut=new SinCosLUT();
  }

  public void clear() {
    // nothing to do here
  }
  
  public final float getVoxelAt(int i) {
    return getVoxelAt(i % resX, (i % sliceRes) / resX, i / sliceRes);
  }

  public final float getVoxelAt(int x, int y, int z) {
    float val = 0;
    if (x > 0 && x < resX1 && y > 0 && y < resY1 && z > 0 && z < resZ1) {
      float xx = (float) x / resX - 0.5f;
      float yy = (float) y / resY - 0.5f;
      float zz = (float) z / resZ - 0.5f;
      val = lut.sin(xx * FREQ) + lut.cos(yy * FREQ) + lut.sin(zz * FREQ);
      if (val > upperBound) {
        val = 0;
      }
    }
    return val;
  }
}

