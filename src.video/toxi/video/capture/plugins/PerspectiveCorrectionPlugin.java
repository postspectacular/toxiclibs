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

import processing.core.*;
import toxi.geom.Quad;
import toxi.video.capture.LibCompVis;
import toxi.video.capture.ProcessorPipeline;

public class PerspectiveCorrectionPlugin extends ProcessorPlugin {

	public static final String QUAD = "libcv.plugin.perspective.quad";

	private PGraphics correctionGfx;

	private PImage tex;

	private Quad correctionQuad;

	/**
	 * @param p
	 */
	public PerspectiveCorrectionPlugin(ProcessorPipeline p, String id) {
		super(p,id);
		LibCompVis cv = p.getHost();
		correctionGfx = cv.createGraphics(cv.getWidth(), cv.getHeight(),
				PConstants.P3D);
		correctionGfx.defaults();
		tex = new PImage(cv.getWidth(), cv.getHeight());
		tex.format = PConstants.RGB;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see toxi.video.capture.ProcessorPlugin#configure(java.lang.Object)
	 */
	public void configure(HashMap conf) {
		correctionQuad=(Quad)conf.get(QUAD);
	}

	/* (non-Javadoc)
	 * @see toxi.video.capture.ProcessorPlugin#getConfig()
	 */
	public HashMap getConfig() {
		HashMap config = new HashMap();
		config.put(QUAD, new Quad(correctionQuad));
		return config;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see toxi.video.capture.ProcessorPlugin#process(int[], int, int)
	 */
	public int[] process(int[] pixels, int w, int h) {
		tex.pixels = pixels;
		correctionGfx.background(0);
		correctionGfx.beginShape(PConstants.QUADS);
		correctionGfx.texture(tex);
		correctionGfx.vertex(0, 0, correctionQuad.vertices[0].x,
				correctionQuad.vertices[0].y);
		correctionGfx.vertex(w - 1, 0, correctionQuad.vertices[1].x,
				correctionQuad.vertices[1].y);
		correctionGfx.vertex(w - 1, h - 1, correctionQuad.vertices[2].x,
				correctionQuad.vertices[2].y);
		correctionGfx.vertex(0, h - 1, correctionQuad.vertices[3].x,
				correctionQuad.vertices[3].y);
		correctionGfx.endShape();
		correctionGfx.updatePixels();
		// System.arraycopy(correctionGfx.pixels,0,dest.pixels,0,dest.pixels.length);
		return correctionGfx.pixels;
	}

}
