/**
<p>SimplexNoise demo showing the noise space in 1-4 dimensions.</p>
<p><strong>Key controls</strong><br/>
1 - 4 : set new number of dimensions for the noise to be computed
</p>
*/

/* 
 * Copyright (c) 2009 Karsten Schmidt
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

void keyPressed() {
  if (key>'0' && key<'5') NOISE_DIMENSIONS=key-'0';
}
