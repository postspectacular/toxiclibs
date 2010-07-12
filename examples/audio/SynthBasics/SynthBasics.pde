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
  float[] sample=new float[SAMPLE_FREQ*4];
  // calculate the base frequency in radians
  float freq=AbstractWave.hertzToRadians(220,SAMPLE_FREQ);
  // create an oscillator for the left channel
  AbstractWave osc=new SineWave(0,freq,0.5,0);
  // and another modulated one for the right channel (at 2x the base freq)
  // (first define the freq modulation wave)
  AbstractWave fmod=new FMSquareWave(0,AbstractWave.hertzToRadians(6,SAMPLE_FREQ),freq,0);
  // (use a triangle wave to modulate the amplitude)
  AbstractWave amod=new FMTriangleWave(0,AbstractWave.hertzToRadians(1,SAMPLE_FREQ),0.5,0);
  // now create the actual 2nd oscillator
  AbstractWave osc2=new AMFMSineWave(0,freq*2,0,fmod,amod);
  // populate the sample buffer
  for(int i=0; i<sample.length; i+=2) {
    sample[i]=osc.update();
    sample[i+1]=osc2.update();
  }
  // init the audio library
  audio=JOALUtil.getInstance();
  audio.init();
  // convert raw signal into JOAL 16bit stereo buffer
  buffer=SynthUtil.floatArrayTo16bitStereoBuffer(audio,sample,SAMPLE_FREQ);
  // create a sound source, enable looping & play it
  source=audio.generateSource();
  source.setBuffer(buffer);
  source.setLooping(true);
  source.play();
}

