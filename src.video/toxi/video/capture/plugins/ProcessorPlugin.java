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

public abstract class ProcessorPlugin {

	protected ProcessorPipeline host;

	protected String id;

	/**
	 * flag indicating if plugin is active
	 */
	protected boolean isEnabled = true;

	/**
	 * @param p
	 */
	public ProcessorPlugin(ProcessorPipeline p, String id) {
		host = p;
		this.id = id;
	}

	/**
	 * Executes the actual plugin action in the ProcessorPipeline.
	 * 
	 * @param pixels
	 * @param w
	 * @param h
	 * @return
	 */
	public abstract int[] process(int[] pixels, int w, int h);

	/**
	 * Provides an user hook to configure the plugin using a HashMap. Plugin
	 * specific constants are used as parameter reference and keys in the
	 * hashmap.
	 * 
	 * @param conf
	 */
	public void configure(HashMap conf) {

	}

	/**
	 * Returns current config setting
	 * 
	 * @return
	 */
	public abstract HashMap getConfig();

	public void setEnabled(boolean state) {
		isEnabled = state;
	}

	/**
	 * @return
	 */
	public boolean isEnabled() {
		return isEnabled;
	}

	/**
	 * @return
	 */
	public String getID() {
		return id;
	}
}
