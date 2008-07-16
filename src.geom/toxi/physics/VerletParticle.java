package toxi.physics;

/* 
 * Copyright (c) 2008 Karsten Schmidt
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

import toxi.geom.Vec3D;

public class VerletParticle extends Vec3D {
	protected Vec3D prev, temp;

	public float weight = 1.0f;

	protected boolean isLocked;

	public VerletParticle(float x, float y, float z) {
		super(x, y, z);
		prev = new Vec3D(this);
		temp = new Vec3D();
	}

	public VerletParticle(Vec3D v) {
		this(v.x,v.y,v.z);
	}

	void update(float force) {
		if (!isLocked) {
			temp.set(this);
			addSelf(sub(prev).scaleSelf(force));
			prev.set(temp);
		}
	}

	public void lock() {
		isLocked = true;
	}

	public void unlock() {
		prev.set(this);
		isLocked = false;
	}
}
