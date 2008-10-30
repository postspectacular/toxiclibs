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
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.logging.Logger;

import javax.sound.sampled.UnsupportedAudioFileException;

import net.java.games.joal.AL;
import net.java.games.joal.ALC;
import net.java.games.joal.ALCcontext;
import net.java.games.joal.ALCdevice;
import net.java.games.joal.ALException;
import net.java.games.joal.ALFactory;
import net.java.games.joal.eax.EAX;
import net.java.games.joal.eax.EAXConstants;
import net.java.games.joal.eax.EAXFactory;
import net.java.games.joal.util.ALut;
import net.java.games.joal.util.WAVData;
import net.java.games.joal.util.WAVLoader;

public class LibOAL {

	private static final Logger logger = Logger.getLogger(LibOAL.class
			.getName());

	private ArrayList buffers;
	private ArrayList sources;

	private SoundListener listener;

	private AL al;
	private ALC alc;

	private boolean isEAX;

	private EAX eax;

	public LibOAL() {
		try {
			ALut.alutInit();
			al = ALFactory.getAL();
			alc = ALFactory.getALC();
		} catch (ALException e) {
			throw new RuntimeException("OpenAL could not be initialized: "
					+ e.getMessage());
		}
	}

	public boolean init(boolean attemptEAX) {
		buffers = new ArrayList();
		sources = new ArrayList();
		listener = new SoundListener(this);
		isEAX = al.alIsExtensionPresent("EAX2.0");
		if (isEAX && attemptEAX)
			initEAX();
		return (al.alGetError() == AL.AL_NO_ERROR);
	}

	private void initEAX() {
		eax = EAXFactory.getEAX();
		IntBuffer b = IntBuffer.allocate(1);
		b.put(EAXConstants.EAX_ENVIRONMENT_HANGAR);
		eax.setListenerProperty(
				EAXConstants.DSPROPERTY_EAXLISTENER_ENVIRONMENT, b);
	}

	public void shutdown() {
		logger.info("shutting down JOAL");
		int[] tmpbuf = new int[buffers.size()];
		for (int i = 0; i < tmpbuf.length; i++) {
			tmpbuf[i] = ((Integer) buffers.get(i)).intValue();
		}
		al.alDeleteBuffers(tmpbuf.length, tmpbuf, 0);
		buffers.clear();
		logger.info(tmpbuf.length + " buffers released");
		int[] tmpsrc = new int[sources.size()];
		for (int i = 0; i < tmpsrc.length; i++) {
			tmpsrc[i] = ((Integer) sources.get(i)).intValue();
		}
		al.alDeleteSources(tmpsrc.length, tmpsrc, 0);
		sources.clear();
		logger.info(tmpsrc.length + " sources released");

		ALCcontext curContext = alc.alcGetCurrentContext();
		ALCdevice curDevice = alc.alcGetContextsDevice(curContext);

		alc.alcMakeContextCurrent(null);
		alc.alcDestroyContext(curContext);
		alc.alcCloseDevice(curDevice);

		alc = null;
		al = null;
	}

	public AudioBuffer[] generateBuffers(int numBuffers) {
		AudioBuffer[] result = new AudioBuffer[numBuffers];
		int[] arr = new int[numBuffers];
		al.alGenBuffers(numBuffers, arr, 0);

		for (int i = 0; i < numBuffers; i++) {
			result[i] = new AudioBuffer(al, arr[i]);
			buffers.add(new Integer(arr[i]));
		}
		return result;
	}

	public AudioBuffer loadBuffer(InputStream is)
			throws UnsupportedAudioFileException, IOException {
		AudioBuffer result;
		AudioBuffer[] tmp = generateBuffers(1);
		result = tmp[0];

		WAVData wd = WAVLoader.loadFromStream(is);
		result.configure(wd.data, wd.format, wd.freq);

		return result;
	}

	public AudioBuffer loadBuffer(String fileName) throws IOException,
			UnsupportedAudioFileException {
		AudioBuffer result;
		AudioBuffer[] tmp = generateBuffers(1);
		result = tmp[0];

		WAVData wd = WAVLoader.loadFromFile(fileName);
		result.configure(wd.data, wd.format, wd.freq);

		return result;
	}

	public AudioSource[] generateSources(int numSources) {
		AudioSource[] result = new AudioSource[numSources];
		int[] arr = new int[numSources];
		al.alGenSources(numSources, arr, 0);

		for (int i = 0; i < numSources; i++) {
			result[i] = new AudioSource(al, arr[i]);
			sources.add(new Integer(arr[i]));
		}

		return result;
	}

	public AL getAL() {
		return al;
	}

	public SoundListener getListener() {
		if (listener == null) {
			listener = new SoundListener(this);
		}
		return listener;
	}

	public boolean isEAXSupported() {
		return isEAX;
	}
}
