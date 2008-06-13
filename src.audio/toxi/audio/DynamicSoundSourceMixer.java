/**
 * 
 */
package toxi.audio;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.sound.sampled.UnsupportedAudioFileException;

import net.java.games.sound3d.Source;
import toxi.math.FastMath;
import toxi.math.waves.SineWave;

/**
 * @author kschmidt
 * 
 */
public class DynamicSoundSourceMixer {

	private OALUtil audiosys;

	private String baseDir;

	private HashMap<String,DynamicSoundSource> staticSources = new HashMap<String,DynamicSoundSource>();
	
	private HashMap<String,DynamicSoundSource> dynamicLoops = new HashMap<String,DynamicSoundSource>();
	
	public DynamicSoundSourceMixer() {
		audiosys = new OALUtil();
		audiosys.getListener().setGain(1);
	}
	
	public DynamicSoundSourceMixer(OALUtil asys) {
		audiosys = asys;
	}
	
	public void addStaticLoop(InputStream is) {
		try {
			Source s = audiosys.loadSource(is);
			s.setLooping(true);
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setBaseDir(String dir) {
		baseDir = dir;
	}
	
	public void playAll() {
		for(String key : staticSources.keySet()) {
			DynamicSoundSource src=staticSources.get(key);
			src.play();
		}
 	}
}