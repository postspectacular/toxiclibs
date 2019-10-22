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

import toxi.geom.VecD3D;
import toxi.geom.mesh.WingedEdgeD;

/**
 * This subdivision strategy splits an edge in two equal halves at its mid
 * point. The midpoint itself is being displaced, however, in the average
 * direction of face normals associated with this edge. The displacement amount
 * is configurable as fraction of the original edge length. So given that:
 * 
 * <pre>
 * M = edge midpoint
 * N = averaged normal vector
 * l = edge length
 * a = displacement amplification factor
 * 
 * M' = M + N * a * l
 * </pre>
 */
public class NormalDisplacementSubdivisionD extends DisplacementSubdivisionD {

    public NormalDisplacementSubdivisionD(double amp) {
        super(amp);
    }

    @Override
    public List<VecD3D> computeSplitPointsD(WingedEdgeD edge) {
        VecD3D mid = edge.getMidPoint();
        VecD3D n = edge.faces.get(0).normal;
        if (edge.faces.size() > 1) {
            n.addSelf(edge.faces.get(1).normal);
        }
        n.normalizeTo(amp * edge.getLength());
        List<VecD3D> points = new ArrayList<VecD3D>(3);
        points.add(mid.addSelf(n));
        return points;
    }

}
