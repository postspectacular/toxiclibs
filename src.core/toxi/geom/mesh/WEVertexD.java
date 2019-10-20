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

package toxi.geom.mesh;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import toxi.geom.ReadonlyVecD3D;
import toxi.geom.VecD3D;

public class WEVertexD extends VertexD {

    public List<WingedEdgeD> edges = new ArrayList<WingedEdgeD>(4);

    public WEVertexD(VecD3D v, int id) {
        super(v, id);
    }

    public void addEdge(WingedEdgeD e) {
        edges.add(e);
    }

    public List<WingedEdgeD> getEdges() {
        return edges;
    }

    public WEVertexD getNeighborInDirection(ReadonlyVecD3D dir, double tolerance) {
        WEVertexD closest = null;
        double delta = 1 - tolerance;
        for (WEVertexD n : getNeighbors()) {
            double d = n.sub(this).normalize().dot(dir);
            if (d > delta) {
                closest = n;
                delta = d;
            }
        }
        return closest;
    }

    public List<WEVertexD> getNeighbors() {
        List<WEVertexD> neighbors = new ArrayList<WEVertexD>(edges.size());
        for (WingedEdgeD e : edges) {
            neighbors.add(e.getOtherEndFor(this));
        }
        return neighbors;
    }

    /**
     * Returns a list of all faces this vertex belongs to.
     * 
     * @return face list
     */
    public List<WEFaceD> getRelatedFaces() {
        Set<WEFaceD> faces = new HashSet<WEFaceD>(edges.size() * 2);
        for (WingedEdgeD e : edges) {
            faces.addAll(e.faces);
        }
        return new ArrayList<WEFaceD>(faces);
    }

    public void removeEdge(WingedEdge e) {
        edges.remove(e);
    }

    public String toString() {
        return id + " {" + x + "," + y + "," + z + "}";
    }
}