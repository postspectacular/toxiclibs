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

public class BoxBrush extends VolumetricBrush {

    public BoxBrush(VolumetricSpace volume, float size) {
        super(volume);
        setSize(size);
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
            for (int y = minY; y < maxY; y++) {
                for (int x = minX; x < maxX; x++) {
                    updateVoxel(x, y, z, density);
                }
            }
        }
    }

    @Override
    public void setSize(float size) {
        setSize(size, size, size);
    }

    public void setSize(float sizeX, float sizeY, float sizeZ) {
        this.cellRadiusX = (int) (sizeX * 0.5f / volume.scale.x * volume.resX + 1);
        this.cellRadiusY = (int) (sizeY * 0.5f / volume.scale.y * volume.resY + 1);
        this.cellRadiusZ = (int) (sizeZ * 0.5f / volume.scale.z * volume.resZ + 1);
        stretchY = (float) cellRadiusX / cellRadiusY;
        stretchZ = (float) cellRadiusX / cellRadiusZ;
        if (logger.isLoggable(Level.FINE)) {
            logger.fine("new brush size: " + cellRadiusX + "x" + cellRadiusY
                    + "x" + cellRadiusZ);
        }
    }

}
