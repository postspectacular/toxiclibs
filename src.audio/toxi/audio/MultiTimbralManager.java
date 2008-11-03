package toxi.audio;

import java.util.logging.Logger;

/**
 * Implements a manager to play a number of shared samples in a multitimbral
 * manner without interrupting/restarting the playback of currently playing
 * samples. This is different to the default JOAL way of accessing sources
 * directly. The manager is keeping track of active sources and will attempt to
 * assign free ones for newly requested playbacks. If all voices are active, the
 * oldest one will be used for new samples.
 */
public class MultiTimbralManager {

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
	}

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
				if (!pool[id].updateStatus())
					hasFreeSource = true;
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
					pool[i].src.stop();
					now = pool[i].startTime;
				}
			}
			logger.warning("no free src, using oldest slot #" + id);
			currIndex = (id + 1) % maxSources;
		}
		pool[id].activate();
		return pool[id].src;
	}

	public void debug() {
		String info = "";
		for (int i = 0; i < maxSources; i++) {
			info += pool[i].isActive + ",";
		}
		logger.info(info);
	}

	class SourceState {
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
}