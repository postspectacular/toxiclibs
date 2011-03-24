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

package toxi.geom.mesh;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import toxi.geom.Vec3D;
import toxi.math.MathUtils;

/**
 * Abstract parent class for selecting mesh vertices and manipulating resulting
 * selections using set theory operations. Implementations of this class should
 * aim to work with all mesh types (e.g. {@link TriangleMesh},
 * {@link WETriangleMesh}).
 */
public abstract class VertexSelector {

    protected Mesh3D mesh;
    protected Set<Vertex> selection;

    /**
     * Creates a new selector assigned to the given mesh
     * 
     * @param mesh
     */
    public VertexSelector(Mesh3D mesh) {
        this.mesh = mesh;
        this.selection = new HashSet<Vertex>();
    }

    /**
     * Adds all vertices selected by the given selector to the current
     * selection. The other selector needs to be assigned to the same mesh
     * instance.
     * 
     * @param sel2
     *            other selector
     * @return itself
     */
    public VertexSelector addSelection(VertexSelector sel2) {
        checkMeshIdentity(sel2.getMesh());
        selection.addAll(sel2.getSelection());
        return this;
    }

    /**
     * Utility function to check if the given mesh is the same instance as ours.
     * 
     * @param mesh2
     */
    protected void checkMeshIdentity(Mesh3D mesh2) {
        if (mesh2 != mesh) {
            throw new IllegalArgumentException(
                    "The given selector is not using the same mesh instance");
        }
    }

    /**
     * Clears the current selection.
     * 
     * @return itself
     */
    public VertexSelector clearSelection() {
        selection.clear();
        return this;
    }

    /**
     * Returns the associated mesh for this selector.
     * 
     * @return itself
     */
    public Mesh3D getMesh() {
        return mesh;
    }

    /**
     * Returns the actual collection of selected vertices
     * 
     * @return vertex collection
     */
    public Collection<Vertex> getSelection() {
        return selection;
    }

    /**
     * Creates a new selection of all vertices NOT currently selected.
     * 
     * @return itself
     */
    public VertexSelector invertSelection() {
        final int size = MathUtils.max(0,
                mesh.getNumVertices() - selection.size());
        HashSet<Vertex> newSel = new HashSet<Vertex>(size);
        for (Vertex v : mesh.getVertices()) {
            if (!selection.contains(v)) {
                newSel.add(v);
            }
        }
        selection = newSel;
        return this;
    }

    /**
     * Selects vertices identical or closest to the ones given in the list of
     * points.
     * 
     * @param points
     * @return itself
     */
    public VertexSelector selectSimilar(Collection<? extends Vec3D> points) {
        for (Vec3D v : points) {
            selection.add(mesh.getClosestVertexToPoint(v));
        }
        return this;
    }

    /**
     * Selects vertices using an implementation specific method. This is the
     * only method which needs to be implemented by any selector subclass.
     * 
     * @return itself
     */
    public abstract VertexSelector selectVertices();

    /**
     * Assigns a new mesh instance to this selector and clears the current
     * selection.
     * 
     * @param mesh
     *            the mesh to set
     */
    public void setMesh(Mesh3D mesh) {
        this.mesh = mesh;
        clearSelection();
    }

    /**
     * Returns the current number of selected vertices.
     * 
     * @return number of vertices
     */
    public int size() {
        return selection.size();
    }

    /**
     * Removes all vertices selected by the given selector from the current
     * selection. The other selector needs to be assigned to the same mesh
     * instance.
     * 
     * @param sel2
     *            other selector
     * @return itself
     */
    public VertexSelector subtractSelection(VertexSelector sel2) {
        checkMeshIdentity(sel2.getMesh());
        selection.removeAll(sel2.getSelection());
        return this;
    }
}
