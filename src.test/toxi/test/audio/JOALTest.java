package toxi.test.audio;

import toxi.audio.AudioBuffer;
import toxi.audio.AudioSource;
import toxi.audio.JOALUtil;
import toxi.audio.SoundListener;
import toxi.geom.ReadonlyVec3D;

public class JOALTest {

    public static void main(String[] args) {
        JOALUtil audioUtil = JOALUtil.getInstance();
        String[] devices = audioUtil.getDeviceList();
        for (String d : devices) {
            System.out.println(d);
        }
        audioUtil.init(JOALUtil.SOFTWARE, false);
        SoundListener l = audioUtil.getListener();
        l.setGain(1);
        AudioBuffer b = audioUtil.loadBuffer("test/phone_ulaw.wav");
        int delay = b.getSampleSize() * 1000 / b.getFrequency();
        System.out.println(b + " length=" + delay);
        if (b.convertUlawToPCM(false)) {
            AudioSource src = audioUtil.generateSource();
            src.setBuffer(b);
            src.play();
        } else {
            System.out.println("couldn't convert buffer data");
        }
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        audioUtil.shutdown();
    }

    ReadonlyVec3D pos;
}
