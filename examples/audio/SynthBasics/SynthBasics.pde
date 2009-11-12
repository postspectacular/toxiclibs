/**
 * SynthBasics showcases some of the wave generators to synthesize sounds
 * and play them as looping sample using the JOAL library.
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

import java.nio.*;

import toxi.math.waves.*;
import toxi.geom.*;
import toxi.math.*;

import toxi.audio.*;

int SAMPLE_FREQ=44100;

JOALUtil audio;
AudioBuffer buffer;
AudioSource source;

void setup() {
  size(100,100);
  // create an array for a 1 second long stereo sample
  byte[] sample=new byte[SAMPLE_FREQ*2];
  // calculate the base frequency in radians
  float freq=AbstractWave.hertzToRadians(220,SAMPLE_FREQ);
  // create an oscillator for the left channel
  AbstractWave osc=new FMSawtoothWave(0,freq,127,0);
  // and another for the right one (at 1/2 the base freq)
  AbstractWave osc2=new SineWave(0,freq/2,127,0);
  // populate the sample buffer
  for(int i=0; i<sample.length; i+=2) {
    sample[i]=(byte)osc.update();
    sample[i+1]=(byte)osc2.update();
  }
  // init the audio library
  audio=JOALUtil.getInstance();
  audio.init();
  // allocate a buffer via JOAL
  buffer=audio.generateBuffers(1)[0];
  // set the computed sample as buffer data
  buffer.configure(ByteBuffer.wrap(sample),AudioBuffer.FORMAT_STEREO8,SAMPLE_FREQ);
  // create a sound source, enable looping & play it
  source=audio.generateSource();
  source.setBuffer(buffer);
  source.setLooping(true);
  source.setReferenceDistance(width/2);
  source.play();
}

