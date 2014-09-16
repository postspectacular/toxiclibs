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
import java.util.List;

/**
 * Implements a spatial subdivision tree to work efficiently with large numbers
 * of 2D particles. This quadtree can only be used for particle type objects and
 * does NOT support 2D mesh geometry as other forms of quadtree might do.
 * 
 * For further reference also see the QuadtreeDemo in the /examples folder.
 * 
 */
public class PointQuadtree extends Rect implements SpatialIndex<Vec2D> {

    public enum Type {
        EMPTY,
        BRANCH,
        LEAF;
    }

    private PointQuadtree parent;
    private PointQuadtree childNW, childNE, childSW, childSE;

    private Type type;

    private Vec2D value;
    private float mx, my;

    public PointQuadtree(float x, float y, float w, float h) {
        this(null, x, y, w, h);
    }

    public PointQuadtree(PointQuadtree parent, float x, float y, float w,
            float h) {
        super(x, y, w, h);
        this.parent = parent;
        this.type = Type.EMPTY;
        mx = x + w * 0.5f;
        my = y + h * 0.5f;
    }

    public PointQuadtree(Rect r) {
        this(null, r.x, r.y, r.width, r.height);
    }

    private void balance() {
        switch (type) {
            case EMPTY:
            case LEAF:
                if (parent != null) {
                    parent.balance();
                }
                break;

            case BRANCH:
                PointQuadtree leaf = null;
                if (childNW.type != Type.EMPTY) {
                    leaf = childNW;
                }
                if (childNE.type != Type.EMPTY) {
                    if (leaf != null) {
                        break;
                    }
                    leaf = childNE;
                }
                if (childSW.type != Type.EMPTY) {
                    if (leaf != null) {
                        break;
                    }
                    leaf = childSW;
                }
                if (childSE.type != Type.EMPTY) {
                    if (leaf != null) {
                        break;
                    }
                    leaf = childSE;
                }
                if (leaf == null) {
                    type = Type.EMPTY;
                    childNW = childNE = childSW = childSE = null;
                } else if (leaf.type == Type.BRANCH) {
                    break;
                } else {
                    type = Type.LEAF;
                    childNW = childNE = childSW = childSE = null;
                    value = leaf.value;
                }
                if (parent != null) {
                    parent.balance();
                }
        }
    }

    public void clear() {
        childNW = childNE = childSW = childSE = null;
        type = Type.EMPTY;
        value = null;
    }

    public PointQuadtree findNode(Vec2D p) {
        switch (type) {
            case EMPTY:
                return null;
            case LEAF:
                return value.x == x && value.y == y ? this : null;
            case BRANCH:
                return getQuadrantForPoint(p.x, p.y).findNode(p);
            default:
                throw new IllegalStateException("Invalid node type");
        }
    };

    private PointQuadtree getQuadrantForPoint(float x, float y) {
        if (x < mx) {
            return y < my ? childNW : childSW;
        } else {
            return y < my ? childNE : childSE;
        }
    }

    public boolean index(Vec2D p) {
        if (containsPoint(p)) {
            switch (type) {
                case EMPTY:
                    setPoint(p);
                    return true;

                case LEAF:
                    if (value.x == p.x && value.y == p.y) {
                        return false;
                    } else {
                        split();
                        return getQuadrantForPoint(p.x, p.y).index(p);
                    }

                case BRANCH:
                    return getQuadrantForPoint(p.x, p.y).index(p);
            }
        }
        return false;
    }

    public boolean isIndexed(Vec2D p) {
        return findNode(p) != null;
    }

    public List<Vec2D> itemsWithinRadius(Vec2D p, float radius,
            List<Vec2D> results) {
        if (intersectsCircle(p, radius)) {
            if (type == Type.LEAF) {
                if (value.distanceToSquared(p) < radius * radius) {
                    if (results == null) {
                        results = new ArrayList<Vec2D>();
                    }
                    results.add(value);
                }
            } else if (type == Type.BRANCH) {
                PointQuadtree[] children = new PointQuadtree[] {
                        childNW, childNE, childSW, childSE
                };
                for (int i = 0; i < 4; i++) {
                    if (children[i] != null) {
                        results = children[i].itemsWithinRadius(p, radius,
                                results);
                    }
                }
            }
        }
        return results;
    }

    public List<Vec2D> itemsWithinRect(Rect bounds, List<Vec2D> results) {
        if (bounds.intersectsRect(this)) {
            if (type == Type.LEAF) {
                if (bounds.containsPoint(value)) {
                    if (results == null) {
                        results = new ArrayList<Vec2D>();
                    }
                    results.add(value);
                }
            } else if (type == Type.BRANCH) {
                PointQuadtree[] children = new PointQuadtree[] {
                        childNW, childNE, childSW, childSE
                };
                for (int i = 0; i < 4; i++) {
                    if (children[i] != null) {
                        results = children[i].itemsWithinRect(bounds, results);
                    }
                }
            }
        }
        return results;
    }

    public void prewalk(QuadtreeVisitor visitor) {
        switch (type) {
            case LEAF:
                visitor.visitNode(this);
                break;

            case BRANCH:
                visitor.visitNode(this);
                childNW.prewalk(visitor);
                childNE.prewalk(visitor);
                childSW.prewalk(visitor);
                childSE.prewalk(visitor);
                break;
        }
    }

    public boolean reindex(Vec2D p, Vec2D q) {
        unindex(p);
        return index(q);
    }

    private void setPoint(Vec2D p) {
        if (type == Type.BRANCH) {
            throw new IllegalStateException("invalid node type: BRANCH");
        }
        type = Type.LEAF;
        value = p;
    }

    public int size() {
        return 0;
    }

    private void split() {
        Vec2D oldPoint = value;
        value = null;

        type = Type.BRANCH;

        float w2 = width * 0.5f;
        float h2 = height * 0.5f;

        childNW = new PointQuadtree(this, x, y, w2, h2);
        childNE = new PointQuadtree(this, x + w2, y, w2, h2);
        childSW = new PointQuadtree(this, x, y + h2, w2, h2);
        childSE = new PointQuadtree(this, x + w2, y + h2, w2, h2);

        index(oldPoint);
    }

    public boolean unindex(Vec2D p) {
        PointQuadtree node = findNode(p);
        if (node != null) {
            node.value = null;
            node.type = Type.EMPTY;
            node.balance();
            return true;
        } else {
            return false;
        }
    }
}
