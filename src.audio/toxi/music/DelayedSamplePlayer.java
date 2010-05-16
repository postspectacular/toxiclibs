package toxi.music;

import toxi.audio.AudioBuffer;
import toxi.audio.AudioSource;

public class DelayedSamplePlayer extends Thread {

    private long delay;
    private AudioSource src;

    public DelayedSamplePlayer(AudioSource src, AudioBuffer buffer, long delay) {
        this.src = src;
        this.delay = delay;
        if (buffer != null) {
            src.setBuffer(buffer);
        }
    }

    public DelayedSamplePlayer(AudioSource src, long delay) {
        this(src, null, delay);
    }

    public void run() {
        try {
            if (delay > 0) {
                Thread.sleep(delay);
            }
        } catch (InterruptedException e) {
        }
        src.play();
    }

}
