/**
 * <p>This example shows how to easily integrate the EventDispatcher helper to
 * quickly realise the Observer design pattern in your own classes. All you need
 * to do is define an interface for your event listeners and add a public field
 * for the EventDispatcher in your observable class.</p>
 *
 * <p>The concrete example here is using a DelayManager class to trigger
 * micro-threads to delay individual key events. Whenever a delay is finished,
 * the manager then distributes the event to all listeners.</p>
 *
 * <p><strong>Usage:</strong> Press any key to trigger delayed events</p>
 */
 
 /*
  * Copyright (c) 2006-2011 Karsten Schmidt
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
  * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301, USA
  */

import toxi.util.events.*;

import java.util.Date;

DelayManager manager = new DelayManager();

void setup() {
  size(100,100);
  manager.listeners.addListener(new DummyListener());
}

void draw() { 
}

// pass key presses to manager
void keyPressed() {
  manager.queueEvent();
}

// this interface defines a function name for our event listeners
interface DelayedTriggerListener {
  void delayedAction(long timeStamp);
}

// this mini class is an actual event listener implementation
// it's registered with the manager in setup()
class DummyListener implements DelayedTriggerListener {
  void delayedAction(long from) {
    println("delayed event from: "+new Date(from)+" now: "+new Date());
  }
}

// actual "observable" manager class using
// the EventDispatcher helper class this whole demo is about
class DelayManager {
  
  EventDispatcher<DelayedTriggerListener> listeners = new EventDispatcher<DelayedTriggerListener>();
  
  // trigger a new delayed event
  void queueEvent() {
    new DelayThread(this,System.currentTimeMillis(),2000).start();
  }
  
  // called when the delay is finished, notifies all listeners
  void broadcastEvent(DelayThread t) {
    for(DelayedTriggerListener l : listeners) {
      l.delayedAction(t.timeStamp);
    }
  }
}

// simple helper class to delay an individual event
class DelayThread extends Thread {

  long timeStamp;
  long delay;
  DelayManager parent;

  DelayThread(DelayManager parent, long timeStamp, long delay) {
    this.parent = parent;
    this.timeStamp = timeStamp;
    this.delay = delay;
  }
  
  void run() {
    try {
      if (delay > 0) {
        Thread.sleep(delay);
      }
      parent.broadcastEvent(this);
    }
    catch (InterruptedException e) {
    }
  }
}
