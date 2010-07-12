/**
 * SynthXfadeFmod showcases some of the wave generators to synthesize spacey
 * sounds and play them as looping sample using the JOAL library. This example is
 * slightly more advanced than SynthBasics and introduces oscillator cross-fading,
 * frequency modulation and a simple delay/echo effect.
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

int delayTime=11025;
float feedback = 0.9;

JOALUtil audio;
AudioBuffer buffer;
AudioSource source;

void setup() {
  size(100,100);
  // create an array for stereo samples
  byte[] sample=new byte[SAMPLE_FREQ*2*16];
  float[] raw=new float[sample.length];
  // calculate the base frequency in radians
  float freq=AbstractWave.hertzToRadians(110,SAMPLE_FREQ);
  // create an square wave oscillator for the left channel
  AbstractWave osc=new FMHarmonicSquareWave(0,freq,1,0);
  ((FMHarmonicSquareWave)osc).maxHarmonics=8;
  // and a frequency modulated sinewave for the right one (at 1/2 the base freq)
  AbstractWave osc2=new FMSineWave(0,freq*2,new SineWave(0,AbstractWave.hertzToRadians(1/16.0,SAMPLE_FREQ)));
  // another wave to be used for cross fading the 2 oscillators
  AbstractWave xfade=new SineWave(0,AbstractWave.hertzToRadians(1,SAMPLE_FREQ),0.5,0.5);
  // populate the sample buffer
  for(int i=0; i<raw.length; i+=2) {
    float x=xfade.update();
    float a=osc.update();
    float b=osc2.update();
    // calculate crossfaded contributions for each channel
    raw[i]=a+(b-a)*x;
    raw[i+1]=b+(a-b)*x;
    // if index > delay time, apply delay/echo effect
    if (i>delayTime) {
      raw[i]=raw[i]*(1-feedback)+raw[i-delayTime]*feedback;
      raw[i+1]=raw[i+1]*(1-feedback)+raw[i-delayTime+1]*feedback;
    }
  }
  // init the audio library
  audio=JOALUtil.getInstance();
  audio.init();
  // convert raw signal into JOAL 16bit stereo buffer
  buffer=SynthUtil.floatArrayTo16bitStereoBuffer(audio,raw,SAMPLE_FREQ);
  // create a sound source, enable looping & play it
  source=audio.generateSource();
  source.setBuffer(buffer);
  source.setLooping(true);
  source.play();
}

