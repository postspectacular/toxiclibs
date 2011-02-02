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

import toxi.geom.Triangle3D;
import toxi.geom.Vec2D;
import toxi.geom.Vec3D;

public class Face {

    public Vertex a, b, c;
    public Vec2D uvA, uvB, uvC;
    public Vec3D normal;

    public Face(Vertex a, Vertex b, Vertex c) {
        this.a = a;
        this.b = b;
        this.c = c;
        normal = a.sub(c).crossSelf(a.sub(b)).normalize();
        a.addFaceNormal(normal);
        b.addFaceNormal(normal);
        c.addFaceNormal(normal);
    }

    public Face(Vertex a, Vertex b, Vertex c, Vec2D uvA, Vec2D uvB, Vec2D uvC) {
        this(a, b, c);
        this.uvA = uvA;
        this.uvB = uvB;
        this.uvC = uvC;
    }

    public void computeNormal() {
        normal = a.sub(c).crossSelf(a.sub(b)).normalize();
    }

    public void flipVertexOrder() {
        Vertex t = a;
        a = b;
        b = t;
        normal.invert();
    }

    public Vec3D getCentroid() {
        return a.add(b).addSelf(c).scale(1f / 3);
    }

    public final Vertex[] getVertices(Vertex[] verts) {
        if (verts != null) {
            verts[0] = a;
            verts[1] = b;
            verts[2] = c;
        } else {
            verts = new Vertex[] { a, b, c };
        }
        return verts;
    }

    public String toString() {
        return getClass().getName() + " " + a + ", " + b + ", " + c;
    }

    /**
     * Creates a generic {@link Triangle3D} instance using this face's vertices.
     * The new instance is made up of copies of the original vertices and
     * manipulating them will not impact the originals.
     * 
     * @return triangle copy of this mesh face
     */
    public Triangle3D toTriangle() {
        return new Triangle3D(a.copy(), b.copy(), c.copy());
    }
}