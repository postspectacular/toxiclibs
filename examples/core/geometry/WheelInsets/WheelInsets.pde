/**
 * <p>This example was used for designing the outer rim of some
 * generative wheels. The wheels are designed for laser cutting from
 * acrylic and the outer rim has a number of cutouts for friction fitting
 * wire track modules.</p>
 * <p>The actual generative wheel design is available under GPL v3 and
 * can be downloaded from:
 * 
 * http://toxiclibs.org/2010/01/re-inventing-the-wheel/
 * </p>
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

int NUM_CUTOUTS=18;
int NUM_ARCSEG=8;
int RADIUS=300;
float INSET=RADIUS-15;
float MATERIAL_THICKNESS=5;
float TOLERANCE=0.05;
float SIZE=MATERIAL_THICKNESS+TOLERANCE;
int BLEED=20;

void setup() {
  size(2*RADIUS+BLEED,2*RADIUS+BLEED);
  background(255);
  translate(width/2,height/2);
  noFill();
  beginShape();
  for(int i=0; i<NUM_CUTOUTS; i++) {
    float alpha=i*TWO_PI/NUM_CUTOUTS;
    float beta=(i+1)*TWO_PI/NUM_CUTOUTS;
    // cutout
    Vec2D[] insets=computePoints(alpha,INSET,SIZE);
    vertex(insets[1]);
    vertex(insets[0]);
    float gamma=computeAngles(alpha,RADIUS,SIZE)[0];
    float delta=computeAngles(beta,RADIUS,SIZE)[1];
    // arc between cutouts
    for(int j=0; j<=NUM_ARCSEG; j++) {
      float theta=lerp(gamma,delta,j/(float)NUM_ARCSEG);
      vertex(Vec2D.fromTheta(theta).scaleSelf(RADIUS));
    }
  }
  endShape(CLOSE);
}

void vertex(Vec2D v) {
  vertex(v.x,v.y);
}

float[] computeAngles(float theta, float radius, float dist) {
  Vec2D[] points=computePoints(theta,radius,dist);
  Vec2D a=points[0].toPolar();
  Vec2D b=points[1].toPolar();
  float alpha=a.y<0 ? TWO_PI+a.y : a.y;
  float beta=b.y<0 ? TWO_PI+b.y : b.y;
  return new float[]{
    alpha,beta };
}

Vec2D[] computePoints(float theta, float radius, float dist) {
  Vec2D p=Vec2D.fromTheta(theta).scaleSelf(radius);
  Vec2D n=p.getPerpendicular().normalizeTo(dist/2);
  Vec2D a=p.add(n);
  Vec2D b=p.sub(n);
  return new Vec2D[]{
    a,b };
}
