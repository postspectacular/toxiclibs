/*
 *   __               .__       .__  ._____.           
 * _/  |_  _______  __|__| ____ |  | |__\_ |__   ______
 * \   __\/  _ \  \/  /  |/ ___\|  | |  || __ \ /  ___/
 *  |  | (  <_> >    <|  \  \___|  |_|  || \_\ \\___ \ 
 *  |__|  \____/__/\_ \__|\___  >____/__||___  /____  >
 *                   \/       \/             \/     \/ 
 *
 * Copyright (c) 2006-2011 Karsten Schmidt
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
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301, USA
 */

package toxi.math.conversion;

public class UnitTranslator {

    /**
     * Number of millimeters per inch
     */
    public static final double INCH_MM = 25.4;

    /**
     * Number of points per inch
     */
    public static final double POINT_POSTSCRIPT = 72.0;

    /**
     * Converts millimeters into pixels.
     * 
     * @param mm
     *            millimeters
     * @param dpi
     *            DPI resolution
     * @return number of pixels
     */
    public static int millisToPixels(double mm, int dpi) {
        return (int) (mm / INCH_MM * dpi);
    }

    /**
     * Converts millimeters into PostScript points.
     * 
     * @param mm
     *            millimeters
     * @return number of points
     */
    public static double millisToPoints(double mm) {
        return mm / INCH_MM * POINT_POSTSCRIPT;
    }

    /**
     * Converts pixels into inches.
     * 
     * @param pix
     *            pixels
     * @param dpi
     *            DPI resolution to use
     * @return number of inches
     */
    public static double pixelsToInch(int pix, int dpi) {
        return (double) pix / dpi;
    }

    /**
     * Converts pixels into millimeters.
     * 
     * @param pix
     *            pixels
     * @param dpi
     *            DPI resolution
     * @return number of millimeters
     */
    public static double pixelsToMillis(int pix, int dpi) {
        return pixelsToInch(pix, dpi) * INCH_MM;
    }

    /**
     * Converts pixels into points.
     * 
     * @param pix
     *            pixels
     * @param dpi
     *            DPI resolution
     * @return number of points
     */
    public static double pixelsToPoints(int pix, int dpi) {
        return pixelsToInch(pix, dpi) * POINT_POSTSCRIPT;
    }

    /**
     * Converts points into millimeters.
     * 
     * @param pt
     * @return number of millimeters
     */
    public static double pointsToMillis(double pt) {
        return pt / POINT_POSTSCRIPT * INCH_MM;
    }

    /**
     * Converts points into pixels.
     * 
     * @param pt
     *            points
     * @param dpi
     *            DPI resolution
     * @return number of pixels
     */
    public static int pointsToPixels(double pt, int dpi) {
        return millisToPixels(pointsToMillis(pt), dpi);
    }

    /**
     * Converts an area measure in square inch to square millimeters.
     * 
     * @param area
     * @return square mm
     */
    public static double squareInchToMillis(double area) {
        return area * INCH_MM * INCH_MM;
    }

    /**
     * Converts an area measure in points to square inch.
     * 
     * @param area
     * @return square inch
     */
    public static double squarePointsToInch(double area) {
        return area / (POINT_POSTSCRIPT * POINT_POSTSCRIPT);
    }

    /**
     * Converts an area measure in points to square millimeters.
     * 
     * @param area
     * @return square mm
     */
    public static double squarePointsToMillis(double area) {
        return squareInchToMillis(squarePointsToInch(area));
    }
}
