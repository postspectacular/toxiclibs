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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;

import toxi.video.capture.plugins.ProcessorPlugin;

/**
 * @author kschmidt
 * 
 */
public class ProcessorPipeline {

	private ArrayList<ProcessorPlugin> pipeline = new ArrayList<ProcessorPlugin>();

	private LibCompVis host;

	/**
	 * @param host
	 */
	public ProcessorPipeline(LibCompVis host) {
		this.host = host;
	}

	public ProcessorPlugin addPlugin(Class c, String id)
			throws InvocationTargetException {
		ProcessorPlugin plugin = getPluginInstance(c, id);

		if (plugin != null)
			pipeline.add(plugin);

		return plugin;
	}

	public ProcessorPlugin insertPlugin(Class c, String id, String oldID)
			throws InvocationTargetException {
		
		ProcessorPlugin oldPlugin = getPluginForID(oldID);
		if (oldPlugin != null) {
			int insertPos = pipeline.indexOf(oldPlugin);
			ProcessorPlugin plugin = getPluginInstance(c, id);
			if (plugin != null) {
				pipeline.add(insertPos, plugin);
				return plugin;
			}
		}
		return null;
	}

	/**
	 * @param c
	 * @param id
	 * @return
	 * @throws InvocationTargetException
	 */
	@SuppressWarnings("unchecked")
	private ProcessorPlugin getPluginInstance(Class c, String id)
			throws InvocationTargetException {
		try {
			Class constructorParams[] = null;
			Object constructorValues[] = null;

			constructorParams = new Class[] { ProcessorPipeline.class,
					String.class };
			constructorValues = new Object[] { this, id };

			Constructor constructor = c.getConstructor(constructorParams);

			return (ProcessorPlugin) constructor.newInstance(constructorValues);
		} catch (Exception e) {
			throw new InvocationTargetException(e, "can't instantiate plugin: "
					+ c.getName());
		}
	}

	public ArrayList<ProcessorPlugin> list() {
		return new ArrayList<ProcessorPlugin>(pipeline);
	}

	public ProcessorPlugin getPluginForID(String id) {
		for (Iterator i = pipeline.iterator(); i.hasNext();) {
			ProcessorPlugin p = (ProcessorPlugin)i.next();
			if (p.getID().equals(id))
				return p;
		}
		return null;
	}

	public void clear() {
		pipeline.clear();
	}

	public int[] process(int[] pixels, int w, int h) {
		for (Iterator i = pipeline.iterator(); i.hasNext();) {
			ProcessorPlugin p = (ProcessorPlugin)i.next();
			if (p.isEnabled())
				pixels = p.process(pixels, w, h);
		}
		return pixels;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(host.getCapture().getDeviceName());
		sb.append(" -> ");
		for (Iterator i = pipeline.iterator(); i.hasNext();) {
			ProcessorPlugin p = (ProcessorPlugin)i.next();
			sb.append(p.getClass().getName());
			sb.append(":");
			sb.append(p.getID());
			if (!p.isEnabled()) {
				sb.append(" (disabled) ");
			}
			if (i.hasNext())
				sb.append(" -> ");
		}
		return sb.toString();
	}

	/**
	 * @return the host
	 */
	public LibCompVis getHost() {
		return host;
	}

}
