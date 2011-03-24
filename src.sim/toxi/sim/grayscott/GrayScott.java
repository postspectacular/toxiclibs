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

package toxi.sim.grayscott;

import toxi.geom.Rect;
import toxi.math.MathUtils;

/**
 * Implementation of the Gray-Scott reaction diffusion model described in detail
 * on the links below:
 * <ul>
 * <li>http://groups.csail.mit.edu/mac/projects/amorphous/GrayScott/</li>
 * <li>http://ix.cs.uoregon.edu/~jlidbeck/java/rd/</li>
 * <li>http://www.mrob.com/pub/comp/xmorphia/</li>
 * </ul>
 */
public class GrayScott {

    public float[] u, v;
    protected float[] uu, vv;

    protected int width, height;

    protected float f, k;
    protected float dU, dV;

    protected boolean isTiling;

    public GrayScott(int width, int height, boolean wrap) {
        this.width = width;
        this.height = height;
        this.u = new float[width * height];
        this.v = new float[u.length];
        this.uu = new float[u.length];
        this.vv = new float[u.length];
        this.isTiling = wrap;
        reset();
        // default config
        setCoefficients(0.023f, 0.077f, 0.16f, 0.08f);
    }

    /**
     * Convenience method to access U array using 2D coordinates.
     * 
     * @param x
     * @param y
     * @return current u value at the given position
     */
    public float getCurrentUAt(int x, int y) {
        if (y >= 0 && y < height && x >= 0 && x < width) {
            return u[y * width + x];
        }
        return 0;
    }

    /**
     * Convenience method to access V array using 2D coordinates.
     * 
     * @param x
     * @param y
     * @return current v value at the given position
     */
    public float getCurrentVAt(int x, int y) {
        if (y >= 0 && y < height && x >= 0 && x < width) {
            return v[y * width + x];
        }
        return 0;
    }

    /**
     * @return the diffuse U coefficient
     */
    public float getDiffuseU() {
        return dU;
    }

    /**
     * @return the diffuse V coefficient
     */
    public float getDiffuseV() {
        return dV;
    }

    /**
     * @return the F coefficient
     */
    public float getF() {
        return f;
    }

    /**
     * Extension point for subclasses to modulate the F coefficient of the
     * reaction diffusion, based on spatial (or other) parameters. This method
     * is called for every cell/pixel of the simulation space from the main
     * {@link #update(float)} cycle and can be used to create parameter
     * gradients, animations and other spatial or temporal modulations.
     * 
     * @param x
     * @param y
     * @return the active F coefficient at the given position
     */
    public float getFCoeffAt(int x, int y) {
        return f;
    }

    /**
     * @return the K coefficient
     */
    public float getK() {
        return k;
    }

    /**
     * Extension point for subclasses to modulate the K coefficient of the
     * reaction diffusion, based on spatial (or other) parameters. This method
     * is called for every cell/pixel of the simulation space and can be used to
     * create parameter gradients, animations and other spatial or temporal
     * modulations.
     * 
     * @param x
     * @param y
     * @return the active K coefficient at the given position
     */
    public float getKCoeffAt(int x, int y) {
        return k;
    }

    /**
     * @return the isTiling
     */
    public boolean isTiling() {
        return isTiling;
    }

    /**
     * Resets the simulation matrix to an initial, clean state.
     */
    public void reset() {
        for (int i = 0; i < uu.length; i++) {
            uu[i] = 1.0f;
            vv[i] = 0.0f;
        }
    }

    public void seedImage(int[] pixels, int imgWidth, int imgHeight) {
        int xo = MathUtils.clip((width - imgWidth) / 2, 0, width - 1);
        int yo = MathUtils.clip((height - imgHeight) / 2, 0, height - 1);
        imgWidth = MathUtils.min(imgWidth, width);
        imgHeight = MathUtils.min(imgHeight, height);
        for (int y = 0; y < imgHeight; y++) {
            int i = y * imgWidth;
            for (int x = 0; x < imgWidth; x++) {
                if (0 < (pixels[i + x] & 0xff)) {
                    int idx = (yo + y) * width + xo + x;
                    uu[idx] = 0.5f;
                    vv[idx] = 0.25f;
                }
            }
        }
    }

    public void setCoefficients(float f, float k, float dU, float dV) {
        this.f = f;
        this.k = k;
        this.dU = dU;
        this.dV = dV;
    }

    /**
     * @param dU
     *            the diffuse U coefficient to set
     */
    public void setDiffuseU(float dU) {
        this.dU = dU;
    }

    /**
     * @param dV
     *            the diffuse V coefficient to set
     */
    public void setDiffuseV(float dV) {
        this.dV = dV;
    }

    /**
     * @param f
     *            the F coefficient to set
     */
    public void setF(float f) {
        this.f = f;
    }

    /**
     * @param k
     *            the K coefficient to set
     */
    public void setK(float k) {
        this.k = k;
    }

    /**
     * @param x
     * @param y
     * @param w
     * @param h
     */
    public void setRect(int x, int y, int w, int h) {
        int mix = MathUtils.clip(x - w / 2, 0, width);
        int max = MathUtils.clip(x + w / 2, 0, width);
        int miy = MathUtils.clip(y - h / 2, 0, height);
        int may = MathUtils.clip(y + h / 2, 0, height);
        for (int yy = miy; yy < may; yy++) {
            for (int xx = mix; xx < max; xx++) {
                int idx = yy * width + xx;
                uu[idx] = 0.5f;
                vv[idx] = 0.25f;
            }
        }
    }

    public void setRect(Rect r) {
        setRect((int) r.x, (int) r.y, (int) r.width, (int) r.height);
    }

    /**
     * @param isTiling
     *            the isTiling to set
     */
    public void setTiling(boolean isTiling) {
        this.isTiling = isTiling;
    }

    public void update(float t) {
        t = MathUtils.clip(t, 0, 1f);
        int w1 = width - 1;
        int h1 = height - 1;
        for (int y = 1; y < h1; y++) {
            for (int x = 1; x < w1; x++) {
                int idx = y * width + x;
                int top = idx - width;
                int bottom = idx + width;
                int left = idx - 1;
                int right = idx + 1;
                float currF = getFCoeffAt(x, y);
                float currK = getKCoeffAt(x, y);
                float currU = uu[idx];
                float currV = vv[idx];
                float d2 = currU * currV * currV;
                u[idx] = MathUtils
                        .max(0,
                                currU
                                        + t
                                        * ((dU
                                                * ((uu[right] + uu[left]
                                                        + uu[bottom] + uu[top]) - 4 * currU) - d2) + currF
                                                * (1.0f - currU)));
                v[idx] = MathUtils
                        .max(0,
                                currV
                                        + t
                                        * ((dV
                                                * ((vv[right] + vv[left]
                                                        + vv[bottom] + vv[top]) - 4 * currV) + d2) - currK
                                                * currV));
            }
        }

        if (isTiling) {
            int w2 = w1 - 1;
            int idxH1 = h1 * width;
            int idxH2 = (h1 - 1) * width;
            for (int x = 0; x < width; x++) {
                int left = (x == 0 ? w1 : x - 1);
                int right = (x == w1 ? 0 : x + 1);
                int idx = idxH1 + x;
                float cu = uu[x];
                float cv = vv[x];
                float cui = uu[idx];
                float cvi = vv[idx];
                float d = cu * cv * cv;
                u[x] = MathUtils
                        .max(0,
                                cu
                                        + t
                                        * ((dU
                                                * ((uu[right] + uu[left]
                                                        + uu[width + x] + cui) - 4 * cu) - d) + f
                                                * (1.0f - cu)));
                v[x] = MathUtils
                        .max(0,
                                cv
                                        + t
                                        * ((dV
                                                * ((vv[right] + vv[left]
                                                        + vv[width + x] + cvi) - 4 * cv) + d) - k
                                                * cv));
                d = cui * cvi * cvi;
                u[idx] = MathUtils
                        .max(0,
                                cui
                                        + t
                                        * ((dU
                                                * ((uu[idxH1 + right]
                                                        + uu[idxH1 + left] + cu + uu[idxH2
                                                        + x]) - 4 * cui) - d) + f
                                                * (1.0f - cui)));
                v[idx] = MathUtils
                        .max(0,
                                cvi
                                        + t
                                        * ((dU
                                                * ((vv[idxH1 + right]
                                                        + vv[idxH1 + left] + cv + vv[idxH2
                                                        + x]) - 4 * cvi) + d) - k
                                                * cvi));
            }

            for (int y = 0; y < height; y++) {
                int idx = y * width;
                int idxW1 = idx + w1;
                int idxW2 = idx + w2;
                float cu = uu[idx];
                float cv = vv[idx];
                float cui = uu[idxW1];
                float cvi = vv[idxW1];
                float d = cu * cv * cv;
                int up = (y == 0 ? h1 : y - 1) * width;
                int down = (y == h1 ? 0 : y + 1) * width;
                u[idx] = MathUtils
                        .max(0,
                                cu
                                        + t
                                        * ((dU
                                                * ((uu[idx + 1] + cui
                                                        + uu[down] + uu[up]) - 4 * cu) - d) + f
                                                * (1.0f - cu)));
                v[idx] = MathUtils
                        .max(0,
                                cv
                                        + t
                                        * ((dV
                                                * ((vv[idx + 1] + cvi
                                                        + vv[down] + vv[up]) - 4 * cv) + d) - k
                                                * cv));
                d = cui * cvi * cvi;
                u[idxW1] = MathUtils
                        .max(0,
                                cui
                                        + t
                                        * ((dU
                                                * ((cu + uu[idxW2]
                                                        + uu[down + w1] + uu[up
                                                        + w1]) - 4 * cui) - d) + f
                                                * (1.0f - cui)));
                v[idxW1] = MathUtils
                        .max(0,
                                cvi
                                        + t
                                        * ((dV
                                                * ((cv + vv[idxW2]
                                                        + vv[down + w1] + vv[up
                                                        + w1]) - 4 * cvi) + d) - k
                                                * cvi));
            }
        }
        System.arraycopy(u, 0, uu, 0, u.length);
        System.arraycopy(v, 0, vv, 0, v.length);
    }
}
