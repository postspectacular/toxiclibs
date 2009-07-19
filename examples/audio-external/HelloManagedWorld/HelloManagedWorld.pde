/**
 * A Hello World demo for using the JOAL based audioutils to play a sample on demand.
 * Also shows usage of MultiTimbralManager utility class to handle audio resourcing,
 * scheduling and some house keeping tasks...
 * 
 * Key controls:
 * Press key 0 - 9 to play a sound at different pitches
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
MultiTimbralManager soundManager;
AudioBuffer buf;

void setup() {
  size(100,100);
  // JOALUtil is implemented as singleton, so get an instance like this & initialize it
  audioSys = JOALUtil.getInstance();
  audioSys.init();
  // create a pool of 8 managed voices to play our samples
  // using more than one voice ensures that new sounds don't truncate
  // already playing ones. The manager also takes care of respecting
  // looping sounds and handles other resource & memory management
  soundManager=new MultiTimbralManager(audioSys,8);
  // load a sample into a reusable buffer
  // later this buffer will be assigned to actual sound sources
  buf=audioSys.loadBuffer(dataPath("synth.wav"));
}

// needed because else Processing is calling stop() prematurely
void draw() {
}

void keyPressed() {
  if (key>='0' && key<='9') {
    int transpose=key=='0' ? 9 : key-'1';
    float pitch=getPitchForSemitone(transpose);
    // get a free voice from the manager
    AudioSource currVoice=soundManager.getNextVoice();
    // assign the sample buffer & play
    currVoice.setBuffer(buf);
    currVoice.setPitch(pitch);
    currVoice.play();
  }
}

/**
 * computes pitch factor for the given semi-tone offset
 * @param st semitone offset
 * @return pitch factor
 */
float getPitchForSemitone(int st) {
  return (float) Math.pow(2, st / 12.0);
}

public void stop() {
  println("stop");
  super.stop();
  audioSys.shutdown();
}

