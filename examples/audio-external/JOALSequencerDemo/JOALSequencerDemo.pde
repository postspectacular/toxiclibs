/**
 * Sequencer demo showing the following:
 * - typical use case for the MultiTimbralManager for playing multiple
 *   samples without truncating the playback of the ones already playing
 * - implementation of a simple, but with high precision timing sequencer
 *   running in its own thread and able to smoothly interpolate tempo changes
 * - rule-based, semi-randomized musical pattern creation from single note samples
 * - usage of wave generators to automate pattern volume (fading in/out)
 * - implements a chromatic scale to transpose all notes on demand
 * - 3D positioning of samples (MONO ONLY & NEEDS 3D capable soundcard, else only stereo pan)
 *
 * Key controls:
 * 1,2,3 : mute/unmute audio patterns
 * a - y : change global pitch (-/+1 octave up, m = normal pitch)
 * [ , ] : adjust pitch bending amount
 *
 * Click & drag mouse left/right to adjust tempo
 * 
 * @author Karsten Schmidt <info at postspectacular dot com>
 * 
 * This demo & music is copyright 2008 Karsten Schmidt.
 * Released under the Creative Commons BY-NC-SA licence:
 * http://creativecommons.org/licenses/by-nc-sa/2.0/uk/
 *
 * Please do let me know your comments & share your improvements done to this demo!
 * Thanks :)
 */

import toxi.audio.*;
import toxi.geom.*;
import toxi.math.waves.*;
import toxi.util.datatypes.ArrayUtil;
import java.util.Iterator;

int CURR_PITCH=0;
float PITCH_BEND_AMP=0;
Scale SCALE=new ChromaticScale();

JOALUtil audioSys;
MultiTimbralManager soundManager;
AutoSequencer sequencer;

void setup() {
  size(480,320);
  textFont(createFont("arial",12));
  // JOALUtil is implemented as singleton, so get an instance like this:
  audioSys = JOALUtil.getInstance();
  // then initialize it
  audioSys.init();

  // create a pool of 64 managed voices to play our samples
  soundManager=new MultiTimbralManager(audioSys,64);  

  // create a new sequencer instance
  // tempo starting at 96bpm, but moving to 112bpm
  sequencer=new AutoSequencer(96,112,8);
  // adding some melody patterns
  // x samples are loaded and played in sequence at specified time points within each pattern
  // the pattern differentiates between main emphasis and an offbeat, which has a probability
  // attached & so might not always get played, thus changing the rhythm of the pattern
  // also, the offbeat position is slowly moving around the pattern to create variations on the 
  // original musical theme the pattern started with 
  FadeInOutPattern p=new FadeInOutPattern("synth","audio/synth",7,0,5,1);
  // here we attach a sine wave to control the automatic fading in/out of the samples associated
  // with this pattern
  p.setVolumeAutomation(new SineWave(HALF_PI,0.005,0.43,0.043));
  // 25% chance every 2 bars to randomize the order of notes in this pattern
  p.skipChance=0.25;
  sequencer.addPattern(p);

  // adding some more patterns in the same fashion
  p=new FadeInOutPattern("pad","audio/pad",4,0,6,0.1);
  p.setVolumeAutomation(new SineWave(-HALF_PI,0.0041,0.28,0.28));
  p.skipChance=0.1;
  sequencer.addPattern(p);

  p=new FadeInOutPattern("808","audio/808",1,0,8,0.05);
  p.setVolumeAutomation(new FMSquareWave(PI,0.01,0.1,0.1));
  p.ticksPerBar=16;
  sequencer.addPattern(p);

  p=new FadeInOutPattern("seq","audio/hiseq_mono",2,2,0,0);
  p.setVolumeAutomation(new SineWave(-HALF_PI,0.0027,0.18,0.18));
  p.ticksPerBar=32;
  sequencer.addPattern(p);

  // start sequencer playback (in its own thread)
  sequencer.start();
}

void draw() {
  if (mousePressed) {
    float masterBPM=map(mouseX,0,width,60,160);
    sequencer.setTempo(masterBPM);
  }
  background(0);
  fill(255);
  text("tempo: "+nf((float)sequencer.bpm,2,3),10,20);
  text("ticks: "+(sequencer.beatCount/8)+"/"+(sequencer.beatCount%8),200,20);
  text("pitch: "+CURR_PITCH,200,40);
  text("pitch bend: "+PITCH_BEND_AMP,200,60);
  text("fps: "+frameRate,360,20);
  Iterator ip=sequencer.patterns.iterator();
  int idx=0;
  while(ip.hasNext()) {
    AudioPattern p=(AudioPattern)ip.next();
    text(p.name+": "+p.offbeat+" / "+p.emphasis+(p.isMuted ? " (mute)": ""),10,40+idx*20);
    idx++;
  }
}

public void stop() {
  sequencer.isActive=false;
  sequencer.releaseAll();
  if (audioSys != null) {
    audioSys.shutdown();
    audioSys = null;
  }
}

void keyPressed() {
  if (key>='a' && key<='y') CURR_PITCH=key-'m';
  if (key>='1' && key<='3') sequencer.toggleMutePattern(key-'1');
  if (key=='[') PITCH_BEND_AMP=max(PITCH_BEND_AMP-0.1,0);
  if (key==']') PITCH_BEND_AMP=min(PITCH_BEND_AMP+0.1,0.9);
}
