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

package toxi.geom.mesh.subdiv;

/**
 * Abstract parent class for all displacement subdivision strategies. It adds
 * common support for the displacement amplification value, which subclasses can
 * utilize.
 */
public abstract class DisplacementSubdivision extends SubdivisionStrategy {

    protected float amp;

    public DisplacementSubdivision(float amp) {
        this.amp = amp;
    }

    /**
     * @return the amp
     */
    public float getAmp() {
        return amp;
    }

    public DisplacementSubdivision invertAmp() {
        this.amp *= -1;
        return this;
    }

    public DisplacementSubdivision scaleAmp(float scale) {
        this.amp *= scale;
        return this;
    }

    /**
     * @param amp
     *            the amp to set
     * @return itself
     */
    public DisplacementSubdivision setAmp(float amp) {
        this.amp = amp;
        return this;
    }
}
