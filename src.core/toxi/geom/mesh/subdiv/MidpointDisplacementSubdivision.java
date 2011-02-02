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

import java.util.ArrayList;
import java.util.List;

import toxi.geom.Vec3D;
import toxi.geom.mesh.WingedEdge;

/**
 * This subdivision strategy splits an edge in two equal halves at its mid
 * point. The midpoint itself is being displaced, however, in the direction of
 * the vector going from the configured reference point (often the mesh centroid
 * is used) towards the edge midpoint. The displacement amount is configurable
 * as fraction of the original edge length. So given that:
 * 
 * <pre>
 * M = edge midpoint
 * R = reference point
 * l = edge length
 * a = displacement amplification factor
 * 
 * D  = normalized(M-R)
 * M' = M + D * a * l
 * </pre>
 */
public class MidpointDisplacementSubdivision extends DisplacementSubdivision {

    public Vec3D centroid;

    public MidpointDisplacementSubdivision(Vec3D centroid, float amount) {
        super(amount);
        this.centroid = centroid;
    }

    @Override
    public List<Vec3D> computeSplitPoints(WingedEdge edge) {
        Vec3D mid = edge.getMidPoint();
        // Vec3D mid = edge.a.interpolateTo(edge.b, 0.25f);
        Vec3D n = mid.sub(centroid).normalizeTo(amp * edge.getLength());
        List<Vec3D> points = new ArrayList<Vec3D>(1);
        points.add(mid.addSelf(n));
        return points;
    }

}
