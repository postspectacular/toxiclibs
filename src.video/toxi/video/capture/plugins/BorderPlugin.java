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

import toxi.video.capture.ProcessorPipeline;

public class BorderPlugin extends ProcessorPlugin {

	public static final String BORDER_color = "libcv.plugin.border.color";

	private int color = 0xff000000;

	/**
	 * @param p
	 * @param id
	 */
	public BorderPlugin(ProcessorPipeline p, String id) {
		super(p, id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see toxi.video.capture.plugins.ProcessorPlugin#configure(java.util.HashMap)
	 */
	public void configure(HashMap conf) {
		color = ((Integer) conf.get(BORDER_color)).intValue();
		color |= 0xff000000;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see toxi.video.capture.plugins.ProcessorPlugin#getConfig()
	 */
	public HashMap getConfig() {
		HashMap config = new HashMap();
		config.put(BORDER_color, new Integer(color));
		return config;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see toxi.video.capture.plugins.ProcessorPlugin#process(int[], int, int)
	 */
	public int[] process(int[] pixels, int w, int h) {
		int w1 = w - 1;
		int h1 = h - 1;
		int idx = 0;
		final int value = color;
		for (int x = 0; x < w; x++)
			pixels[idx++] = value;
		for (int y = 1; y < h1; y++) {
			pixels[idx] = value;
			pixels[idx + w1] = value;
			idx += w;
		}
		for (int x = 0; x < w; x++)
			pixels[idx++] = value;
		return pixels;
	}

}
