/* 
 * Copyright (c) 2006, 2007 Karsten Schmidt
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
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package toxi.geom;

import toxi.geom.Vec3D;

public class Quad {
	public Vec3D[] vertices;

	public Quad(Vec3D[] vertices, int vertOffset) {
		this.vertices = new Vec3D[4];
		System.arraycopy(vertices, vertOffset, this.vertices, 0, 4);
	}

	public Quad(Quad q) {
		vertices = new Vec3D[4];
		for (int i = 0; i < 4; i++) {
			vertices[i] = new Vec3D(q.vertices[i]);
		}
	}

	public Quad(float x1, float y1, float x2, float y2, float x3, float y3,
			float x4, float y4) {
		vertices = new Vec3D[] { new Vec3D(x1, y1, 0), new Vec3D(x2, y2, 0),
				new Vec3D(x3, y3, 0), new Vec3D(x4, y4, 0) };
	}
}
