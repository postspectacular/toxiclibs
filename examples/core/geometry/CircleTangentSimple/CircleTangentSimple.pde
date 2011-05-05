/**
 * This example shows how to compute the tangent of a point on a circle.
 */

/* 
 * Copyright (c) 2011 Karsten Schmidt
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
import toxi.processing.*;

ToxiclibsSupport gfx;
void setup() {
  size(400, 400);
  noFill();
  gfx=new ToxiclibsSupport(this);
}

void draw() {
  // new circle
  Circle c=new Circle(width/2, height/2, sin(frameCount*0.025)*50+60);

  // get direction from centre to point
  Vec2D mousePos=new Vec2D(mouseX, mouseY);
  Vec2D dir=mousePos.sub(c).normalize();
  // tangent direction
  Vec2D tangent=dir.getPerpendicular();
  // point on circle
  Vec2D pos=c.add(dir.scale(c.getRadius()));

  background(255);
  stroke(0);
  gfx.ellipse(c);
  stroke(0,255,0);
  gfx.line(c,pos);
  stroke(255,0,255);
  // scale tangent to be more visible
  tangent.scaleSelf(100);
  gfx.line(pos.sub(tangent),pos.add(tangent));
}

