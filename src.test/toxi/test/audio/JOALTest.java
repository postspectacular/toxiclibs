package toxi.test.audio;

import toxi.audio.AudioSource;
import toxi.audio.JOALUtil;
import toxi.audio.SoundListener;
import toxi.geom.Vec3D;

public class JOALTest {

	public static void main(String[] args) {
		JOALUtil audioUtil = JOALUtil.getInstance();
		audioUtil.init(JOALUtil.SOFTWARE, false);
		SoundListener l = audioUtil.getListener();
		l.setGain(1);
		AudioSource src = audioUtil
				.generateSourceFromFile("examples/audio-external/HelloAudioWorld/data/synth.wav");
		src.play();
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	Vec3D pos;
}
