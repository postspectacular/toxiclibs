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
 * This subdivision strategy splits an edge in three equal parts using two split
 * points at 33% and 66% of the edge. The split points themselves are being
 * displaced, however, in the direction of the vector going from the configured
 * reference point (often the mesh centroid is used) towards each split point.
 * The displacement amount is configurable as fraction of the original edge
 * length. So given that:
 * 
 * <pre>
 * S = edge split point
 * R = reference point
 * l = edge length
 * a = displacement amplification factor
 * 
 * S' = S + (S-R) * a * l
 * </pre>
 */
public class DualDisplacementSubdivision extends SubdivisionStrategy {

    public Vec3D centroid;
    public float ampA, ampB;

    public DualDisplacementSubdivision(Vec3D centroid, float ampA, float ampB) {
        this.centroid = centroid;
        this.ampA = ampA;
        this.ampB = ampB;
    }

    public List<Vec3D> computeSplitPoints(WingedEdge edge) {
        List<Vec3D> mid = new ArrayList<Vec3D>(2);
        float len = edge.getLength();
        Vec3D a = edge.a.interpolateTo(edge.b, 0.3333f);
        a.addSelf(a.sub(centroid).normalizeTo(ampA * len));
        Vec3D b = edge.a.interpolateTo(edge.b, 0.6666f);
        b.addSelf(b.sub(centroid).normalizeTo(ampB * len));
        mid.add(a);
        mid.add(b);
        return mid;
    }
}
