abstract class Scale {
  public static final float HS=1.059463;

  abstract float getScaleForSemitone(int st);
}

class ChromaticScale extends Scale {
  float getScaleForSemitone(int st) {
    return (float) Math.pow(2, st / 12.0);
  }
}

