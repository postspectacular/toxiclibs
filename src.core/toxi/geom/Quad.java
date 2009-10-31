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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class Quad {

	@XmlElement(name = "point")
	public Vec3D[] vertices = new Vec3D[4];

	public Quad() {
	}

	public Quad(float x1, float y1, float x2, float y2, float x3, float y3,
			float x4, float y4) {
		vertices[0] = new Vec3D(x1, y1, 0);
		vertices[1] = new Vec3D(x2, y2, 0);
		vertices[2] = new Vec3D(x3, y3, 0);
		vertices[3] = new Vec3D(x4, y4, 0);
	}

	public Quad(Quad q) {
		for (int i = 0; i < 4; i++) {
			vertices[i] = new Vec3D(q.vertices[i]);
		}
	}

	public Quad(Vec3D a, Vec3D b, Vec3D c, Vec3D d) {
		vertices[0] = a;
		vertices[1] = b;
		vertices[2] = c;
		vertices[3] = d;
	}

	public Quad(Vec3D[] vertices, int vertOffset) {
		System.arraycopy(vertices, vertOffset, this.vertices, 0, 4);
	}
}
