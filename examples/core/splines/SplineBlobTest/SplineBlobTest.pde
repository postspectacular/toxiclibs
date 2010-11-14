/**
 * <p>SplineBlob demo showing how to use the Spline2D class to create generative
 * character outlines as used in http://postspectacular.com/work/nokia/friends/
 * Once the outline is generated is horizontally deformed by another spline
 * constituting the character's "spine".</p>
 * 
 * <p>Key controls:</p>
 * <ul>
 * <li>1 : draw blob outline</li>
 * <li>2 : draw blob deformed by spine</li>
 * <li>3 : draw blob both (1) and (2) overlaid</li>
 * </ul>
 */

/* 
 * Copyright (c) 2008 Karsten Schmidt
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

import processing.opengl.*;
import toxi.geom.*;

SplineBlob blob;
SpineSplineBlob spineBlob;

boolean newBlob=true;
int drawMode=1;

void setup() {
  size(800,800,OPENGL);
  smooth();
  cursor(CROSS);
}

void draw() {
  if (newBlob) {
    blob=new SplineBlob(random(random(1)<0.8 ? 90 : 40, 120),random(200,300),10);
    spineBlob=new SpineSplineBlob(blob);
    newBlob=false;
  }
  background(255);
  translate(mouseX,mouseY,0);
  scale(2);
  if ((drawMode&1)!=0) blob.draw();
  if ((drawMode&2)!=0) spineBlob.draw();
}

void keyPressed() {
  if (key>='1' && key<='3') drawMode=key-'0';
  else newBlob=true;
}

