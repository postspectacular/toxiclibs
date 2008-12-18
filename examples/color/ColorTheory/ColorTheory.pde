/**
 * ColorTheory demo showing the following:
 * - application of colorTheoryStrategies to create harmonious color palettes
 * - using the generated palettes to create additional shades
 * - sorting colorLists
 *
 * Press any key to re-generate all with a random base TColor
 *
 * @author Karsten Schmidt <info at postspectacular dot com>
 */

import java.util.Iterator;

import toxi.color.*;
import toxi.color.theory.*;
import toxi.math.*;
import toxi.util.datatypes.*;

float   SWATCH_HEIGHT = 40;
float   SWATCH_WIDTH = 5;
int     SWATCH_GAP = 1;

float   MAX_SIZE = 150;
int     NUM_DISCS = 300;

boolean showDiscs=true;

void setup() {
  size(1024, 768);
  noLoop();
  textFont(createFont("arial", 9));

}

void draw() {
  background(0);
  TColor col = colorRange.DARK.getColor();
  int yoff = 130;
  ArrayList strategies = ColorTheoryRegistry.getRegisteredStrategies();
  for (Iterator i=strategies.iterator(); i.hasNext();) {
    ColorTheoryStrategy s = (ColorTheoryStrategy) i.next();
    colorList list = colorList.createUsingStrategy(s, col);
    swatches(list, 235, yoff);
    list=new colorRange(list).addBrightnessRange(0,1).getColors(null,100,0.05);
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

void swatches(colorList sorted, int x, int y) {
  noStroke();
  for (Iterator i = sorted.iterator(); i.hasNext();) {
    TColor c = (TColor) i.next();
    swatch(c, x, y);
    x += SWATCH_WIDTH + SWATCH_GAP;
  }
}





