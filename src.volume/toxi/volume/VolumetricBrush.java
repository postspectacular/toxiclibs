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

import java.util.logging.Logger;

import toxi.geom.Vec3D;
import toxi.math.MathUtils;

public abstract class VolumetricBrush {

    protected static final Logger logger = Logger
            .getLogger(VolumetricBrush.class.getName());

    public static final BrushMode MODE_ADDITIVE = new AdditiveBrush();
    public static final BrushMode MODE_MULTIPLY = new MultiplyBrush();
    public static final BrushMode MODE_REPLACE = new ReplaceBrush();
    public static final BrushMode MODE_PEAK = new PeakBrush();

    protected VolumetricSpace volume;
    protected int cellRadiusX, cellRadiusY, cellRadiusZ;
    protected float stretchY, stretchZ;

    protected BrushMode brushMode = MODE_ADDITIVE;

    public VolumetricBrush(VolumetricSpace volume) {
        this.volume = volume;
    }

    public void drawAtAbsolutePos(Vec3D pos, float density) {
        float cx = MathUtils.clip((pos.x + volume.halfScale.x) / volume.scale.x
                * volume.resX1, 0, volume.resX1);
        float cy = MathUtils.clip((pos.y + volume.halfScale.y) / volume.scale.y
                * volume.resY1, 0, volume.resY1);
        float cz = MathUtils.clip((pos.z + volume.halfScale.z) / volume.scale.z
                * volume.resZ1, 0, volume.resZ1);
        drawAtGridPos(cx, cy, cz, density);
    }

    public abstract void drawAtGridPos(float cx, float cy, float cz,
            float density);

    public void setMode(BrushMode mode) {
        brushMode = mode;
    }

    public abstract void setSize(float radius);

    protected final void updateVoxel(int x, int y, int z, float cellVal) {
        int idx = volume.getIndexFor(x, y, z);
        volume.setVoxelAt(idx, brushMode.apply(volume.getVoxelAt(idx), cellVal));
    }
}