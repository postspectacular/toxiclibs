/**
 * <p>This little demo shows how to use the datautils Atom parser to read
 * a Flickr Atom feed and display the enclosed images in a simple slide show.
 * For each item, the title and author name are displayed.</p>
 *
 * <p>If you want to display a different Atom feed, simply edit the atomURL
 * variable.</p>
 *
 * <p>UPDATES:
 * <ul>
 * <li>2010-10-30: scale images to always fit screen</li>
 * </ul></p>
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

import toxi.data.feeds.*;
import java.util.List;

String atomURL="http://api.flickr.com/services/feeds/photos_public.gne?id=73281046@N00&lang=en-us&format=atom";

AtomFeed feed;
int entryID;

void setup() {
  size(1024,576);
  textFont(createFont("SansSerif",18));
  // load the feed
  feed=AtomFeed.newFromURL(atomURL);
  println(feed.entries.size()+" entries loaded");
}

void draw() {
  background(0);
  // get the current entry
  AtomEntry entry=feed.entries.get(entryID);
  // look for JPG enclosures in each entry
  List<AtomLink> enclosures=entry.getEnclosuresForType("image/jpeg");
  if (enclosures!=null) {
    // if there are any, get the URL of the first (probably only) enclosure
    String imgURL=enclosures.get(0).href;
    println("loading image: "+imgURL);
    // load the image & display
    PImage img=loadImage(imgURL);
    if (img!=null) {
      if (img.width>img.height) {
        image(img,0,0,width,width/((float)img.width/img.height));
      } 
      else {
        image(img,0,0,height*((float)img.width/img.height),height);
      }
      // show image info
      fill(0);
      textSize(18);
      text(entry.title,20,40);
      textSize(12);
      text("by "+entry.author.name,20,60);
      delay(2000);
    }
  }
  // cycle over all entries
  entryID=(entryID+1) % feed.entries.size();
}
