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
import java.util.logging.Logger;

import javax.sound.sampled.UnsupportedAudioFileException;

import net.java.games.joal.ALC;
import net.java.games.joal.ALCcontext;
import net.java.games.joal.ALCdevice;
import net.java.games.joal.ALException;
import net.java.games.joal.ALFactory;
import net.java.games.joal.util.WAVData;
import net.java.games.joal.util.WAVLoader;
import net.java.games.sound3d.AudioSystem3D;
import net.java.games.sound3d.Buffer;
import net.java.games.sound3d.Listener;
import net.java.games.sound3d.Source;

public class OALUtil {
	private static final Logger logger = Logger.getLogger(OALUtil.class
			.getName());

	private ALC alc;

	public OALUtil() {
		try {
			initOpenAL();
		} catch (ALException e) {
			throw new RuntimeException("OpenAL could not be initialized: "
					+ e.getMessage());
		}
	}

	private void initOpenAL() throws ALException {
		AudioSystem3D.init();

		alc = ALFactory.getALC();

		ALCdevice device;
		ALCcontext context;
		String deviceSpecifier;

		// Get handle to default device.
		device = alc.alcOpenDevice(null);
		if (device == null) {
			throw new ALException("Error opening default OpenAL device");
		}

		// Get the device specifier.
		deviceSpecifier = alc.alcGetString(device, ALC.ALC_DEVICE_SPECIFIER);
		if (deviceSpecifier == null) {
			throw new ALException(
					"Error getting specifier for default OpenAL device");
		}

		logger.info("Using device " + deviceSpecifier);

		// Create audio context.
		context = alc.alcCreateContext(device, null);
		if (context == null) {
			throw new ALException("Error creating OpenAL context");
		}

		// Set active context.
		alc.alcMakeContextCurrent(context);

		// Check for an error.
		if (alc.alcGetError(device) != ALC.ALC_NO_ERROR) {
			throw new ALException("Error making OpenAL context current");
		}
	}

	public void cleanup() {
		ALCcontext curContext;
		ALCdevice curDevice;

		// Get the current context.
		curContext = alc.alcGetCurrentContext();

		// Get the device used by that context.
		curDevice = alc.alcGetContextsDevice(curContext);

		// Reset the current context to NULL.
		alc.alcMakeContextCurrent(null);

		// Release the context and the device.
		alc.alcDestroyContext(curContext);
		alc.alcCloseDevice(curDevice);

		alc = null;
	}

	public Buffer loadBuffer(InputStream is)
			throws UnsupportedAudioFileException, IOException {
		Buffer result;
		Buffer[] tmp = AudioSystem3D.generateBuffers(1);
		result = tmp[0];

		WAVData wd = WAVLoader.loadFromStream(is);
		result.configure(wd.data, wd.format, wd.freq);

		return result;
	}

	public Buffer loadBuffer(String fileName) throws IOException,
			UnsupportedAudioFileException {
		return AudioSystem3D.loadBuffer(fileName);
	}

	/**
	 * @param stream
	 * @throws IOException
	 * @throws UnsupportedAudioFileException
	 * @return new Source instance
	 */
	public Source loadSource(InputStream stream)
			throws UnsupportedAudioFileException, IOException {
		return AudioSystem3D.generateSource(loadBuffer(stream));
	}

	public Listener getListener() {
		return AudioSystem3D.getListener();
	}

	/**
	 * @param buffer
	 * @return new Source instance
	 */
	public Source createSource(Buffer buffer) {
		return AudioSystem3D.generateSource(buffer);
	}
}
