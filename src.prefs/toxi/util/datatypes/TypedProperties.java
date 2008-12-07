/*
 * Copyright (c) 2006-2008 Karsten Schmidt
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * http://creativecommons.org/licenses/LGPL/2.1/
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA
 */

package toxi.util.datatypes;

import java.io.FileInputStream;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.logging.Logger;

/**
 * Convenience wrapper providing typed access to Java {@link Properties} files.
 * 
 * @author toxi
 * 
 */

@SuppressWarnings("serial")
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
			logger.warning("error opening config file: " + path);
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
	 * @param defaultValue
	 * @return prop value
	 */
	public float getFloat(String id, float defaultValue) {
		return Float.parseFloat(getProperty(id, "" + defaultValue));
	}

	/**
	 * Shorthand wrapper for {{@link #getIntArray(String, int[])} automatically
	 * supplying an empty int[] array as default value.
	 * 
	 * @param id
	 * @return prop values as array
	 */
	public int[] getIntArray(String id) {
		return getIntArray(id, new int[0]);
	}

	/**
	 * Returns a comma delimited property value as int[] array. Non-integer
	 * items will be ignored.
	 * 
	 * @param id
	 *            prop name
	 * @return prop items as array
	 */
	public int[] getIntArray(String id, int[] defaultArray) {
		StringTokenizer tokenizer = new StringTokenizer(getProperty(id, ""),
				DELIM);
		int pieces[] = new int[tokenizer.countTokens()];
		int index = 0;
		while (tokenizer.hasMoreTokens()) {
			try {
				pieces[index] = Integer.parseInt(tokenizer.nextToken());
				index++;
			} catch (NumberFormatException e) {
				// ignore non-integer items
			}
		}
		if (index > 0) {
			int[] result = new int[index];
			System.arraycopy(pieces, 0, result, 0, index);
			return result;
		} else
			return defaultArray;
	}

	/**
	 * Shorthand wrapper for {@link #getFloatArray(String, float[])}
	 * automatically supplying an empty float[] array as default value.
	 * 
	 * @param id
	 * @return prop values as array
	 */
	public float[] getFloatArray(String id) {
		return getFloatArray(id, new float[0]);
	}

	/**
	 * Returns a comma delimited property value as float[] array.
	 * 
	 * @param id
	 *            prop name
	 * @return prop items as array
	 */
	public float[] getFloatArray(String id, float[] defaultArray) {
		StringTokenizer tokenizer = new StringTokenizer(getProperty(id, ""),
				DELIM);
		float pieces[] = new float[tokenizer.countTokens()];
		int index = 0;
		while (tokenizer.hasMoreTokens()) {
			try {
				pieces[index] = Float.parseFloat(tokenizer.nextToken());
				index++;
			} catch (NumberFormatException e) {
				// ignore NaN items
			}
		}
		if (index > 0) {
			float[] result = new float[index];
			System.arraycopy(pieces, 0, result, 0, index);
			return result;
		} else
			return defaultArray;
	}

	/**
	 * Shorthand wrapper for {@link #getByteArray(String, byte[])} automatically
	 * supplying an empty byte[] as default value.
	 * 
	 * @param id
	 * @return prop values as array
	 */
	public byte[] getByteArray(String id) {
		return getByteArray(id, new byte[0]);
	}

	/**
	 * Returns a comma delimited property value as byte[] array. Non-byte values
	 * will be ignored.
	 * 
	 * @param id
	 *            prop name
	 * @return prop values as array
	 */
	public byte[] getByteArray(String id, byte[] defaultArray) {
		StringTokenizer tokenizer = new StringTokenizer(getProperty(id, ""),
				DELIM);
		byte[] pieces = new byte[tokenizer.countTokens()];
		int index = 0;
		while (tokenizer.hasMoreTokens()) {
			try {
				pieces[index] = Byte.parseByte(tokenizer.nextToken());
				index++;
			} catch (NumberFormatException e) {
				// ignore non-integer items
			}
		}
		if (index > 0) {
			byte[] result = new byte[index];
			System.arraycopy(pieces, 0, result, 0, index);
			return result;
		} else
			return defaultArray;
	}

	public String[] getStringArray(String id) {
		return getStringArray(id, new String[0]);
	}

	public String[] getStringArray(String id, String[] defaultArray) {
		StringTokenizer tokenizer = new StringTokenizer(getProperty(id, ""),
				DELIM);
		int index = 0;
		String[] pieces = null;
		while (tokenizer.hasMoreTokens()) {
			if (pieces == null) {
				pieces = new String[tokenizer.countTokens()];
			}
			String token = tokenizer.nextToken();
			if (token.length() > 0) {
				pieces[index++] = token;
			}
		}
		if (index > 0) {
			String[] result = new String[index];
			System.arraycopy(pieces, 0, result, 0, index);
			return result;
		} else
			return defaultArray;
	}
}