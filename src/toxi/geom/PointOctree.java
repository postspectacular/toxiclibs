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

import java.util.ArrayList;

import processing.core.PApplet;

@SuppressWarnings("unchecked")
public class PointOctree {

	/**
	 * tree recursion limit
	 */
	private float maxTreeDepth = 8;

	/**
	 * tree recursion limit, number of world units when cells are not subdivided
	 * any further
	 */
	private float minNodeSize = 4;

	protected PointOctree parent;

	protected PointOctree[] children;

	protected byte numChildren;

	protected ArrayList data;

	protected float dim, dim2, dimMag;

	protected Vec3D offset, centre;

	protected int depth = 0;

	public PointOctree(Vec3D o, float size) {
		this(null,o,size);
	}
	
	public PointOctree(PointOctree p, Vec3D o, float size) {
		parent = p;
		if (parent != null)
			depth = parent.depth + 1;
		offset = o;
		dim = size;
		dim2 = dim * 0.5f;
		centre = offset.add(new Vec3D(dim2, dim2, dim2));
		numChildren = 0;
	}

	public boolean isPointInside(Vec3D p) {
		return (p.x >= offset.x && p.x < offset.x + dim && p.y >= offset.y
				&& p.y < offset.y + dim && p.z >= offset.z && p.z < offset.z
				+ dim);
	}

	protected int getOctantID(Vec3D plocal) {
		return (plocal.x >= dim2 ? 1 : 0) + (plocal.y >= dim2 ? 2 : 0)
				+ (plocal.z >= dim2 ? 4 : 0);
	}

	public boolean addPoint(Vec3D p) {
		// check if point is inside cube
		if (isPointInside(p)) {
			// only add data to leaves for now
			if (depth == maxTreeDepth || dim <= minNodeSize) {
				if (data == null) {
					data = new ArrayList();
				}
				data.add(p);
				return true;
			} else {
				Vec3D plocal = p.sub(offset);
				if (children == null) {
					children = new PointOctree[8];
				}
				int octant = getOctantID(plocal);
				if (children[octant] == null) {
					Vec3D off = offset.add(new Vec3D((octant & 1) != 0 ? dim2
							: 0, (octant & 2) != 0 ? dim2 : 0,
							(octant & 4) != 0 ? dim2 : 0));
					children[octant] = new PointOctree(this, off, dim2);
					numChildren++;
				}
				return children[octant].addPoint(p);
			}
		} else {
			// println("point "+p+" is outside");
		}
		return false;
	}

	protected PointOctree getLeafForPoint(Vec3D p) {
		// if not a leaf node...
		if (isPointInside(p)) {
			if (numChildren > 0) {
				int octant = getOctantID(p.sub(offset));
				if (children[octant] != null) {
					return children[octant].getLeafForPoint(p);
				}
			} else if (data != null) {
				return this;
			}
		}
		return null;
	}

	public ArrayList getPointsWithinRadius(Vec3D p, float clipRadius) {
		return getPointsWithinSquaredRadius(p, clipRadius*clipRadius);
	}
	
	public ArrayList getPointsWithinSquaredRadius(Vec3D p, float clipRadius) {
		ArrayList results = null;
		float radius = dim2 * dim2 * 4;
		if (p.sub(centre).magSquared() < clipRadius + radius) {
			if (data != null) {
				for (int i = data.size() - 1; i >= 0; i--) {
					Vec3D q = (Vec3D) data.get(i);
					if (p.sub(q).magSquared() < clipRadius) {
						if (results == null) {
							results = new ArrayList();
						}
						results.add(q);
					}
				}
				return results;
			} else if (numChildren > 0) {
				results = new ArrayList();
				for (int i = 0; i < 8; i++) {
					if (children[i] != null) {
						ArrayList points = children[i]
								.getPointsWithinSquaredRadius(p, clipRadius);
						if (points != null)
							results.addAll(points);
					}
				}
			}
		}
		return results;
	}

	public void draw(PApplet app) {
		if (numChildren > 0) {
			app.noFill();
			app.stroke(depth * 24, 50);
			app.pushMatrix();
			app.translate(centre.x, centre.y, centre.z);
			app.box(dim);
			app.popMatrix();
			for (int i = 0; i < 8; i++) {
				if (children[i] != null)
					children[i].draw(app);
			}
		}
	}

	public String toString() {
		return "<octree> offset: " + offset + " size: " + dim;
	}

	public float getMaxDepth() {
		return maxTreeDepth;
	}

	public void setMaxDepth(float maxTreeDepth) {
		this.maxTreeDepth = maxTreeDepth;
	}

	public float getMinNodeSize() {
		return minNodeSize;
	}

	public void setMinNodeSize(float minNodeSize) {
		this.minNodeSize = minNodeSize;
	}
}
