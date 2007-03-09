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

import java.util.Vector;

class QuadTreeNode {

	float[] bounds;

	float midX, midY;

	int type;

	QuadTreeNode[] children;

	Vector objects;

	static final int NODE = 1;
	static final int LEAF = 2;

	int numChildren;

	protected QuadTreeNode(float x, float y, float w, int t) {
		type = t;
		bounds = new float[] { x, y, x + w - 0.1f, y + w - 0.1f };
		//System.out.println(x+";"+y+" -> "+bounds[2]+";"+bounds[3]);
		midX = x + w * 0.5f;
		midY = y + w * 0.5f;
		if (QuadTreeNode.NODE == type) {
			children = new QuadTreeNode[4];
		} else {
			objects = new Vector();
		}
	}

	/**
	 * adds a child node to the current node
	 * only used by quad tree constructor
	 * @throws QuadTreeException 
	 */

	protected void addChild(QuadTreeNode node) throws QuadTreeException {
		if (numChildren < 4)
			children[numChildren++] = node;
		else throw new QuadTreeException("attempted overloading of node: "+node);
	}

	/**
	 * add a new object to this node's list no duplicates are stored
	 */

	protected void addObject(Object obj) {
		if (!objects.contains(obj))
			objects.add(obj);
	}

	/**
	 * removes an object from leaf
	 */

	void removeObject(Object o) {
		objects.remove(o);
	}

	/**
	 * check if point is inside node's bounding rect
	 */

	boolean isPointInside(float x, float y) {
		return (x >= bounds[0] && x < bounds[2] && y >= bounds[1] && y < bounds[3]);
	}

	/**
	 * gets child node for specified point assumes that point is inside the node's
	 * bounds
	 */

	QuadTreeNode getChildForPoint(float x, float y) {
		int idx = (x >= midX ? 1 : 0);
		int idy = (y >= midY ? 2 : 0);
		return children[idx + idy];
	}
}