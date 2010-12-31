/**
 * <p>This demo was an example exercise used for the dataviz workshop run at HEAD Geneva,
 * in October 2010. More info and other source code can be found on the workshop wiki:
 * 
 * http://learn.postspectacular.com/Workshop:HEADGeneva
 * </p>
 *
 * <p>This demo uses the 3rd party Twitter4j library (BSD licensed) to execute a twitter search
 * and filter tweets with an associated geo location. These tweets are then mapped onto an (untextured)
 * globe. Additionally a list of reference points/cities will be plotted for better orientation.
 *
 * Download the library from here: http://www.twitter4j.org and place the library JAR file into the
 * "code" folder of this sketch.</p>
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

import toxi.geom.*;
import toxi.processing.*;
import processing.opengl.*;

final int EARTH_RADIUS = 200;

final String SEARCH_QUERY="4sq";
final int TIME_STEP = 1;
final int NUM_SEARCHES = 10;

// number of ms per day
final long DAY_DURATION = 1000*60*60*24;

Twitter twitter = new Twitter("user","pass");

HashMap<Long,Tweet> tweets=new HashMap<Long,Tweet>();

Vec2D[] cities = new Vec2D[] {
  new Vec2D(40.6666667,-73.8827778), /* New York */
  new Vec2D(34.0522222,-118.2427778), /* LA */
  new Vec2D(51.5,-0.1166667), /* London */
  new Vec2D(28.6666667,77.2166667), /* Dehli */
  new Vec2D(35.685,139.7513889), /* Tokyo */
  new Vec2D(55.7522222,37.6155556), /* Moscow */
  new Vec2D(-36.8666667,174.7666667), /* Auckland */
  new Vec2D(-33.9166667,18.4166667), /* Capetown */
  new Vec2D(13.75,100.5166667), /* Bangkok */
  new Vec2D(-6.1744444,106.8294444), /* Jakarta */
};

ToxiclibsSupport gfx;

void setup() {
  size(1024,576,OPENGL);
  gfx=new ToxiclibsSupport(this);
  searchTwitter(SEARCH_QUERY);
}

void searchTwitter(String searchQuery) {
  Query query = new Query(searchQuery);
  query.rpp(100);
  Date referenceDate=new Date();
  for(int i=0; i<NUM_SEARCHES; i++) {
    String d=new SimpleDateFormat("yyyy-MM-dd").format(referenceDate);
    println("executing search for date: "+d);
    query.until(d);
    // execute search
    try {
      QueryResult result = twitter.search(query);
      for (Tweet tweet : result.getTweets()) {
        // first check if ID is unique?
        if (tweets.get(tweet.getId())==null) {
          // filter out tweets with GPS loc
          GeoLocation loc=tweet.getGeoLocation();
          if (loc!=null) {
            tweets.put(tweet.getId(),tweet);
            println("adding tweet: "+tweet);
          }
        } else {
          println("skipping: "+tweet.getId());
        }
      }
    } 
    catch(TwitterException e) {
      println(e.getMessage());
    }
    referenceDate.setTime(referenceDate.getTime()-TIME_STEP*DAY_DURATION);
  }
}

void draw() {
  background(0);
  fill(255);
  lights();
  translate(width/2,height/2,0);
  rotateX(mouseY*0.01);
  rotateY(mouseX*0.01);
  gfx.origin(EARTH_RADIUS+100);
  sphere(EARTH_RADIUS);
  noStroke();
  fill(255,0,0);
  // iterate over all collected tweets
  for(Tweet tweet : tweets.values()) {
    GeoLocation loc=tweet.getGeoLocation();
    drawLocation((float)loc.getLongitude(),(float)loc.getLatitude(),2);
  }
  fill(0,255,255);
  for(Vec2D loc : cities) {
    drawLocation(loc.y,loc.x,5);
  }
}

void drawLocation(float lon, float lat, float sz) {
  // convert geo location into cartesian world coordinates
  Vec3D p=new Vec3D(EARTH_RADIUS,radians(lon)-HALF_PI,radians(lat)).toCartesian();
  // create a box at the computed position
  AABB box=new AABB(p,sz);
  // draw box
  gfx.box(box);
}
