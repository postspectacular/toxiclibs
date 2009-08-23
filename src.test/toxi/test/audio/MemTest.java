package toxi.test.audio;

import toxi.audio.AudioSource;
import toxi.audio.JOALUtil;
import toxi.geom.Vec3D;

public class MemTest {

	Vec3D pos;

	public static void main(String[] a) {
		try {
			JOALUtil audio = JOALUtil.getInstance();
			audio.init();
			while (true) {
				AudioSource src = audio
						.generateSourceFromFile("examples/audio-external/LoopPitchScratch/data/livetest_mono.wav");
				AudioSource s2 = audio.generateSource();
				s2.setBuffer(src.getBuffer());
				s2.play();
				Thread.sleep(1000);
				audio.deleteAll();
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
