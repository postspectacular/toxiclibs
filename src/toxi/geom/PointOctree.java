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

/**
 * Implements a spatial subdivision tree to work efficiently with large numbers
 * of 3D particles. This octree can only be used for particle type objects and
 * does NOT support 3D mesh geometry as other forms of Octrees do.
 * 
 * For further reference also see the OctreeDemo in the /examples folder.
 * 
 */
public class PointOctree extends AABB {

	/**
	 * tree recursion limit
	 */
	private float maxTreeDepth = 6;

	/**
	 * alternative tree recursion limit, number of world units when cells are
	 * not subdivided any further
	 */
	private float minNodeSize = 4;

	/**
	 * 
	 */
	protected PointOctree parent;

	protected PointOctree[] children;

	protected byte numChildren;

	protected ArrayList data;

	protected float dim, dim2, dimMag;

	protected Vec3D offset;

	protected int depth = 0;

	/**
	 * Constructs a new PointOctree node within the AABB cube volume:
	 * {o.x, o.y, o.z} ... {o.x+size, o.y+size, o.z+size}
	 * 
	 * @param o
	 *            tree origin
	 * @param size
	 *            size of the tree volume along a single axis
	 */
	public PointOctree(Vec3D o, float size) {
		this(null, o, size);
	}

	/**
	 * Constructs a new PointOctree node within the AABB cube volume:
	 * {o.x, o.y, o.z} ... {o.x+size, o.y+size, o.z+size}
	 * 
	 * @param p parent node
	 * @param o tree origin
	 * @param size size of the tree volume along a single axis
	 */
	public PointOctree(PointOctree p, Vec3D o, float size) {
		super(o.add(size * .5f, size * .5f, size * .5f), new Vec3D(size, size,
				size).scale(0.5f));
		parent = p;
		if (parent != null)
			depth = parent.depth + 1;
		dim = size;
		dim2 = dim * 0.5f;
		offset = o;
		numChildren = 0;
	}

	/**
	 * Computes the local octant for the given point
	 * @param plocal point in the node-local coordinate system
	 * @return octant index
	 */
	protected int getOctantID(Vec3D plocal) {
		return (plocal.x >= dim2 ? 1 : 0) + (plocal.y >= dim2 ? 2 : 0)
				+ (plocal.z >= dim2 ? 4 : 0);
	}

	/**
	 * Adds a new point/particle to the tree structure. All points are stored
	 * within leaf nodes only. The tree implementation is using lazy
	 * instantiation for all intermediate tree levels.
	 * 
	 * @param p
	 * @return
	 */
	public boolean addPoint(Vec3D p) {
		// check if point is inside cube
		if (p.isInAABB(this)) {
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

	/**
	 * Finds the leaf node which spatially relates to the given point
	 * 
	 * @param p
	 *            point to check
	 * @return leaf node
	 */
	protected PointOctree getLeafForPoint(Vec3D p) {
		// if not a leaf node...
		if (p.isInAABB(this)) {
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

	/**
	 * Selects all stored points within the given sphere volume
	 * 
	 * @param sphereOrigin
	 * @param clipRadius
	 * @return selected points
	 */
	public ArrayList getPointsWithinSphere(Vec3D sphereOrigin, float clipRadius) {
		return getPointsWithinSphere(new Sphere(sphereOrigin, clipRadius));
	}

	/**
	 * Selects all stored points within the given sphere volume
	 * 
	 * @param s
	 *            sphere
	 * @return selected points
	 */
	public ArrayList getPointsWithinSphere(Sphere s) {
		ArrayList results = null;
		if (this.intersectsSphere(s)) {
			if (data != null) {
				for (int i = data.size() - 1; i >= 0; i--) {
					Vec3D q = (Vec3D) data.get(i);
					if (q.isInSphere(s)) {
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
						ArrayList points = children[i].getPointsWithinSphere(s);
						if (points != null)
							results.addAll(points);
					}
				}
			}
		}
		return results;
	}

	/**
	 * Selects all stored points within the given axis-aligned bounding box.
	 * 
	 * @param b
	 *            AABB
	 * @return all points with the box volume
	 */
	public ArrayList getPointsWithinBox(AABB b) {
		ArrayList results = null;
		if (this.intersectsBox(b)) {
			if (data != null) {
				for (int i = data.size() - 1; i >= 0; i--) {
					Vec3D q = (Vec3D) data.get(i);
					if (q.isInAABB(b)) {
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
						ArrayList points = children[i].getPointsWithinBox(b);
						if (points != null)
							results.addAll(points);
					}
				}
			}
		}
		return results;
	}

	// FIXME remove PApplet dependency
	/**
	 * @param app
	 */
	public void draw(PApplet app) {
		if (numChildren > 0) {
			app.noFill();
			app.stroke(depth * 24, 50);
			app.pushMatrix();
			app.translate(x, y, z);
			app.box(dim);
			app.popMatrix();
			for (int i = 0; i < 8; i++) {
				if (children[i] != null)
					children[i].draw(app);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see toxi.geom.AABB#toString()
	 */
	public String toString() {
		return "<octree> offset: " + super.toString() + " size: " + dim;
	}

	/**
	 * Returns maximum recursion level (tree depth). Together with the minimum
	 * node size this level acts as a recursion limit. This value should be
	 * adjusted based on the overall size of the tree volume and object density.
	 * Default value is 6.
	 * 
	 * @return maximum tree depth limit
	 */
	public float getMaxDepth() {
		return maxTreeDepth;
	}

	/**
	 * @param maxTreeDepth
	 */
	public void setMaxDepth(float maxTreeDepth) {
		this.maxTreeDepth = maxTreeDepth;
	}

	/**
	 * Returns the minimum size of nodes (in world units). This value acts as
	 * tree recursion limit since nodes smaller than this size are not
	 * subdivided further. Leaf node are always smaller or equal to this size.
	 * 
	 * @return the minimum size of tree nodes
	 */
	public float getMinNodeSize() {
		return minNodeSize;
	}

	/**
	 * @param minNodeSize
	 */
	public void setMinNodeSize(float minNodeSize) {
		this.minNodeSize = minNodeSize;
	}
}
