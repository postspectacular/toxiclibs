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
import java.util.List;

import toxi.geom.VecD2D;

public final class WEFaceD extends FaceD {

    public List<WingedEdgeD> edges = new ArrayList<WingedEdgeD>(3);

    public WEFaceD(WEVertexD a, WEVertexD b, WEVertexD c) {
        super(a, b, c);
    }

    public WEFaceD(WEVertexD a, WEVertexD b, WEVertexD c, VecD2D uvA, VecD2D uvB, VecD2D uvC) {
        super(a, b, c, uvA, uvB, uvC);
    }

    public void addEdge(WingedEdgeD e) {
        edges.add(e);
    }

    /**
     * @return the edges
     */
    public List<WingedEdgeD> getEdges() {
        return edges;
    }

    public final WEVertexD[] getVertices(WEVertexD[] verts) {
        if (verts != null) {
            verts[0] = (WEVertexD) a;
            verts[1] = (WEVertexD) b;
            verts[2] = (WEVertexD) c;
        } else {
            verts = new WEVertexD[] { (WEVertexD) a, (WEVertexD) b, (WEVertexD) c };
        }
        return verts;
    }
}