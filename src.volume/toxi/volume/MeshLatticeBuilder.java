/*
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

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import toxi.geom.Vec3D;
import toxi.geom.mesh.WETriangleMesh;
import toxi.geom.mesh.WingedEdge;
import toxi.math.ScaleMap;
import toxi.util.datatypes.FloatRange;

public class MeshLatticeBuilder {

    protected static final Logger logger = Logger
            .getLogger(MeshLatticeBuilder.class.getName());

    public static WETriangleMesh build(WETriangleMesh mesh, int res,
            float stroke) {
        return build(mesh, res, new FloatRange(stroke, stroke));
    }

    public static WETriangleMesh build(WETriangleMesh mesh, int res,
            FloatRange stroke) {
        logger.info("creating lattice...");
        VolumetricHashMap volume =
                new VolumetricHashMap(mesh.getBoundingBox().getExtent()
                        .scale(2), res, res, res, 0.33f);
        VolumetricBrush brush = new RoundBrush(volume, 1);
        List<Float> edgeLengths = new ArrayList<Float>(mesh.edges.size());
        for (WingedEdge e : mesh.edges.values()) {
            edgeLengths.add(e.getLength());
        }
        FloatRange range = FloatRange.fromSamples(edgeLengths);
        ScaleMap brushSize =
                new ScaleMap(range.min, range.max, stroke.min, stroke.max);
        for (WingedEdge e : mesh.edges.values()) {
            List<Vec3D> points = e.splitIntoSegments(null, 1, true);
            brush.setSize((float) brushSize.getClippedValueFor(e.getLength()));
            for (Vec3D p : points) {
                brush.drawAtAbsolutePos(p, 0.5f);
            }
        }
        logger.info("volume density: " + volume.getDensity());
        volume.closeSides();
        mesh = null;
        IsoSurface surface;
        surface = new HashIsoSurface(volume);
        mesh =
                (WETriangleMesh) surface.computeSurfaceMesh(new WETriangleMesh(
                        "iso", 300000, 900000), 0.2f);
        logger.info("created lattice mesh: " + mesh);
        volume = null;
        surface = null;
        return mesh;
    }

}
