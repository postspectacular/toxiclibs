/**
 * ColourTheme demo showing the following:
 * - construction of colour themes via textual descriptions of shades and colours
 * - adding an random element to the theme
 * - showing off different sort modes for the created ColourList
 *
 * Press SPACE to toggle rendering mode, any other key will re-generate a random variation of the colour theme
 *
 * @author Karsten Schmidt <info at postspectacular dot com>
 */
 
import java.util.Iterator;

import toxi.colour.*;
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
  smooth();
  noLoop();
}

void draw() {
  // first define our new theme
  ColourTheme t = new ColourTheme("test");
  // add different colour options, each with their own weight
  t.addRange("soft ivory", 0.5f);
  t.addRange("intense goldenrod", 0.25f);
  t.addRange("warm saddlebrown", 0.15f);
  t.addRange("fresh teal", 0.05f);
  t.addRange("bright yellow", 0.05f);

  // now add another random hue which is using only bright shades
  t.addRange(ColourRange.BRIGHT, Colour.newRandom(), MathUtils.random(0.02f, 0.05f));

  // use the colour theme to create a list of 160 colours
  ColourList list = t.getColors(160);


  if (showDiscs) {
    background(list.getLightest().toARGB());
    discs(list);
  } 
  else {
    background(0);
    int yoff=32;
    list.sortByDistance(false);
    swatches(list, 32, yoff);
    yoff+=SWATCH_HEIGHT+10;
    
    list.sortByCriteria(AccessCriteria.LUMINANCE,false);
    swatches(list, 32, yoff);
    yoff+=SWATCH_HEIGHT+10;
    
    list.sortByCriteria(AccessCriteria.BRIGHTNESS,false);
    swatches(list, 32, yoff);
    yoff+=SWATCH_HEIGHT+10;
    
    list.sortByCriteria(AccessCriteria.SATURATION,false);
    swatches(list, 32, yoff);
    yoff+=SWATCH_HEIGHT+10;
    
    list.sortByCriteria(AccessCriteria.HUE,false);
    swatches(list, 32, yoff);
    yoff+=SWATCH_HEIGHT+10;
    
    list.sortByProximityTo(NamedColour.WHITE,new RGBDistanceProxy(),false);
    swatches(list, 32, yoff);
    yoff+=SWATCH_HEIGHT+10;
  }
}

void keyPressed() {
  if (key==' ') showDiscs=!showDiscs;
  redraw();
}

void swatches(ColourList sorted, int x, int y) {
  noStroke();
  for (Iterator i = sorted.iterator(); i.hasNext();) {
    Colour c = (Colour) i.next();
    fill(c.toARGB());
    rect(x, y, SWATCH_WIDTH, SWATCH_HEIGHT);
    x += SWATCH_WIDTH + SWATCH_GAP;
  }
}

void discs(ColourList list) {
  float numCols = list.size();
  for (int i = 0; i < NUM_DISCS; i++) {
    Colour c = list.get((int) random(numCols)).copy();
    c.alpha = random(0.5f, 1);
    fill(c.toARGB());
    c = list.get((int) random(numCols));
    stroke(c.toARGB());
    strokeWeight(random(10));
    float r = random(MAX_SIZE);
    ellipse(random(width), random(height), r, r);
  }
}


