/**
 * <p>This demo uses the generic EventDispatcher helper class to control multiple
 * application components using custom events. It demonstrates the use of Java
 * interfaces to define event listeners and implement event receivers.
 * A simple timebased event broadcaster creates "tick" events at a regular interval
 * which are then received by two classes to change the background color as well as
 * toggle the display of a text label. The result is an elegant separation of concerns
 * within the application and a much simplified logic within the main programm loop
 * (the draw() method).
 * </p>
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
 
import toxi.util.events.*;

// our demo class to emit custom events
DemoEventBroadcaster ticker;
// a demo event receiver to change background color when events are received
BgUpdater bg;
// another event receiver to toggle a text display
DemoLabel label;

void setup() {
  size(200,200);
  // create instances
  bg=new BgUpdater();
  label=new DemoLabel("hello");
  // configure broadcaster to emit an event every 500ms
  ticker=new DemoEventBroadcaster(500);
  // attach listeners
  ticker.dispatcher.addListener(bg);
  ticker.dispatcher.addListener(label);
  
  textFont(createFont("SansSerif",24));
}

void draw() {
  // update broadcaster
  ticker.update();
  // use current bg color from BgUpdater
  background(bg.col);
  label.draw();
}

// this interface defines the actual event listener capabilities
// each method defined in here is a possible event:
// in this case each listener implementation only needs to provide a single tick() method
interface DemoEventListener {
  void tick();
}

// this event listener changes the background color
// with each event notification
class BgUpdater implements DemoEventListener {
  int col;
  
  // event callback, triggered by DemoEventBroadcaster
  // change color
  void tick() {
    col=(int)random(0xffffff);
  }
}

// this event listener toggles the display of a text label
// with each event notification
class DemoLabel implements DemoEventListener {
  boolean isEnabled;
  String txt;
  
  DemoLabel(String txt) {
    this.txt=txt;
  }
  
  // event callback, triggered by DemoEventBroadcaster
  // toggle display flag
  void tick() {
    isEnabled=!isEnabled;
  }
  
  void draw() {
    if (isEnabled) {
      fill(255);
      textAlign(CENTER);
      text(txt,width/2,height/2);
    }
  }
}

// actual event generator to which our listeners above are subscribed to
// uses the generic EventDispatcher helper class in toxi.util.events package
// this class generated events at a regular time interval configured in the constructor
class DemoEventBroadcaster {
  
  public EventDispatcher<DemoEventListener> dispatcher=new EventDispatcher<DemoEventListener>();
  
  long lastEvent;
  long period;
  
  DemoEventBroadcaster(long period) {
    this.lastEvent=System.currentTimeMillis();
    this.period=period;
  }
  
  // update timer & trigger new events if needed
  void update() {
    long now=System.currentTimeMillis();
    // emit new event if period has been reached
    if (now-lastEvent>=period) {
      // iterate over all registered listeners
      for(DemoEventListener l : dispatcher) {
        l.tick();
      }
      lastEvent=now;
    }
  }
}
