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

package toxi.geom;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Implements a spatial subdivision tree to work efficiently with large numbers
 * of 3D particles. This octree can only be used for particle type objects and
 * does NOT support 3D mesh geometry as other forms of Octrees do.
 * 
 * For further reference also see the OctreeDemo in the /examples folder.
 * 
 */
public class PointOctree extends AABB implements Shape3D {

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

    protected ArrayList<Vec3D> points;

    protected float size, halfSize;

    protected Vec3D offset;

    private int depth = 0;

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
        this.parent = p;
        this.halfSize = halfSize;
        this.size = halfSize * 2;
        this.offset = o;
        this.numChildren = 0;
        if (parent != null) {
            depth = parent.depth + 1;
            minNodeSize = parent.minNodeSize;
        }
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
        if (containsPoint(p)) {
            // only add points to leaves for now
            if (halfSize <= minNodeSize) {
                if (points == null) {
                    points = new ArrayList<Vec3D>();
                }
                points.add(p);
                return true;
            } else {
                Vec3D plocal = p.sub(offset);
                if (children == null) {
                    children = new PointOctree[8];
                }
                int octant = getOctantID(plocal);
                if (children[octant] == null) {
                    Vec3D off = offset.add(new Vec3D(
                            (octant & 1) != 0 ? halfSize : 0,
                            (octant & 2) != 0 ? halfSize : 0,
                            (octant & 4) != 0 ? halfSize : 0));
                    children[octant] = new PointOctree(this, off,
                            halfSize * 0.5f);
                    numChildren++;
                }
                return children[octant].addPoint(p);
            }
        }
        return false;
    }

    /**
     * Applies the given {@link OctreeVisitor} implementation to this node and
     * all of its children.
     */
    public void applyVisitor(OctreeVisitor visitor) {
        visitor.visitNode(this);
        if (numChildren > 0) {
            for (PointOctree c : children) {
                if (c != null) {
                    c.applyVisitor(visitor);
                }
            }
        }
    }

    public boolean containsPoint(ReadonlyVec3D p) {
        return p.isInAABB(this);
    }

    public void empty() {
        numChildren = 0;
        children = null;
        points = null;
    }

    /**
     * @return a copy of the child nodes array
     */
    public PointOctree[] getChildren() {
        if (children != null) {
            PointOctree[] clones = new PointOctree[8];
            System.arraycopy(children, 0, clones, 0, 8);
            return clones;
        }
        return null;
    }

    /**
     * @return the depth
     */
    public int getDepth() {
        return depth;
    }

    /**
     * Finds the leaf node which spatially relates to the given point
     * 
     * @param p
     *            point to check
     * @return leaf node or null if point is outside the tree dimensions
     */
    public PointOctree getLeafForPoint(ReadonlyVec3D p) {
        // if not a leaf node...
        if (p.isInAABB(this)) {
            if (numChildren > 0) {
                int octant = getOctantID(p.sub(offset));
                if (children[octant] != null) {
                    return children[octant].getLeafForPoint(p);
                }
            } else if (points != null) {
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
        return size;
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
        return (plocal.x >= halfSize ? 1 : 0) + (plocal.y >= halfSize ? 2 : 0)
                + (plocal.z >= halfSize ? 4 : 0);
    }

    /**
     * @return the offset
     */
    public ReadonlyVec3D getOffset() {
        return offset;
    }

    /**
     * @return the parent
     */
    public PointOctree getParent() {
        return parent;
    }

    /**
     * @return the points
     */
    public List<Vec3D> getPoints() {
        List<Vec3D> results = null;
        if (points != null) {
            results = new ArrayList<Vec3D>(points);
        } else if (numChildren > 0) {
            for (int i = 0; i < 8; i++) {
                if (children[i] != null) {
                    List<Vec3D> childPoints = children[i].getPoints();
                    if (childPoints != null) {
                        if (results == null) {
                            results = new ArrayList<Vec3D>();
                        }
                        results.addAll(childPoints);
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
    public List<Vec3D> getPointsWithinBox(AABB b) {
        ArrayList<Vec3D> results = null;
        if (this.intersectsBox(b)) {
            if (points != null) {
                for (Vec3D q : points) {
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
                        List<Vec3D> points = children[i].getPointsWithinBox(b);
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
    public List<Vec3D> getPointsWithinSphere(Sphere s) {
        ArrayList<Vec3D> results = null;
        if (this.intersectsSphere(s)) {
            if (points != null) {
                for (Vec3D q : points) {
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
                        List<Vec3D> points = children[i]
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
    public List<Vec3D> getPointsWithinSphere(Vec3D sphereOrigin,
            float clipRadius) {
        return getPointsWithinSphere(new Sphere(sphereOrigin, clipRadius));
    }

    /**
     * @return the size
     */
    public float getSize() {
        return size;
    }

    private void reduceBranch() {
        if (points != null && points.size() == 0) {
            points = null;
        }
        if (numChildren > 0) {
            for (int i = 0; i < 8; i++) {
                if (children[i] != null && children[i].points == null) {
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
    public boolean remove(ReadonlyVec3D p) {
        boolean found = false;
        PointOctree leaf = getLeafForPoint(p);
        if (leaf != null) {
            if (leaf.points.remove(p)) {
                found = true;
                if (isAutoReducing && leaf.points.size() == 0) {
                    leaf.reduceBranch();
                }
            }
        }
        return found;
    }

    public void removeAll(Collection<Vec3D> points) {
        for (ReadonlyVec3D p : points) {
            remove(p);
        }
    }

    /**
     * @param minNodeSize
     */
    public void setMinNodeSize(float minNodeSize) {
        this.minNodeSize = minNodeSize * 0.5f;
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
        return "<octree> offset: " + super.toString() + " size: " + size;
    }
}
