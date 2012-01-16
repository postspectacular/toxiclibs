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

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import toxi.geom.AABB;
import toxi.geom.Line3D;
import toxi.geom.Vec3D;
import toxi.geom.mesh.Mesh3D;
import toxi.geom.mesh.WETriangleMesh;
import toxi.geom.mesh.WingedEdge;
import toxi.math.ScaleMap;
import toxi.util.datatypes.FloatRange;
import toxi.util.datatypes.IntegerRange;

public class MeshLatticeBuilder {

    protected static final Logger logger = Logger
            .getLogger(MeshLatticeBuilder.class.getName());

    public static WETriangleMesh build(WETriangleMesh mesh, int res,
            float stroke) {
        return build(mesh, res, new FloatRange(stroke, stroke));
    }

    public static WETriangleMesh build(WETriangleMesh mesh, int res,
            FloatRange stroke) {
        VolumetricSpace volume = buildVolume(mesh, res, stroke);
        IsoSurface surface = new HashIsoSurface(volume);
        mesh = (WETriangleMesh) surface.computeSurfaceMesh(new WETriangleMesh(
                "iso", 300000, 900000), 0.2f);
        logger.info("created lattice mesh: " + mesh);
        return mesh;
    }

    public static VolumetricSpace buildVolume(WETriangleMesh mesh, int res,
            float stroke) {
        return buildVolume(mesh, res, new FloatRange(stroke, stroke));
    }

    public static VolumetricSpace buildVolume(WETriangleMesh mesh, int res,
            FloatRange stroke) {
        MeshLatticeBuilder builder = new MeshLatticeBuilder(mesh
                .getBoundingBox().getExtent().scale(2), res, res, res, stroke);
        return builder.buildVolume(mesh);
    }

    protected IntegerRange voxRangeX, voxRangeY, voxRangeZ;
    protected FloatRange stroke;

    private VolumetricSpace volume;

    private float drawStep = 0.5f;

    private ScaleMap bboxToVoxelX;

    private ScaleMap bboxToVoxelY;
    private ScaleMap bboxToVoxelZ;

    public MeshLatticeBuilder(Vec3D scale, int res, float stroke) {
        this(scale, res, res, res, new FloatRange(stroke, stroke));
    }

    public MeshLatticeBuilder(Vec3D scale, int resX, int resY, int resZ,
            FloatRange stroke) {
        this.stroke = stroke;
        this.voxRangeX = new IntegerRange(1, resX - 2);
        this.voxRangeY = new IntegerRange(1, resY - 2);
        this.voxRangeZ = new IntegerRange(1, resZ - 2);
        volume = new VolumetricHashMap(scale, resX, resY, resZ, 0.1f);
    }

    public WETriangleMesh buildLattice(WETriangleMesh mesh, Mesh3D targetMesh,
            float isoValue) {
        if (volume == null) {
            volume = buildVolume(mesh);
        }
        if (targetMesh == null) {
            targetMesh = new WETriangleMesh();
        }
        IsoSurface surface = new HashIsoSurface(volume);
        mesh = (WETriangleMesh) surface
                .computeSurfaceMesh(targetMesh, isoValue);
        return mesh;
    }

    public VolumetricSpace buildVolume(WETriangleMesh mesh) {
        VolumetricBrush brush = new RoundBrush(volume, 1);
        brush.setMode(VolumetricBrush.MODE_PEAK);
        return buildVolume(mesh, brush);
    }

    public VolumetricSpace buildVolume(final WETriangleMesh mesh,
            final VolumetricBrush brush) {
        logger.info("creating lattice...");
        setMesh(mesh);
        List<Float> edgeLengths = new ArrayList<Float>(mesh.edges.size());
        for (WingedEdge e : mesh.edges.values()) {
            edgeLengths.add(e.getLength());
        }
        FloatRange range = FloatRange.fromSamples(edgeLengths);
        ScaleMap brushSize = new ScaleMap(range.min, range.max, stroke.min,
                stroke.max);
        for (WingedEdge e : mesh.edges.values()) {
            brush.setSize((float) brushSize.getClippedValueFor(e.getLength()));
            createLattice(brush, e, drawStep);
        }
        volume.closeSides();
        return volume;
    }

    public void createLattice(VolumetricBrush brush, Line3D l, float drawStep) {
        List<Vec3D> points = l.splitIntoSegments(null, drawStep, true);
        for (Vec3D p : points) {
            float x = (float) bboxToVoxelX.getClippedValueFor(p.x);
            float y = (float) bboxToVoxelY.getClippedValueFor(p.y);
            float z = (float) bboxToVoxelZ.getClippedValueFor(p.z);
            brush.drawAtGridPos(x, y, z, 1);
        }
    }

    /**
     * @return the drawStep
     */
    public float getDrawStep() {
        return drawStep;
    }

    /**
     * @return the volume
     */
    public VolumetricSpace getVolume() {
        return volume;
    }

    /**
     * Sets the distance between {@link VolumetricBrush} positions when tracing
     * mesh edges.
     * 
     * @param drawStep
     *            the drawStep to set
     */
    public void setDrawStepLength(float drawStep) {
        this.drawStep = drawStep;
    }

    public void setInputBounds(AABB box) {
        Vec3D bmin = box.getMin();
        Vec3D bmax = box.getMax();
        bboxToVoxelX = new ScaleMap(bmin.x, bmax.x, voxRangeX.min,
                voxRangeX.max);
        bboxToVoxelY = new ScaleMap(bmin.y, bmax.y, voxRangeY.min,
                voxRangeY.max);
        bboxToVoxelZ = new ScaleMap(bmin.z, bmax.z, voxRangeZ.min,
                voxRangeZ.max);
    }

    public void setMesh(WETriangleMesh mesh) {
        setInputBounds(mesh.getBoundingBox());
    }

    protected void setRangeMinMax(IntegerRange range, int min, int max,
            int maxRes) {
        // swap if necessary...
        if (min > max) {
            max ^= min;
            min ^= max;
            max ^= min;
        }
        if (min < 0 || min >= maxRes || max < 0 || max >= maxRes) {
            throw new IllegalArgumentException(
                    "voxel range min/max is out of bounds: " + min + "->" + max);
        }
        range.min = min;
        range.max = max;
    }

    /**
     * @param volume
     *            the volume to set
     */
    public void setVolume(VolumetricSpace volume) {
        this.volume = volume;
    }

    public MeshLatticeBuilder setVoxelRangeX(int min, int max) {
        setRangeMinMax(voxRangeX, min, max, volume.resX);
        return this;
    }

    public MeshLatticeBuilder setVoxelRangeY(int min, int max) {
        setRangeMinMax(voxRangeY, min, max, volume.resY);
        return this;
    }

    public MeshLatticeBuilder setVoxelRangeZ(int min, int max) {
        setRangeMinMax(voxRangeZ, min, max, volume.resZ);
        return this;
    }
}
