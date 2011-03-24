/*
 *   __               .__       .__  ._____.           
 * _/  |_  _______  __|__| ____ |  | |__\_ |__   ______
 * \   __\/  _ \  \/  /  |/ ___\|  | |  || __ \ /  ___/
 *  |  | (  <_> >    <|  \  \___|  |_|  || \_\ \\___ \ 
 *  |__|  \____/__/\_ \__|\___  >____/__||___  /____  >
 *                   \/       \/             \/     \/ 
 *
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
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301, USA
 */

package toxi.audio;

import java.util.logging.Logger;

/**
 * Implements a manager to play a number of shared samples in a multitimbral
 * manner without interrupting/restarting the playback of currently playing
 * sources. This is different to the default JOAL way of accessing sources
 * directly. The manager is keeping track of active sources and will attempt to
 * assign free ones for newly requested playbacks. If all voices are active, the
 * oldest one will be stopped and used as the newly requested voice.
 */
public class MultiTimbralManager {

    static class SourceState {

        AudioSource src;
        boolean isActive;
        long startTime;

        SourceState(AudioSource src) {
            this.src = src;
        }

        void activate() {
            isActive = true;
            startTime = System.currentTimeMillis();
        }

        boolean updateStatus() {
            isActive = isActive
                    && (src.isLooping() || src.getBuffersProcessed() == 0);
            return isActive;
        }
    }

    private static final Logger logger = Logger
            .getLogger(MultiTimbralManager.class.getName());
    protected SourceState[] pool;
    protected int maxSources;

    protected int currIndex;

    public MultiTimbralManager(JOALUtil liboal, int num) {
        logger.info("attempting to allocate " + num + " audio voices");
        AudioSource[] tmp = liboal.generateSources(num);
        maxSources = tmp.length;
        pool = new SourceState[maxSources];
        for (int i = 0; i < maxSources; i++) {
            AudioSource src = tmp[i];
            src.setReferenceDistance(100);
            src.setGain(1);
            pool[i] = new SourceState(src);
        }
        logger.info("done. all sources created.");
    }

    /**
     * Uses the class' logger to print out status information about the current
     * usage of the managed audio sources.
     */
    public void debug() {
        String sources = "";
        int numActive = 0;
        for (int i = 0; i < maxSources; i++) {
            if (pool[i].isActive) {
                sources += i + ",";
                numActive++;
            }
        }
        String info = "active sources: " + numActive;
        logger.info(info);
        logger.info(sources);
    }

    /**
     * Attempts to find an available, currently unused {@link AudioSource}
     * instance which can then be configured and played by the client
     * application. If no free source is available the oldest playing one will
     * be stopped and returned as free.
     * 
     * @return a free AudioSource instance
     */
    public AudioSource getNextVoice() {
        boolean hasFreeSource = false;
        int numIterations = 0;
        int origID = currIndex;
        int id;
        // find first free slot...
        do {
            id = currIndex;
            currIndex = (currIndex + 1) % maxSources;
            if (pool[id].isActive) {
                if (!pool[id].updateStatus()) {
                    hasFreeSource = true;
                }
            } else {
                hasFreeSource = true;
            }
            numIterations++;
        } while (!hasFreeSource && numIterations < maxSources);
        // use oldest slot if no free one is available
        if (!hasFreeSource) {
            long now = System.currentTimeMillis();
            id = origID;
            for (int i = 0; i < maxSources; i++) {
                if (pool[i].startTime < now) {
                    id = i;
                    now = pool[i].startTime;
                }
            }
            pool[id].src.stop();
            logger.warning("no free src, using oldest slot #" + id);
            currIndex = (id + 1) % maxSources;
        }
        pool[id].activate();
        return pool[id].src;
    }

    /**
     * Produces an array of status flags of used voices. Elements set to true
     * indicate the voice is currently active/playing.
     * 
     * @return boolean array
     */
    public boolean[] getUsage() {
        boolean[] usage = new boolean[maxSources];
        for (int i = 0; i < maxSources; i++) {
            usage[i] = pool[i].isActive;
        }
        return usage;
    }
}