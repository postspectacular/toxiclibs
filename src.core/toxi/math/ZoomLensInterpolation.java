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

package toxi.math;

/**
 * This class provides an adjustable zoom lens to either bundle or dilate values
 * around a focal point within a given interval. For a example use cases, please
 * have a look at the provided ScaleMapDataViz and ZoomLens examples.
 */
public class ZoomLensInterpolation implements InterpolateStrategy {

    protected CircularInterpolation leftImpl = new CircularInterpolation();
    protected CircularInterpolation rightImpl = new CircularInterpolation();

    protected float lensPos;
    protected float lensStrength, absStrength;

    public ZoomLensInterpolation() {
        this(0.5f, 1);
    }

    public ZoomLensInterpolation(float lensPos, float lensStrength) {
        this.lensPos = lensPos;
        this.lensStrength = lensStrength;
        this.absStrength = MathUtils.abs(lensStrength);
        leftImpl.setFlipped(lensStrength > 0);
        rightImpl.setFlipped(lensStrength < 0);
    }

    public double interpolate(double min, double max, double t) {
        double val = min + (max - min) * t;
        if (t < lensPos) {
            val += (leftImpl.interpolate(min, min + (max - min) * lensPos, t
                    / lensPos) - val)
                    * absStrength;
        } else {
            val += (rightImpl.interpolate(min + (max - min) * lensPos, max,
                    (t - lensPos) / (1 - lensPos)) - val) * absStrength;
        }
        return val;
    }

    public float interpolate(float min, float max, float t) {
        float val = min + (max - min) * t;
        if (t < lensPos) {
            val += (leftImpl.interpolate(min, min + (max - min) * lensPos, t
                    / lensPos) - val)
                    * absStrength;
        } else {
            val += (rightImpl.interpolate(min + (max - min) * lensPos, max,
                    (t - lensPos) / (1 - lensPos)) - val) * absStrength;
        }
        return val;
    }

    public void setLensPos(float pos, float smooth) {
        lensPos += (MathUtils.clipNormalized(pos) - lensPos) * smooth;
    }

    public void setLensStrength(float str, float smooth) {
        lensStrength += (MathUtils.clip(str, -1, 1) - lensStrength) * smooth;
        absStrength = MathUtils.abs(lensStrength);
        leftImpl.setFlipped(lensStrength > 0);
        rightImpl.setFlipped(lensStrength < 0);
    }
}