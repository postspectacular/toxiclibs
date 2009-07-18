/**
 * Loop Pitch/Scratch demo using the JOAL based audioutils.
 * Demonstrates pitch & spatial positioning using the SoundListener class
 *
 * Mouse controls:
 * Move mouse to adjust position and pitch of the sample loop playing
 * Click/drag mouse to move position of listener itself
 * 
 * Key controls:
 * Press any key to toggle volume falloff on/off
 */

/* 
 * Copyright (c) 2008-2009 Karsten Schmidt
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

import toxi.audio.*;
import toxi.geom.*;

JOALUtil audioSys;
AudioSource sound;
SoundListener listener;

boolean useFalloff=true;

void setup() {
  size(100,100);
  rectMode(CENTER);
  // JOALUtil is implemented as singleton, so get an instance like this & initialize it
  audioSys = JOALUtil.getInstance();
  audioSys.init();
  // load a sample and initialize an audio source using it
  // NOTE: path to file needs to be absolute, hence we use the dataPath() wrapper
  sound=audioSys.generateSourceFromFile(dataPath("livetest_mono.wav"));
  sound.setLooping(true);
  // each sound source has a falloff distance at which it's volume will be 50%
  // distance always is relative to listener position (see below)
  // by setting the falloff to a large amount we effectively disable it
  sound.setReferenceDistance(useFalloff ? width/16 : 10000);
  sound.play();
  // set 3D position of the virtual listener
  listener=audioSys.getListener();
  listener.setPosition(width/2,height/2,0);
}

void draw() {
  // move listener position when mouse pressed
  if (mousePressed) {
    listener.setPosition(mouseX,mouseY,0);
  }
  // update pitch and position
  sound.setPitch(map(mouseY,0,height,0.1,1.9)).setPosition(mouseX,mouseY,0);

  background(255);
  // show hair cross for orientation
  // if mouse is in left half the sound will be louder on the left channel
  // the horizontal line identifies original pitch
  line(0,height/2,width,height/2);
  line(listener.x,0,listener.x,height);
  fill(255,0,0);
  rect(listener.x,listener.y,10,10);
  fill(0,255,0);
  rect(mouseX,mouseY,5,5);
}

void keyPressed() {
  useFalloff=!useFalloff;
  sound.setReferenceDistance(useFalloff ? width/16 : 10000);
}

// release all audio resources
public void stop() {
  audioSys.shutdown();
}
