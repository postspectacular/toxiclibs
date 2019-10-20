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

import toxi.geom.LineD3D;

public class WingedEdgeD extends LineD3D {

    public List<WEFaceD> faces = new ArrayList<WEFaceD>(2);
    public final int id;

    public WingedEdgeD(WEVertexD a, WEVertexD b, WEFaceD f, int id) {
        super(a, b);
        this.id = id;
        addFaceD(f);
    }

    public WingedEdgeD addFaceD(WEFaceD f) {
        faces.add(f);
        return this;
    }

    /**
     * @return the faces
     */
    public List<WEFaceD> getFaceDs() {
        return faces;
    }

    public WEVertexD getOtherEndFor(WEVertexD v) {
        if (a == v) {
            return (WEVertexD) b;
        }
        if (b == v) {
            return (WEVertexD) a;
        }
        return null;
    }

    public void remove() {
        for (WEFaceD f : faces) {
            f.edges.remove(this);
        }
        ((WEVertexD) a).edges.remove(this);
        ((WEVertexD) b).edges.remove(this);
    }

    public String toString() {
        return "id: " + id + " " + super.toString() + " f: " + faces.size();
    }
}
