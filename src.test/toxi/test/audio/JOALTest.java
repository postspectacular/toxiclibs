package toxi.test.audio;

import toxi.audio.AudioBuffer;
import toxi.audio.AudioSource;
import toxi.audio.JOALUtil;
import toxi.audio.SoundListener;
import toxi.geom.Vec3D;

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
        if (b.convertUlawToPCM(false)) {
            AudioSource src = audioUtil.generateSource();
            src.setBuffer(b);
            src.play();
        } else {
            System.out.println("couldn't convert buffer data");
        }
        try {
            Thread.sleep(b.getSampleSize() * 1000 / b.getFrequency());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        audioUtil.shutdown();
    }

    Vec3D pos;
}
