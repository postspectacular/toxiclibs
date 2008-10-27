/* 
 * Copyright (c) 2006-2008 Karsten Schmidt
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

package toxi.util.datatypes;

import java.io.FileInputStream;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.logging.Logger;

public class TypedProperties extends Properties {

	public static final String DELIM = "\t\n\r\f\u00A0,";

	private static final Logger logger = Logger.getLogger(TypedProperties.class
			.getName());

	/**
	 * Attempts to load properties from the specified (absolute) file path (In
	 * Processing use sketchPath() or dataPath() to build absolute path).
	 * 
	 * @param path
	 *            config file
	 * @return true, if successful.
	 */
	public boolean load(String path) {
		try {
			load(new FileInputStream(path));
			return true;
		} catch (Exception e) {
			logger
					.severe("error opening config file: " + path
							+ ". exiting...");
			return false;
		}
	}

	/**
	 * Returns a property as boolean.
	 * 
	 * @param id
	 *            property name
	 * @param defaultState
	 * @return prop value
	 */
	public boolean getBoolean(String id, boolean defaultState) {
		return Boolean.parseBoolean(getProperty(id, "" + defaultState));
	}

	/**
	 * Returns a property as integer.
	 * 
	 * @param id
	 *            property name
	 * @param defaultValue
	 * @return prop value
	 */
	public int getInt(String id, int defaultValue) {
		return Integer.parseInt(getProperty(id, "" + defaultValue));
	}

	/**
	 * Returns a hexadecimal property as integer
	 * 
	 * @param id
	 *            prop name
	 * @param defaultValue
	 * @return prop value
	 */
	public int getHexInt(String id, int defaultValue) {
		return Integer.parseInt(getProperty(id, Integer
				.toHexString(defaultValue)), 16);
	}

	/**
	 * Returns a property as float.
	 * 
	 * @param id
	 * @param defVal
	 * @return
	 */
	public float getFloat(String id, float defVal) {
		return Float.parseFloat(getProperty(id, "" + defVal));
	}

	/**
	 * Returns a property as int[] array
	 * 
	 * @param id
	 *            prop name
	 * @return prop items as array
	 */
	public int[] getIntArray(String id) {
		StringTokenizer tokenizer = new StringTokenizer(getProperty(id), DELIM);
		int pieces[] = new int[tokenizer.countTokens()];
		int index = 0;
		while (tokenizer.hasMoreTokens()) {
			try {
				pieces[index++] = Integer.parseInt(tokenizer.nextToken());
			} catch (NumberFormatException e) {
				pieces[index++] = 0;
			}
		}
		return pieces;
	}

	public float[] getFloatArray(String id) {
		StringTokenizer tokenizer = new StringTokenizer(getProperty(id), DELIM);
		float pieces[] = new float[tokenizer.countTokens()];
		int index = 0;
		while (tokenizer.hasMoreTokens()) {
			try {
				pieces[index++] = Float.parseFloat(tokenizer.nextToken());
			} catch (NumberFormatException e) {
				pieces[index++] = 0;
			}
		}
		return pieces;
	}
}