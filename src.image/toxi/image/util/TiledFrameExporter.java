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

import processing.core.PApplet;
import processing.core.PImage;
import toxi.geom.Vec3D;

public class TiledFrameExporter {

    private PApplet parent;

    private PImage buffer;

    private Vec3D[] offsets;

    private double normTileSize;

    private double aspect;

    private int numTiles;

    private int tileID;

    private float subTileID;

    private boolean isTiling;

    private String fileName;

    TiledFrameExporter(PApplet p, int n) {
        parent = p;
        numTiles = n;
        buffer = new PImage(p.width * n, p.height * n);
        offsets = new Vec3D[numTiles * numTiles];
        normTileSize = 2.0 / numTiles;
        aspect = (double) p.height / p.width;
        int idx = 0;
        double y = 1 - normTileSize;
        while (idx < offsets.length) {
            double x = -1;
            for (int xi = 0; xi < numTiles; xi++) {
                offsets[idx++] = new Vec3D((float) x, (float) y, 0);
                x += normTileSize;
            }
            y -= normTileSize;
        }
    }

    public PImage getBuffer() {
        return buffer;
    }

    public int getCurrentTileID() {
        return tileID;
    }

    public float getProgress() {
        return (float) tileID / offsets.length;
    }

    public boolean isTiling() {
        return isTiling;
    }

    public void post() {
        if (isTiling) {
            subTileID += 0.5;
            if (subTileID > 1) {
                int x = tileID % numTiles;
                int y = tileID / numTiles;
                parent.loadPixels();
                // TODO add optional callback hook for post-processing tile
                buffer.set(x * parent.width, y * parent.height, parent.g);
                if (tileID == offsets.length - 1) {
                    buffer.save(parent.sketchPath(fileName + "_" + buffer.width
                            + "x" + buffer.height + ".png"));
                }
                subTileID = 0;
                isTiling = (++tileID < offsets.length);
            }
        }
    }

    public void pre() {
        if (isTiling) {
            setupTile(tileID);
        }
    }

    public void save(String fn) {
        fileName = fn;
        tileID = 0;
        subTileID = 0;
        isTiling = true;
    }

    protected void setupTile(int id) {
        Vec3D o = offsets[id];
        parent.frustum(o.x, o.x + (float) normTileSize, (float) (o.y * aspect),
                (float) (aspect * (o.y + normTileSize)), 0.01f, 10000.f);
    }
}
