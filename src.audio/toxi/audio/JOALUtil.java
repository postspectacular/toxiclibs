/* 
 * Copyright (c) 2006-2011 Karsten Schmidt
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
import net.java.games.joal.util.WAVData;
import net.java.games.joal.util.WAVLoader;

/**
 * <a href="https://joal.dev.java.net/">JOAL</a> convenience wrapper. Full
 * documentation forthcoming. Please see the attached Processing demo & source
 * distribution of this package for basic usage.
 */
public class JOALUtil {

    public static String HARDWARE = "Generic Hardware";
    public static String SOFTWARE = "Generic Software";

    public static final Logger logger = Logger.getLogger(JOALUtil.class
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
    protected ALCcontext context;
    protected ALCdevice device;

    protected EAX eax;
    protected boolean isInited;

    protected boolean isEAX;

    protected JOALUtil() {
    }

    /**
     * Deletes & releases all sources and buffers created via this class.
     */
    public void deleteAll() {
        logger.info("deleting all sources & buffers...");
        while (sources.size() > 0) {
            deleteSource(sources.get(0), true);
        }
        sources.clear();
        buffers.clear();
    }

    public boolean deleteBuffer(AudioBuffer b) {
        if (b != null) {
            for (AudioSource s : sources) {
                if (s.getBuffer() == b) {
                    s.stop();
                    logger.fine("forced stopping source: " + s);
                }
            }
            boolean result = b.delete();
            if (buffers.remove(b)) {
                logger.info("deleted buffer: " + b);
            }
            return result;
        } else {
            logger.warning("attempted to delete null buffer");
            return true;
        }
    }

    public boolean deleteSource(AudioSource src) {
        return deleteSource(src, false);
    }

    public boolean deleteSource(AudioSource src, boolean killBuffer) {
        AudioBuffer buffer = src.getBuffer();
        boolean result = src.delete();
        if (sources.remove(src)) {
            logger.info("deleted source: " + src);
        } else {
            logger.warning("deleted unmanaged source: " + src);
        }
        if (killBuffer && buffer != null) {
            result = result && deleteBuffer(buffer);
        }
        return result;
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
        if (!isInited) {
            init();
        }
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
     * Convenience wrapper for {@link #generateSources(int)} to create a single
     * {@link AudioSource}.
     * 
     * @return audio source instance
     */
    public AudioSource generateSource() {
        return generateSources(1)[0];
    }

    /**
     * Convenience wrapper bundling {@link #loadBuffer(String)} &
     * {@link #generateSource()} in a single method call. Generates a new
     * {@link AudioSource} and assigns the sample buffer created from the given
     * WAV file.
     * 
     * @param file
     *            absolute path to WAV file
     * @return configured audio source instance
     */
    public AudioSource generateSourceFromFile(String file) {
        if (!isInited) {
            init();
        }
        AudioSource source = null;
        AudioBuffer buffer = loadBuffer(file);
        if (buffer != null) {
            source = generateSource();
            source.setBuffer(buffer);
        }
        return source;
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
        if (!isInited) {
            init();
        }
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
        if (!isInited) {
            init();
        }
        return al;
    }

    /**
     * Retrieves a list of available OpenAL compatible audio devices. This
     * method can be called before a call to {@link #init()}.
     * 
     * @return array of device names
     */
    public String[] getDeviceList() {
        if (alc == null) {
            alc = ALFactory.getALC();
        }
        return alc.alcGetDeviceSpecifiers();
    }

    /**
     * Returns the {@link SoundListener} instance for the associated OpenAL
     * context.
     * 
     * @return listener object
     */
    public SoundListener getListener() {
        if (!isInited) {
            init();
        }
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
        return init(null, false);
    }

    /**
     * Initializes the OpenAL context and if parameter is true, will attempt to
     * also setup an EAX environment. The method does nothing if it had been
     * called previously and not been {@link #shutdown()} meanwhile.
     * 
     * @param attemptEAX
     * @return true, if successful (does not care if EAX is supported & has
     *         succeeded).
     */
    public boolean init(String deviceName, boolean attemptEAX) {
        if (context != null) {
            throw new ALException("OpenAL already initialized");
        }
        if (al == null) {
            al = ALFactory.getAL();
        }
        if (alc == null) {
            alc = ALFactory.getALC();
        }
        ALCdevice d = alc.alcOpenDevice(deviceName);
        if (d == null) {
            throw new ALException("Error opening default OpenAL device");
        }
        ALCcontext c = alc.alcCreateContext(d, null);
        if (c == null) {
            alc.alcCloseDevice(d);
            throw new ALException("Error creating OpenAL context");
        }
        alc.alcMakeContextCurrent(c);
        if (alc.alcGetError(d) != 0) {
            alc.alcDestroyContext(c);
            alc.alcCloseDevice(d);
            throw new ALException("Error making OpenAL context current");
        }
        // Fully initialized; finish setup
        device = d;
        context = c;
        isInited = (al.alGetError() == AL.AL_NO_ERROR);
        buffers = new ArrayList<AudioBuffer>();
        sources = new ArrayList<AudioSource>();
        listener = new SoundListener(this);
        isEAX = al.alIsExtensionPresent("EAX2.0");
        if (isEAX && attemptEAX) {
            initEAX();
        }
        return isInited;
    }

    protected void initEAX() {
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
     * Destroys all objects, sources, buffers, contexts created by this class.
     */
    public void shutdown() {
        if (isInited) {
            logger.info("shutting down JOAL");
            deleteAll();
            alc.alcMakeContextCurrent(null);
            alc.alcDestroyContext(context);
            alc.alcCloseDevice(device);

            context = null;
            device = null;
            alc = null;
            al = null;
            buffers = null;
            sources = null;

            isInited = false;
        }
    }
}
