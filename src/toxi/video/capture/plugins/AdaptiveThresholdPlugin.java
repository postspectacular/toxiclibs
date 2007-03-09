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

public class AdaptiveThresholdPlugin extends ProcessorPlugin {

	public static final String KERNEL_SIZE = "libcv.plugin.adaptivethreshold.kernel_size";

	public static final String FILTER_CONSTANT = "libcv.plugin.adaptivethreshold.filter_const";

	private int kernelSize = 3;

	private int filterConst = 7;

	/**
	 * @param p
	 * @param id
	 */
	public AdaptiveThresholdPlugin(ProcessorPipeline p, String id) {
		super(p, id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see toxi.video.capture.plugins.ProcessorPlugin#configure(java.util.HashMap)
	 */
	public void configure(HashMap conf) {
		kernelSize = ((Integer) conf.get(KERNEL_SIZE)).intValue();
		filterConst = ((Integer) conf.get(FILTER_CONSTANT)).intValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see toxi.video.capture.plugins.ProcessorPlugin#getConfig()
	 */
	public HashMap getConfig() {
		HashMap config = new HashMap();
		config.put(KERNEL_SIZE, new Integer(kernelSize));
		config.put(FILTER_CONSTANT, new Integer(filterConst));
		return config;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see toxi.video.capture.plugins.ProcessorPlugin#process(int[], int, int)
	 */
	public int[] process(int[] pixels, int w, int h) {
		return Filter8bit.adaptiveThreshold(pixels, w, h, kernelSize,
				filterConst);
	}

	/**
	 * @param i
	 */
	public void setKernelSize(int s) {
		kernelSize = s;
	}

	/**
	 * @param c
	 */
	public void setConstant(int c) {
		filterConst = c;
	}
}
