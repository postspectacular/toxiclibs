/*
 * Copyright (c) 2006-2008 Karsten Schmidt
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * http://creativecommons.org/licenses/LGPL/2.1/
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA
 */

package toxi.geom;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * This class enables group/set operations of multiple Vec3D's at once.
 * 
 * @author Karsten Schmidt
 * 
 */
public class PointList extends ArrayList<Vec3D> {

	public PointList() {
		super();
	}

	public PointList addSelf(Vec3D offset) {
		Iterator<Vec3D> i = iterator();
		while (i.hasNext()) {
			i.next().addSelf(offset);
		}
		return this;
	}

	public PointList scaleSelf(float factor) {
		Iterator<Vec3D> i = iterator();
		while (i.hasNext()) {
			i.next().scaleSelf(factor);
		}
		return this;
	}

	public PointList scaleSelf(Vec3D factor) {
		Iterator<Vec3D> i = iterator();
		while (i.hasNext()) {
			i.next().scaleSelf(factor);
		}
		return this;
	}

	public PointList subSelf(Vec3D offset) {
		Iterator<Vec3D> i = iterator();
		while (i.hasNext()) {
			i.next().subSelf(offset);
		}
		return this;
	}
}
