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

public class BackgroundSubtractionPlugin extends ProcessorPlugin {

	/**
	 * @param p
	 * @param id
	 */
	public BackgroundSubtractionPlugin(ProcessorPipeline p, String id) {
		super(p, id);
	}

	/* (non-Javadoc)
	 * @see toxi.video.capture.plugins.ProcessorPlugin#getConfig()
	 */
	public HashMap getConfig() {
		return null;
	}

	/* (non-Javadoc)
	 * @see toxi.video.capture.plugins.ProcessorPlugin#process(int[], int, int)
	 */
	public int[] process(int[] pixels, int w, int h) {
		int[] bg=host.getHost().getBackground().pixels;
		for(int i=0; i<pixels.length; i++) {
			int diff=abs((0xff ^ (pixels[i]&0xff))-(0xff^(bg[i]&0xff)));
			pixels[i]=diff | 0xff000000;
		}
		return pixels;
	}
	
	private static final int abs(int a) {
		return (a<0 ? -a : 0);
	}
}
