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

package toxi.physics;

import toxi.geom.Vec3D;

public class VerletSpring {
	public VerletParticle a, b;

	public float restLength;

	public float strength;

	protected boolean isALocked, isBLocked;

	protected float maxDelta;

	public VerletSpring(VerletParticle a, VerletParticle b, float len, float str) {
		this.a = a;
		this.b = b;
		restLength = len;
		strength = str;
	}

	void update() {
		Vec3D delta = b.sub(a);
		// add minute offset to avoid div-by-zero errors
		float dist = delta.magnitude() + 0.00001f;
		float normD = (dist - restLength) / dist;
		delta.scaleSelf(strength * normD).limit(maxDelta);
		if (!a.isLocked && !isALocked)
			a.addSelf(delta);
		if (!b.isLocked && !isBLocked)
			b.subSelf(delta);
	}

	public void setMaxMagnitude(float len) {
		maxDelta = len;
	}

	public void lockA(boolean s) {
		isALocked = s;
	}

	public void lockB(boolean s) {
		isBLocked = s;
	}
}
