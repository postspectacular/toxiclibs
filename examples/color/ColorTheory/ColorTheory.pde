/**
 * ColorTheory demo showing the following:
 * - application of ColorTheoryStrategies to create harmonious colorpalettes
 * - using the generated palettes to create additional shades
 * - sorting ColorLists
 *
 * Press any key to re-generate all with a random base TColor
 *
 * @author Karsten Schmidt <info at postspectacular dot com>
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

import toxi.color.*;
import toxi.color.theory.*;
import toxi.util.datatypes.*;

float   SWATCH_HEIGHT = 40;
float   SWATCH_WIDTH = 5;
int     SWATCH_GAP = 1;

float   MAX_SIZE = 150;
int     NUM_DISCS = 300;

void setup() {
  size(1024, 768);
  noLoop();
  textFont(createFont("arial", 9));
}

void draw() {
  background(0);
  TColor col = ColorRange.DARK.getColor();
  int yoff = 130;
  ArrayList<ColorTheoryStrategy> strategies = ColorTheoryRegistry.getRegisteredStrategies();
  for (ColorTheoryStrategy s : strategies) {
    ColorList list = ColorList.createUsingStrategy(s, col);
    swatches(list, 235, yoff);
    list=new ColorRange(list).addBrightnessRange(0,1).getColors(null,100,0.05);
    list.sortByDistance(false);
    swatches(list,335,yoff);
    fill(255);
    text(s.getName(),85,yoff+SWATCH_HEIGHT-3);
    yoff += SWATCH_HEIGHT + 10;
  }
}

void keyPressed() {
  redraw();
}


void swatch(TColor c, int x, int y) {
  fill(c.toARGB());
  rect(x, y, SWATCH_WIDTH, SWATCH_HEIGHT);
}

void swatches(ColorList sorted, int x, int y) {
  noStroke();
  for (TColor c : sorted) {
    swatch(c, x, y);
    x += SWATCH_WIDTH + SWATCH_GAP;
  }
}

