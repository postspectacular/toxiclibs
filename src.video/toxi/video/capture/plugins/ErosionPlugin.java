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
package toxi.video.capture.plugins;

import java.util.HashMap;

import toxi.image.util.Filter8bit;
import toxi.video.capture.ProcessorPipeline;

public class ErosionPlugin extends ProcessorPlugin {

	public static final String NUM_PASSES = "libcv.plugin.erosion.num_passes";
	public static final String INVERTED = "libcv.plugin.erosion.inverted";
	
	private int numPasses = 1;
	private boolean isInverted = false;
	
	/**
	 * @param p
	 * @param id
	 */
	public ErosionPlugin(ProcessorPipeline p, String id) {
		super(p, id);
	}

	public void configure(HashMap conf) {
		numPasses = ((Integer) conf.get(NUM_PASSES)).intValue();
		isInverted = ((Boolean) conf.get(INVERTED)).booleanValue();
	}
	
	/* (non-Javadoc)
	 * @see toxi.video.capture.plugins.ProcessorPlugin#getConfig()
	 */
	public HashMap getConfig() {
		HashMap config = new HashMap();
		config.put(NUM_PASSES, new Integer(numPasses));
		config.put(INVERTED, new Boolean(isInverted));
		return config;
	}

	/* (non-Javadoc)
	 * @see toxi.video.capture.plugins.ProcessorPlugin#process(int[], int, int)
	 */
	public int[] process(int[] pixels, int w, int h) {
		for(int i=0; i<numPasses; i++) {
			pixels=Filter8bit.erodePixels(pixels, w, h, isInverted);
		}
		return pixels;
	}

}
