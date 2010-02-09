class AudioPattern {
  AudioBuffer[] notes;

  int ticksPerBar=8;
  int emphasis, offbeat;

  float offBeatChance=0;
  float skipChance=0;
  float muteChance=0;
  int muteIntervalBars=4;

  int id=0;
  int currNoteID=0;
  int currNoteTicks=0;

  int offbeatProgress=2;
  float offbeatProgressChance=0.2;
  int offbeatChangeBars=8;
  float offbeatChangeChance=0.5;
  int shuffleIntervalBeats=2;
  float patternDensity=0.45;

  String name;
  boolean isMuted;

  AudioSource currVoice;

  AudioPattern(String n, String fn, int num, int e, int o, float oc) {
    name=n;
    notes=new AudioBuffer[num];
    try {
      for(int i=0; i<notes.length; i++) {
        String fileID=dataPath(fn+(i+1)+".wav");
        println("loading: "+fileID);
        AudioBuffer b = audioSys.loadBuffer(fileID);
        notes[i]=b;
      }
      emphasis=e;
      offbeat=o;
      offBeatChance=oc;
    }  
    catch (Exception ex) {
      ex.printStackTrace();
      System.exit(1);
    }
  }

  void setSkipChance(float c) {
    skipChance=c;
  }

  void setOffBeatChance(float c) {
    offBeatChance=c;
  }

  void update(int ticks) {
    if (0==ticks%(muteIntervalBars*4*ticksPerBar) && random(1)<muteChance) {
      isMuted=!isMuted;
    }
    int modBeats=ticks%ticksPerBar;
    if (currVoice!=null) currVoice.setPitch(SCALE.getScaleForSemitone(CURR_PITCH)+PITCH_BEND_AMP*sin((ticks-currNoteTicks)*0.01));
    if ((modBeats==emphasis || (random(1)<offBeatChance && modBeats==offbeat)) && notes!=null) {
      currVoice=soundManager.getNextVoice();
      currVoice.setBuffer(notes[id]);
      if (currVoice!=null && !isMuted) {
        onNoteOn(currVoice,ticks);
        currVoice.setPitch(SCALE.getScaleForSemitone(CURR_PITCH));
        currVoice.play();
        currNoteTicks=ticks;
        currNoteID=id;
      }
      id=(id+1)%notes.length;
      if (0==ticks%(shuffleIntervalBeats*ticksPerBar) && random(1)<skipChance) {
        println(name+" shuffle");
        ArrayUtil.shuffle(notes);
      }
      if (modBeats==emphasis && (ticks%(offbeatChangeBars*4*ticksPerBar))<ticksPerBar && random(1)<offbeatChangeChance) {
        if (random(1)<offbeatProgressChance) {
          offbeatProgress=(int)random(1,4.9999);
          println(name+": offbeatProgress: "+offbeatProgress);
        }
        offbeat=(offbeat+offbeatProgress)%ticksPerBar;
        println(name+": new offbeat: "+offbeat);
        int dist=offbeat-emphasis;
        if (dist==-1 || dist==1 || dist==ticksPerBar-1) {
          if (dist==1) {
            offbeat=(offbeat+1)%ticksPerBar;
            println("++: "+offbeat);
          } 
          else {
            offbeat=(offbeat+3)%ticksPerBar;
            println("+3: "+offbeat);
          }
        }
      }
    }
  }

  void release() {
    println(this+" <release>");
    for(int i=0; i<notes.length; i++) {
      notes[i]=null;
    }
    notes=null;
  }

  void stop() {
    this.notes=null;
  }

  void onNoteOn(AudioSource note, int ticks) {
  }

  void mute() {
    isMuted=true;
  }

  void unmute() {
    isMuted=false;
  }
}

class FadeInOutPattern extends AudioPattern {

  AbstractWave automator;

  FadeInOutPattern(String n, String fn, int num, int e, int o, float oc) {
    super(n, fn, num, e, o, oc);
  }

  void setVolumeAutomation(AbstractWave ctrl) {
    automator=ctrl;
  }

  void update(int ticks) {
    automator.update();
    super.update(ticks);
  }

  void onNoteOn(AudioSource note, int ticks) {
    note.setPosition(Vec3D.randomVector().scaleSelf(100)); //random(-1,1)*100,random(-1,1)*100,random(-1,1)*100);
    note.setGain(automator!=null ? automator.value : 1);
  }
}

