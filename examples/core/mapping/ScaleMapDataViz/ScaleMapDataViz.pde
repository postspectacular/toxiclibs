/**
 * <p>Recording mouse clicks and visualizing them as stream (incl. click durations).
 * Simple repeat patterns within the click stream are shown too. 
 * Clicks of equal length (with tolerance) will be connected with
 * arcs. The example is making use of the {@link java.util.HashMap} class to do
 * the grouping of related clicks.</p>
 * 
 * <p>This demo was an example exercise used for the DataViz workshop run at CADA,
 * Lisboa in April 2009. More info and other source code can be found on the workshop wiki:
 * 
 * <a href="http://dataviz.postspectacular.com">http://dataviz.postspectacular.com</a>
 * </p> 
 */

/* 
 * Copyright (c) 2009 Karsten Schmidt
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

import toxi.color.*;
import toxi.math.*;
import java.util.Iterator;
import java.util.Date;

ArrayList clicks = new ArrayList();
ClickParcel current;

/**
 * HashMap used for assembling & storing groups of equal-length clicks.
 * We'll use the click duration as hash key linked to ArrayList instances
 * storing the actual clicks.
 */
HashMap uniqueDurations = new HashMap();

ScaleMap timeMap;
ScaleMap hueMap;

ZoomLensInterpolation zoomLens = new ZoomLensInterpolation();
float lensSmooth = 0.05f;

int historyLength = 10000;
int hueDuration = 2500;
int minClickDuration = 30;

/**
 * All click durations will be rounded to the nearest number of milliseconds
 */
int clickTolerance = 250;

void setup() {
  size(680, 256);
  textFont(createFont("SansSerif", 10));
  long now = System.currentTimeMillis();
  timeMap = new ScaleMap(now - historyLength, now, 0, width);
  timeMap.setMapFunction(zoomLens);
  hueMap = new ScaleMap(0, hueDuration, 0, 1.0);
}

void draw() {
  background(0);
  fill(255);
  zoomLens.setLensPos((float) mouseX / width, lensSmooth);
  if (current != null) {
    current.update();
  }
  long now = System.currentTimeMillis();
  long startTime = now - historyLength;
  timeMap.setInputRange(now - historyLength, now);
  drawRuler(startTime, now);
  text(new Date(startTime).toString(), 10, height - 10);
  text(new Date(now).toString(), width - 180, height - 10);
  text("current clicks: " + clicks.size(), 10, 24);
  translate(0, height * 0.33f);
  drawClicks(startTime);
  findAndDrawArcs();
}

void findAndDrawArcs() {
  uniqueDurations.clear();
  for (Iterator i=clicks.iterator(); i.hasNext();) {
    ClickParcel p=(ClickParcel)i.next();
    int len = (int) (p.duration / clickTolerance) * clickTolerance;
    ArrayList items = (ArrayList)uniqueDurations.get(len);
    if (items != null) {
      items.add(p);
    }
    else {
      items = new ArrayList();
      items.add(p);
      uniqueDurations.put(len, items);
    }
  }
  noFill();
  for (Iterator i=uniqueDurations.values().iterator(); i.hasNext();) {
    ArrayList items=(ArrayList)i.next();
    int num = items.size() - 1;
    for (int j = 0; j < num; j++) {
      ClickParcel p = (ClickParcel)items.get(j);
      ClickParcel q = (ClickParcel)items.get(j + 1);
      float xp = (float) timeMap.getClippedValueFor(p.startTime);
      float xq = (float) timeMap.getClippedValueFor(q.startTime);
      float r = min((xq - xp) * 0.5f, 120);
      stroke((xq - xp) * 128f / width + 128);
      bezier(xp, 50, xp, 50 + r, xq, 50 + r, xq, 50);
    }
    endShape();
  }
}

void drawClicks(long startTime) {
  beginShape(TRIANGLES);
  for (Iterator i = clicks.iterator(); i.hasNext();) {
    ClickParcel p = (ClickParcel)i.next();
    if (p.endTime > startTime) {
      float xStart = (float) timeMap.getClippedValueFor(p.startTime);
      float xEnd = (float) timeMap.getClippedValueFor(p.endTime);
      float hue = (float) hueMap.getClippedValueFor(p.duration);
      TColor col = TColor.newHSV(hue, 1, 1);
      fill(col.toARGB());
      vertex(xStart, -50 * hue);
      vertex(max(xEnd, xStart + 1), 0);
      vertex(xStart, 50 * hue);
    }
    else {
      i.remove();
    }
  }
  endShape();
}

/**
 * Draws markers at every second in the given interval.
 * 
 * @param startTime
 * @param now
 */
void drawRuler(long startTime, long now) {
  stroke(80);
  // start at nearest second
  long rulerTime = (startTime / 1000) * 1000 + 1000;
  while (rulerTime < now) {
    float x = (float) timeMap.getClippedValueFor(rulerTime);
    line(x, 0, x, height);
    rulerTime += 1000;
  }
  noStroke();
}

void mousePressed() {
  current = new ClickParcel();
  clicks.add(current);
}

void mouseReleased() {
  current = null;
}

class ClickParcel {
  long startTime;
  long endTime;
  long duration;

  ClickParcel() {
    startTime = System.currentTimeMillis();
    update();
  }

  void update() {
    endTime = System.currentTimeMillis() + minClickDuration;
    duration = endTime - startTime;
  }
}
