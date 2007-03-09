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

package toxi.util.datatypes;

import java.util.Enumeration;
import java.util.Iterator;

/**
 * Generic QuadTree implementation for optimizing 2D lookup & collision
 * detection scenarios. The simulation space is converted into a tree structure
 * with user objects assigned to the tree leaves. That way the number of
 * object-object checks can be reduced to local checks required..
 * 
 * @author toxi <info at toxi.co.uk>
 * @version 1.0
 */

public class QuadTree {

	// max. width of a leaf node
	// the actual width will only be equal to this value if
	// total tree width and leafWidth are both powers of 2
	protected float leafWidth;

	// 
	protected float totalWidth;

	protected QuadTreeNode tree;

	public QuadTree(float x, float y, float tw, float lw) {
		leafWidth = lw;
		totalWidth = tw;
		tree = addNode(x, y, totalWidth, 0, null);
	}

	public QuadTree(int tw, int lw) {
		this(0, 0, tw, lw);
	}

	/**
	 * internal, private, recursive tree creation do not call explicitly (used
	 * by constructor)
	 */

	private QuadTreeNode addNode(float x, float y, float w, int level,
			QuadTreeNode p) {
		int t;
		if (w > leafWidth)
			t = QuadTreeNode.NODE;
		else
			t = QuadTreeNode.LEAF;
		QuadTreeNode currnode = new QuadTreeNode(x, y, w, t);
		if (p != null) {
			try {
				p.addChild(currnode);
			} catch (QuadTreeException e) {
				// should never get here
				e.printStackTrace();
			}
		}
		if (t == QuadTreeNode.NODE) {
			// halve width & increase level for children
			w *= 0.5;
			level++;
			addNode(x, y, w, level, currnode);
			addNode(x + w, y, w, level, currnode);
			addNode(x, y + w, w, level, currnode);
			addNode(x + w, y + w, w, level, currnode);
		}
		return currnode;
	}
	
	/**
	 * internal, private, recursive leaf lookup
	 */

	private QuadTreeNode searchLeafAtPoint(float x, float y,
			QuadTreeNode parentNode) {
		System.out.println("checking node @ "+x+";"+y);
		QuadTreeNode child = parentNode.getChildForPoint(x, y);
		if (child.type == QuadTreeNode.NODE) {
			child = searchLeafAtPoint(x, y, child);
		}
		return child;
	}

	
	/**
	 * adds an object to the list of the point's associated leaf
	 * 
	 * @param obj
	 *            object reference
	 * @param x
	 *            the object's X coordinate
	 * @param y
	 *            the object's Y coordinate
	 * 
	 * @throws QuadTreeException
	 *             if point is outside the tree bounds
	 */

	public void addObjectAtPoint(Object obj, float x, float y)
			throws QuadTreeException {
		try {
			getLeafAtPoint(x, y).addObject(obj);
		} catch (QuadTreeException e) {
			throw new QuadTreeException(
					"Attempt to add object at point outside tree bounds", x, y);
		}
	}

	/**
	 * removes the object from the point's associated list of objects
	 */

	public void removeObjectAtPoint(Object obj, float x, float y)
			throws QuadTreeException {
		try {
			getLeafAtPoint(x, y).removeObject(obj);
		} catch (QuadTreeException e) {
			throw new QuadTreeException(
					"Attempt to delete object at point outside tree bounds", x,
					y);
		}
	}

	/**
	 * attempts to move an object to a new leaf in the tree x1/y1 specify old 2d
	 * position x2/y2 new 2d position function will determine leafs associated,
	 * then delete object reference in old list and add to new leaf
	 * 
	 * @param obj
	 *            object to be updated in tree
	 * @param x1
	 *            object's current x position
	 * @param y1
	 *            object's current y position
	 * @param x2
	 *            object's target x position
	 * @param y2
	 *            object's target y position
	 */

	public void moveObject(Object obj, float x1, float y1, float x2, float y2)
			throws QuadTreeException {
		QuadTreeNode leaf, leaf2;
		try {
			leaf = getLeafAtPoint(x1, y1);
			leaf2 = getLeafAtPoint(x2, y2);
			if (leaf != leaf2) {
				leaf.removeObject(obj);
				leaf2.addObject(obj);
			}
		} catch (QuadTreeException e) {
			throw new QuadTreeException(
					"Attempt to move object from/to point outside tree bounds");
		}
	}

	/**
	 * returns the object list of the given point's leaf node
	 * 
	 * @param x
	 *            X coordinate
	 * @param y
	 *            Y coordinate
	 * 
	 * @return list of all assigned objects for that point's cell as Iterator
	 * @throws QuadTreeException
	 *             if point is outside tree's bounds
	 */

	public Iterator getObjectsForLeafAt(float x, float y)
			throws QuadTreeException {
		try {
			return getLeafAtPoint(x, y).objects.iterator();
		} catch (QuadTreeException e) {
			throw new QuadTreeException(
					"Attempt to get object list for leaf at point outside tree bounds",
					x, y);
		}
	}

	/**
	 * return a handle to the leaf associated with the specified 2D coordinates
	 * returns nothing if point is invalid
	 */

	public QuadTreeNode getLeafAtPoint(float x, float y)
			throws QuadTreeException {
		if (tree.isPointInside(x, y)) {
			return searchLeafAtPoint(x, y, tree);
		}
		throw new QuadTreeException(
				"Attempt to get leaf at point outside tree bounds", x, y);
	}
}