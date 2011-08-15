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

package toxi.volume;

import java.util.logging.Level;

import toxi.math.MathUtils;

public class RoundBrush extends VolumetricBrush {

    protected float radius, radSquared;

    /**
     * Creates a new spherical brush to work on the given volume.
     * 
     * @param volume
     *            VolumetricSpaceArray instance
     * @param radius
     *            radius in world units
     */
    public RoundBrush(VolumetricSpace volume, float radius) {
        super(volume);
        setSize(radius);
    }

    @Override
    public void drawAtGridPos(float cx, float cy, float cz, float density) {
        int minX = MathUtils.max(Math.round(cx - cellRadiusX), 0);
        int minY = MathUtils.max(Math.round(cy - cellRadiusY), 0);
        int minZ = MathUtils.max(Math.round(cz - cellRadiusZ), 0);
        int maxX = MathUtils.min(Math.round(cx + cellRadiusX), volume.resX);
        int maxY = MathUtils.min(Math.round(cy + cellRadiusY), volume.resY);
        int maxZ = MathUtils.min(Math.round(cz + cellRadiusZ), volume.resZ);
        for (int z = minZ; z < maxZ; z++) {
            float dz = (z - cz) * stretchZ;
            dz *= dz;
            for (int y = minY; y < maxY; y++) {
                float dyz = (y - cy) * stretchY;
                dyz = dyz * dyz + dz;
                for (int x = minX; x < maxX; x++) {
                    float dx = x - cx;
                    float d = (float) Math.sqrt(dx * dx + dyz);
                    if (d <= cellRadiusX) {
                        float cellVal = (1 - d / cellRadiusX) * density;
                        updateVoxel(x, y, z, cellVal);
                    }
                }
            }
        }
    }

    @Override
    public void setSize(float radius) {
        this.radius = radius;
        this.cellRadiusX = (int) (radius / volume.scale.x * volume.resX + 1);
        this.cellRadiusY = (int) (radius / volume.scale.y * volume.resY + 1);
        this.cellRadiusZ = (int) (radius / volume.scale.z * volume.resZ + 1);
        stretchY = (float) cellRadiusX / cellRadiusY;
        stretchZ = (float) cellRadiusX / cellRadiusZ;
        if (logger.isLoggable(Level.FINE)) {
            logger.fine("new brush size: " + radius);
        }
    }
}
