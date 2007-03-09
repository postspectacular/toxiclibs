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
package toxi.video.capture;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;

/**
 * @author kschmidt
 * 
 */
public class LibCompVis {

	SimpleCapture capture;

	PApplet app;

	protected PImage buffer, prevBuffer, deltaBuffer, bg;

	private ProcessorPipeline pipeline = new ProcessorPipeline(this);

	/**
	 * buffer dimensions
	 */
	private int width, height;

	/**
	 * @param capture
	 * @param app
	 */
	public LibCompVis(PApplet app, SimpleCapture capture) {
		this.capture = capture;
		this.app = app;
		width = this.capture.getWidth();
		height = this.capture.getHeight();
		initBuffers();
	}

	/**
	 * initializes all default buffers
	 */
	private void initBuffers() {
		buffer = new PImage(width, height);
		buffer.format = PImage.RGB;
		prevBuffer = new PImage(width, height);
		prevBuffer.format = PImage.RGB;
		deltaBuffer = new PImage(width, height);
		deltaBuffer.format = PImage.RGB;
		bg = new PImage(width, height);
		bg.format = PImage.RGB;
	}

	public PGraphics createGraphics(int w, int h, String type) {
		return app.createGraphics(w, h, type);
	}

	public ProcessorPipeline getPipeline() {
		return pipeline;
	}

	/**
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Returns most recent, raw frame from the capture object and updates
	 * internal buffer references.
	 * 
	 * @return
	 */
	public PImage getFrame() {
		return getFrame(true);
	}

	/**
	 * @param flipBuffers
	 * @return
	 */
	public PImage getFrame(boolean flipBuffers) {
		if (capture.isNewFrameAvailable()) {
			if (flipBuffers)
				flipBuffers();
			return buffer = capture.getFrame();
		} else return buffer;
	}

	/**
	 * Returns most recent frame from the capture object, processed using the
	 * current {@link ProcessorPipeline}.
	 * 
	 * @return
	 */
	public PImage getProcessedFrame() {
		if (capture.isNewFrameAvailable()) {
			getFrame(true);
			buffer.pixels = pipeline.process(buffer.pixels, buffer.width,
					buffer.height);
		}
		return buffer;
	}

	/**
	 * Swaps the current frame buffer with the history buffer
	 */
	private void flipBuffers() {
		PImage t = prevBuffer;
		prevBuffer = buffer;
		buffer = t;
		// System.arraycopy(buffer.pixels, 0, prevBuffer.pixels, 0,
		// buffer.pixels.length);
	}

	/**
	 * @return
	 */
	public SimpleCapture getCapture() {
		return capture;
	}

	public PImage getBackground() {
		return bg;
	}

	public void setBackground(int[] pixels) {
		System.arraycopy(pixels, 0, bg.pixels, 0, bg.pixels.length);
	}

	public void accumulateBackground() {
		accumulateBackground(buffer.pixels);
	}

	public void accumulateBackground(int[] pixels) {
		for (int i = 0; i < pixels.length; i++) {
			bg.pixels[i] = 0xff000000 | mix((bg.pixels[i] & 0xff),(pixels[i] & 0xff),128);
		}
	}

	private static final int mix(int a, int b, int f) {
		return a + (((b - a) * f) >> 8);
	}

	public PImage getDeltaFrame() {
		return deltaBuffer;
	}

	public void setDeltaFrame(int[] pixels) {
		System.arraycopy(pixels, 0, deltaBuffer.pixels, 0,
				deltaBuffer.pixels.length);
	}
	
	public void stop() {
		if (capture!=null) {
			capture.shutdown();
		}
		capture=null;
		buffer=null;
		bg=null;
		deltaBuffer=null;
	}
}
