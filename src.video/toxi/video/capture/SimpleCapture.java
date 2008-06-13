/* 
 * Copyright (c) 2006 Karsten Schmidt
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

import processing.core.PImage;

/**
 * This is a minimal definition of a video capture interface as currently used
 * by the {@link toxi.video.capture.LibCV} library.
 * 
 * <p>
 * This generic interface is used to ensure a relative independence from various
 * capture backends. It should be very straightforward to write a wrapper for
 * the existing Processing Capture object implementing this interface to enable
 * its use with the LibCV library.
 * </p>
 * 
 * @author Karsten Schmidt < i n f o [ a t ] t o x i . co . u k >
 */
public interface SimpleCapture {

	/**
	 * Initializes video capture for the given device.
	 * 
	 * @param deviceID
	 *            device descriptor
	 * @param width
	 *            capture frame width
	 * @param height
	 *            capture frame height
	 * @param fps
	 *            frame rate
	 * 
	 * @return False, if the device could not be initialized. The exact reason
	 *         can be found out via {@link JMFSimpleCapture#getError()}.
	 */
	public boolean initVideo(String deviceID, int width, int height,
			int fps);

	/**
	 * Handles freeing up any resources used by the SimpleCapture instance
	 */
	public void shutdown();

	/**
	 * Returns the name of the currently used capture device.
	 * @return device name
	 */
	public String getDeviceName();
	
	/**
	 * Reads the current frame from the internal video capture and converts it
	 * into a PImage object
	 * 
	 * @return the most recent captured frame as PImage
	 */
	public PImage getFrame();

	/**
	 * Indicates if a new frame is available from the capture device.
	 * @return true, if new frame is available
	 */
	public boolean isNewFrameAvailable();
	
	/**
	 * Retrieves the pixel width of the capture instance
	 * 
	 * @return image width
	 */
	public int getWidth();

	/**
	 * Retrieves the pixel height of the capture instance
	 * 
	 * @return image height
	 */
	public int getHeight();

	/**
	 * String description of an error occured previously.
	 * 
	 * @return string or null if all is ok
	 */
	public String getError();
}