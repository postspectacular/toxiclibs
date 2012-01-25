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

package toxi.sim.erosion;

/**
 * For each neighbour it's computed the difference between the processed cell
 * and the neighbour:
 * 
 * <pre>
 * d[i] = h - h[i];
 * </pre>
 * 
 * the maximum positive difference is stored in d_max, and the sum of all the
 * positive differences that are bigger than T (this numer is n), the talus
 * angle, is stored in d_tot.
 * 
 * Now it's possible to update all the n cells (where d[i] is bigger than T)
 * using this formula:
 * 
 * <pre>
 * h[i] = h[i] + c * (d_max - T) * (d[i] / d_tot);
 * </pre>
 * 
 * and the main cell with this other formula:
 * 
 * <pre>
 * h = h - (d_max - (n * d_max * T / d_tot));
 * </pre>
 * 
 * The Talus angle T is a threshold that determines which slopes are affected by
 * the erosion, instead the c constant determines how much material is eroded.
 */
public class TalusAngleErosion extends ErosionFunction {

    private float theta;
    private float amount;

    /**
     * @param theta
     *            talus angle
     * @param amount
     *            material transport amount
     */
    public TalusAngleErosion(float theta, float amount) {
        this.theta = theta;
        this.amount = amount;
    }

    @Override
    public void erodeAt(int x, int y) {
        int idx = y * width + x;
        float maxD = 0;
        float sumD = 0;
        int n = 0;
        for (int i = 0; i < 9; i++) {
            h[i] = elevation[idx + off[i]];
            d[i] = elevation[idx] - h[i];
            if (d[i] > theta) {
                sumD += d[i];
                n++;
                if (d[i] > maxD) {
                    maxD = d[i];
                }
            }
        }
        if (sumD > 0) {
            elevation[idx] -= (maxD - (n * maxD * theta / sumD));
            for (int i = 0; i < 9; i++) {
                if (d[i] > theta) {
                    elevation[idx + off[i]] = h[i] + amount * (maxD - theta)
                            * (d[i] / sumD);
                }
            }
        }
    }

    /**
     * @return the amount
     */
    public float getAmount() {
        return amount;
    }

    /**
     * @return the theta
     */
    public float getTheta() {
        return theta;
    }

    /**
     * @param amount
     *            the amount to set
     */
    public void setAmount(float amount) {
        this.amount = amount;
    }

    /**
     * @param theta
     *            the theta to set
     */
    public void setTheta(float theta) {
        this.theta = theta;
    }

    public String toString() {
        return getClass().getName() + ": theta=" + theta + " amount=" + amount;
    }
}
