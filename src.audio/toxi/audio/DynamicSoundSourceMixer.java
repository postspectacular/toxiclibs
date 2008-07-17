/* 
 * Copyright (c) 2006, 2007 Karsten Schmidt
 * 
 * This library is free software; you can redistribute it and/or
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

package toxi.audio;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.sound.sampled.UnsupportedAudioFileException;

import net.java.games.sound3d.Source;
import toxi.math.waves.SineWave;

/**
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