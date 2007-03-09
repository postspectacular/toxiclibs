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

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import net.java.games.joal.AL;
import net.java.games.joal.ALException;
import net.java.games.joal.ALFactory;
import net.java.games.joal.eax.EAX;
import net.java.games.joal.eax.EAXConstants;
import net.java.games.joal.eax.EAXFactory;
import net.java.games.joal.util.ALut;

public class LibOAL {

	private int numSources;

	private int sourcesUsed;

	private int[] buffers;

	private int[] sources;

	private SoundSource[] sndSources;

	private SoundListener listener;

	private AL al;

	boolean isEAX;

	EAX eax;

	public LibOAL(int numSrc) {
		try {
			ALut.alutInit();
			al = ALFactory.getAL();
			numSources = numSrc;
		} catch (ALException e) {
			throw new RuntimeException("OpenAL could not be initialized: "
					+ e.getMessage());
		}
	}

	public boolean init(boolean attemptEAX) {
		buffers = new int[numSources];
		sources = new int[numSources];
		listener = new SoundListener(this);
		sndSources = new SoundSource[numSources];
		al.alGenBuffers(numSources, buffers, 0);
		isEAX = al.alIsExtensionPresent("EAX2.0");
		if (isEAX)
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
		al.alDeleteBuffers(sourcesUsed, buffers, 0);
		al.alDeleteSources(sourcesUsed, sources, 0);
	}

	public SoundSource createSoundSource(InputStream is) {
		if (sourcesUsed < numSources) {
			int[] format = new int[1];
			int[] size = new int[1];
			ByteBuffer[] data = new ByteBuffer[1];
			int[] freq = new int[1];
			int[] loop = new int[1];
			ALut.alutLoadWAVFile(is, format, data, size, freq, loop);
			al.alBufferData(buffers[sourcesUsed], format[0], data[0], size[0],
					freq[0]);

			al.alGenSources(1, sources, sourcesUsed);

			int currSrcID = sources[sourcesUsed];
			SoundSource src = new SoundSource(this, currSrcID, sourcesUsed,
					size[0]);

			al.alSourcei(currSrcID, AL.AL_BUFFER, buffers[sourcesUsed]);

			if (al.alGetError() == AL.AL_NO_ERROR) {
				sndSources[sourcesUsed] = src;
				sourcesUsed++;
				return src;
			}
		}
		return null;
	}

	public AL getAL() {
		return al;
	}

	public SoundListener getListener() {
		return listener;
	}

	public void playAllSources() {
		for (int i = 0; i < sourcesUsed; i++)
			sndSources[i].play();
	}

	public void stopAllSources() {
		for (int i = 0; i < sourcesUsed; i++)
			sndSources[i].stop();
	}

	protected int getBufferID(int idx) {
		if (idx < sourcesUsed) {
			return buffers[idx];
		}
		return -1;
	}

	public boolean isEAXSupported() {
		return isEAX;
	}

}
