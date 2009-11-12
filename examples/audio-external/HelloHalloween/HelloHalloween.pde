/**
 * HelloHalloween is using toxiclibs audioutils to create a dynamic mix of randomly
 * chosen monster samples. Also shows usage of MultiTimbralManager utility class to
 * handle audio resourcing, scheduling and some house keeping tasks...
 *
 * You will need the following libraries from:
 * http://code.google.com/p/toxiclibs/downloads/list
 *
 * toxiclibscore-0014 or newer
 * audioutils-0004 or newer
 *
 * All audio samples by Pitx (http://www.freesound.org/usersViewSingle.php?id=40665),
 * licensed under Creative Commons Sampling Plus 1.0
 * http://creativecommons.org/licenses/sampling+/1.0/
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
AudioBuffer[] buf=new AudioBuffer[4];

void setup() {
  size(100,100);
  audioSys = JOALUtil.getInstance();
  audioSys.init();
  // create a sound manager instance for 8 audio sources/channels
  soundManager=new MultiTimbralManager(audioSys,8);
  // load the samples
  for(int i=0; i<buf.length; i++) {
    buf[i]=audioSys.loadBuffer(dataPath("monster"+i+".wav"));
  }
}

void draw() {
  // every frame there's 0.4% chance for a new sample being triggered
  // the sample will be assigned to a free audio source managed by the
  // multi-timbral sound manager
  if (random(100)<0.4) {
    soundManager.getNextVoice().setBuffer(buf[(int)random(buf.length)]).play();
  }
}

public void stop() {
  super.stop();
  audioSys.shutdown();
}



