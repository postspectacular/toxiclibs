/**
 * <p>The Explorer piece by Leander Herzog demonstrates the use of polar
 * coordinates for steering and Line2D instances for finding line intersections.
 * The underlying process of the piece is as follows: update steering
 * direction, move forward if no existing line is being crossed, else search
 * for a new direction.</p>
 *
 * <p>Ported to toxiclibs & extended by Karsten Schmidt. The process is now
 * also utilizing a flexible BoundaryCheck interface to define its outer shape.
 * The demo includes a rectangular & circular boundary implementation, but
 * you're encouraged to try out more complex shapes as an exercise.</p>
 */

/* 
 * Copyright (c) 2010 Leander Herzog
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

Path p;

void setup() {
  size(600,600);
  smooth();
  noFill();
  //p=new Path(new RectBoundary(new Rect(10,10,width-20,height-20)),10,0.03,3000);
  p=new Path(new CircleBoundary(new Circle(width/2,height/2,250)),10,0.03,3000);
}

void draw() {
  background(255);
  for(int i=0; i<50; i++) p.grow();
  p.render();
}

