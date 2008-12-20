// This is based on a simpler, less complex bare bones version I've posted
// in this thread of the Processing forums:
// http://processing.org/discourse/yabb_beta/YaBB.cgi?board=Syntax;action=display;num=1213599231#2

// There's also a more complex & flexible version utilizing Eclipse EMF technology to create
// complex compositions utilizing hundreds of patterns, pattern groups & themes. This will most
// likely be released as separate project for generative composers in 2009.
// If you'd like to stay informed about this please send me an email:
// <info a-t postspectacular d-o-t com>


class AutoSequencer extends Thread {

  long previousTime;
  boolean isActive=true;
  double interval;
  double bpm,targetBPM;

  int beatCount=0;
  int ticksPerBar;

  int tempoChangeBars=32;

  ArrayList patterns=new ArrayList();

  AutoSequencer(double bpm, double tbpm, int tpb) {
    this.bpm=bpm;
    this.targetBPM=tbpm;
    ticksPerBar=tpb;
    setPriority(Thread.NORM_PRIORITY+3);
    // interval currently hard coded to quarter beats
    interval = 1000.0 / (bpm / 60.0) / 8; 
    previousTime=System.nanoTime();
  }

  void addPattern(AudioPattern p) {
    patterns.add(p);
  }

  void setTempo(double bpm) {
    this.bpm=bpm;
    interval = 1000.0 / (bpm / 60.0) / 8; 
  }

  void run() {
    try {
      while(isActive) {
        // calculate time difference since last beat & wait if necessary
        double timePassed=(System.nanoTime()-previousTime)*1.0e-6;
        while(timePassed<interval) {
          timePassed=(System.nanoTime()-previousTime)*1.0e-6;
        }
        Iterator i=patterns.iterator();
        while(i.hasNext()) {
          AudioPattern p=(AudioPattern)i.next();
          p.update(beatCount);
        }
        beatCount++;
        if (0==beatCount%(tempoChangeBars*4*ticksPerBar)) {
          bpm=random(76,random(1)<0.66 ? 100 : 130);
          if (random(1)<0.66) targetBPM=bpm;
        }
        // adjust tempo and interval
        bpm+=(targetBPM-bpm)*0.0015;
        setTempo((float)bpm);
        // calculate real time until next beat
        long delay=(long)(interval-(System.nanoTime()-previousTime)*1.0e-6);
        if (delay<0) delay=0;
        previousTime=System.nanoTime();
        Thread.sleep(delay);
      }
    }
    catch(InterruptedException e) {
      println("force quit...");
    }
  }

  void soloPattern(int id) {
    for(int i=patterns.size()-1; i>=0; i--) {
      AudioPattern p=(AudioPattern)patterns.get(i);
      if (i==id) p.unmute();
      else p.mute();
    }
  }

  void toggleMutePattern(int id) {
    AudioPattern p=(AudioPattern)patterns.get(id);
    if (p.isMuted) p.unmute();
    else p.mute();
  }

  void releaseAll() {
    Iterator i=patterns.iterator();
    while(i.hasNext()) {
      ((AudioPattern)i.next()).release();
    }
  }
}

