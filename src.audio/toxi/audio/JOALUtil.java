/* 
 * Copyright (c) 2006-2008 Karsten Schmidt
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

/**
 * <a href="https://joal.dev.java.net/">JOAL</a> convenience wrapper. Full
 * documentation forthcoming. Please see the attached Processing demo & source
 * distribution of this package for basic usage.
 * 
 * @author toxi
 */
public class JOALUtil {

	protected static final Logger logger = Logger.getLogger(JOALUtil.class
			.getName());

	protected static JOALUtil instance;

	public static JOALUtil getInstance() {
		if (instance == null) {
			synchronized (JOALUtil.class) {
				if (instance == null) {
					instance = new JOALUtil();
				}
			}
		}
		return instance;
	}

	protected ArrayList<AudioBuffer> buffers;

	protected ArrayList<AudioSource> sources;

	protected SoundListener listener;
	protected AL al;
	protected ALC alc;

	protected EAX eax;
	protected boolean isInited;

	protected boolean isEAX;

	protected JOALUtil() {
	}

	public void deleteBuffer(AudioBuffer b) {
		for (AudioSource s : sources) {
			if (s.getBuffer() == b) {
				s.stop();
				logger.fine("forced stopping source: " + s);
			}
		}
		b.delete();
		buffers.remove(b);
		logger.info("deleted buffer: " + b);
	}

	public void deleteSource(AudioSource src) {
		src.delete();
		sources.remove(src);
		logger.info("deleted source: " + src);
	}

	/**
	 * Creates the specified number of audio sample buffers and returns an array
	 * of {@link AudioBuffer} wrappers.
	 * 
	 * @param numBuffers
	 *            number of requested buffers
	 * @return array
	 */
	public AudioBuffer[] generateBuffers(int numBuffers) {
		AudioBuffer[] result = new AudioBuffer[numBuffers];
		int[] arr = new int[numBuffers];
		al.alGenBuffers(numBuffers, arr, 0);
		for (int i = 0; i < numBuffers; i++) {
			result[i] = new AudioBuffer(al, arr[i]);
			buffers.add(result[i]);
		}
		return result;
	}

	/**
	 * Creates the specified number of hardware audio sources required to
	 * actually play the sample data stored in {@link AudioBuffer}s.
	 * 
	 * @param numSources
	 *            number of sources required
	 * @return array
	 */
	public AudioSource[] generateSources(int numSources) {
		AudioSource[] result = new AudioSource[numSources];
		int[] arr = new int[numSources];
		al.alGenSources(numSources, arr, 0);
		for (int i = 0; i < numSources; i++) {
			result[i] = new AudioSource(al, arr[i]);
			sources.add(result[i]);
		}
		return result;
	}

	/**
	 * Returns a direct reference to the OpenAL API.
	 * 
	 * @return JOAL context
	 */
	public AL getAL() {
		return al;
	}

	/**
	 * Returns the {@link SoundListener} instance for the associated OpenAL
	 * context.
	 * 
	 * @return listener object
	 */
	public SoundListener getListener() {
		if (listener == null) {
			listener = new SoundListener(this);
		}
		return listener;
	}

	/**
	 * Initializes the OpenAL context. Safe to be called multiple times (only
	 * first time is executed).
	 * 
	 * @return true, if successful
	 */
	public boolean init() {
		return init(false);
	}

	/**
	 * Initializes the OpenAL context and if parameter is true, will attempt to
	 * also setup an EAX environment.
	 * 
	 * @param attemptEAX
	 * @return true, if successful (does not care if EAX is supported).
	 */
	public boolean init(boolean attemptEAX) {
		if (!isInited) {
			try {
				ALut.alutInit();
				al = ALFactory.getAL();
				alc = ALFactory.getALC();
			} catch (ALException e) {
				throw new RuntimeException("OpenAL could not be initialized: "
						+ e.getMessage());
			}
			buffers = new ArrayList<AudioBuffer>();
			sources = new ArrayList<AudioSource>();
			listener = new SoundListener(this);
			isEAX = al.alIsExtensionPresent("EAX2.0");
			if (isEAX && attemptEAX) {
				initEAX();
			}
			isInited = (al.alGetError() == AL.AL_NO_ERROR);
		}
		return isInited;
	}

	private void initEAX() {
		eax = EAXFactory.getEAX();
		IntBuffer b = IntBuffer.allocate(1);
		b.put(EAXConstants.EAX_ENVIRONMENT_HANGAR);
		eax.setListenerProperty(
				EAXConstants.DSPROPERTY_EAXLISTENER_ENVIRONMENT, b);
	}

	/**
	 * Checks if EAX are supported by the underlying hardware.
	 * 
	 * @return true, if supported.
	 */
	public boolean isEAXSupported() {
		return isEAX;
	}

	/**
	 * Loads a WAV file from the given {@link InputStream}.
	 * 
	 * @param is
	 *            input stream
	 * @return buffer wrapper instance
	 * @throws UnsupportedAudioFileException
	 * @throws IOException
	 */
	public AudioBuffer loadBuffer(InputStream is)
			throws UnsupportedAudioFileException, IOException {
		AudioBuffer result;
		AudioBuffer[] tmp = generateBuffers(1);
		result = tmp[0];
		WAVData wd = WAVLoader.loadFromStream(is);
		result.configure(wd.data, wd.format, wd.freq);
		return result;
	}

	/**
	 * Loads a WAV file (mono/stereo) from the specified file name
	 * 
	 * @param fileName
	 *            audio file name
	 * @return buffer wrapper instance
	 * @throws IOException
	 * @throws UnsupportedAudioFileException
	 */
	public AudioBuffer loadBuffer(String fileName) {
		AudioBuffer result = null;
		try {
			WAVData wd = WAVLoader.loadFromFile(fileName);
			AudioBuffer[] tmp = generateBuffers(1);
			result = tmp[0];
			result.configure(wd.data, wd.format, wd.freq);
		} catch (IOException e) {
			logger.severe(e.getMessage());
		} catch (UnsupportedAudioFileException e) {
			logger.severe(e.getMessage());
		}
		return result;
	}

	/**
	 * Destroys all objects, sources, buffers, contexts created by
	 */
	public void shutdown() {
		logger.info("shutting down JOAL");
		int[] tmpbuf = new int[buffers.size()];
		for (int i = 0; i < tmpbuf.length; i++) {
			tmpbuf[i] = buffers.get(i).getID();
		}
		al.alDeleteBuffers(tmpbuf.length, tmpbuf, 0);
		logger.info(tmpbuf.length + " buffers released");
		int[] tmpsrc = new int[sources.size()];
		for (int i = 0; i < tmpsrc.length; i++) {
			tmpsrc[i] = sources.get(i).getID();
		}
		al.alDeleteSources(tmpsrc.length, tmpsrc, 0);
		logger.info(tmpsrc.length + " sources released");

		ALCcontext curContext = alc.alcGetCurrentContext();
		ALCdevice curDevice = alc.alcGetContextsDevice(curContext);

		alc.alcMakeContextCurrent(null);
		alc.alcDestroyContext(curContext);
		alc.alcCloseDevice(curDevice);

		alc = null;
		al = null;
		buffers = null;
		sources = null;

		isInited = false;
	}
}
