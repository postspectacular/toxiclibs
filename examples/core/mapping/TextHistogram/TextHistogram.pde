/**
 * <p>Exercise originally created for Data visualization workshop taught at
 * HEAD Geneva, October 2010. For more information visit the workshop wiki at:
 * http://learn.postspectacular.com/Workshop:HEADGeneva</p>
 *
 * <p>This small application displays a histogram of the top 500 most frequently
 * used words of an arbitrarily chosen piece of text. The visualization is using
 * the ZoomLens class to make better use of the available screen space and users
 * can zoom in to any area by moving the mouse vertically. Furthermore, this was
 * an exercise in object-oriented design and we can also re-sort the visualization
 * to instead show the longest words used or sort them alphabetically. Finally,
 * the text sample can be chosen using a standard file chooser dialog box.</p>
 *
 * <p><strong>Usage:</strong>
 * <ul>
 * <li>f: sort by word length</li>
 * <li>l: sort by word frequency</li>
 * <li>r: load new text</li>
 * </ul></p>
 *
 * The text bundled with this demo is "The Art of War" by Sunzi:
 * http://www.gutenberg.org/ebooks/132
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

import java.awt.FileDialog;
 
import toxi.util.*;
import toxi.math.*;

import java.util.List;
import java.util.Collections;
import java.util.Comparator;

List<HistogramWord> uniqueWords;

ZoomLensInterpolation zoomLens = new ZoomLensInterpolation();

float smoothStep=0.2;

int gap=10;
int labelGap=150;
int wordLimit=500;

HistogramSorter sortFunction;

void setup() {
  size(680,382);
  initHistogram();
}

void draw() {
  float maxBarHeight=height-labelGap-20;
  float normFactor=maxBarHeight/sortFunction.getMetric(uniqueWords.get(0));
  float focalPos=map(mouseX,gap,width-gap,0.0,1.0);
  zoomLens.setLensPos(focalPos,smoothStep);
  zoomLens.setLensStrength(map(mouseY,0,height,-1,+1),smoothStep);
  int focalX=(int)zoomLens.interpolate(gap, width-gap, focalPos);
  background(255);
  noStroke();
  textAlign(RIGHT);
  fill(0);
  
  int maxWords=min(uniqueWords.size(),wordLimit);
  for(int i=0; i<maxWords; i++) {
    float relativePos=(float)i/maxWords;
    HistogramWord w=uniqueWords.get(i);
    float barHeight=sortFunction.getMetric(w)*normFactor;
    int x=(int)zoomLens.interpolate(gap, width-gap, relativePos);
    int x2=(int)zoomLens.interpolate(gap, width-gap, (float)(i+1)/maxWords);
    int barWidth=max(x2-x-1,1);
    int barCenter=(x+x2)/2;
    if (abs(barCenter-focalX)<=barWidth/2) {
      fill(255,0,255);
    } else {
      fill(0);
    }
    rect((int)x,height-labelGap-barHeight-10,barWidth,barHeight);
    float ts=min(x2-x,18);
    if (ts>3) {
      textSize(ts);
      pushMatrix();
      translate(barCenter+ts/4,height-labelGap);
      rotate(-HALF_PI);
      text("("+sortFunction.getMetric(w)+") "+w.word,0,0);
      popMatrix();
    }
  } 
}

void initHistogram() {
  // use toxiclibs FileUtils to display a file chooser
  String fileName=null;
  fileName=FileUtils.showFileDialog(
    frame,
    "Choose a text file...",
    dataPath(""),
    new String[]{".txt",".txt.gz"},
    FileDialog.LOAD
  );
  // if user pressed cancel, use default file
  if (fileName==null) {
    fileName="artofwar.txt.gz";
  }
  String[] lines= loadStrings(fileName);

  int totalWordCount =0;
  HashMap<String,Integer> histogram=new HashMap<String,Integer>();
  for(int i=0; i<lines.length; i++) {
    //quand c'est un multiple de 1000 il va diviser par 1000
    if(0==i %1000) {
      println("processing line : " +i);
    }
   //ne pas prendre en compte la ponctuation
   String[] words=splitTokens(lines[i]," ,./?!:():;'\"-&");

   //si le mot est encore inconnu met se mot à la valeur 1
   for(String w : words) {
     totalWordCount++;
     w=w.toLowerCase();
     if(histogram.get(w)==null){
       histogram.put(w,1);
       println("new word: "+w);
     } else {
       //augmente le mot de 1
       histogram.put(w,histogram.get(w)+1);
     }
   }
  }
  println("----\nstatistics:");
  println("total: "+totalWordCount);
  println("unique: "+histogram.size());

  uniqueWords=new ArrayList<HistogramWord>();
  for(String w : histogram.keySet()) {
    HistogramWord hw=new HistogramWord(w,histogram.get(w));
    uniqueWords.add(hw);
  }
  setSortFunction(new FrequencyComparator());
  for(int i=0; i<1000; i++) {
    println(uniqueWords.get(i).word.length());
  }
}

void keyPressed() {
  if (key=='f') {
    setSortFunction(new FrequencyComparator());
  }
  if (key=='l') {
    setSortFunction(new WordLengthComparator());
  }
  if (key=='r') {
    initHistogram();
  }
}

// DONT REPEAT YOURSELF!
void setSortFunction(HistogramSorter s) {
  sortFunction=s;
  Collections.sort(uniqueWords, sortFunction);
}
