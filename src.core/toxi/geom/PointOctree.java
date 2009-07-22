/*
 * Copyright (c) 2006, 2007 Karsten Schmidt
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
import java.util.Collection;

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
	 * alternative tree recursion limit, number of world units when cells are
	 * not subdivided any further
	 */
	protected float minNodeSize = 4;

	/**
	 * 
	 */
	protected PointOctree parent;

	protected PointOctree[] children;

	protected byte numChildren;

	protected ArrayList<Vec3D> data;

	protected float dim, dim2;

	protected Vec3D offset;

	protected int depth = 0;

	private boolean isAutoReducing = false;

	/**
	 * Constructs a new PointOctree node within the AABB cube volume: {o.x, o.y,
	 * o.z} ... {o.x+size, o.y+size, o.z+size}
	 * 
	 * @param p
	 *            parent node
	 * @param o
	 *            tree origin
	 * @param halfSize
	 *            half length of the tree volume along a single axis
	 */
	private PointOctree(PointOctree p, Vec3D o, float halfSize) {
		super(o.add(halfSize, halfSize, halfSize), new Vec3D(halfSize,
				halfSize, halfSize));
		parent = p;
		if (parent != null) {
			depth = parent.depth + 1;
		}
		dim = halfSize * 2;
		dim2 = halfSize;
		offset = o;
		numChildren = 0;
	}

	/**
	 * Constructs a new PointOctree node within the AABB cube volume: {o.x, o.y,
	 * o.z} ... {o.x+size, o.y+size, o.z+size}
	 * 
	 * @param o
	 *            tree origin
	 * @param size
	 *            size of the tree volume along a single axis
	 */
	public PointOctree(Vec3D o, float size) {
		this(null, o, size / 2);
	}

	/**
	 * Adds all points of the collection to the octree. IMPORTANT: Points need
	 * be of type Vec3D or have subclassed it.
	 * 
	 * @param points
	 *            point collection
	 * @return true, if all points have been added successfully.
	 */
	public boolean addAll(Collection<Vec3D> points) {
		boolean addedAll = true;
		for (Vec3D p : points) {
			addedAll &= addPoint(p);
		}
		return addedAll;
	}

	/**
	 * Adds a new point/particle to the tree structure. All points are stored
	 * within leaf nodes only. The tree implementation is using lazy
	 * instantiation for all intermediate tree levels.
	 * 
	 * @param p
	 * @return true, if point has been added successfully
	 */
	public boolean addPoint(Vec3D p) {
		// check if point is inside cube
		if (p.isInAABB(this)) {
			// only add data to leaves for now
			if (dim2 <= minNodeSize) {
				if (data == null) {
					data = new ArrayList<Vec3D>();
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
					children[octant] = new PointOctree(this, off, dim2 * 0.5f);
					numChildren++;
				}
				return children[octant].addPoint(p);
			}
		}
		return false;
	}

	public void empty() {
		numChildren = 0;
		children = null;
		data = null;
	}

	/**
	 * @return a copy of the child nodes array
	 */
	public PointOctree[] getChildren() {
		PointOctree[] clones = new PointOctree[8];
		System.arraycopy(children, 0, clones, 0, 8);
		return clones;
	}

	/**
	 * Finds the leaf node which spatially relates to the given point
	 * 
	 * @param p
	 *            point to check
	 * @return leaf node or null if point is outside the tree dimensions
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
	 * Returns the minimum size of nodes (in world units). This value acts as
	 * tree recursion limit since nodes smaller than this size are not
	 * subdivided further. Leaf node are always smaller or equal to this size.
	 * 
	 * @return the minimum size of tree nodes
	 */
	public float getMinNodeSize() {
		return minNodeSize;
	}

	public float getNodeSize() {
		return dim;
	}

	/**
	 * @return the number of child nodes (max. 8)
	 */
	public int getNumChildren() {
		return numChildren;
	}

	/**
	 * Computes the local child octant/cube index for the given point
	 * 
	 * @param plocal
	 *            point in the node-local coordinate system
	 * @return octant index
	 */
	protected final int getOctantID(Vec3D plocal) {
		return (plocal.x >= dim2 ? 1 : 0) + (plocal.y >= dim2 ? 2 : 0)
				+ (plocal.z >= dim2 ? 4 : 0);
	}

	/**
	 * Selects all stored points within the given axis-aligned bounding box.
	 * 
	 * @param b
	 *            AABB
	 * @return all points with the box volume
	 */
	public ArrayList<Vec3D> getPointsWithinBox(AABB b) {
		ArrayList<Vec3D> results = null;
		if (this.intersectsBox(b)) {
			if (data != null) {
				for (Vec3D q : data) {
					if (q.isInAABB(b)) {
						if (results == null) {
							results = new ArrayList<Vec3D>();
						}
						results.add(q);
					}
				}
			} else if (numChildren > 0) {
				for (int i = 0; i < 8; i++) {
					if (children[i] != null) {
						ArrayList<Vec3D> points = children[i]
								.getPointsWithinBox(b);
						if (points != null) {
							if (results == null) {
								results = new ArrayList<Vec3D>();
							}
							results.addAll(points);
						}
					}
				}
			}
		}
		return results;
	}

	/**
	 * Selects all stored points within the given sphere volume
	 * 
	 * @param s
	 *            sphere
	 * @return selected points
	 */
	public ArrayList<Vec3D> getPointsWithinSphere(Sphere s) {
		ArrayList<Vec3D> results = null;
		if (this.intersectsSphere(s)) {
			if (data != null) {
				for (Vec3D q : data) {
					if (s.containsPoint(q)) {
						if (results == null) {
							results = new ArrayList<Vec3D>();
						}
						results.add(q);
					}
				}
			} else if (numChildren > 0) {
				for (int i = 0; i < 8; i++) {
					if (children[i] != null) {
						ArrayList<Vec3D> points = children[i]
								.getPointsWithinSphere(s);
						if (points != null) {
							if (results == null) {
								results = new ArrayList<Vec3D>();
							}
							results.addAll(points);
						}
					}
				}
			}
		}
		return results;
	}

	/**
	 * Selects all stored points within the given sphere volume
	 * 
	 * @param sphereOrigin
	 * @param clipRadius
	 * @return selected points
	 */
	public ArrayList<Vec3D> getPointsWithinSphere(Vec3D sphereOrigin,
			float clipRadius) {
		return getPointsWithinSphere(new Sphere(sphereOrigin, clipRadius));
	}

	private void reduceBranch() {
		if (data != null && data.size() == 0) {
			data = null;
		}
		if (numChildren > 0) {
			for (int i = 0; i < 8; i++) {
				if (children[i] != null && children[i].data == null) {
					children[i] = null;
				}
			}
		}
		if (parent != null) {
			parent.reduceBranch();
		}
	}

	/**
	 * Removes a point from the tree and (optionally) tries to release memory by
	 * reducing now empty sub-branches.
	 * 
	 * @param p
	 *            point to delete
	 * @return true, if the point was found & removed
	 */
	public boolean remove(Vec3D p) {
		boolean found = false;
		PointOctree leaf = getLeafForPoint(p);
		if (leaf != null) {
			if (leaf.data.remove(p)) {
				found = true;
				if (isAutoReducing && leaf.data.size() == 0) {
					leaf.reduceBranch();
				}
			}
		}
		return found;
	}

	public void removeAll(Collection<Vec3D> points) {
		for (Vec3D p : points) {
			remove(p);
		}
	}

	/**
	 * @param minNodeSize
	 */
	public void setMinNodeSize(float minNodeSize) {
		this.minNodeSize = minNodeSize;
	}

	/**
	 * Enables/disables auto reduction of branches after points have been
	 * deleted from the tree. Turned off by default.
	 * 
	 * @param state
	 *            true, to enable feature
	 */
	public void setTreeAutoReduction(boolean state) {
		isAutoReducing = state;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see toxi.geom.AABB#toString()
	 */
	public String toString() {
		return "<octree> offset: " + super.toString() + " size: " + dim;
	}
}
