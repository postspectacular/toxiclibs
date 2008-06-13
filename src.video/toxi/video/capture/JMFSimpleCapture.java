/*
 * Copyright (c) 2006 Karsten Schmidt
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.</p>
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
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA</p>
 */

package toxi.video.capture;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import java.io.IOException;
import java.io.PrintStream;

import javax.media.*;
import javax.media.control.*;
import javax.media.format.*;
import javax.media.protocol.*;
import javax.media.util.BufferToImage;

import processing.core.*;

/**
 * JMFSimpleCapture is a implementation of the
 * {@link toxi.video.capture.SimpleCapture} interface using Sun's <a
 * href="http://java.sun.com/products/java-media/jmf/">Java Media Framework</a>.
 * It came about as hopefully slightly more robust alternative to the the
 * QT4Java approach of the existing Processing Capture class, though so far has
 * only been tested under Windows and with a small set of camera models. Since
 * meanwhile JMF is an unmaintained product, there might be major issues with
 * newer cameras and/or larger capture sizes. I wrote this code for a particular
 * project and I had a good user experience with the underlying technology.
 * 
 * @author Karsten Schmidt < i n f o [ a t ] t o x i . co . u k >
 * @version 0.4
 * 
 */
public class JMFSimpleCapture implements SimpleCapture {

	/**
	 * pixel width of capture image
	 */

	private int width;

	/**
	 * pixel height of capture image
	 */

	private int height;

	/**
	 * JMF player instance to supply frame sequence
	 */

	private Player player;

	/**
	 * JMF util to grab single frames from the player stream
	 */
	private FrameGrabbingControl frameGrabber;

	/**
	 * JMF util to convert raw encoded frames into AWT images
	 */
	private BufferToImage frameConv;

	/**
	 * intermediate container object for storing frames
	 */
	private BufferedImage buffImg;

	/**
	 * needed to draw the frame in the {@see JMFSimpleCapture#buffImg} before
	 * getting it's RGB data
	 */
	private Graphics2D g;

	private PImage video;

	// Error handling

	private String error = null;

	private float frameDuration;

	private long lastFrameTime=0;

	private CaptureDeviceInfo captureDevice;

	/**
	 * Lists all connected capture devices to the console
	 */
	public static void listDevices() {
		listDevices(System.out, false);
	}

	/**
	 * Writes details of all connected capture devices to the given PrintStream
	 * and optionally shows available capture formats too
	 * 
	 * @param ps
	 *            a valid PrintStream instance
	 * @param showFormats
	 *            true, if format information is required
	 */
	public static void listDevices(PrintStream ps, boolean showFormats) {
		java.util.Enumeration devices = CaptureDeviceManager
				.getDeviceList(null).elements();
		if (devices.hasMoreElements()) {
			while (devices.hasMoreElements()) {
				CaptureDeviceInfo deviceInfo = (CaptureDeviceInfo) devices
						.nextElement();
				String deviceID = deviceInfo.getName();
				ps.println("-- device: " + deviceID + " --");
				if (showFormats)
					listDeviceFormats(ps, deviceID);
			}
		} else {
			ps.println("no capture devices found.");
		}
	}

	/**
	 * Prints all available capture formats of the given device to the console
	 * 
	 * @param deviceID
	 *            descriptor string of device
	 */
	public static void listDeviceFormats(String deviceID) {
		listDeviceFormats(System.out, deviceID);
	}

	/**
	 * Prints all available capture formats of the given device to a PrintStream
	 * 
	 * @param ps
	 *            a valid PrintStream instance
	 * @param deviceID
	 *            descriptor string of device
	 */
	public static void listDeviceFormats(PrintStream ps, String deviceID) {
		if (deviceID == null) {
			return;
		}

		CaptureDeviceInfo captureDevice = CaptureDeviceManager
				.getDevice(deviceID);

		if (captureDevice == null) {
			return;
		}

		Format[] deviceFormats = captureDevice.getFormats();
		for (int i = 0; i < deviceFormats.length; i++) {
			ps.println(deviceFormats[i]);
			if (deviceFormats[i] instanceof VideoFormat) {
				ps.println("framerate: "
						+ ((VideoFormat) deviceFormats[i]).getFrameRate());
			}
		}
	}

	/**
	 * Initializes video capture for the given device.
	 * <p>
	 * <strong>IMPORTANT:</strong> framerate is currently ignored. The device's
	 * default framerate for the chosen resolution is used.
	 * </p>
	 * 
	 * @param deviceID
	 *            device descriptor
	 * @param width
	 *            capture frame width
	 * @param height
	 *            capture frame height
	 * @param fps
	 *            frame rate, currently only used for dropping getFrame()
	 *            queries if within a frame duration
	 * 
	 * @return false, if the device could not be initialized. The exact reason
	 *         can be found out via {@link JMFSimpleCapture#getError()}.
	 * 
	 * @see toxi.video.capture.SimpleCapture#initVideo(java.lang.String, int,
	 *      int, int)
	 */

	public boolean initVideo(String deviceID, int width, int height, int fps) {
		if (deviceID == null) {
			setError("Capture device ID missing");
			return false;
		}

		captureDevice = CaptureDeviceManager
				.getDevice(deviceID);

		if (captureDevice == null) {
			setError("problems accessing device: " + deviceID);
			return false;
		}

		this.width = width;
		this.height = height;
		this.frameDuration = 1000.0f / fps;

		Format[] deviceFormats = captureDevice.getFormats();
		VideoFormat fmt = null;
		for (int i = 0; i < deviceFormats.length; i++) {
			VideoFormat currFormat = (VideoFormat) deviceFormats[i];
			Dimension d = currFormat.getSize();
			if (d.width == width && d.height == height) {
				fmt = currFormat;
				// fmt = new VideoFormat(deviceFormats[i].getEncoding(), new
				// Dimension(width,height),Format.NOT_SPECIFIED, null, 1);
				break;
			}
		}

		// attempting to setup data source via the device locator
		MediaLocator loc = captureDevice.getLocator();
		DataSource captureSrc;

		try {
			captureSrc = Manager.createDataSource(loc);
		} catch (IOException e) {
			setError(e);
			return false;
		} catch (NoDataSourceException e) {
			setError(e);
			return false;
		}

		// attempt to set desired format
		FormatControl[] fmtc = ((CaptureDevice) captureSrc).getFormatControls();
		for (int i = 0; i < fmtc.length; i++) {
			if (fmtc[i].setFormat(fmt) != null) {
				break;
			}
		}

		// now kickoff the player thread for the datasource object
		try {
			player = Manager.createRealizedPlayer(captureSrc);
			player.start();

			Thread.sleep(1000); // this is such a hack :(

			frameGrabber = (FrameGrabbingControl) player
					.getControl("javax.media.control.FrameGrabbingControl");

			video = new PImage(width, height);
			video.format = PImage.RGB;

		} catch (Exception e) {
			setError(e);
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see toxi.video.capture.SimpleCapture#getFrame()
	 */

	public PImage getFrame() {
		if (isNewFrameAvailable()) {
			Buffer buf = frameGrabber.grabFrame();
			if (frameConv == null)
				frameConv = new BufferToImage((VideoFormat) buf.getFormat());

			Image img = frameConv.createImage(buf);
			if (img != null) {
				if (buffImg == null) {
					buffImg = new BufferedImage(img.getWidth(null), img
							.getHeight(null), BufferedImage.TYPE_INT_RGB);
					g = buffImg.createGraphics();
				}
				g.drawImage(img, null, null);

				buffImg.getRGB(0, 0, buffImg.getWidth(), buffImg.getHeight(),
						video.pixels, 0, video.width);
				video.updatePixels();
			}
			lastFrameTime=System.currentTimeMillis();
		}
		return video;
	}

	/**
	 * @return
	 */
	public boolean isNewFrameAvailable() {
		return (System.currentTimeMillis() - lastFrameTime > frameDuration);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see toxi.video.capture.SimpleCapture#getError()
	 */
	public String getError() {
		return error;
	}

	private void setError(Exception e) {
		error = e.getMessage();
	}

	private void setError(String string) {
		error = string;
	}

	/*
	 * (non-Javadoc)
	 */
	/*
	 * public void controllerUpdate(ControllerEvent evt) { if (evt instanceof
	 * EndOfMediaEvent) { player.close(); System.exit(0); } }
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see toxi.video.capture.SimpleCapture#getWidth()
	 */

	public int getWidth() {
		return width;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see toxi.video.capture.SimpleCapture#getHeight()
	 */
	public int getHeight() {
		return height;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see toxi.video.capture.SimpleCapture#shutdown()
	 */
	public void shutdown() {
		frameGrabber = null;
		if (player != null) {
			player.stop();
			player.close();
		}
	}

	/* (non-Javadoc)
	 * @see toxi.video.capture.SimpleCapture#getDeviceName()
	 */
	public String getDeviceName() {
		return captureDevice.getName();
	}
}