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

package toxi.image.util;

import processing.core.PImage;

/**
 * Filter8bit is a collection of non-destructive, threadsafe filters for
 * grayscale images in Processing PImage format. All filters are implemented as
 * static methods so no instance is needed.
 * 
 * @version 0.1
 */
public class Filter8bit {

    private static int[] koff;

    private static int prevKernelSize = -1;

    private static int prevWidth = -1;

    public static int[] adaptiveThreshold(int[] pix, int width, int height,
            int kernelSize, int filterConst) {
        int maxIdx = pix.length;
        int[] dest = new int[maxIdx];
        int kl = kernelSize * kernelSize;
        int ck = kernelSize >> 1;
        if (kl != prevKernelSize || width != prevWidth) {
            System.out.println("recalc threshold filter kernel");
            koff = new int[kl];
            prevKernelSize = kl;
            prevWidth = width;
            for (int k = 0, off = -width * ck - ck; k < kl; k++) {
                koff[k] = off;
                if ((k % kernelSize) == kernelSize - 1) {
                    off += width - kernelSize + 1;
                } else {
                    off++;
                }
            }
        }
        for (int i = 0; i < maxIdx; i++) {
            int mean = 0;
            for (int k = 0; k < kl; k++) {
                int idx = i + koff[k];
                if (idx >= 0 && idx < maxIdx) {
                    mean += pix[idx] & 0xff;
                }
            }
            mean = (mean / kl) - filterConst;
            if ((pix[i] & 0xff) > mean) {
                dest[i] = 0xff0000ff;
            } else {
                dest[i] = 0xff000000;
            }
        }
        return dest;
    }

    /**
     * Non-destructively applies an adaptive thresholding filter to the passed
     * in image (grayscale only). Filter8bit only uses data stored in the blue
     * channel of the image (lowest 8 bit).
     * 
     * @param img
     *            image to be filtered
     * @param ks
     *            kernel size
     * @param c
     *            constant integer value to be subtracted from kernel result
     * @return filtered version of the image (with alpha channel set to full
     *         opacity)
     */

    public static PImage adaptiveThreshold(PImage img, int ks, int c) {
        PImage img2 = new PImage(img.width, img.height);
        img2.format = img.format;
        img2.pixels = adaptiveThreshold(img.pixels, img.width, img.height, ks,
                c);
        return img2;
    }

    /**
     * @param pixels
     * @param width
     * @param height
     * @param isInverted
     * @return filtered pixel array
     */
    public static int[] erodePixels(int[] pixels, int width, int height,
            boolean isInverted) {
        int currIdx = 0;
        int maxIdx = pixels.length;
        int[] out = new int[maxIdx];

        if (isInverted) {
            // dilation (grow light areas)
            while (currIdx < maxIdx) {
                int currRowIdx = currIdx;
                int maxRowIdx = currIdx + width;
                while (currIdx < maxRowIdx) {
                    int colOrig, colOut;
                    colOrig = colOut = pixels[currIdx];
                    int idxLeft = currIdx - 1;
                    int idxRight = currIdx + 1;
                    int idxUp = currIdx - width;
                    int idxDown = currIdx + width;
                    if (idxLeft < currRowIdx) {
                        idxLeft = currIdx;
                    }
                    if (idxRight >= maxRowIdx) {
                        idxRight = currIdx;
                    }
                    if (idxUp < 0) {
                        idxUp = 0;
                    }
                    if (idxDown >= maxIdx) {
                        idxDown = currIdx;
                    }

                    int colUp = pixels[idxUp];
                    int colLeft = pixels[idxLeft];
                    int colDown = pixels[idxDown];
                    int colRight = pixels[idxRight];

                    // compute luminance
                    int currLum = colOrig & 0xff;
                    int lumLeft = colLeft & 0xff;
                    int lumRight = colRight & 0xff;
                    int lumUp = colUp & 0xff;
                    int lumDown = colDown & 0xff;

                    if (lumLeft > currLum) {
                        colOut = colLeft;
                        currLum = lumLeft;
                    }
                    if (lumRight > currLum) {
                        colOut = colRight;
                        currLum = lumRight;
                    }
                    if (lumUp > currLum) {
                        colOut = colUp;
                        currLum = lumUp;
                    }
                    if (lumDown > currLum) {
                        colOut = colDown;
                        currLum = lumDown;
                    }
                    out[currIdx++] = colOut;
                }
            }
        } else {
            // erode (grow dark areas)
            while (currIdx < maxIdx) {
                int currRowIdx = currIdx;
                int maxRowIdx = currIdx + width;
                while (currIdx < maxRowIdx) {
                    int colOrig, colOut;
                    colOrig = colOut = pixels[currIdx];
                    int idxLeft = currIdx - 1;
                    int idxRight = currIdx + 1;
                    int idxUp = currIdx - width;
                    int idxDown = currIdx + width;
                    if (idxLeft < currRowIdx) {
                        idxLeft = currIdx;
                    }
                    if (idxRight >= maxRowIdx) {
                        idxRight = currIdx;
                    }
                    if (idxUp < 0) {
                        idxUp = 0;
                    }
                    if (idxDown >= maxIdx) {
                        idxDown = currIdx;
                    }

                    int colUp = pixels[idxUp];
                    int colLeft = pixels[idxLeft];
                    int colDown = pixels[idxDown];
                    int colRight = pixels[idxRight];

                    // compute luminance
                    int currLum = colOrig & 0xff;
                    int lumLeft = colLeft & 0xff;
                    int lumRight = colRight & 0xff;
                    int lumUp = colUp & 0xff;
                    int lumDown = colDown & 0xff;

                    if (lumLeft < currLum) {
                        colOut = colLeft;
                        currLum = lumLeft;
                    }
                    if (lumRight < currLum) {
                        colOut = colRight;
                        currLum = lumRight;
                    }
                    if (lumUp < currLum) {
                        colOut = colUp;
                        currLum = lumUp;
                    }
                    if (lumDown < currLum) {
                        colOut = colDown;
                        currLum = lumDown;
                    }
                    out[currIdx++] = colOut;
                }
            }
        }
        return out;
    }

    /**
     * @param img
     * @param isInverted
     * @return filtered result as new image
     */
    public static PImage erodePixels(PImage img, boolean isInverted) {
        PImage img2 = new PImage(img.width, img.height);
        img2.format = img.format;
        img2.pixels = erodePixels(img.pixels, img.width, img.height, isInverted);
        return img2;
    }

    public static int[] stretchLevels(int[] srcpix, int lolim, int uplim) {
        int min = srcpix[0] & 0xff;
        int max = srcpix[0] & 0xff;
        int src_rgb;
        int result;
        float quotient = 0;

        int[] dest = new int[srcpix.length];

        // Find the max and min values
        for (int i = 0; i < srcpix.length; i++) {
            src_rgb = srcpix[i] & 0xff;
            if (src_rgb < min) {
                min = src_rgb;
            } else if (src_rgb > max) {
                max = src_rgb;
            }
        }
        quotient = (((float) (uplim - lolim)) / ((float) (max - min)));
        // Calculate the output values
        for (int i = 0; i < srcpix.length; i++) {
            src_rgb = srcpix[i] & 0xff;
            result = (int) ((src_rgb - min) * quotient) + lolim;
            if (result > 255) {
                result = 255;
            } else if (result < 0) {
                result = 0;
            }
            dest[i] = 0xff000000 | result;
        }
        return dest;
    }

    /**
     * Non-destructively stretches levels of an 8bit grayscale image
     * 
     * @param img
     *            Image to be level stretched
     * @param uplim
     *            The upper limit for the values to be stretched between
     * @param lolim
     *            The lower limit for the values to be stretched between
     * @return filtered version of the image (with alpha channel set to full
     *         opacity)
     */

    public static PImage stretchLevels(PImage img, int lolim, int uplim) {
        PImage img2 = new PImage(img.width, img.height);
        img2.format = img.format;
        img2.pixels = stretchLevels(img.pixels, lolim, uplim);
        return img2;
    }

    public static int[] threshold(int[] pix, int threshold) {
        int[] dest = new int[pix.length];
        for (int i = 0; i < pix.length; i++) {
            dest[i] = (pix[i] & 0xff) > threshold ? 0xff0000ff : 0xff000000;
        }
        return dest;
    }
}
