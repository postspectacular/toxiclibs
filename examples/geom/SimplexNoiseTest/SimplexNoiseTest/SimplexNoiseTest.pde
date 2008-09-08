import toxi.math.noise.*;

int NOISE_DIMENSIONS=1; // increase upto 4

float NS = 0.05f; // noise scale (try from 0.005 to 0.5)
float noiseOffset = 100;

void setup() {
  size(200, 200, P3D);
}

void draw() {
  background(0);
  for (int i = 0; i < width; i++) {
    for (int j = 0; j < height; j++) {
      float noiseVal=0;
      switch(NOISE_DIMENSIONS) {
      case 1:
      default:
        noiseVal = (float) SimplexNoise.noise(i * NS + noiseOffset, 0); 
        break;
      case 2:
        noiseVal = (float) SimplexNoise.noise(i * NS + noiseOffset, j * NS + noiseOffset); 
        break;
      case 3: 
        noiseVal = (float) SimplexNoise.noise(i * NS + noiseOffset, j * NS + noiseOffset , frameCount * 0.01); 
        break;
      case 4: 
        noiseVal = (float) SimplexNoise.noise(i * NS + noiseOffset, j * NS + noiseOffset, 0 , frameCount * 0.01); 
        break;
      }
      int c = (int) (noiseVal * 127 + 128);
      set(i, j, c << 16 | c << 8 | c | 0xff000000);
    }
  }
  noiseOffset+=NS/2;
}
