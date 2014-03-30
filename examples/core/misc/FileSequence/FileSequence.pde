/**
 * This example demonstrates how to use the features of the FileUtils and
 * FileSequenceDescriptor classes to display a file dialog box and to easily
 * load numbered images sequence through means of an standard iterator.
 *
 * Usage:
 * The file chooser automatically is pointed to this sketch's data folder
 * from which you should choose the first image of the sequence. The sketch
 * will then load all images in the identified sequence and display them.
 */

/* 
 * Copyright (c) 2010 Karsten Schmidt
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

import toxi.util.*;
import java.awt.FileDialog;
import java.util.Iterator;

FileSequenceDescriptor fsd;
Iterator images;

void setup() {
  size(256, 256);
  textSize(9);
  // display the file chooser and set a number
  // of supported file formats which the user can choose
  // all other file types will not be able to be selected
  String path = FileUtils.showFileDialog(
    frame,
    "Choose start frame...",
    dataPath(""),
    new String[]{ ".tga",".png",".jpg" },
    FileDialog.LOAD
  );
  // the path variable will be null if the user has cancelled
  if (path != null) {
    // get an descriptor for this base path
    // this will analyse and identify the length of the sequence
    // see javadocs for further details
    fsd=FileUtils.getFileSequenceDescriptorFor(path);
    println("start: "+fsd.getStartIndex()+" end: "+fsd.getFinalIndex());
    // now ask descriptor for an iterator which will return
    // absolute file paths for all images in this sequence in succession
    images=fsd.iterator();
  }
  else {
    // quit if user cancelled dialog...
    exit();
  }
}

void draw() {
  background(0);
  // check if we're at the end of the sequence
  // if so, get a new iterator to create looping
  if (!images.hasNext()) {
    images=fsd.iterator();
  }
  // get the next path, load & display image
  String imgPath=(String)images.next();
  PImage img=loadImage(imgPath);
  image(img,0,0,width,height);
  // display image file path
  fill(255,0,0);
  text(imgPath,10,height-50, width-20,50);
}
