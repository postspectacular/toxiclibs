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

/**
 * LibCV ProcessorPipeline plugin for level correction
 * 
 * @author kschmidt
 * 
 */
public class LevelsPlugin extends ProcessorPlugin {

	/**
	 * Unique parameter identifier used for specifying the target black point.
	 */
	public static final String LOW = "libcv.plugin.levels.low";

	/**
	 * Unique parameter identifier used for specifying the target white point.
	 */
	public static final String HIGH = "libcv.plugin.levels.high";

	private int low = 0;

	private int high = 255;

	/**
	 * @param p
	 *            parent {@link ProcessorPipeline}
	 */
	public LevelsPlugin(ProcessorPipeline p, String id) {
		super(p, id);
	}

	public void configure(HashMap conf) {
		low = ((Integer) conf.get(LOW)).intValue();
		high = ((Integer) conf.get(HIGH)).intValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see toxi.video.capture.ProcessorPlugin#getConfig()
	 */
	public HashMap getConfig() {
		HashMap config = new HashMap();
		config.put(LOW, new Integer(low));
		config.put(HIGH, new Integer(high));
		return config;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see toxi.video.capture.ProcessorPlugin#process(int[], int, int)
	 */
	public int[] process(int[] pixels, int w, int h) {
		return Filter8bit.stretchLevels(pixels, low, high);
	}

}
