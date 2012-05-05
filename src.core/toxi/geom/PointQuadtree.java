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
 * of 2D particles. This quadtree can only be used for particle type objects and
 * does NOT support 2D mesh geometry as other forms of quadtree might do.
 * 
 * For further reference also see the QuadtreeDemo in the /examples folder.
 * 
 */
public class PointQuadtree extends Rect implements Shape2D, SpatialIndex<Vec2D> {

    /**
     * alternative tree recursion limit, number of world units when cells are
     * not subdivided any further
     */
    protected float minNodeSize = 4;

    protected PointQuadtree parent;
    protected PointQuadtree[] children;

    protected ArrayList<Vec2D> points;

    protected final int depth;
    protected int numChildren;
    protected float size, halfSize;
    protected Vec2D offset;
    protected boolean isAutoReducing = false;

    /**
     * Constructs a new PointQuadtree node within the Rect: {o.x, o.y} ...
     * {o.x+size, o.y+size}
     * 
     * @param p
     *            parent node
     * @param o
     *            tree origin
     * @param halfSize
     *            half length of the tree volume along a single axis
     */
    private PointQuadtree(PointQuadtree p, Vec2D o, float size) {
        super(o.x, o.y, size, size);
        this.parent = p;
        this.size = size;
        this.halfSize = size / 2;
        this.offset = o;
        this.numChildren = 0;
        if (parent != null) {
            depth = parent.depth + 1;
            minNodeSize = parent.minNodeSize;
        } else {
            depth = 0;
        }
    }

    /**
     * Constructs a new PointQuadtree node within the Rect: {o.x, o.y} ...
     * {o.x+size, o.y+size}
     * 
     * @param o
     *            tree origin
     * @param size
     *            size of the tree along a single axis
     */
    public PointQuadtree(Vec2D o, float size) {
        this(null, o, size);
    }

    /**
     * Adds all points of the collection to the quadtree. IMPORTANT: Points need
     * be of type Vec2D or have subclassed it.
     * 
     * @param points
     *            point collection
     * @return true, if all points have been added successfully.
     */
    public boolean addAll(Collection<Vec2D> points) {
        boolean addedAll = true;
        for (Vec2D p : points) {
            addedAll &= index(p);
        }
        return addedAll;
    }

    /**
     * Applies the given {@link OctreeVisitor} implementation to this node and
     * all of its children.
     */
    public void applyVisitor(QuadtreeVisitor visitor) {
        visitor.visitNode(this);
        if (numChildren > 0) {
            for (PointQuadtree c : children) {
                if (c != null) {
                    c.applyVisitor(visitor);
                }
            }
        }
    }

    public void clear() {
        numChildren = 0;
        children = null;
        points = null;
    }

    public boolean containsPoint(ReadonlyVec2D p) {
        return p.isInRectangle(this);
    }

    /**
     * @return a copy of the child nodes array
     */
    public PointQuadtree[] getChildren() {
        if (children != null) {
            PointQuadtree[] clones = new PointQuadtree[4];
            System.arraycopy(children, 0, clones, 0, 4);
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
    public PointQuadtree getLeafForPoint(ReadonlyVec2D p) {
        // if not a leaf node...
        if (p.isInRectangle(this)) {
            if (numChildren > 0) {
                int quadrant = getQuadrantID(p.sub(offset));
                if (children[quadrant] != null) {
                    return children[quadrant].getLeafForPoint(p);
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
     * @return the offset
     */
    public ReadonlyVec2D getOffset() {
        return offset;
    }

    /**
     * @return the parent
     */
    public PointQuadtree getParent() {
        return parent;
    }

    /**
     * @return the points
     */
    public List<Vec2D> getPoints() {
        List<Vec2D> results = null;
        if (points != null) {
            results = new ArrayList<Vec2D>(points);
        } else if (numChildren > 0) {
            for (int i = 0; i < 8; i++) {
                if (children[i] != null) {
                    List<Vec2D> childPoints = children[i].getPoints();
                    if (childPoints != null) {
                        if (results == null) {
                            results = new ArrayList<Vec2D>();
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
     * @param r
     *            clipping rect
     * @return all points with the rect
     */
    public List<Vec2D> getPointsWithinRect(Rect r) {
        ArrayList<Vec2D> results = null;
        if (this.intersectsRect(r)) {
            if (points != null) {
                for (Vec2D q : points) {
                    if (q.isInRectangle(r)) {
                        if (results == null) {
                            results = new ArrayList<Vec2D>();
                        }
                        results.add(q);
                    }
                }
            } else if (numChildren > 0) {
                for (int i = 0; i < children.length; i++) {
                    if (children[i] != null) {
                        List<Vec2D> points = children[i].getPointsWithinRect(r);
                        if (points != null) {
                            if (results == null) {
                                results = new ArrayList<Vec2D>();
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
     * Computes the local child quadrant/rect index for the given point
     * 
     * @param plocal
     *            point in the node-local coordinate system
     * @return quadrant index
     */
    protected final int getQuadrantID(Vec2D plocal) {
        return (plocal.x > halfSize ? 1 : 0) + (plocal.y > halfSize ? 2 : 0);
    }

    /**
     * @return the size
     */
    public float getSize() {
        return size;
    }

    /**
     * Adds a new point/particle to the tree structure. All points are stored
     * within leaf nodes only. The tree implementation is using lazy
     * instantiation for all intermediate tree levels.
     * 
     * @param p
     * @return true, if point has been added successfully
     */
    public boolean index(Vec2D p) {
        // check if point is inside
        if (containsPoint(p)) {
            // only add points to leaves for now
            if (halfSize <= minNodeSize) {
                if (points == null) {
                    points = new ArrayList<Vec2D>();
                }
                points.add(p);
                return true;
            } else {
                Vec2D plocal = p.sub(offset);
                if (children == null) {
                    children = new PointQuadtree[4];
                }
                int quadrant = getQuadrantID(plocal);
                if (children[quadrant] == null) {
                    Vec2D off = offset.add(new Vec2D(
                            (quadrant & 1) != 0 ? halfSize : 0,
                            (quadrant & 2) != 0 ? halfSize : 0));
                    children[quadrant] = new PointQuadtree(this, off, halfSize);
                    numChildren++;
                }
                return children[quadrant].index(p);
            }
        }
        return false;
    }

    public boolean isIndexed(Vec2D item) {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * Selects all stored points within the given circle
     * 
     * @param c
     *            circle
     * @return selected points
     */
    public List<Vec2D> itemsWithinRadius(Vec2D p, float radius) {
        ArrayList<Vec2D> results = null;
        if (this.intersectsCircle(p, radius)) {
            if (points != null) {
                float rsq = radius * radius;
                for (Vec2D q : points) {
                    if (p.distanceToSquared(q) < rsq) {
                        if (results == null) {
                            results = new ArrayList<Vec2D>();
                        }
                        results.add(q);
                    }
                }
            } else if (numChildren > 0) {
                for (int i = 0; i < 4; i++) {
                    if (children[i] != null) {
                        List<Vec2D> points = children[i].itemsWithinRadius(p,
                                radius);
                        if (points != null) {
                            if (results == null) {
                                results = new ArrayList<Vec2D>();
                            }
                            results.addAll(points);
                        }
                    }
                }
            }
        }
        return results;
    }

    private void reduceBranch() {
        if (points != null && points.size() == 0) {
            points = null;
        }
        if (numChildren > 0) {
            for (int i = 0; i < children.length; i++) {
                if (children[i] != null && children[i].points == null) {
                    children[i] = null;
                }
            }
        }
        if (parent != null) {
            parent.reduceBranch();
        }
    }

    public boolean reindex(Vec2D p, Vec2D q) {
        unindex(p);
        return index(q);
        // PointQuadtree leaf1 = getLeafForPoint(p);
        // PointQuadtree leaf2 = getLeafForPoint(q);
        // if (leaf2 != null) {
        // if (leaf2 != leaf1) {
        // if (leaf1 != null && leaf1.points.remove(p)) {
        // if (isAutoReducing && leaf1.points.size() == 0) {
        // leaf1.reduceBranch();
        // }
        // }
        // leaf2.points.add(q);
        // } else {
        // leaf1.points.remove(p);
        // leaf1.points.add(q);
        // }
        // }
        // return index(q);
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

    public int size() {
        // TODO Auto-generated method stub
        return 0;
    }

    public String toString() {
        return "<quadtree> offset: " + super.toString() + " size: " + size;
    }

    /**
     * Removes a point from the tree and (optionally) tries to release memory by
     * reducing now empty sub-branches.
     * 
     * @param p
     *            point to delete
     * @return true, if the point was found & removed
     */
    public boolean unindex(Vec2D p) {
        boolean found = false;
        PointQuadtree leaf = getLeafForPoint(p);
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

    public boolean unindexAll(Collection<Vec2D> points) {
        boolean removedAll = true;
        for (Vec2D p : points) {
            removedAll &= unindex(p);
        }
        return removedAll;
    }

}
